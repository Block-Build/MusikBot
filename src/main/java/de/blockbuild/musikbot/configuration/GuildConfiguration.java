package de.blockbuild.musikbot.configuration;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import de.blockbuild.musikbot.Bot;
import de.blockbuild.musikbot.core.GuildMusicManager;

import net.dv8tion.jda.api.entities.Role;

public class GuildConfiguration extends ConfigurationManager {
	private final GuildMusicManager musicManager;
	private String guildName;
	private int volume, messageDeleteDelay;
	private List<Long> blacklist, whitelist;
	private Boolean disconnectIfAlone, disconnectAfterLastTrack, useWhitelist;
	private Map<String, Object> autoConnect, defaultTextChannel, defaultVoiceChannel, roles, nowPlaying;
	private static String header;

	public GuildConfiguration(Bot bot, GuildMusicManager musicManager) {
		super(new File(bot.getMain().getDataFolder(), "/Guilds/" + musicManager.getGuild().getId() + ".yml"));
		this.musicManager = musicManager;
		this.guildName = musicManager.getGuild().getName();

		StringBuilder builder = new StringBuilder();
		builder.append("MusikBot by Block-Build\n");
		builder.append("+=====================+\n");
		builder.append("| GUILD CONFIGURATION |\n");
		builder.append("+=====================+\n");
		builder.append("\n");
		header = builder.toString();

		readConfig();
		writeConfig();
	}

	public boolean writeConfig() {
		try {
			YamlConfiguration config = new YamlConfiguration();

			config.set("Guild_Name", this.guildName);
			config.set("Volume", this.volume);
			config.set("Delete_Command_Massages_Delay", this.messageDeleteDelay);
			config.set("Whitelist_Enabled", this.useWhitelist);
			config.set("Whitelist", this.whitelist);
			config.set("Blacklist", this.blacklist);

			this.phraseMap(config.createSection("Command_Permission_Roles"), this.roles, "Music_Commands",
					"Radio_Commands", "Connection_Commands", "Setup_Commands");

			config.set("Auto_Disconnect_If_Alone", this.disconnectIfAlone);
			config.set("Auto_Disconnect_After_Last_Track", this.disconnectAfterLastTrack);

			this.phraseMap(config.createSection("Auto_Connect_On_Startup"), this.autoConnect, "Enabled",
					"VoiceChannelId", "Track");
			this.phraseMap(config.createSection("Message_Now_Playing_Track"), this.nowPlaying, "Enabled",
					"TextChannelId");
			this.phraseMap(config.createSection("Default_TextChannel"), this.defaultTextChannel, "Enabled",
					"TextChannelId");
			this.phraseMap(config.createSection("Default_VoiceChannel"), this.defaultVoiceChannel, "Enabled",
					"VoiceChannelId");

			return this.saveConfig(config, header);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean readConfig() {
		try {
			YamlConfiguration config = this.loadConfig();
			ConfigurationSection c;

			config.addDefault("Guild_Name", this.guildName);
			config.addDefault("Volume", 100);
			config.addDefault("Delete_Command_Massages_Delay", 0);
			config.addDefault("Whitelist_Enabled", "");
			config.addDefault("Whitelist", null);
			config.addDefault("Blacklist", null);

			c = this.addDefaultSection(config, "Command_Permission_Roles");
			c.addDefault("Music_Commands", "");
			c.addDefault("Radio_Commands", "");
			c.addDefault("Connection_Commands", "");
			c.addDefault("Setup_Commands", "");

			config.addDefault("Auto_Disconnect_If_Alone", false);
			config.addDefault("Auto_Disconnect_After_Last_Track", false);

			c = this.addDefaultSection(config, "Auto_Connect_On_Startup");
			c.addDefault("Enabled", false);
			c.addDefault("VoiceChannelId", 0L);
			c.addDefault("Track", "");

			c = this.addDefaultSection(config, "Message_Now_Playing_Track");
			c.addDefault("Enabled", false);
			c.addDefault("TextChannelId", 0L);

			c = this.addDefaultSection(config, "Default_TextChannel");
			c.addDefault("Enabled", false);
			c.addDefault("TextChannelId", 0L);

			c = this.addDefaultSection(config, "Default_VoiceChannel");
			c.addDefault("Enabled", false);
			c.addDefault("VoiceChannelId", 0L);

			this.volume = !(config.getInt("Volume") < 1) && !(config.getInt("Volume") > 100) ? config.getInt("Volume")
					: 100;
			this.messageDeleteDelay = config.getInt("Delete_Command_Massages_Delay");
			this.useWhitelist = config.getBoolean("Whitelist_Enabled");
			this.whitelist = config.getLongList("Whitelist");
			this.blacklist = config.getLongList("Blacklist");
			this.roles = config.getConfigurationSection("Command_Permission_Roles").getValues(false);
			this.disconnectIfAlone = config.getBoolean("Auto_Disconnect_If_Alone");
			this.disconnectAfterLastTrack = config.getBoolean("Auto_Disconnect_After_Last_Track");

			this.autoConnect = config.getConfigurationSection("Auto_Connect_On_Startup").getValues(false);
			this.nowPlaying = config.getConfigurationSection("Message_Now_Playing_Track").getValues(false);
			this.defaultTextChannel = config.getConfigurationSection("Default_TextChannel").getValues(false);
			this.defaultVoiceChannel = config.getConfigurationSection("Default_VoiceChannel").getValues(false);

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

		if (messageDeleteDelay < 0) {
			messageDeleteDelay = 0;
		}

		if (isAutoConnectEnabled() && getAutoConnectVoiceChannelId() == 0L)
			setAutoConnectEnabled(false);

		if (isNowPlayingTrackEnabled() && getNowPlayingTrackTextChannelId() == 0L)
			setNowPlayingTrackEnabled(false);

		if (isDefaultTextChannelEnabled() && getDefaultTextChannel() == 0L)
			setDefaultTextChannelEnabled(false);

		if (isDefaultVoiceChannelEnabled() && getDefaultVoiceChannel() == 0L)
			setDefaultVoiceChannelEnabled(false);
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

	public long getAutoConnectVoiceChannelId() {
		return Long.parseLong(autoConnect.get("VoiceChannelId").toString());
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

	public Boolean isNowPlayingTrackEnabled() {
		return (Boolean) nowPlaying.get("Enabled");
	}

	public void setNowPlayingTrackEnabled(Boolean bool) {
		nowPlaying.replace("Enabled", bool);
	}

	public long getNowPlayingTrackTextChannelId() {
		return Long.parseLong(nowPlaying.get("TextChannelId").toString());
	}

	public void setNowPlayingTrackTextChannelId(Long textChannelId) {
		nowPlaying.replace("TextChannelId", textChannelId);
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

	public boolean isDefaultTextChannelEnabled() {
		return (Boolean) defaultTextChannel.get("Enabled");
	}

	public void setDefaultTextChannelEnabled(boolean bool) {
		defaultTextChannel.replace("Enabled", bool);
	}

	public long getDefaultTextChannel() {
		return (long) defaultTextChannel.get("TextChannelId");
	}

	public void setDefaultTextChannel(long id) {
		defaultTextChannel.replace("TextChannelId", id);
	}

	public boolean isDefaultVoiceChannelEnabled() {
		return (Boolean) defaultVoiceChannel.get("Enabled");
	}

	public void setDefaultVoiceChannelEnabled(boolean bool) {
		defaultVoiceChannel.replace("Enabled", bool);
	}

	public long getDefaultVoiceChannel() {
		return (long) defaultVoiceChannel.get("VoiceChannelId");
	}

	public void setDefaultVoiceChannel(long id) {
		defaultVoiceChannel.replace("VoiceChannelId", id);
	}

	public Role getMusicRole() {
		return this.getRole((String) roles.get("Music_Commands"));
	}

	public Role getRadioRole() {
		return this.getRole((String) roles.get("Radio_Commands"));
	}

	public Role getConnectionRole() {
		return this.getRole((String) roles.get("Connection_Commands"));
	}

	public Role getSetupRole() {
		return this.getRole((String) roles.get("Setup_Commands"));
	}

	private Role getRole(String role) {
		if (role.isEmpty() || role.equals("@everyone")) {
			return null;
		}
		List<Role> r = musicManager.getGuild().getRolesByName(role, true);
		if (r.isEmpty()) {
			System.err.println("[MusikBot] '" + role + "' isn't a vaild role");
			return null;
		}
		return r.get(0);
	}

	public int getMessageDeleteDelay() {
		return messageDeleteDelay;
	}
}
