package be.appify.stereotype.view.spring.mvc;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Entity;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.RegexPatternTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.ClassUtils;

import be.appify.stereotype.core.beans.BeanModelRegistry;

import com.google.common.collect.Lists;

@Named
public class ClassPathScanningBeanModelRegistryInitializer implements InitializingBean {

	private ClassPathScanningCandidateComponentProvider componentProvider;
	private String basePackage;
	private final BeanModelRegistry beanModelRegistry;

	@Inject
	public ClassPathScanningBeanModelRegistryInitializer(BeanModelRegistry beanModelRegistry) {
		this.beanModelRegistry = beanModelRegistry;
	}

	@Value("${beanLocation}")
	public void setBeanLocation(String beanLocation) {
		String[] locations = beanLocation.split("\\s*[\\s,;]\\s*");
		componentProvider = new ClassPathScanningCandidateComponentProvider(false);
		StringBuilder patternBuilder = new StringBuilder();
		for (String location : locations) {
			basePackage = highestPackage(basePackage, location);
			if (patternBuilder.length() > 0) {
				patternBuilder.append("|");
			}
			patternBuilder.append(location.replaceAll("\\.", "\\\\.").replaceAll("\\*", "\\.*"));
		}
		final TypeFilter filter1 = new RegexPatternTypeFilter(Pattern.compile(patternBuilder.toString()));
		final TypeFilter filter2 = new AnnotationTypeFilter(Entity.class);
		componentProvider.addIncludeFilter(new TypeFilter() {

			@Override
			public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory)
					throws IOException {
				return filter1.match(metadataReader, metadataReaderFactory) &&
						filter2.match(metadataReader, metadataReaderFactory);
			}

		});
	}

	private String highestPackage(String... packages) {
		String highestPackage = null;
		for (String p : packages) {
			if (p != null) {
				p = stripAsterisks(p);
			}
			if (highestPackage == null) {
				highestPackage = p;
			} else {
				highestPackage = findCommonRootPackage(highestPackage, p);
			}
		}
		if (highestPackage == null) {
			highestPackage = "";
		}
		return highestPackage;
	}

	private String findCommonRootPackage(String package1, String package2) {
		if (package1.startsWith(package2)) {
			return package2;
		} else if (package2.startsWith(package1)) {
			return package1;
		} else {
			String current = "";
			StringBuilder tentative = new StringBuilder("");
			for (String part : package1.split("\\.")) {
				if (tentative.length() > 0) {
					tentative.append(".");
				}
				tentative.append(part);
				String prefix = tentative.toString();
				if (package2.startsWith(prefix)) {
					current = prefix;
				} else {
					break;
				}
			}
			return current;
		}
	}

	private String stripAsterisks(String p) {
		if (p.contains("*")) {
			String stripped = StringUtils.substringBefore(p, "*");
			if (stripped.endsWith(".")) {
				stripped = StringUtils.substringBeforeLast(stripped, ".");
			}
			return stripped;
		}
		return p;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		List<Class<?>> beanClasses = Lists.newArrayList();
		for (BeanDefinition beanDefinition : componentProvider.findCandidateComponents(basePackage)) {
			beanClasses.add(ClassUtils.resolveClassName(beanDefinition.getBeanClassName(),
					ClassUtils.getDefaultClassLoader()));
		}
		beanModelRegistry.initialize(beanClasses.toArray(new Class<?>[beanClasses.size()]));
	}
}
