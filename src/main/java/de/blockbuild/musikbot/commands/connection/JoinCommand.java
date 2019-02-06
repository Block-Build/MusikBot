package de.blockbuild.musikbot.commands.connection;

import com.jagrosh.jdautilities.command.CommandEvent;

import de.blockbuild.musikbot.Bot;
import de.blockbuild.musikbot.commands.ConnectionCommand;

public class JoinCommand extends ConnectionCommand {

	public JoinCommand(Bot bot) {
		super(bot);
		this.name = "join";
		this.help = "Triggers the Bot to join a voice channel!";
		this.arguments = "[ChannelName]";
		this.joinOnCommand = false;
	}

	@Override
	protected void doGuildCommand(CommandEvent event) {
		if (args.isEmpty()) {
			if (channel == null) {
				StringBuilder builder = new StringBuilder(event.getClient().getWarning());
				builder.append(" You need to specify a channel!");
				event.reply(builder.toString());
			} else if (channel.equals(selfChannel)) {
				StringBuilder builder = new StringBuilder(event.getClient().getWarning());
				builder.append(" We are already in the same channel!");
				event.reply(builder.toString());
			} else {
				if (allowedToJoinVoiceChannel(musicManager, channel.getIdLong())) {
					bot.joinDiscordVoiceChannel(guild, channel.getIdLong());
				} else {
					sendDefaultVoiceChannelInfo(event, musicManager);
				}
			}
		} else {
			if (allowedToJoinVoiceChannel(musicManager, args)) {
				if (!bot.joinDiscordVoiceChannel(guild, args)) {
					StringBuilder builder = new StringBuilder(event.getClient().getWarning());
					builder.append(" Missing permission or there is no channel called `").append(args).append("`.");
					event.reply(builder.toString());
				}
			} else {
				sendDefaultVoiceChannelInfo(event, musicManager);
			}
		}
	}

	@Override
	protected void doPrivateCommand(CommandEvent event) {
		event.reply(event.getClient().getError() + " This command cannot be used in Direct messages.");

		StringBuilder builder = new StringBuilder().append(event.getClient().getSuccess());

		builder.append(" **MusikBot** ").append("by Block-Build\n");
		builder.append("SpigotMC: `https://www.spigotmc.org/resources/the-discord-musikbot-on-minecraft.64277/`\n");
		builder.append("GitHub: `https://github.com/Block-Build/MusikBot`\n");
		builder.append("Version: `").append(bot.getMain().getDescription().getVersion()).append("`\n");
		builder.append("Do you have any problem or suggestion? Open an Issue on GitHub.");

		event.reply(builder.toString());
	}
}
