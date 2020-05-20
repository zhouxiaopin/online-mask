package cn.sk.mask.sys.utils;

import cn.sk.mask.sys.pojo.SysUserCustom;
import org.apache.shiro.SecurityUtils;
import org.springframework.util.ObjectUtils;

public class SysUtils {
    /**
     * 获取登录用户的id
     * @return
     */
    public static Integer getUserId() {
        Integer id = null;
        SysUserCustom sysUserInfo = (SysUserCustom) SecurityUtils.getSubject().getPrincipal();
        if(!ObjectUtils.isEmpty(sysUserInfo)) {
            id = sysUserInfo.getuId();
        }
        return id;
    }

    /**
     * 获取登录用户
     * @return
     */
    public static SysUserCustom getSysUser() {
        SysUserCustom sysUserInfo = (SysUserCustom) SecurityUtils.getSubject().getPrincipal();
        return sysUserInfo;
    }

}
