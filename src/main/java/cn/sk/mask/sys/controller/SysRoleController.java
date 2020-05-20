package cn.sk.mask.sys.controller;

import cn.sk.mask.base.controller.BaseController;
import cn.sk.mask.sys.common.SysConst;
import cn.sk.mask.sys.common.ServerResponse;
import cn.sk.mask.sys.pojo.SysRoleCustom;
import cn.sk.mask.sys.pojo.SysRoleQueryVo;
import cn.sk.mask.sys.service.ISysRoleService;
import cn.sk.mask.sys.utils.SysUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 系统角色 Controller
 */
@RestController
@RequestMapping("/sysRole")
@RequiresAuthentication
public class SysRoleController extends BaseController<SysRoleCustom, SysRoleQueryVo> {
    private static final String UPDATE_RECORDSTATUS_OPRT = "updateRecordStatus";
    @Autowired
    private ISysRoleService sysRoleService;


    //更新记录状态，禁用启用切换
    @PostMapping(value = "updateRecordStatus")
    public ServerResponse<SysRoleCustom> updateRecordStatus(SysRoleCustom sysRoleCustom) {
        //权限校验
        authorityValidate(UPDATE_RECORDSTATUS_OPRT);

        String rs = sysRoleCustom.getRecordStatus();
        ServerResponse<SysRoleCustom> serverResponse = sysRoleService.update(sysRoleCustom);
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


    /****************************以下是重新父类的方法*****************************/

    //根据oprt返回对应的页面
    @Override
    protected String getPage(String oprt) {
        String prefix = "sys/sysRole/";
        if (oprt.equals(QUERY_OPRT)) {
            return prefix + "sysRoleQuery";
        }
        if (oprt.equals(UPDATE_OPRT)) {
            return prefix + "sysRole";
        }
        if (oprt.equals(ADD_OPRT)) {
            return prefix + "sysRole";
        }
        return super.getPage(oprt);
    }

    //参数检验
    @Override
    protected ServerResponse<SysRoleCustom> paramValidate(String oprt, SysRoleCustom sysRoleCustom) {
        SysRoleQueryVo sysRoleQueryVo;
        SysRoleCustom condition;
        ServerResponse<List<SysRoleCustom>> serverResponse;
        switch (oprt) {
            case ADD_OPRT://添加
                if (StringUtils.isEmpty(sysRoleCustom.getRoleFlag())||StringUtils.isEmpty(sysRoleCustom.getRoleName())) {
                    return ServerResponse.createByParamError();
                }

                //判断角色标识是否存在
                sysRoleQueryVo = new SysRoleQueryVo();
                condition = new SysRoleCustom();

                sysRoleQueryVo.getIsNoLike().put("roleFlag",true);

                condition.setRoleFlag(sysRoleCustom.getRoleFlag());

                sysRoleQueryVo.setSysRoleCustom(condition);
                serverResponse = this.queryAllByCondition(sysRoleQueryVo);
                if(!CollectionUtils.isEmpty(serverResponse.getData())){
                    return ServerResponse.createByErrorMessage("角色标识已存在");
                }

                sysRoleCustom.setOptId(SysUtils.getUserId());
                //默认可用
                sysRoleCustom.setRecordStatus(SysConst.RecordStatus.ABLE);
                break;
            case UPDATE_OPRT://修改
                //判断角色标识是否存在
                sysRoleQueryVo = new SysRoleQueryVo();
                condition = new SysRoleCustom();

                sysRoleQueryVo.getIsNoLike().put("roleFlag",true);

                condition.setRoleFlag(sysRoleCustom.getRoleFlag());

                sysRoleQueryVo.setSysRoleCustom(condition);
                serverResponse = this.queryAllByCondition(sysRoleQueryVo);
                List<SysRoleCustom> sysRoleCustoms = serverResponse.getData();
                if(!CollectionUtils.isEmpty(sysRoleCustoms)){
                    for (int i = 0, len = sysRoleCustoms.size(); i < len; i++){
                        if(sysRoleCustom.getRoleId() != sysRoleCustoms.get(i).getRoleId()) {
                            return ServerResponse.createByErrorMessage("角色标识已存在");
                        }
                    }
                }
                break;
        }
        return super.paramValidate(oprt, sysRoleCustom);
    }

    //权限校验
    @Override
    protected void authorityValidate(String oprt) {
        switch (oprt) {
            case ADD_OPRT://添加
                SecurityUtils.getSubject().checkPermission(SysConst.ShiroPermis.SysRole.ADD);
                break;
            case UPDATE_RECORDSTATUS_OPRT://修改记录状态（禁用/启用）
                SecurityUtils.getSubject().checkPermission(SysConst.ShiroPermis.SysRole.UPDATE_RECORDSTATUS);
                break;
            case UPDATE_OPRT://修改
                SecurityUtils.getSubject().checkPermission(SysConst.ShiroPermis.SysRole.UPDATE);
                break;
            case DEL_OPRT://删除
                SecurityUtils.getSubject().checkPermission(SysConst.ShiroPermis.SysRole.DEL);
                break;
            case REAL_DEL_OPRT://硬删除
                SecurityUtils.getSubject().checkPermission(SysConst.ShiroPermis.SysRole.REAL_DEL);
                break;
            case BATCH_DEL_OPRT://批量删除
                SecurityUtils.getSubject().checkPermission(SysConst.ShiroPermis.SysRole.BATCH_DEL);
                break;
            case BATCH_REAL_DEL_OPRT://批量硬删除
                SecurityUtils.getSubject().checkPermission(SysConst.ShiroPermis.SysRole.BATCH_REAL_DEL);
                break;
        }
    }
}
