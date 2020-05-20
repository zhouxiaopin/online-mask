package cn.sk.mask.business.controller;

import cn.sk.mask.business.service.IFileService;
import cn.sk.mask.sys.common.ServerResponse;
import cn.sk.mask.sys.common.SysConst;
import cn.sk.mask.sys.custom.ueditor.SkActionEnter;
import cn.sk.mask.sys.utils.AppContext;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.*;

@RestController
@RequestMapping("ueditor")
public class UeditorController {
    // 读取配置文件中配置的上传文件保存地址
    @Value("${sk.ueditor.saveDir}")
    private String ueditorSaveDir;
    @Value("${sk.httpPrefix}")
    private String httpPrefix;
    @Autowired
    private IFileService fileService;


    /**
     * 接收和获取百度编辑插件的文件，需要注意以下几点
     * 1，需要在 ueditorSaveDir 目录下创建 ueditor 目录，将 config.json 拷贝到该路径下
     * 2，必须将 ueditorSaveDir 目录设置为静态资源路径，否则上传的文件可能无法访问
     * 3，注意访问配置文件的方式 ueditor.config.js 请求的路径就是 config.json 放置的同级路径
     */
    @GetMapping("config")
    public String config(HttpServletRequest request, String action) {
        String path = AppContext.getJarPath()+ueditorSaveDir;
        System.out.println(path);
        String exec = new SkActionEnter(request, path).exec();
        if(action!=null && (action.equals("listfile") || action.equals("listimage"))) {
            exec = exec.replace(path.substring(1), "/");
        }
        return exec;
//        String result = new ActionEnter(request, ueditorSaveDir).exec();
//        ueditorSaveDir = ueditorSaveDir.replaceAll("\\\\", "/");
//        if (action != null && (action.equals("listfile") || action.equals("listimage"))) {
//            return result.replaceAll(ueditorSaveDir, "");
//        }
//        return result;
    }
    //图片上传
    @PostMapping("uploadImg")
    public Map<String,String> uploadImg(@RequestParam("upfile") MultipartFile file){
        ServerResponse upload = fileService.upload(file, "ueditor/img/");
        Map<String,String> result = Maps.newHashMap();
        if(upload.isSuccess()) {
            Map<String,String> data = (Map<String, String>) upload.getData();
            result.put("state","SUCCESS");
            result.put("url",data.get("url"));
            result.put("title",data.get("fileName"));
            result.put("original",data.get("fileName"));
        }else{
            result.put("state","FAIL");
        }


        return result;

    }
    /* 涂鸦图片上传 */
    @PostMapping("uploadScrawl")
    public Map<String,String> uploadScrawl(@RequestParam(value = "upfile") String content){
        ServerResponse upload = fileService.imgUpload("data:image/jpg;base64,"+content, "ueditor/img/");
        Map<String,String> result = Maps.newHashMap();
        if(upload.isSuccess()) {
            Map<String,String> data = (Map<String, String>) upload.getData();
            result.put("state","SUCCESS");
            result.put("url",data.get("url"));
            result.put("title",data.get("fileName"));
            result.put("original",data.get("fileName"));
        }else{
            result.put("state","FAIL");
        }
        return result;

    }
    //视频上传
    @PostMapping("uploadVideo")
    public Map<String,String> uploadVideo(@RequestParam("upfile") MultipartFile file){
        ServerResponse upload = fileService.upload(file, "ueditor/video/");
        Map<String,String> result = Maps.newHashMap();
        if(upload.isSuccess()) {
            Map<String,String> data = (Map<String, String>) upload.getData();
            result.put("state","SUCCESS");
            result.put("url",data.get("url"));
            result.put("title",data.get("fileName"));
            result.put("original",data.get("fileName"));
        }else{
            result.put("state","FAIL");
        }


        return result;

    }
    //上传附件
    @PostMapping("uploadFile")
    public Map<String,String> uploadFile(@RequestParam("upfile") MultipartFile file){
        ServerResponse upload = fileService.upload(file, "ueditor/file/");
        Map<String,String> result = Maps.newHashMap();
        if(upload.isSuccess()) {
            Map<String,String> data = (Map<String, String>) upload.getData();
            result.put("state","SUCCESS");
            result.put("url",data.get("url"));
            result.put("title",data.get("fileName"));
            result.put("original",data.get("fileName"));
        }else{
            result.put("state","FAIL");
        }


        return result;

    }
    //列出指定路径图片
    @GetMapping("listImage")
    public Map<String,Object> listImage(@RequestParam("start") Integer start,@RequestParam("size") Integer size){
        String dirPath = AppContext.getJarPath()+SysConst.UPLOAD_FILE_PREFIX+"ueditor/img";
        LinkedList<File> files = (LinkedList<File>) FileUtils.listFiles(new File(dirPath), null, true);

        List<Map<String,Object>> list = Lists.newArrayList();
        for(int i = start,len = files.size(); (i < len && (i-start)<size); i++) {
            File file = files.get(i);
            String path = file.getAbsolutePath().replace("\\","/").replace(AppContext.getJarPath(),"");
            Map<String,Object> item = Maps.newHashMap();
            item.put("url",httpPrefix+path);
            list.add(item);
        }
        Map<String,Object> result = Maps.newHashMap();
        result.put("state","SUCCESS");
        result.put("list",list);
        result.put("start",start);
        result.put("total",files.size());

        return result;

    }
    //列出指定路径附件
    @GetMapping("listFile")
    public Map<String,Object> listFile(@RequestParam("start") Integer start,@RequestParam("size") Integer size){
        String dirPath = AppContext.getJarPath()+SysConst.UPLOAD_FILE_PREFIX+"ueditor/file";
        LinkedList<File> files = (LinkedList<File>) FileUtils.listFiles(new File(dirPath), null, true);

        List<Map<String,Object>> list = Lists.newArrayList();
        for(int i = start,len = files.size(); (i < len && (i-start)<size); i++) {
            File file = files.get(i);
            String path = file.getAbsolutePath().replace("\\","/").replace(AppContext.getJarPath(),"");
            Map<String,Object> item = Maps.newHashMap();
            item.put("url",httpPrefix+path);
            list.add(item);
        }
        Map<String,Object> result = Maps.newHashMap();
        result.put("state","SUCCESS");
        result.put("list",list);
        result.put("start",start);
        result.put("total",files.size());
        return result;

    }
}
