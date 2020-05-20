package cn.sk.mask.sys.mapper;

import cn.sk.mask.base.mapper.IBaseMapper;
import cn.sk.mask.sys.pojo.SysUserCustom;
import cn.sk.mask.sys.pojo.SysUserQueryVo;
import org.springframework.stereotype.Repository;

@Repository
public interface SysUserMapper extends IBaseMapper<SysUserCustom, SysUserQueryVo> {

}