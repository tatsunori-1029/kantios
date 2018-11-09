package app.saiou.kantios.auth;

public interface Authenticator {
	
	public static final String SESSION_KEY = UserInfo.class + ".sessionKey";
	
	/**
	 * 認証処理
	 * 
	 * @param userId ユーザーID
	 * @param password パスワード
	 * @return 認証成功時はUserInfoを返す。失敗時はnullまたはAuthExceptionをスロー
	 */
	UserInfo authenticate(String userId, String password);
	
	public interface UserInfo {
		String getUserId();
		String getUserName();
		String getPassword();
	}
	
	public static class StandardUserInfo implements UserInfo {
		
		private String _userId;
		private String _userName;
		private String _password;
		
		StandardUserInfo(String userId, String userName, String password) {
			_userId = userId;
			_userName = userName;
			_password = password;
		}

		@Override
		public String getUserId() {
			return _userId;
		}

		@Override
		public String getUserName() {
			return _userName;
		}

		@Override
		public String getPassword() {
			return _password;
		}
	}
	
	public class AuthException extends RuntimeException {
		private static final long serialVersionUID = 5585637510862458422L;

		public AuthException(String reason) {
			super(reason);
		}

		public AuthException(String reason, Throwable cause) {
			super(reason, cause);
		}
	}
}