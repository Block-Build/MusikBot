package de.blockbuild.musikbot.commands.music;

import java.util.concurrent.TimeUnit;

import com.github.breadmoirai.discordemoji.Emoji;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.menu.ButtonMenu;
import com.jagrosh.jdautilities.menu.OrderedMenu;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import de.blockbuild.musikbot.Bot;
import de.blockbuild.musikbot.commands.MusicCommand;
import de.blockbuild.musikbot.core.TrackScheduler;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.exceptions.PermissionException;

public class PlayCommand extends MusicCommand {

	private Boolean isSearch;

	public PlayCommand(Bot bot) {
		super(bot);
		this.name = "play";
		this.aliases = new String[] { "p" };
		this.help = "Shows current track or plays given track";
		this.arguments = "[URL|title]";
		this.joinOnCommand = true;
		this.isSearch = false;
	}

	@Override
	protected void doGuildCommand(CommandEvent event) {
		TrackScheduler trackScheduler = musicManager.getTrackScheduler();
		AudioPlayer player = musicManager.getAudioPlayer();
		AudioPlayerManager playerManager = bot.getPlayerManager();

		if (args.isEmpty()) {
			StringBuilder builder = new StringBuilder();

			if (!event.getMessage().getAttachments().isEmpty()) {
				if (!event.getMessage().getAttachments().get(0).isImage()) {
					String TrackURL = event.getMessage().getAttachments().get(0).getUrl();
					String FileName = event.getMessage().getAttachments().get(0).getFileName();
					event.reply(Emoji.MAG_RIGHT.getUtf8() + " Trying to load **" + FileName + "**", m -> playerManager
							.loadItemOrdered(musicManager, TrackURL, new ResultHandler(trackScheduler, event, m)));
				}
			} else if (player.getPlayingTrack() == null) {
				if (trackScheduler.isQueueEmpty()) {
					builder.append(event.getClient().getWarning()).append(" **Nothing to play at the moment**");
					event.reply(builder.toString());
				} else {
					// If player don't play but songs are in queue
					AudioTrack track = trackScheduler.playNextTrack();
					if (track != null) {
						builder.append(Emoji.NOTES.getUtf8()).append(" Now playing: **")
								.append(player.getPlayingTrack().getInfo().title).append("**. Left time: (`")
								.append(trackScheduler.getTime(player.getPlayingTrack().getDuration()
										- player.getPlayingTrack().getPosition()))
								.append("`) Minutes.");
						event.reply(builder.toString());
					}
				}
			} else {
				builder.append(Emoji.NOTES.getUtf8()).append(" Now playing: **")
						.append(player.getPlayingTrack().getInfo().title).append("**. Left time: (`")
						.append(trackScheduler.getTime(
								player.getPlayingTrack().getDuration() - player.getPlayingTrack().getPosition()))
						.append("`) Minutes.");
				event.reply(builder.toString());
			}
		} else {
			final String TrackUrl;
			if (args.startsWith("http")) {
				isSearch = false;
				TrackUrl = args;
			} else {
				isSearch = true;
				TrackUrl = "ytsearch:" + args;
			}

			event.reply(Emoji.MAG_RIGHT.getUtf8() + " Searching... `" + event.getArgs() + "`", m -> playerManager
					.loadItemOrdered(musicManager, TrackUrl, new ResultHandler(trackScheduler, event, m)));
		}
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
		private final Message m;

		public ResultHandler(TrackScheduler trackScheduler, CommandEvent event, Message m) {
			this.m = m;
			this.trackScheduler = trackScheduler;
			this.event = event;
		}

		@Override
		public void trackLoaded(AudioTrack track) {
			trackScheduler.messagePlayTrack(track, m);
			trackScheduler.playTrack(track);
		}

		@Override
		public void playlistLoaded(AudioPlaylist playlist) {
			if (isSearch) {
				StringBuilder builder = new StringBuilder().append(event.getClient().getSuccess());
				builder.append(" Click the number or type in chat to choose one of the search results:");

				final OrderedMenu.Builder bbuilder = new OrderedMenu.Builder().useCancelButton(true)
						.allowTextInput(true).setEventWaiter(bot.getWaiter()).setTimeout(1, TimeUnit.MINUTES);
				bbuilder.setText(builder.toString()).setColor(event.getSelfMember().getColor())
						.setChoices(new String[0]).setSelection((msg, i) -> {

							AudioTrack track = playlist.getTracks().get(i - 1);
							trackScheduler.playTrack(track);
							event.reply(Emoji.MAG_RIGHT.getUtf8() + " Loading...", reply -> {
								trackScheduler.messagePlayTrack(track, reply);
							});
						}).setUsers(event.getAuthor());

				for (int i = 0; i < 5 && i < playlist.getTracks().size(); i++) {

					AudioTrack track = playlist.getTracks().get(i);

					bbuilder.addChoice("`[" + trackScheduler.getTime(track.getDuration()) + "]` [**"
							+ track.getInfo().title + "**](" + track.getInfo().uri + ")");
				}
				bbuilder.build().display(m);
			} else {
				final AudioTrack firstTrack = playlist.getSelectedTrack() == null ? playlist.getTracks().get(0)
						: playlist.getSelectedTrack();

				String message = trackScheduler.messagePlayTrack(firstTrack, m);
				trackScheduler.playTrack(firstTrack);

				if (event.getMember().hasPermission(event.getTextChannel(), Permission.MESSAGE_ADD_REACTION)) {

					StringBuilder builder = new StringBuilder(Emoji.MAG_RIGHT.getUtf8());
					builder.append(" The track you provided has a playlist of **" + playlist.getTracks().size()
							+ "** tracks attached. Select " + Emoji.THUMBSUP.getUtf8()
							+ " to load the whole playlist.");

					new ButtonMenu.Builder().setText(message + "\n" + builder.toString())
							.setChoices(Emoji.THUMBSUP.getUtf8(), Emoji.THUMBSDOWN.getUtf8())
							.addUsers(event.getAuthor()).setEventWaiter(bot.getWaiter())
							.setTimeout(30, TimeUnit.SECONDS).setAction(rem -> {

								if (rem.getName().equals(Emoji.THUMBSUP.getUtf8())) {
									loadPlaylist(playlist, firstTrack);
									m.editMessage(message + "\n" + Emoji.WHITE_CHECK_MARK.getUtf8() + " Added **"
											+ (playlist.getTracks().size() - 1) + "** additional tracks to queue!")
											.queue();
								}
							}).setFinalAction(m -> {
								try {
									m.clearReactions().queue();
								} catch (PermissionException ignore) {
								}
							}).build().display(m);
				}
			}
		}

		private void loadPlaylist(AudioPlaylist playlist, AudioTrack exclude) {
			for (AudioTrack track : playlist.getTracks()) {
				if (!track.equals(exclude)) {
					trackScheduler.queueTrack(track);
				}
			}
		}

		@Override
		public void noMatches() {
			StringBuilder builder = new StringBuilder(event.getClient().getError());
			builder.append(" No result found: `").append(args).append("`");
			event.reply(builder.toString());
		}

		@Override
		public void loadFailed(FriendlyException throwable) {
			StringBuilder builder = new StringBuilder(event.getClient().getError());
			builder.append(" Faild to load `").append(args).append("`");
			event.reply(builder.toString());
		}
	}
}
