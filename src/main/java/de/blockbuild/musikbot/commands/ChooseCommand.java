package de.blockbuild.musikbot.commands;

import com.jagrosh.jdautilities.command.CommandEvent;

import de.blockbuild.musikbot.Bot;
import de.blockbuild.musikbot.core.GuildMusicManager;
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
	protected void doCommand(CommandEvent event) {
		GuildMusicManager musicManager = bot.getGuildAudioPlayer(event.getGuild());

		if (musicManager.tracks == null || musicManager.tracks.isEmpty()) {
			StringBuilder builder = new StringBuilder().append(event.getClient().getWarning());
			builder.append("There is nothing to choose. First you need to search for an YT-Video with `!Play <title>`");
			event.reply(builder.toString());
		} else {
			try {
				choose = Integer.parseInt(event.getArgs());
			} catch (Exception e) {
				// not an Integer
			} finally {
				if (choose > 0 && choose <= 5) {
					if (musicManager.isQueue) {
						musicManager.getTrackScheduler().queue(musicManager.tracks.get(choose - 1), event);
					} else {
						musicManager.getTrackScheduler().playTrack(musicManager.tracks.get(choose - 1), event);
					}
				} else {
					StringBuilder builder = new StringBuilder().append(event.getClient().getError());
					builder.append("`").append(event.getArgs()).append("` is not vaild numer. Use `!").append(this.name)
							.append("` ").append(this.getArguments());
					event.reply(builder.toString());
				}
				musicManager.tracks = null;
			}
		}
	}
}
