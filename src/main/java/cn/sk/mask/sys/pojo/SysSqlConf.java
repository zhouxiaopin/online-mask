package cn.sk.mask.sys.pojo;

import java.util.Date;

public class SysSqlConf {
    private Integer scId;

    private String scCode;

    private String scName;

    private String scStatement;

    private String scType;

    private String descri;

    private Integer optId;

    private String field1;

    private String field2;

    private String field3;

    private String field4;

    private String recordStatus;

    private Date updateTime;

    private Date createTime;


    public SysSqlConf(Integer scId, String scCode, String scName, String scStatement, String scType, String descri, Integer optId, String field1, String field2, String field3, String field4, String recordStatus, Date updateTime, Date createTime) {
        this.scId = scId;
        this.scCode = scCode;
        this.scName = scName;
        this.scStatement = scStatement;
        this.scType = scType;
        this.descri = descri;
        this.optId = optId;
        this.field1 = field1;
        this.field2 = field2;
        this.field3 = field3;
        this.field4 = field4;
        this.recordStatus = recordStatus;
        this.updateTime = updateTime;
        this.createTime = createTime;
    }

    public SysSqlConf() {
        super();
    }

    public Integer getScId() {
        return scId;
    }

    public void setScId(Integer scId) {
        this.scId = scId;
    }

    public String getScCode() {
        return scCode;
    }

    public void setScCode(String scCode) {
        this.scCode = scCode == null ? null : scCode.trim();
    }

    public String getScName() {
        return scName;
    }

    public void setScName(String scName) {
        this.scName = scName == null ? null : scName.trim();
    }

    public String getScType() {
        return scType;
    }

    public void setScType(String scType) {
        this.scType = scType == null ? null : scType.trim();
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

    public String getField1() {
        return field1;
    }

    public void setField1(String field1) {
        this.field1 = field1 == null ? null : field1.trim();
    }

    public String getField2() {
        return field2;
    }

    public void setField2(String field2) {
        this.field2 = field2 == null ? null : field2.trim();
    }

    public String getField3() {
        return field3;
    }

    public void setField3(String field3) {
        this.field3 = field3 == null ? null : field3.trim();
    }

    public String getField4() {
        return field4;
    }

    public void setField4(String field4) {
        this.field4 = field4 == null ? null : field4.trim();
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

    public String getScStatement() {
        return scStatement;
    }

    public void setScStatement(String scStatement) {
        this.scStatement = scStatement == null ? null : scStatement.trim();
    }
}