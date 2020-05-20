package cn.sk.mask.sys.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Collection;

@Component
@Slf4j
public class AppContext implements ApplicationContextAware {
    private static ApplicationContext applicationContext = null;


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (AppContext.applicationContext == null) {
            AppContext.applicationContext = applicationContext;
        }
    }


    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }

    public static <T> Collection<T> getBeans(Class<T> clazz) {
        return applicationContext.getBeansOfType(clazz).values();
    }
    public static String getActiveProfile() {
        String[] activeProfile = AppContext.getApplicationContext().getEnvironment().getActiveProfiles();
        if(null == activeProfile|| activeProfile.length <=0) {
            return "";
        }
        return activeProfile[0];
    }
    public static InputStream getInputStreamByFilePath(String filePath) {
        if(StringUtils.isBlank(filePath)) {
            return null;
        }
        InputStream in = null;
        // FIXME: 2020/4/7 部署记得把loc改为prod
        if(StringUtils.equals("prod",getActiveProfile())) {
            ApplicationHome h = new ApplicationHome(AppContext.class);
            // 本地获取的路径 D:\idea\springboot2.x\target  upload 跟 项目jar平级
//            jar
            String path = h.getSource().getParent();
            File file = new File(path,filePath);
            try {
                in = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }else{
            in = AppContext.class.getClassLoader().getResourceAsStream(filePath);
        }
        return in;
    }
    public static String getJarPath() {
        String path = "";
        // FIXME: 2020/4/7 部署记得把loc改为prod
        if(StringUtils.equals("prod",getActiveProfile())) {
            ApplicationHome h = new ApplicationHome(AppContext.class);
            // 本地获取的路径 D:\idea\springboot2.x\target  upload 跟 项目jar平级
//            jar
            path = h.getSource().getParent();
        }else{
            path =  ClassUtils.getDefaultClassLoader().getResource("").getPath();
            if(path.startsWith("/")) {
                path = path.substring(1);
            }
        }
        return path;
    }


}

