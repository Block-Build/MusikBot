package de.blockbuild.musikbot.commands.radio;

import com.github.breadmoirai.discordemoji.Emoji;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import de.blockbuild.musikbot.Bot;
import de.blockbuild.musikbot.commands.RadioCommand;
import de.blockbuild.musikbot.core.TrackScheduler;

import net.dv8tion.jda.core.entities.Message;

public class RadioBobCommand extends RadioCommand {

	public RadioBobCommand(Bot bot) {
		super(bot);
		this.name = "radiobob";
		this.aliases = new String[] { "rb", "bob" };
		this.help = "Streams RadioBob!";
		this.joinOnCommand = true;
	}

	@Override
	protected void doGuildCommand(CommandEvent event) {
		TrackScheduler trackScheduler = musicManager.getTrackScheduler();
		AudioPlayerManager playerManager = bot.getPlayerManager();

		event.reply(Emoji.MAG_RIGHT.getUtf8() + " Loading...",
				m -> playerManager.loadItemOrdered(musicManager,
						"http://streams.radiobob.de/bob-live/mp3-192/mediaplayer",
						new ResultHandler(trackScheduler, event, m)));
	}

	@Override
	protected void doPrivateCommand(CommandEvent event) {
		event.reply(event.getClient().getError() + " This command cannot be used in Direct messages.");

		StringBuilder builder = new StringBuilder().append(event.getClient().getSuccess());

		builder.append(" **MusikBot** ").append("by Block-Build\n");
		builder.append("SpigotMC: `https://www.spigotmc.org/resources/the-discord-musikbot-on-minecraft.64277/`\n");
		builder.append("GitHub: `https://github.com/Block-Build/MusikBot`\n");
		builder.append("Version: `").append(bot.getMain().getDescription().getVersion()).append("`\n");
		builder.append("Do you have any problem or suggestion? Open an Issue on GitHub.");

		event.reply(builder.toString());
	}

	private class ResultHandler implements AudioLoadResultHandler {

		private TrackScheduler trackScheduler;
		private CommandEvent event;
		final private Message m;

		public ResultHandler(TrackScheduler trackScheduler, CommandEvent event, Message m) {
			this.trackScheduler = trackScheduler;
			this.event = event;
			this.m = m;
		}

		@Override
		public void trackLoaded(AudioTrack track) {
			trackScheduler.playTrack(track);
			trackScheduler.messageAddTrack(track, m);
		}

		@Override
		public void playlistLoaded(AudioPlaylist playlist) {
			// should never called
		}

		@Override
		public void noMatches() {
			StringBuilder builder = new StringBuilder(event.getClient().getError());
			builder.append(" No result found: **").append(args).append("**");
			event.reply(builder.toString());
		}

		@Override
		public void loadFailed(FriendlyException throwable) {
			StringBuilder builder = new StringBuilder(event.getClient().getError());
			builder.append(" Faild to load **").append(args).append("**");
			event.reply(builder.toString());
		}
	}
}
