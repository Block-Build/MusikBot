package de.blockbuild.musikbot.core;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEvent;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventListener;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public class TrackScheduler implements AudioEventListener {

	AudioPlayer player;

	@Override
	public void onEvent(AudioEvent event) {
		// TODO Auto-generated method stub
	}

	public TrackScheduler(AudioPlayer player) {
		this.player = player;
	}

	public void playTrack(AudioTrack track) {
		player.playTrack(track);
	}
	
	public AudioPlayer getPlayer(){
		return player;
	}
}
