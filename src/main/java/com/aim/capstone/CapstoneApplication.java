package com.aim.capstone;

import com.aim.capstone.properties.CorsUrlProperties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;


@SpringBootApplication
@EnableConfigurationProperties({ CorsUrlProperties.class })
public class CapstoneApplication 
{

	public static void main(String[] args) 
	{
		SpringApplication.run(CapstoneApplication.class, args);
	}

}
