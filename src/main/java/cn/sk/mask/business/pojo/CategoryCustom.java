package cn.sk.mask.business.pojo;

import lombok.*;

import java.util.Date;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CategoryCustom extends Category {
    //用户名
    private String parentName;

    public CategoryCustom(Integer id, Integer parentId, String name, Integer sort, String recordStatus, String expand1, String expand2, String expand3, Date updateTime, Date createTime) {
        super(id, parentId, name, sort, recordStatus, expand1, expand2, expand3, updateTime, createTime);
    }
}