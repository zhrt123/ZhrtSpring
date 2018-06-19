package com.myspring.aop;

import java.lang.reflect.Method;
import java.util.List;

import com.myspring.aop.advice.MethodInterceptor;
import com.myspring.config.Interceptor;

public class MethodInvocation {
	private List<Object> chain;
	private int chainIndex;
	private Method method;
	private Object[] args;
	private Object target;

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public Object[] getArgs() {
		return args;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}

	public Object getTarget() {
		return target;
	}

	public void setTarget(Object target) {
		this.target = target;
	}

	public MethodInvocation(List<Object> chain, Method method, Object[] args, Object target) {
		this.chain = chain;
		chainIndex = -1;
		this.method = method;
		this.args = args;
		this.target = target;
	}

	boolean methodCheck(Interceptor interceptor) {
		// 检查是否有正则表达式
		if (interceptor.getCheck() == null) {
			// 检查是否有注解
			if (method.isAnnotationPresent(AdviceAnnotation.class) == false)
				return true;
			AdviceAnnotation[] annotations = (AdviceAnnotation[]) method.getAnnotations();
			for (AdviceAnnotation annotation : annotations) {
				if (annotation.check().matches(interceptor.getName()))
					return true;
			}
			return false;
		}
		// 有正则表达式则直接用正则表达式匹配
		return method.getName().matches(interceptor.getCheck());
	}

	public Object proceed() throws Throwable {
		if (chainIndex == chain.size() - 1) {
			return method.invoke(target, args);
		}
		Interceptor interceptor = (Interceptor) chain.get(++chainIndex);
		// 检查是否调用拦截器
		if (methodCheck(interceptor))
			return ((MethodInterceptor) interceptor.getInterceptor()).invoke(this);
		else
			return proceed();
	}
}
