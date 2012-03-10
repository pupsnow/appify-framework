package be.appify.view.jaxrs;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "language")
public class JaxRsLanguageView {

	private String code;

	public JaxRsLanguageView(String code) {
		this.code = code;
	}

	@XmlElement
	public String getCode() {
		return code;
	}

}
