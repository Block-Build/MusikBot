package de.blockbuild.musikbot.commands;

import com.jagrosh.jdautilities.command.CommandEvent;

import de.blockbuild.musikbot.Bot;
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
	protected void doGuildCommand(CommandEvent event) {
		if (args.isEmpty()) {
			StringBuilder builder = new StringBuilder(event.getClient().getSuccess());
			builder.append(" Current volume is `").append(musicManager.getVolume()).append("`.");
			event.reply(builder.toString());
		} else {
			int volume;
			try {
				volume = Integer.parseInt(args);
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

	@Override
	protected void doPrivateCommand(CommandEvent event) {
		event.reply(event.getClient().getError() + " This command cannot be used in Direct messages.");

		StringBuilder builder = new StringBuilder().append(event.getClient().getSuccess());

		builder.append(" **MusikBot** ").append("by Block-Build\n");
		builder.append("SpigotMC: `https://www.spigotmc.org/resources/the-discord-musikbot-on-minecraft.64277/`\n");
		builder.append("GitHub: `https://github.com/Block-Build/MusikBot`\n");
		builder.append("Version: `").append(bot.getMain().getDescription().getVersion()).append("`\n");
		builder.append("Do you have any problem or suggestion? Open an Issue on GitHub.");

		event.reply(builder.toString());
	}
}