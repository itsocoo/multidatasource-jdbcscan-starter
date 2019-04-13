package com.itsocoo.multidatasource.jdbc.scan;

import com.itsocoo.multidatasource.jdbc.scan.annotations.MultiDataScanApiService;
import com.itsocoo.multidatasource.jdbc.scan.properties.ScanConfigurationProperties;
import com.itsocoo.multidatasource.jdbc.scan.properties.ScanSupportFactory;
import com.itsocoo.multidatasource.jdbc.scan.properties.ScanSupportFactoryBean;
import com.itsocoo.multidatasource.jdbc.scan.proxys.MultiDataMapperFactoryBean;
import com.itsocoo.multidatasource.jdbc.scan.transation.MultiDataTransactionAspect;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.Advice;
import org.aspectj.weaver.AnnotatedElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author wanghaibo
 * @version V1.0
 * @desc
 * @date 2019/4/1 17:52
 */
@org.springframework.context.annotation.Configuration
@EnableConfigurationProperties(ScanConfigurationProperties.class)
// 需要ScanSupportFactory和ScanSupportFactoryBean在classpath中都存在
// @ConditionalOnClass({ScanSupportFactory.class, ScanSupportFactoryBean.class})
public class MultiDataScannerAutoConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(MultiDataScannerAutoConfiguration.class);
    private final ScanConfigurationProperties properties;

    @Autowired
    public MultiDataScannerAutoConfiguration(ScanConfigurationProperties scanConfigurationProperties) {
        this.properties = scanConfigurationProperties;
    }

    @Bean("scanSupportFactory")
    @ConditionalOnMissingBean
    public ScanSupportFactory scanSupportFactory() throws Exception {
        ScanSupportFactoryBean factory = new ScanSupportFactoryBean();
        factory.setScanConfigurationProperties(properties);
        return factory.getObject();
    }

    public static class AutoConfiguredTransactionScannerRegistrar
            implements BeanFactoryAware, ImportBeanDefinitionRegistrar, ResourceLoaderAware {

        private BeanFactory beanFactory;

        private ResourceLoader resourceLoader;

        @Override
        public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
            if (!AutoConfigurationPackages.has(this.beanFactory)) {
                logger.debug("Could not determine auto-configuration package, automatic mapper scanning disabled.");
                return;
            }

            logger.debug("Searching for mappers annotated with @MultiDataScanApiService");

            MultiDataClassPathMapperScanner scanner = new MultiDataClassPathMapperScanner(registry, false);

            if (this.resourceLoader != null) {
                scanner.setResourceLoader(this.resourceLoader);
            }

            logger.debug("Searching for mappers annotated with @MultiDataScanApiService");

            // 获取扫描的包路径
            List<String> packages = AutoConfigurationPackages.get(this.beanFactory);
            if (logger.isDebugEnabled()) {
                packages.forEach(pkg -> logger.debug("Using auto-configuration base package '{}'", pkg));
            }

            // 注入接口上的annotation
            scanner.setAnnotationClass(MultiDataScanApiService.class);
            scanner.registerFilters();
            scanner.setStringJdbcTemplateMapBeanName("jdbcTemplateMap");
            scanner.setScanSupportFactoryBeanName("scanSupportFactory");
            scanner.doScan(StringUtils.toStringArray(packages));
        }

        @Override
        public void setBeanFactory(BeanFactory beanFactory) {
            this.beanFactory = beanFactory;
        }

        @Override
        public void setResourceLoader(ResourceLoader resourceLoader) {
            this.resourceLoader = resourceLoader;
        }
    }

    @org.springframework.context.annotation.Configuration
    @Import({AutoConfiguredTransactionScannerRegistrar.class})
    public static class TransactionScannerNotFoundConfiguration implements InitializingBean {

        @Override
        public void afterPropertiesSet() {
            logger.debug("No {} found.", MultiDataMapperFactoryBean.class.getName());
        }
    }

    @ConditionalOnClass({EnableAspectJAutoProxy.class, Aspect.class, Advice.class,
            AnnotatedElement.class})
    @EnableAspectJAutoProxy
    @org.springframework.context.annotation.Configuration
    @Import({MultiDataTransactionAspect.class})
    public static class AspectTransactionNotFoundConfiguration implements InitializingBean {

        @Override
        public void afterPropertiesSet() {
            logger.debug("No {} found.", MultiDataTransactionAspect.class.getName());
        }
    }
}
