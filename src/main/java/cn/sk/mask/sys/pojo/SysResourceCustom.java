package cn.sk.mask.sys.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 系统资源实体类的扩展类
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
//@AllArgsConstructor
public class SysResourceCustom extends SysResource {

    //用户名
    private String userName;
    //级别对应的值
    private String rLevelStr;
    //父资源名
    private String parentName;

    public SysResourceCustom(Integer rId, String rName, String rUrl, Integer parentId, String rType, Integer rLevel, Integer rSort, String descri, Integer optId, String leftIcon, String expand1, String expand2, String expand3, String recordStatus, Date updateTime, Date createTime) {
        super(rId, rName, rUrl, parentId, rType, rLevel, rSort, descri, optId, leftIcon, expand1, expand2, expand3, recordStatus, updateTime, createTime);
    }

}