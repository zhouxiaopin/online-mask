package cn.sk.mask;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
//扫描 mybatis dao 包路径
@MapperScan(basePackages = {"cn.sk.mask.sys.mapper","cn.sk.mask.business.mapper"})
//扫描 所有需要的包, 包含一些自用的工具类包 所在的路径
//@ComponentScan(basePackages= {"cn.gzticc"})
@EnableTransactionManagement
//开启定时任务
@EnableScheduling
//开启异步调用方法
@EnableAsync
public class OnlineMaskApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnlineMaskApplication.class, args);
	}

}
