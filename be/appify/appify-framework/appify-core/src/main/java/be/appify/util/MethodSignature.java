package be.appify.util;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.builder.HashCodeBuilder;

public final class MethodSignature {
    private final String name;
    private final Class<?> returnType;
    private final List<Class<?>> parameterTypes;
    private final int modifiers;

    public MethodSignature(Method method) {
        this(method.getReturnType(), method.getName(), method.getModifiers(), method.getParameterTypes());
    }

    public MethodSignature(String name, int modifiers, Class<?>... parameterTypes) {
        this(null, name, modifiers, parameterTypes);
    }

    public MethodSignature(Class<?> returnType, String name, int modifiers, Class<?>... parameterTypes) {
        this.name = name;
        this.returnType = returnType;
        if (Modifier.isAbstract(modifiers)) {
            modifiers = modifiers - Modifier.ABSTRACT;
        }
        if (Modifier.isFinal(modifiers)) {
            modifiers = modifiers - Modifier.FINAL;
        }
        this.modifiers = modifiers;
        this.parameterTypes = Arrays.asList(parameterTypes);
    }

    public String getName() {
        return name;
    }

    public Class<?> getReturnType() {
        return returnType;
    }

    public List<Class<?>> getParameterTypes() {
        return Collections.unmodifiableList(parameterTypes);
    }

    public Method findOn(Class<?> type) {
        for (Method method : type.getMethods()) {
            MethodSignature signature = new MethodSignature(method);
            if (equals(signature)) {
                return method;
            }
        }
        return null;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(name).append(returnType).append(modifiers).append(parameterTypes).toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof MethodSignature)) {
            return false;
        }
        MethodSignature m = (MethodSignature) obj;
        if (m.name.equals(name) && (m.modifiers) == (modifiers)) {
            if (!m.returnType.equals(returnType)) {
                return false;
            }
            List<Class<?>> params1 = m.parameterTypes;
            List<Class<?>> params2 = parameterTypes;
            if (params1.size() == params2.size()) {
                for (int i = 0; i < params1.size(); i++) {
                    if (params1.get(i) != params2.get(i)) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(Modifier.toString(modifiers))
                .append(" ")
                .append(returnType != null ? returnType.getSimpleName() : "void")
                .append(" ")
                .append(name)
                .append("(");
        boolean first = true;
        for (Class<?> parameterType : parameterTypes) {
            if (first) {
                first = false;
            } else {
                builder.append(", ");
            }
            builder.append(parameterType.getSimpleName());
        }
        builder.append(")");
        return builder.toString();
    }

}
