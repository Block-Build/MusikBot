package de.blockbuild.musikbot.commands.music;

import com.github.breadmoirai.discordemoji.Emoji;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import de.blockbuild.musikbot.Bot;
import de.blockbuild.musikbot.commands.MusicCommand;
import de.blockbuild.musikbot.core.TrackScheduler;

public class SkipCommand extends MusicCommand {

	public SkipCommand(Bot bot) {
		super(bot);
		this.name = "skip";
		this.aliases = new String[] { "s", "sk" };
		this.help = "Skips the track";
		this.arguments = "[Amount]";
		this.joinOnCommand = true;
	}

	@Override
	protected void doGuildCommand(CommandEvent event) {
		TrackScheduler trackScheduler = musicManager.getTrackScheduler();
		AudioPlayer player = musicManager.getAudioPlayer();

		if (!(args.isEmpty())) {
			int i = 0;
			try {
				i = Integer.parseInt(args);
			} catch (Exception e) {
				// no integer
			} finally {
				if (i > 0) {
					int num = trackScheduler.flushQueue(i - 1);
					StringBuilder builder = new StringBuilder(event.getClient().getSuccess());
					builder.append(" **").append(num + 1).append("** tracks got skipped!");

					event.reply(Emoji.MAG_RIGHT.getUtf8() + " Loading...", m -> {
						if (trackScheduler.playNextTrack()) {
							trackScheduler.messageNowPlayingTrackLong(player.getPlayingTrack(), m, builder.toString());
						} else {
							builder.append("\n")
									.append(event.getClient().getWarning() + " **Nothing to play at the moment!**");
							m.editMessage(builder.toString()).queue();
						}
					});
				} else {
					StringBuilder builder = new StringBuilder(event.getClient().getError());
					builder.append(" The number must be greater than zero!");
					event.reply(builder.toString());
				}
			}
		} else {
			if (trackScheduler.playNextTrack()) {
				event.reply(Emoji.MAG_RIGHT.getUtf8() + " Loading...",
						m -> trackScheduler.messageNowPlayingTrackLong(player.getPlayingTrack(), m, null));
			} else {
				event.reply(event.getClient().getWarning() + " **Nothing to play at the moment!**");
			}
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

}
