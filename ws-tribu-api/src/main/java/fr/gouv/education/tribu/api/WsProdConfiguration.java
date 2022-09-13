package fr.gouv.education.tribu.api;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.*;
import org.springframework.core.io.UrlResource;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.net.MalformedURLException;

@Configuration
@Conditional(WsProdConfiguration.Condition.class)
@ImportResource({"file:${catalina.base}/conf/api-users.xml"})
@PropertySource({"file:${catalina.base}/conf/application.properties"})
public class WsProdConfiguration implements ApplicationContextAware {

    static class Condition implements ConfigurationCondition {
        @Override
        public ConfigurationPhase getConfigurationPhase() {
            return ConfigurationPhase.PARSE_CONFIGURATION;
        }

        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            // only load config in prod environnement
            return !("dev".equals(System.getProperty("spring.profiles.active")));
        }
    }

    private ApplicationContext applicationContext;

    @Value("${catalina.home}")
    private String catalinaHome;

    @Bean
    public EhCacheManagerFactoryBean cacheFactoryBean() throws MalformedURLException {
        EhCacheManagerFactoryBean ehCacheManagerFactoryBean = new EhCacheManagerFactoryBean();
        ehCacheManagerFactoryBean.setConfigLocation(new UrlResource("file:"+catalinaHome+"/conf/ehcache-replicated.xml"));
        return ehCacheManagerFactoryBean;
    }

    @Bean
    public CacheManager cacheManager() {
        EhCacheCacheManager cacheManager = new EhCacheCacheManager();

        EhCacheManagerFactoryBean cacheFactoryBean = applicationContext.getBean(EhCacheManagerFactoryBean.class);

        cacheManager.setCacheManager(cacheFactoryBean.getObject());
        return cacheManager;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
