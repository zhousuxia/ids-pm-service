package com.glaway.ids;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = {"com.glaway.*","com.alibaba.druid"})
@EnableDiscoveryClient
@EnableFeignClients
@ComponentScan(basePackages = {"com.glaway.*"})
@EnableTransactionManagement
@EntityScan(basePackages = {"com.glaway.*"})
@EnableAspectJAutoProxy(exposeProxy = true)
@EnableScheduling
@Configuration
@ImportResource(locations = {"classpath:spring-message-pm.xml"})
public class IDSServiceApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(IDSServiceApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(IDSServiceApplication.class, args);
    }
}

