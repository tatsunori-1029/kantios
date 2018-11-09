package app.saiou.kantios.auth;

public class EmbedAuthenticator implements Authenticator {
	
	private Authenticator _authenticator = new MockAuthenticator();

	// 要実装
	
	@Override
	public UserInfo authenticate(String userId, String password) {
		return _authenticator.authenticate(userId, password);
	}

}
