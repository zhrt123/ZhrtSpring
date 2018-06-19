package com.myspring.bean;

public class B {
	private String name;
	private A refA;

	public A getRefA() {
		return refA;
	}

	public void setRefA(A refA) {
		this.refA = refA;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String toString() {
		return "B[" + name + " " + refA.getName() + "]";
	}
}
