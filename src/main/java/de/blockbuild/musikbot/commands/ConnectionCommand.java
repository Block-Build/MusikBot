package de.blockbuild.musikbot.commands;

import de.blockbuild.musikbot.Bot;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Role;

public abstract class ConnectionCommand extends MBCommand {

	public ConnectionCommand(Bot bot) {
		super(bot);
		this.category = new Category("CONNECTION", event -> {
			if (event.getGuild() == null) {
				return true;
			}

			Role setup = event.getGuild().getRolesByName("load from config", true).isEmpty() ? null
					: event.getGuild().getRolesByName("load from config", true).get(0);

			if (event.isOwner() || event.getMember().hasPermission(Permission.MANAGE_SERVER)
					|| !(setup == null) && event.getMember().getRoles().contains(setup)) {

				if (!event.getSelfMember().getVoiceState().inVoiceChannel()) {
					return true;
				}

				if (!event.getMember().getVoiceState().getChannel()
						.equals(event.getSelfMember().getVoiceState().getChannel())) {
					// in different channels
					StringBuilder builder = new StringBuilder(event.getClient().getWarning());
					builder.append(" You must be in the same channel as me to use that command!");
					event.reply(builder.toString());
					return false;
				}
				return true;
			} else {
				event.reply(event.getClient().getWarning() + " You are not permitted to use this command");
				return false;
			}
		});
	}
}
