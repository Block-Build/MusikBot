package de.blockbuild.musikbot.configuration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import de.blockbuild.musikbot.Bot;

public class BotConfiguration extends ConfigurationManager {
	private String token, trigger, game, ownerID, inviteURL;
	private Map<String, String> emojis;

	// Avatar
	// Name

	public BotConfiguration(Bot bot) {
		super(new File(bot.getMain().getDataFolder(), "BotConfig.yml"));

		readConfig();
		writeConfig();
	}

	public boolean writeConfig() {
		YamlConfiguration config = new YamlConfiguration();

		config.set("Bot_Token", this.token);
		config.set("Owner_ID", this.ownerID);
		config.set("Command_Trigger", this.trigger);
		config.set("Game", this.game);
		config.createSection("Emojis", this.emojis);
		config.set("Invite_URL", this.inviteURL);

		return this.saveConfig(config, null);
	}

	public boolean readConfig() {
		try {
			YamlConfiguration config = this.loadConfig();

			this.token = config.getString("Bot_Token", "Insert Token here");
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
			return true;
		} catch (Exception e) {
			System.out.println("Couldn't read BotConfiguration!");
			e.printStackTrace();
			return false;
		}
	}

	public String getToken() {
		return this.token;
	}

	public String getGame() {
		return this.game;
	}

	public String getTrigger() {
		return this.trigger;
	}

	public String getOwnerID() {
		return this.ownerID;
	}

	public String getSuccess() {
		return this.emojis.get("Success");
	}

	public String getWarning() {
		return this.emojis.get("Warning");
	}

	public String getError() {
		return this.emojis.get("Error");
	}

	public void setInviteLink(String inviteURL) {
		this.inviteURL = inviteURL;
		writeConfig();
	}
}
