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
	}

	@Override
	protected void doCommand(CommandEvent e) {
		TrackScheduler ts = main.getBot().getScheduler();
		AudioPlayer player = ts.getPlayer();
		if (e.getArgs().isEmpty()) {
			e.reply("Currently playing: " + player.getPlayingTrack().getInfo().title + "\n"
					+ player.getPlayingTrack().getInfo().author);
		} else {
			AudioPlayerManager playerManager = main.getBot().getPlayerManager();
			playerManager.loadItem(e.getArgs(), new ResultHandler(ts));
		}
	}

	private class ResultHandler implements AudioLoadResultHandler {

		private TrackScheduler ts;

		public ResultHandler(TrackScheduler trackScheduler) {
			this.ts = trackScheduler;
		}

		@Override
		public void trackLoaded(AudioTrack track) {
			ts.playTrack(track);
		}

		@Override
		public void playlistLoaded(AudioPlaylist playlist) {
			for (AudioTrack track : playlist.getTracks()) {
				ts.playTrack(track);
			}
		}

		@Override
		public void noMatches() {
			// Notify the user that we've got nothing
		}

		@Override
		public void loadFailed(FriendlyException throwable) {
			// Notify the user that everything exploded
		}
	}
}
