package dev.elektronisch.dieter.daemon.common;

import com.google.common.base.Preconditions;
import dev.elektronisch.dieter.daemon.common.communication.DaemonCommunicationService;
import dev.elektronisch.dieter.daemon.common.configuration.ConfigurationUtil;
import dev.elektronisch.dieter.daemon.common.configuration.DaemonConfiguration;
import dev.elektronisch.dieter.daemon.common.installer.AbstractApplicationInstaller;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Getter
public abstract class AbstractDieterDaemonApplication {

    private File configurationFile;
    private DaemonConfiguration configuration;

    private DaemonCommunicationService communicationService;

    void internalEnable() {
        final File workingDirectory = determineWorkingDirectory();
        final File applicationDirectory = getApplicationDirectory();
        Preconditions.checkNotNull(applicationDirectory, "application directory may not be null");

        configurationFile = new File(applicationDirectory, "config.json");
        if (configurationFile.exists()) {
            configuration = ConfigurationUtil.readConfiguration(configurationFile);
        }

        if (!workingDirectory.equals(applicationDirectory)) {
            final AbstractApplicationInstaller installer = getInstaller();
            if (installer == null) {
                System.exit(1);
                return;
            }

            installer.run();
            return;
        }

        if (!isCurrentUserDaemonUser()) {
            final AbstractApplicationInstaller installer = getInstaller();
            if (installer == null) {
                System.exit(1);
                return;
            }

            installer.run();
            return;
        }

        log.info("Version: {}", configuration.getApplicationVersion());
        log.info("Key: {}\n", configuration.getDeviceKey());

        log.info("Starting communication service...");
        communicationService = new DaemonCommunicationService(this);

        log.info("Enabling operating-system implementation...");
        enable();
    }

    void internalDisable() {
        log.info("Disabling operating-system implementation...");
        disable();
    }

    public abstract void enable();

    public abstract void disable();

    public abstract void shutdownMachine();

    public abstract void restartMachine();

    public abstract AbstractApplicationInstaller getInstaller();

    public abstract File getApplicationDirectory();

    public abstract boolean isCurrentUserDaemonUser();

    private File determineWorkingDirectory() {
        final Path currentRelativePath = Paths.get("");
        return currentRelativePath.toAbsolutePath().toFile();
    }
}
