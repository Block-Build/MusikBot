package de.blockbuild.musikbot.commands;

import com.jagrosh.jdautilities.commandclient.CommandEvent;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import de.blockbuild.musikbot.Main;
import de.blockbuild.musikbot.core.MBCommand;
import de.blockbuild.musikbot.core.TrackScheduler;

public class PlayCommand extends MBCommand {

	public PlayCommand(Main main) {
		super(main);
		this.name = "play";
		this.aliases = new String[] { "p" };
		this.help = "Plays given track";
		this.arguments = "<URL|title>";
	}

	@Override
	protected void doCommand(CommandEvent e) {
		TrackScheduler ts = main.getBot().getScheduler();
		AudioPlayer player = ts.getPlayer();
		if (e.getArgs().isEmpty()) {
			e.reply("Currently playing: " + player.getPlayingTrack().getInfo().title);
		} else {
			AudioPlayerManager playerManager = main.getBot().getPlayerManager();
			playerManager.loadItem(e.getArgs(), new ResultHandler(ts, e));
		}
	}

	private class ResultHandler implements AudioLoadResultHandler {

		private TrackScheduler ts;
		private CommandEvent event;

		public ResultHandler(TrackScheduler trackScheduler, CommandEvent event) {
			this.ts = trackScheduler;
			this.event = event;
		}

		@Override
		public void trackLoaded(AudioTrack track) {
			ts.playTrack(track, event);
		}

		@Override
		public void playlistLoaded(AudioPlaylist playlist) {
			AudioTrack firstTrack = playlist.getSelectedTrack();

			if (firstTrack == null) {
				firstTrack = playlist.getTracks().get(0);
			}
			ts.playTrack(firstTrack, event);
		}

		@Override
		public void noMatches() {
			System.out.println("no results found: " + event.getArgs());
			// Notify the user that we've got nothing
		}

		@Override
		public void loadFailed(FriendlyException throwable) {
			System.out.println("faild to load: " + event.getArgs());
			// Notify the user that everything exploded
		}
	}
}
