package de.blockbuild.musikbot.core;

import com.jagrosh.jdautilities.commandclient.Command;
import com.jagrosh.jdautilities.commandclient.CommandEvent;

import de.blockbuild.musikbot.Bot;
import de.blockbuild.musikbot.Main;
import net.dv8tion.jda.core.Permission;

public abstract class MBCommand extends Command implements Comparable<Command> {

	protected static final long RESPONSE_DURATION = 5;

	protected Main main;

	public MBCommand(Main main) {
		this.main = main;
		this.guildOnly = true;
		// this.helpBiConsumer = (, c);
		this.botPermissions = RECOMMENDED_PERMS();
	}

	private Permission[] RECOMMENDED_PERMS() {
		return Bot.RECOMMENDED_PERMS;
	}

	@Override
	protected void execute(CommandEvent event) {
		// check permission?
		try {
			doCommand(event);
		} catch (Exception e) {
			System.err.print(e);
		}
	}

	protected abstract void doCommand(CommandEvent e);

	@Override
	public int compareTo(Command o) {
		return this.getName().compareTo(o.getName());
	}

}
