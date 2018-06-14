package com.myspring.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

public class JDKProxyFactory implements InvocationHandler {
	private Object target; // 代理的目标对象
	private String proxyInterface;// 代理要实现的接口
	private List<Object> interceptors = new ArrayList<Object>(); // 要注入目标的通知

	public JDKProxyFactory(Object target, String proxyInterface, List<Object> interceptors) {
		this.target = target;
		this.proxyInterface = proxyInterface;
		this.interceptors = interceptors;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		MethodInvocation m = new MethodInvocation(interceptors, method, args, target);
		return m.proceed();

	}

	public Object getJDKProxy() {
		try {
			return Proxy.newProxyInstance(target.getClass().getClassLoader(),
					new Class[] { Class.forName(proxyInterface) }, this);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
