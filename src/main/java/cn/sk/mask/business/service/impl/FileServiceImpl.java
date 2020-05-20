package cn.sk.mask.business.service.impl;


import cn.sk.mask.business.service.IFileService;
import cn.sk.mask.sys.common.ServerResponse;
import cn.sk.mask.sys.common.SysConst;
import cn.sk.mask.sys.utils.AppContext;
import cn.sk.mask.sys.utils.Base64Util;
import cn.sk.mask.sys.utils.FileUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Slf4j
@Service
public class FileServiceImpl implements IFileService {

    //    @Value("${ftp.server.img.http.prefix}")
//    private String ftpServerImgHttpPrefix;
//    @Value("${ftp.server.video.http.prefix}")
//    private String ftpServerVideoHttpPrefix;
    @Value("${sk.httpPrefix}")
    private String httpPrefix;
//    @Value("${rp.picRemotePath}")
//    private String picRemotePath;
//    @Value("${us.videoRemotePath}")
//    private String videoRemotePath;

//    @Autowired
//    private FTPUtil ftpUtil;

    @Override
    public ServerResponse downTemplateFile(HttpServletResponse response, String realPath) {

//        InputStream inputStream = null;
//        OutputStream outputStream = null;
        String fileName = SysConst.DOWNLOAD_FILE_PREFIX+realPath;
        boolean flag = FileUtils.exportFile(response, fileName);
        if(!flag) {
            log.error("下载失败");
            return ServerResponse.createByErrorMessage("下载失败");
        }
        return ServerResponse.createBySuccess();
    }

    @Override
    public ServerResponse imgUpload(String[] base64Strs, String path){
        List<MultipartFile> files = Lists.newArrayList();

        for(int i = 0, len = base64Strs.length; i < len; i++) {
            MultipartFile file = Base64Util.base64ToMultipart(base64Strs[i]);
            files.add(file);
        }
        return upload(files,SysConst.UPLOAD_FILE_IMG_PREFIX+path);
//        return upload(files,path,"",picRemotePath);

    }
    @Override
    public ServerResponse imgUpload(String base64Str, String path){

        MultipartFile file = Base64Util.base64ToMultipart(base64Str);
        return upload(file,SysConst.UPLOAD_FILE_IMG_PREFIX+path);
//        return upload(files,path,"",picRemotePath);

    }
    @Override
    public ServerResponse imgUpload(MultipartFile[] files, String path){
        return upload(Lists.newArrayList(files),SysConst.UPLOAD_FILE_IMG_PREFIX+path);
//        return upload(Lists.newArrayList(files),path,ftpServerVideoHttpPrefix,picRemotePath);
    }
//    @Override
//    public ServerResponse videoUpload(MultipartFile[] files, String path){
//        return upload(Lists.newArrayList(files),path,ftpServerVideoHttpPrefix,videoRemotePath);
//    }


    @Override
    public ServerResponse upload(MultipartFile[] files, String path){
        return upload(Lists.newArrayList(files),path);
    }

    @Override
    public ServerResponse upload(MultipartFile file, String path) {

        String fileName = file.getOriginalFilename();
        //扩展名
        //abc.jpg
        String fileExtensionName = fileName.substring(fileName.lastIndexOf(".")+1);
        String uploadFileName = UUID.randomUUID().toString()+"."+fileExtensionName;
        String datePath = FileUtils.getYyyyMmDdPath();
        File fileDir = new File(AppContext.getJarPath()+"/"+SysConst.UPLOAD_FILE_PREFIX+path+datePath);
        if(!fileDir.exists()){
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }
        log.info("开始上传文件,上传文件的文件名:{},上传的路径:{},新文件名:{}",fileName,fileDir.getAbsolutePath(),uploadFileName);
        File targetFile = new File(fileDir,uploadFileName);
        Map<String,Object> dataMap = Maps.newHashMap();
        try {
            file.transferTo(targetFile);

            String targetFileName = datePath+"/"+targetFile.getName();

            String url = httpPrefix+SysConst.UPLOAD_FILE_PREFIX+path+targetFileName;


            dataMap.put("fileName",targetFileName);
            dataMap.put("url",url);
        } catch (IOException e) {
            log.error("上传文件异常",e);
            return ServerResponse.createByErrorMessage("上传失败");
        }

        return ServerResponse.createBySuccess("上传成功",dataMap);
    }

    private ServerResponse upload(List<MultipartFile> files, String path){
        List<String> urls = Lists.newLinkedList();
        List<String> fileNames = Lists.newLinkedList();

        List<File> targetFiles = Lists.newLinkedList();

        for(int i = 0, len = files.size(); i < len; i++) {
            MultipartFile file = files.get(i);
            String fileName = file.getOriginalFilename();
            //扩展名
            //abc.jpg
            String fileExtensionName = fileName.substring(fileName.lastIndexOf(".")+1);
            String uploadFileName = UUID.randomUUID().toString()+"."+fileExtensionName;
            String datePath = FileUtils.getYyyyMmDdPath();
            File fileDir = new File(AppContext.getJarPath()+"/"+SysConst.UPLOAD_FILE_PREFIX+path+datePath);
            if(!fileDir.exists()){
                fileDir.setWritable(true);
                fileDir.mkdirs();
            }
            log.info("开始上传文件,上传文件的文件名:{},上传的路径:{},新文件名:{}",fileName,fileDir.getAbsolutePath(),uploadFileName);
            File targetFile = new File(fileDir,uploadFileName);

            try {
                file.transferTo(targetFile);

                String targetFileName = datePath+"/"+targetFile.getName();

                String url = httpPrefix+SysConst.UPLOAD_FILE_PREFIX+path+targetFileName;

                fileNames.add(targetFileName);
                urls.add(url);

                //文件已经上传成功了
                targetFiles.add(targetFile);

            } catch (IOException e) {
                log.error("上传文件异常",e);
                return ServerResponse.createByErrorMessage("上传失败");
            }
        }

        Map<String,Object> dataMap = Maps.newHashMap();
        dataMap.put("fileNames",fileNames);
        dataMap.put("urls",urls);
        return ServerResponse.createBySuccess("上传成功",dataMap);

        /*try {

            //上传到ftp服务器上
            boolean uploadResult = ftpUtil.uploadFile(remotePath,targetFiles);
            for(int i = 0, len = targetFiles.size(); i < len; i++) {
                targetFiles.get(i).delete();
            }

            if(!uploadResult) {
                return ServerResponse.createByErrorMessage("上传失败");
            }

            //已经上传到ftp服务器上
            Map<String,Object> dataMap = Maps.newHashMap();
            dataMap.put("fileNames",fileNames);
            dataMap.put("urls",urls);
            return ServerResponse.createBySuccess("上传成功",dataMap);

        } catch (IOException e) {
            log.error("上传文件异常",e);
            return ServerResponse.createByErrorMessage("上传失败");
        }*/

    }

    /**
     *
     * @param files 要上传的多个文件
     * @param path  图片缓存路径
     * @param HttpPrefix 上传成功后返回url的前缀
     * @param remotePath  上传到ftp的路径名
     * @return
     */
    private ServerResponse upload(List<MultipartFile> files, String path, String HttpPrefix, String remotePath){
        List<String> urls = Lists.newLinkedList();
        List<String> fileNames = Lists.newLinkedList();

        List<File> targetFiles = Lists.newLinkedList();

        for(int i = 0, len = files.size(); i < len; i++) {
            MultipartFile file = files.get(i);
            String fileName = file.getOriginalFilename();
            //扩展名
            //abc.jpg
            String fileExtensionName = fileName.substring(fileName.lastIndexOf(".")+1);
            String uploadFileName = UUID.randomUUID().toString()+"."+fileExtensionName;
            log.info("开始上传文件,上传文件的文件名:{},上传的路径:{},新文件名:{}",fileName,path,uploadFileName);

            File fileDir = new File(path);
            if(!fileDir.exists()){
                fileDir.setWritable(true);
                fileDir.mkdirs();
            }
            File targetFile = new File(path,uploadFileName);

            try {
                file.transferTo(targetFile);

                String targetFileName = targetFile.getName();
                String url = HttpPrefix+targetFileName;
                fileNames.add(targetFileName);
                urls.add(url);

                //文件已经上传成功了
                targetFiles.add(targetFile);

            } catch (IOException e) {
                log.error("上传文件异常",e);
                return ServerResponse.createByErrorMessage("上传失败");
            }
        }

        Map<String,Object> dataMap = Maps.newHashMap();
        dataMap.put("fileNames",fileNames);
        dataMap.put("urls",urls);
        return ServerResponse.createBySuccess("上传成功",dataMap);

        /*try {

            //上传到ftp服务器上
            boolean uploadResult = ftpUtil.uploadFile(remotePath,targetFiles);
            for(int i = 0, len = targetFiles.size(); i < len; i++) {
                targetFiles.get(i).delete();
            }

            if(!uploadResult) {
                return ServerResponse.createByErrorMessage("上传失败");
            }

            //已经上传到ftp服务器上
            Map<String,Object> dataMap = Maps.newHashMap();
            dataMap.put("fileNames",fileNames);
            dataMap.put("urls",urls);
            return ServerResponse.createBySuccess("上传成功",dataMap);

        } catch (IOException e) {
            log.error("上传文件异常",e);
            return ServerResponse.createByErrorMessage("上传失败");
        }*/

    }

}