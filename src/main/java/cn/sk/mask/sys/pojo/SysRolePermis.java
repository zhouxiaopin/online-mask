package cn.sk.mask.sys.pojo;

public class SysRolePermis {
    private Integer id;

    private Integer roleId;

    private Integer permisId;

    public SysRolePermis(Integer id, Integer roleId, Integer permisId) {
        this.id = id;
        this.roleId = roleId;
        this.permisId = permisId;
    }

    public SysRolePermis() {
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

    public Integer getPermisId() {
        return permisId;
    }

    public void setPermisId(Integer permisId) {
        this.permisId = permisId;
    }
}