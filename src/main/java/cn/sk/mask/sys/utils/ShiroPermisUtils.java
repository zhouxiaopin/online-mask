package cn.sk.mask.sys.utils;

import cn.sk.mask.sys.pojo.SysUserCustom;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.subject.Subject;

import javax.naming.NoPermissionException;

/**
 * 验证shiro权限的工具类
 */
@Slf4j
public class ShiroPermisUtils {

    private ShiroPermisUtils() {

    }

    /**
     * 检查当前用户是否有权限(任意一项)
     *
     * @param permissionCodes
     *            任意权限
     * @throws NoPermissionException
     */
    public static void checkPerissionAny(String... permissionCodes) {
        if (permissionCodes == null || permissionCodes.length == 0) {
            return;
        }

        // 获取用户信息
        Subject currentUser = SecurityUtils.getSubject();
        for (String permission : permissionCodes) {
            boolean permitted = currentUser.isPermitted(permission);// 判断是否有权限
            if (permitted) {
                return;
            }
        }

        // 没权限就抛出一个异常
        Object principal = currentUser.getPrincipal();
        if (principal instanceof SysUserCustom) {
            SysUserCustom sysUserInfo = (SysUserCustom) principal;
            log.error("user {} no permission !", sysUserInfo.getUserName());
        }
        throw new UnauthorizedException();
    }

    /**
     * 检查当前用户是否有权限(所有的)
     *
     * @param permissionCodes
     *            任意权限
     * @throws NoPermissionException
     */
    public static void checkPerissionAll(String... permissionCodes) {
        if (permissionCodes == null || permissionCodes.length == 0) {
            return;
        }

        // 获取用户信息
        Subject currentUser = SecurityUtils.getSubject();
        for (String permission : permissionCodes) {
            boolean permitted = currentUser.isPermitted(permission);// 判断是否有权限
            if (!permitted) {
                // 没权限就抛出一个异常
                Object principal = currentUser.getPrincipal();
                if (principal instanceof SysUserCustom) {
                    SysUserCustom sysUserInfo = (SysUserCustom) principal;
                    log.error("user {} no permission !", sysUserInfo.getUserName());
                }
                throw new UnauthorizedException();
            }
        }
    }
}
