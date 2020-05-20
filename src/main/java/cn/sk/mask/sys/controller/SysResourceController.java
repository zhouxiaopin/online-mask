package cn.sk.mask.sys.controller;

import cn.sk.mask.base.controller.BaseController;
import cn.sk.mask.sys.common.SysConst;
import cn.sk.mask.sys.common.ServerResponse;
import cn.sk.mask.sys.pojo.SysResourceCustom;
import cn.sk.mask.sys.pojo.SysResourceQueryVo;
import cn.sk.mask.sys.service.ISysResourceService;
import cn.sk.mask.sys.utils.SysUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 系统资源 Controller
 */
@RestController
@RequestMapping("/sysResource")
@RequiresAuthentication
public class SysResourceController extends BaseController<SysResourceCustom, SysResourceQueryVo> {
    private static final String UPDATE_RECORDSTATUS_OPRT = "updateRecordStatus";
    @Autowired
    private ISysResourceService sysResourceService;



    //更新记录状态，禁用启用切换
    @PostMapping(value = "updateRecordStatus")
    public ServerResponse<SysResourceCustom> updateRecordStatus(SysResourceCustom sysResourceCustom) {
        //权限校验
        authorityValidate(UPDATE_RECORDSTATUS_OPRT);

        String rs = sysResourceCustom.getRecordStatus();
        ServerResponse<SysResourceCustom> serverResponse = sysResourceService.update(sysResourceCustom);
        if (serverResponse.isSuccess()) {
            if (StringUtils.equals(rs, SysConst.RecordStatus.ABLE)) {
                serverResponse.setMsg("启用成功");
            } else if (StringUtils.equals(rs, SysConst.RecordStatus.DISABLE)) {
                serverResponse.setMsg("禁用成功");
            }
        } else {
            if (StringUtils.equals(rs, SysConst.RecordStatus.ABLE)) {
                serverResponse.setMsg("启用失败");
            } else if (StringUtils.equals(rs, SysConst.RecordStatus.DISABLE)) {
                serverResponse.setMsg("禁用失败");
            }
        }
        return serverResponse;
    }

    //获取树形
    @PostMapping(value = "querySysResourceTree")
    public ServerResponse<List<Map<String,Object>>> querySysResourceTree(SysResourceQueryVo sysResourceQueryVo) {
        return sysResourceService.querySysResourceTree(sysResourceQueryVo);
    }



    /****************************以下是重新父类的方法*****************************/

    //根据oprt返回对应的页面
    @Override
    protected String getPage(String oprt) {
        String prefix = "sys/sysResource/";
        if (oprt.equals(QUERY_OPRT)) {
            return prefix + "sysResourceQuery";
        }
        if (oprt.equals(UPDATE_OPRT)) {
            return prefix + "sysResource";
        }
        if (oprt.equals(ADD_OPRT)) {
            return prefix + "sysResource";
        }
        return super.getPage(oprt);
    }

    //参数检验
    @Override
    protected ServerResponse<SysResourceCustom> paramValidate(String oprt, SysResourceCustom sysResourceCustom) {
        switch (oprt) {
            case ADD_OPRT://添加
//                if (StringUtils.isEmpty(sysRoleCustom.getRoleFlag())||StringUtils.isEmpty(sysRoleCustom.getRoleName())) {
//                    return ServerResponse.createByParamError();
//                }

                if(ObjectUtils.isEmpty(sysResourceCustom.getParentId())) {
                    sysResourceCustom.setParentId(SysConst.Permis.DEFAULT_PARENTID);
                }
                sysResourceCustom.setOptId(SysUtils.getUserId());
                //默认可用
                sysResourceCustom.setRecordStatus(SysConst.RecordStatus.ABLE);
                break;
        }
        return super.paramValidate(oprt, sysResourceCustom);
    }

    //权限校验
    @Override
    protected void authorityValidate(String oprt) {
        switch (oprt) {
            case ADD_OPRT://添加
                SecurityUtils.getSubject().checkPermission(SysConst.ShiroPermis.SysResource.ADD);
                break;
            case UPDATE_RECORDSTATUS_OPRT://修改记录状态（禁用/启用）
                SecurityUtils.getSubject().checkPermission(SysConst.ShiroPermis.SysResource.UPDATE_RECORDSTATUS);
                break;
            case UPDATE_OPRT://修改
                SecurityUtils.getSubject().checkPermission(SysConst.ShiroPermis.SysResource.UPDATE);
                break;
            case DEL_OPRT://删除
                SecurityUtils.getSubject().checkPermission(SysConst.ShiroPermis.SysResource.DEL);
                break;
            case REAL_DEL_OPRT://硬删除
                SecurityUtils.getSubject().checkPermission(SysConst.ShiroPermis.SysResource.REAL_DEL);
                break;
            case BATCH_DEL_OPRT://批量删除
                SecurityUtils.getSubject().checkPermission(SysConst.ShiroPermis.SysResource.BATCH_DEL);
                break;
            case BATCH_REAL_DEL_OPRT://批量硬删除
                SecurityUtils.getSubject().checkPermission(SysConst.ShiroPermis.SysResource.BATCH_REAL_DEL);
                break;
        }
    }

}
