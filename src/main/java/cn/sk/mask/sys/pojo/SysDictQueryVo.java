package cn.sk.mask.sys.pojo;

import cn.sk.mask.base.pojo.BaseQueryVo;
import lombok.Getter;
import lombok.Setter;

/**
 * 系统数字字典实体类的包装对象
 */
@Getter
@Setter
public class SysDictQueryVo extends BaseQueryVo {
    private SysDictCustom sysDictCustom;

    public static void getQueryKvInstance(String dictType) {

    }
}