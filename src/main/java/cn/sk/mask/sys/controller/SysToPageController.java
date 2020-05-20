package cn.sk.mask.sys.controller;

import cn.sk.mask.sys.service.ISysToPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;

@RestController
public class SysToPageController {
    @Autowired
    private ISysToPageService sysToPageService;

    @GetMapping("/index")
    public ModelAndView index(ModelAndView mv, HttpServletResponse response){
//        SysUserCustom sysUserInfo = (SysUserCustom)SecurityUtils.getSubject().getSession().getAttribute(SysConst.SessionKey.SYSUSER_INFO);
        return sysToPageService.index(mv,response);
    }
    @GetMapping("/toWelcome")
    public ModelAndView index(ModelAndView mv){
        mv.setViewName("common/welcome");
        return mv;
    }
}
