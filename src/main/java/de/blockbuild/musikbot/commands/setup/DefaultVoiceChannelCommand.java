package de.blockbuild.musikbot.commands.setup;

import com.jagrosh.jdautilities.command.CommandEvent;

import de.blockbuild.musikbot.Bot;
import de.blockbuild.musikbot.commands.MBCommand;

public class DefaultVoiceChannelCommand extends MBCommand {

	public DefaultVoiceChannelCommand(Bot bot) {
		super(bot);
		this.name = "defaultvoicechannel";
		this.help = "set's the `Default_VoiceChannel` option's in config";
		this.arguments = "<enable|disable|channel> [textchannelid|clear]";
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
			musicManager.config.setDefaultVoiceChannelEnabled(true);

			StringBuilder builder = new StringBuilder().append(event.getClient().getSuccess());
			builder.append(" 'Default_VoiceChannel' set to `Enabled`");
			event.reply(builder.toString());

		} else if (args.startsWith("disable")) {
			musicManager.config.setDefaultVoiceChannelEnabled(false);

			StringBuilder builder = new StringBuilder().append(event.getClient().getSuccess());
			builder.append(" 'Default_VoiceChannel' set to `Disabled`");
			event.reply(builder.toString());

		} else if (args.startsWith("channel ")) {
			if (args.substring(8).startsWith("clear")) {
				musicManager.config.setDefaultVoiceChannel(0L);

				StringBuilder builder = new StringBuilder().append(event.getClient().getSuccess());
				builder.append(" 'Default_VoiceChannel' VoiceChannelId removed");
				event.reply(builder.toString());
				return;
			}
			Long l = this.getLong(args.substring(8), event);
			if (l == null)
				return;

			musicManager.config.setDefaultVoiceChannel(l);

			StringBuilder builder = new StringBuilder().append(event.getClient().getSuccess());
			builder.append(" 'Default_VoiceChannel' set VoiceChannelId: `").append(l).append("`");
			event.reply(builder.toString());

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
