package be.appify.view.jaxrs.parameter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import be.appify.i18n.InternationalizationProvider;

@XmlType(name = "parameter")
public class JaxRsParameterView implements Comparable<JaxRsParameterView> {

    private String descriptionKey;
    private String name;
    private String type;
    private InternationalizationProvider i18nProvider;
    private int order;
    private boolean required;

    public JaxRsParameterView() {
    }

    public JaxRsParameterView(InternationalizationProvider i18nProvider, String containingTypeKey, String name, String type, Parameter parameter) {
        this.i18nProvider = i18nProvider;
        this.descriptionKey = containingTypeKey + "." + name;
        this.name = name;
        this.type = type;
        this.order = parameter.order();
        this.required = parameter.required();
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
    public boolean isRequired() {
        return required;
    }

    @Override
    public int compareTo(JaxRsParameterView o) {
        return Integer.valueOf(order).compareTo(o.order);
    }

}
