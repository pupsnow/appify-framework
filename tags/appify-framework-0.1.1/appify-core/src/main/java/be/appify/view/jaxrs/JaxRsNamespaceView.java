package be.appify.view.jaxrs;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import be.appify.i18n.InternationalizationProvider;
import be.appify.view.jaxrs.attribute.DelegatingAttributeViewFactory;

@XmlRootElement(name = "namespace")
public class JaxRsNamespaceView {
    private static final String NAMESPACE_MESSAGE_PREFIX = "namespace.";

    private final String name;
    private final InternationalizationProvider i18nProvider;
    private final List<JaxRsTypeView> types;

    public JaxRsNamespaceView(String name, List<ActiveJaxRsView> types, InternationalizationProvider i18nProvider,
            DelegatingAttributeViewFactory attributeViewFactory) {
        this.name = name;
        this.types = new ArrayList<JaxRsTypeView>();
        for (ActiveJaxRsView type : types) {
            this.types.add(new JaxRsTypeView(type, i18nProvider, attributeViewFactory));
        }
        this.i18nProvider = i18nProvider;
    }

    @XmlElement
    public String getName() {
        return name;
    }

    @XmlElement
    public String getDescription() {
        return i18nProvider.getMessage(NAMESPACE_MESSAGE_PREFIX + name);
    }

    @XmlElement
    public List<JaxRsTypeView> getTypes() {
        return new ArrayList<JaxRsTypeView>(types);
    }
}
