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

public class RadioBonnRheinSiegCommand extends MBCommand {

	public RadioBonnRheinSiegCommand(Main main) {
		super(main);
		this.name = "RadioBonnRheinSieg";
		this.aliases = new String[] { "rbrs" };
		this.help = "Plays RadioBonnRheinSieg!";
		this.joinOnCommand = true;
		this.category = MUSIC;
	}

	@Override
	protected void doCommand(CommandEvent event) {
		TrackScheduler trackScheduler = main.getBot().getGuildAudioPlayer(event.getGuild()).getTrackScheduler();
		AudioPlayerManager playerManager = main.getBot().getPlayerManager();

		playerManager.loadItem("http://stream.lokalradio.nrw/rbrs", new ResultHandler(trackScheduler, event));
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
			trackScheduler.playTrack(track, event);
		}

		@Override
		public void playlistLoaded(AudioPlaylist playlist) {
			// should never called
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
