package dev.elektronisch.dieter.daemon.common;

import com.google.common.base.Preconditions;
import dev.elektronisch.dieter.client.DaemonDieterClient;
import dev.elektronisch.dieter.daemon.common.configuration.ConfigurationUtil;
import dev.elektronisch.dieter.daemon.common.configuration.DaemonConfiguration;
import lombok.extern.slf4j.Slf4j;

import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
public abstract class AbstractDieterDaemonApplication {

    private File configurationFile;
    private DaemonConfiguration configuration;
    private DaemonDieterClient client;

    void internalEnable() {
        log.info("Starting client... (Key: {})", configuration.getDeviceKey());
        client = new DaemonDieterClient(configuration.getDeviceKey());

        // TODO send heartbeats, handle tasks

        log.info("Enabling operating-system implementation...");
        enable();
    }

    void internalDisable() {
        log.info("Disabling operating-system implementation...");
        disable();
    }

    void internalInstall(final UUID deviceKey) {
        Preconditions.checkArgument(!configurationFile.exists(), "configuration must not exist");

        log.info("Creating configuration...");
        final Path configurationFilePath = configurationFile.toPath();
        try {
            Files.createFile(configurationFilePath);
        } catch (final IOException e) {
            log.error("An error occurred while creating configuration", e);
            System.exit(1);
        }

        final DaemonConfiguration configuration = new DaemonConfiguration(getClass().getPackage().getImplementationVersion(), deviceKey);
        ConfigurationUtil.saveConfiguration(configurationFile, configuration);

        log.info("Launching operating-system specific installation...");
        install(deviceKey);
    }

    void internalUninstall() {
        Preconditions.checkArgument(configurationFile.exists(), "configuration must exist");

        log.info("Deleting configuration...");
        final Path configurationFilePath = configurationFile.toPath();
        try {
            Files.delete(configurationFilePath);
        } catch (final IOException e) {
            log.error("An error occurred while deleting configuration", e);
            System.exit(1);
        }

        log.info("Launching operating-system specific uninstallation...");
        uninstall();
    }

    void init() {
        final File workingDirectory = determineWorkingDirectory();
        final File applicationDirectory = getApplicationDirectory();

        configurationFile = new File(applicationDirectory, "config.json");
        if (configurationFile.exists()) {
            configuration = ConfigurationUtil.readConfiguration(configurationFile);
        }

        if (workingDirectory.equals(applicationDirectory)) {
            if (!isCurrentUserDaemonUser()) {
                System.exit(1);
                return;
            }

            // TODO implement updater
            internalEnable();
        } else {
            runInstaller();
        }
    }

    void runInstaller() {
        log.info(" ______     _____   ________   _________   ________   _______");
        log.info("|_   _ `.  |_   _| |_   __  | |  _   _  | |_   __  | |_   __ \\");
        log.info("  | | `. \\   | |     | |_ \\_| |_/ | | \\_|   | |_ \\_|   | |__) |");
        log.info("  | |  | |   | |     |  _| _      | |       |  _| _    |  __ /");
        log.info(" _| |_.' /  _| |_   _| |__/ |    _| |_     _| |__/ |  _| |  \\ \\_");
        log.info("|______.'  |_____| |________|   |_____|   |________| |____| |___|");
        log.info("    :: DIETER {} © 2022 All rights reserved ::\n", getClass().getPackage().getImplementationVersion());
        if (configuration != null) {
            log.info("Installed version: " + configuration.getApplicationVersion());
            log.info("Device key: " + configuration.getDeviceKey() + "\n");
        } else {
            log.info("Not installed on this machine!\n");
        }

        final Console console = System.console();
        while (!Thread.interrupted()) {
            log.info("Please specify option! (install/uninstall/configure):");
            final String option = console.readLine();
            switch (option) {
                case "install" -> {
                    if (configuration != null) {
                        log.error("Already installed. Please uninstall first!");
                        continue;
                    }

                    log.info("Please enter device key:");
                    final UUID deviceKey = readUUIDFromConsole();
                    if (deviceKey == null) {
                        log.error("Invalid device key!");
                        continue;
                    }

                    internalInstall(deviceKey);
                    System.exit(0);
                }
                case "uninstall" -> {
                    if (configuration == null) {
                        log.error("Not yet installed!");
                        continue;
                    }

                    internalUninstall();
                    System.exit(0);
                }
                case "configure" -> {
                    if (configuration == null) {
                        log.error("Not yet installed. Please install first!");
                        continue;
                    }

                    log.info("Please enter new device key:");
                    final UUID deviceKey = readUUIDFromConsole();
                    if (deviceKey == null) {
                        log.error("Invalid device key!");
                        continue;
                    }

                    configuration.setDeviceKey(deviceKey);
                    if (ConfigurationUtil.saveConfiguration(configurationFile, configuration)) {
                        log.info("Key was set successfully! Do you want to restart now? (y/n)");
                        final String restart = console.readLine();
                        if (restart.equalsIgnoreCase("y")) {
                            restartSystem();
                        }
                    }
                }
                default -> log.error("Invalid option!");
            }
        }
    }

    public abstract void enable();

    public abstract void disable();

    public abstract void install(final UUID deviceKey);

    public abstract void uninstall();

    public abstract void shutdownSystem();

    public abstract void restartSystem();

    public abstract File getApplicationDirectory();

    public abstract boolean isCurrentUserDaemonUser();

    private File determineWorkingDirectory() {
        final Path currentRelativePath = Paths.get("");
        return currentRelativePath.toAbsolutePath().toFile();
    }

    private UUID readUUIDFromConsole() {
        final String rawDeviceKey = System.console().readLine();
        try {
            return UUID.fromString(rawDeviceKey);
        } catch (final IllegalArgumentException ignored) {
            return null;
        }
    }
}
