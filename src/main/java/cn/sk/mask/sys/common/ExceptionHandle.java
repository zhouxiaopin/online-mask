package cn.sk.mask.sys.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 *异常处理器
 */
@ControllerAdvice
@Slf4j
public class ExceptionHandle {
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Object handle(Exception e, HttpServletRequest request) {
//        log.error("{} Exception",request.getRequestURI(),e);
        //以后要添加判断是否是ajax请求
        if (e instanceof CustomException) {
            log.error("【CustomException异常】{}", e.getMessage());
            CustomException customException = (CustomException) e;
            return ServerResponse.createByErrorCodeMessage(customException.getCode(),customException.getMessage());
        }else if (e instanceof HttpRequestMethodNotSupportedException) {
            log.error("【请求方式异常】{}", e.getMessage());
            return ServerResponse.createByErrorMessage(e.getMessage());
        }else if (e instanceof UnauthenticatedException) {
            log.error("【没有登录】{}", e.getMessage());
//            return new ModelAndView("redirect:/sysUser/initLogin");
            return ServerResponse.createByError(ResponseCode.NO_AUTHENTICATED);
        }else if (e instanceof UnauthorizedException) {
            log.error("【没有权限】{}", e.getMessage());
            return ServerResponse.createByError(ResponseCode.NO_AUTHORIZED);
        }else {
            log.error("【系统异常】{}", e);
            return ServerResponse.createByErrorCodeMessage(ResponseCode.SYS_UNKNOWN_ERROR.getCode(),ResponseCode.SYS_UNKNOWN_ERROR.getMsg());
        }
    }
}
