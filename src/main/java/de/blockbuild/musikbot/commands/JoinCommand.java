package de.blockbuild.musikbot.commands;

import com.jagrosh.jdautilities.commandclient.CommandEvent;

import de.blockbuild.musikbot.Bot;
import de.blockbuild.musikbot.core.GuildMusicManager;
import de.blockbuild.musikbot.core.MBCommand;

import net.dv8tion.jda.core.entities.VoiceChannel;

public class JoinCommand extends MBCommand {

	public JoinCommand(Bot bot) {
		super(bot);
		this.name = "join";
		this.help = "Triggers the Bot to join a voice channel!";
		this.arguments = "[ChannelName]";
		this.joinOnCommand = false;
		this.category = CONNECTION;
	}

	@Override
	protected void doCommand(CommandEvent event) {
		GuildMusicManager musikManager = bot.getGuildAudioPlayer(event.getGuild());
		VoiceChannel channel = event.getMember().getVoiceState().getChannel();

		if (event.getArgs().isEmpty()) {
			if (event.getMember().getVoiceState().getChannel()
					.equals((event.getSelfMember().getVoiceState().getChannel()))) {
				StringBuilder builder = new StringBuilder(event.getClient().getWarning());
				builder.append(" We are already in the same channel!");
				event.reply(builder.toString());
			} else {
				if (allowedToJoinVoiceChannel(musikManager, channel.getIdLong())) {
					bot.joinDiscordVoiceChannel(event.getGuild(), channel.getIdLong());
				} else {
					sendDefaultVoiceChannelInfo(event, musikManager);
				}
			}
		} else {
			if (allowedToJoinVoiceChannel(musikManager, event.getArgs())) {
				bot.joinDiscordVoiceChannel(event.getGuild(), event.getArgs());
			} else {
				sendDefaultVoiceChannelInfo(event, musikManager);
			}
		}
	}
}
