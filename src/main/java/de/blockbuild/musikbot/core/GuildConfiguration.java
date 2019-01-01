package de.blockbuild.musikbot.core;

import java.io.File;
import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;

import de.blockbuild.musikbot.Main;

import net.dv8tion.jda.core.entities.Guild;

public class GuildConfiguration {
	private final File file;
	private final Guild guild;
	public String guildName;
	public int volume;
	public List<Long> blacklist, whitelist;
	public Boolean disconnectIfAlone, disconnectAfterLastTrack, useWhitelist;

	public GuildConfiguration(Main main, Guild guild) {
		this.guild = guild;
		this.file = new File(main.getDataFolder(), "/Guilds/" + guild.getId() + ".yml");

		loadConfig();
		writeConfig();
	}

	public synchronized void writeConfig() {
		try {
			YamlConfiguration config = new YamlConfiguration();

			config.set("Guild_Name", this.guildName);
			config.set("Volume", this.volume);
			config.set("Use_Whitelist", this.useWhitelist);
			config.set("Whitelisted_Users", this.whitelist);
			config.set("Blocked_Users", this.blacklist);
			// config.set("", );

			config.save(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public synchronized void loadConfig() {
		try {
			YamlConfiguration config = new YamlConfiguration();

			if (!file.exists()) {
				config.save(file);
			}
			config.load(file);

			this.guildName = guild.getName();
			this.volume = !(config.getInt("Volume") < 1) && !(config.getInt("Volume") > 100) ? config.getInt("Volume")
					: 100;
			this.useWhitelist = config.getBoolean("Use_Whitelist", false);
			this.whitelist = config.getLongList("Whitelisted_Users");
			this.blacklist = config.getLongList("Blocked_Users");
			// Playlist
			// Blocked User
			// auto disconnect
			// disconnect after last track
			// default text channel
			// default voice channel
			// auto connect
			// auto play

		} catch (Exception e) {
			System.out.println("Couldn't load GuildConfig!");
			e.printStackTrace();
		}
	}
}
