package de.blockbuild.musikbot.commands.music;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import de.blockbuild.musikbot.Bot;
import de.blockbuild.musikbot.commands.MBCommand;

public class ResumeCommand extends MBCommand {

	public ResumeCommand(Bot bot) {
		super(bot);
		this.name = "resume";
		this.help = "Resume playback.";
		this.joinOnCommand = true;
		this.category = MUSIC;
	}

	@Override
	protected void doGuildCommand(CommandEvent event) {
		if (!(guild.getAudioManager().isConnected())) {
			// not yet Tested!!!
			guild.getAudioManager().openAudioConnection(selfMember.getVoiceState().getChannel());
		}

		AudioPlayer player = musicManager.getAudioPlayer();
		player.setPaused(false);
		// event.getJDA().getPresence().setStatus(OnlineStatus.ONLINE);
		// event.getJDA().getPresence().setGame(Game.of(GameType.LISTENING,
		// player.getPlayingTrack().getInfo().title));
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