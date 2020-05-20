package cn.sk.mask.business.controller;

import cn.sk.mask.base.controller.BaseController;
import cn.sk.mask.business.pojo.RepairCustom;
import cn.sk.mask.business.pojo.RepairQueryVo;
import cn.sk.mask.business.service.IRepairService;
import cn.sk.mask.sys.common.ServerResponse;
import cn.sk.mask.sys.common.SkLog;
import cn.sk.mask.sys.common.SysConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * 机构信息 Controller
 */
@RestController
@RequestMapping("/repair")
public class RepairController extends BaseController<RepairCustom, RepairQueryVo> {
    //分发
    private static final String DISTR_OPRT = "distr";
    //意见
    private static final String IDEA_OPRT = "idea";

    @Autowired
    private IRepairService repairService;


    //进入分发页面
    @GetMapping(value = "/initDistr")
    public ModelAndView initDistr(ModelAndView model, RepairCustom entity){
        authorityValidate(DISTR_OPRT);
        model.addObject(OPRT_KEY, DISTR_OPRT);
        try {
            init(model, entity);
            addReturnVale(DISTR_OPRT,model);
        } catch (Exception e) {
            model.addObject("msg", SysConst.ResponseMsg.OPRT_FAIL);
        }
        model.setViewName(page(DISTR_OPRT));
        return model;
    }

    @SkLog("报修管理-分发")
    @PostMapping(value = "/distr")
//    public ServerResponse<RepairCustom> distr(RepairCustom repairCustom){
//        authorityValidate(DISTR_OPRT);
//        ServerResponse<RepairCustom> sr = paramValidate(DISTR_OPRT, repairCustom);
//        if (null != sr&&!sr.isSuccess()) {
//            return sr;
//        }
//        RepairCustom obj = getObj(repairCustom);
//        Long wxEmpId = repairCustom.getWxEmpId();
//        obj.setWxEmpId(wxEmpId);
//        ServerResponse<Employee> serverResponse = employeeService.queryEmpByEmpId(wxEmpId);
//        Employee employee = serverResponse.getData();
//        if (ObjectUtils.allNotNull(employee)){
//            obj.setWxPerson(employee.getEmpName());
//            obj.setRecordStatus(Const.RepairRecordStatus.WAIT_REPAIR);
//            obj.setHandleTime(new Date());
//            sr = repairService.update(obj);
//            sr.setMsg("分发成功");
//            return sr;
//        }
//        return ServerResponse.createByErrorMessage("分发失败");
//    }

    //进入意见页面
    @GetMapping(value = "/initIdea")
    public ModelAndView initIdea(ModelAndView model, RepairCustom entity){
        authorityValidate(IDEA_OPRT);
        model.addObject(OPRT_KEY, IDEA_OPRT);
        try {
            init(model, entity);
            addReturnVale(IDEA_OPRT,model);
        } catch (Exception e) {
            model.addObject("msg", SysConst.ResponseMsg.OPRT_FAIL);
        }
        model.setViewName(page(IDEA_OPRT));
        return model;
    }

    @SkLog("报修管理-意见")
    @PostMapping(value = "/idea")
    public ServerResponse<RepairCustom> idea(RepairCustom repairCustom){
        authorityValidate(IDEA_OPRT);
        ServerResponse<RepairCustom> sr = paramValidate(IDEA_OPRT, repairCustom);
        if (null != sr&&!sr.isSuccess()) {
            return sr;
        }
        RepairCustom obj = getObj(repairCustom);

        obj.setRpIdea(repairCustom.getRpIdea());
        return repairService.update(obj);
    }



    /****************************以下是重新父类的方法*****************************/

    //根据oprt返回对应的页面
    @Override
    protected String getPage(String oprt) {
        String prefix = "business/repair/";
        if (oprt.equals(QUERY_OPRT)) {
            return prefix + "repairQuery";
        }
        if (oprt.equals(QUERYDETAIL_OPRT)) {
            return prefix + "repair";
        }
        if (oprt.equals(DISTR_OPRT)) {
            return prefix + "repair";
        }
        if (oprt.equals(IDEA_OPRT)) {
            return prefix + "repair";
        }
//        if (oprt.equals(ADD_OPRT)) {
//            return prefix + "repair";
//        }
        return super.getPage(oprt);
    }

    //参数检验
    @Override
    protected ServerResponse<RepairCustom> paramValidate(String oprt, RepairCustom organCustom) {
//        switch (oprt) {
//            case ADD_OPRT://添加
//
//                break;
//        }
        return super.paramValidate(oprt, organCustom);
    }

    //权限校验
    @Override
    protected void authorityValidate(String oprt) {
        switch (oprt) {
//            case QUERYDETAIL_OPRT://查看详情
//                SecurityUtils.getSubject().checkPermission(Const.ShiroPermis.Repair.QUERYDETAIL);
//                break;
//            case DISTR_OPRT://分发
//                SecurityUtils.getSubject().checkPermission(Const.ShiroPermis.Repair.DISTR);
//                break;
//            case IDEA_OPRT://意见
//                SecurityUtils.getSubject().checkPermission(Const.ShiroPermis.Repair.IDEA);
//                break;
//            case ADD_OPRT://添加
//                SecurityUtils.getSubject().checkPermission(Const.ShiroPermis.Organ.ADD);
//                break;
//            case UPDATE_OPRT://修改
//                SecurityUtils.getSubject().checkPermission(Const.ShiroPermis.Organ.UPDATE);
//                break;
//            case DEL_OPRT://删除
//                SecurityUtils.getSubject().checkPermission(Const.ShiroPermis.Organ.DEL);
//                break;
//            case REAL_DEL_OPRT://硬删除
//                SecurityUtils.getSubject().checkPermission(Const.ShiroPermis.Organ.REAL_DEL);
//                break;
//            case BATCH_DEL_OPRT://批量删除
//                SecurityUtils.getSubject().checkPermission(Const.ShiroPermis.Organ.BATCH_DEL);
//                break;
//            case BATCH_REAL_DEL_OPRT://批量硬删除
//                SecurityUtils.getSubject().checkPermission(Const.ShiroPermis.Organ.BATCH_REAL_DEL);
//                break;
        }
    }

}
