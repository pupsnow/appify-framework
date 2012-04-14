package be.appify.view.jaxrs;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.XmlElement;

import org.apache.commons.lang.Validate;

import be.appify.i18n.FallbackResourceBundleInternationalizationProvider;
import be.appify.i18n.InternationalizationProvider;
import be.appify.util.AnnotationUtil;
import be.appify.util.LazyInitializer;
import be.appify.view.jaxrs.parameter.DelegatingParameterViewFactory;
import be.appify.view.jaxrs.parameter.JaxRsParameterView;

public abstract class AbstractActiveJaxRsView implements ActiveJaxRsView {
    private final LazyInitializer<List<JaxRsResourceView>> operations = new LazyInitializer<List<JaxRsResourceView>>() {

        @Override
        protected List<JaxRsResourceView> initialize() {
            return initializeOperations();
        }
    };
    private UriBuilder builder;
    private InternationalizationProvider i18nProvider;
    private final Object[] values;
    private UriInfo uriInfo;
    private String typeName;
    private DelegatingParameterViewFactory parameterFactory;
	private HttpHeaders headers;

    // CDI needs this constructor.
    AbstractActiveJaxRsView() {
        this(new Object[0]);
    }

    @Inject
    public void setParameterFactory(DelegatingParameterViewFactory parameterFactory) {
        this.parameterFactory = parameterFactory;
    }

    protected DelegatingParameterViewFactory getParameterFactory() {
        return parameterFactory;
    }

    @Context
    public void setUriInfo(UriInfo info) {
    	if(info != null) {
    		setUriBuilder(info.getBaseUriBuilder());
    	}
        this.uriInfo = info;
    }

    public void setUriBuilder(UriBuilder builder) {
        this.builder = builder.path(getClass());
    }

    protected void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    protected UriBuilder getUriBuilder() {
        return builder;
    }

    protected UriInfo getUriInfo() {
        return uriInfo;
    }

    @Context
    public void setHttpHeaders(HttpHeaders headers) {
    	this.headers = headers;
        setInternationalizationProvider(new FallbackResourceBundleInternationalizationProvider(
                headers.getAcceptableLanguages().toArray(new Locale[0])));
    }

    public void setInternationalizationProvider(InternationalizationProvider i18nProvider) {
        this.i18nProvider = i18nProvider;
    }

    protected InternationalizationProvider getInternationalizationProvider() {
        return i18nProvider;
    }

    protected AbstractActiveJaxRsView(Object... values) {
        this.values = Arrays.copyOf(values, values.length);
    }
    
    protected HttpHeaders getHeaders() {
    	return headers;
    }

    @Override
    @XmlElement
    public List<JaxRsResourceView> getOperations() {
        return operations.get();
    }

    @Override
    public JaxRsResourceView getOperation(String name) {
        JaxRsResourceView result = null;
        for (JaxRsResourceView view : getOperations()) {
            if (view.getName().equals(name)) {
                result = view;
            }
        }
        return result;
    }

    private List<JaxRsResourceView> initializeOperations() {
        List<JaxRsResourceView> operations = new ArrayList<JaxRsResourceView>();
        if (this.getClass().isAnnotationPresent(Path.class)) {
            if (builder == null) {
                builder = UriBuilder.fromResource(this.getClass());
            }
            UriBuilder baseUriBuilder = builder.clone();
            URI baseUri = baseUriBuilder.build(values);
            for (Method method : this.getClass().getMethods()) {
                if (method.isAnnotationPresent(Operation.class)) {
                    HttpMethod httpMethod = null;
                    if (method.isAnnotationPresent(HEAD.class)) {
                        httpMethod = HttpMethod.HEAD;
                    } else if (method.isAnnotationPresent(GET.class)) {
                        httpMethod = HttpMethod.GET;
                    } else if (method.isAnnotationPresent(PUT.class)) {
                        httpMethod = HttpMethod.PUT;
                    } else if (method.isAnnotationPresent(DELETE.class)) {
                        httpMethod = HttpMethod.DELETE;
                    } else if (method.isAnnotationPresent(POST.class)) {
                        httpMethod = HttpMethod.POST;
                    }
                    if (httpMethod != null) {
                        UriBuilder b = baseUriBuilder.clone();
                        if (method.isAnnotationPresent(Path.class)) {
                            b = b.path(method);
                        }
                        Operation operationAnnotation = method.getAnnotation(Operation.class);
                        boolean defaultOperation = operationAnnotation.isDefault();
                        URI uri = b.build(values);
                        if (httpMethod != HttpMethod.GET || !uri.equals(baseUri)) {
                            String typeName = getTypeName(this.getClass());
                            if (typeName != null) {
                                Validate.notNull(i18nProvider, "InternationalizationProvider must be set.");
                                String key = "type." + typeName + "." + method.getName();
                                String description = i18nProvider.getMessage(key, values);
                                try {
                                    List<JaxRsParameterView> parameters = extractParameters(method);
                                    operations.add(new JaxRsResourceView(method.getName(), description, uri, httpMethod, operationAnnotation.order(),
                                            defaultOperation, parameters));
                                } catch (RuntimeException e) {
                                    e.printStackTrace();
                                    throw e;
                                }
                            }
                        }
                    }
                }
            }
        }
        Collections.sort(operations);
        return operations;
    }

    private List<JaxRsParameterView> extractParameters(Method method) {
        List<JaxRsParameterView> parameters = new ArrayList<JaxRsParameterView>();
        if (hasParameterObject(method)) {
            parameters.addAll(extractParametersFromParameterObject(method));
        } else {
            parameters.addAll(extractParametersFromMethodParameters(method));
        }
        Collections.sort(parameters);
        return parameters;
    }

    private boolean hasParameterObject(Method method) {
        if (method.getParameterTypes().length == 1) {
            for (Method m : method.getParameterTypes()[0].getMethods()) {
                if (m.isAnnotationPresent(FormParam.class)) {
                    return true;
                }
            }
        }
        return false;
    }

    private List<JaxRsParameterView> extractParametersFromParameterObject(Method method) {
        List<JaxRsParameterView> parameters = new ArrayList<JaxRsParameterView>();
        for (Method m : method.getParameterTypes()[0].getMethods()) {
            if (m.isAnnotationPresent(FormParam.class)) {
                parameters.addAll(extractParameter(m));
            }
        }
        return parameters;
    }

    private List<JaxRsParameterView> extractParametersFromMethodParameters(Method method) {
        List<JaxRsParameterView> parameters = new ArrayList<JaxRsParameterView>();
        for (int index = 0; index < method.getParameterAnnotations().length; index++) {
            parameters.addAll(extractParameter(method, index));
        }
        return parameters;
    }

    private List<JaxRsParameterView> extractParameter(Method method, int index) {
        List<JaxRsParameterView> parameters = new ArrayList<JaxRsParameterView>();
        FormParam formParam = getAnnotation(method.getParameterAnnotations()[index], FormParam.class);
        if (formParam != null) {
            JaxRsParameterView parameter = parameterFactory.createParameter(formParam.value(), method.getParameterAnnotations()[index],
                    method.getParameterTypes()[index], getTypeKey(), i18nProvider);
            if (parameter != null) {
                parameters.add(parameter);
            }
        }
        return parameters;
    }

    private <T extends Annotation> T getAnnotation(Annotation[] annotations, Class<T> annotationType) {
        T result = null;
        for (Annotation annotation : annotations) {
            if (annotationType.isAssignableFrom(annotation.getClass())) {
                result = annotationType.cast(annotation);
                break;
            }
        }
        return result;
    }

    private List<JaxRsParameterView> extractParameter(Method method) {
        List<JaxRsParameterView> parameters = new ArrayList<JaxRsParameterView>();
        FormParam formParam = method.getAnnotation(FormParam.class);
        if (formParam != null) {
            JaxRsParameterView attribute = parameterFactory.createParameter(formParam.value(), method.getAnnotations(),
                    method.getParameterTypes()[0], getTypeKey(), i18nProvider);
            if (attribute != null) {
                parameters.add(attribute);
            }
        }
        return parameters;
    }

    private String getTypeKey() {
        return "type." + getTypeName(this.getClass());
    }

    private String getTypeName(Class<? extends AbstractActiveJaxRsView> type) {
        String typeName = this.typeName;
        if (typeName == null) {
            TypePresenter typePresenter = AnnotationUtil.getAnnotation(this.getClass(), TypePresenter.class);
            TypeView typeView = null;
            if (typePresenter != null) {
                typeView = typePresenter.value().getAnnotation(TypeView.class);
            } else {
                typeView = AnnotationUtil.getAnnotation(this.getClass(), TypeView.class);
            }
            if (typeView != null) {
                typeName = typeView.namespace() + "." + typeView.type();
            }
        }
        return typeName;
    }

    protected void initialize(AbstractActiveJaxRsView copy) {
        copy.i18nProvider = i18nProvider;
        copy.builder = builder;
        copy.parameterFactory = parameterFactory;
    }
}
