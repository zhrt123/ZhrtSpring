package com.myspring.bean;

import java.lang.reflect.Method;

import com.myspring.aop.advice.AfterReturningAdvice;

public class MyAfterAdvice extends AfterReturningAdvice {

	@Override
	public Object afterReturning(Object result, Method method, Object[] args, Object target) {
		System.out.println("-----after-----");
		return result;
	}
}
