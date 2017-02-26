package com.bjhetang.contorller;

import com.bjhetang.domain.AccountManagement;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 2017/2/26.
 */
@Controller
public class HomeController {
    @RequestMapping({"/", "/index"})
    public String index(Model model) {
        //添加所有账号类型
        model.addAttribute("types", getTypeList());
        //添加所有账号状态
        model.addAttribute("statuses", getStatusList());
        return "index";
    }
    //将所有账号类型常亮转为list,以便freemarker渲染
    private List<String> getTypeList() {
        List<String> types = new ArrayList<>(5);
        types.add(AccountManagement.TYPE_ADMIN);
        types.add(AccountManagement.TYPE_SALES);
        types.add(AccountManagement.TYPE_USER);
        return types;
    }
    //将所有账号类型常亮转为list,以便freemarker渲染
    private List<String> getStatusList(){
        List<String> statuses = new ArrayList<>(5);
        statuses.add(AccountManagement.STATUS_LOCKED);
        statuses.add(AccountManagement.STATUS_UNLOCKED);
        return statuses;
    }
}
