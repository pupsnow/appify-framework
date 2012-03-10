package be.appify.test;

import java.util.List;

import be.appify.view.jaxrs.ActiveJaxRsView;
import be.appify.view.jaxrs.JaxRsResourceView;
import be.appify.view.jaxrs.TypePresenter;
import be.appify.view.jaxrs.TypeView;

@TypePresenter(ScannableOutsideRootPackage.View.class)
public class ScannableOutsideRootPackage {
    @TypeView(type = "outsideRoot", namespace = "outside")
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
