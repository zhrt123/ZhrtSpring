package com.myspring.config;

import com.myspring.aop.advice.MethodInterceptor;

public class Interceptor {
	private MethodInterceptor interceptor;
	private String check;
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public MethodInterceptor getInterceptor() {
		return interceptor;
	}

	public void setInterceptor(MethodInterceptor interceptor) {
		this.interceptor = interceptor;
	}

	public String getCheck() {
		return check;
	}

	public void setCheck(String check) {
		this.check = check;
	}

	public String toString() {
		return "[" + interceptor.toString() + " " + check + "]";
	}
}
