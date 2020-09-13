package org.astropeci.skillbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SkillbotApplication {

	public static void main(String[] args) {
		SpringApplication.run(SkillbotApplication.class, args);
	}
}
