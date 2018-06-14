package com.myspring.aop;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class CGlibProxyFactory implements MethodInterceptor {
	private Object target; // 代理的目标对象
	private List<Object> interceptors = new ArrayList<Object>(); // 要注入目标的通知

	public CGlibProxyFactory(Object target, List<Object> interceptors) {
		this.target = target;
		this.interceptors = interceptors;
	}

	public Object intercept(Object target, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
		MethodInvocation m = new MethodInvocation(interceptors, method, args, this.target);
		return m.proceed();
	}

	public Object getProxy() {
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(target.getClass());
		enhancer.setCallback(this);
		return enhancer.create();
	}
}
