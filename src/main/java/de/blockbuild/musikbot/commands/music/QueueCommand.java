package de.blockbuild.musikbot.commands.music;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.github.breadmoirai.discordemoji.Emoji;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.menu.ButtonMenu;
import com.jagrosh.jdautilities.menu.OrderedMenu;
import com.jagrosh.jdautilities.menu.Paginator;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import de.blockbuild.musikbot.Bot;
import de.blockbuild.musikbot.commands.MusicCommand;
import de.blockbuild.musikbot.configuration.ConfigFactory;
import de.blockbuild.musikbot.configuration.Configuration;
import de.blockbuild.musikbot.core.TrackScheduler;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.exceptions.PermissionException;

public class QueueCommand extends MusicCommand {

	private Boolean isSearch;

	public QueueCommand(Bot bot) {
		super(bot);
		this.name = "queue";
		this.aliases = new String[] { "q" };
		this.help = "Returns the playlist or adds the given track to queue.";
		this.arguments = "[URL|title]";
		this.joinOnCommand = true;
		this.isSearch = false;
	}

	@Override
	protected void doGuildCommand(CommandEvent event) {
		TrackScheduler trackScheduler = musicManager.getTrackScheduler();
		AudioPlayerManager playerManager = bot.getPlayerManager();

		if (args.isEmpty()) {
			if (!event.getMessage().getAttachments().isEmpty()) {
				// Add track to queue
				if (!event.getMessage().getAttachments().get(0).isImage()) {
					String TrackURL = event.getMessage().getAttachments().get(0).getUrl();
					String FileName = event.getMessage().getAttachments().get(0).getFileName();

					event.reply(Emoji.MAG_RIGHT.getUtf8() + " Trying to load **" + FileName + "**", m -> playerManager
							.loadItemOrdered(musicManager, TrackURL, new ResultHandler(trackScheduler, event, m)));
				}
			} else {
				if (trackScheduler.isQueueEmpty()) {
					// If the queue is empty
					event.reply(Emoji.NOTEPAD_SPIRAL.getUtf8() + " **Queued tracks:**\n`No tracks Queued!`");
				} else {
					// List queued Tracks
					List<AudioTrack> tracks = trackScheduler.getQueue();
					long time = 0;

					Paginator.Builder pbuilder = new Paginator.Builder().setEventWaiter(bot.getWaiter())
							.setColor(event.getSelfMember().getColor()).setItemsPerPage(15).waitOnSinglePage(true)
							.wrapPageEnds(true).setTimeout(2, TimeUnit.MINUTES).setItems(new String[0])
							.showPageNumbers(true).useNumberedItems(true);

					for (AudioTrack track : tracks) {
						pbuilder.addItems("`[" + trackScheduler.getTime(track.getDuration()) + "]` [**"
								+ track.getInfo().title + "**](" + track.getInfo().uri + ")");
						time += track.getDuration();
					}

					pbuilder.setText(Emoji.NOTEPAD_SPIRAL.getUtf8() + " **Queued tracks:**\n" + Emoji.NOTES.getUtf8()
							+ " Current queue **[** `" + tracks.size() + " Tracks` **|** `"
							+ trackScheduler.getTimeBig(time) + "` **]**");

					event.reply(Emoji.MAG_RIGHT.getUtf8() + " Loading...", m -> pbuilder.build().display(m));
				}
			}
		} else {
			// Load search or URL
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
		final Configuration config = ConfigFactory.getInstance().getConfig();

		builder.append(" **MusikBot** ").append("by Block-Build\n");
		builder.append("SpigotMC: `https://www.spigotmc.org/resources/the-discord-musikbot-on-minecraft.64277/`\n");
		builder.append("GitHub: `https://github.com/Block-Build/MusikBot`\n");
		builder.append("Version: `").append(config.getVersion()).append("`\n");
		builder.append("Do you have any problem or suggestion? Open an Issue on GitHub.");

		event.reply(builder.toString());
	}

	private class ResultHandler implements AudioLoadResultHandler {

		private TrackScheduler trackScheduler;
		private CommandEvent event;
		private final Message m;

		public ResultHandler(TrackScheduler trackScheduler, CommandEvent event, Message m) {
			this.trackScheduler = trackScheduler;
			this.event = event;
			this.m = m;
		}

		@Override
		public void trackLoaded(AudioTrack track) {
			int pos = trackScheduler.queueTrack(track);
			if (pos == 0) {
				trackScheduler.messageAddTrack(track, m);
			} else {
				trackScheduler.messageQueueTrack(track, m, pos);
			}
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
							int pos = trackScheduler.queueTrack(track);
							event.reply(Emoji.MAG_RIGHT.getUtf8() + " Loading...", reply -> {
								if (pos == 0) {
									trackScheduler.messageAddTrack(track, reply);
								} else {
									trackScheduler.messageQueueTrack(track, reply, pos);
								}
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

				int pos = trackScheduler.queueTrack(firstTrack);
				String message = pos == 0 ? trackScheduler.messageAddTrack(firstTrack, m)
						: trackScheduler.messageQueueTrack(firstTrack, m, pos);

				if (event.getMember().hasPermission(event.getTextChannel(), Permission.MESSAGE_ADD_REACTION)) {

					StringBuilder builder = new StringBuilder(Emoji.MAG_RIGHT.getUtf8());
					builder.append(" The track you provided has a playlist of **" + playlist.getTracks().size()
							+ "** tracks attached. Select " + Emoji.THUMBSUP.getUtf8()
							+ " to queue the whole playlist.");

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