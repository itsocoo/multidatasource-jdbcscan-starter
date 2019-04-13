package com.itsocoo.multidatasource.jdbc.scan.proxys;

import com.itsocoo.multidatasource.jdbc.scan.annotations.MultiChoiceParams;
import com.itsocoo.multidatasource.jdbc.scan.annotations.MultiDataScanApi;
import com.itsocoo.multidatasource.jdbc.scan.common.ChoiceHelper;
import com.itsocoo.multidatasource.jdbc.scan.properties.ScanConfigurationProperties;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.Types;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author wanghaibo
 * @version V1.0
 * @desc 动态代理实现类
 * @date 2018/9/7 15:01
 */
public class MyMapperProxy<T> implements InvocationHandler, Serializable {
    private static final long serialVersionUID = -225417109130801171L;
    private static final Logger logger = LoggerFactory.getLogger(MyMapperProxy.class);

    private final Map<String, JdbcTemplate> stringJdbcTemplateMap;

    private final Map<String, PlatformTransactionManager> stringTransactionManagerMap;

    private final Class<T> mapperInterface;

    private final Map<Method, MultiDataMapperMethod> methodCache;

    private final ScanConfigurationProperties properties;

    public MyMapperProxy(Map<String, JdbcTemplate> stringJdbcTemplateMap, Map<String, PlatformTransactionManager> stringTransactionManagerMap, Class<T> mapperInterface, Map<Method, MultiDataMapperMethod> methodCache, ScanConfigurationProperties scanProperties) {
        this.stringJdbcTemplateMap = stringJdbcTemplateMap;
        this.mapperInterface = mapperInterface;
        this.methodCache = methodCache;
        this.properties = scanProperties;
        this.stringTransactionManagerMap = stringTransactionManagerMap;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // try {
        //     if (Object.class.equals(method.getDeclaringClass())) {
        //         return method.invoke(this, args);
        //     } else if (isDefaultMethod(method)) {
        //         return invokeDefaultMethod(proxy, method, args);
        //     }
        // } catch (Throwable t) {
        //     throw ExceptionUtil.unwrapThrowable(t);
        // }
        // final MultiDataMapperMethod mapperMethod = cachedMapperMethod(method);

        logger.debug("===============>stringJdbcTemplateMap:{}", stringJdbcTemplateMap);

        return invokeProxyAction(method, args);
    }

    private Object invokeProxyAction(Method method, Object[] args) throws Exception {
        MultiDataScanApi apiClient = method.getAnnotation(MultiDataScanApi.class);

        String sql = apiClient.sql();

        Map<String, Object> paramsTypeMap = getSqlKeyMap(sql);

        Class<?> types = apiClient.types();

        logger.info("===============>properties:{}", properties);

        // 得到调用的参数Map的属性和值 获取到就不获取后面的
        List<Integer> integerArrayList = new ArrayList<>();
        List<Object> objectArrayList = new ArrayList<>();

        String name = Thread.currentThread().getName();
        logger.info("===============>name:{}",name);

        // 获取jdbcTemplate的键
        String baseChoice = getChoiceKey(method, args);

        if (StringUtils.isBlank(baseChoice)) {
            throw new Exception("找不到你配置的数据源的KEY！");
        }

        String jdbcTemplateKey = baseChoice + properties.getJdbcTemplateSuffix();
        String platformTransactionManagerKey = baseChoice + properties.getTransactionManagerSuffix();

        if (StringUtils.isBlank(jdbcTemplateKey) || StringUtils.isBlank(platformTransactionManagerKey)) {
            throw new Exception("找不到你“ChoiceHelper”中配置的数据源！");
        }
        for (Object arg : args) {
            // 参数为Map
            if (arg instanceof Map) {
                Map<? extends String, ?> map = (Map<? extends String, ?>) arg;
                // 去掉参数中的键
                map.remove(properties.getJdbcTemplateArgsKey());
                // TODO: putAll 覆盖后参数的顺序不变
                paramsTypeMap.putAll(map);
                logger.debug("===============>paramsTypeMap:{}", paramsTypeMap);

                paramsTypeMap.forEach((o, o2) -> {
                    objectArrayList.add(o2);
                    String typeName = o2.getClass().getTypeName();
                    switch (typeName) {
                        case "java.lang.String":
                            integerArrayList.add(Types.VARCHAR);
                            break;
                        case "java.lang.Integer":
                            integerArrayList.add(Types.INTEGER);
                            break;
                        case "java.lang.Long":
                            integerArrayList.add(Types.BIGINT);
                            break;
                        case "java.time.LocalDateTime":
                            integerArrayList.add(Types.TIMESTAMP);
                            break;
                        case "java.util.Date":
                            integerArrayList.add(Types.TIMESTAMP);
                            break;
                        case "java.math.BigDecimal":
                            integerArrayList.add(Types.DECIMAL);
                            break;
                        default:
                            break;
                    }
                });

            }
        }
        logger.debug("===============>integerArrayList:{}", integerArrayList);

        JdbcTemplate jdbcTemplate = stringJdbcTemplateMap.get(jdbcTemplateKey);

        Propagation propagation = apiClient.propagation();

        if (jdbcTemplate == null) {
            throw new Exception("找不到你“ChoiceHelper”中配置的数据源！");
        }

        // 获取方法的返回值类型
        Class<?> returnType = method.getReturnType();

        String returnTypeName = returnType.getTypeName();

        Object objectList = new Object();

        switch (apiClient.executeType().name()) {
            case "SELECT":
                objectList = queryAction(sql, paramsTypeMap, (Class<Object>) types, integerArrayList, objectArrayList, jdbcTemplate, (Class<Object>) returnType, returnTypeName, objectList);
                break;
            case "UPDATE":
                // 走事务
                if (apiClient.transaction()) {
                    objectList = updateTranAction(sql, integerArrayList, objectArrayList, jdbcTemplate, platformTransactionManagerKey, propagation.value());
                } else {
                    objectList = updateAction(sql, integerArrayList, objectArrayList, jdbcTemplate);
                }

                break;
            // DELETE, INSERT
            default:
                break;
        }

        return objectList;
    }


    /**
     * 执行select查询
     */
    private Object queryAction(String sql, Map<String, Object> paramsTypeMap, Class<Object> types, List<Integer> integerArrayList, List<Object> objectArrayList, JdbcTemplate jdbcTemplate, Class<Object> returnType, String returnTypeName, Object objectList) {

        switch (returnTypeName) {
            case "java.util.List":
                objectList = getExecuteResultList(sql, paramsTypeMap, types, integerArrayList, objectArrayList, jdbcTemplate);
                break;
            case "java.lang.Integer":
                objectList = getExecuteResultCount(sql, paramsTypeMap, returnType, integerArrayList, objectArrayList, jdbcTemplate);
                break;
        }
        return objectList;
    }

    /**
     * 执行update查询
     */
    private int updateTranAction(String sql, List<Integer> integerArrayList, List<Object> objectArrayList, JdbcTemplate jdbcTemplate, String jdbcTemplateKey, Integer propagation) {
        logger.debug("op=start_updateTranAction, sql={}, integerArrayList={}, objectArrayList={}, jdbcTemplate={}, jdbcTemplateKey={}, propagation={}", sql, integerArrayList, objectArrayList, jdbcTemplate, jdbcTemplateKey, propagation);

        PlatformTransactionManager transactionManager = stringTransactionManagerMap.get(jdbcTemplateKey);
        // 定义事务类型
        DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        // 如果当前没有事务，就新建一个事务，
        // 如果已经存在一个事务，就使用这个事务中
        transactionDefinition.setPropagationBehavior(propagation);
        // 获取事务
        TransactionStatus transaction = transactionManager.getTransaction(transactionDefinition);

        int returnObject = 0;

        try {
            returnObject = jdbcTemplate.update(getSqlString(sql), objectArrayList.toArray(new Object[objectArrayList.size() - 1]), listToInt(integerArrayList));

            transactionManager.commit(transaction);// 提交事务，操作成功
        } catch (DataAccessException e) {
            transactionManager.rollback(transaction);// 回滚事务，操作失败
            logger.error("\n\n===============>", e);
        }

        return returnObject;
    }

    private int updateAction(String sql, List<Integer> integerArrayList, List<Object> objectArrayList, JdbcTemplate jdbcTemplate) {
        logger.debug("op=start_updateAction, sql={}, integerArrayList={}, objectArrayList={}, jdbcTemplate={}", sql, integerArrayList, objectArrayList, jdbcTemplate);

        int returnObject = 0;

        try {
            returnObject = jdbcTemplate.update(getSqlString(sql), objectArrayList.toArray(new Object[objectArrayList.size() - 1]), listToInt(integerArrayList));

        } catch (DataAccessException e) {
            // e.printStackTrace();
            logger.error("\n\n===============>", e);
        }
        return returnObject;
    }

    /**
     * @author wanghaibo
     * @version V1.0
     * @desc 查询Count
     * @date 2018/9/18 18:21
     */
    private Object getExecuteResultCount(String sql, Map<String, Object> paramsTypeMap, Class<Object> returnType, List<Integer> integerArrayList, List<Object> objectArrayList, JdbcTemplate jdbcTemplate) {

        Object returnObject = new Object();

        try {
            if (paramsTypeMap.isEmpty()) {
                returnObject = jdbcTemplate.queryForObject(sql, returnType);
            } else {
                returnObject = jdbcTemplate.queryForObject(getSqlString(sql), objectArrayList.toArray(new Object[objectArrayList.size() - 1]), listToInt(integerArrayList), returnType);
            }
        } catch (DataAccessException e) {
            // e.printStackTrace();
            logger.error("\n\n===============>", e);
        }

        return returnObject;
    }

    /**
     * @author wanghaibo
     * @version V1.0
     * @desc 查询List
     * @date 2018/9/18 18:21
     */
    private List<Object> getExecuteResultList(String sql, Map<String, Object> paramsTypeMap, Class<Object> types, List<Integer> integerArrayList, List<Object> objectArrayList, JdbcTemplate jdbcTemplate) {
        List<Object> objectList = new ArrayList<>();

        try {
            if (paramsTypeMap.isEmpty()) {
                objectList = jdbcTemplate.query(getSqlString(sql), getRowMapper(types));
            } else {
                objectList = jdbcTemplate.query(getSqlString(sql), objectArrayList.toArray(new Object[objectArrayList.size() - 1]), listToInt(integerArrayList), getRowMapper(types));
            }
        } catch (DataAccessException e) {
            // e.printStackTrace();
            logger.error("\n\n===============>", e);
        }
        return objectList;
    }

    private BeanPropertyRowMapper<Object> getRowMapper(Class<Object> types) {
        return new BeanPropertyRowMapper<Object>(types) {
            @Override
            protected void initBeanWrapper(BeanWrapper bw) {
                super.initBeanWrapper(bw);
            }
        };
    }

    private MultiDataMapperMethod cachedMapperMethod(Method method) {
        MultiDataMapperMethod mapperMethod = methodCache.get(method);
        if (mapperMethod == null) {
            mapperMethod = new MultiDataMapperMethod(mapperInterface, method, stringJdbcTemplateMap);
            methodCache.put(method, mapperMethod);
        }
        return mapperMethod;
    }

   /* @UsesJava7
    private Object invokeDefaultMethod(Object proxy, Method method, Object[] args)
            throws Throwable {
        final Constructor<MethodHandles.Lookup> constructor = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class, int.class);
        if (!constructor.isAccessible()) {
            constructor.setAccessible(true);
        }
        final Class<?> declaringClass = method.getDeclaringClass();
        return constructor.newInstance(declaringClass,
                MethodHandles.Lookup.PRIVATE
                        | MethodHandles.Lookup.PROTECTED
                        | MethodHandles.Lookup.PACKAGE
                        | MethodHandles.Lookup.PUBLIC)
                .unreflectSpecial(method, declaringClass).bindTo(proxy).invokeWithArguments(args);
    }*/

    /**
     * Backport of java.lang.reflect.Method#isDefault()
     */
    private boolean isDefaultMethod(Method method) {
        return (method.getModifiers()
                & (Modifier.ABSTRACT | Modifier.PUBLIC | Modifier.STATIC)) == Modifier.PUBLIC
                && method.getDeclaringClass().isInterface();
    }

    private int[] listToInt(List<Integer> integers) {
        return Arrays.stream(integers.toArray(new Integer[integers.size() - 1])).mapToInt(Integer::valueOf).toArray();
    }

    /**
     * 将参数:key转为?占位符
     */
    private static String getSqlString(String sql) {
        // String regEx = ":(\\w+)?";
        // 排除掉2018-11-11 00:00:00
        String regEx = ":([a-zA-Z]+)";
        return sql.replaceAll(regEx, "?");
    }

    /**
     * 来获取参数的顺序Map
     */
    private static Map<String, Object> getSqlKeyMap(String sql) {
        // String regEx = ":(\\w+)?";
        String regEx = ":([a-zA-Z]+)";

        Pattern r = Pattern.compile(regEx);
        Matcher m = r.matcher(sql);
        // 使用LinkedHashMap 来获取参数的顺序
        Map<String, Object> keyMaps = new LinkedHashMap<>();
        int sort = 1;
        while (m.find()) {
            // group(0)或group()将会返回整个匹配的字符串（完全匹配）；group(i)则会返回与分组i匹配的字符
            keyMaps.put(m.group(1), sort++);
        }
        return keyMaps;
    }

    private static List<String> getSqlKeyList(String sql) {
        // String regEx = ":(\\w+)?";
        String regEx = ":([a-zA-Z]+)";

        Pattern r = Pattern.compile(regEx);
        Matcher m = r.matcher(sql);
        List<String> keyLists = new LinkedList<>();
        while (m.find()) {
            // group(0)或group()将会返回整个匹配的字符串（完全匹配）；group(i)则会返回与分组i匹配的字符
            // 这个例子只有一个分组
            keyLists.add(m.group(1));

            // System.out.println(m.group(1));
        }
        return keyLists;
    }

    // 依次获取method绑定的key
    private String getChoiceKey(Method method, Object[] args) {
        // 1.从@MultiChoiceParams注解中获取值
        String baseChoice = getArgsByAnnoParams(args, method);

        // 2.从参数Map中获取值
        if (StringUtils.isBlank(baseChoice)) {
            baseChoice = getArgsByMapParams(args);
        }

        // 3.从ChoiceHelper中获取本线程的值
        if (StringUtils.isBlank(baseChoice)) {
            baseChoice = ChoiceHelper.choice();
        }
        // // 4.从ChoiceHelper中获取多线程共享的值
        // if (StringUtils.isBlank(baseChoice)) {
        //     baseChoice = ChoiceHelper.choice();
        // }
        return baseChoice;
    }

    // 获取方法参数中@MultiChoiceParams参数中包含的的值
    private static String getArgsByAnnoParams(Object[] args, Method method) {
        String choice = "";
        // 参数(多个)
        if (args.length > 0) {
            Annotation[][] parameterAnnotations = method.getParameterAnnotations();
            // 获取参数类型
            Class[] parameterTypes = method.getParameterTypes();

            for0:
            for (Annotation[] parameterAnnotation : parameterAnnotations) {
                int paramIndex = ArrayUtils.indexOf(parameterAnnotations, parameterAnnotation);
                for (Annotation annotation : parameterAnnotation) {
                    if (annotation instanceof MultiChoiceParams) {
                        Object paramValue = args[paramIndex];
                        Class parameterType = parameterTypes[paramIndex];
                        String typeName = parameterType.getTypeName();
                        // 参数必须是字符串才处理
                        if (typeName.equalsIgnoreCase("java.lang.String")) {
                            choice = (String) paramValue;
                            if (StringUtils.isNotBlank(choice)) {
                                break for0;
                            }
                        }
                    }
                }
            }

        }
        return choice;
    }


    // 获取方法参数中Map参数中包含的Key
    private String getArgsByMapParams(Object[] args) {
        String baseChoice = "";
        for (Object arg : args) {
            // 参数为Map
            if (arg instanceof Map) {
                Map<? extends String, ?> map = (Map<? extends String, ?>) arg;
                // 获取jdbcTemplate的键
                String key = properties.getJdbcTemplateArgsKey();
                baseChoice = (String) map.get(key);
                if (StringUtils.isNotBlank(baseChoice)) {
                    break;
                }
            }
        }
        return baseChoice;
    }


    // public static void main(String[] args) {


    // String sql = "SELECT :brand AS brand,id, title, pic_url AS picUrl, link_url AS linkUrl, tags, sort_by AS sortBy, source, create_time AS createTime, sub_title, marketing_words AS marketingWords FROM wx_share_article WHERE create_time>= :preTodayStart AND create_time<:preTodayEnd";
    //
    // String s = getSqlString(sql);
    //
    // logger.info("===============>s:{}", s);

    // String[] split = sql.split(regEx);
    // logger.info("===============>Arrays.asList(split):{}",Arrays.asList(split));
    //
    // String join = String.join("?", split);
    //
    // logger.info("===============>join:{}",join);
    // Map<String, Object> keyMaps = getSqlKeyMap(sql);
    // // List<String> sqlKeyList = getSqlKeyList(sql);
    //
    // Map<String, Object> params = new LinkedHashMap<>();
    // params.put("preTodayStart", "2010-06-29 23:59:59");
    // params.put("brand", "only");
    // params.put("preTodayEnd", "2010-09-06 23:59:59");
    //
    // keyMaps.putAll(params);
    //
    // logger.info("===============>keyMaps:{}", keyMaps);

    // String regEx = ":([a-zA-Z]+)";
    // String s = "SELECT 'Y' AS haveOrder FROM big_order WHERE length(pay_way)>2 AND contact_tel=:contactTel AND create_time >'2018-11-11 00:00:00' AND create_time < '2018-11-12 01:00:00' UNION ALL SELECT 'Y' AS haveOrder FROM big_order t1 LEFT JOIN member_info t2 ON t1.member_id=t2.id WHERE length(t1.pay_way)>2 AND t2.phone=:phone AND t1.create_time >'2018-11-11 00:00:00' AND t1.create_time < '2018-11-12 01:00:00' LIMIT 1".replaceAll(regEx, "?");
    // System.out.println(s);

    // System.out.println(underscoreName("big_order"));
    // }


}