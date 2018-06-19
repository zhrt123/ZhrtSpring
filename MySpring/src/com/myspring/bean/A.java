package com.myspring.bean;

public class A {
	private String name;
	private B refB;

	public B getRefB() {
		return refB;
	}

	public void setRefB(B refB) {
		this.refB = refB;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String toString() {
		return "A[" + name + " " + refB.getName() + "]";
	}
}
