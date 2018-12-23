package de.blockbuild.musikbot.commands;

import com.jagrosh.jdautilities.commandclient.CommandEvent;

import de.blockbuild.musikbot.Main;
import de.blockbuild.musikbot.core.MBCommand;
import de.blockbuild.musikbot.core.TrackScheduler;

public class StopCommand extends MBCommand {

	public StopCommand(Main main) {
		super(main);
		this.name = "stop";
		this.help = "Disconnect and delete queue";
		this.joinOnCommand = false;
		this.category = CONNECTION;
	}

	@Override
	protected void doCommand(CommandEvent event) {
		TrackScheduler trackScheduler = main.getBot().getGuildAudioPlayer(event.getGuild()).getTrackScheduler();
		trackScheduler.flushQueue();
		trackScheduler.stopTrack();
		event.getSelfMember().getGuild().getAudioManager().closeAudioConnection();
	}
}
