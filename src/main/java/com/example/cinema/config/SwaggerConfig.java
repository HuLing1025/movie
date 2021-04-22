package com.example.cinema.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * <p>
 *  配置Swagger2
 * </p>
 *
 * @author huling
 * @since 2020-12-23
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket createRestApiManagement() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                //为当前包路径
                .apis(RequestHandlerSelectors.basePackage("com.example.cinema.controller.management"))
                .paths(PathSelectors.any())
                .build().groupName("management");
    }

    @Bean
    public Docket createRestApiPromotion() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                //为当前包路径
                .apis(RequestHandlerSelectors.basePackage("com.example.cinema.controller.promotion"))
                .paths(PathSelectors.any())
                .build().groupName("promotion");
    }

    @Bean
    public Docket createRestApiRouter() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                //为当前包路径
                .apis(RequestHandlerSelectors.basePackage("com.example.cinema.controller.router"))
                .paths(PathSelectors.any())
                .build().groupName("router");
    }

    @Bean
    public Docket createRestApiSales() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                //为当前包路径
                .apis(RequestHandlerSelectors.basePackage("com.example.cinema.controller.sales"))
                .paths(PathSelectors.any())
                .build().groupName("sales");
    }

    @Bean
    public Docket createRestApiStatistics() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                //为当前包路径
                .apis(RequestHandlerSelectors.basePackage("com.example.cinema.controller.statistics"))
                .paths(PathSelectors.any())
                .build().groupName("statistics");
    }

    @Bean
    public Docket createRestApiUser() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                //为当前包路径
                .apis(RequestHandlerSelectors.basePackage("com.example.cinema.controller.user"))
                .paths(PathSelectors.any())
                .build().groupName("user");
    }

    //构建 api文档的详细信息函数,注意这里的注解引用的是哪个
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                //页面标题
                .title("青瓜电影院购票系统API文档")
                //创建人
                .contact(new Contact("胡令", "https://github.com/HuLing1025/", "huling1025@sina.com"))
                //版本号
                .version("1.0")
                //描述
                .description("青瓜电影院购票系统后端API文档")
                .build();
    }
}