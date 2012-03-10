package be.appify.view.jaxrs.parameter;

import javax.inject.Inject;

import be.appify.i18n.InternationalizationProvider;
import be.appify.view.jaxrs.ActiveViewCatalog;
import be.appify.view.jaxrs.JaxRsEnumPresenter;
import be.appify.view.jaxrs.JaxRsResourceView;
import be.appify.view.jaxrs.attribute.AttributeType;

public class EnumParameterViewFactory implements TypeSpecificParameterViewFactory {
    private final ActiveViewCatalog viewCatalog;

    @Inject
    public EnumParameterViewFactory(ActiveViewCatalog viewCatalog) {
        this.viewCatalog = viewCatalog;
    }

    @Override
    @SuppressWarnings("unchecked")
    public JaxRsParameterView createParameter(String name, Class<?> parameterType, Parameter parameter, String containingTypeKey,
            InternationalizationProvider i18nProvider) {
        @SuppressWarnings("rawtypes")
        Class<Enum> enumType = (Class<Enum>) parameterType;
        JaxRsResourceView listResource = viewForType(enumType).getOperation("list");
        return new JaxRsListParameterView(i18nProvider, containingTypeKey, name, AttributeType.ENUMERATION, parameter, listResource.getUri(),
                "basic.enumerationConstant");
    }

    private <T extends Enum<T>> JaxRsEnumPresenter viewForType(Class<T> enumType) {
        JaxRsEnumPresenter view = viewCatalog.getView(JaxRsEnumPresenter.class);
        view = view.forEnum(enumType);
        return view;
    }

    @Override
    public Class<?> getType() {
        return Enum.class;
    }

}
