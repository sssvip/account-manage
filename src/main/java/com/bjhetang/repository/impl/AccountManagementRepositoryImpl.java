package com.bjhetang.repository.impl;

import com.bjhetang.domain.AccountManagement;
import com.bjhetang.exception.RepositoryException;
import com.bjhetang.repository.AccountManagementRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by David on 2017/2/24.
 */
@Repository("accountManagementRepository")
public class AccountManagementRepositoryImpl implements AccountManagementRepository {
    @Resource
    NamedParameterJdbcTemplate template;

    private class AccountManagementRowMapper implements RowMapper {

        @Override
        public AccountManagement mapRow(ResultSet resultSet, int i) throws SQLException {
            AccountManagement accountManagement = new AccountManagement();
            accountManagement.setSerialNumber(resultSet.getInt("serial_number"));
            accountManagement.setName(resultSet.getString("name"));
            accountManagement.setRemark(resultSet.getString("remark"));
            accountManagement.setStatus(resultSet.getString("status"));
            accountManagement.setCreateOn(resultSet.getTimestamp("create_On"));
            accountManagement.setLastLoginTime(resultSet.getTimestamp("last_login_time"));
            accountManagement.setType(resultSet.getString("type"));
            return accountManagement;
        }
    }

    @Override
    public AccountManagement save(AccountManagement accountManagement) throws RepositoryException {
        //判空
        if (accountManagement == null) {
            return null;
        }
        //检验重复
        if (findOne(accountManagement.getSerialNumber()) != null) {
            throw new RepositoryException("编码已经存在");
        }
        String sql = "INSERT INTO TB_ACCOUNT_MANAGEMENT (serial_number,name,remark,status,create_On,last_login_time,type) VALUES (:serial_number,:name,:remark,:status,:create_On,:last_login_time,:type)";
        Map namedParameters = new HashMap();
        namedParameters.put("serial_number", accountManagement.getSerialNumber());
        namedParameters.put("name", accountManagement.getName());
        namedParameters.put("remark", accountManagement.getRemark());
        namedParameters.put("status", accountManagement.getStatus());
        namedParameters.put("create_On", accountManagement.getCreateOn());
        namedParameters.put("last_login_time", accountManagement.getLastLoginTime());
        namedParameters.put("type", accountManagement.getType());
        if (template.update(sql, namedParameters) > 0) {
            return accountManagement;
        } else {
            throw new RepositoryException("增加账户管理异常");
        }
    }

    @Override
    public AccountManagement delete(int serialNumber) throws RepositoryException {
        return null;
    }

    @Override
    public AccountManagement update(AccountManagement accountManagement) throws RepositoryException {
        return null;
    }

    @Override
    public AccountManagement findOne(int serialNumber) throws RepositoryException {
        List<AccountManagement> accountManagements = template.query("SELECT * FROM TB_ACCOUNT_MANAGEMENT WHERE SERIAL_NUMBER = :serial_number", new MapSqlParameterSource("serial_number", serialNumber), new AccountManagementRowMapper());
        return accountManagements == null ? null : accountManagements.size() > 0 ? accountManagements.get(0) : null;
    }

    @Override
    public List<AccountManagement> list(int page, int pageSize) throws RepositoryException {
        return null;
    }

    @Override
    public boolean lock(int serialNumber) throws RepositoryException {
        return false;
    }

    @Override
    public boolean unlock(int serialNumber) throws RepositoryException {
        return false;
    }
}
