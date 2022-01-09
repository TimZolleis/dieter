package dev.elektronisch.dieter.daemon.common.config;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

@Slf4j
public final class ConfigurationUtil {

    private static final Gson GSON = new Gson();

    private ConfigurationUtil() {

    }

    public static DaemonConfig readConfiguration(File file) {
        try (final JsonReader reader = new JsonReader(new FileReader(file))) {
            return GSON.fromJson(reader, DaemonConfig.class);
        } catch (final IOException e) {
            log.error("An error occurred while reading configuration", e);
        }
        return null;
    }

    public static boolean saveConfiguration(File file, DaemonConfig config) {
        try (final JsonWriter writer = new JsonWriter(new FileWriter(file))) {
            GSON.toJson(config, DaemonConfig.class, writer);
            return true;
        } catch (final IOException e) {
            log.error("An error occurred while writing configuration", e);
        }
        return false;
    }
}