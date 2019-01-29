package de.blockbuild.musikbot.commands;

import com.jagrosh.jdautilities.command.CommandEvent;

import de.blockbuild.musikbot.Bot;
import de.blockbuild.musikbot.core.GuildMusicManager;
import de.blockbuild.musikbot.core.MBCommand;

public class YTAutoPlayCommand extends MBCommand {
	public YTAutoPlayCommand(Bot bot) {
		super(bot);
		this.name = "autoplay";
		this.aliases = new String[] { "ytautoplay", "ap", "yap" };
		this.help = "Enable or disable Youtube autoplay";
		this.arguments = "<Enable|Disable>";
		this.joinOnCommand = true;
		this.category = MUSIC;
	}

	@Override
	protected void doCommand(CommandEvent event) {
		GuildMusicManager musicManager = bot.getGuildAudioPlayer(event.getGuild());
		StringBuilder builder = new StringBuilder().append(event.getClient().getSuccess());

		if (event.getArgs().equalsIgnoreCase("enable")) {
			musicManager.setIsAutoPlay(true);
			builder.append(" YouTube autoplay: `enabled`");
		} else if (event.getArgs().equalsIgnoreCase("disable")) {
			musicManager.setIsAutoPlay(false);
			builder.append(" YouTube autoplay: `disabled`");
		} else {
			sendCommandInfo(event);
		}
		event.reply(builder.toString());
	}
}
