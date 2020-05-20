package cn.sk.mask.sys.controller;

import cn.sk.mask.base.controller.BaseController;
import cn.sk.mask.sys.common.ServerResponse;
import cn.sk.mask.sys.common.SysConst;
import cn.sk.mask.sys.pojo.SysDictCustom;
import cn.sk.mask.sys.pojo.SysDictQueryVo;
import cn.sk.mask.sys.service.ISysDictService;
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
 * 系统数字字典 Controller
 */
@RestController
@RequestMapping("/sysDict")
@RequiresAuthentication
public class SysDictController extends BaseController<SysDictCustom, SysDictQueryVo> {
    private static final String UPDATE_RECORDSTATUS_OPRT = "updateRecordStatus";
    @Autowired
    private ISysDictService sysDictService;



    //更新记录状态，禁用启用切换
    @PostMapping(value = "updateRecordStatus")
    public ServerResponse<SysDictCustom> updateRecordStatus(SysDictCustom sysDictCustom) {
        //权限校验
        authorityValidate(UPDATE_RECORDSTATUS_OPRT);

        String rs = sysDictCustom.getRecordStatus();
        ServerResponse<SysDictCustom> serverResponse = sysDictService.update(sysDictCustom);
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
        String prefix = "sys/sysDict/";
        if (oprt.equals(QUERY_OPRT)) {
            return prefix + "sysDictQuery";
        }
        if (oprt.equals(UPDATE_OPRT)) {
            return prefix + "sysDict";
        }
        if (oprt.equals(ADD_OPRT)) {
            return prefix + "sysDict";
        }
        return super.getPage(oprt);
    }
    //参数检验
    @Override
    protected ServerResponse<SysDictCustom> paramValidate(String oprt, SysDictCustom sysDictCustom) {
        if(StringUtils.equals(oprt,ADD_OPRT)) {//添加
            //判断字典编码是否存在
            SysDictQueryVo sysDictQueryVo = new SysDictQueryVo();
            SysDictCustom condition = new SysDictCustom();

            sysDictQueryVo.getIsNoLike().put("dictType",true);

            condition.setDictType(sysDictCustom.getDictType());
            condition.setDictCode(sysDictCustom.getDictCode());

            sysDictQueryVo.setSysDictCustom(condition);
            ServerResponse<List<SysDictCustom>> serverResponse = this.queryAllByCondition(sysDictQueryVo);
            if(!CollectionUtils.isEmpty(serverResponse.getData())){
                return ServerResponse.createByErrorMessage("字典编码已存在");
            }

            //默认可用
            sysDictCustom.setRecordStatus(SysConst.RecordStatus.ABLE);
        }
        if(StringUtils.equals(oprt,UPDATE_OPRT)) {//修改
            //判断字典编码是否存在
            SysDictQueryVo sysDictQueryVo = new SysDictQueryVo();
            SysDictCustom condition = new SysDictCustom();

            sysDictQueryVo.getIsNoLike().put("dictType",true);

            condition.setDictType(sysDictCustom.getDictType());
            condition.setDictCode(sysDictCustom.getDictCode());

            sysDictQueryVo.setSysDictCustom(condition);
            ServerResponse<List<SysDictCustom>> serverResponse = this.queryAllByCondition(sysDictQueryVo);
            List<SysDictCustom> sysDictCustoms = serverResponse.getData();
            if(!CollectionUtils.isEmpty(sysDictCustoms)){
                for (int i = 0, len = sysDictCustoms.size(); i < len; i++){
                    if(sysDictCustom.getDictId() != sysDictCustoms.get(i).getDictId()) {
                        return ServerResponse.createByErrorMessage("字典编码已存在");
                    }
                }

            }
        }

        return super.paramValidate(oprt, sysDictCustom);
    }
    //权限校验
    @Override
    protected void authorityValidate(String oprt) {
        switch (oprt) {
            case ADD_OPRT://添加
                SecurityUtils.getSubject().checkPermission(SysConst.ShiroPermis.SysDict.ADD);
                break;
            case UPDATE_RECORDSTATUS_OPRT://修改记录状态（禁用/启用）
                SecurityUtils.getSubject().checkPermission(SysConst.ShiroPermis.SysDict.UPDATE_RECORDSTATUS);
                break;
            case UPDATE_OPRT://修改
                SecurityUtils.getSubject().checkPermission(SysConst.ShiroPermis.SysDict.UPDATE);
                break;
            case DEL_OPRT://删除
                SecurityUtils.getSubject().checkPermission(SysConst.ShiroPermis.SysDict.DEL);
                break;
            case REAL_DEL_OPRT://硬删除
                SecurityUtils.getSubject().checkPermission(SysConst.ShiroPermis.SysDict.REAL_DEL);
                break;
            case BATCH_DEL_OPRT://批量删除
                SecurityUtils.getSubject().checkPermission(SysConst.ShiroPermis.SysDict.BATCH_DEL);
                break;
            case BATCH_REAL_DEL_OPRT://批量硬删除
                SecurityUtils.getSubject().checkPermission(SysConst.ShiroPermis.SysDict.BATCH_REAL_DEL);
                break;
        }
    }
}
