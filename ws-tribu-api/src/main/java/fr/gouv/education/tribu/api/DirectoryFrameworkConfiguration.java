package fr.gouv.education.tribu.api;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.ldap.core.ContextSource;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.odm.core.impl.DefaultObjectDirectoryMapper;
import org.springframework.ldap.odm.typeconversion.impl.ConversionServiceConverterManager;
import org.springframework.ldap.odm.typeconversion.impl.ConversionServiceConverterManager.StringToNameConverter;
import org.springframework.ldap.pool2.factory.PoolConfig;
import org.springframework.ldap.pool2.factory.PooledContextSource;
import org.springframework.ldap.pool2.validation.DefaultDirContextValidator;
import org.springframework.ldap.transaction.compensating.manager.ContextSourceTransactionManager;
import org.springframework.ldap.transaction.compensating.manager.ContextSourceTransactionManagerDelegate;
import org.springframework.ldap.transaction.compensating.manager.TransactionAwareContextSourceProxy;
import org.springframework.ldap.transaction.compensating.support.DefaultTempEntryRenamingStrategy;

import fr.gouv.education.tribu.api.directory.dao.converter.DateToGeneralizedTime;
import fr.gouv.education.tribu.api.directory.dao.converter.GeneralizedTimeToDate;


/**
 * Config directory
 * @author Loïc Billon
 */
@Configuration
public class DirectoryFrameworkConfiguration {


	private ApplicationContext context;
	
	public static final String BASE_DN_CONF = "${ldap.base:dc=osivia,dc=org}";
	
	@Value("${ldap.url}")
	private String ldapUrl;
	
	@Value("${ldap.manager.dn}")
	private String ldapManagerDn;
	
	@Value("${ldap.manager.pswd}")
	private String ldapManagerPswd;
	
	@Value("${ldap.timeout:10000}")
	private String ldapTimeout;
	
	// ----- pooling config ----- 
	@Value("${ldap.pool.testOnBorrow:true}")
	private Boolean testOnBorrow;
	@Value("${ldap.pool.testWhileIdle:true}")
	private Boolean testWhileIdle;	
	@Value("${ldap.pool.minEvictableIdleTimeMillis:600000}")
	private Long minEvictableIdleTimeMillis;		
	@Value("${ldap.pool.timeBetweenEvictionRunsMillis:300000}")
	private Long timeBetweenEvictionRunsMillis;

	// ----- end pooling config -----
	
	public DirectoryFrameworkConfiguration(ApplicationContext context) {
		this.context = context;
		
	}
	
	
	@Bean(name="contextSourceTransactionAwareProxy")
	public TransactionAwareContextSourceProxy txProxy() {
		
		LdapContextSource source = new LdapContextSource();
		source.setUrl(ldapUrl);
				
		source.setUserDn(ldapManagerDn);
		source.setPassword(ldapManagerPswd);
		source.setPooled(false);
		Map<String, Object> baseEnvironmentProperties = new HashMap<String, Object>();
		baseEnvironmentProperties.put("com.sun.jndi.ldap.connect.timeout", ldapTimeout);
		source.setBaseEnvironmentProperties(baseEnvironmentProperties);
		source.afterPropertiesSet();	
		
		
		PooledContextSource configurePooling = configurePooling(source);
		
		return new TransactionAwareContextSourceProxy(configurePooling);
	}
	

	/**
	 * Pooling configuration
	 */
	private PooledContextSource configurePooling(LdapContextSource source) {
		PoolConfig poolConfig = new PoolConfig();
		
		poolConfig.setTestOnBorrow(testOnBorrow);
		poolConfig.setTestWhileIdle(testWhileIdle);
		poolConfig.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
		poolConfig.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis );
		
		// Enable pooling
		PooledContextSource pcs = new PooledContextSource(poolConfig);
		DefaultDirContextValidator defaultDirContextValidator = new DefaultDirContextValidator();
		
		pcs.setContextSource(source);
		pcs.setDirContextValidator(defaultDirContextValidator);

		return pcs;
	}	
	
    /**
     * Get LDAP template
     * 
     * @param contextSource context source
     * @param objectDirectoryMapper object directory mapper
     * @return LDAP template
     */
	@Bean(name="ldapTemplate")
	@Primary
	public LdapTemplate getLdapTemplate(TransactionAwareContextSourceProxy contextSource) {
        
        
        GenericConversionService conversionService = new DefaultConversionService();
        conversionService.addConverter(new StringToNameConverter());
        conversionService.addConverter(context.getBean(GeneralizedTimeToDate.class));
        conversionService.addConverter(context.getBean(DateToGeneralizedTime.class));
    	
        ConversionServiceConverterManager converterManager = new ConversionServiceConverterManager(conversionService);
        
        DefaultObjectDirectoryMapper objectDirectoryMapper = new DefaultObjectDirectoryMapper();
		objectDirectoryMapper.setConverterManager(converterManager);

		LdapTemplate ldapTemplate = new LdapTemplate(contextSource);
        ldapTemplate.setObjectDirectoryMapper(objectDirectoryMapper);
        return ldapTemplate;
	}
	

//	@Bean(name="authenticateLdapTemplate")
//	public LdapTemplate getAuthenticateLdapTemplate() {
//		
//		LdapContextSource source = new LdapContextSource();
//		source.setUrl(ldapUrl);
//		
//		source.setPooled(false);
//		Map<String, Object> baseEnvironmentProperties = new HashMap<String, Object>();
//		baseEnvironmentProperties.put("com.sun.jndi.ldap.connect.timeout", ldapTimeout);
//		source.setBaseEnvironmentProperties(baseEnvironmentProperties);
//		source.afterPropertiesSet();	
//		
//		return new LdapTemplate(source);
//	}
	
	
	@Bean
	public ContextSourceTransactionManager getTxManager() {
		ContextSourceTransactionManager txManager = new ContextSourceTransactionManager();
		txManager.setContextSource((ContextSource) context.getBean("contextSourceTransactionAwareProxy"));
		txManager.setRenamingStrategy(new DefaultTempEntryRenamingStrategy());		
		
		return txManager;
	}	
	
	/**
	 * For composite TM
	 * @return
	 */
	@Bean(name="ldapTransactionManagerDelegate")
	public ContextSourceTransactionManagerDelegate getTxManagerDelegate() {
		ContextSourceTransactionManagerDelegate txManagerDelegate = new ContextSourceTransactionManagerDelegate();
		txManagerDelegate.setContextSource((ContextSource) context.getBean("contextSourceTransactionAwareProxy"));
		
		return txManagerDelegate;
	}	
}
