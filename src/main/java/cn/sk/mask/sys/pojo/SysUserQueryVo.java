package cn.sk.mask.sys.pojo;

import cn.sk.mask.base.pojo.BaseQueryVo;
import lombok.Getter;
import lombok.Setter;

/**
 * 系统用户实体类的包装对象
 */
@Getter
@Setter
public class SysUserQueryVo extends BaseQueryVo {
    private SysUserCustom sysUserCustom;
}