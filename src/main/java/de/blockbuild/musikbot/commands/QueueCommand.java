package de.blockbuild.musikbot.commands;

import com.jagrosh.jdautilities.commandclient.CommandEvent;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import de.blockbuild.musikbot.Main;
import de.blockbuild.musikbot.core.MBCommand;
import de.blockbuild.musikbot.core.TrackScheduler;

public class QueueCommand extends MBCommand {

	public QueueCommand(Main main) {
		super(main);
		this.name = "queue";
		this.aliases = new String[] { "q" };
		this.help = "Adds the given track to queue";
		this.arguments = "<URL|title>";
	}

	@Override
	protected void doCommand(CommandEvent event) {
		TrackScheduler trackScheduler = main.getBot().getScheduler();
		if (event.getArgs().isEmpty()) {
			StringBuilder builder = new StringBuilder(event.getClient().getSuccess());
			builder.append("Tracks in Queue:\n").append(trackScheduler.getPlaylist());
			event.reply(builder.toString());
		} else {
			AudioPlayerManager playerManager = main.getBot().getPlayerManager();
			playerManager.loadItem(event.getArgs(), new ResultHandler(trackScheduler, event));
		}
	}

	private class ResultHandler implements AudioLoadResultHandler {

		private TrackScheduler trackScheduler;
		private CommandEvent event;

		public ResultHandler(TrackScheduler trackScheduler, CommandEvent event) {
			this.trackScheduler = trackScheduler;
			this.event = event;
		}

		@Override
		public void trackLoaded(AudioTrack track) {
			trackScheduler.queue(track, event);
		}

		@Override
		public void playlistLoaded(AudioPlaylist playlist) {
			for (AudioTrack track : playlist.getTracks()) {
				trackScheduler.queue(track, event);
			}
		}

		@Override
		public void noMatches() {
			StringBuilder builder = new StringBuilder(event.getClient().getError());
			builder.append(" No result found: ").append(event.getArgs());
			event.reply(builder.toString());
			System.out.println("no results found: " + event.getArgs());
		}

		@Override
		public void loadFailed(FriendlyException throwable) {
			StringBuilder builder = new StringBuilder(event.getClient().getError());
			builder.append(" faild to load ").append(event.getArgs());
			event.reply(builder.toString());
			System.out.println("faild to load: " + event.getArgs());
		}
	}
}