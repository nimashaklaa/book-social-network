package com.ami.book_net;

import com.ami.book_net.role.Role;
import com.ami.book_net.role.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableJpaAuditing
@EnableAsync
public class BookNetApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookNetApplication.class, args);
	}

	//a specific functional interface used to run a piece of code automatically right after the application starts up and the application context is fully loaded.
	@Bean
	public CommandLineRunner runner(RoleRepository roleRepository) {
		// by default, the USER role is created.
		return args -> {
			if(roleRepository.findByName("USER").isEmpty()){
				roleRepository.save(
						Role.builder()
								.name("USER")
								.build()
				);
			}
		};
	}
}
