package com.myspring.bean;

import com.myspring.aop.MethodInvocation;
import com.myspring.aop.advice.AroundAdvice;

public class MyAroundAdvice extends AroundAdvice {

	@Override
	public Object invoke(MethodInvocation m) throws Throwable {
		System.out.println("Around-----����ǰ");
		Object result = m.proceed();
		System.out.println("Around-----������");
		return result;
	}

}
