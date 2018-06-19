package com.myspring.aop.factory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import com.myspring.aop.MethodInvocation;

public class JDKProxyFactory implements InvocationHandler {
	private Object target; // �����Ŀ�����
	private Object proxyInterface;// ����Ҫʵ�ֵĽӿ�
	private List<Object> interceptors = new ArrayList<Object>(); // Ҫע��Ŀ���֪ͨ

	public JDKProxyFactory(Object target, Object proxyInterface, List<Object> interceptors) {
		this.target = target;
		this.proxyInterface = proxyInterface;
		this.interceptors = interceptors;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		MethodInvocation m = new MethodInvocation(interceptors, method, args, target);
		return m.proceed();

	}

	// ��ȡһ��JDK�������
	public Object getJDKProxy() {
		try {
			return Proxy.newProxyInstance(target.getClass().getClassLoader(),
					new Class[] { (Class) proxyInterface }, this);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
