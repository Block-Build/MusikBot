package de.blockbuild.musikbot.commands;

import com.jagrosh.jdautilities.command.CommandEvent;

import de.blockbuild.musikbot.Bot;
import de.blockbuild.musikbot.core.GuildMusicManager;
import de.blockbuild.musikbot.core.MBCommand;

public class WhitelistCommand extends MBCommand {

	public WhitelistCommand(Bot bot) {
		super(bot);
		this.name = "whitelist";
		this.help = "edits the whitelist";
		this.arguments = "<add|remove|clear|list|enable|disable> [UserID]";
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

		if (event.getArgs().startsWith("add ")) {
			Long l = this.getLong(event.getArgs().substring(4), event);
			if (l == null)
				return;

			musicManager.config.whitelistAdd(l);

			StringBuilder builder = new StringBuilder().append(event.getClient().getSuccess());
			builder.append(" Successfully added '").append(bot.getUserNameById(l)).append(" ").append(l)
					.append("' to whitelist.");
			event.reply(builder.toString());

		} else if (event.getArgs().startsWith("remove ")) {
			Long l = this.getLong(event.getArgs().substring(7), event);
			if (l == null)
				return;

			if (!musicManager.config.isWhitelistedUser(l)) {
				StringBuilder builder = new StringBuilder().append(event.getClient().getWarning());
				builder.append(" ").append(l).append(" is not whitelisted.");
				event.reply(builder.toString());
				return;
			}

			musicManager.config.whitelistRemove(l);

			StringBuilder builder = new StringBuilder().append(event.getClient().getSuccess());
			builder.append(" Successfully removed '").append(bot.getUserNameById(l)).append(" ").append(l)
					.append("' from whitelist.");
			event.reply(builder.toString());

		} else if (event.getArgs().startsWith("clear")) {
			musicManager.config.whitelistClear();

			StringBuilder builder = new StringBuilder().append(event.getClient().getSuccess());
			builder.append(" Whitelist successfully cleard");
			event.reply(builder.toString());

		} else if (event.getArgs().startsWith("list")) {
			StringBuilder builder = new StringBuilder().append(event.getClient().getSuccess());
			builder.append(" **Whitelist:**\n");
			for (Long l : musicManager.config.getWhitelist()) {
				builder.append("").append(bot.getUserNameById(l)).append(" `").append(l).append("`\n");
			}
			event.reply(builder.toString());

		} else if (event.getArgs().startsWith("enable")) {
			musicManager.config.setWhitelistEnabled(true);

			StringBuilder builder = new StringBuilder().append(event.getClient().getSuccess());
			builder.append(" Whitelist now is `Enabled`");
			event.reply(builder.toString());

		} else if (event.getArgs().startsWith("disable")) {
			musicManager.config.setWhitelistEnabled(false);

			StringBuilder builder = new StringBuilder().append(event.getClient().getSuccess());
			builder.append(" Whitelist now is `Disabled`");
			event.reply(builder.toString());
		} else {
			sendCommandInfo(event);
		}
	}
}
