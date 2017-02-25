package com.bjhetang.repository.impl;

import com.bjhetang.domain.AccountManagement;
import com.bjhetang.repository.AccountManagementRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import java.sql.Array;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by David on 2017/2/24.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class AccountManagementRepositoryImplTest {
    @Resource
    AccountManagementRepository accountManagementRepository;

    @Test
    public void save() throws Exception {
        AccountManagement accountManagement = saveHelper();
        //删除测试数据
        accountManagementRepository.delete(accountManagement.getSerialNumber());
        //断言添加后编码不为默认值0
        Assert.assertNotEquals(0, accountManagement.getSerialNumber(), 0);

    }

    private AccountManagement saveHelper() throws Exception {
        AccountManagement accountManagement = new AccountManagement();
        accountManagement.setName("测试账户");
        accountManagement.setRemark("测试数据，不要使用");
        accountManagement.setType("卖家");
        accountManagement.setStatus("启封");
        return accountManagementRepository.save(accountManagement);
    }

    private void deleteHelper(int serialNumber) throws Exception {
        accountManagementRepository.delete(serialNumber);
    }

    @Test
    public void delete() throws Exception {
        System.out.println(accountManagementRepository.delete(2));
        ;
    }

    @Test
    public void update() throws Exception {

    }

    @Test
    public void findOne() throws Exception {

    }

    @Test
    public void list() throws Exception {
        List<AccountManagement> accountManagementList = accountManagementRepository.list(0, 3);
    }

    @Test
    public void lock() throws Exception {
        accountManagementRepository.lock(2);
    }

    @Test
    public void unlock() throws Exception {
        accountManagementRepository.unlock(2);
    }
}