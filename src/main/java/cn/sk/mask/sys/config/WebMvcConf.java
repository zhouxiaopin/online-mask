package cn.sk.mask.sys.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 *@Deseription 
 *@Author zhoucp
 *@Date 2020/1/6 16:47
 **/
@Configuration
public class WebMvcConf implements WebMvcConfigurer {


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")	// 允许跨域访问的路径
                .allowedOrigins("*")	// 允许跨域访问的源
                .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")	// 允许请求方法
                .maxAge(168000)	// 预检间隔时间
                .allowedHeaders("*")  // 允许头部设置
                .allowCredentials(true);	// 是否发送cookie
    }

    /*@Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration corsConfiguration = new CorsConfiguration();
        *//* 是否允许请求带有验证信息 *//*
        corsConfiguration.setAllowCredentials(true);
        *//* 允许访问的客户端域名 *//*
        corsConfiguration.addAllowedOrigin("*");
        *//* 允许服务端访问的客户端请求头 *//*
        corsConfiguration.addAllowedHeader("*");
        *//* 允许访问的方法名,GET POST等 *//*
        corsConfiguration.addAllowedMethod("*");
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(urlBasedCorsConfigurationSource);
    }*/

//    静态资源配置
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        //需要配置1：--- 需要告知系统，这是要被当成静态文件的！  设置内部静态资源
//        //第一个方法设置访问路径前缀，第二个方法设置资源路径
//        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
//        registry.addResourceHandler("/upload/**").addResourceLocations("classpath:/upload/");
//        registry.addResourceHandler("/templates/**").addResourceLocations("classpath:/templates/");
//        //关键在这
//        //  获取与jar同级目录下的upload文件夹  设置与jar同级静态资源配置
//        ApplicationHome h = new ApplicationHome(getClass());
//        // 本地获取的路径 D:\idea\springboot2.x\target  upload 跟 项目jar平级
//        String path = h.getSource().getParent();
//        String realPath = path + "/static/";
//        registry.addResourceHandler("/static/**").addResourceLocations("file:"+realPath);
//    }
}
