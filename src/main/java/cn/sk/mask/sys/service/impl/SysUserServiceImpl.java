package cn.sk.mask.sys.service.impl;

import cn.sk.mask.base.service.impl.BaseServiceImpl;
import cn.sk.mask.sys.common.SysConst;
import cn.sk.mask.sys.common.CustomException;
import cn.sk.mask.sys.common.ResponseCode;
import cn.sk.mask.sys.common.ServerResponse;
import cn.sk.mask.sys.mapper.SysRoleMapper;
import cn.sk.mask.sys.mapper.SysUserMapper;
import cn.sk.mask.sys.mapper.SysUserRoleMapper;
import cn.sk.mask.sys.pojo.SysUserCustom;
import cn.sk.mask.sys.pojo.SysUserQueryVo;
import cn.sk.mask.sys.pojo.SysUserRole;
import cn.sk.mask.sys.service.ISysUserService;
import cn.sk.mask.sys.utils.ShiroUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 系统用户业务逻辑接口实现类
 */
@Service
public class SysUserServiceImpl extends BaseServiceImpl<SysUserCustom,SysUserQueryVo> implements ISysUserService {
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;
    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Override
    protected ServerResponse<SysUserCustom> insertBefore(SysUserCustom sysUserCustom) {
        //判断盐值是否存在
        String salt = sysUserCustom.getSalt();
        if(StringUtils.isEmpty(salt)) {
            salt = ShiroUtils.DEFALT_SALT;
            sysUserCustom.setSalt(salt);
        }
        sysUserCustom.setPassword(ShiroUtils.getMd5Pwd(salt,sysUserCustom.getPassword()));
        sysUserCustom.setRecordStatus(SysConst.RecordStatus.DISABLE);
//        sysUserCustom.setCreateTime(new Date());
        return super.insertBefore(sysUserCustom);
    }

    @Override
    protected ServerResponse<SysUserCustom> insertAfter(SysUserCustom sysUserCustom) {
        //判断是否有设置角色
        String[] roleIds = StringUtils.split(sysUserCustom.getRoleIds(),",");
        if(!ArrayUtils.isEmpty(roleIds)) {
            List<SysUserRole> sysUserRoles = Lists.newArrayList();
            for(int i = 0,len = roleIds.length; i < len; i++) {
                SysUserRole sysUserRole = new SysUserRole(null,sysUserCustom.getuId(),
                        Integer.valueOf(roleIds[i]));
                sysUserRoles.add(sysUserRole);
            }
            int num = sysUserRoleMapper.batchInsert(sysUserRoles);
            if(num < 1) {
                throw new CustomException(ResponseCode.ADD_FAIL);
            }
        }
        return super.insertAfter(sysUserCustom);
    }

    @Override
    public ServerResponse<SysUserCustom> queryObj(SysUserCustom entityCustom) {
        ServerResponse<SysUserCustom> serverResponse = super.queryObj(entityCustom);
        //查询用户角色
        List<SysUserRole> sysUserRoles = sysUserRoleMapper.selectByUserId(entityCustom.getuId());
        StringBuilder roleIds = new StringBuilder();
        for(int i = 0,len = sysUserRoles.size(); i < len; i++) {
            roleIds.append(sysUserRoles.get(i).getRoleId()).append(",");
        }
        if(roleIds.length()>1) {
            roleIds.deleteCharAt(roleIds.length()-1);
        }
        serverResponse.getData().setRoleIds(roleIds.toString());
        return serverResponse;
    }

    @Override
    protected ServerResponse<SysUserCustom> updateBefore(SysUserCustom sysUserCustom) {
        //判断是否有修改角色
        if(StringUtils.equals(sysUserCustom.getRoleIds(),sysUserCustom.getOldRoleIds())) {
            return super.updateBefore(sysUserCustom);
        }

        //查询系统用户之前是有拥有角色
        Integer userId = sysUserCustom.getuId();
        List<SysUserRole> sysUserRoles = sysUserRoleMapper.selectByUserId(userId);
        if(!CollectionUtils.isEmpty(sysUserRoles)) {
            //清除原来的角色
            int num = sysUserRoleMapper.realDeleteInUserId(userId);
            if(num < 1) {
                throw new CustomException(ResponseCode.MDF_FAIL);
            }
        }


        //判断是否有设置角色
        String[] roleIds = StringUtils.split(sysUserCustom.getRoleIds(),",");
        if(!ArrayUtils.isEmpty(roleIds)) {
            sysUserRoles = Lists.newArrayList();
            for(int i = 0,len = roleIds.length; i < len; i++) {
                SysUserRole sysUserRole = new SysUserRole(null,sysUserCustom.getuId(),
                        Integer.valueOf(roleIds[i]));
                sysUserRoles.add(sysUserRole);
            }
            int num = sysUserRoleMapper.batchInsert(sysUserRoles);
            if(num < 1) {
                throw new CustomException(ResponseCode.MDF_FAIL);
            }
        }
        return super.updateBefore(sysUserCustom);
    }

    @Override
    public ServerResponse<List<SysUserCustom>> queryObjs(SysUserQueryVo entityQueryVo) {

        List<SysUserCustom> list = baseMapper.selectListByQueryVo(entityQueryVo);

        Map<String,Object> params = Maps.newHashMap();
        params.put("recordStatus", SysConst.RecordStatus.ABLE);
        StringBuilder roleNames = new StringBuilder();
        for(int i = 0,len = list.size(); i < len; i++) {
            SysUserCustom sysUserCustom = list.get(i);

            params.put("userId",sysUserCustom.getuId());
            //根据用户id查找权限
            List<Map<String,Object>> sysRoles = sysRoleMapper.selectListByUserId(params);
            roleNames.delete(0,roleNames.length());
            for(int j = 0,length = sysRoles.size(); j < length; j++) {
                roleNames.append(sysRoles.get(j).get("roleName")).append(",");
            }
            if(roleNames.length() >1 )roleNames.deleteCharAt(roleNames.length()-1);
            list.get(i).setRoleName(roleNames.toString());
        }

        return ServerResponse.createBySuccess(SysConst.ResponseMsg.QUERY_SUCCE,list);
    }

    @Override
    protected ServerResponse<SysUserCustom> deleteInIdsAfter(String[] ids) {
        deleteAfter(ids);
        return super.deleteInIdsAfter(ids);
    }

    @Override
    protected ServerResponse<SysUserCustom> realDeleteInIdsAfter(String[] ids) {
        deleteAfter(ids);
        return super.realDeleteInIdsAfter(ids);
    }

    private void deleteAfter(String[] ids){
        for(int i = 0,len = ids.length; i < len; i++) {
            sysUserRoleMapper.realDeleteInUserId(Integer.valueOf(ids[i]));
        }
    }

}
