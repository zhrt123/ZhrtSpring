package com.myspring.aop;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME) // ע�����class�ֽ����ļ��д��ڣ�������ʱ����ͨ�������ȡ��
@Target({ ElementType.METHOD }) // ����ע�������Ŀ��
@Repeatable(AdviceAnnotations.class)
public @interface AdviceAnnotation {
	String check() default "";
}
