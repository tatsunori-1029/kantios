package app.saiou.kantios.util;

@FunctionalInterface
public interface StringParser<T> {
	
	public T parse(String value);
	
	public static final StringParser<String> STRING = value -> value;

	public static final StringParser<Boolean> BOOLEAN = value -> {
		if (value != null && value.equalsIgnoreCase("true")) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	};
}
