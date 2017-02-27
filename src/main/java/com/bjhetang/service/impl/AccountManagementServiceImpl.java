package com.bjhetang.service.impl;

import com.bjhetang.domain.AccountManagement;
import com.bjhetang.dto.AccountManagementFilter;
import com.bjhetang.exception.RepositoryException;
import com.bjhetang.exception.ServiceException;
import com.bjhetang.repository.AccountManagementRepository;
import com.bjhetang.service.AccountManagementService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by David on 2017/2/26.
 * 仅仅简单调用持久层，这里简单快速起见就不在写单元测试，在实际业务中是必须写的
 */
@Service("accountManagementService")
public class AccountManagementServiceImpl implements AccountManagementService {
    @Resource
    AccountManagementRepository accountManagementRepository;

    @Override
    public AccountManagement save(AccountManagement accountManagement) throws ServiceException {
        try {
            return accountManagementRepository.save(accountManagement);
        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public AccountManagement delete(int serialNumber) throws ServiceException {
        try {
            return accountManagementRepository.delete(serialNumber);
        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public AccountManagement update(AccountManagement accountManagement) throws ServiceException {
        try {
            return accountManagementRepository.update(accountManagement);
        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public AccountManagement findOne(int serialNumber) throws ServiceException {
        try {
            return accountManagementRepository.findOne(serialNumber);
        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public List<AccountManagement> list(int page, int pageSize) throws ServiceException {
        try {
            return accountManagementRepository.list(page, pageSize);
        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public boolean lock(int serialNumber) throws ServiceException {
        try {
            return accountManagementRepository.lock(serialNumber);
        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public boolean unlock(int serialNumber) throws ServiceException {
        try {
            return accountManagementRepository.unlock(serialNumber);
        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public List<AccountManagement> page(int page, int pageSize, AccountManagementFilter accountManagementFilter) throws ServiceException {
        try {
            return accountManagementRepository.page(page, pageSize, accountManagementFilter);
        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }
    }
}
