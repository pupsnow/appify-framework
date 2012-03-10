package be.appify.view.jaxrs.parameter;

import java.net.URI;

import javax.xml.bind.annotation.XmlElement;

import be.appify.i18n.InternationalizationProvider;

public class JaxRsListParameterView extends JaxRsParameterView {

    private final URI listUri;
    private final String referencedType;

    public JaxRsListParameterView(InternationalizationProvider i18nProvider, String containingTypeKey, String name, String type, Parameter parameter,
            URI listUri, String referencedType) {
        super(i18nProvider, containingTypeKey, name, type, parameter);
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
