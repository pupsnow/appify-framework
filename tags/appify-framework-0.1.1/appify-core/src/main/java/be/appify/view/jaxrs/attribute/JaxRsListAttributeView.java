package be.appify.view.jaxrs.attribute;

import java.net.URI;

import javax.xml.bind.annotation.XmlElement;

import be.appify.i18n.InternationalizationProvider;

public class JaxRsListAttributeView extends JaxRsAttributeView {

    private final URI listUri;
    private final String referencedType;

    public JaxRsListAttributeView(InternationalizationProvider i18nProvider, String containingTypeKey, String name, String type, Attribute attribute,
            URI listUri,
            String referencedType) {
        super(i18nProvider, containingTypeKey, name, type, attribute);
        this.listUri = listUri;
        this.referencedType = referencedType;
    }

    @XmlElement
    public URI getListUri() {
        return listUri;
    }

    @XmlElement
    public String getReferencedType() {
        return referencedType;
    }

}
