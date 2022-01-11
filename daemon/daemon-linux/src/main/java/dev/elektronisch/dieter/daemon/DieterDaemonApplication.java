package dev.elektronisch.dieter.daemon;

import com.sun.security.auth.module.UnixSystem;
import dev.elektronisch.dieter.daemon.common.AbstractDieterDaemonApplication;
import dev.elektronisch.dieter.daemon.common.Bootstrap;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Slf4j
public final class DieterDaemonApplication extends AbstractDieterDaemonApplication {

    private final File applicationDirectory = new File("/usr/bin/dieter");

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
            Runtime.getRuntime().exec("shutdown -h now");
        } catch (final IOException e) {
            log.error("An error occurred while restarting system");
        }
    }

    @Override
    public void restartSystem() {
        try {
            Runtime.getRuntime().exec("shutdown -r now");
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
        final UnixSystem unixSystem = new UnixSystem();
        return unixSystem.getUsername().equals("dieter");
    }

    public static void main(final String[] args) {
        Bootstrap.run(DieterDaemonApplication::new);
    }
}
