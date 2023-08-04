package org.knou.keyproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

//@EnableJpaAuditing
@SpringBootApplication
public class KeyProjectApplication extends SpringBootServletInitializer {

//	@Override
//	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
//		return application.sources(KeyProjectApplication.class);
//
//	}

	public static void main(String[] args) {
		SpringApplication.run(KeyProjectApplication.class, args);
	}

	@Bean
	MappingJackson2JsonView jsonView() {
		return new MappingJackson2JsonView();
	}
}
