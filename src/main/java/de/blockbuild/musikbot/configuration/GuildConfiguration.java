package de.blockbuild.musikbot.configuration;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import de.blockbuild.musikbot.Bot;
import de.blockbuild.musikbot.core.GuildMusicManager;

import net.dv8tion.jda.core.entities.Role;

public class GuildConfiguration extends ConfigurationManager {
	private final GuildMusicManager musicManager;
	private String guildName;
	private int volume, messageDeleteDelay;
	private List<Long> blacklist, whitelist;
	private Boolean disconnectIfAlone, disconnectAfterLastTrack, useWhitelist;
	private Map<String, Object> autoConnect, defaultTextChannel, defaultVoiceChannel, roles;
	private static String header;

	public GuildConfiguration(Bot bot, GuildMusicManager musicManager) {
		super(new File(bot.getMain().getDataFolder(), "/Guilds/" + musicManager.getGuild().getId() + ".yml"));
		this.musicManager = musicManager;
		this.guildName = musicManager.getGuild().getName();

		StringBuilder builder = new StringBuilder();
		builder.append("# MusikBot by Block-Build\n");
		builder.append("# +=====================+\n");
		builder.append("# | GUILD CONFIGURATION |\n");
		builder.append("# +=====================+\n");
		builder.append("# \n");
		header = builder.toString();

		readConfig();
		writeConfig();
	}

	public boolean writeConfig() {
		Map<String, Object> config = new LinkedHashMap<String, Object>();

		config.put("Guild_Name", this.guildName);
		config.put("Volume", this.volume);
		config.put("Delete_Command_Massages_Delay", this.messageDeleteDelay);
		config.put("Whitelist_Enabled", this.useWhitelist);
		config.put("Whitelist", this.whitelist);
		config.put("Blacklist", this.blacklist);

		config.put("Command_Permission_Roles", this.roles);

		config.put("Auto_Disconnect_If_Alone", this.disconnectIfAlone);
		config.put("Auto_Disconnect_After_Last_Track", this.disconnectAfterLastTrack);

		config.put("Auto_Connect_On_Startup", this.autoConnect);
		config.put("Default_TextChannel", this.defaultTextChannel);
		config.put("Default_VoiceChannel", this.defaultVoiceChannel);

		return this.saveConfig(config, header);
	}

	@SuppressWarnings("unchecked")
	public boolean readConfig() {
		try {
			Map<String, Object> config = this.loadConfig1();

			config.putIfAbsent("Guild_Name", this.guildName);
			config.putIfAbsent("Volume", 100);
			config.putIfAbsent("Delete_Command_Massages_Delay", 0);
			config.putIfAbsent("Whitelist_Enabled", "");
			config.putIfAbsent("Whitelist", null);
			config.putIfAbsent("Blacklist", null);

			Map<String, Object> section;
			if (config.containsKey("Command_Permission_Roles")) {
				section = (Map<String, Object>) config.get("Command_Permission_Roles");
			} else {
				section = new LinkedHashMap<String, Object>();
			}
			section.putIfAbsent("Music_Commands", "");
			section.putIfAbsent("Radio_Commands", "");
			section.putIfAbsent("Connection_Commands", "");
			section.putIfAbsent("Setup_Commands", "");
			config.put("Command_Permission_Roles", section);
			this.roles = section;

			config.putIfAbsent("Auto_Disconnect_If_Alone", false);
			config.putIfAbsent("Auto_Disconnect_After_Last_Track", false);

			if (config.containsKey("Auto_Connect_On_Startup")) {
				section = (Map<String, Object>) config.get("Auto_Connect_On_Startup");
			} else {
				section = new LinkedHashMap<String, Object>();
			}
			section.putIfAbsent("Enabled", false);
			section.putIfAbsent("VoiceChannelId", 0L);
			section.putIfAbsent("Track", "");
			config.put("Auto_Connect_On_Startup", section);
			this.autoConnect = section;

			if (config.containsKey("Default_TextChannel")) {
				section = (Map<String, Object>) config.get("Default_TextChannel");
			} else {
				section = new LinkedHashMap<String, Object>();
			}
			section.putIfAbsent("Enabled", false);
			section.putIfAbsent("TextChannelId", 0L);
			config.put("Default_TextChannel", section);
			this.defaultTextChannel = section;

			if (config.containsKey("Default_VoiceChannel")) {
				section = (Map<String, Object>) config.get("Default_VoiceChannel");
			} else {
				section = new LinkedHashMap<String, Object>();
			}
			section.putIfAbsent("Enabled", false);
			section.putIfAbsent("VoiceChannelId", 0L);
			config.put("Default_VoiceChannel", section);
			this.defaultVoiceChannel = section;

			this.volume = !((int) config.get("Volume") < 1) && !((int) config.get("Volume") > 100)
					? (int) config.get("Volume")
					: 100;
			this.messageDeleteDelay = (int) config.get("Delete_Command_Massages_Delay");
			this.useWhitelist = (Boolean) config.get("Whitelist_Enabled");
			this.whitelist = (List<Long>) config.get("Whitelist");
			this.blacklist = (List<Long>) config.get("Blacklist");
			this.disconnectIfAlone = (Boolean) config.get("Auto_Disconnect_If_Alone");
			this.disconnectAfterLastTrack = (Boolean) config.get("Auto_Disconnect_After_Last_Track");

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
