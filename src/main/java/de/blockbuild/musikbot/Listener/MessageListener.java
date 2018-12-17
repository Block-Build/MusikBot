package de.blockbuild.musikbot.Listener;

import java.util.Random;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter {

	JDA jda;
	long responseNumber;
	User author;
	String author2;
	String author3;
	Message message;
	MessageChannel channel;
	String msg;
	boolean bot;
	Guild guild;
	TextChannel textChannel;
	Member member;
	String name;

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		jda = event.getJDA();
		responseNumber = event.getResponseNumber();
		author = event.getAuthor();
		name = author.getName();
		msg = event.getMessage().getContentDisplay();
		channel = event.getChannel();
		bot = author.isBot();

		if (event.isFromType(ChannelType.TEXT)) {
			guild = event.getGuild();
			textChannel = event.getTextChannel();
			member = event.getMember();
			name = member.getEffectiveName();
		}

		if (msg.equals("!ping")) {
			channel.sendMessage("pong!").queue();
		} else if (msg.equals("!roll")) {
			Random rand = new Random();
			int roll = rand.nextInt(6) + 1;
			channel.sendMessage("Your roll: " + roll).queue(sentMessage -> // This is called a lambda statement. If you
																			// don't know
			{ // what they are or how they work, try google!
				if (roll < 3) {
					channel.sendMessage("The roll for messageId: " + sentMessage.getId()
							+ " wasn't very good... Must be bad luck!\n").queue();
				}
			});
		}
	}
}
