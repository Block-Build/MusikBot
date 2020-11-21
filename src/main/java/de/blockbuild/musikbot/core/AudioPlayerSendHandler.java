package de.blockbuild.musikbot.core;

import java.nio.ByteBuffer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;

import de.blockbuild.musikbot.Bot;

import net.dv8tion.jda.api.audio.AudioSendHandler;

public class AudioPlayerSendHandler implements AudioSendHandler {
	private final Bot bot;
	public final AudioPlayer player;
	public AudioFrame lastFrame;

	public AudioPlayerSendHandler(AudioPlayer player, Bot bot) {
		this.player = player;
		this.bot = bot;
	}

	@Override
	public boolean canProvide() {
		lastFrame = player.provide();
		return lastFrame != null;
	}

	@Override
	public ByteBuffer provide20MsAudio() {
		return ByteBuffer.wrap(lastFrame.getData());
	}

	@Override
	public boolean isOpus() {
		return true;
	}

	public AudioPlayer getPlayer() {
		return player;
	}

	public Bot getBot() {
		return bot;
	}
}
