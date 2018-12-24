package de.blockbuild.musikbot.commands;

import java.util.ArrayList;

import com.jagrosh.jdautilities.commandclient.CommandEvent;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import de.blockbuild.musikbot.Main;
import de.blockbuild.musikbot.core.GuildMusicManager;
import de.blockbuild.musikbot.core.MBCommand;
import de.blockbuild.musikbot.core.TrackScheduler;

public class QueueCommand extends MBCommand {

	private Boolean isSearch;

	public QueueCommand(Main main) {
		super(main);
		this.name = "queue";
		this.aliases = new String[] { "q" };
		this.help = "Returns the playlist or adds the given track to queue.";
		this.arguments = "[URL|title]";
		this.joinOnCommand = true;
		this.category = MUSIC;
	}

	@Override
	protected void doCommand(CommandEvent event) {
		GuildMusicManager musicManager = main.getBot().getGuildAudioPlayer(event.getGuild());
		TrackScheduler trackScheduler = musicManager.getTrackScheduler();
		if (event.getArgs().isEmpty()) {
			StringBuilder builder = new StringBuilder(event.getClient().getSuccess());
			builder.append("Tracks in Queue:\n").append(trackScheduler.getPlaylist());
			event.reply(builder.toString());
		} else {
			String TrackUrl = event.getArgs();
			if (!event.getArgs().startsWith("http")) {
				TrackUrl = "ytsearch:" + TrackUrl;
				isSearch = true;
			}
			AudioPlayerManager playerManager = main.getBot().getPlayerManager();
			playerManager.loadItemOrdered(musicManager, TrackUrl, new ResultHandler(trackScheduler, event));
		}
	}

	private class ResultHandler implements AudioLoadResultHandler {

		private TrackScheduler trackScheduler;
		private CommandEvent event;
		private GuildMusicManager musicManager;

		public ResultHandler(TrackScheduler trackScheduler, CommandEvent event) {
			this.trackScheduler = trackScheduler;
			this.event = event;
			this.musicManager = main.getBot().getGuildAudioPlayer(event.getGuild());
		}

		@Override
		public void trackLoaded(AudioTrack track) {
			trackScheduler.queue(track, event);
		}

		@Override
		public void playlistLoaded(AudioPlaylist playlist) {
			if (isSearch) {
				musicManager.tracks = new ArrayList<>();

				StringBuilder builder = new StringBuilder().append(event.getClient().getSuccess());
				builder.append(" Use `!Choose <1-5>` to choose one of the search results: \n");
				for (int i = 0; i < 5; i++) {
					builder.append("`").append(i + 1 + ". ").append(playlist.getTracks().get(i).getInfo().title).append("`\n");
					musicManager.tracks.add(playlist.getTracks().get(i));
					musicManager.isQueue = true;
				}
				event.reply(builder.toString());
			} else {
				trackScheduler.queue(playlist, event);
			}
		}

		@Override
		public void noMatches() {
			StringBuilder builder = new StringBuilder(event.getClient().getError());
			builder.append(" No result found: ").append(event.getArgs());
			event.reply(builder.toString());
		}

		@Override
		public void loadFailed(FriendlyException throwable) {
			StringBuilder builder = new StringBuilder(event.getClient().getError());
			builder.append(" faild to load ").append(event.getArgs());
			event.reply(builder.toString());
		}
	}
}