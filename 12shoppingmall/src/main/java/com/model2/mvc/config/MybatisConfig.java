package com.model2.mvc.config;

import org.springframework.context.annotation.Configuration;

@Configuration
/* 이 방식은 @Mapper의 bean 생성이 안되는 문제에 효과가 없었다... 대신 MapperFactoryBean으로 수동으로 선언함으로써 해결했다.
@MapperScan(basePackages="com.model2.mvc.service",
	annotationClass = org.apache.ibatis.annotations.Mapper.class )
*/
public class MybatisConfig {

}
