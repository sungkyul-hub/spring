package org.skuhub.skuhub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "org.skuhub.skuhub.repository")
public class SkuhubApplication {

    public static void main(String[] args) {
        SpringApplication.run(SkuhubApplication.class, args);
    }

}
