package cn.sk.mask.business.controller;

import cn.sk.mask.business.service.IFileService;
import cn.sk.mask.sys.common.ServerResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 图片和视频上传的 Controller
 */
@RestController
@RequestMapping("upload")
public class UploadController{

    @Autowired
    private IFileService fileService;

    /**
     * 图片上传
     * @param base64Strs
     * @return
     */
    @PostMapping("imgUploadByBs64")
    public ServerResponse<Map<String,Object>> imgUploadByBs64(@RequestParam("base64Strs") String[] base64Strs, HttpServletRequest request){
        return fileService.imgUpload(base64Strs,"");
    }

    /**
     * 上传文件
     * @param files
     * @return
     */
    @PostMapping("file")
    public ServerResponse<Map<String,Object>> file(@RequestParam("file") MultipartFile[] files, HttpServletRequest request){
        String pathType = request.getParameter("pathType");
        if(StringUtils.isNotEmpty(pathType)) {
            String path = "";
            if("1".equals(pathType)) {
                path = "img/";
            }
            return fileService.upload(files,path);
        }
        return fileService.upload(files,"");

    }

    /**
     * 视频上传
     * @param files
     * @return
     */
//    @PostMapping("videoUpload")
//    public ServerResponse<Map<String,Object>> videoUpload(@RequestParam("files") MultipartFile[] files){
//        String path = request.getSession().getServletContext().getRealPath("upload");
//        return fileService.videoUpload(files,path);
//
//    }
}