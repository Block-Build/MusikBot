package de.blockbuild.musikbot.core;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public class BasicResultHandler implements AudioLoadResultHandler {

	private TrackScheduler trackScheduler;

	public BasicResultHandler(GuildMusicManager musicManager) {
		this.trackScheduler = musicManager.getTrackScheduler();
	}

	@Override
	public void trackLoaded(AudioTrack track) {
		trackScheduler.playTrack(track);
	}

	@Override
	public void playlistLoaded(AudioPlaylist playlist) {
		// player.playTrack(playlist.getTracks().get(0));

		for (AudioTrack track : playlist.getTracks()) {
			trackScheduler.queueTrack(track);
		}
	}

	@Override
	public void noMatches() {
	}

	@Override
	public void loadFailed(FriendlyException throwable) {
	}
}
