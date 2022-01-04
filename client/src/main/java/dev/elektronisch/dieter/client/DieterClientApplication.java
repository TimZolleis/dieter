package dev.elektronisch.dieter.client;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configurator;

@Slf4j
public final class DieterClientApplication {

    public void enable() {
    }

    public void disable() {

    }

    public static void main(String[] args) {
        DieterClientApplication application = new DieterClientApplication();

        // Registering shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            application.disable();
            Configurator.shutdown((LoggerContext) LogManager.getContext());
        }));

        // Starting the application in a separate thread
        new Thread(application::enable).start();

        // Blocking the main thread
        try {
            synchronized (application) {
                application.wait();
            }
        } catch (final InterruptedException e) {
            log.error("An error occurred while blocking main thread", e);
        }
    }
}
