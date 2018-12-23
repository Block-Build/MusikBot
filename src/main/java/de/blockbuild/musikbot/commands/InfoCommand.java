package de.blockbuild.musikbot.commands;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.jagrosh.jdautilities.commandclient.CommandEvent;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;

import de.blockbuild.musikbot.Main;
import de.blockbuild.musikbot.core.GuildMusicManager;
import de.blockbuild.musikbot.core.MBCommand;

public class InfoCommand extends MBCommand {

	public InfoCommand(Main main) {
		super(main);
		this.name = "info";
		this.aliases = new String[] { "i" };
		this.help = "Info of the current Track.";
		this.joinOnCommand = false;
		this.category = MUSIC;
	}

	@Override
	protected void doCommand(CommandEvent event) {
		GuildMusicManager musicManager = main.getBot().getGuildAudioPlayer(event.getGuild());
		if (!(musicManager.getAudioPlayer().getPlayingTrack() == null)) {
			AudioTrackInfo trackInfo = musicManager.getAudioPlayer().getPlayingTrack().getInfo();
			StringBuilder builder = new StringBuilder(event.getClient().getSuccess()).append("\n");
			builder.append("Title: `").append(trackInfo.title).append("`\n");
			builder.append("Author: `").append(trackInfo.author).append("`\n");
			builder.append("Duration: `").append(getTime(trackInfo.length)).append("`\n");
			builder.append("URL: `").append(trackInfo.uri).append("`\n");
			event.reply(builder.toString());
		} else {
			StringBuilder builder = new StringBuilder(event.getClient().getWarning());
			builder.append(" Currently there is no track playing. Use `!Play` to start a track.");
			event.reply(builder.toString());
		}
	}

	private String getTime(long lng) {
		return (new SimpleDateFormat("mm:ss")).format(new Date(lng));
	}

}
