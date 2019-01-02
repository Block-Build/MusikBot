package de.blockbuild.musikbot.core;

import java.util.List;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import de.blockbuild.musikbot.Bot;

import net.dv8tion.jda.core.entities.Guild;

public class GuildMusicManager {
	private final AudioPlayer player;
	private final TrackScheduler trackScheduler;
	public final GuildConfiguration config;
	public final Bot bot;
	private final Guild guild;
	public List<AudioTrack> tracks;
	public Boolean isQueue;

	public GuildMusicManager(AudioPlayerManager playerManager, Guild guild, Bot bot) {
		this.bot = bot;
		this.guild = guild;
		this.player = playerManager.createPlayer();
		this.trackScheduler = new TrackScheduler(guild, this);
		player.addListener(trackScheduler);
		this.config = new GuildConfiguration(bot, this);
	}

	public Guild getGuild() {
		return this.guild;
	}

	public AudioPlayerSendHandler getSendHandler() {
		return new AudioPlayerSendHandler(player, bot);
	}

	public TrackScheduler getTrackScheduler() {
		return this.trackScheduler;
	}

	public AudioPlayer getAudioPlayer() {
		return this.player;
	}

	public boolean isBlockedUser(long ID) {
		return config.blacklist.contains(ID);
	}

	public void setVolume(int volume) {
		player.setVolume(volume);
		config.volume = volume;
	}

	public int getVolume() {
		return player.getVolume();
	}

	public Boolean isWhitelistEnabled() {
		return config.useWhitelist;
	}

	public void setWhitelistEnabled(Boolean bool) {
		config.useWhitelist = bool;
	}

	public boolean isWhitelistedUser(long ID) {
		return config.whitelist.contains(ID);
	}

}
