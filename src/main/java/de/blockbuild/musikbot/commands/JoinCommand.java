package de.blockbuild.musikbot.commands;

import com.jagrosh.jdautilities.commandclient.CommandEvent;

import de.blockbuild.musikbot.Main;
import de.blockbuild.musikbot.core.MBCommand;

public class JoinCommand extends MBCommand {

	public JoinCommand(Main main) {
		super(main);
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
				main.getBot().joinDiscordVoiceChannel(event.getJDA(),
						event.getMember().getVoiceState().getChannel().getName());
			}

		} else {
			main.getBot().joinDiscordVoiceChannel(event.getJDA(), event.getArgs());
		}
	}
}
