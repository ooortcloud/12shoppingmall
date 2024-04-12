package com.model2.mvc.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.model2.mvc.common.web.LogonCheckInterceptor;

//===================== 추가된 Class  ======================//
//Interceptor 등록하는 WebMvcCongigurer 구현 Bean
//=======================================================//
@Configuration
public class WebConfig implements WebMvcConfigurer {
	
	@Autowired
	@Qualifier("logonCheckInterceptor")
	private LogonCheckInterceptor interceptor;
	
	public WebConfig() {
		System.out.println("WebConfig instance 생성");
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		
		List<String> patterns = new ArrayList<String>();
		patterns.add("/user/**");
		patterns.add("/product/**");
		patterns.add("/purchase/**");
		registry.addInterceptor(interceptor).addPathPatterns(patterns);
	}

}
