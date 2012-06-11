package be.appify.stereotype.core.beans.fields.stereotypes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import be.appify.stereotype.core.beans.fields.Field;
import be.appify.stereotype.core.beans.fields.FieldType;
import be.appify.stereotype.core.beans.validation.MaxLength;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.PARAMETER })
@Field(FieldType.TEXT)
@MaxLength(255)
public @interface ShortTextType {
	String value() default "";
}
