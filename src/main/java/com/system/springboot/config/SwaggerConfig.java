package com.system.springboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

//访问地址  http://127.0.0.1:8090/swagger-ui.html
//@Configuration // 告诉Spring容器，这个类是一个配置类
//@EnableSwagger2 // 启用Swagger2功能  swagger2.9版
@EnableOpenApi
public class SwaggerConfig {
    /**
     * 配置Swagger2相关的bean
     */
    @Bean
    public Docket createRestApi() {
//        return new Docket(DocumentationType.SWAGGER_2)  //swagger2.9版
        return new Docket(DocumentationType.OAS_30)
                .groupName("组名")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.system.springboot.controller"))// com包下所有API都交给Swagger2管理
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * 此处主要是API文档页面显示信息
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("演示项目API") // 标题
                .description("演示项目") // 描述
                .termsOfServiceUrl("http://localhost:8080/")     //相关的网址
                .contact(new Contact("后端开发","http://bl.wpeng.ga/","wzpenga@163.com"))    //作者  邮箱等
                .version("1.0") // 版本
                .build();
    }
}


