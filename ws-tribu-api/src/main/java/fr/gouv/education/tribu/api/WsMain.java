package fr.gouv.education.tribu.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Import;

import fr.gouv.education.tribu.api.security.WsSecurityConfiguration;

/**
 * Starter webapp
 * @author Loïc Billon
 *
 */
@SpringBootApplication
@Import({WsConfiguration.class, WsDevConfiguration.class, WsProdConfiguration.class,
        WsSecurityConfiguration.class, DirectoryFrameworkConfiguration.class})
public class WsMain  extends SpringBootServletInitializer {

    public static void main(String[] args) {
    	SpringApplication.run(WsMain.class, args);
    }

}
