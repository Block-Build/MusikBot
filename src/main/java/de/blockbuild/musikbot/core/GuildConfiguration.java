package de.blockbuild.musikbot.core;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import de.blockbuild.musikbot.Bot;

import net.dv8tion.jda.core.entities.Guild;

public class GuildConfiguration extends ConfigurationManager {
	private final Guild guild;
	private final GuildMusicManager musicManager;
	private String guildName;
	private int volume;
	private List<Long> blacklist, whitelist;
	private Boolean disconnectIfAlone, disconnectAfterLastTrack, useWhitelist;
	private Map<String, Object> autoConnect;

	public GuildConfiguration(Bot bot, GuildMusicManager musicManager) {
		super(new File(bot.getMain().getDataFolder(), "/Guilds/" + musicManager.getGuild().getId() + ".yml"));
		this.musicManager = musicManager;
		this.guild = musicManager.getGuild();

		readConfig();
		writeConfig();
	}

	public boolean writeConfig() {
		try {
			YamlConfiguration config = new YamlConfiguration();

			config.set("Guild_Name", this.guildName);
			config.set("Volume", this.volume);
			config.set("Whitelist_Enabled", this.useWhitelist);
			config.set("Whitelist", this.whitelist);
			config.set("Blacklist", this.blacklist);
			config.set("Auto_Disconnect_If_Alone", this.disconnectIfAlone);
			config.set("Auto_Disconnect_After_Last_Track", this.disconnectAfterLastTrack);
			config.set("Auto_Connect_On_Startup", this.autoConnect);
			// config.set("", );

			return this.saveConfig(config);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean readConfig() {
		try {
			YamlConfiguration config = this.loadConfig();

			this.guildName = guild.getName();
			this.volume = !(config.getInt("Volume") < 1) && !(config.getInt("Volume") > 100) ? config.getInt("Volume")
					: 100;
			this.useWhitelist = config.getBoolean("Whitelist_Enabled", false);
			this.whitelist = config.getLongList("Whitelist");
			this.blacklist = config.getLongList("Blacklist");
			this.disconnectIfAlone = config.getBoolean("Auto_Disconnect_If_Alone", false);
			this.disconnectAfterLastTrack = config.getBoolean("Auto_Disconnect_After_Last_Track", false);

			this.autoConnect = new HashMap<>();
			if (!config.contains("Auto_Connect_On_Startup")) {
				config.createSection("Auto_Connect_On_Startup", this.autoConnect);
			}
			ConfigurationSection autoConnectList = config.getConfigurationSection("Auto_Connect_On_Startup");
			autoConnect.put("Enabled", autoConnectList.getBoolean("Enabled", false));
			autoConnect.put("VoiceChannelId", autoConnectList.getLong("VoiceChannelId"));
			autoConnect.put("Track", autoConnectList.getString("Track", ""));

			// Playlist
			// default text channel
			// default voice channel

			initConfig();
			return true;
		} catch (Exception e) {
			System.out.println("Couldn't read GuildConfiguration!");
			e.printStackTrace();
			return false;
		}
	}

	private void initConfig() {
		musicManager.getAudioPlayer().setVolume(this.volume);
	}

	public Boolean isDisconnectIfAloneEnabled() {
		return disconnectIfAlone;
	}

	public Boolean isDisconnectAfterLastTrackEnabled() {
		return disconnectAfterLastTrack;
	}

	public void setDisconnectIfAlone(Boolean bool) {
		disconnectIfAlone = bool;
	}

	public void setDisconnectAfterLastTrack(Boolean bool) {
		disconnectAfterLastTrack = bool;
	}

	public Boolean isWhitelistEnabled() {
		return useWhitelist;
	}

	public void setWhitelistEnabled(Boolean bool) {
		useWhitelist = bool;
	}

	public Boolean isAutoConnectEnabled() {
		return (Boolean) autoConnect.get("Enabled");
	}

	public Long getAutoConnectVoiceChannelId() {
		return (Long) autoConnect.get("VoiceChannelId");
	}

	public String getAutoConnectTrack() {
		return (String) autoConnect.get("Track");
	}

	public void setAutoConnectEnabled(Boolean bool) {
		autoConnect.replace("Enabled", bool);
	}

	public void setAutoConnectVoiceChannelId(Long voiceChannelId) {
		autoConnect.replace("VoiceChannelId", voiceChannelId);
	}

	public void setAutoConnectTrack(String track) {
		autoConnect.replace("Track", track);
	}

	public int getVolume() {
		return this.volume;
	}

	public void setVolume(int volume) {
		this.volume = volume;
	}

	public boolean isBlockedUser(long ID) {
		return this.blacklist.contains(ID);
	}

	public void blacklistAdd(Long id) {
		blacklist.add(id);
	}

	public void blacklistRemove(Long id) {
		blacklist.remove(id);
	}

	public void blacklistClear() {
		blacklist.clear();
	}

	public List<Long> getBlacklist() {
		return blacklist;
	}

	public boolean isWhitelistedUser(long ID) {
		return this.whitelist.contains(ID);
	}

	public void whitelistAdd(Long id) {
		whitelist.add(id);
	}

	public void whitelistRemove(Long id) {
		whitelist.remove(id);
	}

	public void whitelistClear() {
		whitelist.clear();
	}

	public List<Long> getWhitelist() {
		return whitelist;
	}
}
