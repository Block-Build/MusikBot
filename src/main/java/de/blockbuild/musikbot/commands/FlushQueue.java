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
		this.arguments = "[Amount]";
		this.joinOnCommand = false;
		this.category = MUSIC;
	}

	@Override
	protected void doCommand(CommandEvent event) {
		TrackScheduler trackScheduler = main.getBot().getGuildAudioPlayer(event.getGuild()).getTrackScheduler();
		if (event.getArgs().isEmpty()) {
			trackScheduler.flushQueue();
			StringBuilder builder = new StringBuilder();
			builder.append(event.getClient().getSuccess()).append(" Queue flushed!");
			event.reply(builder.toString());
		} else {
			int i = 0;
			try {
				i = Integer.parseInt(event.getArgs());
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
					builder.append(event.getClient().getError()).append(" `").append(event.getArgs())
							.append("` isn't a vaild Number.");
					event.reply(builder.toString());
				}
			}
		}
	}
}