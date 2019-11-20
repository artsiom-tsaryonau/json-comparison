package com.assignment.diff.jsoncomparison;

import com.google.common.base.Predicates;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.io.IOException;
import java.io.InputStreamReader;

@EnableSwagger2
@SpringBootApplication
public class JsonComparisonApplication {
    /**
     * Main method.
     * @param args args
     */
    public static void main(String[] args) {
        SpringApplication.run(JsonComparisonApplication.class, args);
    }

    /**
     * Swagger bean initializer.
     * @return bean
     */
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
            .useDefaultResponseMessages(false)
            .select()
            .apis(Predicates.not(RequestHandlerSelectors.basePackage("org.springframework.boot")))
            .paths(PathSelectors.any())
            .build();
    }

    /**
     * Configures default index page to redirect to /swagger-ui.html.
     * @return bean
     */
    @Bean
    public WebMvcConfigurer index() {
        return new WebMvcConfigurer() {
            @Override
            public void addViewControllers(ViewControllerRegistry registry) {
                registry.addRedirectViewController("/", "/swagger-ui.html");
            }
        };
    }

    /**
     * Instantiates database.
     * @param client client
     * @return command line runner
     */
    @Bean
    public CommandLineRunner startup(DatabaseClient client) {
        return (args) -> client
            .execute(() -> {
                var resource = new ClassPathResource("ddl/script.sql");
                try (var is = new InputStreamReader(resource.getInputStream())) {
                    return FileCopyUtils.copyToString(is);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            })
            .then()
            .block();
    }
}
