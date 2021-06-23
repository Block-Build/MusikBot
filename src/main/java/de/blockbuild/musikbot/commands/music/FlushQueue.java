package de.blockbuild.musikbot.commands.music;

import com.github.breadmoirai.discordemoji.Emoji;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import de.blockbuild.musikbot.Bot;
import de.blockbuild.musikbot.commands.MusicCommand;
import de.blockbuild.musikbot.configuration.ConfigFactory;
import de.blockbuild.musikbot.configuration.Configuration;
import de.blockbuild.musikbot.core.TrackScheduler;

public class FlushQueue extends MusicCommand {

	public FlushQueue(Bot bot) {
		super(bot);
		this.name = "flush";
		this.aliases = new String[] { "f", "fl" };
		this.help = "Clears the queue";
		this.arguments = "[Amount]";
	}

	@Override
	protected void doGuildCommand(CommandEvent event) {
		TrackScheduler trackScheduler = musicManager.getTrackScheduler();
		if (args.isEmpty()) {
			trackScheduler.flushQueue();
			StringBuilder builder = new StringBuilder();
			builder.append(event.getClient().getSuccess()).append(" Queue flushed!");
			event.reply(builder.toString());
		} else {
			int i = 0;
			try {
				i = Integer.parseInt(args);
			} catch (Exception e) {
				// no integer
			} finally {
				StringBuilder builder = new StringBuilder();
				if (i > 0) {
					int num = trackScheduler.flushQueue(i);
					builder.append(event.getClient().getSuccess()).append(" **").append(num)
							.append("** Tracks got flushed!");
					if (!(trackScheduler.getNextTrack() == null)) {
						AudioTrack track = trackScheduler.getNextTrack();
						builder.append("\n").append(Emoji.NOTES.getUtf8()).append(" Next Track: **")
								.append(track.getInfo().title).append("**");
						builder.append(" (`").append(trackScheduler.getTime(track.getDuration())).append("`)");
					}
				} else {
					builder.append(event.getClient().getError()).append(" **").append(args)
							.append(" The number must be greater than zero!");
				}
				event.reply(builder.toString());
			}
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
}