package be.appify.view.jaxrs.attribute;

import javax.inject.Inject;

import be.appify.i18n.InternationalizationProvider;
import be.appify.view.jaxrs.ActiveViewCatalog;
import be.appify.view.jaxrs.JaxRsEnumConstantView;
import be.appify.view.jaxrs.JaxRsEnumPresenter;
import be.appify.view.jaxrs.JaxRsResourceView;

public class EnumAttributeViewFactory implements TypeSpecificAttributeViewFactory {
    private final ActiveViewCatalog viewCatalog;

    @Inject
    public EnumAttributeViewFactory(ActiveViewCatalog viewCatalog) {
        this.viewCatalog = viewCatalog;
    }

    @Override
    @SuppressWarnings("unchecked")
    public JaxRsAttributeView createAttribute(String name, Class<?> parameterType, Attribute attribute, String containingTypeKey,
            InternationalizationProvider i18nProvider) {
        @SuppressWarnings("rawtypes")
        Class<Enum> enumType = (Class<Enum>) attribute.referencedType();
        if (!enumType.isEnum()) {
            throw new IllegalArgumentException("Referenced type for " + containingTypeKey + "." + name + " should be an Enum type, found: " + enumType);
        }
        JaxRsResourceView listResource = viewForType(enumType).getOperation("list");
        return new JaxRsListAttributeView(i18nProvider, containingTypeKey, name, AttributeType.ENUMERATION, attribute, listResource.getUri(),
                "basic.enumerationConstant");
    }

    private <T extends Enum<T>> JaxRsEnumPresenter viewForType(Class<T> enumType) {
        JaxRsEnumPresenter view = viewCatalog.getView(JaxRsEnumPresenter.class);
        view = view.forEnum(enumType);
        return view;
    }

    @Override
    public Class<?> getType() {
        return JaxRsEnumConstantView.class;
    }

}
