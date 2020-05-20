package cn.sk.mask.sys.shiro;

import cn.sk.mask.sys.service.ISysUserService;
import cn.sk.mask.sys.common.SysConst;
import cn.sk.mask.sys.mapper.SysPermisMapper;
import cn.sk.mask.sys.mapper.SysRoleMapper;
import cn.sk.mask.sys.pojo.SysUserCustom;
import cn.sk.mask.sys.pojo.SysUserQueryVo;
import cn.sk.mask.sys.utils.ShiroUtils;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
public class SkShiroRealm extends AuthorizingRealm {
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysPermisMapper sysPermisMapper;


    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(
            AuthenticationToken token) throws AuthenticationException {
        log.info("***********身份认证************");

        //1. 把 AuthenticationToken 转换为 UsernamePasswordToken
        UsernamePasswordToken upToken = (UsernamePasswordToken) token;

        //2. 从 UsernamePasswordToken 中来获取 username
        String username = upToken.getUsername();

        SysUserQueryVo sysUserQueryVo = new SysUserQueryVo();

        SysUserCustom condition = new SysUserCustom();
        condition.setUserName(username);
        sysUserQueryVo.getIsNoLike().put("userName",true);
        sysUserQueryVo.setSysUserCustom(condition);


        List<SysUserCustom> sysUserCustoms = sysUserService.queryObjs(sysUserQueryVo).getData();

        //3. 调用数据库的方法, 从数据库中查询 username 对应的用户记录
        log.info("从数据库中获取 username: " + username + " 所对应的用户信息.");


        //4. 若用户不存在, 则可以抛出 UnknownAccountException 异常
        if(CollectionUtils.isEmpty(sysUserCustoms)) {
            throw new UnknownAccountException("用户不存在!");
        }

//    }catch (UnknownAccountException uae) {
//
//    }catch (LockedAccountException lae)  {
//        throw new BaseException(BaseEnum.LOGIN_ADMIN_NO_USE);
//    }catch (AuthenticationException ae) { // 所有认证时异常的父类.
//        throw new BaseException(BaseEnum.LOGIN_FAIL);
//    }


//		if("unknown".equals(username)){
//			throw new UnknownAccountException("用户不存在!");
//		}


        SysUserCustom sysUserCustom = sysUserCustoms.get(0);

        //5. 根据用户信息的情况, 决定是否需要抛出其他的 AuthenticationException 异常.



        //6. 根据用户的情况, 来构建 AuthenticationInfo 对象并返回. 通常使用的实现类为: SimpleAuthenticationInfo
        //以下信息是从数据库中获取的.
        //1). principal: 认证的实体信息. 可以是 username, 也可以是数据表对应的用户的实体类对象.
        Object principal = sysUserCustom;
        //2). credentials: 密码.
        Object credentials = sysUserCustom.getPassword(); //"fc1709d0a95a6be30bc5926fdb7f22f4";
        String salt = sysUserCustom.getSalt();
        if(!credentials.equals(ShiroUtils.getMd5Pwd(salt,new String(upToken.getPassword())))) {
            throw new IncorrectCredentialsException("密码错误");
        }

        if(StringUtils.equals(SysConst.RecordStatus.DISABLE,sysUserCustom.getRecordStatus())) {
            throw new LockedAccountException("用户被禁用");
        }
//        if("admin".equals(username)){
//            credentials = "038bdaf98f2037b31f1e75b5b4c9b26e";
//        }

        //3). realmName: 当前 realm 对象的 name. 调用父类的 getName() 方法即可
        String realmName = getName();
        //4). 盐值.
//        ByteSource credentialsSalt = ByteSource.Util.bytes(username);
        ByteSource credentialsSalt = ByteSource.Util.bytes(salt);

        SimpleAuthenticationInfo info = null; //new SimpleAuthenticationInfo(principal, credentials, realmName);
        info = new SimpleAuthenticationInfo(principal, credentials, credentialsSalt, realmName);
        return info;
    }

    public static void main(String[] args) {
//        String hashAlgorithmName = "SHA1";
        String hashAlgorithmName = "MD5";
        Object credentials = "123456";
        Object salt = ByteSource.Util.bytes("ticc");
        int hashIterations = 1024;

        Object result = new SimpleHash(hashAlgorithmName, credentials, salt, hashIterations);
        System.out.println(result);
    }

    //授权会被 shiro 回调的方法
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(
            PrincipalCollection principals) {
        //1. 从 PrincipalCollection 中来获取登录用户的信息
//        Object principal = principals.getPrimaryPrincipal();
        SysUserCustom sysUserCustom = (SysUserCustom) principals.getPrimaryPrincipal();
        Map<String,Object> params = Maps.newHashMap();
        params.put("userId",sysUserCustom.getuId());
        params.put("recordStatus", SysConst.RecordStatus.ABLE);
        List<Map<String,Object>> sysRoleCustoms = sysRoleMapper.selectListByUserId(params);
        //2. 利用登录的用户的信息来用户当前用户的角色或权限(可能需要查询数据库)
        Set<String> roles = new HashSet<>();
        Set<Integer> roleIds = Sets.newHashSet();
        Map<String,Object> item;
        if(!CollectionUtils.isEmpty(sysRoleCustoms)) {
            for (int i = 0, len = sysRoleCustoms.size(); i < len; i++){
                item = sysRoleCustoms.get(i);
                roles.add(item.get("roleFlag").toString());
                roleIds.add(Integer.valueOf(item.get("roleId").toString()));
            }
        }

        Set<String> permissions = new HashSet<>();
        if(!CollectionUtils.isEmpty(sysRoleCustoms)) {
            params.clear();
            params.put("roleIds",roleIds);
            params.put("recordStatus", SysConst.RecordStatus.ABLE);
            List<Map<String,Object>> sysPermisCustoms = sysPermisMapper.selectListByRoleId(params);

            Map<String,Object> sysPermisItem;
            for (int i = 0, len = sysPermisCustoms.size(); i < len; i++){
                sysPermisItem = sysPermisCustoms.get(i);
//                permissions.add(sysMenuSysRole.getSysRoleNo()+":"+sysMenuSysRole.getSysMenuNo());
                permissions.add(sysPermisItem.get("pFlag").toString());
            }
        }


//        permissions.add("admin:add");
//        permissions.add("admin:del");

        //3. 创建 SimpleAuthorizationInfo, 并设置其 reles 属性.
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo(roles);
        info.addStringPermissions(permissions);
        //4. 返回 SimpleAuthorizationInfo 对象.
        return info;
    }
}
