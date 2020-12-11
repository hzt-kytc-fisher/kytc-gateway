/*
 * Copyright 2020 tuhu.cn All right reserved. This software is the
 * confidential and proprietary information of tuhu.cn ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Tuhu.cn
 */
package com.kytc;

import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.cloud.context.scope.refresh.RefreshScope;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.ctrip.framework.apollo.model.ConfigChange;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: 何志同
 * @Date: 2020/11/16 20:12
 * @Description:
 **/
@Component
@Slf4j
public class AppConfig implements ApplicationContextAware {
    ApplicationContext applicationContext;
    @Autowired
    RefreshScope refreshScope;

    @ApolloConfigChangeListener("TEST1.auth")
    public void onChange(ConfigChangeEvent changeEvent) {
        boolean eurekaPropertiesChanged = false;
        for (String changedKey : changeEvent.changedKeys()) {
            if (changedKey.startsWith("authMap")) {
                log.info("===============================================================");
                ConfigChange configChange = changeEvent.getChange(changedKey);
                log.info("changedKey:{} value:{},oldValue:{}",changedKey,changeEvent.getChange(changedKey),configChange.getOldValue());
                eurekaPropertiesChanged = true;
                break;
            }
        }
        //refreshProperties(changeEvent);
        if (eurekaPropertiesChanged) {
            refreshProperties(changeEvent);
        }
    }
    public void refreshProperties(ConfigChangeEvent changeEvent){
        this.applicationContext.publishEvent(new EnvironmentChangeEvent(changeEvent.changedKeys()));
        refreshScope.refreshAll();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}