package cn.sk.mask.business.controller;

import cn.sk.mask.base.controller.BaseController;
import cn.sk.mask.business.pojo.ProductCustom;
import cn.sk.mask.business.pojo.ProductQueryVo;
import cn.sk.mask.business.service.IProductService;
import cn.sk.mask.sys.common.ServerResponse;
import cn.sk.mask.sys.common.SysConst;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * 产品 Controller
 */
@RestController
@RequestMapping("/product")
@RequiresAuthentication
public class ProductController extends BaseController<ProductCustom, ProductQueryVo> {
    private static final String UPDATE_RECORDSTATUS_OPRT = "updateRecordStatus";
    @Value("${sk.httpPrefix}"+ SysConst.UPLOAD_FILE_PREFIX+ SysConst.UPLOAD_FILE_IMG_PREFIX)
    private String imgHttpPrefix;
    @Autowired
    private IProductService productService;



    //更新记录状态，禁用启用切换
    @PostMapping(value = "updateRecordStatus")
    public ServerResponse<ProductCustom> updateRecordStatus(ProductCustom productCustom) {
        //权限校验
        authorityValidate(UPDATE_RECORDSTATUS_OPRT);

        String rs = productCustom.getRecordStatus();
        ServerResponse<ProductCustom> serverResponse = productService.update(productCustom);
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


    @Override
    public ModelAndView initUpdate(ModelAndView model, ProductCustom entity) throws Exception {
        model.addObject("imgHttpPrefix",imgHttpPrefix);
        return super.initUpdate(model, entity);
    }

    /****************************以下是重新父类的方法*****************************/



    //根据oprt返回对应的页面
    @Override
    protected String getPage(String oprt) {
        String prefix = "business/mask/product/";
        if (oprt.equals(QUERY_OPRT)) {
            return prefix + "productQuery";
        }
        if (oprt.equals(UPDATE_OPRT)) {
            return prefix + "product";
        }
        if (oprt.equals(ADD_OPRT)) {
            return prefix + "product";
        }
        return super.getPage(oprt);
    }

    //参数检验
    @Override
    protected ServerResponse<ProductCustom> paramValidate(String oprt, ProductCustom productCustom) {
        switch (oprt) {
            case ADD_OPRT://添加
//                if (StringUtils.isEmpty(sysRoleCustom.getRoleFlag())||StringUtils.isEmpty(sysRoleCustom.getRoleName())) {
//                    return ServerResponse.createByParamError();
//                }
                //默认可用
                productCustom.setRecordStatus(SysConst.RecordStatus.ABLE);
                break;
        }
        return super.paramValidate(oprt, productCustom);
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
