package de.blockbuild.musikbot.commands;

import com.jagrosh.jdautilities.command.CommandEvent;

import de.blockbuild.musikbot.Bot;
import de.blockbuild.musikbot.core.MBCommand;
import de.blockbuild.musikbot.core.TrackScheduler;

public class QuitCommand extends MBCommand {

	public QuitCommand(Bot bot) {
		super(bot);
		this.name = "quit";
		this.aliases = new String[] { "leave", "disconnect" };
		this.help = "Disconnect and delete queue.";
		this.joinOnCommand = false;
		this.category = CONNECTION;
	}

	@Override
	protected void doCommand(CommandEvent event) {
		TrackScheduler trackScheduler = bot.getGuildAudioPlayer(event.getGuild()).getTrackScheduler();
		trackScheduler.flushQueue();
		trackScheduler.stopTrack();
		event.getSelfMember().getGuild().getAudioManager().closeAudioConnection();
	}
}
