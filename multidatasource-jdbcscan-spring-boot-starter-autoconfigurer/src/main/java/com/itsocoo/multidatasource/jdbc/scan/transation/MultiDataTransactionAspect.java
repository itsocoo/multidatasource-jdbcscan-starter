package com.itsocoo.multidatasource.jdbc.scan.transation;

import com.itsocoo.multidatasource.jdbc.scan.annotations.MultiTranParams;
import com.itsocoo.multidatasource.jdbc.scan.annotations.MultiTransactional;
import com.itsocoo.multidatasource.jdbc.scan.properties.ScanConfigurationProperties;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author wanghaibo
 * @version V1.0
 * @desc 事务拦截aop
 * @date 2019/4/8 11:31
 */
@Aspect
public class MultiDataTransactionAspect implements ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(MultiDataTransactionAspect.class);
    private ApplicationContext applicationContext;

    private ScanConfigurationProperties properties;

    public MultiDataTransactionAspect() {
    }

    // 定义切入点
    @Pointcut(value = "@annotation(com.itsocoo.multidatasource.jdbc.scan.annotations.MultiTransactional)")
    public void tranPoint() {
    }

    // 匹配参数是
    // @MultiTransactional
    // public void test(String[] choice) {}
    @Around("tranPoint() && args(choice,..)")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint, String[] choice) {
        MethodSignature joinPointObject = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = joinPointObject.getMethod();

        MultiTransactional multiTransactional = method.getAnnotation(MultiTransactional.class);
        Propagation propagation = multiTransactional.propagation();

        if (choice == null || choice.length == 0) {
            choice = multiTransactional.transactionManager();
        }

        if (choice.length == 0) {
            return null;
        }

        List<String> choices = Arrays.asList(choice);

        logger.info("===============>choices:{}", choices);

        Map<String, PlatformTransactionManager> transactionManagerMap = applicationContext.getBeansOfType(PlatformTransactionManager.class);

        PlatformTransactionManager transactionManager;

        logger.info("===============>choiceHelper:{}", choices);
        if (choices.size() > 1) {
            // ---------------配置链式事务-----------------------
            PlatformTransactionManager[] transactionManagers = choices.stream().map(cho -> transactionManagerMap.get(cho + properties.getTransactionManagerSuffix())).toArray(PlatformTransactionManager[]::new);
            transactionManager = new ChainedTransactionManager(transactionManagers);
        } else {
            transactionManager = transactionManagerMap.get(choices.get(0) + properties.getTransactionManagerSuffix());
        }

        // 定义事务类型
        return invokeMethod(proceedingJoinPoint, propagation, transactionManager);
    }

    // 匹配参数是
    // @MultiTransactional
    // public void test(@MultiTranParams String[] choice) {}
    @Around("tranPoint()")
    public Object doAround2(ProceedingJoinPoint proceedingJoinPoint) {
        String[] choice = new String[]{};

        MethodSignature joinPointObject = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = joinPointObject.getMethod();

        choice = getArgsParams(proceedingJoinPoint, choice, joinPointObject, method);

        MultiTransactional multiTransactional = method.getAnnotation(MultiTransactional.class);
        Propagation propagation = multiTransactional.propagation();

        if (choice.length == 0) {
            choice = multiTransactional.transactionManager();
        }

        if (choice.length == 0) {
            return null;
        }

        List<String> choices = Arrays.asList(choice);

        logger.info("===============>choices:{}", choices);

        Map<String, PlatformTransactionManager> transactionManagerMap = applicationContext.getBeansOfType(PlatformTransactionManager.class);

        PlatformTransactionManager transactionManager;

        logger.info("===============>choiceHelper:{}", choices);
        if (choices.size() > 1) {
            // ---------------配置链式事务-----------------------
            PlatformTransactionManager[] transactionManagers = choices.stream().map(chs -> transactionManagerMap.get(chs + properties.getTransactionManagerSuffix())).toArray(PlatformTransactionManager[]::new);
            transactionManager = new ChainedTransactionManager(transactionManagers);
        } else {
            transactionManager = transactionManagerMap.get(choices.get(0) + properties.getTransactionManagerSuffix());
        }

        return invokeMethod(proceedingJoinPoint, propagation, transactionManager);
    }

    // 执行目标方法并添加事务
    private Object invokeMethod(ProceedingJoinPoint proceedingJoinPoint, Propagation propagation, PlatformTransactionManager transactionManager) {
        if (transactionManager == null) {
            return null;
        }

        DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        // 如果当前没有事务，就新建一个事务
        // 如果已经存在一个事务，就使用这个事务中
        transactionDefinition.setPropagationBehavior(propagation.value());
        // 获取事务
        TransactionStatus transaction = transactionManager.getTransaction(transactionDefinition);
        logger.info("===============>Start Transaction:{}",transaction);

        Object object = null;
        try {
            object = proceedingJoinPoint.proceed();

            transactionManager.commit(transaction);// 提交事务，操作成功
            logger.info("===============>Commit Transaction:{}",transaction);

        } catch (Throwable throwable) {
            logger.error("\n===============>Rollback Transaction:{} throwable:{}\n",transaction, throwable);
            transactionManager.rollback(transaction);// 回滚事务，操作失败
        }
        return object;
    }

    // 获取带有注解参数的参数值
    private String[] getArgsParams(ProceedingJoinPoint proceedingJoinPoint, String[] choice, MethodSignature joinPointObject, Method method) {
        // 参数(多个)
        Object[] args = proceedingJoinPoint.getArgs();
        if (args.length > 0) {
            Annotation[][] parameterAnnotations = method.getParameterAnnotations();
            // 获取参数类型
            Class[] parameterTypes = joinPointObject.getParameterTypes();

            for0:
            for (Annotation[] parameterAnnotation : parameterAnnotations) {
                int paramIndex = ArrayUtils.indexOf(parameterAnnotations, parameterAnnotation);
                for (Annotation annotation : parameterAnnotation) {
                    if (annotation instanceof MultiTranParams) {
                        Object paramValue = args[paramIndex];
                        Class parameterType = parameterTypes[paramIndex];
                        String typeName = parameterType.getTypeName();
                        // 参数必须是字符串数组 才处理
                        if (paramValue.getClass().isArray() && typeName.equalsIgnoreCase("java.lang.String[]")) {
                            choice = (String[]) paramValue;

                            break for0;
                        }
                    }
                }
            }

        }
        return choice;
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        this.properties = applicationContext.getBean(ScanConfigurationProperties.class);
    }
}
