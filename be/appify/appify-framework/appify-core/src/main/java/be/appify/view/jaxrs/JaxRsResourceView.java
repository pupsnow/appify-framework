package be.appify.view.jaxrs;

import java.net.URI;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import be.appify.view.jaxrs.parameter.JaxRsParameterView;

@XmlType(name = "resource")
public class JaxRsResourceView implements Comparable<JaxRsResourceView> {
    private URI uri;
    private HttpMethod method;
    private boolean defaultOperation;
    private String description;
    private int order;
    private String name;
    private List<JaxRsParameterView> parameters;

    public JaxRsResourceView() {
    }

    public JaxRsResourceView(String name, String description, URI uri, HttpMethod method, int order, boolean defaultOperation,
            List<JaxRsParameterView> parameters) {
        this.name = name;
        this.description = description;
        this.uri = uri;
        this.method = method;
        this.order = order;
        this.defaultOperation = defaultOperation;
        this.parameters = parameters;
    }

    @XmlElement
    public String getName() {
        return name;
    }

    @XmlElement
    public URI getUri() {
        return uri;
    }

    @XmlElement
    public HttpMethod getMethod() {
        return method;
    }

    @XmlElement
    public boolean isDefaultOperation() {
        return defaultOperation;
    }

    @XmlElement
    public String getDescription() {
        return description;
    }

    @XmlElement
    public List<JaxRsParameterView> getParameters() {
        return parameters;
    }

    @Override
    public int compareTo(JaxRsResourceView o) {
        return Integer.valueOf(order).compareTo(o.order);
    }

}