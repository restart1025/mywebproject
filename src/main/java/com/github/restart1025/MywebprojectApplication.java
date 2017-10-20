package com.github.restart1025;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
@MapperScan(basePackages= "com.github.restart1025.dao")
//@ComponentScan("com.github.restart1025.dao")
public class MywebprojectApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(MywebprojectApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(MywebprojectApplication.class);
	}
}
