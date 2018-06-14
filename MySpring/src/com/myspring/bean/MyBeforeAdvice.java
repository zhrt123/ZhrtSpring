package com.myspring.bean;

import java.lang.reflect.Method;

import com.myspring.aop.advice.MethodBeforeAdvice;

public class MyBeforeAdvice extends MethodBeforeAdvice {

	@Override
	public void before(Method method, Object[] args, Object target) {
		System.out.println("-----before-----");
	}

}
