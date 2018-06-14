package com.myspring.aop.advice;

import java.lang.reflect.Method;

import com.myspring.aop.MethodInvocation;

public abstract class AfterReturningAdvice implements MethodInterceptor {
	public abstract Object afterReturning(Object result, Method method, Object[] args, Object target);

	@Override
	public Object invoke(MethodInvocation m) throws Throwable {
		Object result = m.proceed();
		afterReturning(result, m.getMethod(), m.getArgs(), m.getTarget());
		return result;
	}
}
