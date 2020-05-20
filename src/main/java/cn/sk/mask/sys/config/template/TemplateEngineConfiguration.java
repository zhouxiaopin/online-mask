package cn.sk.mask.sys.config.template;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import cn.sk.mask.sys.custom.dialect.SkDialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.WebApplicationContext;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * thymeleaf自定义标签库配置
 */
@Configuration
public class TemplateEngineConfiguration {
    @Value("${spring.thymeleaf.prefix}")
    private String prefix;
    @Value("${spring.thymeleaf.suffix}")
    private String suffix;
    @Value("${spring.thymeleaf.mode}")
    private String templateMode;
    @Value("${spring.thymeleaf.cache}")
    private boolean cacheable;
    @Value("${spring.thymeleaf.encoding}")
    private String characterEncoding;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setPrefix(prefix);
//        templateResolver.setPrefix("/templates/thymeleaf/");
        templateResolver.setSuffix(suffix);
        templateResolver.setTemplateMode(templateMode);
        templateResolver.setCacheable(cacheable);
        templateResolver.setCharacterEncoding(characterEncoding);
        return templateResolver;
    }
    @Bean("templateEngine")
    public SpringTemplateEngine templateEngine(SpringResourceTemplateResolver templateResolver, SkDialect skDialect){
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        Set<IDialect> additionalDialects=new LinkedHashSet<>();
        //自定义方言
//        additionalDialects.add(new TiccDialect());
//        additionalDialects.add(new SkDialect());
        additionalDialects.add(new ShiroDialect());
        additionalDialects.add(skDialect);
        templateEngine.setAdditionalDialects(additionalDialects);
//        templateEngine.setDialect(ticcDialect);
        templateEngine.setTemplateResolver(templateResolver);
        return templateEngine;
    }
    @Bean
    public ThymeleafViewResolver viewResolver(@Qualifier("templateEngine") SpringTemplateEngine templateEngine){
        ThymeleafViewResolver thymeleafViewResolver = new ThymeleafViewResolver();
        thymeleafViewResolver.setTemplateEngine(templateEngine);
        thymeleafViewResolver.setCharacterEncoding(characterEncoding);
        thymeleafViewResolver.setOrder(1);
        return thymeleafViewResolver;
    }


/*    @Bean
    public ContentNegotiatingViewResolver getViewResolver(){
        ServletContext servletContext = webApplicationContext.getServletContext();

        ServletContextTemplateResolver templateResolver=new ServletContextTemplateResolver(servletContext);
        templateResolver.setPrefix(prefix);
//        templateResolver.setPrefix("/templates/thymeleaf/");
        templateResolver.setSuffix(suffix);
        templateResolver.setTemplateMode(templateMode);
        templateResolver.setCacheable(cacheable);
        templateResolver.setCharacterEncoding(characterEncoding);
//        templateResolver.setPrefix("/WEB-INF/views/");
//        templateResolver.setSuffix(".html");
//        templateResolver.setTemplateMode("HTML5");
//        templateResolver.setCacheable(false);
//        templateResolver.setCharacterEncoding("UTF-8");
        Set<IDialect> additionalDialects=new LinkedHashSet<>();
        //自定义方言
        additionalDialects.add(new TiccDialect());
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setAdditionalDialects(additionalDialects);
        templateEngine.setTemplateResolver(templateResolver);

        ThymeleafViewResolver thymeleafViewResolver = new ThymeleafViewResolver();
        thymeleafViewResolver.setTemplateEngine(templateEngine);
        thymeleafViewResolver.setCharacterEncoding(characterEncoding);
        thymeleafViewResolver.setOrder(1);

        List<ViewResolver> viewResolvers= new ArrayList<>();
        viewResolvers.add(thymeleafViewResolver);
        ContentNegotiatingViewResolver viewResolver = new ContentNegotiatingViewResolver();
        viewResolver.setViewResolvers(viewResolvers);
        return viewResolver;
    }*/

}
