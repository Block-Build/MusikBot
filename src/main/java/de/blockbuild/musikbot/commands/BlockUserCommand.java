package de.blockbuild.musikbot.commands;

import com.jagrosh.jdautilities.commandclient.CommandEvent;
import de.blockbuild.musikbot.Main;
import de.blockbuild.musikbot.core.GuildMusicManager;
import de.blockbuild.musikbot.core.MBCommand;

public class BlockUserCommand extends MBCommand {

	public BlockUserCommand(Main main) {
		super(main);
		this.name = "blockuser";
		this.help = "edits the blocked users list";
		this.arguments = "<add|remove|clear|list> [UserID]";
		this.joinOnCommand = false;
		this.category = SETUP;
	}

	@Override
	protected void doCommand(CommandEvent event) {
		GuildMusicManager musicManager = main.getBot().getGuildAudioPlayer(event.getGuild());

		if (!event.isOwner()) {
			StringBuilder builder = new StringBuilder().append(event.getClient().getWarning());
			builder.append(" Only the Owner is permitted to use this command");
			event.reply(builder.toString());
			return;
		}

		if (event.getArgs().isEmpty()) {
			StringBuilder builder = new StringBuilder().append(event.getClient().getWarning());
			builder.append(" ").append(this.arguments);
			event.reply(builder.toString());
			return;
		}

		if (event.getArgs().startsWith("add")) {
			Long l = this.getLong(event.getArgs().substring(4), event);
			if (l == null) return;

			musicManager.config.blockedUser.add(l);

			StringBuilder builder = new StringBuilder().append(event.getClient().getSuccess());
			builder.append(" Successfully added '").append(main.getBot().getUserByID(l)).append(" ").append(l)
					.append("' to blocked list.");
			event.reply(builder.toString());

		} else if (event.getArgs().startsWith("remove")) {
			Long l = this.getLong(event.getArgs().substring(7), event);
			if (l == null) return;

			if (!musicManager.isBlockedUser(l)) {
				StringBuilder builder = new StringBuilder().append(event.getClient().getWarning());
				builder.append(" ").append(l).append(" is not a blocked User.");
				event.reply(builder.toString());
				return;
			}

			musicManager.config.blockedUser.remove(l);

			StringBuilder builder = new StringBuilder().append(event.getClient().getSuccess());
			builder.append(" Successfully removed '").append(main.getBot().getUserByID(l)).append(" ").append(l)
					.append("' from blocked list.");
			event.reply(builder.toString());

		} else if (event.getArgs().startsWith("clear")) {
			musicManager.config.blockedUser.clear();

			StringBuilder builder = new StringBuilder().append(event.getClient().getSuccess());
			builder.append(" List of blocked users successfully cleard");
			event.reply(builder.toString());

		} else if (event.getArgs().startsWith("list")) {
			StringBuilder builder = new StringBuilder().append(event.getClient().getSuccess());
			builder.append(" **Blocked Users:**\n");
			for (Long l : musicManager.config.blockedUser) {
				builder.append("").append(main.getBot().getUserByID(l)).append("");
				builder.append(" `").append(l).append("`").append("\n");
			}
			event.reply(builder.toString());

		} else {
			StringBuilder builder = new StringBuilder().append(event.getClient().getWarning());
			builder.append(" ").append(this.arguments);
			event.reply(builder.toString());
		}
	}
}
