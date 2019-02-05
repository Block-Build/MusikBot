package de.blockbuild.musikbot.commands.setup;

import com.jagrosh.jdautilities.command.CommandEvent;

import de.blockbuild.musikbot.Bot;
import de.blockbuild.musikbot.commands.MBCommand;

public class AutoDisconnectCommand extends MBCommand {

	public AutoDisconnectCommand(Bot bot) {
		super(bot);
		this.name = "autodisconnect";
		this.help = "set's auto disconnect option in config";
		this.arguments = "<alone|trackend> <enable|disable>";
		this.joinOnCommand = false;
		this.category = SETUP;
	}

	@Override
	protected void doGuildCommand(CommandEvent event) {
		if (args.startsWith("alone ")) {
			if (args.substring(6).startsWith("enable")) {
				musicManager.config.setDisconnectIfAlone(true);

				StringBuilder builder = new StringBuilder().append(event.getClient().getSuccess());
				builder.append(" 'Auto_Disconnect_If_Alone' set to `Enabled`");
				event.reply(builder.toString());

			} else if (args.substring(6).startsWith("disable")) {
				musicManager.config.setDisconnectIfAlone(false);

				StringBuilder builder = new StringBuilder().append(event.getClient().getSuccess());
				builder.append(" 'Auto_Disconnect_If_Alone' set to `Disabled`");
				event.reply(builder.toString());

			} else {
				sendCommandInfo(event);
			}

		} else if (args.startsWith("trackend ")) {
			if (args.substring(9).startsWith("enable")) {
				musicManager.config.setDisconnectAfterLastTrack(true);

				StringBuilder builder = new StringBuilder().append(event.getClient().getSuccess());
				builder.append(" 'Auto_Disconnect_After_Last_Track' set to `Enabled`");
				event.reply(builder.toString());

			} else if (args.substring(9).startsWith("disable")) {
				musicManager.config.setDisconnectAfterLastTrack(false);

				StringBuilder builder = new StringBuilder().append(event.getClient().getSuccess());
				builder.append(" 'Auto_Disconnect_After_Last_Track' set to `Disabled`");
				event.reply(builder.toString());

			} else {
				sendCommandInfo(event);
			}
		} else {
			sendCommandInfo(event);
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
