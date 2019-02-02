package de.blockbuild.musikbot.commands;

import com.jagrosh.jdautilities.command.CommandEvent;

import de.blockbuild.musikbot.Bot;
import de.blockbuild.musikbot.core.MBCommand;

public class AutoConnectCommand extends MBCommand {

	public AutoConnectCommand(Bot bot) {
		super(bot);
		this.name = "autoconnect";
		this.help = "set's auto connect option's in config";
		this.arguments = "<enable|disable|channel|track> [voicechannelid|URL|clear]";
		this.joinOnCommand = false;
		this.category = SETUP;
	}

	@Override
	protected void doGuildCommand(CommandEvent event) {
		if (args.isEmpty()) {
			sendCommandInfo(event);
			return;
		}

		if (args.startsWith("enable")) {
			musicManager.config.setAutoConnectEnabled(true);

			StringBuilder builder = new StringBuilder().append(event.getClient().getSuccess());
			builder.append(" 'Auto_Connect_On_Startup' set to `Enabled`");
			event.reply(builder.toString());

		} else if (args.startsWith("disable")) {
			musicManager.config.setAutoConnectEnabled(false);

			StringBuilder builder = new StringBuilder().append(event.getClient().getSuccess());
			builder.append(" 'Auto_Connect_On_Startup' set to `Disabled`");
			event.reply(builder.toString());

		} else if (args.startsWith("channel ")) {
			if (args.substring(8).startsWith("clear")) {
				musicManager.config.setAutoConnectVoiceChannelId(0L);

				StringBuilder builder = new StringBuilder().append(event.getClient().getSuccess());
				builder.append(" 'Auto_Connect_On_Startup' VoiceChannelId removed");
				event.reply(builder.toString());
				return;
			}

			Long l = this.getLong(args.substring(8), event);
			if (l == null)
				return;

			musicManager.config.setAutoConnectVoiceChannelId(l);

			StringBuilder builder = new StringBuilder().append(event.getClient().getSuccess());
			builder.append(" 'Auto_Connect_On_Startup' set VoiceChannelId: `").append(l).append("`");
			event.reply(builder.toString());

		} else if (args.startsWith("track ")) {
			String x = args.substring(6);
			if (x.startsWith("clear")) {
				musicManager.config.setAutoConnectTrack("");

				StringBuilder builder = new StringBuilder().append(event.getClient().getSuccess());
				builder.append(" 'Auto_Connect_On_Startup' Track removed");
				event.reply(builder.toString());

			} else if (!x.startsWith("http")) {
				StringBuilder builder = new StringBuilder().append(event.getClient().getError());
				builder.append(" Track must be a URL.");
				event.reply(builder.toString());

				sendCommandInfo(event);
			} else {
				musicManager.config.setAutoConnectTrack(x);

				StringBuilder builder = new StringBuilder().append(event.getClient().getSuccess());
				builder.append(" 'Auto_Connect_On_Startup' set Track: `").append(x).append("`");
				event.reply(builder.toString());
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
