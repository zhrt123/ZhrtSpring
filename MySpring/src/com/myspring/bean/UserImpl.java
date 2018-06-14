package com.myspring.bean;

public class UserImpl implements User {
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void sayHello() {
		System.out.println("hello! I'm " + name);
	}

}
