package cn.sk.mask.sys.controller;

import cn.sk.mask.base.controller.BaseController;
import cn.sk.mask.sys.common.*;
import cn.sk.mask.sys.pojo.SysUserCustom;
import cn.sk.mask.sys.pojo.SysUserQueryVo;
import cn.sk.mask.sys.service.ISysUserService;
import cn.sk.mask.sys.utils.ShiroUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * 系统用户 Controller
 */
@RestController
@RequestMapping("/sysUser")
public class SysUserController extends BaseController<SysUserCustom, SysUserQueryVo> {

    private static final String UPDATE_PASSWORD_OPRT = "updatePassword";
    private static final String UPDATE_RECORDSTATUS_OPRT = "updateRecordStatus";
    private static final String LOGIN_OPRT = "login";

    @Autowired
    private ISysUserService sysUserService;


    //进入登录页面
    @GetMapping(value = "/initLogin")
    public ModelAndView initLogin(ModelAndView model){
        model.addObject(OPRT_KEY, LOGIN_OPRT);
        model.setViewName(page(LOGIN_OPRT));
        return model;
    }

    @SkLog(value ="登录系统", saveParams=false)
    @PostMapping(value = "/login")
    public ServerResponse login(SysUserCustom sysUserCustom){

        try {
            Subject currentUser = SecurityUtils.getSubject();


            if (!currentUser.isAuthenticated()) {
                // 把用户名和密码封装为 UsernamePasswordToken 对象
                String account = sysUserCustom.getUserName();
                String password = sysUserCustom.getPassword();
                UsernamePasswordToken token = new UsernamePasswordToken(account, password);
//            token.setRememberMe(true);
                // 执行登录.
                currentUser.login(token);

//                SysUserCustom sysUserInfo = (SysUserCustom) currentUser.getPrincipal();
//                currentUser.getSession().setAttribute(SysConst.SessionKey.SYSUSER_INFO,sysUserInfo);
//                session.setAttribute("adminInfo",adminInfo);
            }

        }catch (UnknownAccountException uae) {
            throw new CustomException(ResponseCode.LOGIN_NO_EXIST);
        }catch (LockedAccountException lae)  {
            throw new CustomException(ResponseCode.LOGIN_NO_USE);
        }catch (IncorrectCredentialsException lce){
            throw new CustomException(ResponseCode.LOGIN_PWD_FAIL);
        }catch (AuthenticationException ae) { // 所有认证时异常的父类.
            throw new CustomException(ResponseCode.LOGIN_FAIL);
        }
        return ServerResponse.createBySuccessMessage(ResponseCode.LOGIN_SUCCESS.getMsg());

    }

    @SkLog(value ="退出系统", saveParams=false)
    @GetMapping(value = "/sysLogout")
    public ModelAndView sysLogout(ModelAndView model){
        Subject currentUser = SecurityUtils.getSubject();

        if (currentUser.isAuthenticated()) {
            currentUser.logout();
        }

//        session.setAttribute("adminInfo", null);
//        session.removeAttribute(SysConst.SessionKey.SYSUSER_INFO);
//        session.invalidate();
        model.setViewName("redirect:initLogin");

        return model;
//        return "redirect:/index.jsp";
    }

    //更新记录状态，禁用启用切换
    @PostMapping(value = "updateRecordStatus")
    public ServerResponse<SysUserCustom> updateRecordStatus(SysUserCustom sysUserCustom) {
        //权限校验
        authorityValidate(UPDATE_RECORDSTATUS_OPRT);

        String rs = sysUserCustom.getRecordStatus();

        ServerResponse<SysUserCustom> serverResponse = sysUserService.update(sysUserCustom);
        if (StringUtils.equals(rs, SysConst.RecordStatus.ABLE)) {
            if (serverResponse.isSuccess()) {
                serverResponse.setMsg("启用成功");
            }else{
                serverResponse.setMsg("启用失败");
            }
            logUtil.writLog(this.getClass(),"updateRecordStatus","系统用户启用");
        } else if (StringUtils.equals(rs, SysConst.RecordStatus.DISABLE)) {
            if (serverResponse.isSuccess()) {
                serverResponse.setMsg("禁用成功");
            }else{
                serverResponse.setMsg("禁用失败");
            }
            logUtil.writLog(this.getClass(),"updateRecordStatus","系统用户禁用");
        }

        return serverResponse;
    }

    //进入修改密码页面
    @GetMapping(value = "/initUpdatePassword")
    public ModelAndView initUpdatePassword(ModelAndView model, SysUserCustom sysUserCustom) throws Exception {
        //权限校验
        authorityValidate(UPDATE_PASSWORD_OPRT);
        model.addObject(OPRT_KEY, UPDATE_PASSWORD_OPRT);
        try {
            init(model, sysUserCustom);
        } catch (Exception e) {
            model.addObject("msg", SysConst.ResponseMsg.OPRT_FAIL);
        }
        model.setViewName(page(UPDATE_PASSWORD_OPRT));
        return model;
    }

    //修改密码
    @SkLog(value ="修改密码", saveParams=false)
    @PostMapping(value = "updatePassword")
    public ServerResponse<SysUserCustom> updatePassword(SysUserCustom sysUserCustom) {
        //权限校验
        authorityValidate(UPDATE_PASSWORD_OPRT);
        //参数检验
        ServerResponse sr = paramValidate(UPDATE_PASSWORD_OPRT, sysUserCustom);
        if (!sr.isSuccess()) {
            return sr;
        }

        //业务逻辑
        SysUserCustom oldObj = getObj(sysUserCustom);
        //判断盐值是否存在
        String salt = oldObj.getSalt();
        if (StringUtils.isEmpty(salt)) {
            salt = ShiroUtils.DEFALT_SALT;
            sysUserCustom.setSalt(salt);
        }
        sysUserCustom.setPassword(ShiroUtils.getMd5Pwd(salt, sysUserCustom.getPassword()));

        return sysUserService.update(sysUserCustom);
    }



    /****************************以下是重新父类的方法*****************************/
    //修改之前
    @Override
    protected ServerResponse<SysUserCustom> updateBefore(SysUserCustom oldObj, SysUserCustom sysUserCustom) {
        //判断是否有修改密码
        if (!StringUtils.equals(sysUserCustom.getPassword(), sysUserCustom.getPassword2())) {
            //判断盐值是否存在
            String salt = oldObj.getSalt();
            if (StringUtils.isEmpty(salt)) {
                salt = ShiroUtils.DEFALT_SALT;
                sysUserCustom.setSalt(salt);
            }
            sysUserCustom.setPassword(ShiroUtils.getMd5Pwd(salt, sysUserCustom.getPassword()));
        }
        return super.updateBefore(oldObj, sysUserCustom);
    }

    //根据oprt返回对应的页面
    @Override
    protected String getPage(String oprt) {
        String prefix = "sys/sysUser/";
        if (oprt.equals(LOGIN_OPRT)) {
            return "login";
        }
        if (oprt.equals(QUERY_OPRT)) {
            return prefix + "sysUserQuery";
        }
        if (oprt.equals(UPDATE_OPRT)) {
            return prefix + "sysUser";
        }
        if (oprt.equals(ADD_OPRT)) {
            return prefix + "sysUser";
        }
        if (oprt.equals(UPDATE_PASSWORD_OPRT)) {
            return prefix + "sysUser";
        }
        return super.getPage(oprt);
    }

    //参数检验
    @Override
    protected ServerResponse<SysUserCustom> paramValidate(String oprt, SysUserCustom sysUserCustom) {
        ServerResponse<List<SysUserCustom>> serverResponse;
        SysUserQueryVo sysUserQueryVo;
        SysUserCustom condition;
        switch (oprt) {
            case ADD_OPRT://添加
                if (!StringUtils.equals(sysUserCustom.getPassword(), sysUserCustom.getPassword2())) {
                    return ServerResponse.createByErrorMessage("两次密码输入不一致");
                }

                //判断字用户名是否存在
                sysUserQueryVo = new SysUserQueryVo();
                condition = new SysUserCustom();

                sysUserQueryVo.getIsNoLike().put("userName",true);

                condition.setUserName(sysUserCustom.getUserName());

                sysUserQueryVo.setSysUserCustom(condition);
                serverResponse = this.queryAllByCondition(sysUserQueryVo);
                if(!CollectionUtils.isEmpty(serverResponse.getData())){
                    return ServerResponse.createByErrorMessage("用户名已存在");
                }

                break;
            case UPDATE_OPRT://修改
                //判断字用户名是否存在
                sysUserQueryVo = new SysUserQueryVo();
                condition = new SysUserCustom();

                sysUserQueryVo.getIsNoLike().put("userName",true);

                condition.setUserName(sysUserCustom.getUserName());

                sysUserQueryVo.setSysUserCustom(condition);
                serverResponse = this.queryAllByCondition(sysUserQueryVo);
                List<SysUserCustom> sysUserCustoms = serverResponse.getData();
                if(!CollectionUtils.isEmpty(sysUserCustoms)){
                    for (int i = 0, len = sysUserCustoms.size(); i < len; i++){
                        if(sysUserCustom.getuId() != sysUserCustoms.get(i).getuId()) {
                            return ServerResponse.createByErrorMessage("用户名已存在");
                        }
                    }
                }

                break;
            case UPDATE_PASSWORD_OPRT://修改密码
                if (StringUtils.isEmpty(sysUserCustom.getPassword()) || StringUtils.isEmpty(sysUserCustom.getPassword2())) {
                    return ServerResponse.createByErrorMessage("密码不能为空");
                }
                if (!StringUtils.equals(sysUserCustom.getPassword(), sysUserCustom.getPassword2())) {
                    return ServerResponse.createByErrorMessage("两次密码输入不一致");
                }

                break;
        }
        return super.paramValidate(oprt, sysUserCustom);
    }

    //权限校验
    @Override
    protected void authorityValidate(String oprt) {
        switch (oprt) {
            case ADD_OPRT://添加
                SecurityUtils.getSubject().checkPermission(SysConst.ShiroPermis.SysUser.ADD);
                break;
            case UPDATE_RECORDSTATUS_OPRT://修改记录状态（禁用/启用）
                SecurityUtils.getSubject().checkPermission(SysConst.ShiroPermis.SysUser.UPDATE_RECORDSTATUS);
                break;
            case UPDATE_OPRT://修改
                SecurityUtils.getSubject().checkPermission(SysConst.ShiroPermis.SysUser.UPDATE);
                break;
            case UPDATE_PASSWORD_OPRT://修改密码
                SecurityUtils.getSubject().checkPermission(SysConst.ShiroPermis.SysUser.UPDATE_PSD);
                break;
            case DEL_OPRT://删除
                SecurityUtils.getSubject().checkPermission(SysConst.ShiroPermis.SysUser.DEL);
                break;
            case REAL_DEL_OPRT://硬删除
                SecurityUtils.getSubject().checkPermission(SysConst.ShiroPermis.SysUser.REAL_DEL);
                break;
            case BATCH_DEL_OPRT://批量删除
                SecurityUtils.getSubject().checkPermission(SysConst.ShiroPermis.SysUser.BATCH_DEL);
                break;
            case BATCH_REAL_DEL_OPRT://批量硬删除
                SecurityUtils.getSubject().checkPermission(SysConst.ShiroPermis.SysUser.BATCH_REAL_DEL);
                break;
        }
    }
}
