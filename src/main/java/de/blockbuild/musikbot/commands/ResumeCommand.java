package de.blockbuild.musikbot.commands;

import com.jagrosh.jdautilities.commandclient.CommandEvent;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import de.blockbuild.musikbot.Main;
import de.blockbuild.musikbot.core.MBCommand;

import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Game.GameType;

public class ResumeCommand extends MBCommand {

	public ResumeCommand(Main main) {
		super(main);
		this.name = "resume";
		this.help = "Resume playback.";
		this.joinOnCommand = true;
		this.category = MUSIC;
	}

	@Override
	protected void doCommand(CommandEvent event) {
		if (!(event.getGuild().getAudioManager().isConnected())) {
			//not yet Tested!!!
			event.getGuild().getAudioManager().openAudioConnection(event.getSelfMember().getVoiceState().getChannel());
		}

		AudioPlayer player = main.getBot().getScheduler().getPlayer();
		player.setPaused(false);
		event.getJDA().getPresence().setStatus(OnlineStatus.ONLINE);
		event.getJDA().getPresence().setGame(Game.of(GameType.LISTENING, player.getPlayingTrack().getInfo().title));
	}
}