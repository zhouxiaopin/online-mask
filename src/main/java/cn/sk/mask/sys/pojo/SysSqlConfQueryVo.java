package cn.sk.mask.sys.pojo;

import cn.sk.mask.base.pojo.BaseQueryVo;
import lombok.Getter;
import lombok.Setter;

/**
 * 系统sql语句配置实体类的包装对象
 */
@Getter
@Setter
public class SysSqlConfQueryVo extends BaseQueryVo {
    private SysSqlConfCustom sysSqlConfCustom;
}