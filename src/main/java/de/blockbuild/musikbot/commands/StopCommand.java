package de.blockbuild.musikbot.commands;

import com.jagrosh.jdautilities.commandclient.CommandEvent;

import de.blockbuild.musikbot.Bot;
import de.blockbuild.musikbot.core.MBCommand;
import de.blockbuild.musikbot.core.TrackScheduler;

public class StopCommand extends MBCommand {

	public StopCommand(Bot bot) {
		super(bot);
		this.name = "stop";
		this.help = "Disconnect and delete queue";
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
