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

		if (musicManager.config.isDefaultTextChannelEnabled()) {
			if (!(event.getTextChannel().getIdLong() == musicManager.config.getDefaultTextChannel())
					&& !(musicManager.config.getDefaultTextChannel() == 0L)) {
				return;
			}
		}

		if (!event.isOwner() && musicManager.config.isBlockedUser(member.getUser().getIdLong())
				|| (!event.isOwner() && (musicManager.config.isWhitelistEnabled()
						&& !(musicManager.config.isWhitelistedUser(member.getUser().getIdLong()))))) {
			User owner = bot.getUserById(bot.config.getOwnerID());
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
					if (allowedToJoinVoiceChannel(musicManager, channel.getIdLong())) {
						bot.joinDiscordVoiceChannel(event.getGuild(), channel.getIdLong());
						doCommand(event);
					} else {
						sendDefaultVoiceChannelInfo(event, musicManager);
					}
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

		if (this.getCategory().getName() == SETUP.getName()) {
			if (!event.isOwner()) {
				StringBuilder builder = new StringBuilder().append(event.getClient().getWarning());
				builder.append(" Only the Owner is permitted to use this command");
				event.reply(builder.toString());
			} else {
				doCommand(event);
			}
			return;
		}

		try {
			doCommand(event);
		} catch (Exception e) {
			e.printStackTrace();
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

	protected boolean allowedToJoinVoiceChannel(GuildMusicManager musicManager, long id) {
		if (musicManager.config.isDefaultVoiceChannelEnabled()
				&& !(musicManager.config.getDefaultVoiceChannel() == 0L)) {
			if (musicManager.config.getDefaultVoiceChannel() == id) {
				return true;
			} else {
				return false;
			}
		} else {
			return true;
		}
	}

	protected boolean allowedToJoinVoiceChannel(GuildMusicManager musicManager, String id) {
		if (musicManager.config.isDefaultVoiceChannelEnabled()
				&& !(musicManager.config.getDefaultVoiceChannel() == 0L)) {
			if (String.valueOf(musicManager.config.getDefaultVoiceChannel()) == id) {
				return true;
			} else {
				return false;
			}
		} else {
			return true;
		}
	}

	public void sendCommandInfo(CommandEvent event) {
		StringBuilder builder = new StringBuilder().append(event.getClient().getWarning());
		builder.append(" !").append(this.name).append(" ").append(this.arguments);
		event.reply(builder.toString());
	}

	public void sendDefaultVoiceChannelInfo(CommandEvent event, GuildMusicManager musicManager) {
		StringBuilder builder = new StringBuilder().append(event.getClient().getWarning());
		builder.append(" Default VoiceChannel is active. I'm only allowed to join `")
				.append(bot.getVoiceChannelById(musicManager.config.getDefaultVoiceChannel()).getName()).append("`");
		event.reply(builder.toString());
	}

	protected abstract void doCommand(CommandEvent event);

	@Override
	public int compareTo(Command o) {
		return this.getName().compareTo(o.getName());
	}

}
