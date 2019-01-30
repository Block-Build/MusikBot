package de.blockbuild.musikbot.configuration;

import java.io.File;
import org.bukkit.configuration.file.YamlConfiguration;

public abstract class ConfigurationManager {
	private final File file;

	public ConfigurationManager(File file) {
		this.file = file;
	}

	public synchronized boolean saveConfig(YamlConfiguration config) {
		try {
			config.save(this.file);
			return true;
		} catch (Exception e) {
			System.out.println("Couldn't save " + file.getName());
			e.printStackTrace();
			return false;
		}
	}

	public synchronized YamlConfiguration loadConfig(String header) {
		try {
			YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
			config.options().header(header);
			config.options().copyDefaults(true);
			return config;

		} catch (Exception e) {
			System.out.println("Couldn't load " + file.getName());
			e.printStackTrace();
		}
		return null;
	}

	public synchronized boolean deleteConfig() {
		return file.delete();
	}

	public abstract boolean writeConfig();

	public abstract boolean readConfig();
}