package com.kytc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.*;

@Primary
@Component
public class GatewaySwaggerResourceProvider implements SwaggerResourcesProvider {
    /**
     * swagger2默认的url后缀
     */
    protected static final String SWAGGER2URL = "/v2/api-docs";

    protected static final String OAS_30_URL = "/v3/api-docs";
 
    /**
     * 网关路由
     */
    @Autowired
    private RouteLocator routeLocator;

    @Autowired
    private GatewayProperties gatewayProperties;
 
    /**
     * 网关应用名称
     */
    @Value("${spring.application.name}")
    private String self;
 
    @Override
    public List<SwaggerResource> get() {
        List<RouteDefinition> ds = gatewayProperties.getRoutes();
        List<SwaggerResource> resources = new ArrayList<>();
        List<String> routeHosts = new ArrayList<>();
        // 获取所有可用的host：serviceId
        routeLocator.getRoutes()
                .filter(route -> route.getUri().getHost() != null)
                .filter(route -> Objects.equals(route.getUri().getScheme(), "lb"))
//                .filter(route -> !self.equals(route.getUri().getHost()))
                .subscribe(route -> routeHosts.add(route.getUri().getHost()));
 
        // 记录已经添加过的server，存在同一个应用注册了多个服务在nacos上
        Set<String> dealed = new HashSet<>();
        routeHosts.forEach(instance -> {
            // 拼接url
            String url = "/" + instance.toLowerCase() + SWAGGER2URL;
            if (!dealed.contains(url)) {
                dealed.add(url);
                SwaggerResource swaggerResource = new SwaggerResource();
                swaggerResource.setUrl(url);
                swaggerResource.setName(instance);
                //swaggerResource.setSwaggerVersion("3.0.3");
                resources.add(swaggerResource);
            }
        });
        return resources;
    }
}