package be.appify.view.jaxrs.attribute;

import java.util.List;

import be.appify.i18n.InternationalizationProvider;

public class CompositeAttributeViewFactory implements NamedAttributeViewFactory {

    @Override
    public JaxRsAttributeView createAttribute(String name, Class<?> parameterType, Attribute attribute, String containingTypeKey,
            InternationalizationProvider i18nProvider, DelegatingAttributeViewFactory factory) {
        return new JaxRsCompositeAttributeView(i18nProvider, containingTypeKey, name, attribute.type(), attribute,
                extractComponents(parameterType, i18nProvider, factory));
    }

    private List<JaxRsAttributeView> extractComponents(Class<?> parameterType, InternationalizationProvider i18nProvider, DelegatingAttributeViewFactory factory) {
        return new AttributeExtractor(parameterType, i18nProvider, factory).extractAttributes();
    }

    @Override
    public String getName() {
        return AttributeType.COMPOSITE;
    }

}
