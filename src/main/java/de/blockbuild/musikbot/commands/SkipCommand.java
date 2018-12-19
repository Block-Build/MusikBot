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
		trackScheduler.nextTrack(event);
	}

}
