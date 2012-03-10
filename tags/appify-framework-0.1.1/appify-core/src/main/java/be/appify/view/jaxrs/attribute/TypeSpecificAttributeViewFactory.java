package be.appify.view.jaxrs.attribute;

import be.appify.i18n.InternationalizationProvider;

public interface TypeSpecificAttributeViewFactory {

    JaxRsAttributeView createAttribute(String name, Class<?> parameterType, Attribute attribute, String containingTypeKey,
            InternationalizationProvider i18nProvider);

    Class<?> getType();

}