package be.appify.view.jaxrs.parameter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import be.appify.view.jaxrs.attribute.AttributeType;

@Target({ ElementType.METHOD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface Parameter {
    String type() default AttributeType.AUTO;

    int order() default 1;

    boolean required() default false;

    Class<?> referencedType() default Object.class;
}
