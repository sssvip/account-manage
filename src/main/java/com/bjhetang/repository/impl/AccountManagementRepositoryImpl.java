package com.bjhetang.repository.impl;

import com.bjhetang.domain.AccountManagement;
import com.bjhetang.dto.AccountManagementFilter;
import com.bjhetang.exception.RepositoryException;
import com.bjhetang.repository.AccountManagementRepository;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
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
        accountManagement.setSerialNumber(getNextSerialNumber());
        namedParameters.put("serial_number", accountManagement.getSerialNumber());
        namedParameters.put("name", accountManagement.getName());
        namedParameters.put("remark", accountManagement.getRemark());
        namedParameters.put("status", accountManagement.getStatus());
        namedParameters.put("create_On", new Date());
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
        hashMap.put("last_login_time", new Date());
        int result = template.update("UPDATE TB_ACCOUNT_MANAGEMENT SET name =:name,remark =:remark,status =:status ,type =:type, last_login_time =:last_login_time WHERE serial_number= :serialNumber", hashMap);
        return result > 0 ? accountManagement : null;
    }

    @Override
    public AccountManagement findOne(int serialNumber) throws RepositoryException {
        List<AccountManagement> accountManagements = template.query("SELECT * FROM TB_ACCOUNT_MANAGEMENT WHERE SERIAL_NUMBER = :serial_number ORDER BY serial_number DESC", new MapSqlParameterSource("serial_number", serialNumber), new AccountManagementRowMapper());
        return accountManagements == null ? null : accountManagements.size() > 0 ? accountManagements.get(0) : null;
    }

    @Override
    public List<AccountManagement> list(int page, int pageSize) throws RepositoryException {
        return page(page, pageSize, null);
    }

    @Override
    public boolean lock(int serialNumber) throws RepositoryException {
        return changeStatus(serialNumber, AccountManagement.STATUS_LOCKED);
    }

    @Override
    public boolean unlock(int serialNumber) throws RepositoryException {
        return changeStatus(serialNumber, AccountManagement.STATUS_UNLOCKED);
    }

    @Override
    public List<AccountManagement> page(int page, int pageSize, AccountManagementFilter accountManagementFilter) throws RepositoryException {
        //控制起始页
        if (page <= 0) {
            page = 0;
        }
        //构造查询SQL
        StringBuffer filterSql = new StringBuffer();
        filterSql.append("SELECT * FROM TB_ACCOUNT_MANAGEMENT where 1=1 ");
        if (accountManagementFilter != null) {
            //编码模糊查询
            if (accountManagementFilter.getSerialNumber() != null && !"".equals(accountManagementFilter.getSerialNumber())) {
                filterSql.append(" and serial_number like '%" + accountManagementFilter.getSerialNumber() + "%' ");
            }
            //名称模糊查询
            if (accountManagementFilter.getName() != null && !"".equals(accountManagementFilter.getName())) {
                filterSql.append(" and name like '%" + accountManagementFilter.getName() + "%' ");
            }
            //时间处理
            if (accountManagementFilter.getDateRange() != null && !"".equals(accountManagementFilter.getDateRange())) {
                try {
                    String start = accountManagementFilter.getDateRange().split("-")[0].replace("/", "-");
                    String end = accountManagementFilter.getDateRange().split("-")[1].replace("/", "-");
                    filterSql.append(" and create_on BETWEEN '" + start + "' AND '" + end+"' ");
                } catch (Exception e) {
                    //时间处理这里处理不了就跳过，这里应该是约定数据格式的
                    throw new RepositoryException("时间格式需要约定");
                }
            }
            //用户类型
            if (accountManagementFilter.getType() != null && !"".equals(accountManagementFilter.getType())) {
                filterSql.append(" and type = '" + accountManagementFilter.getType()+"' ");
            }
        }
        //添加分页
        filterSql.append(" LIMIT :start,:end");
        HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
        hashMap.put("start", page * pageSize);
        hashMap.put("end", pageSize);
        System.out.println(filterSql.toString());
        return template.query(filterSql.toString(), hashMap, new AccountManagementRowMapper());
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

    //获取下一个编码
    private int getNextSerialNumber() {
        HashMap hashMap = new HashMap();
        return template.queryForObject("SELECT serial_number FROM TB_ACCOUNT_MANAGEMENT  ORDER BY serial_number DESC limit 0,1", hashMap, Integer.class) + 1;
    }
}
