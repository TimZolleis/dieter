package dev.elektronisch.dieter.daemon;

import com.sun.security.auth.module.NTSystem;
import dev.elektronisch.dieter.daemon.common.AbstractDieterDaemonApplication;
import dev.elektronisch.dieter.daemon.common.Bootstrap;

import java.io.File;

public final class DieterDaemonApplication extends AbstractDieterDaemonApplication {

    private final File applicationDirectory = new File("C:\\Program Files\\Dieter");

    @Override
    public void enable() {

    }

    @Override
    public void disable() {

    }

    @Override
    public void install() {

    }

    @Override
    public void uninstall() {

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
        Bootstrap.run(DieterDaemonApplication::new, args);
    }
}
