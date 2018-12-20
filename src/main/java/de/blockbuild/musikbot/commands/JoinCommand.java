package de.blockbuild.musikbot.commands;

import com.jagrosh.jdautilities.commandclient.CommandEvent;

import de.blockbuild.musikbot.Main;
import de.blockbuild.musikbot.core.MBCommand;

public class JoinCommand extends MBCommand {

	public JoinCommand(Main main) {
		super(main);
		this.name = "join";
		this.help = "Triggers the Bot to join a voice channel!";
		this.arguments = "<ChannelName>";
	}

	@Override
	protected void doCommand(CommandEvent event) {
		if (event.getArgs().isEmpty()) {
			main.getBot().joinDiscordVoiceChannel(event.getJDA(),
					event.getMember().getVoiceState().getAudioChannel().getId());
		} else {
			main.getBot().joinDiscordVoiceChannel(event.getJDA(), event.getArgs());
		}
	}
}
