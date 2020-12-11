package com.kytc;
 
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.util.Pair;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.List;
import java.util.UUID;

/**
 * gateway全局过滤器
 * https://blog.csdn.net/forezp/article/details/85057268
 */
@Slf4j
@Component
public class TokenFilter implements GlobalFilter, Ordered {
    @Autowired
    private ApolloConfig apolloConfig;
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("{}",apolloConfig.getAuthMap());
        String path = exchange.getRequest().getPath().value();
        List<String> traceIdList = exchange.getRequest().getHeaders().get("x-trace-id");
        String traceId = UUID.randomUUID().toString();
        if(!CollectionUtils.isEmpty(traceIdList)){
            traceId = traceIdList.get(0);
        }
        MDC.put("traceId",traceId);
        log.info("path:{}",path);
        ServerHttpRequest host = exchange.getRequest().mutate().header("x-trace-id",traceId).build();
        ServerWebExchange build = exchange.mutate().request(host).build();
        return chain.filter(build);
    }
 
    @Override
    public int getOrder() {
        return -100;
    }

    private Tuple2<String,String> calcRequest(String path){
        if(StringUtils.isEmpty(path)){
            return Pair.of("","")
        }
    }
}