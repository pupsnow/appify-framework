package be.appify.view.jaxrs.attribute;

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

public class DelegatingAttributeViewFactory {
    private final Map<String, NamedAttributeViewFactory> classSpecificFactories;

    @Inject
    public DelegatingAttributeViewFactory(@Any Instance<NamedAttributeViewFactory> attributeViewFactories) {
        this.classSpecificFactories = new HashMap<String, NamedAttributeViewFactory>();
        Iterator<NamedAttributeViewFactory> iterator = attributeViewFactories.iterator();
        while (iterator.hasNext()) {
            NamedAttributeViewFactory attributeViewFactory = iterator.next();
            this.classSpecificFactories.put(attributeViewFactory.getName(), attributeViewFactory);
        }
    }

    public JaxRsAttributeView createAttribute(String name, Annotation[] annotationArray, Class<?> parameterType, String containingTypeKey,
            InternationalizationProvider i18nProvider) {
        JaxRsAttributeView result = null;
        Collection<Annotation> annotations = Arrays.asList(annotationArray);
        Attribute a = getAnnotation(annotations, Attribute.class);
        if (a != null) {
            NamedAttributeViewFactory factory = classSpecificFactories.get(a.type());
            if (factory != null) {
                result = factory.createAttribute(name, parameterType, a, containingTypeKey, i18nProvider, this);
            } else {
                result = new JaxRsAttributeView(i18nProvider, containingTypeKey, name, a.type(), a);
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
