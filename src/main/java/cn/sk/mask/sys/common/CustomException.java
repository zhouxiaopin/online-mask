package cn.sk.mask.sys.common;

/**
 * 自定义异常类
 */
public class CustomException extends RuntimeException{

    private Integer code;


    public CustomException(ResponseCode responseCode) {
        super(responseCode.getMsg());
        this.code = responseCode.getCode();
    }
    public CustomException(String msg) {
        super(msg);
        this.code = ResponseCode.ERROR.getCode();
    }
    public CustomException(String msg,boolean success) {
        super(msg);
        if(success) {
            this.code = ResponseCode.SUCCESS.getCode();
        }else {
            this.code = ResponseCode.ERROR.getCode();
        }
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
