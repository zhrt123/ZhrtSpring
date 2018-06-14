package com.myspring.bean;

import java.lang.reflect.Method;

import com.myspring.aop.advice.ThrowsAdvice;

public class MyThrowsAdvice extends ThrowsAdvice {

	@Override
	public void afterThrowing(Method method, Object[] args, Object target, Throwable e) {
		System.out.println("抛出了一个异常：" + e.getCause());
	}

}
