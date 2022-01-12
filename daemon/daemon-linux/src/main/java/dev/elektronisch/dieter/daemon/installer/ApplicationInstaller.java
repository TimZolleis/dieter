package dev.elektronisch.dieter.daemon.installer;

import dev.elektronisch.dieter.daemon.DieterDaemonApplication;
import dev.elektronisch.dieter.daemon.common.installer.AbstractApplicationInstaller;

import java.util.UUID;

public final class ApplicationInstaller extends AbstractApplicationInstaller {

    public ApplicationInstaller(final DieterDaemonApplication application) {
        super(application);
    }

    @Override
    protected boolean install(final UUID deviceKey) {
        return false;
    }

    @Override
    protected boolean uninstall() {
        return false;
    }
}
