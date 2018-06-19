package com.myspring.core;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.myspring.aop.advice.MethodInterceptor;
import com.myspring.aop.factory.ProxyFactoryBean;
import com.myspring.bean.Student;
import com.myspring.config.Bean;
import com.myspring.config.Bean.ScopeType;
import com.myspring.config.ConfigurationManager;
import com.myspring.config.Interceptor;
import com.myspring.config.Property;
import com.myspring.util.BeanUtil;

public class ClassPathXmlApplicationContext implements ApplicationContext {
	private Map<String, Bean> beans; // beanName->Bean的bean池
	private Map<Bean, Object> singletonObjects = new HashMap<Bean, Object>(); // 单例模式缓存
	private Map<Bean, Object> earlySingletonObjects = new HashMap<Bean, Object>();// 提早曝光的单例模式缓存
	private Map<Bean, Object> earlyPrototypeObjects = new HashMap<Bean, Object>();// 提早曝光的原型模式缓存
	private ConfigurationManager configManager; // 读取配置文件

	/*
	 * 初始化Ioc容器,将配置信息读入beans,将单例模式类纳入容器管理
	 * 
	 * @param path
	 */
	public ClassPathXmlApplicationContext(String path) {
		configManager = new ConfigurationManager(path);
		beans = configManager.getBeanconfig();

		if (beans != null) {
			// 将单例模式类放入提早曝光的单例模式缓存
			for (String name : beans.keySet()) {
				Bean bean = beans.get(name);
				if (bean.getScope() == ScopeType.singleton) {
					try {
						Class clazz = Class.forName(bean.getClassName());
						Object obj = clazz.newInstance();
						earlySingletonObjects.put(bean, obj);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
			}

			// 加载单例模式类，并放入单例模式缓存
			for (String name : beans.keySet()) {
				Bean bean = beans.get(name);
				if (bean.getScope() == ScopeType.singleton
						&& bean.getClassName().equals("com.myspring.aop.ProxyFactoryBean") == false) {
					singletonObjects.put(bean, createBean(bean));
					earlySingletonObjects.remove(bean);
				}
			}
		}
	}

	/*
	 * 获取单例模式类
	 */
	private Object getSingleton(Bean bean) {
		Object obj = earlySingletonObjects.get(bean);
		if (obj != null)
			return obj;
		obj = singletonObjects.get(bean);
		return obj;
	}

	/*
	 * 获取原型模式类
	 */
	private Object getPrototype(Bean bean) {
		Object obj = earlyPrototypeObjects.get(bean);
		if (obj != null)
			return obj;
		return this.createBean(bean);
	}

	/*
	 * 获得依赖属性
	 */
	private Object getRef(String beanName) {
		Bean bean = beans.get(beanName);
		if (bean.getScope() == ScopeType.singleton)
			return this.getSingleton(bean);
		else
			return this.getPrototype(bean);
	}

	/*
	 * 创建实例对象
	 */
	private Object createBean(Bean bean) {
		Object beanObj = null;
		try {
			// 反射得到要创建的对象实例
			Class clazz = Class.forName(bean.getClassName());
			if (bean.getScope() == ScopeType.singleton) {
				// 如果该对象是单例模式，则从提早曝光的单例模式缓存中提取
				beanObj = earlySingletonObjects.get(bean);
			} else {
				// 如果该对象是原型模式，则直接创建实例，并将该对象放入提早曝光的原型模式缓存中提取
				beanObj = clazz.newInstance();
				earlyPrototypeObjects.put(bean, beanObj);
			}
			// 为对象的属性注入值
			Method[] methods = clazz.getMethods();
			for (Property pro : bean.getProperties()) {
				if (beanObj instanceof ProxyFactoryBean && pro.getName().equals("interceptorName")) {
					// 拦截器
					Interceptor ref = new Interceptor();
					MethodInterceptor interceptor = (MethodInterceptor) this.getRef(pro.getRef());
					String check = pro.getValue();
					ref.setInterceptor(interceptor);
					ref.setCheck(check);
					ref.setName(pro.getRef());
					for (Method method : methods) {
						if (method.getName().equalsIgnoreCase("set" + pro.getName())) {
							BeanUtil.populate(method, beanObj, ref);
							break;
						}
					}
				} else if (pro.getValue() != null) {
					// 非依赖关系属性
					for (Method method : methods) {
						if (method.getName().equalsIgnoreCase("set" + pro.getName())) {
							BeanUtil.populate(method, beanObj, pro.getValue());
							break;
						}
					}
				} else {
					// 依赖关系属性
					Object ref = this.getRef(pro.getRef());
					for (Method method : methods) {
						if (method.getName().equalsIgnoreCase("set" + pro.getName())) {
							BeanUtil.populate(method, beanObj, ref);
							break;
						}
					}
				}
			}
			// 如果该对象是代理对象，则已将创建对象的所需属性注入，只需创建所需代理对象实例
			if (beanObj instanceof ProxyFactoryBean) {
				// 获取代理对象工厂
				ProxyFactoryBean proxyFactory = (ProxyFactoryBean) beanObj;
				beanObj = proxyFactory.createProxy();
				earlySingletonObjects.remove(bean);
				singletonObjects.put(bean, beanObj);
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return beanObj;
	}

	/*
	 * 获取对象实例
	 * 
	 * @see com.myspring.core.ApplicationContext#getBean(java.lang.String)
	 * 
	 * @Override
	 */
	public Object getBean(String beanName) {
		Bean bean = beans.get(beanName);
		if (bean.getScope() == ScopeType.singleton) {
			if (bean.getClassName().equals("com.myspring.aop.ProxyFactoryBean")) {
				if (singletonObjects.get(bean) != null)
					return singletonObjects.get(bean);
				return createBean(bean);
			}
			return singletonObjects.get(bean);
		} else {
			Object obj = createBean(bean);
			earlyPrototypeObjects.clear();
			return obj;
		}
	}

}
