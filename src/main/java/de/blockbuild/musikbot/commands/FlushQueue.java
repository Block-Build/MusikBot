package de.blockbuild.musikbot.commands;

import com.jagrosh.jdautilities.commandclient.CommandEvent;

import de.blockbuild.musikbot.Main;
import de.blockbuild.musikbot.core.MBCommand;
import de.blockbuild.musikbot.core.TrackScheduler;

public class FlushQueue extends MBCommand {

	public FlushQueue(Main main) {
		super(main);
		this.name = "flush";
		this.aliases = new String[] { "f", "fl" };
		this.help = "Clears the queue";
	}

	@Override
	protected void doCommand(CommandEvent event) {
		TrackScheduler trackScheduler = main.getBot().getScheduler();
		StringBuilder builder = new StringBuilder();
		if (event.getArgs().isEmpty()) {
			trackScheduler.flushQueue();
			builder.append(event.getClient().getSuccess()).append(" Queue flushed!");
		} else {
			int i = 0;
			try {
				i = Integer.parseInt(event.getArgs());
			} catch (Exception e) {
				builder.append(event.getClient().getError()).append(" `").append(event.getArgs())
						.append("` isn't a vaild Number.");
			}
			if (i > 0) {
				trackScheduler.flushQueue(i);
				builder.append(event.getClient().getSuccess()).append(" ").append(i).append(" tracks got flushed!");
				if (!(trackScheduler.getNextTrack() == null)) {
					builder.append("\nNext track: `").append(trackScheduler.getNextTrack().getInfo().title).append("`");
				}
				event.reply(builder.toString());
			}
		}
	}
}