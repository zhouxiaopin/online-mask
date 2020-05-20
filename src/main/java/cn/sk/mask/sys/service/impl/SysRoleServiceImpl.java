package cn.sk.mask.sys.service.impl;

import cn.sk.mask.base.service.impl.BaseServiceImpl;
import cn.sk.mask.sys.common.CustomException;
import cn.sk.mask.sys.common.ResponseCode;
import cn.sk.mask.sys.common.ServerResponse;
import cn.sk.mask.sys.mapper.SysRoleMapper;
import cn.sk.mask.sys.mapper.SysRolePermisMapper;
import cn.sk.mask.sys.mapper.SysRoleResourceMapper;
import cn.sk.mask.sys.pojo.SysRoleCustom;
import cn.sk.mask.sys.pojo.SysRolePermis;
import cn.sk.mask.sys.pojo.SysRoleQueryVo;
import cn.sk.mask.sys.pojo.SysRoleResource;
import cn.sk.mask.sys.service.ISysRoleService;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 系统角色业务逻辑接口实现类
 */
@Service
public class SysRoleServiceImpl extends BaseServiceImpl<SysRoleCustom, SysRoleQueryVo> implements ISysRoleService {
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysRolePermisMapper sysRolePermisMapper;
    @Autowired
    private SysRoleResourceMapper sysRoleResourceMapper;

    @Override
    protected ServerResponse<SysRoleCustom> insertAfter(SysRoleCustom sysRoleCustom) {
        //判断是否有设置权限
        String[] permisIds = StringUtils.split(sysRoleCustom.getPermisIds(),",");
        if(!ArrayUtils.isEmpty(permisIds)) {
            List<SysRolePermis> sysRolePermiss = Lists.newArrayList();
            for(int i = 0,len = permisIds.length; i < len; i++) {
                SysRolePermis sysRolePermis = new SysRolePermis(null,sysRoleCustom.getRoleId(),
                        Integer.valueOf(permisIds[i]));
                sysRolePermiss.add(sysRolePermis);
            }
            int num = sysRolePermisMapper.batchInsert(sysRolePermiss);
            if(num < 1) {
                throw new CustomException(ResponseCode.ADD_FAIL);
            }
        }

        //判断是否有设置资源
        String[] resIds = StringUtils.split(sysRoleCustom.getResIds(),",");
        if(!ArrayUtils.isEmpty(resIds)) {
            List<SysRoleResource> sysRoleResources = Lists.newArrayList();
            for(int i = 0,len = resIds.length; i < len; i++) {
                SysRoleResource sysRoleResource = new SysRoleResource(null,sysRoleCustom.getRoleId(),
                        Integer.valueOf(resIds[i]));
                sysRoleResources.add(sysRoleResource);
            }
            int num = sysRoleResourceMapper.batchInsert(sysRoleResources);
            if(num < 1) {
                throw new CustomException(ResponseCode.ADD_FAIL);
            }
        }
        return super.insertAfter(sysRoleCustom);
    }

    @Override
    public ServerResponse<SysRoleCustom> queryObj(SysRoleCustom entityCustom) {
        ServerResponse<SysRoleCustom> serverResponse = super.queryObj(entityCustom);
        //查询角色权限
        List<SysRolePermis> sysRolePermisList = sysRolePermisMapper.selectByRoleId(entityCustom.getRoleId());
        StringBuilder permisIds = new StringBuilder();
        for(int i = 0,len = sysRolePermisList.size(); i < len; i++) {
            permisIds.append(sysRolePermisList.get(i).getPermisId()).append(",");
        }
        if(permisIds.length()>1) {
            permisIds.deleteCharAt(permisIds.length()-1);
        }
        SysRoleCustom sysRoleCustom = serverResponse.getData();
        sysRoleCustom.setPermisIds(permisIds.toString());

        //查询角色资源
        List<SysRoleResource> sysRoleResourceList = sysRoleResourceMapper.selectByRoleId(entityCustom.getRoleId());
        StringBuilder resIds = new StringBuilder();
        for(int i = 0,len = sysRoleResourceList.size(); i < len; i++) {
            resIds.append(sysRoleResourceList.get(i).getResId()).append(",");
        }
        if(resIds.length()>1) {
            resIds.deleteCharAt(resIds.length()-1);
        }
        sysRoleCustom.setResIds(resIds.toString());
        return serverResponse;
    }

    @Override
    protected ServerResponse<SysRoleCustom> updateBefore(SysRoleCustom sysRoleCustom) {
        //判断是否有修改权限
        if(!StringUtils.equals(sysRoleCustom.getPermisIds(),sysRoleCustom.getOldPermisIds())) {
            //查询系统角色之前是有拥有权限
            Integer roleId = sysRoleCustom.getRoleId();
            List<SysRolePermis> sysRolePermisList = sysRolePermisMapper.selectByRoleId(roleId);
            if(!CollectionUtils.isEmpty(sysRolePermisList)) {
                //清除原来的权限
                int num = sysRolePermisMapper.realDeleteInRoleId(roleId);
                if(num < 1) {
                    throw new CustomException(ResponseCode.MDF_FAIL);
                }
            }


            //判断是否有设置权限
            String[] permisIds = StringUtils.split(sysRoleCustom.getPermisIds(),",");
            if(!ArrayUtils.isEmpty(permisIds)) {
                sysRolePermisList = Lists.newArrayList();
                for(int i = 0,len = permisIds.length; i < len; i++) {
                    SysRolePermis sysRolePermis = new SysRolePermis(null,sysRoleCustom.getRoleId(),
                            Integer.valueOf(permisIds[i]));
                    sysRolePermisList.add(sysRolePermis);
                }
                int num = sysRolePermisMapper.batchInsert(sysRolePermisList);
                if(num < 1) {
                    throw new CustomException(ResponseCode.MDF_FAIL);
                }
            }
        }

        //判断是否有修改资源
        if(!StringUtils.equals(sysRoleCustom.getResIds(),sysRoleCustom.getOldResIds())) {
            //查询系统角色之前是有拥有资源
            Integer roleId = sysRoleCustom.getRoleId();
            List<SysRoleResource> sysRoleResourceList = sysRoleResourceMapper.selectByRoleId(roleId);
            if(!CollectionUtils.isEmpty(sysRoleResourceList)) {
                //清除原来的资源
                int num = sysRoleResourceMapper.realDeleteInRoleId(roleId);
                if(num < 1) {
                    throw new CustomException(ResponseCode.MDF_FAIL);
                }
            }


            //判断是否有设置资源
            String[] resIds = StringUtils.split(sysRoleCustom.getResIds(),",");
            if(!ArrayUtils.isEmpty(resIds)) {
                sysRoleResourceList = Lists.newArrayList();
                for(int i = 0,len = resIds.length; i < len; i++) {
                    SysRoleResource sysRoleResource = new SysRoleResource(null,sysRoleCustom.getRoleId(),
                            Integer.valueOf(resIds[i]));
                    sysRoleResourceList.add(sysRoleResource);
                }
                int num = sysRoleResourceMapper.batchInsert(sysRoleResourceList);
                if(num < 1) {
                    throw new CustomException(ResponseCode.MDF_FAIL);
                }
            }
        }
        return super.updateBefore(sysRoleCustom);
    }


    @Override
    protected ServerResponse<SysRoleCustom> deleteInIdsAfter(String[] ids) {
        deleteAfter(ids);
        return super.deleteInIdsAfter(ids);
    }

    @Override
    protected ServerResponse<SysRoleCustom> realDeleteInIdsAfter(String[] ids) {
        deleteAfter(ids);
        return super.realDeleteInIdsAfter(ids);
    }

    private void deleteAfter(String[] ids){
        for(int i = 0,len = ids.length; i < len; i++) {
            sysRolePermisMapper.realDeleteInRoleId(Integer.valueOf(ids[i]));
            sysRoleResourceMapper.realDeleteInRoleId(Integer.valueOf(ids[i]));
        }
    }
}
