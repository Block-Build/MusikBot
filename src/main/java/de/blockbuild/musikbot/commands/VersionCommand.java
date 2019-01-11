package de.blockbuild.musikbot.commands;

import com.jagrosh.jdautilities.commandclient.CommandEvent;

import de.blockbuild.musikbot.Bot;
import de.blockbuild.musikbot.core.MBCommand;

public class VersionCommand extends MBCommand {

	public VersionCommand(Bot bot) {
		super(bot);
		this.name = "version";
		this.help = "MusikBot version.";
		this.joinOnCommand = false;
		this.category = SETUP;
	}

	@Override
	protected void doCommand(CommandEvent event) {
		StringBuilder builder = new StringBuilder().append(event.getClient().getSuccess());
		builder.append(" ").append(bot.getMain().getDescription().getFullName());
		event.reply(builder.toString());
	}
}