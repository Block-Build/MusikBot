package de.blockbuild.musikbot.configuration;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import de.blockbuild.musikbot.Bot;

public class BotConfiguration extends ConfigurationManager {
	private String token, trigger, game, ownerID, inviteURL;
	private Map<String, Object> emojis;
	private static String header;

	public BotConfiguration(Bot bot) {
		super(new File(bot.getMain().getDataFolder(), "BotConfig.yml"));

		StringBuilder builder = new StringBuilder();
		builder.append("# MusikBot by Block-Build\n");
		builder.append("# +===================+\n");
		builder.append("# | BOT CONFIGURATION |\n");
		builder.append("# +===================+\n");
		builder.append("# \n");
		builder.append("# You have to insert the bot token and owner id.\n");
		builder.append(
				"# Instructions on: `https://www.spigotmc.org/resources/the-discord-musikbot-on-minecraft.64277/`\n");
		builder.append(
				"# Support/Suggestions/Bugs? Have a look on this site: `https://github.com/Block-Build/MusikBot`\n");
		builder.append("# \n");
		header = builder.toString();

		readConfig();
		writeConfig();
	}

	public boolean writeConfig() {
		Map<String, Object> config = new LinkedHashMap<String, Object>();

		config.put("Bot_Token", this.token);
		config.put("Owner_ID", this.ownerID);
		config.put("Command_Trigger", this.trigger);
		config.put("Game", this.game);
		config.put("Emojis", emojis);
		config.put("Invite_URL", this.inviteURL);

		return this.saveConfig(config, header);
	}

	@SuppressWarnings("unchecked")
	public boolean readConfig() {
		try {
			Map<String, Object> config = this.loadConfig();

			config.putIfAbsent("Bot_Token", "insert token here");
			config.putIfAbsent("Owner_ID", "12345");
			config.putIfAbsent("Command_Trigger", "!");
			config.putIfAbsent("Game", "Ready for playing music. !Play");

			Map<String, Object> section;
			if (config.containsKey("Emojis")) {
				section = (Map<String, Object>) config.get("Emojis");
			} else {
				section = new LinkedHashMap<String, Object>();
			}
			section.putIfAbsent("Success", "\uD83D\uDE03");
			section.putIfAbsent("Warning", "\uD83D\uDE2E");
			section.putIfAbsent("Error", "\uD83D\uDE26");
			config.put("Emojis", section);
			this.emojis = section;

			this.token = config.get("Bot_Token").toString();
			this.ownerID = config.get("Owner_ID").toString();
			this.trigger = config.get("Command_Trigger").toString();
			this.game = config.get("Game").toString();

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
