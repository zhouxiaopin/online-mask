package cn.sk.mask.sys.mapper;

import cn.sk.mask.base.mapper.IBaseMapper;
import cn.sk.mask.sys.pojo.SysLogCustom;
import cn.sk.mask.sys.pojo.SysLogQueryVo;
import org.springframework.stereotype.Repository;

@Repository
public interface SysLogMapper extends IBaseMapper<SysLogCustom, SysLogQueryVo> {

}