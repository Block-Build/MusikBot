package de.blockbuild.musikbot.core;

import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.jagrosh.jdautilities.commandclient.CommandEvent;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventListener;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import net.dv8tion.jda.core.entities.Guild;

public class TrackScheduler extends AudioEventAdapter implements AudioEventListener {

	private final AudioPlayer player;
	private final BlockingQueue<AudioTrack> queue;
	private final Guild guild;

	public TrackScheduler(Guild guild, AudioPlayer player) {
		this.player = player;
		this.queue = new LinkedBlockingQueue<AudioTrack>();
		this.guild = guild;
		player.setVolume(10); // ;-)
	}

	public void queue(AudioTrack track, CommandEvent event) {
		StringBuilder builder = new StringBuilder(event.getClient().getSuccess());

		if (!player.startTrack(track, true)) {
			queue.offer(track);
			builder.append(" Successfully added: `").append(track.getInfo().title).append("` on position: ")
					.append(queue.size());
		}

		if (!(event == null)) {
			event.reply(builder.toString());
		}
	}

	public void queue(AudioPlaylist playlist, CommandEvent event) {
		StringBuilder builder = new StringBuilder(event.getClient().getSuccess());
		builder.append(" Successfully added: \n");

		for (AudioTrack track : playlist.getTracks()) {
			queue.offer(track);
			builder.append("`").append(track.getInfo().title).append("` on position: ").append(queue.size())
					.append("\n");
		}

		if (!(event == null)) {
			event.reply(builder.toString());
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
			builder.append(event.getClient().getWarning()).append(" Queue is empty.");
		} else {
			builder.append(event.getClient().getSuccess());
			builder.append(" Now Playing: `").append(queue.peek().getInfo().title).append("`.");
			player.startTrack(queue.poll(), false);
		}
		event.reply(builder.toString());
	}

	public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
		System.out.println("onTrackEnd endReason " + endReason.toString());

		if (queue.isEmpty() && player.getPlayingTrack() == null) {
			player.playTrack(null);
			// jda.getPresence().setGame(Game.of(GameType.DEFAULT, "Ready for playing music. !Play"));
			// may close audio connection?
		}

		if (endReason.mayStartNext) {
			player.playTrack(queue.poll());
		}
	}

	@Override
	public void onTrackStart(AudioPlayer player, AudioTrack track) {
		System.out.println("onTrackStart");
		System.out.println("textChannel " + guild.getSystemChannel().getName());
		if (player.isPaused()) {
			player.setPaused(false);
		}
		// jda.getPresence().setStatus(OnlineStatus.ONLINE);
		// jda.getPresence().setGame(Game.of(GameType.LISTENING,
		// player.getPlayingTrack().getInfo().title));
	}

	public String getPlaylist() {
		StringBuilder builder = new StringBuilder();
		if (queue.isEmpty()) {
			builder.append("`Queue is empty.`");
		} else {
			int i = 0;
			Iterator<AudioTrack> x = queue.iterator();
			while (x.hasNext()) {
				AudioTrack track = x.next();
				builder.append("`").append(++i).append(". ").append(track.getInfo().title).append("`\n");
			}
		}

		return builder.toString();
	}

	public AudioTrack getNextTrack() {
		return queue.peek();
	}

	public void flushQueue() {
		queue.clear();
	}

	public void stopTrack() {
		player.playTrack(null);
	}

	public void flushQueue(int amount) {
		for (int i = 0; i < amount; i++) {
			queue.poll();
		}
	}
}
