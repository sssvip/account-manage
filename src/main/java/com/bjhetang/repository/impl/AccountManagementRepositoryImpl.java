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
 * 简单开发，没有用javadoc形式注释
 */
@Repository("accountManagementRepository")
public class AccountManagementRepositoryImpl implements AccountManagementRepository {
    @Resource
    NamedParameterJdbcTemplate template;

    //这里方法同步可能会存在防止在多线程时并发nextSerialNumber()获取到同一个编码，而导致多个新建账号为同一个编码问题，并发不大的情况效率不是很大影响
    //同步synchronized其实可以加在nextSerialNumber()方法上，减小同步力度
    @Override
    public synchronized AccountManagement save(AccountManagement accountManagement) throws RepositoryException {
        //判空
        if (accountManagement == null) {
            return null;
        }
        //检验重复
        if (findOne(accountManagement.getSerialNumber()) != null) {
            throw new RepositoryException("编码已经存在:" + accountManagement.getSerialNumber());
        }
        String sql = "INSERT INTO TB_ACCOUNT_MANAGEMENT (serial_number,name,remark,status,create_On,last_login_time,type) VALUES (:serial_number,:name,:remark,:status,:create_On,:last_login_time,:type)";
        Map namedParameters = new HashMap();
        //确定新增后编码值
        accountManagement.setSerialNumber(nextSerialNumber());
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
        throwExceptionIfNotExist(serialNumber);
        //暂存
        AccountManagement accountManagement = findOne(serialNumber);
        HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
        hashMap.put("serialNumber", serialNumber);
        int result = template.update("DELETE FROM TB_ACCOUNT_MANAGEMENT WHERE serial_number = :serialNumber", hashMap);
        //如果删除成功则返回原始记录，否则直接返回null
        return result > 0 ? accountManagement : null;
    }

    @Override
    public AccountManagement update(AccountManagement accountManagement) throws RepositoryException {
        //判空
        if (accountManagement == null) {
            return null;
        }
        throwExceptionIfNotExist(accountManagement.getSerialNumber());
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("serialNumber", accountManagement.getSerialNumber());
        hashMap.put("name", accountManagement.getName());
        hashMap.put("remark", accountManagement.getRemark());
        hashMap.put("status", accountManagement.getStatus());
        hashMap.put("type", accountManagement.getType());
        int result = template.update("UPDATE TB_ACCOUNT_MANAGEMENT SET name =:name,remark =:remark,status =:status ,type =:type WHERE serial_number= :serialNumber", hashMap);
        return result > 0 ? accountManagement : null;
    }

    @Override
    public AccountManagement findOne(int serialNumber) throws RepositoryException {
        List<AccountManagement> accountManagements = template.query("SELECT * FROM TB_ACCOUNT_MANAGEMENT WHERE SERIAL_NUMBER = :serial_number", new MapSqlParameterSource("serial_number", serialNumber), new AccountManagementRowMapper());
        return accountManagements == null ? null : accountManagements.size() > 0 ? accountManagements.get(0) : null;
    }

    @Override
    public List<AccountManagement> list(int page, int pageSize) throws RepositoryException {
        //控制起始页
        if (page <= 0) {
            page = 0;
        }
        HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
        hashMap.put("start", page * pageSize);
        hashMap.put("end", pageSize);
        return template.query("SELECT * FROM TB_ACCOUNT_MANAGEMENT LIMIT :start,:end", hashMap, new AccountManagementRowMapper());
    }

    @Override
    public boolean lock(int serialNumber) throws RepositoryException {
        return changeStatus(serialNumber, "封存");
    }

    @Override
    public boolean unlock(int serialNumber) throws RepositoryException {
        return changeStatus(serialNumber, "启封");
    }

    //私有辅助相关全部放下方

    //辅助mapper
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

    //存在性检测辅助方法，不存在抛出异常
    private void throwExceptionIfNotExist(int serialNumber) throws RepositoryException {
        if (findOne(serialNumber) == null) {
            throw new RepositoryException("该编码不存在" + serialNumber);
        }
    }

    //状态修改抽取公共方法
    private boolean changeStatus(int serialNumber, String status) throws RepositoryException {
        throwExceptionIfNotExist(serialNumber);
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("status", status);
        hashMap.put("serialNumber", serialNumber + "");
        int result = template.update("UPDATE TB_ACCOUNT_MANAGEMENT SET status = :status where serial_number = :serialNumber", hashMap);
        return result > 0 ? true : false;
    }

    //
    private int nextSerialNumber() {
        HashMap hashMap = new HashMap();
        return template.queryForObject("SELECT serial_number FROM TB_ACCOUNT_MANAGEMENT  ORDER BY serial_number DESC limit 0,1", hashMap, Integer.class) + 1;
    }
}
