package be.appify.view.jaxrs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.Validate;

import be.appify.i18n.FallbackResourceBundleInternationalizationProvider;
import be.appify.i18n.InternationalizationProvider;
import be.appify.view.jaxrs.attribute.DelegatingAttributeViewFactory;

@Path("/admin/types")
@RequestScoped
public class JaxRsTypePresenter {
    private List<JaxRsNamespaceView> namespaces;
    private InternationalizationProvider i18nProvider;
    private ActiveViewCatalog viewCatalog;
    private DelegatingAttributeViewFactory attributeViewFactory;

    @Inject
    public void setViewCatalog(ActiveViewCatalog viewCatalog) {
        this.viewCatalog = viewCatalog;
    }

    @Inject
    public void setAttributeViewFactory(DelegatingAttributeViewFactory attributeViewFactory) {
        this.attributeViewFactory = attributeViewFactory;
    }

    @Context
    public void setHttpHeaders(HttpHeaders headers) {
        this.i18nProvider = new FallbackResourceBundleInternationalizationProvider(headers.getAcceptableLanguages().toArray(new Locale[0]));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<JaxRsNamespaceView> getNamespaces() {
        if (namespaces == null) {
            synchronized (this) {
                if (namespaces == null) {
                    Validate.notNull(viewCatalog);
                    Validate.notNull(i18nProvider);
                    Validate.notNull(attributeViewFactory);
                    initializeNamespaces();
                }
            }
        }
        return new ArrayList<JaxRsNamespaceView>(namespaces);
    }

    private void initializeNamespaces() {
        Map<String, List<ActiveJaxRsView>> namespaces = viewCatalog.getViewsCategorized();
        this.namespaces = new ArrayList<JaxRsNamespaceView>();
        for (String namespace : namespaces.keySet()) {
            List<ActiveJaxRsView> types = namespaces.get(namespace);
            sortTypes(types);
            this.namespaces.add(new JaxRsNamespaceView(namespace, types, i18nProvider, attributeViewFactory));
        }
        sortNamespaces();
    }

    private void sortNamespaces() {
        Collections.sort(this.namespaces, new Comparator<JaxRsNamespaceView>() {
            @Override
            public int compare(JaxRsNamespaceView ns1, JaxRsNamespaceView ns2) {
                return ns1.getName().compareTo(ns2.getName());
            }
        });
    }

    private void sortTypes(List<ActiveJaxRsView> types) {
        Collections.sort(types, new Comparator<ActiveJaxRsView>() {
            @Override
            public int compare(ActiveJaxRsView tp1, ActiveJaxRsView tp2) {
                TypePresenter t1 = tp1.getClass().getAnnotation(TypePresenter.class);
                TypePresenter t2 = tp2.getClass().getAnnotation(TypePresenter.class);
                String type1 = getType(t1);
                String type2 = getType(t2);
                return type1.compareTo(type2);
            }

            private String getType(TypePresenter t) {
                TypeView typeView = t.value().getAnnotation(TypeView.class);
                Validate.notNull(typeView, "Missing TypeView annotation on " + t.value());
                return typeView.type();
            }
        });
    }
}
