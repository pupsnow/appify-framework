package be.appify.view.jaxrs.attribute;

import javax.inject.Singleton;

import be.appify.i18n.InternationalizationProvider;

@Singleton
public class IntegerAttributeViewFactory implements TypeSpecificAttributeViewFactory {

    @Override
    public JaxRsAttributeView createAttribute(String name, Class<?> parameterType, Attribute attribute, String containingTypeKey,
            InternationalizationProvider i18nProvider) {
        return new JaxRsAttributeView(i18nProvider, containingTypeKey, name, AttributeType.INTEGER, attribute);
    }

    @Override
    public Class<?> getType() {
        return int.class;
    }

}
