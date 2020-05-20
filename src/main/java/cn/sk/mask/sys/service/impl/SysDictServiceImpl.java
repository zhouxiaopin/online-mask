package cn.sk.mask.sys.service.impl;

import cn.sk.mask.base.service.impl.BaseServiceImpl;
import cn.sk.mask.business.common.Const;
import cn.sk.mask.sys.common.ServerResponse;
import cn.sk.mask.sys.common.SysConst;
import cn.sk.mask.sys.mapper.SysDictMapper;
import cn.sk.mask.sys.pojo.SysDictCustom;
import cn.sk.mask.sys.pojo.SysDictQueryVo;
import cn.sk.mask.sys.service.ISysDictService;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 系统数字字典业务逻辑接口实现类
 */
@Service
public class SysDictServiceImpl extends BaseServiceImpl<SysDictCustom,SysDictQueryVo> implements ISysDictService {
    @Autowired
    private SysDictMapper sysDictMapper;


    @Override
    public ServerResponse<Map<String, String>> getDictKvData(String dictType) {
        SysDictQueryVo sysDictQueryVo = new SysDictQueryVo();
        SysDictCustom condition = new SysDictCustom();

        sysDictQueryVo.getIsNoLike().put("dictType",true);

        condition.setDictType(dictType);
        condition.setRecordStatus(SysConst.RecordStatus.ABLE);

        sysDictQueryVo.setSysDictCustom(condition);
        List<SysDictCustom> sysDictCustoms = sysDictMapper.selectListByQueryVo(sysDictQueryVo);
        //封装数据
        Map<String,String> kvMap = Maps.newHashMap();
        for(int i = 0,len = sysDictCustoms.size(); i < len; i++) {
            SysDictCustom sysDictCustom = sysDictCustoms.get(i);
            kvMap.put(sysDictCustom.getDictCode(),sysDictCustom.getCodeName());
        }
        return ServerResponse.createBySuccess(kvMap);
    }

    @Override
    public ServerResponse<Map<String, String>> getDictKvAndExpData(String dictType) {
        SysDictQueryVo sysDictQueryVo = new SysDictQueryVo();
        SysDictCustom condition = new SysDictCustom();

        sysDictQueryVo.getIsNoLike().put("dictType",true);

        condition.setDictType(dictType);
        condition.setRecordStatus(SysConst.RecordStatus.ABLE);

        sysDictQueryVo.setSysDictCustom(condition);
        List<SysDictCustom> sysDictCustoms = sysDictMapper.selectListByQueryVo(sysDictQueryVo);
        //封装数据
        Map<String,String> kvMap = Maps.newHashMap();
        for(int i = 0,len = sysDictCustoms.size(); i < len; i++) {
            SysDictCustom sysDictCustom = sysDictCustoms.get(i);
            StringBuilder codeName = new StringBuilder();
            codeName.append(sysDictCustom.getCodeName());
            String filed1 = sysDictCustom.getField1();
            String filed2 = sysDictCustom.getField2();
            String filed3 = sysDictCustom.getField3();
            String filed4 = sysDictCustom.getField4();
            String filed5 = sysDictCustom.getField5();
            String filed6 = sysDictCustom.getField6();
            if(StringUtils.isNotBlank(filed1)) {
                codeName.append(Const.DEFAULT_SPLIT_SYMBOL).append(filed1);
            }
            if(StringUtils.isNotBlank(filed2)) {
                codeName.append(Const.DEFAULT_SPLIT_SYMBOL).append(filed2);
            }
            if(StringUtils.isNotBlank(filed3)) {
                codeName.append(Const.DEFAULT_SPLIT_SYMBOL).append(filed3);
            }
            if(StringUtils.isNotBlank(filed4)) {
                codeName.append(Const.DEFAULT_SPLIT_SYMBOL).append(filed4);
            }
            if(StringUtils.isNotBlank(filed5)) {
                codeName.append(Const.DEFAULT_SPLIT_SYMBOL).append(filed5);
            }
            if(StringUtils.isNotBlank(filed6)) {
                codeName.append(Const.DEFAULT_SPLIT_SYMBOL).append(filed6);
            }
            kvMap.put(sysDictCustom.getDictCode(),codeName.toString());
        }
        return ServerResponse.createBySuccess(kvMap);
    }
}
