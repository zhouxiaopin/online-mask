package cn.sk.mask.sys.common;

/**
 * 响应枚举类
 */
public enum ResponseCode {

    SUCCESS(0,"SUCCESS"),
    ERROR(1,"ERROR"),
    ILLEGAL_ARGUMENT(2,"参数错误"),
    LOGIN_SUCCESS(-10,"登录成功"),

    FAIL(-1,"操作失败"),
    ADD_FAIL(-2,"增加操作失败"),
    DEL_FAIL(-3,"删除操作失败"),
    MDF_FAIL(-4,"修改操作失败"),
    QRY_FAIL(-5,"查询操作失败"),
    AUD_FAIL(-6,"审核操作失败"),
    LOGIN_NO_EXIST(-7,"用户不存在"),
    LOGIN_NO_USE(-8,"用户被禁用了"),
    LOGIN_PWD_FAIL(-9,"密码错误"),
    LOGIN_FAIL(-10,"登录失败"),
    NO_AUTHORIZED(-11,"没有权限"),
    NO_AUTHENTICATED(-12,"请先登录"),
    SYS_UNKNOWN_ERROR(-200,"系统繁忙,请稍后再试");

    private final int code;
    private final String msg;


    ResponseCode(int code,String desc){
        this.code = code;
        this.msg = desc;
    }

    public int getCode(){
        return code;
    }
    public String getMsg(){
        return msg;
    }

}
