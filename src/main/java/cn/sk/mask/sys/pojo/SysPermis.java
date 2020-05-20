package cn.sk.mask.sys.pojo;

import java.util.Date;

public class SysPermis {
    private Integer pId;

    private String pFlag;

    private String pName;

    private String pUrl;

    private Integer parentId;

    private String pType;

    private Integer pLevel;

    private Integer pSort;

    private String descri;

    private Integer optId;

    private String leftIcon;

    private String expand1;

    private String expand2;

    private String expand3;

    private String recordStatus;

    private Date updateTime;

    private Date createTime;

    public SysPermis(Integer pId, String pFlag, String pName, String pUrl, Integer parentId, String pType, Integer pLevel, Integer pSort, String descri, Integer optId, String leftIcon, String expand1, String expand2, String expand3, String recordStatus, Date updateTime, Date createTime) {
        this.pId = pId;
        this.pFlag = pFlag;
        this.pName = pName;
        this.pUrl = pUrl;
        this.parentId = parentId;
        this.pType = pType;
        this.pLevel = pLevel;
        this.pSort = pSort;
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

    public SysPermis() {
        super();
    }

    public Integer getpId() {
        return pId;
    }

    public void setpId(Integer pId) {
        this.pId = pId;
    }

    public String getpFlag() {
        return pFlag;
    }

    public void setpFlag(String pFlag) {
        this.pFlag = pFlag == null ? null : pFlag.trim();
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName == null ? null : pName.trim();
    }

    public String getpUrl() {
        return pUrl;
    }

    public void setpUrl(String pUrl) {
        this.pUrl = pUrl;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getpType() {
        return pType;
    }

    public void setpType(String pType) {
        this.pType = pType == null ? null : pType.trim();
    }

    public Integer getpLevel() {
        return pLevel;
    }

    public void setpLevel(Integer pLevel) {
        this.pLevel = pLevel;
    }

    public Integer getpSort() {
        return pSort;
    }

    public void setpSort(Integer pSort) {
        this.pSort = pSort;
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