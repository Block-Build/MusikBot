package de.blockbuild.musikbot.commands;

import com.jagrosh.jdautilities.commandclient.CommandEvent;

import de.blockbuild.musikbot.Main;
import de.blockbuild.musikbot.core.MBCommand;
import de.blockbuild.musikbot.core.TrackScheduler;

public class SkipCommand extends MBCommand {

	public SkipCommand(Main main) {
		super(main);
		this.name = "skip";
		this.aliases = new String[] { "s", "sk" };
		this.help = "Skips the track";
	}

	@Override
	protected void doCommand(CommandEvent event) {
		TrackScheduler trackScheduler = main.getBot().getScheduler();

		if (!(event.getArgs().isEmpty())) {
			int i = 0;
			try {
				i = Integer.parseInt(event.getArgs());
			} catch (Exception e) {
				//no integer
			}
			if (i > 0) {
				trackScheduler.flushQueue(i);
				StringBuilder builder = new StringBuilder();
				builder.append(event.getClient().getSuccess()).append(" ").append(i).append(" tracks got flushed!");
				event.reply(builder.toString());
			}
			StringBuilder builder = new StringBuilder(event.getClient().getError());
			builder.append(" `").append(event.getArgs()).append("` isn't a vaild Number.");
			event.reply(builder.toString());
		}
		trackScheduler.nextTrack(event);

	}

}
