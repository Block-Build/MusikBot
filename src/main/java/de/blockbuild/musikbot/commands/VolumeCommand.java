package de.blockbuild.musikbot.commands;

import com.jagrosh.jdautilities.commandclient.CommandEvent;

import de.blockbuild.musikbot.Main;
import de.blockbuild.musikbot.core.AudioPlayerSendHandler;
import de.blockbuild.musikbot.core.MBCommand;

public class VolumeCommand extends MBCommand {

	public VolumeCommand(Main main) {
		super(main);
		this.name = "volume";
		this.aliases = new String[] { "vol", "v" };
		this.help = "Change the volume";
	}

	@Override
	protected void doCommand(CommandEvent event) {
		AudioPlayerSendHandler apsh = (AudioPlayerSendHandler) event.getGuild().getAudioManager().getSendingHandler();
		if (event.getArgs().isEmpty()) {
			event.reply("Current volume is '" + apsh.getPlayer().getVolume() + "'");
		} else {
			int volume;
			try {
				volume = Integer.parseInt(event.getArgs());
			} catch (NumberFormatException e) {
				volume = -1;
				System.err.println(e);
			}
			if (volume < 0 || volume > 100) {
				event.reply(event.getClient().getError() + "Volume must be a valid integer between 0 and 150!");
			} else {
				apsh.getPlayer().setVolume(volume);
				event.reply("Volume now set to '" + volume + "'");
			}
		}
	}
}
