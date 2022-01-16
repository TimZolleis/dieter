package dev.elektronisch.dieter.daemon.common;

import lombok.extern.slf4j.Slf4j;

import java.util.function.Supplier;

@Slf4j
public final class Bootstrap {

    private Bootstrap() {

    }

    public static void run(final Supplier<AbstractDieterDaemonApplication> supplier) {
        final AbstractDieterDaemonApplication application = supplier.get();

        Runtime.getRuntime().addShutdownHook(new Thread(application::internalDisable));
        new Thread(application::internalEnable).start();

        try {
            synchronized (application) {
                application.wait();
            }
        } catch (final InterruptedException e) {
            log.error("An error occurred while waiting", e);
        }
    }
}
