package be.bosa.ebox.providerbsservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class ProviderBsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProviderBsServiceApplication.class, args);
    }

}
