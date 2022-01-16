package dev.elektronisch.dieter.server.mail;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.Set;

@ConstructorBinding
@ConfigurationProperties("mail")
public final record MailConfigurationProperties(String hostname, int port,
                                                String username, String password,
                                                Set<MailTemplate> templates) {
}
