package de.blockbuild.musikbot.commands;

import com.jagrosh.jdautilities.commandclient.CommandEvent;
import de.blockbuild.musikbot.Main;
import de.blockbuild.musikbot.core.GuildMusicManager;
import de.blockbuild.musikbot.core.MBCommand;

public class WhitelistCommand extends MBCommand {

	public WhitelistCommand(Main main) {
		super(main);
		this.name = "whitelist";
		this.help = "edits the whitelist";
		this.arguments = "<add|remove|clear|list|enable|disable> [UserID]";
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
			builder.append(" !").append(this.name).append(" ").append(this.arguments);
			event.reply(builder.toString());
			return;
		}

		if (event.getArgs().startsWith("add ")) {
			Long l = this.getLong(event.getArgs().substring(4), event);
			if (l == null)
				return;

			musicManager.config.whitelist.add(l);

			StringBuilder builder = new StringBuilder().append(event.getClient().getSuccess());
			builder.append(" Successfully added '").append(main.getBot().getUserNameById(l)).append(" ").append(l)
					.append("' to whitelist.");
			event.reply(builder.toString());

		} else if (event.getArgs().startsWith("remove ")) {
			Long l = this.getLong(event.getArgs().substring(7), event);
			if (l == null)
				return;

			if (!musicManager.isBlockedUser(l)) {
				StringBuilder builder = new StringBuilder().append(event.getClient().getWarning());
				builder.append(" ").append(l).append(" is not whitelisted.");
				event.reply(builder.toString());
				return;
			}

			musicManager.config.whitelist.remove(l);

			StringBuilder builder = new StringBuilder().append(event.getClient().getSuccess());
			builder.append(" Successfully removed '").append(main.getBot().getUserNameById(l)).append(" ").append(l)
					.append("' from whitelist.");
			event.reply(builder.toString());

		} else if (event.getArgs().startsWith("clear")) {
			musicManager.config.whitelist.clear();

			StringBuilder builder = new StringBuilder().append(event.getClient().getSuccess());
			builder.append(" Whitelist successfully cleard");
			event.reply(builder.toString());

		} else if (event.getArgs().startsWith("list")) {
			StringBuilder builder = new StringBuilder().append(event.getClient().getSuccess());
			builder.append(" **Whitelist:**\n");
			for (Long l : musicManager.config.whitelist) {
				builder.append("").append(main.getBot().getUserNameById(l)).append(" `").append(l).append("`")
						.append("\n");
			}
			event.reply(builder.toString());

		} else if (event.getArgs().startsWith("enable")) {
			musicManager.setWhitelistEnabled(true);

			StringBuilder builder = new StringBuilder().append(event.getClient().getSuccess());
			builder.append(" Whitelist now is Enabled");
			event.reply(builder.toString());

		} else if (event.getArgs().startsWith("disable")) {
			musicManager.setWhitelistEnabled(false);

			StringBuilder builder = new StringBuilder().append(event.getClient().getSuccess());
			builder.append(" Whitelist now is Disabled");
			event.reply(builder.toString());

		} else {
			StringBuilder builder = new StringBuilder().append(event.getClient().getWarning());
			builder.append(" !").append(this.name).append(" ").append(this.arguments);
			event.reply(builder.toString());
		}
	}
}
