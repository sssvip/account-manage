package com.bjhetang.service;

import com.bjhetang.domain.AccountManagement;
import com.bjhetang.dto.AccountManagementFilter;
import com.bjhetang.exception.RepositoryException;
import com.bjhetang.exception.ServiceException;

import java.util.List;

/**
 * Created by David on 2017/2/26.
 * 业务层就此次就简单实现，不写太多业务代码，初期就是简单调用关系，为后面业务变更提供扩展性
 */
public interface AccountManagementService {
    //增加
    AccountManagement save(AccountManagement accountManagement) throws ServiceException;

    //删除
    AccountManagement delete(int serialNumber) throws ServiceException;

    //修改
    AccountManagement update(AccountManagement accountManagement) throws ServiceException;

    //查询
    AccountManagement findOne(int serialNumber) throws ServiceException;

    //分页查询(这里就不实现sortable什么的了，默认用编码号排序--升序)
    List<AccountManagement> list(int page, int pageSize) throws ServiceException;

    //封存
    boolean lock(int serialNumber) throws ServiceException;

    //启封
    boolean unlock(int serialNumber) throws ServiceException;

    //根据过滤条件过滤
    List<AccountManagement> page(int page, int pageSize, AccountManagementFilter accountManagementFilter) throws ServiceException;
}
