package de.blockbuild.musikbot.commands;

import com.jagrosh.jdautilities.commandclient.CommandEvent;

import de.blockbuild.musikbot.Bot;
import de.blockbuild.musikbot.core.MBCommand;

public class QuitCommand extends MBCommand {

	public QuitCommand(Bot bot) {
		super(bot);
		this.name = "quit";
		this.help = "Triggers the Bot to quit the voice channel!";
		this.joinOnCommand = false;
		this.category = CONNECTION;
	}

	@Override
	protected void doCommand(CommandEvent event) {
		event.getSelfMember().getGuild().getAudioManager().closeAudioConnection();
	}
}
