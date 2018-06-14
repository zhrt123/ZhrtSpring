package com.myspring.aop.advice;

import java.lang.reflect.Method;

import com.myspring.aop.MethodInvocation;

public abstract class MethodBeforeAdvice implements MethodInterceptor {
	public abstract void before(Method method, Object[] args, Object target);

	@Override
	public Object invoke(MethodInvocation m) throws Throwable {
		before(m.getMethod(), m.getArgs(), m.getTarget());
		return m.proceed();
	}
}
