package app.saiou.kantios;

import app.saiou.kantios.util.StringParser;

/**
 * 環境変数設定値取得（取得用キー名などが分散するのを抑止）
 */
public final class EnvProperties <V> {

	private String _key;
	private StringParser<V> _parser;
	private V _defaultValue;

	private EnvProperties(String key, StringParser<V> parser, V defaultValue) {
		_key = key;
		_parser = parser;
		_defaultValue = defaultValue;
	}
	
	public V value() {
		String row = System.getenv(_key);
		if (row == null) {
			return _defaultValue;
		}
		return _parser.parse(row);
	};
	
	private static EnvProperties<String> define(String key) {
		return new EnvProperties<>(key, StringParser.STRING, null);
	}

/* 今は不要
	private static EnvProperties<String> define(String key, String defaultValue) {
		return new EnvProperties<>(key, StringParser.STRING, defaultValue);
	}
*/
	private static <V> EnvProperties<V> define(String key, StringParser<V> parser, V defaultValue) {
		return new EnvProperties<V>(key, parser, defaultValue);
	}

	// ユーザーが設定する環境変数
	
	public static final EnvProperties<String> AUTH_TYPE = EnvProperties.define("authType");
	
	public static final EnvProperties<String> AD_URL= EnvProperties.define("adUrl");
	
	public static final EnvProperties<String> AD_DOMAIN = EnvProperties.define("adDomain");
	
	public static final EnvProperties<String> AD_SEARCH_CONTEXT = EnvProperties.define("adSearchContext");
	
	public static final EnvProperties<Boolean> ENABLE_TEMPLATE_CACHE = EnvProperties.define("enableTemplateCache", StringParser.BOOLEAN, false);

	// OSが設定する環境変数
	
	public static final EnvProperties<String> LOGON_SERVER = EnvProperties.define("LOGONSERVER");
	
	public static final EnvProperties<String> USER_DNS_DOMAIN = EnvProperties.define("USERDNSDOMAIN");
}
