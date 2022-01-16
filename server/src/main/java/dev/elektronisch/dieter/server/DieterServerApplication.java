package dev.elektronisch.dieter.server;

import dev.elektronisch.dieter.server.mail.MailConfigurationProperties;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collections;

@SpringBootApplication
@EnableConfigurationProperties(MailConfigurationProperties.class)
public class DieterServerApplication {

    public DieterServerApplication() {

    }

    public static void main(final String[] args) {
        SpringApplication.run(DieterServerApplication.class, args);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("DeviceDieter API")
                        .description("Yet another device management system")
                        .version(getClass().getPackage().getImplementationVersion())
                        .license(new License().name("MIT License").url("https://opensource.org/licenses/MIT")))
                .servers(Collections.singletonList(new Server().url("https://api.devicedieter.de/")));
    }
}
