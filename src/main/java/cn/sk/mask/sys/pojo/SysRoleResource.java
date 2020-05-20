package cn.sk.mask.sys.pojo;

public class SysRoleResource {
    private Integer id;

    private Integer roleId;

    private Integer resId;

    public SysRoleResource(Integer id, Integer roleId, Integer resId) {
        this.id = id;
        this.roleId = roleId;
        this.resId = resId;
    }

    public SysRoleResource() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getResId() {
        return resId;
    }

    public void setResId(Integer resId) {
        this.resId = resId;
    }
}