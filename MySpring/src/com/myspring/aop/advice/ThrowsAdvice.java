package com.myspring.aop.advice;

import java.lang.reflect.Method;

import com.myspring.aop.MethodInvocation;

public abstract class ThrowsAdvice implements MethodInterceptor{
	public abstract void afterThrowing(Method method, Object[] args, Object target, Throwable e);
	
	@Override
	public Object invoke(MethodInvocation m) throws Throwable {
		try {
			m.proceed();
		} catch (Throwable e) {
			afterThrowing(m.getMethod(), m.getArgs(), m.getTarget(), e);
		}
		return null;
	}
}
