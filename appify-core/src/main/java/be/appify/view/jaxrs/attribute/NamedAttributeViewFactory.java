package be.appify.view.jaxrs.attribute;

import be.appify.i18n.InternationalizationProvider;

public interface NamedAttributeViewFactory {

    JaxRsAttributeView createAttribute(String name, Class<?> parameterType, Attribute attribute,
            String containingTypeKey, InternationalizationProvider i18nProvider, DelegatingAttributeViewFactory factory);

    String getName();

}
