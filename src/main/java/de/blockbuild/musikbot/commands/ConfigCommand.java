package de.blockbuild.musikbot.commands;

import com.jagrosh.jdautilities.command.CommandEvent;

import de.blockbuild.musikbot.Bot;
import de.blockbuild.musikbot.core.GuildMusicManager;
import de.blockbuild.musikbot.core.MBCommand;

public class ConfigCommand extends MBCommand {

	public ConfigCommand(Bot bot) {
		super(bot);
		this.name = "config";
		this.aliases = new String[] { "cfg" };
		this.help = "To load, save or show the Config";
		this.arguments = "<save|load|show>";
		this.joinOnCommand = false;
		this.category = SETUP;
	}

	@Override
	protected void doCommand(CommandEvent event) {
		GuildMusicManager musicManager = bot.getGuildAudioPlayer(event.getGuild());

		StringBuilder builder = new StringBuilder();
		if (event.getArgs().startsWith("save")) {
			if (musicManager.config.writeConfig()) {
				builder.append(event.getClient().getSuccess()).append(" Config saved successfully");
			} else {
				builder.append(event.getClient().getError()).append(" Faild to save config");
			}
		} else if (event.getArgs().startsWith("load")) {
			if (musicManager.config.readConfig()) {
				builder.append(event.getClient().getSuccess()).append(" Config load successfully");
			} else {
				builder.append(event.getClient().getError()).append(" Faild to load config");
			}
		} else if (event.getArgs().startsWith("show")) {
			builder.append(event.getClient().getSuccess()).append(" ");

			builder.append("**Bot Configuration**").append("\n");
			builder.append(bot.config.getRawConfiguration()).append("\n");

			builder.append("**Guild Configuration**").append("\n");
			builder.append(musicManager.config.getRawConfiguration()).append("\n");
		} else {
			sendCommandInfo(event);
		}
		event.reply(builder.toString());
	}
}
