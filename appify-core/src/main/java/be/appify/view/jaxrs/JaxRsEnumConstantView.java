package be.appify.view.jaxrs;

import javax.xml.bind.annotation.XmlElement;

import org.apache.commons.lang.Validate;

import be.appify.i18n.InternationalizationProvider;
import be.appify.view.jaxrs.attribute.Attribute;

public class JaxRsEnumConstantView extends AbstractActiveJaxRsView {

    private final String id;
    private final String description;

    public JaxRsEnumConstantView(Enum<?> constant, InternationalizationProvider i18nProvider) {
        this.id = constant.name();
        TypeView typeView = constant.getClass().getAnnotation(TypeView.class);
        Validate.notNull(typeView, "Missing TypeView annotation on enum type " + constant.getClass().getName());
        this.description = i18nProvider.getMessage("type." + typeView.namespace() + "." + typeView.type() + "." + constant.name());
    }

    @Attribute(order = 1, describing = true)
    @XmlElement
    public String getId() {
        return id;
    }

    @Attribute(order = 2)
    @XmlElement
    public String getDescription() {
        return description;
    }
}
