package com.itsocoo.multidatasource.jdbc.scan.proxys;

import org.springframework.jdbc.core.JdbcTemplate;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author Clinton Begin
 * @author Eduardo Macarron
 * @author Lasse Voss
 */
public class MultiDataMapperMethod {

    public <T> MultiDataMapperMethod(Class<T> mapperInterface, Method method, Map<String, JdbcTemplate> stringJdbcTemplateMap) {
    }

    public Object execute(Map<String, JdbcTemplate> stringJdbcTemplateMap, Object[] args) {
        return new Object();
    }
}
