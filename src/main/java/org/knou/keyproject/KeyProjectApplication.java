package org.knou.keyproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

//@EnableJpaAuditing
@SpringBootApplication
public class KeyProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(KeyProjectApplication.class, args);
	}

	@Bean
	MappingJackson2JsonView jsonView() {
		return new MappingJackson2JsonView();
	}
}
