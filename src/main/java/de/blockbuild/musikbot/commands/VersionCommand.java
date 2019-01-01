package de.blockbuild.musikbot.commands;

import com.jagrosh.jdautilities.commandclient.CommandEvent;

import de.blockbuild.musikbot.Main;
import de.blockbuild.musikbot.core.MBCommand;

public class VersionCommand extends MBCommand {

	public VersionCommand(Main main) {
		super(main);
		this.name = "version";
		this.help = "MusikBot version.";
		this.joinOnCommand = false;
		this.category = SETUP;
	}

	@Override
	protected void doCommand(CommandEvent event) {
		StringBuilder builder = new StringBuilder().append(event.getClient().getSuccess());
		builder.append(" ").append(main.getDescription().getFullName());
		event.reply(builder.toString());
	}
}