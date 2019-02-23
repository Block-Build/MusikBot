package de.blockbuild.musikbot.commands.setup;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.jagrosh.jdautilities.command.CommandEvent;

import de.blockbuild.musikbot.Bot;
import de.blockbuild.musikbot.commands.SetupCommand;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.exceptions.PermissionException;
import net.dv8tion.jda.core.requests.RequestFuture;

public class PurgeCommand extends SetupCommand {

	public PurgeCommand(Bot bot) {
		super(bot);
		this.name = "purge";
		this.help = "delete up to 100 messages from a channel";
		this.arguments = "<amount>";
	}

	@Override
	protected void doGuildCommand(CommandEvent event) {
		event.reply(event.getClient().getWarning() + " Deleting messages...", m -> {
			System.out.println(1);
			textChannel.getHistory().retrievePast(100).queue(messages -> {

				// textChannel.getHistoryBefore(m, 100).queue(messageHistory -> {
				System.out.println(2);
				List<Message> list = messages.stream()
						// messageHistory.getRetrievedHistory().stream()
						.filter(message -> !message.isPinned() && !(message.getIdLong() == m.getIdLong()))
						// !message.getCreationTime().isBefore(OffsetDateTime.now().minusWeeks(2))
						.collect(Collectors.toList());
				System.out.println(3);

				RequestFuture.allOf(textChannel.purgeMessages(list)).thenRun(() -> {
					System.out.println(4);
					m.editMessage(event.getClient().getSuccess() + " Finished deleting.").queue();
					System.out.println(5);
					m.delete().queueAfter(15, TimeUnit.SECONDS);
				});

				/*
				 * textChannel.deleteMessages(list).queue(success -> {
				 * m.editMessage(event.getClient().getSuccess() +
				 * " Finished deleting.").queueAfter(15, TimeUnit.SECONDS, message ->
				 * message.delete().queue()); }, error -> { if (error instanceof
				 * PermissionException) { textChannel.sendMessage(event.getClient().getError() +
				 * " Missing permission: " + ((PermissionException)
				 * error).getPermission().getName()); } else { textChannel.sendMessage(
				 * event.getClient().getError() +
				 * " Error on deleting messages. See log for details.") .queue(); } });
				 */
			});

			/*
			 * textChannel.getHistory().retrievePast(100).queue( messageHistory -> {
			 * messageHistory = messageHistory.stream().filter(message ->
			 * message.getAuthor().isBot() ).collect(Collectors.toList()); });
			 */
		});

		textChannel.getHistory().retrievePast(100).queue(m -> {
			RequestFuture.allOf(textChannel.purgeMessages(m)).thenRun(() -> System.out.println(""));
		});
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
