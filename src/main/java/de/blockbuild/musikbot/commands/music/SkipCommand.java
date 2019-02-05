package de.blockbuild.musikbot.commands.music;

import com.jagrosh.jdautilities.command.CommandEvent;

import de.blockbuild.musikbot.Bot;
import de.blockbuild.musikbot.commands.MBCommand;
import de.blockbuild.musikbot.core.TrackScheduler;

public class SkipCommand extends MBCommand {

	public SkipCommand(Bot bot) {
		super(bot);
		this.name = "skip";
		this.aliases = new String[] { "s", "sk" };
		this.help = "Skips the track";
		this.arguments = "[Amount]";
		this.joinOnCommand = true;
		this.category = MUSIC;
	}

	@Override
	protected void doGuildCommand(CommandEvent event) {
		TrackScheduler trackScheduler = musicManager.getTrackScheduler();

		if (!(args.isEmpty())) {
			int i = 0;
			try {
				i = Integer.parseInt(args);
			} catch (Exception e) {
				// no integer
			} finally {
				if (i > 0) {
					trackScheduler.flushQueue(i - 1);
					StringBuilder builder = new StringBuilder(event.getClient().getSuccess());
					builder.append(" ").append(i).append(" tracks got flushed!");
					event.reply(builder.toString());
					trackScheduler.nextTrack(event);
				} else {
					StringBuilder builder = new StringBuilder(event.getClient().getError());
					builder.append(" `").append(args).append("` isn't a number.");
					event.reply(builder.toString());
				}
			}
		} else {
			trackScheduler.nextTrack(event);
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
