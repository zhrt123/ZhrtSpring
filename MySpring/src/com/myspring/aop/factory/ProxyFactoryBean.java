package com.myspring.aop.factory;

import java.util.ArrayList;
import java.util.List;

public class ProxyFactoryBean {
	private Object target; // �����Ŀ�����
	// private String proxyInterface; // ����Ҫʵ�ֵĽӿ�
	private List<Object> interceptors = new ArrayList<Object>(); // Ҫע��Ŀ���֪ͨ

	public void setTarget(Object target) {
		this.target = target;
	}

	// public void setProxyInterface(String proxyInterface) {
	// this.proxyInterface = proxyInterface;
	// }

	public void setInterceptorName(Object interceptorName) {
		this.interceptors.add(interceptorName);
	}

	/*
	 * �����������
	 */
	public Object createProxy() {
		Class<?>[] proxyInterfaces = target.getClass().getInterfaces();
		if (proxyInterfaces == null || proxyInterfaces.length == 0)
			return createCGlibProxy();
		else
			return createJDKProxy(proxyInterfaces[0]);
	}

	/*
	 * ͨ��JDK�����������
	 */
	private Object createJDKProxy(Object proxyInterface) {
		return new JDKProxyFactory(target, proxyInterface, interceptors).getJDKProxy();
	}

	/*
	 * ͨ��CGlib�����������
	 */
	private Object createCGlibProxy() {

		return new CGlibProxyFactory(target, interceptors).getProxy();

	}
}
