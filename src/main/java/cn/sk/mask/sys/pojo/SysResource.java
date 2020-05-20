package cn.sk.mask.sys.pojo;

import java.util.Date;

public class SysResource {
    private Integer rId;

    private String rName;

    private String rUrl;

    private Integer parentId;

    private String rType;

    private Integer rLevel;

    private Integer rSort;

    private String descri;

    private Integer optId;

    private String leftIcon;

    private String expand1;

    private String expand2;

    private String expand3;

    private String recordStatus;

    private Date updateTime;

    private Date createTime;

    public SysResource(Integer rId, String rName, String rUrl, Integer parentId, String rType, Integer rLevel, Integer rSort, String descri, Integer optId, String leftIcon, String expand1, String expand2, String expand3, String recordStatus, Date updateTime, Date createTime) {
        this.rId = rId;
        this.rName = rName;
        this.rUrl = rUrl;
        this.parentId = parentId;
        this.rType = rType;
        this.rLevel = rLevel;
        this.rSort = rSort;
        this.descri = descri;
        this.optId = optId;
        this.leftIcon = leftIcon;
        this.expand1 = expand1;
        this.expand2 = expand2;
        this.expand3 = expand3;
        this.recordStatus = recordStatus;
        this.updateTime = updateTime;
        this.createTime = createTime;
    }

    public SysResource() {
        super();
    }

    public Integer getrId() {
        return rId;
    }

    public void setrId(Integer rId) {
        this.rId = rId;
    }

    public String getrName() {
        return rName;
    }

    public void setrName(String rName) {
        this.rName = rName == null ? null : rName.trim();
    }

    public String getrUrl() {
        return rUrl;
    }

    public void setrUrl(String rUrl) {
        this.rUrl = rUrl == null ? null : rUrl.trim();
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getrType() {
        return rType;
    }

    public void setrType(String rType) {
        this.rType = rType == null ? null : rType.trim();
    }

    public Integer getrLevel() {
        return rLevel;
    }

    public void setrLevel(Integer rLevel) {
        this.rLevel = rLevel;
    }

    public Integer getrSort() {
        return rSort;
    }

    public void setrSort(Integer rSort) {
        this.rSort = rSort;
    }

    public String getDescri() {
        return descri;
    }

    public void setDescri(String descri) {
        this.descri = descri == null ? null : descri.trim();
    }

    public Integer getOptId() {
        return optId;
    }

    public void setOptId(Integer optId) {
        this.optId = optId;
    }

    public String getLeftIcon() {
        return leftIcon;
    }

    public void setLeftIcon(String leftIcon) {
        this.leftIcon = leftIcon == null ? null : leftIcon.trim();
    }

    public String getExpand1() {
        return expand1;
    }

    public void setExpand1(String expand1) {
        this.expand1 = expand1 == null ? null : expand1.trim();
    }

    public String getExpand2() {
        return expand2;
    }

    public void setExpand2(String expand2) {
        this.expand2 = expand2 == null ? null : expand2.trim();
    }

    public String getExpand3() {
        return expand3;
    }

    public void setExpand3(String expand3) {
        this.expand3 = expand3 == null ? null : expand3.trim();
    }

    public String getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(String recordStatus) {
        this.recordStatus = recordStatus == null ? null : recordStatus.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}