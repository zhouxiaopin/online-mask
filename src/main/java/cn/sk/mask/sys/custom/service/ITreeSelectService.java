package cn.sk.mask.sys.custom.service;

import cn.sk.mask.sys.common.ServerResponse;

import java.util.List;
import java.util.Map;

/**
 *@Deseription 树形下拉框业务逻辑接口
 *@Author zhoucp
 *@Date 2019/7/8 17:02
 **/
public interface ITreeSelectService {

    /**
     * 根据方法名执行
     * @return
     */
    ServerResponse<List<Map<String,Object>>>  exeMethod(String methodName);

    /**
     * 获取员工树形下拉框
     * @return
     */
    ServerResponse<List<Map<String,Object>>>  getEmpTree();
    /**
     * 通过权限标识获取员工树形下拉框
     * @return
     */
    ServerResponse<List<Map<String,Object>>>  getEmpTreeByPermis();
}
