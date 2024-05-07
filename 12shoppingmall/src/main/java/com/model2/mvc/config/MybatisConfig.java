package com.model2.mvc.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages="com.model2.mvc.service",
	annotationClass = org.apache.ibatis.annotations.Mapper.class )
public class MybatisConfig {

}
