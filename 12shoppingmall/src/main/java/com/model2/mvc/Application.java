package com.model2.mvc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
// ���� class�� ���� ������ָ� ���� �� ������...
@MapperScan(basePackages="com.model2.mvc.service",
	annotationClass = org.apache.ibatis.annotations.Mapper.class )
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
