package com.mcp.term.util.db;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

@MappedTypes({Date.class})
public class MyDateTypeHandle extends BaseTypeHandler<Date> {

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, Date date, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i,String.valueOf(date.getTime()/1000));
    }

    @Override
    public Date getNullableResult(ResultSet resultSet, String s) throws SQLException {
        if(resultSet.getString(s).equals("0")){
            return null;
        }
        return new Date(Long.valueOf(resultSet.getString(s)+"000"));
    }

    @Override
    public Date getNullableResult(ResultSet resultSet, int i) throws SQLException {
        if(resultSet.getString(i).equals("0")){
            return null;
        }
        return new Date(Long.valueOf(resultSet.getString(i)+"000"));
    }

    @Override
    public Date getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        if(callableStatement.getString(i).equals("0")){
            return null;
        }
        return new Date(Long.valueOf(callableStatement.getString(i)+"000"));
    }
}
