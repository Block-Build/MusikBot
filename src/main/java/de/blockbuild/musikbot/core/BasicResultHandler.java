package de.blockbuild.musikbot.core;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public class BasicResultHandler implements AudioLoadResultHandler {

	private final AudioPlayer player;
	private final CommandEvent event;
	private final String responce;

	public BasicResultHandler(AudioPlayer player, CommandEvent event, String responce) {
		this.player = player;
		this.event = event;
		this.responce = responce;
	}

	@Override
	public void trackLoaded(AudioTrack track) {
		player.playTrack(track);
		if (!(event == null))
			event.reply(String.format(event.getClient().getSuccess() + " " + responce, track.getInfo().title));
	}

	@Override
	public void playlistLoaded(AudioPlaylist playlist) {
		player.playTrack(playlist.getTracks().get(0));
		if (!(event == null))
			event.reply(String.format(event.getClient().getSuccess() + " " + responce,
					playlist.getTracks().get(0).getInfo().title));
	}

	@Override
	public void noMatches() {
	}

	@Override
	public void loadFailed(FriendlyException throwable) {
	}
}
