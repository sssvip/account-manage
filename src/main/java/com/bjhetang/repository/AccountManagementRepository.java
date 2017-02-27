package com.bjhetang.repository;

import com.bjhetang.domain.AccountManagement;
import com.bjhetang.dto.AccountManagementFilter;
import com.bjhetang.exception.RepositoryException;
import com.bjhetang.exception.ServiceException;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by David on 2017/2/24.
 * 说明一下：关于类和方法的说明可以根据团队方式或样式有所不同，这里我简单用注释来写
 * 关于异常问题，简单实现，在repository层这里不处理了
 */
public interface AccountManagementRepository {
    //增加
    AccountManagement save(AccountManagement accountManagement) throws RepositoryException;

    //删除
    AccountManagement delete(int serialNumber) throws RepositoryException;

    //修改
    AccountManagement update(AccountManagement accountManagement) throws RepositoryException;

    //查询
    AccountManagement findOne(int serialNumber) throws RepositoryException;

    //分页查询(这里就不实现sortable什么的了，默认用编码号排序--升序)
    List<AccountManagement> list(int page, int pageSize) throws RepositoryException;

    //封存
    boolean lock(int serialNumber) throws RepositoryException;

    //启封
    boolean unlock(int serialNumber) throws RepositoryException;

    //根据条件过滤
    List<AccountManagement> page(int page, int pageSize, AccountManagementFilter accountManagementFilter) throws RepositoryException;
}
