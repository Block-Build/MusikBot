package de.blockbuild.musikbot.commands;

import com.jagrosh.jdautilities.commandclient.CommandEvent;

import de.blockbuild.musikbot.Main;
import de.blockbuild.musikbot.core.MBCommand;
import de.blockbuild.musikbot.core.TrackScheduler;

public class NextCommand extends MBCommand {

	public NextCommand(Main main) {
		super(main);
		this.name = "next";
		this.aliases = new String[] { "n" };
		this.help = "Returns the next tracks title.";
		this.joinOnCommand = true;
		this.category = MUSIC;
	}

	@Override
	protected void doCommand(CommandEvent event) {
		TrackScheduler trackScheduler = main.getBot().getGuildAudioPlayer(event.getGuild()).getTrackScheduler();
		StringBuilder builder = new StringBuilder();
		if (trackScheduler.getNextTrack() == null) {
			builder.append(event.getClient().getWarning()).append(" Queue is empty");
		} else {
			builder.append(event.getClient().getSuccess()).append(" Next track: `")
					.append(trackScheduler.getNextTrack().getInfo().title).append("`");
		}
		event.reply(builder.toString());
	}
}
