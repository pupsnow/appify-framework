package be.appify.view.jaxrs.attribute;

import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang.Validate;

import be.appify.i18n.InternationalizationProvider;
import be.appify.view.jaxrs.ActiveJaxRsView;
import be.appify.view.jaxrs.ActiveViewCatalog;
import be.appify.view.jaxrs.JaxRsResourceView;
import be.appify.view.jaxrs.TypeView;

public class ListAttributeViewFactory implements NamedAttributeViewFactory, TypeSpecificAttributeViewFactory {
    private final ActiveViewCatalog viewCatalog;

    @Inject
    public ListAttributeViewFactory(ActiveViewCatalog viewCatalog) {
        this.viewCatalog = viewCatalog;
    }

    @Override
    public String getName() {
        return AttributeType.LIST;
    }

    @Override
    public JaxRsAttributeView createAttribute(String name, Class<?> parameterType, Attribute attribute, String containingTypeKey,
            InternationalizationProvider i18nProvider, DelegatingAttributeViewFactory factory) {
        Class<?> referencedType = attribute.referencedType();
        Validate.notNull(referencedType, "Missing referencedType on Attribute annotation on a list attribute " + containingTypeKey + "." + name);
        TypeView typeView = referencedType.getAnnotation(TypeView.class);
        Validate.notNull(typeView, "Missing TypeView annotation on type view " + referencedType.getClass().getName());
        String typeName = typeView.namespace() + "." + typeView.type();
        ActiveJaxRsView view = viewCatalog.getPresenter(typeView.namespace(), typeView.type());
        JaxRsResourceView listResource = view.getOperation("list");
        Validate.notNull(listResource, "Cannot refer to a type " + typeName + " without list operation.");
        return new JaxRsListAttributeView(i18nProvider, containingTypeKey, name, AttributeType.LIST, attribute, listResource.getUri(), typeName);
    }

    @Override
    public Class<?> getType() {
        return List.class;
    }

    @Override
    public JaxRsAttributeView createAttribute(String name, Class<?> parameterType, Attribute attribute, String containingTypeKey,
            InternationalizationProvider i18nProvider) {
        return createAttribute(name, parameterType, attribute, containingTypeKey, i18nProvider, null);
    }

}
