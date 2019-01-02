package de.blockbuild.musikbot.core;

import com.jagrosh.jdautilities.commandclient.Command;
import com.jagrosh.jdautilities.commandclient.CommandEvent;

import de.blockbuild.musikbot.Bot;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.VoiceChannel;

public abstract class MBCommand extends Command implements Comparable<Command> {

	protected final Category MUSIC = new Category("Music");
	protected final Category CONNECTION = new Category("Connection");
	protected final Category OTHER = new Category("Other");
	protected final Category SETUP = new Category("Setup");
	protected final Bot bot;
	protected Boolean joinOnCommand;

	public MBCommand(Bot bot) {
		this.bot = bot;
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
		GuildMusicManager musicManager = bot.getGuildAudioPlayer(event.getGuild());

		if (!event.isOwner() && musicManager.isBlockedUser(member.getUser().getIdLong())
				|| (!event.isOwner() && (musicManager.isWhitelistEnabled()
						&& !(musicManager.isWhitelistedUser(member.getUser().getIdLong()))))) {
			User owner = bot.getUserById(bot.config.ownerID);
			StringBuilder builder = new StringBuilder().append(event.getClient().getWarning());
			builder.append(" You're not allowed to interact with me!");
			if (!(owner == null)) {
				builder.append("\nFor additional info, contact ").append(owner.getName()).append("#")
						.append(owner.getDiscriminator());
			}
			event.reply(builder.toString());
			return;
		}

		if (this.getCategory().getName() == MUSIC.getName()) {
			if (!member.getVoiceState().inVoiceChannel()) {
				// Should not be triggered!
				return;
			}

			if (!selfMember.getVoiceState().inVoiceChannel()) {
				if (joinOnCommand) {
					bot.joinDiscordVoiceChannel(event.getGuild(), channel.getName());
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

	public Long getLong(String string, CommandEvent event) {
		Long l = null;
		try {
			l = Long.valueOf(string);
		} catch (Exception e) {
			// nothing
		} finally {
			if (l == null) {
				StringBuilder builder = new StringBuilder().append(event.getClient().getError());
				builder.append(" `").append(string).append("` isn't a vaild format.\n");
				builder.append(" !").append(this.name).append(" ").append(this.arguments);
				event.reply(builder.toString());
			}
		}
		return l;
	}
	
	public void sendCommandInfo(CommandEvent event) {
		StringBuilder builder = new StringBuilder().append(event.getClient().getWarning());
		builder.append(" !").append(this.name).append(" ").append(this.arguments);
		event.reply(builder.toString());
	}

	protected abstract void doCommand(CommandEvent event);

	@Override
	public int compareTo(Command o) {
		return this.getName().compareTo(o.getName());
	}

}
