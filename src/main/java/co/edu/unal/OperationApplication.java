package co.edu.unal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaAuditing
//@EnableJpaRepositories("co.edu.unal.repository.**")
public class OperationApplication {

	public static void main(String[] args) {
		SpringApplication.run(OperationApplication.class, args);
	}

}
