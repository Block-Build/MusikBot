package de.blockbuild.musikbot.commands.setup;

import com.jagrosh.jdautilities.command.CommandEvent;

import de.blockbuild.musikbot.Bot;
import de.blockbuild.musikbot.commands.SetupCommand;

public class BlacklistCommand extends SetupCommand {

	public BlacklistCommand(Bot bot) {
		super(bot);
		this.name = "blacklist";
		this.help = "edits the blocked users list";
		this.arguments = "<add|remove|clear|list> [UserID]";
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

			musicManager.config.blacklistAdd(l);

			StringBuilder builder = new StringBuilder().append(event.getClient().getSuccess());
			builder.append(" Successfully added '").append(bot.getUserNameById(l)).append(" ").append(l)
					.append("' to blacklist.");
			event.reply(builder.toString());

		} else if (args.startsWith("remove ")) {
			Long l = this.getLong(args.substring(7), event);
			if (l == null)
				return;

			if (!musicManager.config.isBlockedUser(l)) {
				StringBuilder builder = new StringBuilder().append(event.getClient().getWarning());
				builder.append(" ").append(l).append(" is not blocked.");
				event.reply(builder.toString());
				return;
			}

			musicManager.config.blacklistRemove(l);

			StringBuilder builder = new StringBuilder().append(event.getClient().getSuccess());
			builder.append(" Successfully removed '").append(bot.getUserNameById(l)).append(" ").append(l)
					.append("' from blacklist.");
			event.reply(builder.toString());

		} else if (args.startsWith("clear")) {
			musicManager.config.blacklistClear();

			StringBuilder builder = new StringBuilder().append(event.getClient().getSuccess());
			builder.append(" Blacklist successfully cleard");
			event.reply(builder.toString());

		} else if (args.startsWith("list")) {
			StringBuilder builder = new StringBuilder().append(event.getClient().getSuccess());
			builder.append(" **Blacklist:**\n");
			for (Long l : musicManager.config.getBlacklist()) {
				builder.append("").append(bot.getUserNameById(l)).append(" `").append(l).append("`").append("\n");
			}
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
