package com.myspring.aop.factory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.myspring.aop.MethodInvocation;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class CGlibProxyFactory implements MethodInterceptor {
	private Object target; // �����Ŀ�����
	private List<Object> interceptors = new ArrayList<Object>(); // Ҫע��Ŀ���֪ͨ

	public CGlibProxyFactory(Object target, List<Object> interceptors) {
		this.target = target;
		this.interceptors = interceptors;
	}

	public Object intercept(Object target, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
		MethodInvocation m = new MethodInvocation(interceptors, method, args, this.target);
		return m.proceed();
	}

	// ��ȡһ��CGlib�������
	public Object getProxy() {
		Enhancer enhancer = new Enhancer();
		// ���ø���
		enhancer.setSuperclass(target.getClass());
		// ���ûص�����
		enhancer.setCallback(this);
		return enhancer.create();
	}
}
