package com.example.lpf.databasemodule.orm;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by lpf on 2017/4/24.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD}) //用在属性上面   标记了可以用在什么上面
public @interface Column {
    /**
     * 列名字
     * @return
     */
   String name();

    /**
     * 类型
     * @return
     */
    String type() default "";

    /**
     * 长度
     * @return
     */
    int length() default 0;

}
