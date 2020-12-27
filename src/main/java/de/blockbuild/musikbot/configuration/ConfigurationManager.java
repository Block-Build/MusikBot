package de.blockbuild.musikbot.configuration;

import java.io.File;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public abstract class ConfigurationManager {
	private final File file;

	public ConfigurationManager(File file) {
		this.file = file;
	}

	public synchronized boolean saveConfig(YamlConfiguration config, String header) {
		try {
			config.options().header(header);
			config.options().copyDefaults(true);
			config.save(this.file);
			return true;
		} catch (Exception e) {
			System.out.println("Couldn't save " + file.getName());
			e.printStackTrace();
			return false;
		}
	}

	public synchronized YamlConfiguration loadConfig() {
		try {
			YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
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

	public String getRawConfiguration() {
		return loadConfig().saveToString();
	}

	protected ConfigurationSection phraseMap(ConfigurationSection section, Map<String, Object> map, String... keys) {

		for (String s : keys) {
			section.addDefault(s, map.get(s));
		}
		return section;
	}

	protected ConfigurationSection addDefaultSection(YamlConfiguration config, String section) {
		if (config.contains(section)) {
			return config.getConfigurationSection(section);
		} else {
			return config.createSection(section);
		}
	}
}