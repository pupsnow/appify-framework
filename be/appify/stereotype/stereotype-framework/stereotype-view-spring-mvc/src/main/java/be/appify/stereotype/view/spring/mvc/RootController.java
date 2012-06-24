package be.appify.stereotype.view.spring.mvc;

import java.util.Collection;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import be.appify.stereotype.core.beans.BeanModel;
import be.appify.stereotype.core.beans.BeanModelRegistry;

import com.google.common.collect.Maps;

@Named
@Controller
public class RootController {
	private final BeanModelRegistry beanModelRegistry;

	@Inject
	public RootController(BeanModelRegistry beanModelRegistry) {
		this.beanModelRegistry = beanModelRegistry;
	}

	@RequestMapping("/")
	public Map<String, Collection<BeanModel<?>>> get() {
		Map<String, Collection<BeanModel<?>>> model = Maps.newHashMap();
		model.put("beanModels", beanModelRegistry.getAllRegisteredBeanModels());
		return model;
	}
}
