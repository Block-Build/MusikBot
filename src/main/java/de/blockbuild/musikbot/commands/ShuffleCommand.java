package de.blockbuild.musikbot.commands;

import com.jagrosh.jdautilities.command.CommandEvent;

import de.blockbuild.musikbot.Bot;
import de.blockbuild.musikbot.core.MBCommand;
import de.blockbuild.musikbot.core.TrackScheduler;

public class ShuffleCommand extends MBCommand {

	public ShuffleCommand(Bot bot) {
		super(bot);
		this.name = "shuffle";
		this.help = "Shuffles the playlist";
		this.joinOnCommand = false;
		this.category = MUSIC;
	}

	@Override
	protected void doCommand(CommandEvent event) {
		TrackScheduler trackScheduler = bot.getGuildAudioPlayer(event.getGuild()).getTrackScheduler();
		trackScheduler.shuffle();
		StringBuilder builder = new StringBuilder().append(event.getClient().getSuccess());
		builder.append(" Playlist shuffled!");
		event.reply(builder.toString());
	}

}