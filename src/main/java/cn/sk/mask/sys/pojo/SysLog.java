package cn.sk.mask.sys.pojo;

import java.util.Date;

public class SysLog {
    private Long id;

    private Integer userId;

    //用户名
    private String userName;
    //操作
    private String operation;
    //方法名
    private String methodName;
    //参数
    private String params;
    //ip地址
    private String ip;
    //请求url
    private String requestUrl;
    //请求类型
    private String requestType;

    private String expan1;

    private String expan2;

    private String expan3;

    private String expan4;

    private String expan5;

    private String expan6;

    private String recordStatus;

    private Date updateTime;

    private Date createTime;

    public SysLog(Long id, Integer userId, String userName, String operation, String methodName, String params, String ip, String requestUrl, String requestType, String expan1, String expan2, String expan3, String expan4, String expan5, String expan6, String recordStatus, Date updateTime, Date createTime) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.operation = operation;
        this.methodName = methodName;
        this.params = params;
        this.ip = ip;
        this.requestUrl = requestUrl;
        this.requestType = requestType;
        this.expan1 = expan1;
        this.expan2 = expan2;
        this.expan3 = expan3;
        this.expan4 = expan4;
        this.expan5 = expan5;
        this.expan6 = expan6;
        this.recordStatus = recordStatus;
        this.updateTime = updateTime;
        this.createTime = createTime;
    }

    public SysLog() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation == null ? null : operation.trim();
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName == null ? null : methodName.trim();
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params == null ? null : params.trim();
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl == null ? null : requestUrl.trim();
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType == null ? null : requestType.trim();
    }

    public String getExpan1() {
        return expan1;
    }

    public void setExpan1(String expan1) {
        this.expan1 = expan1 == null ? null : expan1.trim();
    }

    public String getExpan2() {
        return expan2;
    }

    public void setExpan2(String expan2) {
        this.expan2 = expan2 == null ? null : expan2.trim();
    }

    public String getExpan3() {
        return expan3;
    }

    public void setExpan3(String expan3) {
        this.expan3 = expan3 == null ? null : expan3.trim();
    }

    public String getExpan4() {
        return expan4;
    }

    public void setExpan4(String expan4) {
        this.expan4 = expan4 == null ? null : expan4.trim();
    }

    public String getExpan5() {
        return expan5;
    }

    public void setExpan5(String expan5) {
        this.expan5 = expan5 == null ? null : expan5.trim();
    }

    public String getExpan6() {
        return expan6;
    }

    public void setExpan6(String expan6) {
        this.expan6 = expan6 == null ? null : expan6.trim();
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