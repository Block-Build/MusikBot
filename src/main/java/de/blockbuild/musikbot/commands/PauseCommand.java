package de.blockbuild.musikbot.commands;

import com.jagrosh.jdautilities.commandclient.CommandEvent;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import de.blockbuild.musikbot.Main;
import de.blockbuild.musikbot.core.MBCommand;

import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Game.GameType;

public class PauseCommand extends MBCommand {

	public PauseCommand(Main main) {
		super(main);
		this.name = "pause";
		this.help = "Pause playback.";
		this.joinOnCommand = false;
		this.category = MUSIC;
	}

	@Override
	protected void doCommand(CommandEvent event) {
		AudioPlayer player = main.getBot().getGuildAudioPlayer(event.getGuild()).getAudioPlayer();
		if (!(player.getPlayingTrack() == null)) {
			player.setPaused(true);
			event.getJDA().getPresence().setStatus(OnlineStatus.IDLE);
			event.getJDA().getPresence().setGame(Game.of(GameType.DEFAULT, "Paused! | Type '!Resume'"));
		} else {
			StringBuilder builder = new StringBuilder(event.getClient().getWarning());
			builder.append(" Currently there is no track playing. Use `!Play` to start a track.");
			event.reply(builder.toString());
		}

	}
}