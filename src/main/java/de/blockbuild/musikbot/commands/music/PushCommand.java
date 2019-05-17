package de.blockbuild.musikbot.commands.music;

import java.util.ArrayList;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import de.blockbuild.musikbot.Bot;
import de.blockbuild.musikbot.commands.MBCommand;
import de.blockbuild.musikbot.core.GuildMusicManager;
import de.blockbuild.musikbot.core.TrackScheduler;

public class PushCommand extends MBCommand {

	public PushCommand(Bot bot) {
		super(bot);
		this.name = "push";
		// this.aliases = new String[] { "" };
		this.help = "Plays a given track and queues the current playing track";
		// this.arguments = "";
		this.joinOnCommand = true;
	}

	@Override
	protected void doGuildCommand(CommandEvent event) {

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
		private GuildMusicManager musicManager;

		public ResultHandler(TrackScheduler trackScheduler, CommandEvent event) {
			this.trackScheduler = trackScheduler;
			this.event = event;
			this.musicManager = bot.getGuildAudioPlayer(guild);
		}

		@Override
		public void trackLoaded(AudioTrack track) {
			trackScheduler.playTrack(track, event);
		}

		@Override
		public void playlistLoaded(AudioPlaylist playlist) {
			if (isSearch) {
				musicManager.tracks = new ArrayList<>();

				StringBuilder builder = new StringBuilder().append(event.getClient().getSuccess());
				builder.append(" Use `!Choose <1-5>` to choose one of the search results: \n");
				for (int i = 0; i < 5; i++) {
					builder.append("`").append(i + 1 + ". ").append(playlist.getTracks().get(i).getInfo().title)
							.append("`\n");
					musicManager.tracks.add(playlist.getTracks().get(i));
					musicManager.setIsQueue(false);
				}
				event.reply(builder.toString());
			} else {
				AudioTrack firstTrack = playlist.getSelectedTrack();
				if (firstTrack == null) {
					firstTrack = playlist.getTracks().get(0);
				}
				trackScheduler.playTrack(firstTrack, event);
			}
		}

		@Override
		public void noMatches() {
			StringBuilder builder = new StringBuilder(event.getClient().getError());
			builder.append(" No result found: ").append(args);
			event.reply(builder.toString());
		}

		@Override
		public void loadFailed(FriendlyException throwable) {
			StringBuilder builder = new StringBuilder(event.getClient().getError());
			builder.append(" Faild to load ").append(args);
			event.reply(builder.toString());
		}
	}

}
