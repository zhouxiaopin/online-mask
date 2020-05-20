package cn.sk.mask.sys.utils;

import cn.sk.mask.sys.service.ISysLogService;
import cn.sk.mask.sys.common.SysConst;
import cn.sk.mask.sys.pojo.SysLogCustom;
import cn.sk.mask.sys.pojo.SysUserCustom;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Component
public class LogUtil {
    @Autowired
    private ISysLogService sysLogService;

    public void writLog(Class clazz,String methodName,String oprt) {
        this.writLog(clazz, methodName, oprt, true);
    }
    public void writLog(Class clazz,String methodName,String oprt,boolean saveParams) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        log.debug("======系统日志处理开始======");
        //保存日志
        SysLogCustom sysLogCustom = new SysLogCustom();

        //获取请求的类名
//        String className = joinPoint.getTarget().getClass().getName();
        //获取请求的方法名
//        String methodName = method.getName();
//        sysLogCustom.setMethodName(className + "." + methodName);
        String simpleName = clazz.getSimpleName();
        sysLogCustom.setMethodName(simpleName + "_" + methodName);
        sysLogCustom.setExpan1(clazz.getSimpleName() + "_" +methodName);
        //url
        sysLogCustom.setRequestUrl(request.getRequestURL().toString());
        //method
        sysLogCustom.setRequestType(request.getMethod());

        //操作
        sysLogCustom.setOperation(oprt);

        if(saveParams) {
            //请求的参数
            //将参数所在的数组转换成json
            String params = JackJsonUtil.obj2String(request.getParameterMap());
            sysLogCustom.setParams(params);
        }

        //获取用户名
        SysUserCustom sysUserInfo = (SysUserCustom) SecurityUtils.getSubject().getPrincipal();
        if(!ObjectUtils.isEmpty(sysUserInfo)) {
            sysLogCustom.setUserId(sysUserInfo.getuId());
            sysLogCustom.setUserName(sysUserInfo.getUserName());
        }
        //获取用户ip地址
        sysLogCustom.setIp(IpAdrressUtil.getIpAdrress(request));

        sysLogCustom.setRecordStatus(SysConst.RecordStatus.ABLE);
        //调用service保存SysLog实体类到数据库
        sysLogService.insert(sysLogCustom);

        log.debug("======系统日志处理======");
    }
}
