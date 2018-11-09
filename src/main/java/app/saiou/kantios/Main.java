package app.saiou.kantios;

import static spark.Spark.after;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.staticFiles;
import static spark.Spark.stop;
import static spark.Spark.threadPool;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.derby.tools.ij;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import app.saiou.kantios.auth.Authenticator.UserInfo;
import spark.ModelAndView;
import spark.template.thymeleaf.ThymeleafTemplateEngine;


public class Main {
	
	private static Logger _logger = LoggerFactory.getLogger(Main.class);
	
	public static void main(String[] args) {
		
		setupEmbeddedServer();
		setupDatabase();

		get("/stop", (req, res) -> {
			stop();
			return "";
		});
		
		Authentication.define();
		MemoManager.define();
		
		get("/", (req, res) -> {
			UserInfo userInfo = Authentication.getUserInfo(req);
			String userName = userInfo.getUserName();

			Map<String, Object> model = new HashMap<>();
			model.put("userName", userName);

			return new ModelAndView(model, "index");
		}, new ThymeleafTemplateEngine(Main.getTemplateResolver()));
		
		after((req, res) -> {
			// XXX ajaxの時は不要では？
			// redirectの時も不要では？
			res.type("text/html;charset=UTF-8");
		});
	}
	
	private static ITemplateResolver _templateResolver;

	public static ITemplateResolver getTemplateResolver() {
		if (_templateResolver != null) {
			return _templateResolver;
		}

		ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
		resolver.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
		resolver.setPrefix("template/");
		resolver.setSuffix(".html");
		resolver.setCacheable(EnvProperties.ENABLE_TEMPLATE_CACHE.value());

		_templateResolver = resolver;
		return _templateResolver;
	}
	
	private static void setupEmbeddedServer() {
		port(8090);
		staticFiles.location("/content");
		threadPool(8, 2, 30000);
	}
	
	private static final Path DATABASE_PATH = Paths.get("db");
	
	private static void setupDatabase() {
		createDatabase();
		createTables();
	}

	private static void createDatabase() {
		if (Files.isRegularFile(DATABASE_PATH)) {
			throw new IllegalStateException(
					"a file with same name as db directory name exist[" + DATABASE_PATH.toAbsolutePath().toString() + "]");
		}
		
		if (Files.isDirectory(DATABASE_PATH)) {
			_logger.debug("database directory already exist.");
			testConnectionAndShutdown();
		} else {
			try (Connection con = getDerbyConnection(true)) {
				_logger.debug("create database successfully [{}]", con.toString());
			} catch (SQLException e) {
				throw new IllegalStateException("failed to create database.", e);
			}
		}
	}
	
	private static void testConnectionAndShutdown() {
		try (Connection con = getDerbyConnection(false)) {
			_logger.debug("connected successfully [{}]", con.toString());
		} catch (SQLException e) {
			throw new IllegalStateException("exception occurred while connecting database.", e);
		}
		
		// shutdown
		try {
			DriverManager.getConnection("jdbc:derby:;shutdown=true;deregister=false");
		} catch (SQLException e) {
			if ("XJ015".equals(e.getSQLState())) {
				_logger.debug("shutdown derby successfully.");
			} else {
				throw new IllegalStateException("failed to shutdown derby.", e);
			}
		}
	}
	
	private static void createTables() {
		boolean created = false;
		try (Connection con = getDerbyConnection(false)) {
			DatabaseMetaData metaData = con.getMetaData();
			try (ResultSet rs = metaData.getSchemas()) {
				while (rs.next()) {
					String schemaName = rs.getString("TABLE_SCHEM");
					_logger.debug("schema name [{}]", schemaName);
					if (schemaName.equals("DBUSER")) {
						created = true;
						_logger.debug("memo tables already created.");
						break;
					}
				}
			}			
			
			if (created == false) {
				try (InputStream sql = Files.newInputStream(Paths.get("sql").resolve("createTables.sql"));
						ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
					int ret = ij.runScript(con, sql, "MS932", baos, "UTF-8");
					_logger.debug("sql script return code [{}]", ret);
					_logger.debug("sql script stdout [{}]", new String(baos.toByteArray(), "UTF-8"));
				}
			}
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	private static Connection getDerbyConnection(boolean create) throws SQLException {
		Properties dbProperties = new Properties();
		dbProperties.setProperty("create", Boolean.valueOf(create).toString());
		dbProperties.setProperty("user", "dbUser");
		dbProperties.setProperty("password", "dbPassword");
		return DriverManager.getConnection("jdbc:derby:" + DATABASE_PATH.toString(), dbProperties);
	}
}
