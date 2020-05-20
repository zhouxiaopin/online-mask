package cn.sk.mask.sys.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class DataTableVo<T> {
    private Integer draw;//表示请求次数
    private Long recordsTotal;//总记录数
    private Long recordsFiltered;//过滤后的总记录数
    private List<T> data;//具体的数据对象数组
}
