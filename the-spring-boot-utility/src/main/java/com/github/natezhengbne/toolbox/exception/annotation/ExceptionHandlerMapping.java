package com.github.natezhengbne.toolbox.exception.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExceptionHandlerMapping {

    Class<?> value();

}