/*
 * Copyright 2020 tuhu.cn All right reserved. This software is the
 * confidential and proprietary information of tuhu.cn ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Tuhu.cn
 */
package com.kytc;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @Author: 何志同
 * @Date: 2020/9/11 13:47
 * @Description:
 **/
@SpringCloudApplication
@EnableEurekaClient
@EnableDiscoveryClient
public class Bootstrap {
    public static void main(String[] args) {
        SpringApplication.run(Bootstrap.class,args);
    }
}