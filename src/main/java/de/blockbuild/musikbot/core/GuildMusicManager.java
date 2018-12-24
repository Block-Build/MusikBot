package de.blockbuild.musikbot.core;

import java.util.List;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import net.dv8tion.jda.core.entities.Guild;

public class GuildMusicManager {
	private final AudioPlayer player;
	private final TrackScheduler trackScheduler;
	// private final Guild guild;
	public List<AudioTrack> tracks;
	public Boolean isQueue;

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
