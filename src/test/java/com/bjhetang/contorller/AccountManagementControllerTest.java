package com.bjhetang.contorller;

import com.bjhetang.domain.AccountManagement;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.annotation.Resource;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

/**
 * Created by David on 2017/2/26.
 * controller测试还是不一样，做简单测试
 * 简单测试  返回状态码200即可，让然也可以对返回结果进行断言的  讲ResultActions的结果进行解析再断言即可
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountManagementControllerTest {
    @Resource
    AccountManagementController accountManagementController;
    private MockMvc mockMvc;
    private AccountManagement accountManagementHelper;

    //初始mockMvc
    @Before
    public void setUp() throws Exception {
        mockMvc = standaloneSetup(accountManagementController).build();
        //为每一个测试创建数据
        //构造新建数据
        AccountManagement accountManagement = new AccountManagement();
        accountManagement.setRemark("test remark");
        accountManagement.setStatus(AccountManagement.STATUS_UNLOCKED);
        accountManagement.setType(AccountManagement.TYPE_ADMIN);
        accountManagement.setName("test name");
        //创建数据并断言
        ResultActions resultActions = mockMvc.perform(post("/accountmanagements")
                .content(new ObjectMapper().writeValueAsString(accountManagement))
                .contentType(MediaType.APPLICATION_JSON));
        //反序列化  拿到创建的数据-->对象
        accountManagementHelper = resultActions2AccountManagement(resultActions);
        //简单断言成功
        resultActions.andExpect(status().isOk());
    }

    @Test
    public void save() throws Exception {
        //save在setUp做过检测
    }


    @After
    public void deleteTest() throws Exception {
        mockMvc.perform(delete("/accountmanagements/{serialNumber}", accountManagementHelper
                .getSerialNumber()).characterEncoding("UTF-8")).andExpect(status().isOk());
    }

    @Test
    public void updateTest() throws Exception {
        //原备注
        String originRemark = accountManagementHelper.getRemark();
        //修改备注
        accountManagementHelper.setRemark("update test");
        ResultActions resultActions = mockMvc.perform(put("/accountmanagements").characterEncoding("UTF-8")
                .content(new ObjectMapper().writeValueAsString(accountManagementHelper))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        AccountManagement accountManagementUpdated = resultActions2AccountManagement(resultActions);
        //断言备注已经被修改
        Assert.assertNotEquals("update remark test", originRemark, accountManagementUpdated.getRemark());
    }

    @Test
    public void findOne() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/accountmanagements/{serialNumber}",
                accountManagementHelper.getSerialNumber()).characterEncoding("UTF-8")).andExpect(status().isOk());
        AccountManagement accountManagement = resultActions2AccountManagement(resultActions);
        //简单断言findOne为非空对象
        Assert.assertNotNull(accountManagement);
    }

    @Test
    public void list() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/accountmanagements?page=0&pageSize=3")).andExpect(status().isOk());
        //范序列化转数组
        AccountManagement[] accountManagements = new ObjectMapper().readValue(resultActions.andReturn().getResponse().getContentAsString(), AccountManagement[].class);
        //转list
        List<AccountManagement> accountManagementList = Arrays.asList(accountManagements);
        //断言大小非0
        Assert.assertNotEquals(0, accountManagementList.size());
    }
    
    @Test
    public void statusTest() throws Exception {
        ResultActions resultActions = mockMvc.perform(put("/accountmanagements/{serialNumber}/status",
                accountManagementHelper.getSerialNumber())
                .param("status", "启封")
                .characterEncoding("UTF-8")).andExpect(status().isOk());
        //断言返回结果为true
        Assert.assertTrue(Boolean.valueOf(resultActions.andReturn().getResponse().getContentAsString()));
    }

    //这里调用需要注意的是，结果必须是resultActions中内容必须是AccountManagement的序列化字符串
    private AccountManagement resultActions2AccountManagement(ResultActions resultActions) {
        try {
            return new ObjectMapper().readValue(resultActions.andReturn().getResponse().getContentAsString(), AccountManagement.class);
        } catch (IOException e) {
            return null;
        }
    }
}