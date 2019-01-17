package de.blockbuild.musikbot.commands;

import com.jagrosh.jdautilities.commandclient.CommandEvent;

import de.blockbuild.musikbot.Bot;
import de.blockbuild.musikbot.core.GuildMusicManager;
import de.blockbuild.musikbot.core.MBCommand;

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
	protected void doCommand(CommandEvent event) {
		GuildMusicManager musicManager = bot.getGuildAudioPlayer(event.getGuild());

		if (event.getArgs().isEmpty()) {
			sendCommandInfo(event);
			return;
		}

		if (event.getArgs().startsWith("enable")) {
			musicManager.config.setDefaultVoiceChannelEnabled(true);

			StringBuilder builder = new StringBuilder().append(event.getClient().getSuccess());
			builder.append(" 'Default_VoiceChannel' set to `Enabled`");
			event.reply(builder.toString());

		} else if (event.getArgs().startsWith("disable")) {
			musicManager.config.setDefaultVoiceChannelEnabled(false);

			StringBuilder builder = new StringBuilder().append(event.getClient().getSuccess());
			builder.append(" 'Default_VoiceChannel' set to `Disabled`");
			event.reply(builder.toString());

		} else if (event.getArgs().startsWith("channel ")) {
			if (event.getArgs().substring(8).startsWith("clear")) {
				musicManager.config.setDefaultVoiceChannel(0L);

				StringBuilder builder = new StringBuilder().append(event.getClient().getSuccess());
				builder.append(" 'Default_VoiceChannel' VoiceChannelId removed");
				event.reply(builder.toString());
				return;
			}
			Long l = this.getLong(event.getArgs().substring(8), event);
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
}
