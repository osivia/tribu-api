package fr.gouv.education.tribu.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Import;

/**
 * Starter webapp
 * @author Loïc Billon
 *
 */
@SpringBootApplication
@Import({WsConfiguration.class})
public class WsMain  extends SpringBootServletInitializer {

    public static void main(String[] args) {
    	SpringApplication.run(WsMain.class, args);
    }

}
