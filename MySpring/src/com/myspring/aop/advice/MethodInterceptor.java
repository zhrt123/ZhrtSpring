package com.myspring.aop.advice;

import com.myspring.aop.MethodInvocation;

public interface MethodInterceptor {
	public Object invoke(MethodInvocation m) throws Throwable;
}
