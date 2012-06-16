package be.appify.stereotype.core.operation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Operation {

	@SuppressWarnings("rawtypes")
	Class<? extends GenericOperation> value();

}
