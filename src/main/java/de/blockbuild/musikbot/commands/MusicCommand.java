package de.blockbuild.musikbot.commands;

import de.blockbuild.musikbot.Bot;
import de.blockbuild.musikbot.core.GuildMusicManager;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.VoiceChannel;

public abstract class MusicCommand extends MBCommand {

	public MusicCommand(Bot bot) {
		super(bot);
		this.category = new Category("MUSIC", event -> {
			if (event.getGuild() == null) {
				return true;
			}

			Role music = bot.getGuildAudioPlayer(event.getGuild()).config.getMusicRole();

			if (event.isOwner() || event.getMember().hasPermission(Permission.MANAGE_SERVER) || music == null
					|| event.getMember().getRoles().contains(music)) {

				VoiceChannel channel = event.getMember().getVoiceState().getChannel();
				GuildMusicManager musicManager = bot.getGuildAudioPlayer(event.getGuild());

				if (!event.getSelfMember().getVoiceState().inVoiceChannel()) {
					if (joinOnCommand) {
						if (allowedToJoinVoiceChannel(musicManager.config, channel.getIdLong())) {
							bot.joinDiscordVoiceChannel(event.getGuild(), channel.getIdLong());
							return true;
						} else {
							sendDefaultVoiceChannelInfo(event, musicManager);
							return false;
						}
					} else {
						StringBuilder builder = new StringBuilder(event.getClient().getWarning());
						builder.append(" Use `!Join [Channel]` to let me join a channel");
						event.reply(builder.toString());
						return false;
					}
				}

				if (!channel.equals(event.getSelfMember().getVoiceState().getChannel())) {
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
