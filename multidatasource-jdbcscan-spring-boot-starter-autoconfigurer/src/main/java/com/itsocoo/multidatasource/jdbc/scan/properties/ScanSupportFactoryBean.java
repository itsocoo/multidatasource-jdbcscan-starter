package com.itsocoo.multidatasource.jdbc.scan.properties;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

/**
 * @author wanghaibo
 * @version V1.0
 * @desc
 * @date 2019/4/1 18:35
 */

public class ScanSupportFactoryBean implements FactoryBean<ScanSupportFactory>, InitializingBean, ApplicationListener<ApplicationEvent> {

    private ScanSupportFactory scanSupportFactory;

    private ScanConfigurationProperties scanConfigurationProperties;

    public ScanConfigurationProperties getScanConfigurationProperties() {
        return scanConfigurationProperties;
    }

    public void setScanConfigurationProperties(ScanConfigurationProperties scanConfigurationProperties) {
        this.scanConfigurationProperties = scanConfigurationProperties;
    }

    @Override
    public ScanSupportFactory getObject() throws Exception {
        if (this.scanSupportFactory == null) {
            afterPropertiesSet();
        }

        return this.scanSupportFactory;
    }

    @Override
    public Class<?> getObjectType() {
        return ScanSupportFactory.class;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.scanSupportFactory = new DefaultScanSupportFactory(scanConfigurationProperties);
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {

    }
}
