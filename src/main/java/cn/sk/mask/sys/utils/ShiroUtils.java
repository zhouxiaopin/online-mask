package cn.sk.mask.sys.utils;

import org.apache.shiro.codec.Base64;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

/**
 * Created by Administrator on 2017/11/28.
 */
public class ShiroUtils {
    public static final String DEFALT_SALT = "ticc";

    /**
     * 获取MD5加密的密码串
     * @param saltStr
     * @param pwd
     * @return
     */
    public static String getMd5Pwd(String saltStr, String pwd){
        String hashAlgorithmName = "MD5";
        //密码
        Object credentials = pwd;
        //盐值
        if(null == saltStr) {
            saltStr = DEFALT_SALT;
        }
        Object salt = ByteSource.Util.bytes(saltStr);
        int hashIterations = 1024;

        Object result = new SimpleHash(hashAlgorithmName, credentials, salt, hashIterations);
        return result.toString();
    }

    public static String getBase64Pwd(String account, String pwd){
        String hashAlgorithmName = "SHA1";
        //密码
        Object credentials = pwd;
        //账号
        Object salt = ByteSource.Util.bytes(account);
        int hashIterations = 1024;

        Object result = new SimpleHash(hashAlgorithmName, credentials, salt, hashIterations);
        return result.toString();
    }

    public static String encBase64(String str) {
        return Base64.encodeToString(str.getBytes());
    }
    public static String decBase64(String str){
        return Base64.decodeToString(str);
    }


}
