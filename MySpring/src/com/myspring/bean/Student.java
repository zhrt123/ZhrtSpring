package com.myspring.bean;

import com.myspring.aop.AdviceAnnotation;

public class Student {
	private String name;
	private Integer age;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	@AdviceAnnotation(check = "beforeAdvice")
	@AdviceAnnotation(check = "aroundAdvice")
	public void doHello() {
		System.out.println("I'm " + name + " " + age + " years old!");
	}

	public void doThrows() {
		throw new RuntimeException("studentError");
	}
}
