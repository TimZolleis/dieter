package dev.elektronisch.dieter.daemon;

import dev.elektronisch.dieter.client.AuthenticatedDieterClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;

@Slf4j
public final class DieterDaemonApplication {

    public void enable() {
        final AuthenticatedDieterClient client = new AuthenticatedDieterClient("elektr0nisch", "test123");
    }

    public void disable() {

    }

    public static void main(String[] args) {
        DieterDaemonApplication application = new DieterDaemonApplication();

        // Registering shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            application.disable();
            LogManager.shutdown();
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
