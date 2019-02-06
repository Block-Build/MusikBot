package de.blockbuild.musikbot.commands.music;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import de.blockbuild.musikbot.Bot;
import de.blockbuild.musikbot.commands.MusicCommand;

public class PauseCommand extends MusicCommand {
	public PauseCommand(Bot bot) {
		super(bot);
		this.name = "pause";
		this.help = "Pause playback.";
		this.joinOnCommand = false;
	}

	@Override
	protected void doGuildCommand(CommandEvent event) {
		AudioPlayer player = musicManager.getAudioPlayer();
		if (!(player.getPlayingTrack() == null)) {
			player.setPaused(true);
			// event.getJDA().getPresence().setStatus(OnlineStatus.IDLE);
			// event.getJDA().getPresence().setGame(Game.of(GameType.DEFAULT, "Paused! |
			// Type '!Resume'"));
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
}