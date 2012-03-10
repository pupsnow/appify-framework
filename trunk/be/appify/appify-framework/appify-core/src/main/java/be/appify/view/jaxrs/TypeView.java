package be.appify.view.jaxrs;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates this type is an entity type view
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface TypeView {

    /**
     * @return the name of the entity type
     */
    String type();

    /**
     * @return the namespace of the entity type
     */
    String namespace();

}
