package de.blockbuild.musikbot.core;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;

import de.blockbuild.musikbot.Main;

import net.dv8tion.jda.core.audio.AudioSendHandler;

public class AudioPlayerSendHandler implements AudioSendHandler {
	public final Main main;
	public final AudioPlayer player;
	public AudioFrame lastFrame;

	public AudioPlayerSendHandler(AudioPlayer player, Main main) {
		this.player = player;
		this.main = main;
	}

	@Override
	public boolean canProvide() {
		lastFrame = player.provide();
		return lastFrame != null;
	}

	@Override
	public byte[] provide20MsAudio() {
		return lastFrame.getData();
	}

	@Override
	public boolean isOpus() {
		return true;
	}
	
	public AudioPlayer getPlayer(){
		return player;
	}
}
