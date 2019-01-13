package de.blockbuild.musikbot.core;

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
			e.printStackTrace();
			return false;
		}
	}

	public synchronized YamlConfiguration loadConfig() {
		try {
			YamlConfiguration config = new YamlConfiguration();

			if (!file.exists()) {
				config.save(file);
			}
			config.load(file);
			return config;

		} catch (Exception e) {
			System.out.println("Couldn't load " + file.getName());
			e.printStackTrace();
		}
		return null;
	}

	public abstract boolean writeConfig();

	public abstract boolean readConfig();
}
