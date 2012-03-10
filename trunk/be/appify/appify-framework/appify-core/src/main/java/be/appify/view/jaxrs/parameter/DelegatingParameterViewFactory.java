package be.appify.view.jaxrs.parameter;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import be.appify.i18n.InternationalizationProvider;

public class DelegatingParameterViewFactory {
    private final Map<String, NamedParameterViewFactory> classSpecificFactories;

    @Inject
    public DelegatingParameterViewFactory(@Any Instance<NamedParameterViewFactory> parameterViewFactories) {
        this.classSpecificFactories = new HashMap<String, NamedParameterViewFactory>();
        Iterator<NamedParameterViewFactory> iterator = parameterViewFactories.iterator();
        while (iterator.hasNext()) {
            NamedParameterViewFactory parameterViewFactory = iterator.next();
            this.classSpecificFactories.put(parameterViewFactory.getName(), parameterViewFactory);
        }
    }

    public JaxRsParameterView createParameter(String name, Annotation[] annotationArray, Class<?> parameterType, String containingTypeKey,
            InternationalizationProvider i18nProvider) {
        JaxRsParameterView result = null;
        Collection<Annotation> annotations = Arrays.asList(annotationArray);
        Parameter p = getAnnotation(annotations, Parameter.class);
        if (p != null) {
            NamedParameterViewFactory factory = classSpecificFactories.get(p.type());
            if (factory != null) {
                result = factory.createParameter(name, parameterType, p, containingTypeKey, i18nProvider);
            } else {
                result = new JaxRsParameterView(i18nProvider, containingTypeKey, name, p.type(), p);
            }
        }
        return result;
    }

    private <T extends Annotation> T getAnnotation(Collection<Annotation> annotations, Class<T> annotationClass) {
        for (Annotation annotation : annotations) {
            if (annotationClass.isAssignableFrom(annotation.getClass())) {
                return annotationClass.cast(annotation);
            }
        }
        return null;
    }
}
