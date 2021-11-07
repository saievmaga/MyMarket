package com.saiev.MyMarket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@ComponentScan("com.saiev.MyMarket")
@EnableAutoConfiguration
@EnableAspectJAutoProxy
public class MyMarketApplication {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(MyMarketApplication.class, args);
		Message
	}
}
