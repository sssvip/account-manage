package com.bjhetang.contorller;

import com.bjhetang.domain.AccountManagement;
import com.bjhetang.exception.ServiceException;
import com.bjhetang.service.AccountManagementService;
import com.sun.org.glassfish.gmbal.ParameterNames;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by David on 2017/2/26.
 * 这里用RestController就直接采用rest风格输出数据,尽可能用资源状态转移的概念定义接口，通过http请求方法做方法区分，
 * get,post,put,delete分别对应常见的查、加、改、删，不在写过多的uri,
 * 控制层简单期间就不在写单元测试和异常控制，这样做的前提是客户端需要进行数据控制，完备的后端是需要做的。
 * 算了，考虑到这里的单元测试和repository不是同意性质的，需要对web进行模拟，这是另一技术点，还是写一下单元测试，
 * 这也是对控制层（对外接口）的一个保证。
 */
@RestController
@RequestMapping("/accountmanagements")
public class AccountManagementController {
    @Resource
    AccountManagementService accountManagementService;

    //post方法进行增加
    @PostMapping
    public AccountManagement save(@RequestBody AccountManagement accountManagement) {
        try {
            return accountManagementService.save(accountManagement);
        } catch (ServiceException e) {
            return null;
        }
    }

    //删除
    @DeleteMapping("/{serialNumber}")
    public AccountManagement delete(@PathVariable int serialNumber) {
        try {
            return accountManagementService.delete(serialNumber);
        } catch (ServiceException e) {
            return null;
        }
    }

    //修改
    @PutMapping
    public AccountManagement update(@RequestBody AccountManagement accountManagement) {
        try {
            return accountManagementService.update(accountManagement);
        } catch (ServiceException e) {
            return null;
        }
    }

    //查询
    @GetMapping("/{serialNumber}")
    public AccountManagement findOne(@PathVariable int serialNumber) {
        try {
            return accountManagementService.findOne(serialNumber);
        } catch (ServiceException e) {
            return null;
        }
    }

    //分页查询(这里就不实现sortable什么的了，默认用编码号排序--升序)
    //访问accountmanagements就代表访问账户管理复数集，直接返回，这里可以设置页码和单页数据量默认值
    @GetMapping
    public List<AccountManagement> list(int page, int pageSize) {
        try {
            return accountManagementService.list(page, pageSize);
        } catch (ServiceException e) {
            return null;
        }
    }

    @PutMapping("{serialNumber}/status")
    public boolean status(@PathVariable int serialNumber, String status) {
        //如果是封存
        if (AccountManagement.STATUS_LOCKED.equals(status)) {
            try {
                return accountManagementService.lock(serialNumber);
            } catch (ServiceException e) {
                return false;
            }
            //解封
        } else if (AccountManagement.STATUS_UNLOCKED.equals(status)) {
            try {
                return accountManagementService.unlock(serialNumber);
            } catch (ServiceException e) {
                return false;
            }
            //其他异常状态数据情况，当然目前仅定义了两种，不然可以用switch
        } else {
            return false;
        }
    }
}
