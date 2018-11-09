package app.saiou.kantios;

import static spark.Spark.*;
import static spark.Spark.post;

import java.util.HashMap;
import java.util.Map;

import app.saiou.kantios.auth.AdAuthenticator;
import app.saiou.kantios.auth.Authenticator;
import app.saiou.kantios.auth.EmbedAuthenticator;
import app.saiou.kantios.auth.MockAuthenticator;
import app.saiou.kantios.auth.Authenticator.AuthException;
import app.saiou.kantios.auth.Authenticator.UserInfo;
import spark.ModelAndView;
import spark.Request;
import spark.Session;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

public class Authentication {
	
	public static void define() {

		// 認証チェック
		before((req, res) -> {
			if (req.pathInfo().endsWith("/auth") || req.pathInfo().endsWith("/login") || req.pathInfo().endsWith("/stop")) {
				// 認証リクエストの場合はスルー
				return;
			}
			
			Session session = req.session();
			UserInfo userInfo = session.attribute(Authenticator.SESSION_KEY);
			if (userInfo == null) {
				throw new AuthException("認証されていません。");
			}
		});
		
		get("/login", (req, res) -> {
			Map<String, Object> model = new HashMap<>();
			model.put("message", "ログインしてください。");
			Exception exp = req.session().attribute("error");
			if (exp != null) {
				model.put("error", exp.getMessage());
			}
			return new ModelAndView(model, "login");
		}, new ThymeleafTemplateEngine(Main.getTemplateResolver()));
		
		post("/auth", (req, res) -> {
			
			// XXX 既に認証済みの場合はどうするか？
			
			String userId = req.queryParams("userId");
			String password = req.queryParams("password");
			
			Authenticator authenticator = getAuthenticator();
			UserInfo userInfo = authenticator.authenticate(userId, password);
			
			req.session().attribute(Authenticator.SESSION_KEY, userInfo);
			
			res.redirect("/");

			return null;
		});
		
		get("/logout", (req, res) -> {
			req.session().invalidate();

			Map<String, Object> model = new HashMap<>();
			model.put("message", "ログアウトしました。");
			return new ModelAndView(model, "login");
		}, new ThymeleafTemplateEngine(Main.getTemplateResolver()));
		
		exception(AuthException.class, (exp, req, res) -> {
			// エラーメッセージの連携
			req.session().attribute("error", exp);
			
			// ログイン画面へリダイレクト
			res.redirect("/login");
			return;
		});
	}
	
	private static Authenticator _authenticator = null;
	
	static Authenticator getAuthenticator() {
		if (_authenticator != null) {
			return _authenticator;
		}

		String authType = EnvProperties.AUTH_TYPE.value();
		if (authType == null) {
			_authenticator = new EmbedAuthenticator();
			return _authenticator;
		}
		
		if (authType.equalsIgnoreCase("ActiveDirectory")) {
			_authenticator = new AdAuthenticator();
			return _authenticator;
		}
		
		if (authType.equalsIgnoreCase("mock")) {
			_authenticator = new MockAuthenticator();
			return _authenticator;
		}
		
		_authenticator = new EmbedAuthenticator();
		return _authenticator;
	}
	
	public static UserInfo getUserInfo(Request req) {
		return req.session().attribute(Authenticator.SESSION_KEY);
	}
}
