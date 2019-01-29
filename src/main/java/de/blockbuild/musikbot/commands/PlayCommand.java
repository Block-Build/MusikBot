package de.blockbuild.musikbot.commands;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import de.blockbuild.musikbot.Bot;
import de.blockbuild.musikbot.core.GuildMusicManager;
import de.blockbuild.musikbot.core.MBCommand;
import de.blockbuild.musikbot.core.TrackScheduler;

public class PlayCommand extends MBCommand {

	private Boolean isSearch;

	public PlayCommand(Bot bot) {
		super(bot);
		this.name = "play";
		this.aliases = new String[] { "p" };
		this.help = "Shows current track or plays given track";
		this.arguments = "[URL|title]";
		this.joinOnCommand = true;
		this.category = MUSIC;
		this.isSearch = false;
	}

	@Override
	protected void doCommand(CommandEvent event) {
		GuildMusicManager musicManager = bot.getGuildAudioPlayer(event.getGuild());
		TrackScheduler trackScheduler = musicManager.getTrackScheduler();
		AudioPlayer player = musicManager.getAudioPlayer();
		AudioPlayerManager playerManager = bot.getPlayerManager();

		if (event.getArgs().isEmpty()) {
			StringBuilder builder = new StringBuilder();

			if (!event.getMessage().getAttachments().isEmpty()) {
				if (!event.getMessage().getAttachments().get(0).isImage()) {
					String TrackURL = event.getMessage().getAttachments().get(0).getUrl();
					playerManager.loadItemOrdered(musicManager, TrackURL, new ResultHandler(trackScheduler, event));
				}
			} else if (player.getPlayingTrack() == null) {
				builder.append(event.getClient().getWarning()).append(" `Queue is empty`");
			} else {
				builder.append(event.getClient().getSuccess()).append(" Playing: `")
						.append(player.getPlayingTrack().getInfo().title).append("`. Left time: `")
						.append(getTime(
								player.getPlayingTrack().getDuration() - player.getPlayingTrack().getPosition()))
						.append("` Minutes.");
			}
			event.reply(builder.toString());
		} else {
			String TrackUrl = event.getArgs();
			if (!event.getArgs().startsWith("http")) {
				TrackUrl = "ytsearch:" + TrackUrl;
				isSearch = true;
			} else {
				isSearch = false;
			}
			playerManager.loadItemOrdered(musicManager, TrackUrl, new ResultHandler(trackScheduler, event));
		}
	}

	private String getTime(long lng) {
		return (new SimpleDateFormat("mm:ss")).format(new Date(lng));
	}

	private class ResultHandler implements AudioLoadResultHandler {

		private TrackScheduler trackScheduler;
		private CommandEvent event;
		private GuildMusicManager musicManager;

		public ResultHandler(TrackScheduler trackScheduler, CommandEvent event) {
			this.trackScheduler = trackScheduler;
			this.event = event;
			this.musicManager = bot.getGuildAudioPlayer(event.getGuild());
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
			builder.append(" No result found: ").append(event.getArgs());
			event.reply(builder.toString());
		}

		@Override
		public void loadFailed(FriendlyException throwable) {
			StringBuilder builder = new StringBuilder(event.getClient().getError());
			builder.append(" Faild to load ").append(event.getArgs());
			event.reply(builder.toString());
		}
	}
}
