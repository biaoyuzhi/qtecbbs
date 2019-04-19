package cn.qtec.bbs;

import com.jfinal.core.JFinalFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class QtecbbsApplication {

	public static void main(String[] args) {
		SpringApplication.run(QtecbbsApplication.class, args);
	}

	@Bean
	public FilterRegistrationBean jfFilterReg(){
		FilterRegistrationBean reg = new FilterRegistrationBean();
		reg.setFilter(new JFinalFilter());
		reg.addInitParameter("configClass", "cn.qtec.bbs.config.AppJFConfig");
//		reg.addUrlPatterns("/*");
		return reg;
	}

}

