package de.blockbuild.musikbot.commands;

import com.jagrosh.jdautilities.command.CommandEvent;

import de.blockbuild.musikbot.Bot;
import de.blockbuild.musikbot.core.GuildMusicManager;
import de.blockbuild.musikbot.core.MBCommand;

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
	protected void doCommand(CommandEvent event) {
		GuildMusicManager musicManager = bot.getGuildAudioPlayer(event.getGuild());

		if (event.getArgs().startsWith("alone ")) {
			if (event.getArgs().substring(6).startsWith("enable")) {
				musicManager.config.setDisconnectIfAlone(true);

				StringBuilder builder = new StringBuilder().append(event.getClient().getSuccess());
				builder.append(" 'Auto_Disconnect_If_Alone' set to `Enabled`");
				event.reply(builder.toString());

			} else if (event.getArgs().substring(6).startsWith("disable")) {
				musicManager.config.setDisconnectIfAlone(false);

				StringBuilder builder = new StringBuilder().append(event.getClient().getSuccess());
				builder.append(" 'Auto_Disconnect_If_Alone' set to `Disabled`");
				event.reply(builder.toString());

			} else {
				sendCommandInfo(event);
			}

		} else if (event.getArgs().startsWith("trackend ")) {
			if (event.getArgs().substring(9).startsWith("enable")) {
				musicManager.config.setDisconnectAfterLastTrack(true);

				StringBuilder builder = new StringBuilder().append(event.getClient().getSuccess());
				builder.append(" 'Auto_Disconnect_After_Last_Track' set to `Enabled`");
				event.reply(builder.toString());

			} else if (event.getArgs().substring(9).startsWith("disable")) {
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

}
