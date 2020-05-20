package cn.sk.mask.sys.service.impl;

import cn.sk.mask.sys.common.SysConst;
import cn.sk.mask.sys.mapper.SysDictMapper;
import cn.sk.mask.sys.mapper.SysResourceMapper;
import cn.sk.mask.sys.mapper.SysRoleMapper;
import cn.sk.mask.sys.pojo.SysDictCustom;
import cn.sk.mask.sys.pojo.SysDictQueryVo;
import cn.sk.mask.sys.pojo.SysUserCustom;
import cn.sk.mask.sys.pojo.TreeNode;
import cn.sk.mask.sys.service.ISysToPageService;
import cn.sk.mask.sys.utils.SysUtils;
import cn.sk.mask.sys.utils.TreeUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class SysToPageServiceImpl implements ISysToPageService {
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysResourceMapper sysResourceMapper;
    @Autowired
    private SysDictMapper sysDictMapper;


    @Override
    public ModelAndView index(ModelAndView mv, HttpServletResponse response) {
        SysUserCustom sysUserInfo = SysUtils.getSysUser();
        if(ObjectUtils.isEmpty(sysUserInfo)) {
            Subject currentUser = SecurityUtils.getSubject();

            if (currentUser.isAuthenticated()) {
                currentUser.logout();
            }
            try {
                response.sendRedirect("sysUser/initLogin");
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                mv.setViewName("login");
            }
            return mv;
        }

        mv.addObject("sysUserInfo",sysUserInfo);
        //        SysUserCustom sysUserInfo = (SysUserCustom)SecurityUtils.getSubject().getPrincipal();

        Map<String,Object> params = Maps.newHashMap();
        params.put("userId",sysUserInfo.getuId());
        params.put("recordStatus", SysConst.RecordStatus.ABLE);
        List<Map<String,Object>> sysRoleCustoms = sysRoleMapper.selectListByUserId(params);

        Set<Integer> roleIds = Sets.newHashSet();
        Map<String,Object> sysRoleItem;
        if(!CollectionUtils.isEmpty(sysRoleCustoms)) {
            for (int i = 0, len = sysRoleCustoms.size(); i < len; i++){
                sysRoleItem = sysRoleCustoms.get(i);
                roleIds.add(Integer.valueOf(sysRoleItem.get("roleId").toString()));
            }
        }

        if(!CollectionUtils.isEmpty(sysRoleCustoms)) {
            params.clear();
            params.put("roleIds",roleIds);
            params.put("recordStatus", SysConst.RecordStatus.ABLE);
            params.put("rType", SysConst.Permis.MENU);
            params.put("orderBy", "r_sort");
//            List<Map<String,Object>> sysPermisCustoms = sysPermisMapper.selectListByRoleId(params);
            List<Map<String,Object>> sysResourceCustoms = sysResourceMapper.selectListByRoleId(params);
            List<TreeNode> treeNodes = Lists.newArrayList();
            Map<String,Object> sysResourceItem;
            TreeNode treeNode;

            //获取左边图片key-value对
            SysDictQueryVo sysDictQueryVo = new SysDictQueryVo();
            SysDictCustom condition = new SysDictCustom();

            sysDictQueryVo.getIsNoLike().put("dictType",true);

            condition.setDictType(SysConst.Dict.SysResource.LEFT_ICON);
            condition.setRecordStatus(SysConst.RecordStatus.ABLE);

            sysDictQueryVo.setSysDictCustom(condition);
            List<SysDictCustom> sysDictCustoms = sysDictMapper.selectListByQueryVo(sysDictQueryVo);
            //左边图标
            Map<String,String> leftIconMap = Maps.newHashMap();
            for(int i = 0,len = sysDictCustoms.size(); i < len; i++) {
                SysDictCustom sysDictCustom = sysDictCustoms.get(i);
                leftIconMap.put(sysDictCustom.getDictCode(),sysDictCustom.getField1());
            }

            for(int i = 0, len = sysResourceCustoms.size(); i < len; i++) {
                sysResourceItem = sysResourceCustoms.get(i);
                treeNode = new TreeNode();
                treeNode.setId(sysResourceItem.get("rId").toString());
                treeNode.setLevel(Integer.valueOf(sysResourceItem.get("rLevel").toString()));
                treeNode.setParentId(sysResourceItem.get("parentId").toString());
                treeNode.setName(sysResourceItem.get("rName").toString());
                Map<String,Object> attrs = Maps.newHashMap();
                attrs.put("leftIcon",leftIconMap.get(sysResourceItem.get("leftIcon").toString()));
                attrs.put("url",sysResourceItem.get("rUrl").toString());
                treeNode.setAttrs(attrs);

                treeNodes.add(treeNode);
            }
            treeNodes = TreeUtil.bulid(treeNodes, SysConst.SysResource.DEFAULT_PARENTID.toString());
            if(treeNodes.size() >= 1) {
                mv.addObject("menuList", treeNodes);
            }
        }

//        SecurityUtils.getSubject().
        mv.setViewName("common/index");
        return mv;
    }
}
