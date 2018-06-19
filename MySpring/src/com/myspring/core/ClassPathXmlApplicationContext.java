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
	private Map<String, Bean> beans; // beanName->Bean��bean��
	private Map<Bean, Object> singletonObjects = new HashMap<Bean, Object>(); // ����ģʽ����
	private Map<Bean, Object> earlySingletonObjects = new HashMap<Bean, Object>();// �����ع�ĵ���ģʽ����
	private Map<Bean, Object> earlyPrototypeObjects = new HashMap<Bean, Object>();// �����ع��ԭ��ģʽ����
	private ConfigurationManager configManager; // ��ȡ�����ļ�

	/*
	 * ��ʼ��Ioc����,��������Ϣ����beans,������ģʽ��������������
	 * 
	 * @param path
	 */
	public ClassPathXmlApplicationContext(String path) {
		configManager = new ConfigurationManager(path);
		beans = configManager.getBeanconfig();

		if (beans != null) {
			// ������ģʽ����������ع�ĵ���ģʽ����
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

			// ���ص���ģʽ�࣬�����뵥��ģʽ����
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
	 * ��ȡ����ģʽ��
	 */
	private Object getSingleton(Bean bean) {
		Object obj = earlySingletonObjects.get(bean);
		if (obj != null)
			return obj;
		obj = singletonObjects.get(bean);
		return obj;
	}

	/*
	 * ��ȡԭ��ģʽ��
	 */
	private Object getPrototype(Bean bean) {
		Object obj = earlyPrototypeObjects.get(bean);
		if (obj != null)
			return obj;
		return this.createBean(bean);
	}

	/*
	 * �����������
	 */
	private Object getRef(String beanName) {
		Bean bean = beans.get(beanName);
		if (bean.getScope() == ScopeType.singleton)
			return this.getSingleton(bean);
		else
			return this.getPrototype(bean);
	}

	/*
	 * ����ʵ������
	 */
	private Object createBean(Bean bean) {
		Object beanObj = null;
		try {
			// ����õ�Ҫ�����Ķ���ʵ��
			Class clazz = Class.forName(bean.getClassName());
			if (bean.getScope() == ScopeType.singleton) {
				// ����ö����ǵ���ģʽ����������ع�ĵ���ģʽ��������ȡ
				beanObj = earlySingletonObjects.get(bean);
			} else {
				// ����ö�����ԭ��ģʽ����ֱ�Ӵ���ʵ���������ö�����������ع��ԭ��ģʽ��������ȡ
				beanObj = clazz.newInstance();
				earlyPrototypeObjects.put(bean, beanObj);
			}
			// Ϊ���������ע��ֵ
			Method[] methods = clazz.getMethods();
			for (Property pro : bean.getProperties()) {
				if (beanObj instanceof ProxyFactoryBean && pro.getName().equals("interceptorName")) {
					// ������
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
					// ��������ϵ����
					for (Method method : methods) {
						if (method.getName().equalsIgnoreCase("set" + pro.getName())) {
							BeanUtil.populate(method, beanObj, pro.getValue());
							break;
						}
					}
				} else {
					// ������ϵ����
					Object ref = this.getRef(pro.getRef());
					for (Method method : methods) {
						if (method.getName().equalsIgnoreCase("set" + pro.getName())) {
							BeanUtil.populate(method, beanObj, ref);
							break;
						}
					}
				}
			}
			// ����ö����Ǵ���������ѽ������������������ע�룬ֻ�贴������������ʵ��
			if (beanObj instanceof ProxyFactoryBean) {
				// ��ȡ������󹤳�
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
	 * ��ȡ����ʵ��
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
