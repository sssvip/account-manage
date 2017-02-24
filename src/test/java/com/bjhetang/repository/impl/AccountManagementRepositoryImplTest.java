package com.bjhetang.repository.impl;

import com.bjhetang.domain.AccountManagement;
import com.bjhetang.repository.AccountManagementRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

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
        AccountManagement accountManagement = new AccountManagement();
        accountManagement.setSerialNumber(2);
        accountManagement.setName("test");
        System.out.println(accountManagementRepository.save(accountManagement));

    }

    @Test
    public void delete() throws Exception {

    }

    @Test
    public void update() throws Exception {

    }

    @Test
    public void findOne() throws Exception {

    }

    @Test
    public void list() throws Exception {

    }

    @Test
    public void lock() throws Exception {

    }

    @Test
    public void unlock() throws Exception {

    }

}