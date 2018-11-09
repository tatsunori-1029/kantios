package app.saiou.kantios.auth;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import app.saiou.kantios.BeanFactory;
import app.saiou.kantios.entity.TbUser;
import app.saiou.kantios.model.User;

public class MockAuthenticator implements Authenticator {

	private static final String MOCK_AUTH_PROPERTIES_PATH = "properties/mock_auth.properties";

	public static Properties getMockAuthProperties() {
		Path path = Paths.get(MOCK_AUTH_PROPERTIES_PATH);
		if (Files.isRegularFile(path) == false) {
			return new Properties();
		}

		try {
			Properties properties = new Properties();
			properties.load(Files.newBufferedReader(path, StandardCharsets.UTF_8));
			return properties;
		} catch (IOException e) {
			// TODO ログ出力
			return new Properties();
		}
	}

	@Override
	public UserInfo authenticate(String userId, String password) {

		Properties properties = getMockAuthProperties();

		// 例外を発生させたい場合
		String reason = properties.getProperty(new StringBuilder(userId).append(".error").toString());
		if (reason != null) {
			throw new AuthException(reason);
		}

		String userName = properties.getProperty(new StringBuilder(userId).append(".userName").toString(), userId);

		BeanFactory factory = BeanFactory.of("app/saiou/kantios/beanDef.xml");
		User _userModel = factory.getBean("userModel");
		TbUser record = _userModel.handle(userId, userName);
		return new StandardUserInfo(record.getUserId(), record.getDisplayName(), password);
	}
}
