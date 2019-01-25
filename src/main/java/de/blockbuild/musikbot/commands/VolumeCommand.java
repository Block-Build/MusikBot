package de.blockbuild.musikbot.commands;

import com.jagrosh.jdautilities.command.CommandEvent;

import de.blockbuild.musikbot.Bot;
import de.blockbuild.musikbot.core.GuildMusicManager;
import de.blockbuild.musikbot.core.MBCommand;

public class VolumeCommand extends MBCommand {

	public VolumeCommand(Bot bot) {
		super(bot);
		this.name = "volume";
		this.aliases = new String[] { "vol", "v" };
		this.help = "Changes the volume.";
		this.arguments = "[0-100]";
		this.joinOnCommand = false;
		this.category = MUSIC;
	}

	@Override
	protected void doCommand(CommandEvent event) {
		GuildMusicManager musicManager = bot.getGuildAudioPlayer(event.getGuild());

		if (event.getArgs().isEmpty()) {
			StringBuilder builder = new StringBuilder(event.getClient().getSuccess());
			builder.append(" Current volume is `").append(musicManager.getVolume()).append("`.");
			event.reply(builder.toString());
		} else {
			int volume;
			try {
				volume = Integer.parseInt(event.getArgs());
			} catch (NumberFormatException e) {
				volume = -1;
			}
			if (volume < 0 || volume > 100) {
				StringBuilder builder = new StringBuilder(event.getClient().getError());
				builder.append(" Volume must be a valid integer between `0` and `100`!");
				event.reply(builder.toString());
			} else {
				musicManager.setVolume(volume);
				StringBuilder builder = new StringBuilder(event.getClient().getSuccess());
				builder.append(" Volume now set to `").append(volume).append("`.");
				event.reply(builder.toString());
			}
		}
	}
}