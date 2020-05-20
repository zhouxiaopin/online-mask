package cn.sk.mask.base.service.impl;

import cn.sk.mask.base.mapper.IBaseMapper;
import cn.sk.mask.base.pojo.BaseQueryVo;
import cn.sk.mask.base.service.IBaseService;
import cn.sk.mask.sys.common.SysConst;
import cn.sk.mask.sys.common.CustomException;
import cn.sk.mask.sys.common.ResponseCode;
import cn.sk.mask.sys.common.ServerResponse;
import cn.sk.mask.sys.mapper.SysDictMapper;
import cn.sk.mask.sys.service.ISysDictService;
import cn.sk.mask.sys.vo.DataTableVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class BaseServiceImpl<T,V> implements IBaseService<T,V> {
    @Autowired
    protected IBaseMapper<T,V> baseMapper;
    @Autowired
    protected SysDictMapper sysDictMapper;
    @Autowired
    protected ISysDictService sysDictService;


    @Override
    @Transactional(rollbackFor={CustomException.class, Exception.class})
    public ServerResponse<T> insert(T entityCustom) {
        ServerResponse serverResponse = insertBefore(entityCustom);
        if(!serverResponse.isSuccess()) {
            return serverResponse;
        }
        int num = baseMapper.insertSelective(entityCustom);
        if(num > 0) {
            serverResponse = insertAfter(entityCustom);
            if(!serverResponse.isSuccess()) {
                throw new CustomException(ResponseCode.ADD_FAIL);
            }
            return ServerResponse.createBySuccess(SysConst.ResponseMsg.ADD_SUCCE,entityCustom);
        }else {
            return ServerResponse.createByErrorMessage(SysConst.ResponseMsg.ADD_FAIL);
        }
    }

    //添加之前的操作
    protected ServerResponse<T> insertBefore(T t){
        return ServerResponse.createBySuccess(t);
    }
    //添加之后的操作
    protected ServerResponse<T> insertAfter(T t){
        return ServerResponse.createBySuccess(t);
    }

    @Override
    @Transactional(rollbackFor={CustomException.class, Exception.class})
    public ServerResponse<T> update(T entityCustom) {
        ServerResponse serverResponse = updateBefore(entityCustom);
        if(!serverResponse.isSuccess()) {
            return serverResponse;
        }

        int num = baseMapper.updateByPrimaryKeySelective(entityCustom);
        if(num > 0) {
            return ServerResponse.createBySuccess(SysConst.ResponseMsg.UPDATE_SUCCE,entityCustom);
        }else {
            return ServerResponse.createByErrorMessage(SysConst.ResponseMsg.UPDATE_FAIL);
        }
    }

    //删除之后的操作
    protected ServerResponse<T> deleteInIdsAfter(String[] ids){
        return ServerResponse.createBySuccess();
    }
    @Override
    @Transactional(rollbackFor={CustomException.class, Exception.class})
    public ServerResponse<T> deleteInIds(String[] ids) {
        int num = baseMapper.deleteInIds(ids, SysConst.RecordStatus.DELETE);
        if(num > 0) {
            ServerResponse serverResponse = deleteInIdsAfter(ids);
            if(!serverResponse.isSuccess()) {
                throw new CustomException(ResponseCode.DEL_FAIL);
            }
            return ServerResponse.createBySuccessMessage(SysConst.ResponseMsg.DELET_SUCCE);
        }else {
            return ServerResponse.createByErrorMessage(SysConst.ResponseMsg.DELET_FAIL);
        }
    }

    //硬删除之后的操作
    protected ServerResponse<T> realDeleteInIdsAfter(String[] ids){
        return ServerResponse.createBySuccess();
    }
    @Override
    @Transactional(rollbackFor={CustomException.class, Exception.class})
    public ServerResponse<T> realDeleteInIds(String[] ids) {
        int num = baseMapper.realDeleteInIds(ids);
        if(num > 0) {
            ServerResponse serverResponse = realDeleteInIdsAfter(ids);
            if(!serverResponse.isSuccess()) {
                throw new CustomException(ResponseCode.DEL_FAIL);
            }
            return ServerResponse.createBySuccessMessage(SysConst.ResponseMsg.DELET_SUCCE);
        }else {
            return ServerResponse.createByErrorMessage(SysConst.ResponseMsg.DELET_FAIL);
        }
    }

    //修改之前的操作
    protected ServerResponse<T> updateBefore(T t){
        return ServerResponse.createBySuccess();
    }

    @Override
    @Transactional(rollbackFor={CustomException.class, Exception.class})
    public ServerResponse<T> delete(T entityCustom) {
        int num = baseMapper.deleteByPrimaryKey(entityCustom);
        if(num > 0) {
            return ServerResponse.createBySuccess(SysConst.ResponseMsg.DELET_SUCCE,entityCustom);
        }else {
            return ServerResponse.createByErrorMessage(SysConst.ResponseMsg.DELET_FAIL);
        }
    }

    @Override
    public ServerResponse<T> queryObj(T entityCustom) {
        T t = baseMapper.selectByPrimaryKey(entityCustom);
        if(null == t) {
            return ServerResponse.createByErrorMessage(SysConst.ResponseMsg.QUERY_FAIL);
        }
        return ServerResponse.createBySuccess(SysConst.ResponseMsg.QUERY_SUCCE,t);
    }

//    @Override
//    public ServerResponse<DataTableVo> queryObjsByPage(V entityQueryVo) {
//        //startPage--start
//        //填充自己的sql查询逻辑
//        //pageHelper-收尾
//        BaseQueryVo baseQueryVo = (BaseQueryVo)entityQueryVo;
//        PageHelper.startPage(baseQueryVo.getStart(),baseQueryVo.getLength());
//        List<T> list = baseMapper.selectListByQueryVo(entityQueryVo);
//
//        PageInfo pageResult = new PageInfo(list);
//
//        DataTableVo<T> dataTableVo = new DataTableVo();
//
//        dataTableVo.setDraw(baseQueryVo.getDraw());
//        dataTableVo.setRecordsTotal(pageResult.getTotal());
//        dataTableVo.setData(list);
//        return ServerResponse.createBySuccess(dataTableVo);
//    }
    @Override
    public DataTableVo<T> queryObjsByPage(V entityQueryVo) {

        //startPage--start
        //填充自己的sql查询逻辑
        //pageHelper-收尾
        BaseQueryVo baseQueryVo = (BaseQueryVo)entityQueryVo;
//        PageHelper.startPage(baseQueryVo.getStart(),baseQueryVo.getLength());
        PageHelper.offsetPage(baseQueryVo.getStart(),baseQueryVo.getLength());
        List<T> list = baseMapper.selectListByQueryVo(entityQueryVo);

        PageInfo pageResult = new PageInfo(list);

        DataTableVo<T> dataTableVo = new DataTableVo();

        dataTableVo.setDraw(baseQueryVo.getDraw());
        dataTableVo.setRecordsTotal(pageResult.getTotal());
        dataTableVo.setRecordsFiltered(pageResult.getTotal());
        if(CollectionUtils.isEmpty(list)){
            dataTableVo.setData(Lists.newArrayList());
        }else{
            dataTableVo.setData(list);
        }


        return dataTableVo;
    }

    @Override
    public ServerResponse<List<T>> queryObjs(V entityQueryVo) {
        List<T> list = baseMapper.selectListByQueryVo(entityQueryVo);
        return ServerResponse.createBySuccess(SysConst.ResponseMsg.QUERY_SUCCE,list);
    }

}
