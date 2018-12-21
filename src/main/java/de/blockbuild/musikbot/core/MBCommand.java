package de.blockbuild.musikbot.core;

import com.jagrosh.jdautilities.commandclient.Command;
import com.jagrosh.jdautilities.commandclient.CommandEvent;

import de.blockbuild.musikbot.Bot;
import de.blockbuild.musikbot.Main;
import net.dv8tion.jda.core.Permission;

public abstract class MBCommand extends Command implements Comparable<Command> {

	protected Main main;
	protected Boolean joinOnCommand;

	public MBCommand(Main main) {
		this.main = main;
		this.guildOnly = true;
		this.botPermissions = RECOMMENDED_PERMS();
	}

	public final Category MUSIC = new Category("Music");
	public final Category CONNECTION = new Category("Connection");
	public final Category OTHER = new Category("Other");

	private Permission[] RECOMMENDED_PERMS() {
		return Bot.RECOMMENDED_PERMS;
	}

	@Override
	protected void execute(CommandEvent event) {
		if (this.getCategory().getName() == MUSIC.getName()) {
			if (!event.getMember().getVoiceState().inVoiceChannel()) {
				// Should not be triggered!
				return;
			}
			if (!event.getSelfMember().getVoiceState().inVoiceChannel()) {
				if (joinOnCommand) {
					main.getBot().joinDiscordVoiceChannel(event.getJDA(),
							event.getMember().getVoiceState().getChannel().getName());
					doCommand(event);
					return;
				} else {
					StringBuilder builder = new StringBuilder(event.getClient().getWarning());
					builder.append(" Use `!Join [Channel]` to let me join a channel");
					event.reply(builder.toString());
					return;
				}
			}
			if (!event.getMember().getVoiceState().getChannel()
					.equals((event.getSelfMember().getVoiceState().getChannel()))) {
				// in different channels
				StringBuilder builder = new StringBuilder(event.getClient().getWarning());
				builder.append(" You must be in the same channel as me to use that command!");
				event.reply(builder.toString());
				doCommand(event);
				return;
			}
		}
		if (this.getCategory().getName() == CONNECTION.getName()) {
			if(this.guildOnly == false) {
				doCommand(event);
				return;
			}
			if (event.getMember().getVoiceState().inVoiceChannel()) {
				doCommand(event);
				return;
			}else {
				return;
			}
		}
		try

		{
			doCommand(event);
		} catch (Exception e) {
			System.err.print(e);
		}
	}

	protected abstract void doCommand(CommandEvent event);

	@Override
	public int compareTo(Command o) {
		return this.getName().compareTo(o.getName());
	}

}
