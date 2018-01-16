/*
 * @date 2016年10月29日 11:14
 */
package com.icourt.orm.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.core.SqlTypeValue;
import org.springframework.jdbc.core.StatementCreatorUtils;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;

/**
 * 执行数据库操作
 * @author june
 */
public class DataAccess implements IDataAccess{

    private final JdbcTemplate jdbcTemplate;

    public DataAccess(DataSource dataSource){
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public DataSource getDataSource() {
        return jdbcTemplate.getDataSource();
    }

    @Override
    public <T> List<T> select(String sql, Object[] args, RowMapper<T> rowMapper) {
        PreparedStatementCreator csc = getPreparedStatementCreator(sql, args, false);
        return jdbcTemplate.query(csc, new RowMapperResultSetExtractor<>(rowMapper));
    }

    @Override
    public int update(String sql, Object[] args, KeyHolder generatedKeyHolder) {
        boolean returnKeys = generatedKeyHolder != null;
        PreparedStatementCreator psc = getPreparedStatementCreator(sql, args, returnKeys);
        if (generatedKeyHolder == null) {
            return jdbcTemplate.update(psc);
        } else {
            return jdbcTemplate.update(psc, generatedKeyHolder);
        }
    }

    @Override
    public int[] batchUpdate(String sql, List<Object[]> batchArgs) {
        return jdbcTemplate.batchUpdate(sql, batchArgs);
    }

    @Override
    public <T> List<T> select(String sql, Map<String, Object> paramMap, RowMapper<T> rowMapper) {
        return new NamedParameterJdbcTemplate(jdbcTemplate).query(sql,paramMap,rowMapper);
    }

    @Override
    public int update(String sql, Map<String, Object> paramMap) {
        return new NamedParameterJdbcTemplate(jdbcTemplate).update(sql,paramMap);
    }

    private PreparedStatementCreator getPreparedStatementCreator(final String sql, final Object[] args, final boolean returnKeys) {
        return con -> {
            PreparedStatement ps = con.prepareStatement(sql);
            if (returnKeys) {
                ps = con.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS);
            } else {
                ps = con.prepareStatement(sql);
            }
            if (args != null) {
                for (int i = 0; i < args.length; i++) {
                    Object arg = args[i];
                    if (arg instanceof SqlParameterValue) {
                        SqlParameterValue paramValue = (SqlParameterValue) arg;
                        StatementCreatorUtils.setParameterValue(ps, i + 1, paramValue,
                                paramValue.getValue());
                    } else {
                        StatementCreatorUtils.setParameterValue(ps, i + 1,
                                SqlTypeValue.TYPE_UNKNOWN, arg);
                    }
                }
            }
            return ps;
        };
    }

}
