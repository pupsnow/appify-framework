package be.appify.view.jaxrs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.enterprise.inject.Instance;
import javax.ws.rs.core.HttpHeaders;

import org.junit.Test;
import org.mockito.Mockito;

import be.appify.test.types.AnotherScannable;
import be.appify.test.types.Scannable;
import be.appify.test.types.ScannableWithAnotherNamespace;
import be.appify.view.jaxrs.ActiveJaxRsView;
import be.appify.view.jaxrs.ActiveViewCatalog;
import be.appify.view.jaxrs.JaxRsNamespaceView;
import be.appify.view.jaxrs.JaxRsTypePresenter;
import be.appify.view.jaxrs.JaxRsTypeView;
import be.appify.view.jaxrs.attribute.DelegatingAttributeViewFactory;
import be.appify.view.jaxrs.attribute.NamedAttributeViewFactory;

public class JaxRsTypePresenterTest {
    @Test
    public void shouldTypesGroupedByNamespace() {
        JaxRsTypePresenter presenter = new JaxRsTypePresenter();
        presenter.setAttributeViewFactory(new DelegatingAttributeViewFactory(this.<NamedAttributeViewFactory> toInstance()));
        ActiveViewCatalog viewCatalog = new ActiveViewCatalog();
        viewCatalog.setActiveViews(this.<ActiveJaxRsView> toInstance(new Scannable(), new AnotherScannable(), new ScannableWithAnotherNamespace()));
        presenter.setViewCatalog(viewCatalog);
        HttpHeaders headers = Mockito.mock(HttpHeaders.class);
        Mockito.when(headers.getAcceptableLanguages()).thenReturn(Arrays.asList(Locale.ENGLISH));
        presenter.setHttpHeaders(headers);
        List<JaxRsNamespaceView> namespaces = presenter.getNamespaces();
        assertNotNull(namespaces);
        assertEquals(2, namespaces.size());

        JaxRsNamespaceView namespace = namespaces.get(0);
        assertEquals("Another", namespace.getDescription());
        assertEquals("another", namespace.getName());
        List<JaxRsTypeView> types = namespace.getTypes();
        assertNotNull(types);
        assertEquals(1, types.size());
        assertEquals("scannable", types.get(0).getName());

        namespace = namespaces.get(1);
        assertEquals("Inside", namespace.getDescription());
        assertEquals("inside", namespace.getName());
        types = namespace.getTypes();
        assertNotNull(types);
        assertEquals(2, types.size());
        assertEquals("anotherScannable", types.get(0).getName());
        assertEquals("scannable", types.get(1).getName());
    }

    private <T> Instance<T> toInstance(T... objects) {
        List<T> list = Arrays.asList(objects);
        return new InstanceDecorator<T>(list);
    }
}
