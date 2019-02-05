package de.blockbuild.musikbot.commands.connection;

import com.jagrosh.jdautilities.command.CommandEvent;

import de.blockbuild.musikbot.Bot;
import de.blockbuild.musikbot.commands.MBCommand;
import de.blockbuild.musikbot.core.TrackScheduler;

public class QuitCommand extends MBCommand {

	public QuitCommand(Bot bot) {
		super(bot);
		this.name = "quit";
		this.aliases = new String[] { "leave", "disconnect" };
		this.help = "Disconnect and delete queue.";
		this.joinOnCommand = false;
		this.category = CONNECTION;
	}

	@Override
	protected void doGuildCommand(CommandEvent event) {
		TrackScheduler trackScheduler = musicManager.getTrackScheduler();
		trackScheduler.flushQueue();
		trackScheduler.stopTrack();
		guild.getAudioManager().closeAudioConnection();
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
