package de.blockbuild.musikbot.configuration;

import java.io.File;
import java.io.InputStream;

public interface Configuration {
    String getName();
    String getVersion();
    String getFilePath();
    File getDataFolder();
    void reload();
    InputStream getResource(final String resource);
    void onDisable();
}
