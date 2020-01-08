package de.blockbuild.musikbot.commands.music;

import com.github.breadmoirai.discordemoji.Emoji;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import de.blockbuild.musikbot.Bot;
import de.blockbuild.musikbot.commands.MusicCommand;
import de.blockbuild.musikbot.core.TrackScheduler;

public class NextCommand extends MusicCommand {

	public NextCommand(Bot bot) {
		super(bot);
		this.name = "next";
		this.aliases = new String[] { "n" };
		this.help = "Returns the next title of the next track.";
		this.joinOnCommand = true;
	}

	@Override
	protected void doGuildCommand(CommandEvent event) {
		TrackScheduler trackScheduler = musicManager.getTrackScheduler();
		StringBuilder builder = new StringBuilder();
		if (trackScheduler.getNextTrack() == null) {
			builder.append(event.getClient().getWarning()).append(" The queue is empty!");
		} else {
			AudioTrack track = trackScheduler.getNextTrack();
			builder.append(Emoji.NOTES.getUtf8()).append(" Next track: **").append(track.getInfo().title).append("**");
			builder.append(" (`").append(trackScheduler.getTime(track.getDuration())).append("`)");
		}
		event.reply(builder.toString());
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
