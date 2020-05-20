package cn.sk.mask.business.service;

import cn.sk.mask.sys.common.ServerResponse;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * 文件业务路径接口
 */
public interface IFileService {
    /**
     * 下载文件
     * @param realPath
     */
    ServerResponse downTemplateFile(HttpServletResponse response, String realPath);
    /**
     *
     * @param base64Strs
     * @param path
     * @return
     */
    ServerResponse imgUpload(String[] base64Strs, String path);
    ServerResponse imgUpload(String base64Str, String path);
    /**
     *
     * @param files 要上传的多个文件
     * @return
     */
    ServerResponse imgUpload(MultipartFile[] files, String path);

    ServerResponse upload(MultipartFile[] files, String path);
    ServerResponse upload(MultipartFile files, String path);

//    ServerResponse videoUpload(MultipartFile[] files, String path);
}
