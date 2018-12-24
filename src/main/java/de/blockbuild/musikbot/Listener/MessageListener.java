package de.blockbuild.musikbot.Listener;

import java.util.Random;

import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter {

	MessageChannel channel;
	String msg;

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
		}
	}
}
