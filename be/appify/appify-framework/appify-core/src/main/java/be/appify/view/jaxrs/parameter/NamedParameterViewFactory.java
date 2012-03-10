package be.appify.view.jaxrs.parameter;

import be.appify.i18n.InternationalizationProvider;

public interface NamedParameterViewFactory {

    String getName();

    JaxRsParameterView createParameter(String name, Class<?> parameterType, Parameter p, String containingTypeKey, InternationalizationProvider i18nProvider);

}
