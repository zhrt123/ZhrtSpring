package com.myspring.config;

import java.util.List;

public class Bean {
	public static enum ScopeType {
		singleton, prototype
	}

	private String name;
	private String className;
	private List<Property> properties;
	private ScopeType scope = ScopeType.singleton;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public List<Property> getProperties() {
		return properties;
	}

	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}

	public ScopeType getScope() {
		return scope;
	}

	public void setScope(ScopeType scope) {
		this.scope = scope;
	}

	public String toString() {
		return "Bean[name =" + name + " ClassName =" + className + " scope =" + scope + " properties =" + properties
				+ "]";
	}
}
