package dev.elektronisch.dieter.daemon.installer;

import dev.elektronisch.dieter.daemon.DieterDaemonApplication;
import dev.elektronisch.dieter.daemon.common.installer.AbstractApplicationInstaller;
import org.apache.commons.lang3.NotImplementedException;

import java.util.UUID;

public final class ApplicationInstaller extends AbstractApplicationInstaller {

    public ApplicationInstaller(final DieterDaemonApplication application) {
        super(application);
    }

    @Override
    protected void install(final UUID deviceKey) throws Exception {
        throw new NotImplementedException();
    }

    @Override
    protected void uninstall() throws Exception {
        throw new NotImplementedException();
    }
}
