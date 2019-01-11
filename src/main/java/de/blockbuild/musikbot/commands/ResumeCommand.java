package de.blockbuild.musikbot.commands;

import com.jagrosh.jdautilities.commandclient.CommandEvent;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import de.blockbuild.musikbot.Bot;
import de.blockbuild.musikbot.core.MBCommand;

public class ResumeCommand extends MBCommand {

	public ResumeCommand(Bot bot) {
		super(bot);
		this.name = "resume";
		this.help = "Resume playback.";
		this.joinOnCommand = true;
		this.category = MUSIC;
	}

	@Override
	protected void doCommand(CommandEvent event) {
		if (!(event.getGuild().getAudioManager().isConnected())) {
			// not yet Tested!!!
			event.getGuild().getAudioManager().openAudioConnection(event.getSelfMember().getVoiceState().getChannel());
		}

		AudioPlayer player = bot.getGuildAudioPlayer(event.getGuild()).getAudioPlayer();
		player.setPaused(false);
		// event.getJDA().getPresence().setStatus(OnlineStatus.ONLINE);
		// event.getJDA().getPresence().setGame(Game.of(GameType.LISTENING, player.getPlayingTrack().getInfo().title));
	}
}