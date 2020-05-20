package cn.sk.mask.sys.service.impl;

import cn.sk.mask.sys.service.ISysLogService;
import cn.sk.mask.base.service.impl.BaseServiceImpl;
import cn.sk.mask.sys.mapper.SysLogMapper;
import cn.sk.mask.sys.pojo.SysLogCustom;
import cn.sk.mask.sys.pojo.SysLogQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 系统日志逻辑接口实现类
 */
@Service
public class SysLogServiceImpl extends BaseServiceImpl<SysLogCustom, SysLogQueryVo> implements ISysLogService {
    @Autowired
    private SysLogMapper sysLogMapper;



}
