package cn.sk.mask.business.mapper;

import cn.sk.mask.base.mapper.IBaseMapper;
import cn.sk.mask.business.pojo.RepairCustom;
import cn.sk.mask.business.pojo.RepairQueryVo;
import org.springframework.stereotype.Repository;

/**
 *@Deseription 报修信息Mapper
 *@Author zhoucp
 *@Date 2019/7/3 15:36
 **/
@Repository
public interface RepairMapper extends IBaseMapper<RepairCustom, RepairQueryVo> {

//    int updateByPrimaryKeyWithBLOBs(Repair record);

}