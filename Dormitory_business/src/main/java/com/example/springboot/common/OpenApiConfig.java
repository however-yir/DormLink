package com.example.springboot.common;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI dormLinkOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("DormLink API")
                        .description("DormLink 宿舍管理系统接口文档")
                        .version("v1.0.0")
                        .contact(new Contact().name("DormLink Team").email("maintainer@dormlink.local"))
                        .license(new License().name("MIT").url("https://opensource.org/licenses/MIT"))
                );
    }
}
