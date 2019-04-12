package com.itsocoo.multidatasource.jdbc.scan.proxys;

import com.itsocoo.multidatasource.jdbc.scan.properties.ScanConfigurationProperties;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wanghaibo
 * @version V1.0
 * @desc 动态代理入口
 * @date 2018/9/6 17:32
 */
public class MyMapperProxyFactory<T> {

    private final Class<T> mapperInterface;

    private final Map<Method, MultiDataMapperMethod> methodCache = new ConcurrentHashMap<>();

    public MyMapperProxyFactory(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    public Class<T> getMapperInterface() {
        return mapperInterface;
    }

    public Map<Method, MultiDataMapperMethod> getMethodCache() {
        return methodCache;
    }

    @SuppressWarnings("unchecked")
    protected T newInstance(MyMapperProxy<T> mapperProxy) {
        return (T) Proxy.newProxyInstance(mapperInterface.getClassLoader(), new Class[]{mapperInterface}, mapperProxy);
    }

    public T newInstance(Map<String, JdbcTemplate> stringJdbcTemplateMap, Map<String, PlatformTransactionManager> stringTransactionManagerMap, ScanConfigurationProperties scanProperties) {
        final MyMapperProxy<T> mapperProxy = new MyMapperProxy<>(stringJdbcTemplateMap,stringTransactionManagerMap, mapperInterface, methodCache, scanProperties);
        return newInstance(mapperProxy);
    }
}