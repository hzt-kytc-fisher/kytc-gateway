
package com.kytc;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Author: 何志同
 * @Date: 2020/9/11 13:47
 * @Description:
 **/
@SpringCloudApplication
@EnableDiscoveryClient
@EnableApolloConfig
public class Bootstrap {
    public static void main(String[] args) {
        SpringApplication.run(Bootstrap.class,args);
    }
}