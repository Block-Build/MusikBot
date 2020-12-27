package de.blockbuild.musikbot.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import de.blockbuild.musikbot.Bot;
import de.blockbuild.musikbot.configuration.GuildConfiguration;
import de.blockbuild.musikbot.core.GuildMusicManager;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;

public abstract class MBCommand extends Command implements Comparable<Command> {

	protected final Category GENERAL = new Category("GENERAL");
	protected final Bot bot;
	protected Boolean joinOnCommand = false;

	protected User user;
	protected User selfUser;
	protected Member member;
	protected Member selfMember;
	protected VoiceChannel channel;
	protected VoiceChannel selfChannel;
	protected TextChannel textChannel;
	protected GuildMusicManager musicManager;
	protected Guild guild;
	protected String args;

	public MBCommand(Bot bot) {
		this.bot = bot;
		this.guildOnly = false;
		this.botPermissions = RECOMMENDED_PERMS();
		this.category = GENERAL;
	}

	private Permission[] RECOMMENDED_PERMS() {
		return Bot.BASIC_PERMS;
	}

	@Override
	protected void execute(CommandEvent event) {
		args = event.getArgs();
		user = event.getAuthor();
		selfUser = event.getSelfUser();

		if (event.getChannelType() == ChannelType.PRIVATE) {
			if (event.isOwner()) {
				if (!(args == null) && args.length() >= 18) {
					guild = null;
					guild = bot.getGuildById(args.substring(0, 18));
					if (!(guild == null)) {
						args = args.substring(18).trim();
						member = guild.getMember(user);
						selfMember = guild.getSelfMember();
						channel = member.getVoiceState().getChannel();
						selfChannel = selfMember.getVoiceState().getChannel();
						musicManager = bot.getGuildAudioPlayer(guild);

						doGuildCommand(event);
						return;
					}
				}
			}
			doPrivateCommand(event);
			return;
		}

		guild = event.getGuild();
		member = event.getMember();
		selfMember = event.getSelfMember();
		channel = member.getVoiceState().getChannel();
		selfChannel = selfMember.getVoiceState().getChannel();
		textChannel = event.getTextChannel();
		musicManager = bot.getGuildAudioPlayer(guild);

		if (musicManager.config.getMessageDeleteDelay() > 0) {
			musicManager.deleteMessageLater(event.getChannel(), event.getMessage(), musicManager.config.getMessageDeleteDelay());
		}

		if (musicManager.config.isDefaultTextChannelEnabled()) {
			if (!(textChannel.getIdLong() == musicManager.config.getDefaultTextChannel())
					&& !(musicManager.config.getDefaultTextChannel() == 0L)) {
				StringBuilder builder = new StringBuilder().append(event.getClient().getWarning());
				builder.append(" You are only allowd to use commands in `")
						.append(bot.getTextChannelById(musicManager.config.getDefaultTextChannel()).getName())
						.append("`");
				event.replyInDm(builder.toString());
				return;
			}
		}

		if (!event.isOwner() && musicManager.config.isBlockedUser(user.getIdLong())
				|| (!event.isOwner() && (musicManager.config.isWhitelistEnabled()
						&& !(musicManager.config.isWhitelistedUser(user.getIdLong()))))) {
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

		try {
			doGuildCommand(event);
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

	protected boolean allowedToJoinVoiceChannel(GuildConfiguration config, long id) {
		if (config.isDefaultVoiceChannelEnabled() && !(config.getDefaultVoiceChannel() == 0L)) {
			if (config.getDefaultVoiceChannel() == id) {
				return true;
			} else {
				return false;
			}
		} else {
			return true;
		}
	}

	protected boolean allowedToJoinVoiceChannel(GuildConfiguration config, String id) {
		if (config.isDefaultVoiceChannelEnabled() && !(config.getDefaultVoiceChannel() == 0L)) {
			if (String.valueOf(config.getDefaultVoiceChannel()) == id) {
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
		builder.append(" ").append(event.getClient().getPrefix()).append(this.name).append(" ").append(this.arguments);
		event.reply(builder.toString());
	}

	public void sendDefaultVoiceChannelInfo(CommandEvent event, GuildMusicManager musicManager) {
		StringBuilder builder = new StringBuilder().append(event.getClient().getWarning());
		builder.append(" Default VoiceChannel is active. I'm only allowed to join `")
				.append(bot.getVoiceChannelById(musicManager.config.getDefaultVoiceChannel()).getName()).append("`");
		event.reply(builder.toString());
	}

	protected abstract void doGuildCommand(CommandEvent event);

	protected abstract void doPrivateCommand(CommandEvent event);

	@Override
	public int compareTo(Command o) {
		return this.getName().compareTo(o.getName());
	}

}
