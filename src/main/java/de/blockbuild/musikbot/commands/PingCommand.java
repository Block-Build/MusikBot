package de.blockbuild.musikbot.commands;

import com.jagrosh.jdautilities.commandclient.CommandEvent;

import de.blockbuild.musikbot.Main;
import de.blockbuild.musikbot.core.MBCommand;

public class PingCommand extends MBCommand {
	public PingCommand(Main main) {
		super(main);
		this.name = "ping";
		this.help = "Displays the responce time";
		this.joinOnCommand = false;
		this.category = CONNECTION;
		this.guildOnly = false;
	}

	@Override
	protected void doCommand(CommandEvent event) {
		StringBuilder builder = new StringBuilder(event.getClient().getSuccess());
		builder.append(" Ping: `").append(event.getJDA().getPing()).append("ms`");
		event.reply(builder.toString());
	}
}
