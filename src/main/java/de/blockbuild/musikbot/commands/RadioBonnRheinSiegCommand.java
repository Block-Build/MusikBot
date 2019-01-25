package de.blockbuild.musikbot.commands;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import de.blockbuild.musikbot.Bot;
import de.blockbuild.musikbot.core.GuildMusicManager;
import de.blockbuild.musikbot.core.MBCommand;
import de.blockbuild.musikbot.core.TrackScheduler;

public class RadioBonnRheinSiegCommand extends MBCommand {

	public RadioBonnRheinSiegCommand(Bot bot) {
		super(bot);
		this.name = "radiobonnrheinsieg";
		this.aliases = new String[] { "rbrs" };
		this.help = "Plays RadioBonnRheinSieg!";
		this.joinOnCommand = true;
		this.category = MUSIC;
	}

	@Override
	protected void doCommand(CommandEvent event) {
		GuildMusicManager musicManager = bot.getGuildAudioPlayer(event.getGuild());
		TrackScheduler trackScheduler = musicManager.getTrackScheduler();
		AudioPlayerManager playerManager = bot.getPlayerManager();

		playerManager.loadItemOrdered(musicManager, "http://stream.lokalradio.nrw/rbrs",
				new ResultHandler(trackScheduler, event));
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
		}

		@Override
		public void loadFailed(FriendlyException throwable) {
			StringBuilder builder = new StringBuilder(event.getClient().getError());
			builder.append(" faild to load ").append(event.getArgs());
			event.reply(builder.toString());
		}
	}
}
