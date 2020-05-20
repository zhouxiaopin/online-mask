package cn.sk.mask.sys.mapper;

import cn.sk.mask.base.mapper.IBaseMapper;
import cn.sk.mask.sys.pojo.SysPermisCustom;
import cn.sk.mask.sys.pojo.SysPermisQueryVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface SysPermisMapper extends IBaseMapper<SysPermisCustom, SysPermisQueryVo> {
    List<Map<String,Object>> selectListByRoleId(@Param("params") Map<String,Object> params);
}