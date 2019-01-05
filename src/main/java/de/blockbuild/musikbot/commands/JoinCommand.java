package de.blockbuild.musikbot.commands;

import com.jagrosh.jdautilities.commandclient.CommandEvent;

import de.blockbuild.musikbot.Bot;
import de.blockbuild.musikbot.core.MBCommand;

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
		if (event.getArgs().isEmpty()) {
			if (event.getMember().getVoiceState().getChannel()
					.equals((event.getSelfMember().getVoiceState().getChannel()))) {
				StringBuilder builder = new StringBuilder(event.getClient().getWarning());
				builder.append(" We are already in the same channel!");
				event.reply(builder.toString());
			} else {
				bot.joinDiscordVoiceChannel(event.getGuild(), event.getMember().getVoiceState().getChannel().getName());
			}
		} else {
			bot.joinDiscordVoiceChannel(event.getGuild(), event.getArgs());
		}
	}
}
