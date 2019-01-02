package de.blockbuild.musikbot.commands;

import com.jagrosh.jdautilities.commandclient.CommandEvent;

import de.blockbuild.musikbot.Bot;
import de.blockbuild.musikbot.core.GuildMusicManager;
import de.blockbuild.musikbot.core.MBCommand;

public class BlacklistCommand extends MBCommand {

	public BlacklistCommand(Bot bot) {
		super(bot);
		this.name = "blacklist";
		this.help = "edits the blocked users list";
		this.arguments = "<add|remove|clear|list> [UserID]";
		this.joinOnCommand = false;
		this.category = SETUP;
	}

	@Override
	protected void doCommand(CommandEvent event) {
		GuildMusicManager musicManager = bot.getGuildAudioPlayer(event.getGuild());

		if (!event.isOwner()) {
			StringBuilder builder = new StringBuilder().append(event.getClient().getWarning());
			builder.append(" Only the Owner is permitted to use this command");
			event.reply(builder.toString());
			return;
		}

		if (event.getArgs().isEmpty()) {
			sendCommandInfo(event);
			return;
		}

		if (event.getArgs().startsWith("add ")) {
			Long l = this.getLong(event.getArgs().substring(4), event);
			if (l == null)
				return;

			musicManager.config.blacklist.add(l);

			StringBuilder builder = new StringBuilder().append(event.getClient().getSuccess());
			builder.append(" Successfully added '").append(bot.getUserNameById(l)).append(" ").append(l)
					.append("' to blacklist.");
			event.reply(builder.toString());

		} else if (event.getArgs().startsWith("remove ")) {
			Long l = this.getLong(event.getArgs().substring(7), event);
			if (l == null)
				return;

			if (!musicManager.isBlockedUser(l)) {
				StringBuilder builder = new StringBuilder().append(event.getClient().getWarning());
				builder.append(" ").append(l).append(" is not blocked.");
				event.reply(builder.toString());
				return;
			}

			musicManager.config.blacklist.remove(l);

			StringBuilder builder = new StringBuilder().append(event.getClient().getSuccess());
			builder.append(" Successfully removed '").append(bot.getUserNameById(l)).append(" ").append(l)
					.append("' from blacklist.");
			event.reply(builder.toString());

		} else if (event.getArgs().startsWith("clear")) {
			musicManager.config.blacklist.clear();

			StringBuilder builder = new StringBuilder().append(event.getClient().getSuccess());
			builder.append(" Blacklist successfully cleard");
			event.reply(builder.toString());

		} else if (event.getArgs().startsWith("list")) {
			StringBuilder builder = new StringBuilder().append(event.getClient().getSuccess());
			builder.append(" **Blacklist:**\n");
			for (Long l : musicManager.config.blacklist) {
				builder.append("").append(bot.getUserNameById(l)).append(" `").append(l).append("`")
						.append("\n");
			}
			event.reply(builder.toString());

		} else {
			sendCommandInfo(event);
		}
	}
}
