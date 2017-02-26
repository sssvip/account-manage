package com.bjhetang.contorller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by David on 2017/2/26.
 */
@Controller
public class HomeController {
    @RequestMapping({"/", "/index"})
    public String index(Model model) {
        model.addAttribute("test",null);
        return "index";
    }
}
