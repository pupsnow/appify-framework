package be.appify.view.jaxrs;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

@Path("/admin/languages")
@RequestScoped
public class JaxRsLanguagePresenter {
	
	@GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<JaxRsLanguageView> getLanguages(@Context HttpHeaders headers) {
        List<JaxRsLanguageView> languages = new ArrayList<JaxRsLanguageView>();
		for(Locale locale : headers.getAcceptableLanguages()) {
			String localeString = locale.toString();
			String language = locale.getLanguage();
			if(!localeString.equals(language)) {
				languages.add(new JaxRsLanguageView(localeString));
			}
        	languages.add(new JaxRsLanguageView(language));
        }
        return languages;
    }
}
