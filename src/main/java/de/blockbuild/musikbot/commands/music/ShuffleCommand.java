package de.blockbuild.musikbot.commands.music;

import com.github.breadmoirai.discordemoji.Emoji;
import com.jagrosh.jdautilities.command.CommandEvent;

import de.blockbuild.musikbot.Bot;
import de.blockbuild.musikbot.commands.MusicCommand;
import de.blockbuild.musikbot.configuration.ConfigFactory;
import de.blockbuild.musikbot.configuration.Configuration;
import de.blockbuild.musikbot.core.TrackScheduler;

public class ShuffleCommand extends MusicCommand {

	public ShuffleCommand(Bot bot) {
		super(bot);
		this.name = "shuffle";
		this.help = "Shuffles the playlist";
	}

	@Override
	protected void doGuildCommand(CommandEvent event) {
		TrackScheduler trackScheduler = musicManager.getTrackScheduler();

		StringBuilder builder = new StringBuilder();
		if (trackScheduler.isQueueEmpty()) {
			builder.append(event.getClient().getWarning()).append(" The queue is empty!");
		} else {
			trackScheduler.shuffle();
			builder.append(Emoji.TWISTED_RIGHTWARDS_ARROWS.getUtf8());
			builder.append(" Queue shuffled!");
		}
		event.reply(builder.toString());
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