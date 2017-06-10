package com.lexue;

import com.lexue.service.VideoIndexService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.lexue")
public class BootApplication {

	public static void main(String[] args) {
		SpringApplication.run(BootApplication.class, args);

//		ConfigurableApplicationContext context = SpringApplication.run(BootApplication.class, args);
//		VideoIndexService videoIndexService = context.getBean(VideoIndexService.class);
	}
}
