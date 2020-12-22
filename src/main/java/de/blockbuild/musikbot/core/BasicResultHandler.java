package de.blockbuild.musikbot.core;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import net.dv8tion.jda.api.entities.TextChannel;

public class BasicResultHandler implements AudioLoadResultHandler {

	private GuildMusicManager musicManager;
	private TrackScheduler trackScheduler;

	public BasicResultHandler(GuildMusicManager musicManager) {
		this.musicManager = musicManager;
		this.trackScheduler = musicManager.getTrackScheduler();
	}

	@Override
	public void trackLoaded(AudioTrack track) {

		if (musicManager.config.isNowPlayingTrackEnabled()) {
			TextChannel tc = musicManager.getGuild()
					.getTextChannelById(musicManager.config.getNowPlayingTrackTextChannelId());

			tc.sendMessage(trackScheduler.messageNowPlayingTrackShort(musicManager.getAudioPlayer().getPlayingTrack()))
					.queue(m -> {
						if (musicManager.config.getMessageDeleteDelay() > 0) {
							musicManager.deleteMessageLater(tc, m, musicManager.config.getMessageDeleteDelay());
						}
					});
		}

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
