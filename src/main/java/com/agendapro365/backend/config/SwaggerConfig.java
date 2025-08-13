package com.agendapro365.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class SwaggerConfig {

   @Bean
   public OpenAPI customOpenAPI() {
    return new OpenAPI()
          .info(new Info()
              .title("AgendaPro365 API")
              .version("v1.0")
              .description("API para gestion de citas, usuario, pagos y mas")
              .contact(new Contact()
                  .name("Emmanuel Martinez")
                  .email("enmanuelmartinez89@yahoo.es")
                  .url("https://tuwebpersonal.com"))
               .license(new License()
                  .name("Apache 2.0")
                  .url("https://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                   .description("Repositorio Github")
                   .url("https://github.com/tuusuario/AgendaPro365") 
                );
   }
}
