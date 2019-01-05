package de.blockbuild.musikbot.core;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public class BasicResultHandler implements AudioLoadResultHandler {

	private final AudioPlayer player;

	public BasicResultHandler(AudioPlayer player) {
		this.player = player;
	}

	@Override
	public void trackLoaded(AudioTrack track) {
		player.playTrack(track);
	}

	@Override
	public void playlistLoaded(AudioPlaylist playlist) {
		player.playTrack(playlist.getTracks().get(0));
	}

	@Override
	public void noMatches() {
	}

	@Override
	public void loadFailed(FriendlyException throwable) {
	}
}
