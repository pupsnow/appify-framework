package be.appify.view.jaxrs.attribute;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import be.appify.i18n.InternationalizationProvider;

@XmlType(name = "attribute")
public class JaxRsAttributeView implements Comparable<JaxRsAttributeView> {

    private String descriptionKey;
    private String name;
    private String type;
    private InternationalizationProvider i18nProvider;
    private int order;
    private boolean describing;

    public JaxRsAttributeView() {
    }

    public JaxRsAttributeView(InternationalizationProvider i18nProvider, String containingTypeKey, String name, String type, Attribute attribute) {
        this.i18nProvider = i18nProvider;
        this.descriptionKey = containingTypeKey + "." + name;
        this.name = name;
        this.type = type;
        this.order = attribute.order();
        this.describing = attribute.describing();
    }

    @XmlElement
    public String getName() {
        return name;
    }

    @XmlElement
    public String getDescription() {
        return i18nProvider.getMessage(descriptionKey);
    }

    @XmlElement
    public String getType() {
        return type;
    }

    @XmlElement
    public boolean isDescribing() {
        return describing;
    }

    @Override
    public int compareTo(JaxRsAttributeView o) {
        return Integer.valueOf(order).compareTo(o.order);
    }

}
