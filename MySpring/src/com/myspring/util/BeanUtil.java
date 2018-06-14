package com.myspring.util;

import java.lang.reflect.Method;
import java.sql.Date;

/*
 * Ϊ����ע��ֵ�Ĺ���
 */
public class BeanUtil {
	/*
	 * ע��
	 * 
	 * @param method setter����
	 * 
	 * @param obj ��Ҫע�����ԵĶ���
	 * 
	 * @param value ����ֵ
	 */
	public static void populate(Method method, Object obj, String value) throws Exception {
		Class[] clazz = method.getParameterTypes();
		String type = clazz[0].getCanonicalName();
		if (type.equals("java.lang.String")) {
			method.invoke(obj, value);
		}
		if (type.equals("java.lang.Integer")) {
			method.invoke(obj, Integer.parseInt(value));
		}
		if (type.equals("java.lang.Short")) {
			method.invoke(obj, Short.parseShort(value));
		}
		if (type.equals("java.lang.Long")) {
			method.invoke(obj, Long.parseLong(value));
		}
		if (type.equals("java.lang.Double")) {
			method.invoke(obj, Double.parseDouble(value));
		}
		if (type.equals("java.lang.Float")) {
			method.invoke(obj, Float.parseFloat(value));
		}
		if (type.equals("java.lang.Boolean")) {
			method.invoke(obj, Boolean.parseBoolean(value));
		}
		if (type.equals("java.lang.Byte")) {
			method.invoke(obj, Byte.parseByte(value));
		}
		if (type.equals("java.lang.Character")) {
			method.invoke(obj, value.toCharArray()[0]);
		}
		if (type.equals("java.util.Date")) {
			method.invoke(obj, Date.valueOf(value));
		}
	}

	public static void populate(Method method, Object obj, Object value) throws Exception {
		method.invoke(obj, value);
	}
}
