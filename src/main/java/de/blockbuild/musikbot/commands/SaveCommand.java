package de.blockbuild.musikbot.commands;

import com.jagrosh.jdautilities.commandclient.CommandEvent;

import de.blockbuild.musikbot.Main;
import de.blockbuild.musikbot.core.GuildMusicManager;
import de.blockbuild.musikbot.core.MBCommand;

public class SaveCommand extends MBCommand {

	public SaveCommand(Main main) {
		super(main);
		this.name = "saveconfig";
		this.help = "Saves the configuration.";
		this.joinOnCommand = false;
		this.category = SETUP;
	}

	@Override
	protected void doCommand(CommandEvent event) {
		GuildMusicManager musicManager = main.getBot().getGuildAudioPlayer(event.getGuild());

		musicManager.config.writeConfig();
	}

}
