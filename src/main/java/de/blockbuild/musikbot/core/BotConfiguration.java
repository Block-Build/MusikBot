package de.blockbuild.musikbot.core;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import de.blockbuild.musikbot.Bot;

public class BotConfiguration {
	private final File file;
	public String token, trigger, game, ownerID;
	public Map<String, String> emojis;

	// Avatar
	// Name

	public BotConfiguration(Bot bot) {
		this.file = new File(bot.getMain().getDataFolder(), "BotConfig.yml");

		loadConfig();
		writeConfig();
	}

	public synchronized void writeConfig() {
		try {
			YamlConfiguration config = new YamlConfiguration();

			config.set("Bot_Token", this.token);
			config.set("Owner_ID", this.ownerID);
			config.set("Command_Trigger", this.trigger);
			config.set("Game", this.game);
			config.createSection("Emojis", this.emojis);

			config.save(file);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public synchronized void loadConfig() {
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

			this.emojis = new HashMap<>();
			if (!config.contains("Emojis")) {
				config.createSection("Emojis", this.emojis);
			}
			ConfigurationSection emojiList = config.getConfigurationSection("Emojis");
			this.emojis.put("Success", emojiList.get("Success", "\uD83D\uDE03").toString());
			this.emojis.put("Warning", emojiList.get("Warning", "\uD83D\uDE2E").toString());
			this.emojis.put("Error", emojiList.get("Error", "\uD83D\uDE26").toString());

		} catch (Exception e) {
			System.out.println("Couldn't load BotConfig!");
			e.printStackTrace();
		}
	}
}
