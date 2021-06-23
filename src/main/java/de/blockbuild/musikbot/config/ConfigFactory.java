package de.blockbuild.musikbot.config;

public final class ConfigFactory {

    private Configuration configuration;

    private ConfigFactory() {

    }

    private static class ConfigSingleton {
        private static final ConfigFactory INSTANCE = new ConfigFactory();
    }

    public static ConfigFactory getInstance() {
        return ConfigSingleton.INSTANCE;
    }

    public Configuration getConfig() {
        return configuration;
    }

    public void register(final Configuration config) {
        this.configuration = config;
    }

}