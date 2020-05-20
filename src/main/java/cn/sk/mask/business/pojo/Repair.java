package cn.sk.mask.business.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
/**
 *@Deseription 报修信息实体类
 *@Author zhoucp
 *@Date 2019/7/3 15:29
 **/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Repair {
    private Integer rpId;

    private String rpNo;

    private String rpType;

    private Byte rpPosiStorey;

    private String linkPerson;

    private String linkPhone;

    private Long rpEmpId;

    private String rpPerson;

    private String rpDepart;

    private String rpPhone;

    private Long wxEmpId;

    private String wxPerson;

    private Date handleTime;

    private String rpIdea;

    private String recordStatus;

    private Date updateTime;

    private Date createTime;

    private String breakdownPic;

    private String wxPic;

    private String wxPlan;

    private BigDecimal predictWxFee;

    private String breakdownDesc;


}