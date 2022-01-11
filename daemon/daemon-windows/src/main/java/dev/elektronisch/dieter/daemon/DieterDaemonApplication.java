package dev.elektronisch.dieter.daemon;

import com.sun.security.auth.module.NTSystem;
import dev.elektronisch.dieter.daemon.common.AbstractDieterDaemonApplication;
import dev.elektronisch.dieter.daemon.common.Bootstrap;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Slf4j
public final class DieterDaemonApplication extends AbstractDieterDaemonApplication {

    private final File applicationDirectory = new File("C:\\Program Files\\Dieter");

    @Override
    public void enable() {

    }

    @Override
    public void disable() {

    }

    @Override
    public void install(final UUID deviceKey) {

    }

    @Override
    public void uninstall() {

    }

    @Override
    public void shutdownSystem() {
        try {
            Runtime.getRuntime().exec("shutdown /s /f /t 0");
        } catch (final IOException e) {
            log.error("An error occurred while restarting system");
        }
    }

    @Override
    public void restartSystem() {
        try {
            Runtime.getRuntime().exec("shutdown /r /f /t 0");
        } catch (final IOException e) {
            log.error("An error occurred while restarting system");
        }
    }

    @Override
    public File getApplicationDirectory() {
        return applicationDirectory;
    }

    @Override
    public boolean isCurrentUserDaemonUser() {
        final NTSystem system = new NTSystem();
        return system.getName().equals("SYSTEM");
    }

    public static void main(final String[] args) {
        Bootstrap.run(DieterDaemonApplication::new);
    }
}
