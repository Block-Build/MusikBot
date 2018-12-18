package de.blockbuild.musikbot.core;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEvent;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventListener;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

public class TrackScheduler implements AudioEventListener {

	private final AudioPlayer player;
	private final BlockingQueue<AudioTrack> queue;

	public TrackScheduler(AudioPlayer player) {
		this.player = player;
		this.queue = new LinkedBlockingQueue<>();
	}

	public void queue(AudioTrack track) {
		if (!player.startTrack(track, true)) {
			queue.offer(track);
		}
	}

	public void playTrack(AudioTrack track) {
		player.startTrack(track, false);
	}

	public void nextTrack() {
		player.startTrack(queue.poll(), false);
	}

	public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
		if (endReason.mayStartNext) {
			nextTrack();
		}
	}

	@Override
	public void onEvent(AudioEvent event) {
		// TODO Auto-generated method stub
		System.out.println("AudioEvent!");
	}

	public AudioPlayer getPlayer() {
		return player;
	}
}
