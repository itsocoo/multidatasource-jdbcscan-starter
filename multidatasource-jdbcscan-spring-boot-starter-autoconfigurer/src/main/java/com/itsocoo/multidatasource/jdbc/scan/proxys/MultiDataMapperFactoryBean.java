package com.itsocoo.multidatasource.jdbc.scan.proxys;

import com.itsocoo.multidatasource.jdbc.scan.properties.ScanSupportFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Map;

/**
 * @author wanghaibo
 * @version V1.0
 * @desc 实现接口的工厂方法Bean
 * @date 2018/9/6 17:12
 */
public class MultiDataMapperFactoryBean<T> implements FactoryBean<T>, InitializingBean, ApplicationListener<ApplicationEvent>, ApplicationContextAware {

    private ApplicationContext applicationContext;

    private Class<T> mapperInterface;

    private Map<String, JdbcTemplate> stringJdbcTemplateMap;

    private Map<String, PlatformTransactionManager> stringTransactionManagerMap;

    private boolean addToConfig = true;

    private ScanSupportFactory scanSupportFactory;

    // public MultiDataMapperFactoryBean(Map<String, JdbcTemplate> stringJdbcTemplateMap, ScanSupportFactory scanSupportFactory) {
    //     this.stringJdbcTemplateMap = stringJdbcTemplateMap;
    //     this.scanSupportFactory = scanSupportFactory;
    // }

    public MultiDataMapperFactoryBean() {
        //intentionally empty
    }

    public MultiDataMapperFactoryBean(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T getObject() throws Exception {
        // TODO: 调用动态代理实现类
        MyMapperProxyFactory myMapperProxyFactory = new MyMapperProxyFactory(this.mapperInterface);
        return (T) myMapperProxyFactory.newInstance(stringJdbcTemplateMap, stringTransactionManagerMap, scanSupportFactory.getScanConfigurationProperties());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<T> getObjectType() {
        return this.mapperInterface;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSingleton() {
        return true;
    }

    //------------- mutators --------------

    /**
     * Sets the mapper interface of the MyBatis mapper
     *
     * @param mapperInterface class of the interface
     */
    public void setMapperInterface(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    /**
     * Return the mapper interface of the MyBatis mapper
     *
     * @return class of the interface
     */
    public Class<T> getMapperInterface() {
        return mapperInterface;
    }

    /**
     * If addToConfig is false the mapper will not be added to MyBatis. This means
     * it must have been included in mybatis-config.xml.
     * <p/>
     * If it is true, the mapper will be added to MyBatis in the case it is not already
     * registered.
     * <p/>
     * By default addToCofig is true.
     *
     * @param addToConfig
     */
    public void setAddToConfig(boolean addToConfig) {
        this.addToConfig = addToConfig;
    }

    /**
     * Return the flag for addition into MyBatis config.
     *
     * @return true if the mapper will be added to MyBatis in the case it is not already
     * registered.
     */
    public boolean isAddToConfig() {
        return addToConfig;
    }

    public ScanSupportFactory getScanSupportFactory() {
        return scanSupportFactory;
    }

    public void setScanSupportFactory(ScanSupportFactory scanSupportFactory) {
        this.scanSupportFactory = scanSupportFactory;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        this.stringJdbcTemplateMap = applicationContext.getBeansOfType(JdbcTemplate.class);
        this.stringTransactionManagerMap = applicationContext.getBeansOfType(PlatformTransactionManager.class);
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {

    }

}
