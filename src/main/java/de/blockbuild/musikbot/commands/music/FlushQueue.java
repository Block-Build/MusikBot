package de.blockbuild.musikbot.commands.music;

import com.jagrosh.jdautilities.command.CommandEvent;

import de.blockbuild.musikbot.Bot;
import de.blockbuild.musikbot.commands.MBCommand;
import de.blockbuild.musikbot.core.TrackScheduler;

public class FlushQueue extends MBCommand {

	public FlushQueue(Bot bot) {
		super(bot);
		this.name = "flush";
		this.aliases = new String[] { "f", "fl" };
		this.help = "Clears the queue";
		this.arguments = "[Amount]";
		this.joinOnCommand = false;
		this.category = MUSIC;
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
				if (i > 0) {
					trackScheduler.flushQueue(i);
					StringBuilder builder = new StringBuilder();
					builder.append(event.getClient().getSuccess()).append(" ").append(i).append(" tracks got flushed!");
					if (!(trackScheduler.getNextTrack() == null)) {
						builder.append("\nNext track: `").append(trackScheduler.getNextTrack().getInfo().title)
								.append("`");
					}
					event.reply(builder.toString());
				} else {
					StringBuilder builder = new StringBuilder();
					builder.append(event.getClient().getError()).append(" `").append(args)
							.append("` isn't a vaild Number.");
					event.reply(builder.toString());
				}
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