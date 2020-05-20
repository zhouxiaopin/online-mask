package cn.sk.mask.sys.config.datasource;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.google.common.collect.Maps;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class DruidConfig {



    //访问地址http://172.31.211.181:8083/temp/druid/login.html
    //配置druid监控
    //1.配置一个管理后台的Servlet
    @Bean
    public ServletRegistrationBean statViewServle(){
        //org.springframework.boot.context.embedded.ServletRegistrationBean提供类的进行注册.
        ServletRegistrationBean<StatViewServlet> servletRegistrationBean = new ServletRegistrationBean<>(new StatViewServlet(),"/druid/*");

        Map<String,String> initParams = Maps.newHashMap();
        //白名单
        initParams.put("allow","172.31.211.181");
        //IP黑名单 (存在共同时，deny优先于allow)
        initParams.put("deny","127.0.0.2");
        //登录查看信息的账号密码
        initParams.put("loginUsername","admin");
        initParams.put("loginPassword","123456");
        //是否能够重置数据
        initParams.put("resetEnable","false");


        //设置初始化参数：initParams
        servletRegistrationBean.setInitParameters(initParams);

        return servletRegistrationBean;

    }


    //2.配置一个web监控的filter
    @Bean
    public FilterRegistrationBean webStatFilter(){

        FilterRegistrationBean<WebStatFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new WebStatFilter());

        //添加过滤规则.
        bean.addUrlPatterns("/*");

        Map<String,String> initParams = Maps.newHashMap();
        //添加不需要忽略的格式信息
        initParams.put("exclusions","*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");

        bean.setInitParameters(initParams);
        return bean;

    }
}
