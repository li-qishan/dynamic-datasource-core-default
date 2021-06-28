package com.johns.dynamicdatasource.config.aspect;

import com.johns.dynamicdatasource.config.DynamicDataSource;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;


/**
 * 数据来源切面
 *
 * @author johns-li
 * @date 2021/06/23
 */
@Aspect
@Component
public class DataSourceAspect {

    @Autowired
    private DynamicDataSource dynamicDataSource;


    /**
     * 切换数据源应发生在事务之前，否则仍然会使用默认数据源，
     * 故数据源切换放在controller层（默认事务应放到service层）
     * //@Pointcut("@annotation(DynamicSwitchDataSource)")
     */
    @Pointcut("execution(public * com.johns.dynamicdatasource.controller..*.*(..))")
    public void dataSourcePointCut() {
    }

    @Before("dataSourcePointCut()")
    public void beforeExecute() {
        String tenantId = getTenantIdFromSession();
        if (tenantId != null) {
            dynamicDataSource.setCurrentThreadDataSource(tenantId);
        }

    }

    @After("dataSourcePointCut()")
    public void afterExecute() {
        DynamicDataSource.clearCurrentDataSourceKey();
    }

    //TODO 从session中获取租客id
    private String getTenantIdFromSession() {
        String tenantId ;
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest httpServletRequest = attributes.getRequest();
        tenantId = httpServletRequest.getHeader("tenantId");
        return tenantId;
    }
}
