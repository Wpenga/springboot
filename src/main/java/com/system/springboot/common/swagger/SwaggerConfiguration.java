package com.system.springboot.common.swagger;

import com.system.springboot.controller.UserController;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

/**
 * Swagger配置
 *
 * @author lgy
 */
@Slf4j
@Configuration
@EnableOpenApi
public class SwaggerConfiguration {

    @Value("${spring.application.name}")
    private String applicationName;


    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.OAS_30)
                .groupName("全部接口")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))  //接口选择
//                .apis(RequestHandlerSelectors.basePackage("com.system.springboot.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }
//    @Bean
//    public Docket userRestApi() {
//        return new Docket(DocumentationType.OAS_30)
//                .groupName("用户接口")
//                .apiInfo(apiInfo())
//                .select()
////                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))  //接口选择
//                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
//                .paths(PathSelectors.any())
//                .build();
//    }
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(applicationName) // 标题
                .description(applicationName + "接口文档。")// 描述
                .termsOfServiceUrl("http://localhost:8080/")     //相关的网址
                .contact(new Contact("吴泽鹏","http://bl.wpeng.ga/","wzpenga@163.com"))    //作者  邮箱等
                .version("1.0") // 版本
                .build();
    }

    @Bean
    public EnumPropertyBuilderPlugin enumPropertyBuilderPlugin() {
        return new EnumPropertyBuilderPlugin();
    }


    @Bean
    public ValidatePropertyBuilderPlugin validatePropertyBuilderPlugin() {
        return new ValidatePropertyBuilderPlugin();
    }

    @Bean
    public SwaggerConsole swaggerConsole() {
        return new SwaggerConsole();
    }
}
