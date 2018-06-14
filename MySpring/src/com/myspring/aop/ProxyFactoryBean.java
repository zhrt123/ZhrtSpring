package com.myspring.aop;

import java.util.ArrayList;
import java.util.List;

public class ProxyFactoryBean {
	private Object target; // �����Ŀ�����
	private String proxyInterface; // ����Ҫʵ�ֵĽӿ�
	private List<Object> interceptors = new ArrayList<Object>(); // Ҫע��Ŀ���֪ͨ

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
	 * �����������
	 */
	public Object createProxy() {
		
		if (proxyInterface == null || proxyInterface.trim().isEmpty())
			return createCGlibProxy();
		else
			return createJDKProxy();
	}

	/*
	 * ͨ��JDK�����������
	 */
	private Object createJDKProxy() {
		return new JDKProxyFactory(target, proxyInterface, interceptors).getJDKProxy();
	}

	/*
	 * ͨ��CGlib�����������
	 */
	private Object createCGlibProxy() {

		return new CGlibProxyFactory(target, interceptors).getProxy();

	}
}
