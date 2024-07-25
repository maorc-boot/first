package com.asiainfo.biapp.pec.plan.jx.config.multiDataSourceConfig.annotation;

import java.lang.annotation.*;

/**
 * @projectName: serviceend-jx
 * @package: com.asiainfo.biapp.pec.plan.jx.config.multiDataSourceConfig.annotation
 * @className: DataSource
 * @author: chenlin16
 * @description:
 * 1.1当作用于类或者方法上时，根据value切换数据源，必须为value赋值
 * 1.2当作用于参数列表中时，不用为value赋值，根据被标注的参数取值来切换数据源（只取第一次被注解标记的参数）。优先级：参数>方法>类，
 * 2.1当作用于参数对应的实体类上时，如果属性中没有此注解，则必须为value赋值，以value值切换数据源。如果属性中有注解，则两处的注解都不用赋值(以被标记的属性取值来切换数据源)
 * @date: 2023/7/12 18:34
 */

@Target({ElementType.FIELD, ElementType.TYPE, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface DataSource {
    String value() default "";
}
