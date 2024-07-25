package com.asiainfo.biapp.pec.plan.jx.config.multiDataSourceConfig;

import com.asiainfo.biapp.pec.core.exception.BaseException;
import com.asiainfo.biapp.pec.plan.jx.config.multiDataSourceConfig.annotation.DataSource;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * @projectName: customer
 * @package: com.asiainfo.biapp.pec.plan.jx.config.multiDataSourceConfig
 * @className: DataSourceAspect
 * @author: chenlin16
 * @description: 切面类
 * @date: 2023/7/12 18:34
 */
@Aspect
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DataSourceAspect {

    @Autowired
    private DynamicDataSourceProperties properties;

    //分别对应于类上，方法上，参数列表中含有注解@DataSourceName的切入点表达式
    @Around("@within(com.asiainfo.biapp.pec.plan.jx.config.multiDataSourceConfig.annotation.DataSource) || " +
            "@annotation(com.asiainfo.biapp.pec.plan.jx.config.multiDataSourceConfig.annotation.DataSource) || " +
            "execution(* *(@com.asiainfo.biapp.pec.plan.jx.config.multiDataSourceConfig.annotation.DataSource (*),..))"
    )
    public Object around(ProceedingJoinPoint point) throws Throwable {
        //获取切入点切入的类
        Class<?> targetClass = point.getTarget().getClass();
        //获取切入点切入的方法
        Method method = ((MethodSignature) point.getSignature()).getMethod();
        //获取参数列表的值
        Object[] args = point.getArgs();

        DataSource dataSourceAnno;
        String dataSourceName = null;

        //先判断方法上是否有注解，如果没有再判断类上是否有注解
        if ((dataSourceAnno = method.getAnnotation(DataSource.class)) != null || (dataSourceAnno = targetClass.getAnnotation(DataSource.class)) != null)
            dataSourceName = dataSourceAnno.value();

        //当参数列表大于0时，判断参数列表中是否有注解
        if (args.length > 0) {
            //当注解DataSource在参数中时，应当只出现一次，当发现时就停止循环
            Parameter[] parameters = method.getParameters();
            for (int i = 0; i < parameters.length; i++) {
                if (parameters[i].getAnnotation(DataSource.class) != null) {
                    dataSourceName = String.valueOf(args[i]);
                    break;
                }
            }
        }
        //当数据源名称存在于配置文件中的数据源时，设定数据源
        if (properties.getDatasource().containsKey(dataSourceName)) {
            DynamicContextHolder.push(dataSourceName);
        } else {
            throw new BaseException("无效的数据源=" + dataSourceName);
        }

        try {
            return point.proceed();
        } finally {
            DynamicContextHolder.poll();
        }
    }

    //参数只有一个，且这个参数对应的类上含有注解@DataSourceName，或者类上和其中某个属性上含有注解@DataSourceName
    @Around("execution(* *(@com.asiainfo.biapp.pec.plan.jx.config.multiDataSourceConfig.annotation.DataSource *)))")
    public Object aroundField(ProceedingJoinPoint point) throws Throwable {
        //因切入点表达式只匹配一个参数的情况，所以取第一个参数
        Object arg = point.getArgs()[0];
        Class<?> aClass = arg.getClass();

        //先取类上注解的值
        DataSource dataSourceAnno = aClass.getAnnotation(DataSource.class);
        String dataSourceName = dataSourceAnno.value();

        //判断类中是否有属性含有注解@DataSourceName，如果有，就去这个属性的值，只取一次
        Field[] declaredFields = aClass.getDeclaredFields();
        for (int i = 0; i < declaredFields.length; i++) {
            if (declaredFields[i].getAnnotation(DataSource.class) != null) {
                declaredFields[i].setAccessible(true);
                // dataSourceName = String.valueOf(declaredFields[i].get(arg));
                dataSourceName = declaredFields[i].getAnnotation(DataSource.class).value(); // 获取注解值
                break;
            }
        }
        //当数据源名称存在于配置文件中的数据源时，设定数据源
        if (properties.getDatasource().keySet().contains(dataSourceName)) {
            DynamicContextHolder.push(dataSourceName);
        } else {
            throw new BaseException("无效的数据源=" + dataSourceName);
        }

        try {
            return point.proceed();
        } finally {
            DynamicContextHolder.poll();
        }
    }
}
