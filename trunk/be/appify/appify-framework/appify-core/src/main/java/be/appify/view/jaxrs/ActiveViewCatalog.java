package be.appify.view.jaxrs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.apache.commons.lang.Validate;

import be.appify.util.AnnotationUtil;
import be.appify.util.LazyInitializer;
import be.appify.util.collections.TypeMap;

@RequestScoped
public class ActiveViewCatalog {
    private TypeMap<ActiveJaxRsView> activeViews;
    private final LazyInitializer<Map<String, List<ActiveJaxRsView>>> namespaces =
            new LazyInitializer<Map<String, List<ActiveJaxRsView>>>() {

                @Override
                protected Map<String, List<ActiveJaxRsView>> initialize() {
                    Validate.notNull(activeViews);
                    Map<String, List<ActiveJaxRsView>> namespaces = new HashMap<String, List<ActiveJaxRsView>>();
                    for (ActiveJaxRsView view : activeViews.values()) {
                        Class<?> type = view.getClass();
                        TypePresenter typePresenter = AnnotationUtil.getAnnotation(type, TypePresenter.class);
                        if (typePresenter != null) {
                            TypeView typeView = typePresenter.value().getAnnotation(TypeView.class);
                            if (typeView != null) {
                                String namespace = typeView.namespace();
                                List<ActiveJaxRsView> typesForNamespace = namespaces.get(namespace);
                                if (typesForNamespace == null) {
                                    typesForNamespace = new ArrayList<ActiveJaxRsView>();
                                    namespaces.put(namespace, typesForNamespace);
                                }
                                typesForNamespace.add(view);
                            }
                        }
                    }
                    return namespaces;
                }
            };

    @Inject
    @Any
    public void setActiveViews(Instance<ActiveJaxRsView> activeViews) {
        this.activeViews = new TypeMap<ActiveJaxRsView>();
        for (ActiveJaxRsView view : activeViews) {
            this.activeViews.put(view);
        }
    }

    public Map<String, List<ActiveJaxRsView>> getViewsCategorized() {
        return namespaces.get();
    }

    public <T extends ActiveJaxRsView> T getView(Class<T> type) {
        return activeViews.get(type);
    }

    public ActiveJaxRsView getPresenter(String namespace, String type) {
        ActiveJaxRsView result = null;
        for (ActiveJaxRsView view : activeViews.values()) {
            TypePresenter typePresenter = AnnotationUtil.getAnnotation(view.getClass(), TypePresenter.class);
            if (typePresenter != null) {
                TypeView typeView = typePresenter.value().getAnnotation(TypeView.class);
                if (typeView != null) {
                    if (typeView.namespace().equals(namespace) && typeView.type().equals(type)) {
                        result = view;
                        break;
                    }
                }
            }
        }
        return result;
    }
}
