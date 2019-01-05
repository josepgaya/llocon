/**
 * 
 */
package com.josepgaya.llocon.front.config;

import java.io.IOException;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

/**
 * Configuració de Spring MVC.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// ResourceHandlers per Swagger
		registry.addResourceHandler("**/swagger-ui.html").
		addResourceLocations("classpath:/META-INF/resources/"); 
		registry.addResourceHandler("**/webjars/**").
		addResourceLocations("classpath:/META-INF/resources/webjars/");
		// ResourceHandler per Angular, per a que totes les peticions
		// desconegudes passin per l'index.html
		registry.
		addResourceHandler("/**/*").
		addResourceLocations("classpath:/static/").
		resourceChain(true).
		addResolver(new PathResourceResolver() {
			@Override
			protected Resource getResource(
					String resourcePath,
					Resource location) throws IOException {
				Resource requestedResource = location.createRelative(resourcePath);
				if (requestedResource.exists() && requestedResource.isReadable()) {
					return requestedResource;
				} else {
					return new ClassPathResource("/static/index.html");
				}
            }
        });
    }

}
