package de.blockbuild.musikbot.commands;

import de.blockbuild.musikbot.Bot;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Role;

public abstract class SetupCommand extends MBCommand {

	public SetupCommand(Bot bot) {
		super(bot);
		this.category = new Category("SETUP", event -> {

			if (event.isOwner()) {
				return true;
			} else if (event.getGuild() == null) {
				return true;
			} else if (event.getMember().hasPermission(Permission.MANAGE_SERVER)) {
				return true;
			}

			Role setup = bot.getGuildAudioPlayer(event.getGuild()).config.getSetupRole();

			if (setup == null || event.getMember().getRoles().contains(setup)) {
				return true;
			} else {
				event.reply(event.getClient().getWarning() + " You are not permitted to use this command");
				return false;
			}
		});
	}
}
