package de.blockbuild.musikbot.commands.setup;

import com.jagrosh.jdautilities.command.CommandEvent;

import de.blockbuild.musikbot.Bot;
import de.blockbuild.musikbot.commands.SetupCommand;

public class ReloadCommand extends SetupCommand {

	public ReloadCommand(Bot bot) {
		super(bot);
		this.name = "reload";
		this.help = "Reload the Plugin.";
	}

	@Override
	protected void doGuildCommand(CommandEvent event) {
		StringBuilder builder = new StringBuilder().append(event.getClient().getSuccess());
		builder.append(" Reloading...");
		event.reply(builder.toString());

		bot.getMain().reload();
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
