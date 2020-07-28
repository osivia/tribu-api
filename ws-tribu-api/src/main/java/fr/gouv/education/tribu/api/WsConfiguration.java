package fr.gouv.education.tribu.api;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.nuxeo.ecm.automation.client.jaxrs.impl.HttpAutomationClient;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.google.common.collect.Lists;

import fr.gouv.education.tribu.api.repo.NuxeoPoolConfig;
import fr.gouv.education.tribu.api.repo.NuxeoPoolFactory;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Configuration webapp
 * @author Loïc Billon
 *
 */
@Configuration
@ComponentScan(basePackages = { "fr.gouv.education.tribu.api" })
@EnableSwagger2
public class WsConfiguration implements ApplicationContextAware {

    public static final String AUTHORIZATION_HEADER = "Authorization";
	private ApplicationContext applicationContext;
    
    
    @Bean
    public Docket api() {
        Docket docket =  new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.basePackage("fr.gouv.education.tribu.api"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(metaData())
                .securitySchemes(Lists.newArrayList(apiKey()))
                .securityContexts(Arrays.asList(securityContext()));
                
        return docket;
    }
    
    private ApiInfo metaData() {
        return new ApiInfoBuilder().title("API Tribu")
                .description("Api accès nuxeo Tribu")
                .version("1.0.0").license("General Public License")
                .licenseUrl("https://www.gnu.org/licenses/gpl-3.0.fr.html\"").build();
    }
 
    private ApiKey apiKey() {
        return new ApiKey("JWT", AUTHORIZATION_HEADER, "header");
    }
    
    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope
            = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
       return Lists.newArrayList(
            new SecurityReference("JWT", authorizationScopes));
    
    }
    
    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(defaultAuth())
            .forPaths(PathSelectors.any()).build();
        }

    
    @Bean("pool")
    public GenericObjectPool<HttpAutomationClient> getPool() {
    
		PooledObjectFactory<HttpAutomationClient> factory = applicationContext.getBean(NuxeoPoolFactory.class);
				
		GenericObjectPoolConfig<HttpAutomationClient> config = new NuxeoPoolConfig();
		
		GenericObjectPool<HttpAutomationClient> pool = new GenericObjectPool<HttpAutomationClient>(factory, config);
		
		return pool;
    }

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
		
	}
	
    
}