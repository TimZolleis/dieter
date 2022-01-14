package dev.elektronisch.dieter.daemon.installer;

import dev.elektronisch.dieter.daemon.DieterDaemonApplication;
import dev.elektronisch.dieter.daemon.common.installer.AbstractApplicationInstaller;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Slf4j
public final class ApplicationInstaller extends AbstractApplicationInstaller {

    private static final String APPLICATION_NAME = "DieterDaemon.exe";
    private static final String SERVICE_HELPER_NAME = "winsw.exe";
    private static final String SERVICE_HELPER_CONFIGURATION_NAME = "service.xml";
    private static final String START_MENU_FOLDER = "C:\\ProgramData\\Microsoft\\Windows\\Start Menu\\Programs";
    private static final String SYMLINK_NAME = "DieterDaemon.lnk";

    public ApplicationInstaller(final DieterDaemonApplication application) {
        super(application);
    }

    @Override
    public void install(final UUID deviceKey) throws Exception {
        final File applicationDirectory = getApplication().getApplicationDirectory();

        // Copying application
        final Path sourceFilePath = Path.of(getClass()
                .getProtectionDomain()
                .getCodeSource()
                .getLocation().toURI());
        final Path targetFilePath = Path.of(applicationDirectory.getAbsolutePath(), APPLICATION_NAME);
        Files.copy(sourceFilePath, targetFilePath, StandardCopyOption.REPLACE_EXISTING);

        // Extracting files
        final Path helperExecutablePath = Path.of(applicationDirectory.getAbsolutePath(), SERVICE_HELPER_NAME);
        extractFile(SERVICE_HELPER_NAME, helperExecutablePath);

        final Path helperConfigurationPath = Path.of(applicationDirectory.getAbsolutePath(), SERVICE_HELPER_CONFIGURATION_NAME);
        extractFile(SERVICE_HELPER_CONFIGURATION_NAME, helperConfigurationPath);

        final Path linkFilePath = Path.of(START_MENU_FOLDER, SYMLINK_NAME);
        extractFile(SYMLINK_NAME, linkFilePath);

        runCommands("\"" + helperExecutablePath + "\" install --no-elevate \"" + helperConfigurationPath + "\"");
    }

    @Override
    public void uninstall() throws Exception {
        final File applicationDirectory = getApplication().getApplicationDirectory();

        // Uninstall service
        final Path helperExecutablePath = Path.of(applicationDirectory.getAbsolutePath(), SERVICE_HELPER_NAME);
        final Path helperConfigurationPath = Path.of(applicationDirectory.getAbsolutePath(), SERVICE_HELPER_CONFIGURATION_NAME);
        runCommands("\"" + helperExecutablePath + "\" uninstall --no-elevate \"" + helperConfigurationPath + "\"");

        // Delete symlink
        Files.delete(Path.of(START_MENU_FOLDER, SYMLINK_NAME));
    }
}
