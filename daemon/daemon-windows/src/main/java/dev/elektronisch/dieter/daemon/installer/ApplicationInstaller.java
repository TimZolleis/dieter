package dev.elektronisch.dieter.daemon.installer;

import dev.elektronisch.dieter.daemon.DieterDaemonApplication;
import dev.elektronisch.dieter.daemon.common.installer.AbstractApplicationInstaller;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.nio.file.Files;
import java.util.UUID;

@Slf4j
public final class ApplicationInstaller extends AbstractApplicationInstaller {

    public ApplicationInstaller(final DieterDaemonApplication application) {
        super(application);
    }

    @Override
    public boolean install(final UUID deviceKey) {
        final File applicationDirectory = getApplication().getApplicationDirectory();


        try {
            Files.createDirectories(applicationDirectory.toPath());


            runCommands();
        } catch (final Exception e) {
            log.error("An error occurred while installing", e);
        }
        return false;
    }

    @Override
    public boolean uninstall() {
        return false;
    }
}
