package be.appify.view.jaxrs;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.Validate;

import be.appify.i18n.InternationalizationProvider;
import be.appify.util.AnnotationUtil;
import be.appify.view.jaxrs.attribute.AttributeExtractor;
import be.appify.view.jaxrs.attribute.DelegatingAttributeViewFactory;
import be.appify.view.jaxrs.attribute.JaxRsAttributeView;

@XmlType(name = "type")
public class JaxRsTypeView {
    private ActiveJaxRsView view;
    private InternationalizationProvider i18nProvider;
    private TypePresenter typePresenter;
    private List<JaxRsAttributeView> attributes;

    public JaxRsTypeView() {
    }

    public JaxRsTypeView(ActiveJaxRsView view, InternationalizationProvider i18nProvider,
            DelegatingAttributeViewFactory attributeViewFactory) {
        this.view = view;
        this.i18nProvider = i18nProvider;
        this.typePresenter = AnnotationUtil.getAnnotation(view.getClass(), TypePresenter.class);
        if (typePresenter == null) {
            throw new IllegalArgumentException("Unable to create type view on " + view.getClass() + ". Missing TypePresenter annotation.");
        }
        TypePresenter typePresenter = AnnotationUtil.getAnnotation(view.getClass(), TypePresenter.class);
        attributes = new AttributeExtractor(typePresenter.value(), i18nProvider, attributeViewFactory).extractAttributes();
    }

    @XmlElement
    public String getName() {
        return getTypeView().type();
    }

    private TypeView getTypeView() {
        TypeView typeView = typePresenter.value().getAnnotation(TypeView.class);
        Validate.notNull(typeView, "Missing TypeView annotation on " + typePresenter.value());
        return typeView;
    }

    @XmlElement
    public String getDescription() {
        return i18nProvider.getMessage(getTypeKey());
    }

    private String getTypeKey() {
        TypeView typeView = getTypeView();
        return "type." + typeView.namespace() + "." + typeView.type();
    }

    @XmlElement
    public String getPlural() {
        return i18nProvider.getMessage(getTypeKey() + ".plural");
    }

    @XmlElement
    public List<JaxRsResourceView> getOperations() {
        return view.getOperations();
    }

    @XmlElement
    public List<JaxRsAttributeView> getAttributes() {
        return new ArrayList<JaxRsAttributeView>(attributes);
    }
}
