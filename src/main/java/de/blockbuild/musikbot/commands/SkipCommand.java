package de.blockbuild.musikbot.commands;

import com.jagrosh.jdautilities.command.CommandEvent;

import de.blockbuild.musikbot.Bot;
import de.blockbuild.musikbot.core.MBCommand;
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
	protected void doCommand(CommandEvent event) {
		TrackScheduler trackScheduler = bot.getGuildAudioPlayer(event.getGuild()).getTrackScheduler();

		if (!(event.getArgs().isEmpty())) {
			int i = 0;
			try {
				i = Integer.parseInt(event.getArgs());
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
					builder.append(" `").append(event.getArgs()).append("` isn't a number.");
					event.reply(builder.toString());
				}
			}
		} else {
			trackScheduler.nextTrack(event);
		}

	}

}
