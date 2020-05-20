package cn.sk.mask.business.pojo;

import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

/**
 *@Deseription 报修信息实体类的扩展类
 *@Author zhoucp
 *@Date 2019/7/3 15:30
 **/
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RepairCustom extends Repair {
    private String rpTypeStr;
    private String rpPosiStoreyStr;
    private String recordStatusStr;
    private String[] breakdownPics;
    private String[] wxPics;
    public RepairCustom(Integer rpId, String rpNo, String rpType, Byte rpPosiStorey, String linkPerson, String linkPhone, Long rpEmpId, String rpPerson, String rpDepart, String rpPhone, Long wxEmpId, String wxPerson, Date handleTime, String rpIdea, String recordStatus, Date updateTime, Date createTime, String breakdownPic, String wxPic, String wxPlan, BigDecimal predictWxFee, String breakdownDesc) {
        super(rpId, rpNo, rpType, rpPosiStorey, linkPerson, linkPhone, rpEmpId, rpPerson, rpDepart, rpPhone, wxEmpId, wxPerson, handleTime, rpIdea, recordStatus, updateTime, createTime,breakdownPic, wxPic, wxPlan, predictWxFee, breakdownDesc);
    }

}