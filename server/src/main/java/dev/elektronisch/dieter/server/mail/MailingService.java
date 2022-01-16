package dev.elektronisch.dieter.server.mail;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.api.mailer.config.TransportStrategy;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public final class MailingService {

    private final MailConfigurationProperties properties;
    private final Map<String, MailTemplate> templateMap = new HashMap<>();
    private final Map<String, String> mailContentMap = new HashMap<>();

    private Mailer mailer;

    public MailingService(final MailConfigurationProperties properties) {
        this.properties = properties;
    }

    @PostConstruct
    private void init() {
        properties.templates().forEach(template -> {
            // Trying reading mail template content from file
            try {
                final Path path = Path.of(template.contentFilePath());
                final String templateContent = Files.readString(path);
                mailContentMap.put(template.name(), templateContent);
            } catch (final InvalidPathException e) {
                log.error("Invalid content path given for mail template '{}'", template.name(), e);
                System.exit(1);
            } catch (final IOException e) {
                log.error("An error occurred while reading mail template '{}'", template.name(), e);
                System.exit(1);
            }

            // Put template into map if successful
            templateMap.put(template.name(), template);
        });

        // Initialize mailer
        mailer = MailerBuilder
                .withSMTPServer(properties.hostname(), properties.port(),
                        properties.username(), properties.password())
                .withTransportStrategy(TransportStrategy.SMTP_TLS)
                .buildMailer();
    }

    public void sendMail(final String recipientName, final String recipientAddress,
                         final String templateName, final String... replacements) {
        final MailTemplate mailTemplate = templateMap.get(templateName);
        if (mailTemplate == null) {
            throw new IllegalArgumentException("Invalid template: " + templateName);
        }

        sendMail(recipientName, recipientAddress, mailTemplate, replacements);
    }

    public void sendMail(final String recipientName, final String recipientAddress,
                         final MailTemplate template, final String... replacements) {
        Preconditions.checkNotNull(recipientAddress, "recipientAddress may not be null");
        Preconditions.checkNotNull(template, "template may not be null");
        Preconditions.checkNotNull(replacements, "replacements may not be null");

        // Handling parameters
        String subject = applyReplacements(template.subject(), replacements);
        String content = mailContentMap.get(template.name());
        Preconditions.checkNotNull(content, "content may not be null");

        content = applyReplacements(content, replacements);

        // Sending mail
        final Email email = EmailBuilder.startingBlank()
                .to(recipientName, recipientAddress)
                .from("Dieter", properties.username())
                .withSubject(subject)
                .withHTMLText(content)
                .buildEmail();
        mailer.sendMail(email, true).whenComplete((it, throwable) -> {
            if (throwable != null) {
                log.error("An error occurred while sending mail with template '{}' to '{} <{}>'",
                        template.name(), recipientName, recipientAddress, throwable);
                return;
            }

            log.info("Mail with template '{}' successfully sent to '{} <{}>'",
                    template.name(), recipientName, recipientAddress);
        });
    }

    private String applyReplacements(String initialString, final String... replacements) {
        for (int i = 0; i < replacements.length; i += 2) {
            initialString = initialString.replace(replacements[i], replacements[i + 1]);
        }
        return initialString;
    }
}
