package cn.sk.mask.sys.utils;


import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.UUID;

public class FileUtils {
    /**
     * 向客户端下载文件,弹出下载框.
     *
     * @param response
     *            (HttpServletResponse)
     * @param filePath
     *            (需要下载的文件路径)
     *            (文件名)
     * @throws IOException
     */
    public static boolean exportFile(HttpServletResponse response, String filePath){
        String fileName = UUID.randomUUID().toString()
                +filePath.substring(filePath.lastIndexOf("."));
        return exportFile(response,filePath,fileName);
    }

    /**
     * 向客户端下载文件,弹出下载框.
     *
     * @param response
     *            (HttpServletResponse)
     * @param filePath
     *            (需要下载的文件路径)
     * @param fileName
     *            (文件名)
     * @throws IOException
     */
    public static boolean exportFile(HttpServletResponse response, String filePath, String fileName){
        OutputStream out = null;
        InputStream in = null;
        try {

            // 获得文件名
            String filename = URLEncoder.encode(fileName, "UTF-8");
            // 定义输出类型(下载)
            response.setContentType("application/force-download");
            response.setHeader("filename", filename);
            // 定义输出文件头
            response.setHeader("content-disposition", "attachment;filename=" + filename);
            //如果想让浏览器能访问到其他的 响应头的话 需要在服务器上设置 Access-Control-Expose-Headers
            response.setHeader("Access-Control-Expose-Headers", "filename,content-disposition");

            // 取得输出流
            out = response.getOutputStream();
            in = AppContext.getInputStreamByFilePath(filePath);

            byte[] buffer = new byte[1024];
            int i = -1;
            while ((i = in.read(buffer)) != -1) {
                out.write(buffer, 0, i);
            }
            out.flush();
//        if (isDel) {
//            // 删除文件,删除前关闭所有的Stream.
//            file.delete();
//        }
            return true;
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if(null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    //获取日期路径
    public static String getYyyyMmDdPath(){
        StringBuilder path = new StringBuilder();
        path.append(DateUtils.getSystemTime("yyyy"));
        path.append("/");
        path.append(DateUtils.getSystemTime("MM"));
        path.append("/");
        path.append(DateUtils.getSystemTime("dd"));
        return path.toString();
    }
//    private static InputStream getResourceAsStreamByFileName(){
//      applicationContext.getEnvironment().getActiveProfiles()
//    }
//
//    static interface ResourceAsStream{
//        @Profile("loc")
//        InputStream getResourceAsStreamByFileName();
//    }
}
