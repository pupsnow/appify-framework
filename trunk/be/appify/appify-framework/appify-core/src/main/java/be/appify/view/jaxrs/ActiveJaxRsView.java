package be.appify.view.jaxrs;

import java.util.List;

public interface ActiveJaxRsView {
    List<JaxRsResourceView> getOperations();

    JaxRsResourceView getOperation(String name);

}
