package cn.sk.mask.sys.pojo;


import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class TreeNode {
    private String id;//本节点id
    private String name;//本节点名称
    private String parentId;//本节点的父节点
    private Integer order;//本节点排序，可忽略
    private Integer level;//本节点层级
    private Map<String,Object> attrs;
    private List<TreeNode> children;
}
