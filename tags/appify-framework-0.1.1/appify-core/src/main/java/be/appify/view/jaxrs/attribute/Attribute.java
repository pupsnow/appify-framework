package be.appify.view.jaxrs.attribute;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Attribute {
    String type() default AttributeType.AUTO;

    int order() default 1;

    boolean describing() default false;

    Class<?> referencedType() default Object.class;
}
