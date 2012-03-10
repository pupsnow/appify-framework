package be.appify.view.jaxrs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.HttpHeaders;

import org.junit.Test;
import org.mockito.Mockito;

import be.appify.view.jaxrs.AbstractActiveJaxRsView;
import be.appify.view.jaxrs.ActiveJaxRsView;
import be.appify.view.jaxrs.HttpMethod;
import be.appify.view.jaxrs.JaxRsResourceView;
import be.appify.view.jaxrs.Operation;
import be.appify.view.jaxrs.TypePresenter;
import be.appify.view.jaxrs.TypeView;

public class AbstractActiveJaxRsViewTest {

    @Test
    public void shouldExposeMethodsAsResources() throws URISyntaxException {
        SimpleAnnotatedJaxRsView view = new SimpleAnnotatedJaxRsView();
        HttpHeaders headers = Mockito.mock(HttpHeaders.class);
        Mockito.when(headers.getAcceptableLanguages()).thenReturn(Arrays.asList(Locale.ENGLISH));
        view.setHttpHeaders(headers);

        List<JaxRsResourceView> operations = view.getOperations();
        assertNotNull(operations);
        assertEquals(3, operations.size());

        JaxRsResourceView operation1 = operations.get(0);
        assertEquals("Get simple string", operation1.getDescription());
        assertEquals(new URI("/root/simple-string"), operation1.getUri());
        assertEquals(HttpMethod.GET, operation1.getMethod());
        assertTrue(operation1.isDefaultOperation());

        JaxRsResourceView operation2 = operations.get(1);
        assertEquals("Delete it!", operation2.getDescription());
        assertEquals(new URI("/root"), operation2.getUri());
        assertEquals(HttpMethod.DELETE, operation2.getMethod());
        assertFalse(operation2.isDefaultOperation());

        JaxRsResourceView operation3 = operations.get(2);
        assertEquals("Put something", operation3.getDescription());
        assertEquals(new URI("/root"), operation3.getUri());
        assertEquals(HttpMethod.POST, operation3.getMethod());
        assertFalse(operation3.isDefaultOperation());
    }

    @Path("/root")
    @TypePresenter(SimpleAnnotated.class)
    static class SimpleAnnotatedJaxRsView extends AbstractActiveJaxRsView {
        @GET
        public SimpleAnnotatedJaxRsView self() {
            return this;
        }

        @POST
        @Operation(order = 3)
        public void putSomething() {
        }

        @GET
        @Operation(isDefault = true, order = 1)
        @Path("/simple-string")
        public String getSimpleString() {
            return "something simple";
        }

        @DELETE
        @Operation(order = 2)
        public void deleteIt() {
        }
    }

    @TypeView(namespace = "test", type = "simpleAnnotated")
    static class SimpleAnnotated implements ActiveJaxRsView {

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
