package cn.sk.mask.sys.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 系统数字字典实体类的扩展类
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
//@AllArgsConstructor
public class SysDictCustom extends SysDict {
    public SysDictCustom(Integer dictId, String dictType, String dictCode, String codeName, String description, Integer sort, String remark, String field1, String field2, String field3, String field4, String field5, String field6, String recordStatus, Date updateTime, Date createTime) {
        super(dictId, dictType, dictCode, codeName, description,sort, remark, field1, field2, field3, field4, field5, field6, recordStatus, updateTime, createTime);
    }
}