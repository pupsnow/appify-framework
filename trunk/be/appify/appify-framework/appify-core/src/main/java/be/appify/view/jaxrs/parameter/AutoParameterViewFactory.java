package be.appify.view.jaxrs.parameter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import be.appify.i18n.InternationalizationProvider;
import be.appify.view.jaxrs.attribute.AttributeType;

public class AutoParameterViewFactory implements NamedParameterViewFactory {

    private Map<Class<?>, TypeSpecificParameterViewFactory> typeSpecificFactories;

    @Inject
    @Any
    public void setTypeSpecificFactories(Instance<TypeSpecificParameterViewFactory> typeSpecificFactories) {
        setTypeSpecificFactories((Iterable<TypeSpecificParameterViewFactory>) typeSpecificFactories);
    }

    @Override
    public JaxRsParameterView createParameter(String name, Class<?> parameterType, Parameter parameter,
            String containingTypeKey, InternationalizationProvider i18nProvider) {
        JaxRsParameterView result = null;
        TypeSpecificParameterViewFactory factory = getFactoryForType(parameterType);
        if (factory != null) {
            result = factory.createParameter(name, parameterType, parameter, containingTypeKey, i18nProvider);
        }
        return result;
    }

    public TypeSpecificParameterViewFactory getFactoryForType(Class<?> parameterType) {
        TypeSpecificParameterViewFactory factory = typeSpecificFactories.get(parameterType);
        if (factory == null && parameterType.getSuperclass() != Object.class) {
            factory = getFactoryForType(parameterType.getSuperclass());
        }
        return factory;
    }

    @Override
    public String getName() {
        return AttributeType.AUTO;
    }

    public void setTypeSpecificFactories(Iterable<TypeSpecificParameterViewFactory> typeSpecificFactories) {
        this.typeSpecificFactories = new HashMap<Class<?>, TypeSpecificParameterViewFactory>();
        Iterator<TypeSpecificParameterViewFactory> iterator = typeSpecificFactories.iterator();
        while (iterator.hasNext()) {
            TypeSpecificParameterViewFactory ParameterViewFactory = iterator.next();
            this.typeSpecificFactories.put(ParameterViewFactory.getType(), ParameterViewFactory);
        }
    }

}
