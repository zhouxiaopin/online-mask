package cn.sk.mask.sys.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * sql语句配置实体类的扩展类
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
//@AllArgsConstructor
public class SysSqlConfCustom extends SysSqlConf{

    public SysSqlConfCustom(Integer scId, String scCode, String scName, String scStatement, String scType, String descri, Integer optId, String field1, String field2, String field3, String field4, String recordStatus, Date updateTime, Date createTime) {
        super(scId, scCode, scName, scStatement, scType, descri, optId, field1, field2, field3, field4, recordStatus, updateTime, createTime);
    }

}