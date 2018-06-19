package com.myspring.aop;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME) // 注解会在class字节码文件中存在，在运行时可以通过反射获取到
@Target({ ElementType.METHOD }) // 定义注解的作用目标
@Repeatable(AdviceAnnotations.class)
public @interface AdviceAnnotation {
	String check() default "";
}
