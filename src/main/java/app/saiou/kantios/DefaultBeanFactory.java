package app.saiou.kantios;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DefaultBeanFactory implements BeanFactory {

	private static Map<String, DefaultBeanFactory> _cache = new ConcurrentHashMap<>();

	private ApplicationContext _context;
	
	static DefaultBeanFactory getInstance(String... configLocations) {
		String key = String.join(",", configLocations);
		DefaultBeanFactory cached = _cache.get(key);
		if (cached != null) {
			return cached;
		}
		
		DefaultBeanFactory factory = new DefaultBeanFactory(configLocations);
		_cache.put(key, factory);
		return factory;
	}

	DefaultBeanFactory(String... configLocations) {
		_context = new ClassPathXmlApplicationContext(configLocations);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getBean(String beanId) {
		return (T) _context.getBean(beanId);
	}

	@Override
	public <T> T getBean(String beanId, Class<T> classOfT) {
		return _context.getBean(beanId, classOfT);
	}

}
