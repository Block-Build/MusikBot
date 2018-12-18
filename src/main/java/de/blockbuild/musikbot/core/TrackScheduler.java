package de.blockbuild.musikbot.core;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.jagrosh.jdautilities.commandclient.CommandEvent;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventListener;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import net.dv8tion.jda.core.entities.TextChannel;

public class TrackScheduler extends AudioEventAdapter implements AudioEventListener {

	private final AudioPlayer player;
	private final BlockingQueue<AudioTrack> queue;
	private final TextChannel textChannel;

	public TrackScheduler(TextChannel textChannel, AudioPlayer player) {
		this.player = player;
		this.queue = new LinkedBlockingQueue<>();
		this.textChannel = textChannel;
	}

	public void queue(AudioTrack track, CommandEvent event) {
		if (!(event == null)) {
			StringBuilder builder = new StringBuilder(event.getClient().getSuccess());
			builder.append(" Successfully added: ").append(track.getInfo().title).append(" on position ")
					.append(queue.size() + 1);
			event.reply(builder.toString());
		}
		if (!player.startTrack(track, true)) {
			queue.offer(track);
		}
	}

	public void playTrack(AudioTrack track, CommandEvent event) {
		if (!(event == null)) {
			StringBuilder builder = new StringBuilder(event.getClient().getSuccess());
			builder.append(" Successfully added: ").append(track.getInfo().title);
			event.reply(builder.toString());
		}
		player.startTrack(track, false);
	}

	public void nextTrack(CommandEvent event) {
		// StringBuilder builder = new StringBuilder(event.getClient().getSuccess());
		// builder.append(" Successfully added: ").append(track.getInfo().title);
		// event.reply(builder.toString());
		player.startTrack(queue.poll(), false);
	}

	public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
		System.out.println("onTrackEnd");
		if (endReason.mayStartNext) {
			nextTrack(null);
		}
	}

	@Override
	public void onTrackStart(AudioPlayer player, AudioTrack track) {
		System.out.println("onTrackStart");
		System.out.println("textChannel" + textChannel.getName());
		StringBuilder builder = new StringBuilder("Now Playing: ").append(track.getInfo().title);
		textChannel.sendMessage(builder.toString()).queue();
	}

	public AudioPlayer getPlayer() {
		return player;
	}
}
