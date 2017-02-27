package com.bjhetang.repository.impl;

import com.bjhetang.domain.AccountManagement;
import com.bjhetang.dto.AccountManagementFilter;
import com.bjhetang.repository.AccountManagementRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
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
 * 简单测试，可以根据团队、项目情况添加严格的单元测试超时等等要求
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class AccountManagementRepositoryImplTest {
    @Resource
    AccountManagementRepository accountManagementRepository;
    private AccountManagement accountManagementHelper;

    @Test
    public void save() throws Exception {
        //断言添加后编码不为默认值0
        Assert.assertNotEquals(0, accountManagementHelper.getSerialNumber(), 0);
    }


    @Test
    public void delete() throws Exception {
        //这个测试断言由deleteHelper代劳
    }

    @Test
    public void update() throws Exception {
        //备注原记录
        String originRemark = accountManagementHelper.getRemark();
        accountManagementHelper.setRemark("test");
        //修改测试
        AccountManagement accountManagementUpdated = accountManagementRepository.update(accountManagementHelper);
        //修改账户管理，断言编码不变
        Assert.assertEquals(accountManagementHelper.getSerialNumber(), accountManagementUpdated.getSerialNumber(), 0);
        //断言备注已经修改
        Assert.assertNotEquals("修改备注", originRemark, accountManagementUpdated.getRemark());
    }

    @Test
    public void findOne() throws Exception {
        //断言新建的账户管理不为空
        Assert.assertNotNull(accountManagementHelper.getSerialNumber());
    }

    @Test
    public void list() throws Exception {
        List<AccountManagement> accountManagementList = accountManagementRepository.list(0, 3);
        //首先确保不为空
        Assert.assertNotNull(accountManagementList);
        //如果数据库中没有数据，查出来size就是1，如果有就是3，这里的delta所以设2
        Assert.assertEquals(1, accountManagementList.size(), 2);
    }

    @Test
    public void lock() throws Exception {
        //封存测试
        boolean result = accountManagementRepository.lock(accountManagementHelper.getSerialNumber());
        //断言封存成功
        Assert.assertTrue(result);
    }

    @Test
    public void unlock() throws Exception {
        //启封测试
        boolean result = accountManagementRepository.lock(accountManagementHelper.getSerialNumber());
        //断言启封测试成功
        Assert.assertTrue(result);
    }

    //单元测试前创建临时数据
    @Before
    public void saveHelper() throws Exception {
        AccountManagement accountManagement = new AccountManagement();
        accountManagement.setName("测试账户");
        accountManagement.setRemark("测试数据，不要使用");
        accountManagement.setType(AccountManagement.TYPE_ADMIN);
        accountManagement.setStatus(AccountManagement.STATUS_UNLOCKED);
        accountManagementHelper = accountManagementRepository.save(accountManagement);
    }

    //单元测试后删除临时数据
    @After
    public void deleteHelper() throws Exception {
        accountManagementRepository.delete(accountManagementHelper.getSerialNumber());
        //删除后断言空
        Assert.assertNull(accountManagementRepository.findOne(accountManagementHelper.getSerialNumber()));
    }

    @Test
    public void page() throws Exception {
        AccountManagementFilter accountManagementFilter = new AccountManagementFilter();
        accountManagementFilter.setName("测试账户");
        List<AccountManagement> accountManagementList=accountManagementRepository.page(0, 3, accountManagementFilter);
        Assert.assertNotNull(accountManagementList);
    }
}