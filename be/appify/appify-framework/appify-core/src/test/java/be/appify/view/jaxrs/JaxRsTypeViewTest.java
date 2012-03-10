package be.appify.view.jaxrs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import be.appify.i18n.InternationalizationProvider;
import be.appify.view.jaxrs.ActiveJaxRsView;
import be.appify.view.jaxrs.JaxRsResourceView;
import be.appify.view.jaxrs.JaxRsTypeView;
import be.appify.view.jaxrs.TypePresenter;
import be.appify.view.jaxrs.TypeView;
import be.appify.view.jaxrs.attribute.Attribute;
import be.appify.view.jaxrs.attribute.AttributeType;
import be.appify.view.jaxrs.attribute.AutoAttributeViewFactory;
import be.appify.view.jaxrs.attribute.DelegatingAttributeViewFactory;
import be.appify.view.jaxrs.attribute.IntegerAttributeViewFactory;
import be.appify.view.jaxrs.attribute.JaxRsAttributeView;
import be.appify.view.jaxrs.attribute.NamedAttributeViewFactory;
import be.appify.view.jaxrs.attribute.TextAttributeViewFactory;
import be.appify.view.jaxrs.attribute.TypeSpecificAttributeViewFactory;

public class JaxRsTypeViewTest {

    @Mock
    InternationalizationProvider i18nProvider;
    private List<NamedAttributeViewFactory> attributeViewFactories;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        Collection<TypeSpecificAttributeViewFactory> typeSpecificFactories = new HashSet<TypeSpecificAttributeViewFactory>();
        typeSpecificFactories.add(new TextAttributeViewFactory());
        typeSpecificFactories.add(new IntegerAttributeViewFactory());
        AutoAttributeViewFactory auto = new AutoAttributeViewFactory();
        auto.setTypeSpecificFactories(typeSpecificFactories);
        attributeViewFactories = new ArrayList<NamedAttributeViewFactory>();
        attributeViewFactories.add(auto);
    }

    @Test
    @Ignore
    // outdated
    public void shouldExtractAttributesFromParameterObject() {
        ActiveJaxRsView view = new ViewWithParameterObject();
        JaxRsTypeView typeView = new JaxRsTypeView(view, i18nProvider, new DelegatingAttributeViewFactory(new InstanceDecorator<NamedAttributeViewFactory>(
                attributeViewFactories)));
        List<JaxRsAttributeView> attributes = typeView.getAttributes();
        assertNotNull(attributes);
        assertEquals(2, attributes.size());
        JaxRsAttributeView attribute = attributes.get(0);
        assertEquals("someString", attribute.getName());
        assertEquals(AttributeType.URL, attribute.getType());
        attribute = attributes.get(1);
        assertEquals("anInteger", attribute.getName());
        assertEquals(AttributeType.INTEGER, attribute.getType());
    }

    @TypePresenter(ViewWithParameterObject.View.class)
    static class ViewWithParameterObject implements ActiveJaxRsView {

        @POST
        public void methodWithParameterObject(ParameterObject parameter) {
        }

        @Override
        public List<JaxRsResourceView> getOperations() {
            return Collections.emptyList();
        }

        @Override
        public JaxRsResourceView getOperation(String name) {
            return null;
        }

        @TypeView(namespace = "test", type = "withParameterObject")
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

    static class ParameterObject {
        private String someString;
        private int anInteger;

        public String getSomeString() {
            return someString;
        }

        @FormParam("someString")
        @Attribute(type = AttributeType.URL, order = 1)
        public void setSomeString(String someString) {
            this.someString = someString;
        }

        public int getAnInteger() {
            return anInteger;
        }

        @FormParam("anInteger")
        @Attribute(order = 2)
        public void setAnInteger(int anInteger) {
            this.anInteger = anInteger;
        }

    }

}
