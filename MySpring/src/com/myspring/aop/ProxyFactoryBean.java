package com.myspring.aop;

import java.util.ArrayList;
import java.util.List;

public class ProxyFactoryBean {
	private Object target; // 代理的目标对象
	private String proxyInterface; // 代理要实现的接口
	private List<Object> interceptors = new ArrayList<Object>(); // 要注入目标的通知

	public void setTarget(Object target) {
		this.target = target;
	}

	public void setProxyInterface(String proxyInterface) {
		this.proxyInterface = proxyInterface;
	}

	public void setInterceptorName(Object interceptorName) {
		this.interceptors.add(interceptorName);
	}

	/*
	 * 创建代理对象
	 */
	public Object createProxy() {
		
		if (proxyInterface == null || proxyInterface.trim().isEmpty())
			return createCGlibProxy();
		else
			return createJDKProxy();
	}

	/*
	 * 通过JDK创建代理对象
	 */
	private Object createJDKProxy() {
		return new JDKProxyFactory(target, proxyInterface, interceptors).getJDKProxy();
	}

	/*
	 * 通过CGlib创建代理对象
	 */
	private Object createCGlibProxy() {

		return new CGlibProxyFactory(target, interceptors).getProxy();

	}
}
