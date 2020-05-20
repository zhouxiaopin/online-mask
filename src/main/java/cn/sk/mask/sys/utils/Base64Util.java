package cn.sk.mask.sys.utils;

import cn.sk.mask.sys.custom.Base64DecodedMultipartFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import java.io.IOException;

@Slf4j
public class Base64Util {

    /**
     *  base64 转MultipartFile文件
     * @param base64
     * @return
     */
    public static MultipartFile base64ToMultipart(String base64) {
        try {
            String[] baseStrs = base64.split(",");

            BASE64Decoder decoder = new BASE64Decoder();
            byte[] b = new byte[0];
            b = decoder.decodeBuffer(baseStrs[1]);

            for(int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            return new Base64DecodedMultipartFile(b, baseStrs[0]);
        } catch (IOException e) {
            log.error("base64 转MultipartFile文件失败：{}",e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
