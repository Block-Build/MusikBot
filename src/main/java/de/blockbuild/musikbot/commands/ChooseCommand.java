package de.blockbuild.musikbot.commands;

import com.jagrosh.jdautilities.command.CommandEvent;

import de.blockbuild.musikbot.Bot;
import de.blockbuild.musikbot.core.MBCommand;

public class ChooseCommand extends MBCommand {

	private int choose = 0;

	public ChooseCommand(Bot bot) {
		super(bot);
		this.name = "choose";
		this.aliases = new String[] { "c", "cho" };
		this.help = "Chosse one of fife search results";
		this.arguments = "<1-5>";
		this.joinOnCommand = false;
		this.category = MUSIC;
	}

	@Override
	protected void doGuildCommand(CommandEvent event) {
		if (musicManager.tracks == null || musicManager.tracks.isEmpty()) {
			StringBuilder builder = new StringBuilder().append(event.getClient().getWarning());
			builder.append("There is nothing to choose. First you need to search for an YT-Video with `")
					.append(event.getClient().getPrefix()).append("play <title>`");
			event.reply(builder.toString());
		} else {
			try {
				choose = Integer.parseInt(args);
			} catch (Exception e) {
				// not an Integer
			} finally {
				if (choose > 0 && choose <= 5) {
					if (musicManager.isQueue()) {
						musicManager.getTrackScheduler().queue(musicManager.tracks.get(choose - 1), event);
					} else {
						musicManager.getTrackScheduler().playTrack(musicManager.tracks.get(choose - 1), event);
					}
				} else {
					StringBuilder builder = new StringBuilder().append(event.getClient().getError());
					builder.append("`").append(args).append("` is not vaild numer. Use `")
							.append(event.getClient().getPrefix()).append(this.name).append("` ")
							.append(this.getArguments());
					event.reply(builder.toString());
				}
				musicManager.tracks = null;
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
