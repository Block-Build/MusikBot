package de.blockbuild.musikbot.core;

import java.io.File;
import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;

import de.blockbuild.musikbot.Main;

import net.dv8tion.jda.core.entities.Guild;

public class GuildConfiguration {
	private final File file;
	private final Guild guild;
	private final GuildMusicManager musicManager;
	public String guildName;
	public int volume;
	public List<Long> blacklist, whitelist;
	public Boolean disconnectIfAlone, disconnectAfterLastTrack, useWhitelist;

	public GuildConfiguration(Main main, GuildMusicManager musicManager) {
		this.musicManager = musicManager;
		this.guild = musicManager.getGuild();
		this.file = new File(main.getDataFolder(), "/Guilds/" + guild.getId() + ".yml");

		loadConfig();
		writeConfig();
	}

	public synchronized Boolean writeConfig() {
		try {
			YamlConfiguration config = new YamlConfiguration();

			config.set("Guild_Name", this.guildName);
			config.set("Volume", this.volume);
			config.set("Whitelist_Enabled", this.useWhitelist);
			config.set("Whitelist", this.whitelist);
			config.set("Blacklist", this.blacklist);
			// config.set("", );

			config.save(file);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
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
			this.useWhitelist = config.getBoolean("Whitelist_Enabled", false);
			this.whitelist = config.getLongList("Whitelist");
			this.blacklist = config.getLongList("Blacklist");
			// Playlist
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
		initConfig();
	}

	private void initConfig() {
		musicManager.getAudioPlayer().setVolume(this.volume);
	}
}