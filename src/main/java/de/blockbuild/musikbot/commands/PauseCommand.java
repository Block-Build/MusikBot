package de.blockbuild.musikbot.commands;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import de.blockbuild.musikbot.Bot;
import de.blockbuild.musikbot.core.MBCommand;

public class PauseCommand extends MBCommand {
	public PauseCommand(Bot bot) {
		super(bot);
		this.name = "pause";
		this.help = "Pause playback.";
		this.joinOnCommand = false;
		this.category = MUSIC;
	}

	@Override
	protected void doCommand(CommandEvent event) {
		AudioPlayer player = bot.getGuildAudioPlayer(event.getGuild()).getAudioPlayer();
		if (!(player.getPlayingTrack() == null)) {
			player.setPaused(true);
			// event.getJDA().getPresence().setStatus(OnlineStatus.IDLE);
			// event.getJDA().getPresence().setGame(Game.of(GameType.DEFAULT, "Paused! | Type '!Resume'"));
		} else {
			StringBuilder builder = new StringBuilder(event.getClient().getWarning());
			builder.append(" Currently there is no track playing. Use `!Play` to start a track.");
			event.reply(builder.toString());
		}
	}
}