package de.blockbuild.musikbot.commands.music;

import com.jagrosh.jdautilities.command.CommandEvent;

import de.blockbuild.musikbot.Bot;
import de.blockbuild.musikbot.commands.MusicCommand;

public class YTAutoPlayCommand extends MusicCommand {
	public YTAutoPlayCommand(Bot bot) {
		super(bot);
		this.name = "autoplay";
		this.aliases = new String[] { "ytautoplay", "ap", "yap" };
		this.help = "Enable or disable Youtube autoplay";
		this.arguments = "<Enable|Disable>";
		this.joinOnCommand = true;
	}

	@Override
	protected void doGuildCommand(CommandEvent event) {
		StringBuilder builder = new StringBuilder().append(event.getClient().getSuccess());

		if (args.equalsIgnoreCase("enable")) {
			musicManager.setIsAutoPlay(true);
			builder.append(" YouTube autoplay: `enabled`");
		} else if (args.equalsIgnoreCase("disable")) {
			musicManager.setIsAutoPlay(false);
			builder.append(" YouTube autoplay: `disabled`");
		} else {
			sendCommandInfo(event);
		}
		event.reply(builder.toString());
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
