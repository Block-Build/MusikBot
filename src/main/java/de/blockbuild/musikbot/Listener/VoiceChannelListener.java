package de.blockbuild.musikbot.Listener;

import de.blockbuild.musikbot.core.AudioPlayerSendHandler;
import de.blockbuild.musikbot.core.GuildMusicManager;

import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class VoiceChannelListener extends ListenerAdapter {

	MessageChannel channel;
	String msg;

	@Override
	public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) {
		GuildMusicManager musicManager = ((AudioPlayerSendHandler) event.getGuild().getAudioManager()
				.getSendingHandler()).getBot().getGuildAudioPlayer(event.getGuild());

		if (musicManager.config.disconnectIfAlone) {
			VoiceChannel channel = event.getChannelLeft();
			// Member member = event.getMember();
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

			if (!(musicManager.getAudioPlayer().getPlayingTrack() == null)) {
				musicManager.getAudioPlayer().stopTrack();
			}
			// close audio connection
			selfMember.getGuild().getAudioManager().closeAudioConnection();
		}
	}

	@Override
	public void onGuildVoiceMove(GuildVoiceMoveEvent event) {
		GuildMusicManager musicManager = ((AudioPlayerSendHandler) event.getGuild().getAudioManager()
				.getSendingHandler()).getBot().getGuildAudioPlayer(event.getGuild());

		if (musicManager.config.disconnectIfAlone) {
			VoiceChannel channel = event.getChannelLeft();
			// Member member = event.getMember();
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

			if (!(musicManager.getAudioPlayer().getPlayingTrack() == null)) {
				musicManager.getAudioPlayer().stopTrack();
			}
			// close audio connection
			selfMember.getGuild().getAudioManager().closeAudioConnection();
		}
	}
}
