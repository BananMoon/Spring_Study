package com.moonz.memoProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class MemoProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(MemoProjectApplication.class, args);
	}

}
