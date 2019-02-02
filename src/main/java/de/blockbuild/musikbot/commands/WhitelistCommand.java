package de.blockbuild.musikbot.commands;

import com.jagrosh.jdautilities.command.CommandEvent;

import de.blockbuild.musikbot.Bot;
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
	protected void doGuildCommand(CommandEvent event) {
		if (args.isEmpty()) {
			sendCommandInfo(event);
			return;
		}

		if (args.startsWith("add ")) {
			Long l = this.getLong(args.substring(4), event);
			if (l == null)
				return;

			musicManager.config.whitelistAdd(l);

			StringBuilder builder = new StringBuilder().append(event.getClient().getSuccess());
			builder.append(" Successfully added '").append(bot.getUserNameById(l)).append(" ").append(l)
					.append("' to whitelist.");
			event.reply(builder.toString());

		} else if (args.startsWith("remove ")) {
			Long l = this.getLong(args.substring(7), event);
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

		} else if (args.startsWith("clear")) {
			musicManager.config.whitelistClear();

			StringBuilder builder = new StringBuilder().append(event.getClient().getSuccess());
			builder.append(" Whitelist successfully cleard");
			event.reply(builder.toString());

		} else if (args.startsWith("list")) {
			StringBuilder builder = new StringBuilder().append(event.getClient().getSuccess());
			builder.append(" **Whitelist:**\n");
			for (Long l : musicManager.config.getWhitelist()) {
				builder.append("").append(bot.getUserNameById(l)).append(" `").append(l).append("`\n");
			}
			event.reply(builder.toString());

		} else if (args.startsWith("enable")) {
			musicManager.config.setWhitelistEnabled(true);

			StringBuilder builder = new StringBuilder().append(event.getClient().getSuccess());
			builder.append(" Whitelist now is `Enabled`");
			event.reply(builder.toString());

		} else if (args.startsWith("disable")) {
			musicManager.config.setWhitelistEnabled(false);

			StringBuilder builder = new StringBuilder().append(event.getClient().getSuccess());
			builder.append(" Whitelist now is `Disabled`");
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
