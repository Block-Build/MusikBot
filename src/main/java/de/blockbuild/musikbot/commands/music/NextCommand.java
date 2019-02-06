package de.blockbuild.musikbot.commands.music;

import com.jagrosh.jdautilities.command.CommandEvent;

import de.blockbuild.musikbot.Bot;
import de.blockbuild.musikbot.commands.MusicCommand;
import de.blockbuild.musikbot.core.TrackScheduler;

public class NextCommand extends MusicCommand {

	public NextCommand(Bot bot) {
		super(bot);
		this.name = "next";
		this.aliases = new String[] { "n" };
		this.help = "Returns the next tracks title.";
		this.joinOnCommand = true;
	}

	@Override
	protected void doGuildCommand(CommandEvent event) {
		TrackScheduler trackScheduler = bot.getGuildAudioPlayer(guild).getTrackScheduler();
		StringBuilder builder = new StringBuilder();
		if (trackScheduler.getNextTrack() == null) {
			builder.append(event.getClient().getWarning()).append(" Queue is empty");
		} else {
			builder.append(event.getClient().getSuccess()).append(" Next track: `")
					.append(trackScheduler.getNextTrack().getInfo().title).append("`");
		}
		event.reply(builder.toString());
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
