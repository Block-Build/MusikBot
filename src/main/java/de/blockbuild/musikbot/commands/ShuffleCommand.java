package de.blockbuild.musikbot.commands;

import com.jagrosh.jdautilities.commandclient.CommandEvent;

import de.blockbuild.musikbot.Main;
import de.blockbuild.musikbot.core.MBCommand;
import de.blockbuild.musikbot.core.TrackScheduler;

public class ShuffleCommand extends MBCommand{

	public ShuffleCommand(Main main) {
		super(main);
		this.name = "shuffle";
		this.help = "Shuffles the playlist";
		this.joinOnCommand = false;
		this.category = MUSIC;
	}

	@Override
	protected void doCommand(CommandEvent event) {
		TrackScheduler trackScheduler = main.getBot().getGuildAudioPlayer(event.getGuild()).getTrackScheduler();
		trackScheduler.shuffle();
		StringBuilder builder = new StringBuilder().append(event.getClient().getSuccess());
		builder.append(" Playlist shuffled!");
		event.reply(builder.toString());
	}

}