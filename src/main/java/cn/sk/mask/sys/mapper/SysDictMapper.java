package cn.sk.mask.sys.mapper;

import cn.sk.mask.base.mapper.IBaseMapper;
import cn.sk.mask.sys.pojo.SysDictCustom;
import cn.sk.mask.sys.pojo.SysDictQueryVo;
import org.springframework.stereotype.Repository;

@Repository
public interface SysDictMapper extends IBaseMapper<SysDictCustom, SysDictQueryVo> {
}