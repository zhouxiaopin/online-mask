package cn.sk.mask.base.controller;

import cn.sk.mask.base.service.IBaseService;
import cn.sk.mask.sys.service.ISysDictService;
import cn.sk.mask.sys.common.SysConst;
import cn.sk.mask.sys.common.CustomException;
import cn.sk.mask.sys.common.ServerResponse;
import cn.sk.mask.sys.common.SkLog;
import cn.sk.mask.sys.pojo.SysDictCustom;
import cn.sk.mask.sys.pojo.SysDictQueryVo;
import cn.sk.mask.sys.utils.JackJsonUtil;
import cn.sk.mask.sys.utils.LogUtil;
import cn.sk.mask.sys.vo.DataTableVo;
import cn.sk.mask.sys.vo.SelectBoxVo;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
public class BaseController<T, V> {
    @Autowired
    private IBaseService<T, V> baseService;
    @Autowired
    private ISysDictService sysDictService;
    @Autowired
    protected LogUtil logUtil;

    protected static final String OPRT_KEY = "oprt";
    protected static final String QUERY_OPRT = "query";
    protected static final String ADD_OPRT = "add";
    protected static final String UPDATE_OPRT = "update";
    protected static final String QUERYDETAIL_OPRT = "queryDetail";
    protected static final String DEL_OPRT = "del";
    protected static final String REAL_DEL_OPRT = "realDel";
    protected static final String BATCH_DEL_OPRT = "batchDel";
    protected static final String BATCH_REAL_DEL_OPRT = "batchRealDel";

    //根据操作返回对应的页面
    protected String getPage(String oprt) {
        return null;
    }
    //根据操作
    protected void authorityValidate(String oprt){}


    //    protected ServerResponse<T> addBefore(T t){return null;}
    protected ServerResponse<T> updateBefore(T oldObj, T t) {
        return null;
    }

    //参数检验
    protected ServerResponse<T> paramValidate(String oprt, T t) {
        return ServerResponse.createBySuccessMessage("检验成功");
    }
    //添加返回参数
    protected void addReturnVale(String oprt, ModelAndView model) {}

    /**
     * 获取页面
     *
     * @return 返回页面
     */
    protected String page(String oprt) {
        String page = getPage(oprt);
        if (null == page) {
            throw new CustomException(SysConst.ResponseMsg.NO_PAGE);
        }
        return page;
    }


    @GetMapping(value = "/initQuery")
    public ModelAndView initQuery(ModelAndView model) throws Exception {
        authorityValidate(QUERY_OPRT);
        model.addObject(OPRT_KEY, QUERY_OPRT);
        model.addObject(SysConst.Dict.RECORDSTATUS_DICTCODE,
                querySelectBoxVoByDictType(SysConst.Dict.RECORDSTATUS_DICTCODE).getData());
        addReturnVale(QUERY_OPRT,model);
        model.setViewName(page(QUERY_OPRT));
        return model;
    }

    @GetMapping(value = "/initAdd")
    public ModelAndView initAdd(ModelAndView model, T t) throws Exception {
        authorityValidate(ADD_OPRT);
        try {
            model.addObject("obj", t);
            model.addObject(OPRT_KEY, ADD_OPRT);
            model.addObject("msg", SysConst.ResponseMsg.OPRT_SUCCE);
            addReturnVale(ADD_OPRT,model);
        } catch (Exception e) {
            model.addObject("msg", SysConst.ResponseMsg.OPRT_FAIL);
        }
        model.setViewName(page(ADD_OPRT));
        return model;
    }

    @GetMapping(value = "/initUpdate")
    public ModelAndView initUpdate(ModelAndView model, T entity) throws Exception {
        authorityValidate(UPDATE_OPRT);
        model.addObject(OPRT_KEY, UPDATE_OPRT);
        try {
            init(model, entity);
            addReturnVale(UPDATE_OPRT,model);
        } catch (Exception e) {
            model.addObject("msg", SysConst.ResponseMsg.OPRT_FAIL);
        }
        model.setViewName(page(UPDATE_OPRT));
        return model;
    }

    @GetMapping(value = "/initQueryDetail")
    public ModelAndView queryDetail(ModelAndView model, T entity) throws Exception {
        authorityValidate(QUERYDETAIL_OPRT);
        model.addObject(OPRT_KEY, QUERYDETAIL_OPRT);
        try {
            init(model, entity);
            addReturnVale(QUERYDETAIL_OPRT,model);
        } catch (Exception e) {
            model.addObject("msg", SysConst.ResponseMsg.OPRT_FAIL);
        }
        model.setViewName(page(QUERYDETAIL_OPRT));
        return model;
    }

    @SkLog("添加")
    @PostMapping(value = "/add")
    public ServerResponse<T> add(T t) throws Exception {
        authorityValidate(ADD_OPRT);
        ServerResponse<T> serverResponse = paramValidate(ADD_OPRT, t);
        if (null == serverResponse || serverResponse.isSuccess()) {
            return baseService.insert(t);
        } else {
            return serverResponse;
        }
    }

    @SkLog("修改")
    @PostMapping(value = "/update")
    public ServerResponse<T> update(T t) throws Exception {
        authorityValidate(UPDATE_OPRT);
        ServerResponse<T> sr = paramValidate(UPDATE_OPRT, t);
        if (null != sr&&!sr.isSuccess()) {
            return sr;
        }

        T obj = getObj(t);

        ServerResponse<T> serverResponse = updateBefore(obj, t);
        if (null == serverResponse || serverResponse.isSuccess()) {
//            return  baseService.update(obj);
            return baseService.update(t);
        } else {
            return serverResponse;
        }
    }

    //软删除
    @SkLog("软删除")
    @PostMapping(value = "/delete")
    public ServerResponse<T> delete(@RequestParam("ids[]") String[] ids) throws Exception {
        if(ids.length > 1) {
            authorityValidate(BATCH_DEL_OPRT);
        }else{
            authorityValidate(DEL_OPRT);
        }
        return baseService.deleteInIds(ids);

    }

    //硬删除
    @SkLog("硬删除")
    @PostMapping(value = "/realDelete")
    public ServerResponse<T> realDelete(@RequestParam("ids[]") String[] ids) throws Exception {
        if(ids.length > 1) {
            authorityValidate(BATCH_REAL_DEL_OPRT);
        }else{
            authorityValidate(REAL_DEL_OPRT);
        }
        return baseService.realDeleteInIds(ids);

    }



//    @SkLog("查询")
    //根据条件查询记录（分页）
    @PostMapping(value = "/query")
    public DataTableVo list(V v) {
        return baseService.queryObjsByPage(v);
    }
    //根据条件查询记录（非分页）
    @PostMapping(value = "/queryAllByCondition")
    public ServerResponse<List<T>> queryAllByCondition(V v) {
        return baseService.queryObjs(v);
    }

    //需要根据主键获取数据的初始化方法
    protected void init(ModelAndView model, T entity) {
        try {
            T t = getObj(entity);
            if (null != t) {
                model.addObject("obj", t);
//                model.addAttribute("jsonObj", GsonUtils.objToJsonStr(t));
                model.addObject("jsonObj", JackJsonUtil.obj2String(t));
                model.addObject("msg", SysConst.ResponseMsg.OPRT_SUCCE);
            } else {
                model.addObject("msg", SysConst.ResponseMsg.RECORD_EXISTS_NO);
            }
        } catch (Exception e) {
            model.addObject("msg", SysConst.ResponseMsg.OPRT_FAIL);
        }
    }

    //根据主键获取实体对象
    protected T getObj(T entity) {
        T t = null;
        try {
            t = baseService.queryObj(entity).getData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    //获取下拉框
    @PostMapping(value = "/querySelectBoxVoByDictType")
    public ServerResponse<List<SelectBoxVo>> querySelectBoxVoByDictType(String dictType) {
        SysDictQueryVo sysDictQueryVo = new SysDictQueryVo();
        SysDictCustom condition = new SysDictCustom();

        sysDictQueryVo.getIsNoLike().put("dictType",true);

        condition.setDictType(dictType);
        condition.setRecordStatus(SysConst.RecordStatus.ABLE);

        sysDictQueryVo.setSysDictCustom(condition);
        ServerResponse<List<SysDictCustom>> sr = sysDictService.queryObjs(sysDictQueryVo);
        List<SysDictCustom> data = sr.getData();
        List<SelectBoxVo> selectBoxVos = Lists.newArrayList();
        for (int i = 0,len = data.size(); i < len; i++){
            SysDictCustom s = data.get(i);
            SelectBoxVo selectBoxVo = new SelectBoxVo(s.getDictCode(),s.getCodeName());
            selectBoxVos.add(selectBoxVo);
        }
        return ServerResponse.createBySuccess(selectBoxVos);
    }
    //权限校验
    @PostMapping(value = "/authValidate")
    public ServerResponse authValidate(String oprt) {
        authorityValidate(oprt);
        return ServerResponse.createBySuccess();
    }

}
