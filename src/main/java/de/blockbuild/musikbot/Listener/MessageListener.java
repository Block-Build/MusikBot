package de.blockbuild.musikbot.Listener;

import java.util.Random;

import de.blockbuild.musikbot.Bot;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter {

	MessageChannel channel;
	String msg;
	Bot bot;

	public MessageListener(Bot bot) {
		this.bot = bot;
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		channel = event.getChannel();
		msg = event.getMessage().getContentDisplay();

		if (event.isFromType(ChannelType.TEXT)) {
		}

		if (msg.equals("!ping")) {
			channel.sendMessage("pong!").queue();
		} else if (msg.equals("!roll")) {
			Random rand = new Random();
			int roll = rand.nextInt(6) + 1;
			channel.sendMessage("Your roll: " + roll).queue(sentMessage -> // This is called a lambda statement.
			{
				if (roll < 3) {
					channel.sendMessage("The roll for messageId: " + sentMessage.getId()
							+ " wasn't very good... Must be bad luck!\n").queue();
				}
			});
		} else if (msg.equalsIgnoreCase("***trigger")) {
			channel.sendMessage(bot.botConfig.getTrigger()).queue();
		}
	}
}
