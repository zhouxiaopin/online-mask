package cn.sk.mask.sys.common;

import cn.sk.mask.sys.pojo.SysLogCustom;
import cn.sk.mask.sys.pojo.SysUserCustom;
import cn.sk.mask.sys.service.ISysLogService;
import cn.sk.mask.sys.utils.IpAdrressUtil;
import cn.sk.mask.sys.utils.JackJsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * 系统日志：切面处理类
 */
@Component
@Aspect
@Slf4j
public class SysLogAspect {
    @Autowired
    private ISysLogService sysLogService;

    //定义切点 @Pointcut
    //在注解的位置切入代码
    @Pointcut("@annotation( cn.sk.mask.sys.common.SkLog)")
    public void logPoinCut() {
    }

    @Before("logPoinCut()")
    public void doBefore(JoinPoint joinPoint) {
        if(StringUtils.equals("sysLogout",joinPoint.getSignature().getName())) {
            daSaveSysLog(joinPoint);
        }

    }

    //切面 配置通知
    @AfterReturning("logPoinCut()")
    public void saveSysLog(JoinPoint joinPoint) {
        if(!StringUtils.equals("sysLogout",joinPoint.getSignature().getName())) {
            daSaveSysLog(joinPoint);
        }
    }

    private void daSaveSysLog(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        log.debug("======系统日志处理开始======");
        //保存日志
        SysLogCustom sysLogCustom = new SysLogCustom();

        //从切面织入点处通过反射机制获取织入点处的方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        //获取切入点所在的方法
        Method method = signature.getMethod();


        //获取请求的类名
//        String className = joinPoint.getTarget().getClass().getName();
        //获取请求的方法名
//        String methodName = method.getName();
//        sysLogCustom.setMethodName(className + "." + methodName);
        String methodName = method.getName();
        String simpleName = joinPoint.getTarget().getClass().getSimpleName();
        sysLogCustom.setMethodName(simpleName + "_" + methodName);
        sysLogCustom.setExpan1(signature.getDeclaringTypeName() + "_" +methodName);
        //url
        sysLogCustom.setRequestUrl(request.getRequestURL().toString());
        //method
        sysLogCustom.setRequestType(request.getMethod());


        //获取操作
        StringBuilder operation = new StringBuilder();

        SkLog skLog = method.getAnnotation(SkLog.class);
        if (skLog != null) {
            operation.append(skLog.value());

            //保存获取的操作
            sysLogCustom.setOperation(operation.toString());

            if(skLog.saveParams()) {
                //请求的参数
                Object[] args = joinPoint.getArgs();
                //将参数所在的数组转换成json
                String params = JackJsonUtil.obj2String(args);
                sysLogCustom.setParams(params);
            }
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
