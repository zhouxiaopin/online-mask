package cn.sk.mask.business.controller;

import cn.sk.mask.base.controller.BaseController;
import cn.sk.mask.business.common.Const;
import cn.sk.mask.business.pojo.CategoryCustom;
import cn.sk.mask.business.pojo.CategoryQueryVo;
import cn.sk.mask.business.service.ICategoryService;
import cn.sk.mask.sys.common.ServerResponse;
import cn.sk.mask.sys.common.SysConst;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 产品类别 Controller
 */
@RestController
@RequestMapping("/category")
@RequiresAuthentication
public class CategoryController extends BaseController<CategoryCustom, CategoryQueryVo> {
    private static final String UPDATE_RECORDSTATUS_OPRT = "updateRecordStatus";

    @Autowired
    private ICategoryService categoryService;



    //更新记录状态，禁用启用切换
    @PostMapping(value = "updateRecordStatus")
    public ServerResponse<CategoryCustom> updateRecordStatus(CategoryCustom categoryCustom) {
        //权限校验
        authorityValidate(UPDATE_RECORDSTATUS_OPRT);

        String rs = categoryCustom.getRecordStatus();
        ServerResponse<CategoryCustom> serverResponse = categoryService.update(categoryCustom);
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
//    @PostMapping(value = "queryCategoryTree")
//    public ServerResponse<List<Map<String,Object>>> queryCategoryTree(CategoryQueryVo categoryQueryVo) {
//        return categoryService.queryCategoryTree(categoryQueryVo);
//    }



    /****************************以下是重新父类的方法*****************************/

    //根据oprt返回对应的页面
    @Override
    protected String getPage(String oprt) {
        String prefix = "business/mask/category/";
        if (oprt.equals(QUERY_OPRT)) {
            return prefix + "categoryQuery";
        }
        if (oprt.equals(UPDATE_OPRT)) {
            return prefix + "category";
        }
        if (oprt.equals(ADD_OPRT)) {
            return prefix + "category";
        }
        return super.getPage(oprt);
    }

    //参数检验
    @Override
    protected ServerResponse<CategoryCustom> paramValidate(String oprt, CategoryCustom categoryCustom) {
        switch (oprt) {
            case ADD_OPRT://添加
//                if (StringUtils.isEmpty(sysRoleCustom.getRoleFlag())||StringUtils.isEmpty(sysRoleCustom.getRoleName())) {
//                    return ServerResponse.createByParamError();
//                }
                if(ObjectUtils.isEmpty(categoryCustom.getParentId())) {
                    categoryCustom.setParentId(Const.DEFAULT_PARENTID);
                }
                //默认可用
                categoryCustom.setRecordStatus(SysConst.RecordStatus.ABLE);
                break;
        }
        return super.paramValidate(oprt, categoryCustom);
    }

    //权限校验
//    @Override
//    protected void authorityValidate(String oprt) {
//        switch (oprt) {
//            case ADD_OPRT://添加
//                SecurityUtils.getSubject().checkPermission(Const.ShiroPermis.Category.ADD);
//                break;
//            case UPDATE_RECORDSTATUS_OPRT://修改记录状态（禁用/启用）
//                SecurityUtils.getSubject().checkPermission(Const.ShiroPermis.Category.UPDATE_RECORDSTATUS);
//                break;
//            case UPDATE_OPRT://修改
//                SecurityUtils.getSubject().checkPermission(Const.ShiroPermis.Category.UPDATE);
//                break;
//            case DEL_OPRT://删除
//                SecurityUtils.getSubject().checkPermission(Const.ShiroPermis.Category.DEL);
//                break;
//            case REAL_DEL_OPRT://硬删除
//                SecurityUtils.getSubject().checkPermission(Const.ShiroPermis.Category.REAL_DEL);
//                break;
//            case BATCH_DEL_OPRT://批量删除
//                SecurityUtils.getSubject().checkPermission(Const.ShiroPermis.Category.BATCH_DEL);
//                break;
//            case BATCH_REAL_DEL_OPRT://批量硬删除
//                SecurityUtils.getSubject().checkPermission(Const.ShiroPermis.Category.BATCH_REAL_DEL);
//                break;
//        }
//    }

}
