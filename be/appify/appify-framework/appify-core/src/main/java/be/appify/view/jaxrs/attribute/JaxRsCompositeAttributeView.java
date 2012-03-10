package be.appify.view.jaxrs.attribute;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import be.appify.i18n.InternationalizationProvider;

public class JaxRsCompositeAttributeView extends JaxRsAttributeView {

    private final List<JaxRsAttributeView> components;

    public JaxRsCompositeAttributeView(InternationalizationProvider i18nProvider, String containingTypeKey, String name, String type, Attribute attribute,
            List<JaxRsAttributeView> components) {
        super(i18nProvider, containingTypeKey, name, type, attribute);
        this.components = components;
    }

    @XmlElement
    public List<JaxRsAttributeView> getComponents() {
        return components;
    }

}
