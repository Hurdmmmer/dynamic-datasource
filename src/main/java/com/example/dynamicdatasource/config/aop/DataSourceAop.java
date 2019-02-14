package com.example.dynamicdatasource.config.aop;

import com.example.dynamicdatasource.config.DataSourceHolder;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@Aspect
@Order(-1) // 保证 aop 再 @Transactional 之前执行
@Component
public class DataSourceAop {

    private static final Logger logger = LoggerFactory.getLogger(DataSourceAop.class);

    @Pointcut(value = "execution(* com.example.dynamicdatasource.service.*.*(..))")
    public void switchDatabase(){}

    @Before("switchDatabase()")
    public void beforeMethod(JoinPoint joinPoint) {
        if (isExistsDataSource(joinPoint)) {
            return;
        }
        String methodName = joinPoint.getSignature().getName();
        if (StringUtils.startsWithAny(methodName, "get", "select", "find")) {
            DataSourceHolder.slave();
        } else {
            DataSourceHolder.master();
        }

    }

    @After("switchDatabase()")
    public void afterMethod(JoinPoint joinPoint){
        if (isExistsDataSource(joinPoint)) {
            return;
        }
        logger.debug("Revert DataSource by Pointcut");
        clearContextHolder();
    }

    private void clearContextHolder() {
        DataSourceHolder.clearDataSource();
    }

    private boolean isExistsDataSource(JoinPoint joinPoint) {
        Object target = joinPoint.getTarget();
        Object[] args = joinPoint.getArgs();
        Class<?>[] argsType = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            argsType[i] = arg.getClass();
        }
        Signature signature = joinPoint.getSignature();
        try {
            Method targetMethod = target.getClass().getMethod(signature.getName(), argsType);
            logger.info("Invoking method: {}", targetMethod.getName());
            Annotation[] annotations = targetMethod.getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation.annotationType().getSimpleName().equalsIgnoreCase("DataSource")){
                    logger.debug("Change method to include @DataSource to perform annotation-based AOP");
                    return true;
                }
            }
        } catch (Exception e) {
            logger.warn("Check for annotations @DataSource exception");
            return false;
        }
        return false;
    }

    @Before("@annotation(ds)")
    public void beforeAnnotation(JoinPoint jp, DataSource ds) {
        String dbSourceName = ds.name();
        if (!DataSourceHolder.containsDataSource(dbSourceName)) {
            logger.debug("The data source does not exist, use the default data source");
            DataSourceHolder.master();
        } else {
            logger.info("Specify a switch data source: {}", dbSourceName);
            DataSourceHolder.setDataSource(dbSourceName);
        }
    }

    @After("@annotation(ds)")
    public void afterAnnotation(JoinPoint joinPoint, DataSource ds) {
        logger.debug("Revert DataSource by annotation");
        clearContextHolder();
    }
}
