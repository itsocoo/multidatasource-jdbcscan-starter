package com.itsocoo.multidatasource.jdbc.scan;

import com.itsocoo.multidatasource.jdbc.scan.proxys.MultiDataMapperFactoryBean;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Set;

/**
 * @author wanghaibo
 * @version V1.0
 * @desc 具体扫描的类
 * @date 2018/9/4 14:00
 */
public class MultiDataClassPathMapperScanner extends ClassPathBeanDefinitionScanner {
    private Class<? extends Annotation> annotationClass;
    private Class<?> markerInterface;

    private String scanSupportFactoryBeanName;

    private String stringJdbcTemplateMapBeanName;

    private MultiDataMapperFactoryBean<?> multiDataMapperFactoryBean = new MultiDataMapperFactoryBean<>();

    private boolean addToConfig = true;

    /**
     * 注入扫描的接口annotation
     */
    protected void registerFilters() {
        // addIncludeFilter(new AnnotationTypeFilter(annotationClass));
        boolean acceptAllInterfaces = true;

        // if specified, use the given annotation and / or marker interface
        if (this.annotationClass != null) {
            // TODO: 指定扫描自己的Annotation
            addIncludeFilter(new AnnotationTypeFilter(this.annotationClass));
            acceptAllInterfaces = false;
        }

        // override AssignableTypeFilter to ignore matches on the actual marker interface
        if (this.markerInterface != null) {
            addIncludeFilter(new AssignableTypeFilter(this.markerInterface) {
                @Override
                protected boolean matchClassName(String className) {
                    return false;
                }
            });
            acceptAllInterfaces = false;
        }

        if (acceptAllInterfaces) {
            // default include filter that accepts all classes
            addIncludeFilter((metadataReader, metadataReaderFactory) -> true);
        }

        // exclude package-info.java
        addExcludeFilter((metadataReader, metadataReaderFactory) -> {
            String className = metadataReader.getClassMetadata().getClassName();
            return className.endsWith("package-info");
        });
    }

    @Override
    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);

        if (beanDefinitions.isEmpty()) {
            logger.warn("No mapper was found in '" + Arrays.toString(basePackages) + "' package. Please check your configuration.");
        } else {
            processBeanDefinitions(beanDefinitions);
        }

        // 这个集合返回以后 Spring容器会将里面的所有内容注册到容器中
        return beanDefinitions;
    }

    public void setMultiDataMapperFactoryBean(MultiDataMapperFactoryBean<?> mapperFactoryBean) {
        this.multiDataMapperFactoryBean = mapperFactoryBean != null ? mapperFactoryBean : new MultiDataMapperFactoryBean<>();
    }

    /**
     * 将扫描到的符合条件的接口 实现动态代理
     */
    private void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitions) {
        GenericBeanDefinition definition;
        for (BeanDefinitionHolder holder : beanDefinitions) {
            definition = (GenericBeanDefinition) holder.getBeanDefinition();

            if (logger.isDebugEnabled()) {
                logger.debug("Creating MapperFactoryBean with name '" + holder.getBeanName() + "' and '" + definition.getBeanClassName() + "' mapperInterface");
            }

            //把接口的类型设置进去
            definition.getPropertyValues().add("mapperInterface", definition.getBeanClassName());

            // the mapper interface is the original class of the bean
            // but, the actual class of the bean is MapperFactoryBean
            definition.getConstructorArgumentValues().addGenericArgumentValue(definition.getBeanClassName());

            // TODO: 注入接口的工厂方法bean 设置Bean的真实类型MultiDataMapperFactoryBean
            definition.setBeanClass(this.multiDataMapperFactoryBean.getClass());

            // 是否把Mapper接口加入到Mybatis的Config当中去
            definition.getPropertyValues().add("addToConfig", this.addToConfig);

            boolean explicitFactoryUsed = false;
            //如果scanSupportFactoryBeanName的名字不为空 则在Spring容器中查询
            //适合多数据源
            if (StringUtils.hasText(this.scanSupportFactoryBeanName)) {
                definition.getPropertyValues().add("scanSupportFactory", new RuntimeBeanReference(this.scanSupportFactoryBeanName));
                explicitFactoryUsed = true;
            }

            // TODO: 将JdbcTemplateMap注入为RuntimeBeanReference
            if (!explicitFactoryUsed) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Enabling autowire by type for MapperFactoryBean with name '" + holder.getBeanName() + "'.");
                }
                // spring里面可以设置BeanDefinition自动注入类型，默认为AUTOWIRE_NO（不进行自动注入）。mybatis里面的扫描接口生成MapperFactoryBean的时候设置了
                // 他这里是为了按类型自动注入scanSupportFactory
                definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
            }
        }
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean checkCandidate(String beanName, BeanDefinition beanDefinition) {
        if (super.checkCandidate(beanName, beanDefinition)) {
            return true;
        } else {
            logger.warn("Skipping MapperFactoryBean with name '" + beanName
                    + "' and '" + beanDefinition.getBeanClassName() + "' mapperInterface"
                    + ". Bean already defined with the same name!");
            return false;
        }
    }

    public Class<? extends Annotation> getAnnotationClass() {
        return annotationClass;
    }

    public void setAnnotationClass(Class<? extends Annotation> annotationClass) {
        this.annotationClass = annotationClass;
    }

    public MultiDataClassPathMapperScanner(BeanDefinitionRegistry registry) {
        super(registry);
    }

    public MultiDataClassPathMapperScanner(BeanDefinitionRegistry registry, boolean useDefaultFilters) {
        super(registry, useDefaultFilters);
    }


    public Class<?> getMarkerInterface() {
        return markerInterface;
    }

    public void setMarkerInterface(Class<?> markerInterface) {
        this.markerInterface = markerInterface;
    }

    public boolean isAddToConfig() {
        return addToConfig;
    }

    public void setAddToConfig(boolean addToConfig) {
        this.addToConfig = addToConfig;
    }

    public String getStringJdbcTemplateMapBeanName() {
        return stringJdbcTemplateMapBeanName;
    }

    public void setStringJdbcTemplateMapBeanName(String stringJdbcTemplateMapBeanName) {
        this.stringJdbcTemplateMapBeanName = stringJdbcTemplateMapBeanName;
    }

    public MultiDataMapperFactoryBean<?> getMultiDataMapperFactoryBean() {
        return multiDataMapperFactoryBean;
    }

    public String getScanSupportFactoryBeanName() {
        return scanSupportFactoryBeanName;
    }

    public void setScanSupportFactoryBeanName(String scanSupportFactoryBeanName) {
        this.scanSupportFactoryBeanName = scanSupportFactoryBeanName;
    }
}
