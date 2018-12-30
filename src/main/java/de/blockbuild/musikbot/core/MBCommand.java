package de.blockbuild.musikbot.core;

import com.jagrosh.jdautilities.commandclient.Command;
import com.jagrosh.jdautilities.commandclient.CommandEvent;

import de.blockbuild.musikbot.Bot;
import de.blockbuild.musikbot.Main;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.VoiceChannel;

public abstract class MBCommand extends Command implements Comparable<Command> {

	protected final Category MUSIC = new Category("Music");
	protected final Category CONNECTION = new Category("Connection");
	protected final Category OTHER = new Category("Other");
	protected final Main main;
	protected Boolean joinOnCommand;

	public MBCommand(Main main) {
		this.main = main;
		this.guildOnly = true;
		this.botPermissions = RECOMMENDED_PERMS();
	}

	private Permission[] RECOMMENDED_PERMS() {
		return Bot.RECOMMENDED_PERMS;
	}

	@Override
	protected void execute(CommandEvent event) {
		Member member = event.getMember();
		Member selfMember = event.getSelfMember();
		VoiceChannel channel = member.getVoiceState().getChannel();
		VoiceChannel selfChannel = selfMember.getVoiceState().getChannel();

		if (this.getCategory().getName() == MUSIC.getName()) {
			if (!member.getVoiceState().inVoiceChannel()) {
				// Should not be triggered!
				return;
			}

			if (!selfMember.getVoiceState().inVoiceChannel()) {
				if (joinOnCommand) {
					main.getBot().joinDiscordVoiceChannel(event.getGuild(), channel.getName());
					doCommand(event);
					return;
				} else {
					StringBuilder builder = new StringBuilder(event.getClient().getWarning());
					builder.append(" Use `!Join [Channel]` to let me join a channel");
					event.reply(builder.toString());
					return;
				}
			}

			if (!channel.equals(selfChannel)) {
				// in different channels
				StringBuilder builder = new StringBuilder(event.getClient().getWarning());
				builder.append(" You must be in the same channel as me to use that command!");
				event.reply(builder.toString());
				return;
			}
		}

		if (this.getCategory().getName() == CONNECTION.getName()) {
			if (this.guildOnly == false) {
				doCommand(event);
				return;
			}

			if (!member.getVoiceState().inVoiceChannel()) {
				// Should not be triggered!
				return;
			}

			if (!selfMember.getVoiceState().inVoiceChannel()) {
				doCommand(event);
				return;
			}

			if (!event.getMember().getVoiceState().getChannel()
			if (!channel.equals(selfChannel)) {
				// in different channels
				StringBuilder builder = new StringBuilder(event.getClient().getWarning());
				builder.append(" You must be in the same channel as me to use that command!");
				event.reply(builder.toString());
				return;
			}
		}

		try {
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
