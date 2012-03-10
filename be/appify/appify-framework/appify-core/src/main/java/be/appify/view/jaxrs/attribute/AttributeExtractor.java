package be.appify.view.jaxrs.attribute;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

import be.appify.i18n.InternationalizationProvider;
import be.appify.view.jaxrs.TypeView;

public class AttributeExtractor {
    private final Class<?> type;
    private final InternationalizationProvider i18nProvider;
    private final DelegatingAttributeViewFactory attributeFactory;

    public AttributeExtractor(Class<?> type, InternationalizationProvider i18nProvider, DelegatingAttributeViewFactory attributeFactory) {
        this.type = type;
        this.i18nProvider = i18nProvider;
        this.attributeFactory = attributeFactory;
    }

    public List<JaxRsAttributeView> extractAttributes() {
        List<JaxRsAttributeView> attributes = new ArrayList<JaxRsAttributeView>();
        for (Method method : type.getMethods()) {
            if (method.isAnnotationPresent(Attribute.class)) {
                JaxRsAttributeView attribute = extractAttribute(method);
                if (attribute != null) {
                    attributes.add(attribute);
                }
            }
        }
        Collections.sort(attributes);
        return attributes;
    }

    private JaxRsAttributeView extractAttribute(Method method) {
        String methodName = method.getName();
        if (!methodName.startsWith("get")) {
            throw new IllegalArgumentException("Method marked @Attribute is not a getter: " + method);
        }
        String name = StringUtils.uncapitalize(methodName.substring(3));
        JaxRsAttributeView attribute = attributeFactory.createAttribute(name, method.getAnnotations(),
                    method.getReturnType(), getTypeKey(), i18nProvider);
        return attribute;
    }

    private TypeView getTypeView() {
        TypeView typeView = type.getAnnotation(TypeView.class);
        Validate.notNull(typeView, "Missing TypeView annotation on " + type);
        return typeView;
    }

    private String getTypeKey() {
        TypeView typeView = getTypeView();
        return "type." + typeView.namespace() + "." + typeView.type();
    }
}
