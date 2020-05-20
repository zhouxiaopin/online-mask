package cn.sk.mask.sys.service;

import cn.sk.mask.base.service.IBaseService;
import cn.sk.mask.sys.common.ServerResponse;
import cn.sk.mask.sys.pojo.SysPermisCustom;
import cn.sk.mask.sys.pojo.SysPermisQueryVo;

import java.util.List;
import java.util.Map;

/**
 * 系统权限业务逻辑接口
 */
public interface ISysPermisService extends IBaseService<SysPermisCustom,SysPermisQueryVo>{
    //根据条件获取树形
    ServerResponse<List<Map<String,Object>>> querySysPermisTree(SysPermisQueryVo sysPermisQueryVo);
}
