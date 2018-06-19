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
		// ����Ƿ���������ʽ
		if (interceptor.getCheck() == null) {
			// ����Ƿ���ע��
			if (method.isAnnotationPresent(AdviceAnnotation.class) == false)
				return true;
			AdviceAnnotation[] annotations = (AdviceAnnotation[]) method.getAnnotations();
			for (AdviceAnnotation annotation : annotations) {
				if (annotation.check().matches(interceptor.getName()))
					return true;
			}
			return false;
		}
		// ��������ʽ��ֱ����������ʽƥ��
		return method.getName().matches(interceptor.getCheck());
	}

	public Object proceed() throws Throwable {
		if (chainIndex == chain.size() - 1) {
			return method.invoke(target, args);
		}
		Interceptor interceptor = (Interceptor) chain.get(++chainIndex);
		// ����Ƿ����������
		if (methodCheck(interceptor))
			return ((MethodInterceptor) interceptor.getInterceptor()).invoke(this);
		else
			return proceed();
	}
}
