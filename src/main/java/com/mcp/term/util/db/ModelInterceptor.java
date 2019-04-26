package com.mcp.term.util.db;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.Properties;

@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
@Component
public class ModelInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        try {
            if (invocation.getArgs()[0] instanceof MappedStatement) {
                MappedStatement ms = (MappedStatement) invocation.getArgs()[0];
                ms.getConfiguration().setCacheEnabled(false);
                SqlCommandType sqlCommandType = ms.getSqlCommandType();
                Object parameter = invocation.getArgs()[1];
                Class<?> clazz = parameter.getClass();
                //保存时赋值createAt
                if (sqlCommandType.equals(SqlCommandType.INSERT)) {
                    try {
                        Field createdAtField = clazz.getDeclaredField("createAt");
                        createdAtField.setAccessible(true);
                        if (createdAtField.get(parameter) == null) {
                            createdAtField.set(parameter, new Date());
                        }
                    } catch (NoSuchFieldException e) {
                    }
                }
                //更新时赋值updateAt
                if (sqlCommandType.equals(SqlCommandType.UPDATE)) {
                    try {
                        Field updateAtField = clazz.getDeclaredField("updateAt");
                        updateAtField.setAccessible(true);
                        updateAtField.set(parameter, new Date());
                    } catch (NoSuchFieldException e) {
                    }
                }
            }
        } catch (Exception e) {
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object o) {
        return Plugin.wrap(o, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
