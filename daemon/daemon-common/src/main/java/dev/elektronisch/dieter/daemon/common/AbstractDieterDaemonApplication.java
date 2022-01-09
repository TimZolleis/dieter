package dev.elektronisch.dieter.daemon.common;

import dev.elektronisch.dieter.daemon.common.config.ConfigurationUtil;
import dev.elektronisch.dieter.daemon.common.config.DaemonConfig;
import lombok.extern.slf4j.Slf4j;

import java.io.Console;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
public abstract class AbstractDieterDaemonApplication {

    private DaemonConfig config;

    void internalEnable() {
        final File workingDirectory = determineWorkingDirectory();
        final File applicationDirectory = getApplicationDirectory();

        final File configurationFile = new File(applicationDirectory, "config.json");
        if (configurationFile.exists()) {
            config = ConfigurationUtil.readConfiguration(configurationFile);
        }

        if (workingDirectory.equals(applicationDirectory)) {
            if (!isCurrentUserDaemonUser()) {
                log.error("Current user isn't permitted to run this application.");
                System.exit(1);
                return;
            }

            // TODO implement updater
            enable();
        } else {
            log.info("DieterDaemon v" + getClass().getPackage().getImplementationVersion() + " - by Linus Groschke");
            log.info(" ");
            if (config != null) {
                log.info("Installed version: " + config.getApplicationVersion());
                log.info("Device key: " + config.getDeviceKey());
                log.info("Not installed on this machine!");
            } else {
                log.info("Not installed on this machine!");
            }
            log.info(" ");

            final Console console = System.console();
            while (!Thread.interrupted()) {
                log.info("What to do? (install/uninstall/configure):");
                final String option = console.readLine();
                switch (option) {
                    case "install" -> {
                        if (config != null) {
                            log.info("Already installed. Please uninstall first!");
                            continue;
                        }

                        install();
                        System.exit(0);
                    }
                    case "uninstall" -> {
                        if (config == null) {
                            log.info("Not yet installed!");
                            continue;
                        }

                        uninstall();
                        System.exit(0);
                    }
                    case "configure" -> {
                        if (config == null) {
                            log.info("Not yet installed. Please install first!");
                            continue;
                        }

                        final String rawDeviceKey = console.readLine("Please enter new device key: ");
                        try {
                            final UUID deviceKey = UUID.fromString(rawDeviceKey);
                            config.setDeviceKey(deviceKey);
                            ConfigurationUtil.saveConfiguration(configurationFile, config);
                        } catch (final IllegalArgumentException e) {
                            log.error("Invalid device key!");
                        }
                    }
                    default -> log.error("Invalid option!");
                }
            }
        }
    }

    void internalDisable() {
        disable();
    }

    public abstract void enable();

    public abstract void disable();

    public abstract void install();

    public abstract void uninstall();

    public abstract File getApplicationDirectory();

    public abstract boolean isCurrentUserDaemonUser();

    private File determineWorkingDirectory() {
        final Path currentRelativePath = Paths.get("");
        return currentRelativePath.toAbsolutePath().toFile();
    }
}
