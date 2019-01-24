package de.blockbuild.musikbot.commands;

import java.io.File;
import java.util.List;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import de.blockbuild.musikbot.Bot;
import de.blockbuild.musikbot.configuration.PlaylistConfiguration;
import de.blockbuild.musikbot.core.GuildMusicManager;
import de.blockbuild.musikbot.core.MBCommand;
import de.blockbuild.musikbot.core.TrackScheduler;

import net.dv8tion.jda.core.entities.User;

public class PlaylistCommand extends MBCommand {
	List<String> tracks;

	public PlaylistCommand(Bot bot) {
		super(bot);
		this.name = "playlist";
		this.aliases = new String[] { "pl" };
		this.help = "save, load, delete or list your playlists";
		this.arguments = "<save|delete|load|list> <name>";
		this.joinOnCommand = true;
		this.category = MUSIC;
	}

	@Override
	protected void doCommand(CommandEvent event) {
		StringBuilder builder = new StringBuilder().append(event.getClient().getSuccess());
		GuildMusicManager musicManager = bot.getGuildAudioPlayer(event.getGuild());
		TrackScheduler trackScheduler = musicManager.getTrackScheduler();

		String x[] = event.getArgs().split(" ", 2);
		User user = event.getMember().getUser();

		if (event.getArgs().isEmpty() || x.length < 2) {
			if (event.getArgs().startsWith("list") || event.getArgs().startsWith("show")
					|| event.getArgs().startsWith("load")) {
				builder.append(" **Your Playlists:**\n");
				File[] filelist = new File(bot.getMain().getDataFolder(), "/Playlists/" + user.getId() + "/")
						.listFiles();
				if (filelist.length == 0) {
					builder.append("`No saved Playlist.`");
				} else {
					for (File file : filelist) {
						builder.append("`").append(file.getName().substring(0, file.getName().length() - 4))
								.append("`\n");
					}
				}
				event.reply(builder.toString());
			} else {
				sendCommandInfo(event);
			}
			return;
		}

		String name = x[1];
		PlaylistConfiguration playlist = new PlaylistConfiguration(bot, user, name);

		switch (x[0]) {
		case "save":
		case "create":
			if (musicManager.getAudioPlayer().getPlayingTrack() == null) {
				builder.append(" First add some tracks to the player.");
				event.reply(builder.toString());
				return;
			}

			playlist.addTrack(musicManager.getAudioPlayer().getPlayingTrack().getInfo().uri);
			playlist.addTracks(trackScheduler.getQueue());

			if (playlist.writeConfig()) {
				builder.append(" Successfully saved: `").append(name).append("`");
			} else {
				builder.append(" Failed to save Playlist `").append(name).append("`");
			}
			event.reply(builder.toString());

			break;
		case "remove":
		case "delete":
		case "del":
			playlist.deleteConfig();
			builder.append(" Successfully deleted: `").append(name).append("`");
			event.reply(builder.toString());

			break;
		case "load":
			builder.append(" Load Playlist...");
			event.reply(builder.toString());

			if (playlist.getPlaylist().isEmpty()) {
				builder = new StringBuilder().append(event.getClient().getWarning());
				builder.append(" Playlist `").append(name).append("` dosen't exsist.");
				event.reply(builder.toString());
				return;
			}

			for (String track : playlist.getPlaylist()) {
				bot.getPlayerManager().loadItemOrdered(musicManager, track, new ResultHandler(trackScheduler, event));
			}

			builder = new StringBuilder().append(event.getClient().getSuccess());
			builder.append(" Playlist `").append(name).append("` loaded Successfully.");

			event.reply(builder.toString());

			break;
		case "list":
		case "show":
			builder.append(" **Playlist `").append(name).append("`**\n");
			for (String track : playlist.getPlaylist()) {
				builder.append("`").append(track).append("`\n");
			}
			event.reply(builder.toString());

			break;
		default:
			sendCommandInfo(event);
			break;
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
			trackScheduler.queueSilent(track);
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
