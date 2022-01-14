package dev.elektronisch.dieter.daemon.common.installer;

import com.google.common.base.Preconditions;
import dev.elektronisch.dieter.daemon.common.AbstractDieterDaemonApplication;
import dev.elektronisch.dieter.daemon.common.configuration.ConfigurationUtil;
import dev.elektronisch.dieter.daemon.common.configuration.DaemonConfiguration;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Getter
public abstract class AbstractApplicationInstaller {

    private final AbstractDieterDaemonApplication application;

    protected AbstractApplicationInstaller(final AbstractDieterDaemonApplication application) {
        this.application = application;
    }

    protected abstract void install(final UUID deviceKey) throws Exception;

    protected abstract void uninstall() throws Exception;

    public void run() {
        log.info(" ______     _____   ________   _________   ________   _______");
        log.info("|_   _ `.  |_   _| |_   __  | |  _   _  | |_   __  | |_   __ \\");
        log.info("  | | `. \\   | |     | |_ \\_| |_/ | | \\_|   | |_ \\_|   | |__) |");
        log.info("  | |  | |   | |     |  _| _      | |       |  _| _    |  __ /");
        log.info(" _| |_.' /  _| |_   _| |__/ |    _| |_     _| |__/ |  _| |  \\ \\_");
        log.info("|______.'  |_____| |________|   |_____|   |________| |____| |___|");
        log.info("    :: DIETER DAEMON {} ::\n", getClass().getPackage().getImplementationVersion());
        if (application.getConfiguration() != null) {
            log.info("Installed version: " + application.getConfiguration().getApplicationVersion());
            log.info("Device key: " + application.getConfiguration().getDeviceKey() + "\n");
        } else {
            log.info("Not installed on this machine!\n");
        }

        final Console console = System.console();
        while (!Thread.interrupted()) {
            log.info("Please specify option! (install/uninstall/configure):");
            final String option = console.readLine();
            switch (option) {
                case "install" -> {
                    if (application.getConfiguration() != null) {
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
                    askForRestartOrQuit();
                }
                case "uninstall" -> {
                    if (application.getConfiguration() == null) {
                        log.error("Not yet installed!");
                        continue;
                    }

                    internalUninstall();
                    askForRestartOrQuit();
                }
                case "configure" -> {
                    if (application.getConfiguration() == null) {
                        log.error("Not yet installed. Please install first!");
                        continue;
                    }

                    log.info("Please enter new device key:");
                    final UUID deviceKey = readUUIDFromConsole();
                    if (deviceKey == null) {
                        log.error("Invalid device key!");
                        continue;
                    }

                    application.getConfiguration().setDeviceKey(deviceKey);
                    if (ConfigurationUtil.saveConfiguration(application.getConfigurationFile(), application.getConfiguration())) {
                        log.info("Key was set successfully!");
                        askForRestartOrQuit();
                    }
                }
                default -> log.error("Invalid option!");
            }
        }
    }

    protected void runCommands(final String... commands) throws Exception {
        for (int i = 0; i < commands.length; i++) {
            final String command = commands[i];
            final Process process = Runtime.getRuntime().exec(command);
            final InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream());
            final BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bufferedReader.readLine()) != null)
                log.info(line);

            process.waitFor(1, TimeUnit.MINUTES);
            if (process.exitValue() != 0)
                throw new IllegalStateException("Exit value of '" + i + "' was: " + process.exitValue());
        }
    }

    protected void extractFile(final String resourceName, final Path targetPath) throws IOException {
        final InputStream stream = getClass().getResourceAsStream("/" + resourceName);
        if (stream == null) {
            throw new IllegalArgumentException("Invalid resource: " + resourceName);
        }

        Files.copy(stream, targetPath, StandardCopyOption.REPLACE_EXISTING);
    }

    private void internalInstall(final UUID deviceKey) {
        Preconditions.checkArgument(!application.getConfigurationFile().exists(), "configuration must not exist");
        final long startMillis = System.currentTimeMillis();

        log.info("Creating application directory...");
        try {
            Files.createDirectories(application.getApplicationDirectory().toPath());
        } catch (final IOException e) {
            log.error("An error occurred while creating directories", e);
            return;
        }

        log.info("Launching operating-system specific installation...");
        try {
            install(deviceKey);
        } catch (final Exception e) {
            log.error("An error occurred while launching operating-system specific installation", e);
            return;
        }

        log.info("Creating configuration...");
        final Path configurationFilePath = application.getConfigurationFile().toPath();
        try {
            Files.createFile(configurationFilePath);
        } catch (final IOException e) {
            log.error("An error occurred while creating configuration", e);
            return;
        }

        final DaemonConfiguration configuration = new DaemonConfiguration(getClass().getPackage().getImplementationVersion(), deviceKey);
        ConfigurationUtil.saveConfiguration(application.getConfigurationFile(), configuration);

        log.info("Finished! Installation took: {}ms\n", System.currentTimeMillis() - startMillis);
    }

    private void internalUninstall() {
        Preconditions.checkArgument(application.getConfigurationFile().exists(), "configuration must exist");
        final long startMillis = System.currentTimeMillis();

        log.info("Launching operating-system specific uninstallation...");
        try {
            uninstall();
        } catch (final Exception e) {
            log.error("An error occurred while launching operating-system specific uninstallation", e);
            return;
        }

        log.info("Deleting files...");
        try {
            FileUtils.forceDeleteOnExit(application.getApplicationDirectory());
        } catch (final IOException e) {
            log.error("An error occurred while deleting files", e);
            return;
        }

        log.info("Finished! Uninstallation took: {}ms\n", System.currentTimeMillis() - startMillis);
    }

    private UUID readUUIDFromConsole() {
        final String line = System.console().readLine();
        try {
            return UUID.fromString(line);
        } catch (final IllegalArgumentException ignored) {
            return null;
        }
    }

    private void askForRestartOrQuit() {
        log.info("Do you want to restart this machine? (Y/N)");
        final String line = System.console().readLine();
        if (line.equalsIgnoreCase("y")) {
            application.restartMachine();
        } else {
            System.exit(0);
        }
    }
}
