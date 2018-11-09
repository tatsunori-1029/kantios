package app.saiou.kantios.auth;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.StringJoiner;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import app.saiou.kantios.BeanFactory;
import app.saiou.kantios.EnvProperties;
import app.saiou.kantios.entity.TbUser;
import app.saiou.kantios.model.User;

public class AdAuthenticator implements Authenticator {

	private static String getAdUrl() {
		String adUrl = EnvProperties.AD_URL.value();
		if (adUrl != null) {
			return adUrl;
		}

		String logonServerName = EnvProperties.LOGON_SERVER.value();
		if (logonServerName != null) {
			return new StringBuilder("ldap://").append(logonServerName.substring(2)).toString();
		}

		throw new IllegalStateException("ActiveDirectoryのURLが解決できませんでした。");
	}

	private static String getAdDomain() {
		String adDomain = EnvProperties.AD_DOMAIN.value();
		if (adDomain != null) {
			return adDomain;
		}

		String userDnsDomain = EnvProperties.USER_DNS_DOMAIN.value();
		if (userDnsDomain != null) {
			return new StringBuilder("@").append(userDnsDomain).toString();
		}

		throw new IllegalStateException("ActiveDirectoryのドメインが解決できませんでした。");
	}

	// "DC=NLW,DC=LOCAL"のような値を返す
	private static String getAdSearchContext() {
		String adSearchContext = EnvProperties.AD_SEARCH_CONTEXT.value();
		if (adSearchContext != null) {
			return adSearchContext;
		}

		String userDnsDomain = EnvProperties.USER_DNS_DOMAIN.value();
		if (userDnsDomain != null) {
			StringJoiner joiner = new StringJoiner(",DC=", "DC=", "");
			String[] values = userDnsDomain.split("\\.");
			Arrays.stream(values).forEach(val -> joiner.add(val));
			return joiner.toString();
		}

		throw new IllegalStateException("ActiveDirectoryのルート・コンテキストが解決できませんでした。");
	}

	private static Hashtable<?, ?> getAdEnvironment(String userId, String password) {
		Hashtable<String, String> env = new Hashtable<>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.PROVIDER_URL, getAdUrl());
		env.put(Context.SECURITY_PRINCIPAL, new StringBuilder(userId).append(getAdDomain()).toString());
		env.put(Context.SECURITY_CREDENTIALS, password);
		return env;
	}

	@Override
	public UserInfo authenticate(String userId, String password) {

		DirContext ctx = null;

		try {
			ctx = new InitialDirContext(getAdEnvironment(userId, password));

			SearchControls searchControls = new SearchControls();
			searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

			String filter = new StringBuilder("sAMAccountName=").append(userId).toString();

			NamingEnumeration<SearchResult> searchResults = ctx.search(getAdSearchContext(), filter, searchControls);

			SearchResult searchResult;
			String displayName = null;
			while ((searchResult = searchResults.nextElement()) != null) {
				Object displayNameAttribute = searchResult.getAttributes().get("displayName");
				if (displayNameAttribute != null) {
					displayName = ((Attribute) displayNameAttribute).get().toString();
					break;
				}
			}
			ctx.close();

			if (displayName == null) {
				throw new AuthException("ユーザー名が取得できません。");
			}

			BeanFactory factory = BeanFactory.of("app/saiou/kantios/beanDef.xml");
			User _userModel = factory.getBean("userModel");
			TbUser record = _userModel.handle(userId, displayName);
			return new StandardUserInfo(record.getUserId(), record.getDisplayName(), password);

		} catch (NamingException ne) {
			throw new AuthException("認証に失敗しました。", ne);
		} finally {
			if (ctx != null) {
				try {
					ctx.close();
				} catch (Exception exp) {
					// TODO ログ出力
					ctx = null;
				}
			}
		}
	}
}
