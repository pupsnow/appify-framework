package be.appify.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.Validate;

public final class Extender {
    private Extender() {
    }

    public static <T> T extend(Class<T> expectedType, Object base, Object extension) {
        Validate.notNull(expectedType, "expectedType cannot be null.");
        Validate.notNull(base, "base cannot be null.");
        Validate.notNull(extension, "extension cannot be null.");
        ExtenderInvocationHandler<T> invocationHandler = new ExtenderInvocationHandler<T>(expectedType, base, extension);

        Set<Class<?>> interfaces = new HashSet<Class<?>>();
        interfaces.addAll(Arrays.asList(base.getClass().getInterfaces()));
        interfaces.addAll(Arrays.asList(extension.getClass().getInterfaces()));
        interfaces.add(expectedType);
        Object proxy = Proxy.newProxyInstance(Extender.class.getClassLoader(),
                interfaces.toArray(new Class<?>[interfaces.size()]), invocationHandler);
        return expectedType.cast(proxy);
    }

    private static class ExtenderInvocationHandler<T> implements InvocationHandler {

        private final Map<MethodSignature, Object> methodMapping;

        public ExtenderInvocationHandler(Class<T> expectedType, Object base, Object extension) {
            methodMapping = new HashMap<MethodSignature, Object>();
            createMethodMapping(expectedType, base, extension);
        }

        private void createMethodMapping(Class<T> expectedType, Object base, Object extension) {
            Set<MethodSignature> baseSignatures = getMethodSignatures(base.getClass());
            Set<MethodSignature> extensionSignatures = getMethodSignatures(extension.getClass());
            Set<MethodSignature> allSignatures = getMethodSignatures(expectedType);
            allSignatures.addAll(baseSignatures);
            allSignatures.addAll(extensionSignatures);
            for (MethodSignature signature : allSignatures) {
                if (extensionSignatures.contains(signature)) {
                    methodMapping.put(signature, extension);
                } else if (baseSignatures.contains(signature)) {
                    methodMapping.put(signature, base);
                } else {
                    throw new IllegalArgumentException(String.format(
                            "Neither the base type (%1$s) nor the extension (%2$s) declare method from expected type: %4$s",
                            base.getClass().getSimpleName(), extension.getClass().getSimpleName(), expectedType.getSimpleName(), signature));
                }
            }
        }

        private Set<MethodSignature> getMethodSignatures(Class<?> type) {
            Set<MethodSignature> signatures = new HashSet<MethodSignature>();
            for (Method method : type.getMethods()) {
                signatures.add(new MethodSignature(method));
            }
            return signatures;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            MethodSignature signature = new MethodSignature(method);
            Object target = methodMapping.get(signature);
            Method methodToInvoke = signature.findOn(target.getClass());
            methodToInvoke.setAccessible(true); // Required to access restricted
                                                // classes or methods
            return methodToInvoke.invoke(target, args);
        }

    }

}