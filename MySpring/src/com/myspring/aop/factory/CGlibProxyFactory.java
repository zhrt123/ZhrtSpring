package com.myspring.aop.factory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.myspring.aop.MethodInvocation;

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

	// 获取一个CGlib代理对象
	public Object getProxy() {
		Enhancer enhancer = new Enhancer();
		// 设置父类
		enhancer.setSuperclass(target.getClass());
		// 设置回调函数
		enhancer.setCallback(this);
		return enhancer.create();
	}
}
