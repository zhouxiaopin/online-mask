package cn.sk.mask.business.service.impl;

import cn.sk.mask.base.service.impl.BaseServiceImpl;
import cn.sk.mask.business.common.Const;
import cn.sk.mask.business.mapper.RepairMapper;
import cn.sk.mask.business.pojo.RepairCustom;
import cn.sk.mask.business.pojo.RepairQueryVo;
import cn.sk.mask.business.service.IRepairService;
import cn.sk.mask.sys.vo.DataTableVo;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 *@Deseription 报修信息业务逻辑接口实现类
 *@Author zhoucp
 *@Date 2019/7/3 15:51
 **/
@Service
public class RepairServiceImpl extends BaseServiceImpl<RepairCustom, RepairQueryVo> implements IRepairService {
    @Autowired
    private RepairMapper repairMapper;
    @Value("${sk.httpPrefix}")
    private String imgHttpPrefix;


    @Override
    public DataTableVo<RepairCustom> queryObjsByPage(RepairQueryVo entityQueryVo) {

        //报修类型
        Map<String,String> repairTypeMap = sysDictService.getDictKvData(Const.Dict.Repair.REPAIR_TYPE).getData();
        //报修楼层
        Map<String,String> repairPosiStoreyMap = sysDictService.getDictKvData(Const.Dict.Repair.REPAIR_POSI_STOREY).getData();
        //报修记录状态
        Map<String,String> repairRecordStatusMap = sysDictService.getDictKvAndExpData(Const.Dict.Repair.REPAIR_RECORD_STATUS).getData();

        //数据封装
        DataTableVo<RepairCustom> dataTableVo = super.queryObjsByPage(entityQueryVo);
        List<RepairCustom> data = dataTableVo.getData();
        for(int i = 0,len = data.size(); i < len; i++) {
            RepairCustom repairCustom = data.get(i);
            if(ObjectUtils.allNotNull(repairTypeMap)) {
                repairCustom.setRpTypeStr(repairTypeMap.get(repairCustom.getRpType()));
            }
            if(ObjectUtils.allNotNull(repairPosiStoreyMap)) {
                repairCustom.setRpPosiStoreyStr(repairPosiStoreyMap.get(repairCustom.getRpPosiStorey().toString()));
            }
            if(ObjectUtils.allNotNull(repairRecordStatusMap)) {
                repairCustom.setRecordStatusStr(repairRecordStatusMap.get(repairCustom.getRecordStatus()));
            }
        }


        return dataTableVo;
    }

//    @Override
//    public ServerResponse<RepairCustom> queryObj(RepairCustom entityCustom) {
//        //报修类型
//        Map<String,String> repairTypeMap = sysDictService.getDictKvData(Const.Dict.Repair.REPAIR_TYPE).getData();
//        //报修楼层
//        Map<String,String> repairPosiStoreyMap = sysDictService.getDictKvData(Const.Dict.Repair.REPAIR_POSI_STOREY).getData();
//        //报修记录状态
//        Map<String,String> repairRecordStatusMap = sysDictService.getDictKvAndExpData(Const.Dict.Repair.REPAIR_RECORD_STATUS).getData();
//
//        //数据封装
//        ServerResponse<RepairCustom> rerult = super.queryObj(entityCustom);
//        RepairCustom repairCustom = rerult.getData();
//        if(ObjectUtils.allNotNull(repairTypeMap)) {
//            repairCustom.setRpTypeStr(repairTypeMap.get(repairCustom.getRpType()));
//        }
//        if(ObjectUtils.allNotNull(repairPosiStoreyMap)) {
//            repairCustom.setRpPosiStoreyStr(repairPosiStoreyMap.get(repairCustom.getRpPosiStorey().toString()));
//        }
//        if(ObjectUtils.allNotNull(repairRecordStatusMap)) {
//            repairCustom.setRecordStatusStr(repairRecordStatusMap.get(repairCustom.getRecordStatus()));
//        }
//        String breakdownPic = repairCustom.getBreakdownPic();
//        if(!Strings.isNullOrEmpty(breakdownPic)) {
//            String[] breakdownPics = breakdownPic.split(Const.DEFAULT_SPLIT_SYMBOL);
//            for(int i = 0,len = breakdownPics.length; i < len; i++) {
//                breakdownPics[i] =imgHttpPrefix+Const.BREAKDOWN_PIC_UPLOAD_PATH_URL+breakdownPics[i];
//            }
//            repairCustom.setBreakdownPics(breakdownPics);
//        }
//        String wxPic = repairCustom.getWxPic();
//        if(!Strings.isNullOrEmpty(wxPic)) {
//            String[] wxPics = wxPic.split(Const.DEFAULT_SPLIT_SYMBOL);
//            for(int i = 0,len = wxPics.length; i < len; i++) {
//                wxPics[i] =imgHttpPrefix+Const.REPAIR_PIC_UPLOAD_PATH_URL+wxPics[i];
//            }
//            repairCustom.setWxPics(wxPics);
//        }
//
//        return rerult;
//    }
}
