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
		this.queue = new LinkedBlockingQueue<AudioTrack>();
		this.textChannel = textChannel;
		player.setVolume(10); // ;-)
	}

	public void queue(AudioTrack track, CommandEvent event) {
		if (!(event == null)) {
			StringBuilder builder = new StringBuilder(event.getClient().getSuccess());
			builder.append(" Successfully added: ").append(track.getInfo().title).append(" on position: ")
					.append(queue.size() + 1);
			event.reply(builder.toString());
		}
		if (!player.startTrack(track, true)) {
			// seems not to work
			if (!queue.contains(track)) {
				queue.offer(track);

				/*
				 * int i = 0; for(String s : queue.toString().split(",")) {
				 * System.out.println(++i + " " + s); } System.out.println("\n");
				 */

			} else {
				StringBuilder builder = new StringBuilder(event.getClient().getWarning());
				builder.append(" '").append(track.getInfo().title).append("' is already in the queue");
				event.reply(builder.toString());
			}
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
		StringBuilder builder = new StringBuilder();
		if (queue.isEmpty()) {
			builder.append("Queue is empty.");
		} else {
			builder.append(event.getClient().getSuccess());
			builder.append("Now Playing: `").append(queue.peek().getInfo().title).append("`.");
			player.startTrack(queue.poll(), false);
		}
		event.reply(builder.toString());
	}

	public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
		System.out.println("onTrackEnd endReason " + endReason.toString());

		if (queue.isEmpty()) {
			player.playTrack(null);
			// may close audio connection?
		}

		if (endReason.mayStartNext) {
			player.playTrack(queue.poll());
		}
	}

	@Override
	public void onTrackStart(AudioPlayer player, AudioTrack track) {
		System.out.println("onTrackStart");
		System.out.println("textChannel " + textChannel.getName());
		// StringBuilder builder = new StringBuilder("Now Playing:
		// ").append(track.getInfo().title);
		// textChannel.sendMessage(builder.toString()).queue();
	}

	public AudioPlayer getPlayer() {
		return player;
	}
}
