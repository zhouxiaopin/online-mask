package cn.sk.mask.sys.pojo;

import lombok.*;

import java.util.Date;

/**
 * 系统用户实体类的扩展类
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SysUserCustom extends SysUser {
    //确认密码
    private String password2;
    //角色ID
    private String roleIds;
    //旧角色ID
    private String oldRoleIds;

    //角色名
    private String roleName;

    public SysUserCustom(Integer uId, String userName, String password, String realName, String sex, String email, String mobilePhone, String salt, String descri, String recordStatus, Date updateTime, Date createTime) {
        super(uId, userName, password, realName,sex, email, mobilePhone, salt, descri, recordStatus, updateTime, createTime);

    }
}