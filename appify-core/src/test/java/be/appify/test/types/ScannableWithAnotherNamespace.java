package be.appify.test.types;

import java.util.List;

import be.appify.view.jaxrs.AbstractActiveJaxRsView;
import be.appify.view.jaxrs.ActiveJaxRsView;
import be.appify.view.jaxrs.JaxRsResourceView;
import be.appify.view.jaxrs.TypePresenter;
import be.appify.view.jaxrs.TypeView;

@TypePresenter(ScannableWithAnotherNamespace.View.class)
public class ScannableWithAnotherNamespace extends AbstractActiveJaxRsView {
    @TypeView(type = "scannable", namespace = "another")
    static class View implements ActiveJaxRsView {

        @Override
        public List<JaxRsResourceView> getOperations() {
            return null;
        }

        @Override
        public JaxRsResourceView getOperation(String name) {
            return null;
        }

    }
}
