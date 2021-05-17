package com.root;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan("com.root.entity")
@EnableJpaRepositories("com.root.repository")
@SpringBootApplication
public class SpringBootJerseyApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootJerseyApplication.class, args);
	}

}
