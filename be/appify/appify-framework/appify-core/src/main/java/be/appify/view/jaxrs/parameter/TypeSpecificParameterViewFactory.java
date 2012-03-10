package be.appify.view.jaxrs.parameter;

import be.appify.i18n.InternationalizationProvider;

public interface TypeSpecificParameterViewFactory {

    JaxRsParameterView createParameter(String name, Class<?> parameterType, Parameter parameter, String containingTypeKey,
            InternationalizationProvider i18nProvider);

    Class<?> getType();

}
