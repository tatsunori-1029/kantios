package app.saiou.kantios;

public interface BeanFactory {
	
	<T> T getBean(String beanId);
	
	<T> T getBean(String beanId, Class<T> clazz);
	
	public static BeanFactory of(String... configLocations) {
		return DefaultBeanFactory.getInstance(configLocations);
	}
}
