package be.appify.view.jaxrs.parameter;

import javax.inject.Singleton;

import be.appify.i18n.InternationalizationProvider;
import be.appify.view.jaxrs.attribute.AttributeType;

@Singleton
public class IntegerParameterViewFactory implements TypeSpecificParameterViewFactory {

    @Override
    public JaxRsParameterView createParameter(String name, Class<?> parameterType, Parameter parameter, String containingTypeKey,
            InternationalizationProvider i18nProvider) {
        return new JaxRsParameterView(i18nProvider, containingTypeKey, name, AttributeType.INTEGER, parameter);
    }

    @Override
    public Class<?> getType() {
        return int.class;
    }

}
