package de.blockbuild.musikbot.core;

import java.io.File;
import org.bukkit.configuration.file.YamlConfiguration;

import de.blockbuild.musikbot.Main;

public class BotConfiguration {
	private final File file;
	public String token, trigger, game, ownerID;
	
	// Avatar
	// Name

	public BotConfiguration(Main main) {
		this.file = new File(main.getDataFolder(), "BotConfig.yml");

		loadConfig();
		writeConfig();
	}

	public void writeConfig() {
		try {
			YamlConfiguration config = new YamlConfiguration();

			config.set("Bot_Token", this.token);
			config.set("Owner_ID", this.ownerID);
			config.set("Command_Trigger", this.trigger);
			config.set("Game", this.game);

			config.save(file);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void loadConfig() {
		try {
			YamlConfiguration config = new YamlConfiguration();

			if (!file.exists()) {
				config.save(file);
			}
			config.load(file);

			this.token = config.getString("Bot_Token", "insert token here");
			this.ownerID = config.getString("Owner_ID", "12345");
			this.trigger = config.getString("Command_Trigger", "!");
			this.game = config.getString("Game", "Ready for playing music. !Play");
		} catch (Exception e) {
			System.out.println("Couldn't load BotConfig!");
			e.printStackTrace();
		}
	}
}
