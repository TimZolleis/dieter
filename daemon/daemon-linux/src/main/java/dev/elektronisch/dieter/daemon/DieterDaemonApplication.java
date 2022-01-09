package dev.elektronisch.dieter.daemon;

import dev.elektronisch.dieter.daemon.common.AbstractDieterDaemonApplication;
import dev.elektronisch.dieter.daemon.common.Bootstrap;

import java.io.File;
import java.nio.file.Path;

public final class DieterDaemonApplication extends AbstractDieterDaemonApplication {

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
        return null;
    }

    @Override
    public boolean isCurrentUserDaemonUser() {
        return false;
    }

    public static void main(final String[] args) {
        Bootstrap.run(DieterDaemonApplication::new, args);
    }
}
