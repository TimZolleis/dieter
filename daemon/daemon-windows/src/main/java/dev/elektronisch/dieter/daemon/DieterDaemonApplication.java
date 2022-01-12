package dev.elektronisch.dieter.daemon;

import com.sun.security.auth.module.NTSystem;
import dev.elektronisch.dieter.daemon.common.AbstractDieterDaemonApplication;
import dev.elektronisch.dieter.daemon.common.Bootstrap;
import dev.elektronisch.dieter.daemon.common.installer.AbstractApplicationInstaller;
import dev.elektronisch.dieter.daemon.installer.ApplicationInstaller;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;

@Slf4j
public final class DieterDaemonApplication extends AbstractDieterDaemonApplication {

    private final File applicationDirectory = new File("C:\\Program Files\\Dieter");
    private final ApplicationInstaller installer = new ApplicationInstaller(this);

    @Override
    public void enable() {

    }

    @Override
    public void disable() {

    }

    @Override
    public void shutdownMachine() {
        try {
            Runtime.getRuntime().exec("shutdown /s /f /t 0");
        } catch (final IOException e) {
            log.error("An error occurred while restarting system");
        }
    }

    @Override
    public void restartMachine() {
        try {
            Runtime.getRuntime().exec("shutdown /r /f /t 0");
        } catch (final IOException e) {
            log.error("An error occurred while restarting system");
        }
    }

    @Override
    public AbstractApplicationInstaller getInstaller() {
        return installer;
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
