package de.blockbuild.musikbot.core;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

import net.dv8tion.jda.core.entities.Guild;

public class GuildMusicManager {
	private final AudioPlayer player;
	private final TrackScheduler trackScheduler;
	// private final Guild guild;

	public GuildMusicManager(AudioPlayerManager playerManager, Guild guild) {
		this.player = playerManager.createPlayer();
		this.trackScheduler = new TrackScheduler(guild, player);
		// this.guild = guild;
		player.addListener(trackScheduler);
	}

	public AudioPlayerSendHandler getSendHandler() {
		return new AudioPlayerSendHandler(player);
	}

	public TrackScheduler getTrackScheduler() {
		return this.trackScheduler;
	}

	public AudioPlayer getAudioPlayer() {
		return this.player;
	}
}
