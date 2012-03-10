package be.appify.util;

import java.lang.annotation.Annotation;

public final class AnnotationUtil {
    private AnnotationUtil() {
    }

    /**
     * Finds an annotation on the declaring type or one of its supertypes.
     */
    public static <T extends Annotation> T getAnnotation(Class<?> declaringType, Class<T> annotationType) {
        Class<?> unwrapped = unwrapType(declaringType, annotationType);
        if (unwrapped != null) {
            return unwrapped.getAnnotation(annotationType);
        }
        return null;
    }

    /**
     * Locates an annotation on a type or one of its supertypes.
     * 
     * @return the type declaring the annotation, or null if no such type was
     *         found
     */
    public static <T extends Annotation> Class<?> unwrapType(Class<?> declaringType, Class<T> annotationType) {
        Class<?> result = null;
        if (declaringType.isAnnotationPresent(annotationType)) {
            result = declaringType;
        } else if (declaringType.getSuperclass() != null) {
            result = unwrapType(declaringType.getSuperclass(), annotationType);
        }
        return result;
    }
}
