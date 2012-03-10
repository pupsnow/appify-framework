package be.appify.view.jaxrs;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import be.appify.i18n.InternationalizationProvider;
import be.appify.repository.EnumRepository;
import be.appify.view.jaxrs.parameter.DelegatingParameterViewFactory;

@Path("/{namespace}/{type}")
@RequestScoped
public class JaxRsEnumPresenter extends AbstractActiveJaxRsView {

    private Class<? extends Enum<?>> enumType;
    private EnumRepository enumRepository;

    public JaxRsEnumPresenter() {
    }

    @Inject
    public void setEnumRepository(EnumRepository enumRepository) {
        this.enumRepository = enumRepository;
    }

    public JaxRsEnumPresenter(Class<? extends Enum<?>> enumType, UriInfo uriInfo, InternationalizationProvider i18nProvider, EnumRepository enumRepository,
            DelegatingParameterViewFactory parameterFactory) {
        super(enumType.getAnnotation(TypeView.class).namespace(), enumType.getAnnotation(TypeView.class).type());
        this.enumType = enumType;
        this.enumRepository = enumRepository;
        setTypeName(enumType.getAnnotation(TypeView.class).namespace() + "." + enumType.getAnnotation(TypeView.class).type());
        enumRepository.store(enumType);
        setUriInfo(uriInfo);
        setInternationalizationProvider(i18nProvider);
        setParameterFactory(parameterFactory);
    }

    @GET
    @Operation(order = 1)
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public List<JaxRsEnumConstantView> list(@PathParam("namespace") String namespace, @PathParam("type") String type) {
        enumType = enumRepository.find(namespace, type);
        List<JaxRsEnumConstantView> constantViews = new ArrayList<JaxRsEnumConstantView>();
        for (Enum<?> constant : enumType.getEnumConstants()) {
            constantViews.add(new JaxRsEnumConstantView(constant, getInternationalizationProvider()));
        }
        return constantViews;
    }

    public JaxRsEnumPresenter forEnum(Class<? extends Enum<?>> enumType) {
        return new JaxRsEnumPresenter(enumType, getUriInfo(), getInternationalizationProvider(), enumRepository, getParameterFactory());
    }

}
