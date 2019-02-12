package de.blockbuild.musikbot.commands.general;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;

import de.blockbuild.musikbot.Bot;
import de.blockbuild.musikbot.commands.MBCommand;

public class InfoCommand extends MBCommand {

	public InfoCommand(Bot bot) {
		super(bot);
		this.name = "info";
		this.aliases = new String[] { "i" };
		this.help = "Info of the current Track.";
		this.joinOnCommand = false;
	}

	@Override
	protected void doGuildCommand(CommandEvent event) {
		if (!(musicManager.getAudioPlayer().getPlayingTrack() == null)) {
			AudioTrackInfo trackInfo = musicManager.getAudioPlayer().getPlayingTrack().getInfo();
			StringBuilder builder = new StringBuilder(event.getClient().getSuccess()).append("\n");
			builder.append("Title: `").append(trackInfo.title).append("`\n");
			builder.append("Author: `").append(trackInfo.author).append("`\n");
			builder.append("Duration: `").append(getTime(trackInfo.length)).append("`\n");
			builder.append("Source: `")
					.append(musicManager.getAudioPlayer().getPlayingTrack().getSourceManager().getSourceName())
					.append("`\n");
			builder.append("URL: `").append(trackInfo.uri).append("`\n");
			event.reply(builder.toString());
		} else {
			StringBuilder builder = new StringBuilder(event.getClient().getWarning());
			builder.append(" Currently there is no track playing. Use `").append(event.getClient().getPrefix())
					.append("play` to start a track.");
			event.reply(builder.toString());
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

	private String getTime(long lng) {
		return (new SimpleDateFormat("mm:ss")).format(new Date(lng));
	}

}
