package be.appify.view.jaxrs.parameter;

import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang.Validate;

import be.appify.i18n.InternationalizationProvider;
import be.appify.view.jaxrs.ActiveJaxRsView;
import be.appify.view.jaxrs.ActiveViewCatalog;
import be.appify.view.jaxrs.JaxRsResourceView;
import be.appify.view.jaxrs.TypeView;
import be.appify.view.jaxrs.attribute.AttributeType;

public class ListParameterViewFactory implements NamedParameterViewFactory, TypeSpecificParameterViewFactory {
    private final ActiveViewCatalog viewCatalog;

    @Inject
    public ListParameterViewFactory(ActiveViewCatalog viewCatalog) {
        this.viewCatalog = viewCatalog;
    }

    @Override
    public String getName() {
        return AttributeType.LIST;
    }

    @Override
    public JaxRsParameterView createParameter(String name, Class<?> parameterType, Parameter parameter, String containingTypeKey,
            InternationalizationProvider i18nProvider) {
        Class<?> referencedType = parameter.referencedType();
        Validate.notNull(referencedType, "Missing referencedType on Parameter annotation on a list parameter " + containingTypeKey + "." + name);
        TypeView typeView = referencedType.getAnnotation(TypeView.class);
        Validate.notNull(typeView, "Missing TypeView annotation on type view " + referencedType.getClass().getName());
        String typeName = typeView.namespace() + "." + typeView.type();
        ActiveJaxRsView view = viewCatalog.getPresenter(typeView.namespace(), typeView.type());
        JaxRsResourceView listResource = view.getOperation("list");
        Validate.notNull(listResource, "Cannot refer to a type " + typeName + " without list operation.");
        return new JaxRsListParameterView(i18nProvider, containingTypeKey, name, AttributeType.LIST, parameter, listResource.getUri(), typeName);
    }

    @Override
    public Class<?> getType() {
        return List.class;
    }

}
