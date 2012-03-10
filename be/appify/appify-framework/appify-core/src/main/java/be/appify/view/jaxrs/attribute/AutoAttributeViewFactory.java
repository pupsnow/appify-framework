package be.appify.view.jaxrs.attribute;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import be.appify.i18n.InternationalizationProvider;

public class AutoAttributeViewFactory implements NamedAttributeViewFactory {

    private Map<Class<?>, TypeSpecificAttributeViewFactory> typeSpecificFactories;

    @Inject
    @Any
    public void setTypeSpecificFactories(Instance<TypeSpecificAttributeViewFactory> typeSpecificFactories) {
        setTypeSpecificFactories((Iterable<TypeSpecificAttributeViewFactory>) typeSpecificFactories);
    }

    @Override
    public JaxRsAttributeView createAttribute(String name, Class<?> parameterType, Attribute attribute,
            String containingTypeKey, InternationalizationProvider i18nProvider, DelegatingAttributeViewFactory factory) {
        JaxRsAttributeView result = null;
        TypeSpecificAttributeViewFactory typeSpecificFactory = getFactoryForType(parameterType);
        if (typeSpecificFactory != null) {
            result = typeSpecificFactory.createAttribute(name, parameterType, attribute, containingTypeKey, i18nProvider);
        }
        return result;
    }

    public TypeSpecificAttributeViewFactory getFactoryForType(Class<?> parameterType) {
        TypeSpecificAttributeViewFactory factory = typeSpecificFactories.get(parameterType);
        if (factory == null && parameterType.getSuperclass() != Object.class) {
            factory = getFactoryForType(parameterType.getSuperclass());
        }
        return factory;
    }

    @Override
    public String getName() {
        return AttributeType.AUTO;
    }

    public void setTypeSpecificFactories(Iterable<TypeSpecificAttributeViewFactory> typeSpecificFactories) {
        this.typeSpecificFactories = new HashMap<Class<?>, TypeSpecificAttributeViewFactory>();
        Iterator<TypeSpecificAttributeViewFactory> iterator = typeSpecificFactories.iterator();
        while (iterator.hasNext()) {
            TypeSpecificAttributeViewFactory attributeViewFactory = iterator.next();
            this.typeSpecificFactories.put(attributeViewFactory.getType(), attributeViewFactory);
        }
    }

}
