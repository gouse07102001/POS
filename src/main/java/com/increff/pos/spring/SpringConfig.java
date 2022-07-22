package com.increff.pos.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;


@Configuration
@ComponentScan("com.increff.pos")
@PropertySource(value = "classpath:pos.properties", ignoreResourceNotFound = true) //
public class SpringConfig {


}
