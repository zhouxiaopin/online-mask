package cn.sk.mask.business.pojo;

import cn.sk.mask.base.pojo.BaseQueryVo;
import lombok.Getter;
import lombok.Setter;


/**
 *@Deseription 报修信息实体类的包装对象
 *@Author zhoucp
 *@Date 2019/7/3 15:32
 **/
@Getter
@Setter
public class RepairQueryVo extends BaseQueryVo {
    private RepairCustom repairCustom;
}