package de.blockbuild.musikbot.Listener;

import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class VoiceChannelListener extends ListenerAdapter {

	MessageChannel channel;
	String msg;

	@Override
	public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) {
		VoiceChannel channel = event.getChannelLeft();
		//Member member = event.getMember();
		Member selfMember = event.getGuild().getSelfMember();
		
		if (!selfMember.getVoiceState().inVoiceChannel()) {
			return;
		}
		
		if (!(channel == selfMember.getVoiceState().getChannel())) {
			return;
		}
		
		if (channel.getMembers().size() > 1) {
			return;
		}

		// close audio connection
		selfMember.getGuild().getAudioManager().closeAudioConnection();
	}
	
	@Override
	public void onGuildVoiceMove(GuildVoiceMoveEvent event) {
		VoiceChannel channel = event.getChannelLeft();
		//Member member = event.getMember();
		Member selfMember = event.getGuild().getSelfMember();
		
		if (!selfMember.getVoiceState().inVoiceChannel()) {
			return;
		}
		
		if (!(channel == selfMember.getVoiceState().getChannel())) {
			return;
		}
		
		if (channel.getMembers().size() > 1) {
			return;
		}

		// close audio connection
		selfMember.getGuild().getAudioManager().closeAudioConnection();
	}
}
