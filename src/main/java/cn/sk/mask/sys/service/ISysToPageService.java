package cn.sk.mask.sys.service;

import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;

public interface ISysToPageService {
    //根据条件获取树形
    ModelAndView index(ModelAndView mv, HttpServletResponse response);
}
