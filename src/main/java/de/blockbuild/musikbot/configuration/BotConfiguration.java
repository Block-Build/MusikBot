package de.blockbuild.musikbot.configuration;

import java.io.File;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import de.blockbuild.musikbot.Bot;

public class BotConfiguration extends ConfigurationManager {
	private String token, trigger, game, ownerID, inviteURL;
	private Map<String, Object> emojis;
	private static String header;

	public BotConfiguration(Bot bot) {
		super(new File(bot.getMain().getDataFolder(), "BotConfig.yml"));

		StringBuilder builder = new StringBuilder();
		builder.append("MusikBot by Block-Build\n");
		builder.append("+===================+\n");
		builder.append("| BOT CONFIGURATION |\n");
		builder.append("+===================+\n");
		builder.append("\n");
		builder.append("You have to insert the bot token and owner id.\n");
		builder.append(
				"Instructions on: `https://www.spigotmc.org/resources/the-discord-musikbot-on-minecraft.64277/`\n");
		builder.append(
				"Support/Suggestions/Bugs? Have a look on this site: `https://github.com/Block-Build/MusikBot`\n");
		builder.append("\n");
		header = builder.toString();

		readConfig();
		writeConfig();
	}

	public boolean writeConfig() {
		YamlConfiguration config = new YamlConfiguration();

		config.set("Bot_Token", this.token);
		config.set("Owner_ID", this.ownerID);
		config.set("Command_Trigger", this.trigger);
		config.set("Game", this.game);
		this.phraseMap(config.createSection("Emojis"), this.emojis, "Success", "Warning", "Error");
		config.set("Invite_URL", this.inviteURL);

		return this.saveConfig(config, header);
	}

	public boolean readConfig() {
		try {
			YamlConfiguration config = this.loadConfig();
			ConfigurationSection c;

			config.addDefault("Bot_Token", "insert token here");
			config.addDefault("Owner_ID", "12345");
			config.addDefault("Command_Trigger", "!");
			config.addDefault("Game", "Ready for playing music. !Play");

			c = this.addDefaultSection(config, "Emojis");
			c.addDefault("Success", "\uD83D\uDE03");
			c.addDefault("Warning", "\uD83D\uDE2E");
			c.addDefault("Error", "\uD83D\uDE26");

			this.token = config.getString("Bot_Token");
			this.ownerID = config.getString("Owner_ID");
			this.trigger = config.getString("Command_Trigger");
			this.game = config.getString("Game");

			this.emojis = config.getConfigurationSection("Emojis").getValues(false);
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
		return (String) this.emojis.get("Success");
	}

	public String getWarning() {
		return (String) this.emojis.get("Warning");
	}

	public String getError() {
		return (String) this.emojis.get("Error");
	}

	public void setInviteLink(String inviteURL) {
		this.inviteURL = "`" + inviteURL + "`";
		writeConfig();
	}
}
