package cn.sk.mask.sys.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 系统日志实体类的扩展类
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
//@AllArgsConstructor
public class SysLogCustom extends SysLog {

    public SysLogCustom(Long id, Integer userId, String userName, String operation, String methodName, String params, String ip, String requestUrl, String requestType, String expan1, String expan2, String expan3, String expan4, String expan5, String expan6, String recordStatus, Date updateTime, Date createTime) {
        super(id, userId, userName, operation,  methodName, params, ip,requestUrl, requestType, expan1, expan2, expan3, expan4, expan5, expan6, recordStatus, updateTime, createTime);
    }

}