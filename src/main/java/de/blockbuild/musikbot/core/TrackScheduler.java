package de.blockbuild.musikbot.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventListener;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import de.blockbuild.musikbot.Bot;

import net.dv8tion.jda.core.entities.Guild;

public class TrackScheduler extends AudioEventAdapter implements AudioEventListener {

	private final GuildMusicManager musicManager;
	private final AudioPlayer player;
	private final BlockingQueue<AudioTrack> queue;
	private final Guild guild;
	private final Bot bot;
	private final ExtendYoutubeAudioSourceManager eyasm;

	public TrackScheduler(Bot bot, GuildMusicManager musicManager) {
		this.musicManager = musicManager;
		this.player = musicManager.getAudioPlayer();
		this.queue = new LinkedBlockingQueue<AudioTrack>();
		this.bot = bot;
		this.guild = musicManager.getGuild();
		this.eyasm = new ExtendYoutubeAudioSourceManager();
	}

	public void queue(AudioTrack track, CommandEvent event) {
		StringBuilder builder = new StringBuilder(event.getClient().getSuccess());

		if (!player.startTrack(track, true)) {
			queue.offer(track);
			builder.append(" Successfully added: `").append(track.getInfo().title).append("` on position: ")
					.append(queue.size());
		} else {
			builder.append(" Successfully added: `").append(track.getInfo().title).append("`");
		}

		if (!(event == null)) {
			event.reply(builder.toString());
		}
	}

	public void queue(AudioPlaylist playlist, CommandEvent event) {
		StringBuilder builder = new StringBuilder(event.getClient().getSuccess());
		builder.append(" Successfully added: \n");

		for (AudioTrack track : playlist.getTracks()) {
			if (player.getPlayingTrack() == null) {
				this.playTrack(track, event);
			} else {
				queue.offer(track);
				builder.append("`").append(track.getInfo().title).append("` on position: ").append(queue.size())
						.append("\n");
			}
		}

		if (!(event == null)) {
			event.reply(builder.toString());
		}
	}

	public void queueSilent(AudioTrack track) {
		if (!player.startTrack(track, true)) {
			queue.offer(track);
		}
	}

	public void playTrack(AudioTrack track, CommandEvent event) {
		if (!(event == null)) {
			StringBuilder builder = new StringBuilder(event.getClient().getSuccess());
			builder.append(" Successfully added: `").append(track.getInfo().title).append("`");
			event.reply(builder.toString());
		}
		player.startTrack(track, false);
	}

	public void nextTrack(CommandEvent event) {
		StringBuilder builder = new StringBuilder();
		// Kann man mal ausprobieren.
		// synchronized (player) {
		// }
		if (nextYTAutoPlay(player.getPlayingTrack())) {
			// Wait until track is really loaded
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			builder.append(event.getClient().getSuccess());
			builder.append(" Now Playing: `").append(player.getPlayingTrack().getInfo().title).append("`.");
		} else if (queue.isEmpty()) {
			builder.append(event.getClient().getWarning()).append(" Queue is empty.");
			player.stopTrack();
		} else {
			builder.append(event.getClient().getSuccess());
			builder.append(" Now Playing: `").append(queue.peek().getInfo().title).append("`.");
			player.startTrack(queue.poll(), false);
		}
		event.reply(builder.toString());
	}

	public void shuffle() {
		List<AudioTrack> q = new ArrayList<AudioTrack>();
		Iterator<AudioTrack> x = queue.iterator();
		while (x.hasNext()) {
			AudioTrack track = x.next();
			q.add(track);
		}
		Collections.shuffle((List<AudioTrack>) q);
		queue.clear();
		for (AudioTrack track : q) {
			queue.offer(track);
		}
	}

	@Override
	public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
		if (endReason.mayStartNext) {
			if (nextYTAutoPlay(track)) {
				player.playTrack(queue.poll());
			}
		}

		if (player.getPlayingTrack() == null && queue.isEmpty()
				&& musicManager.config.isDisconnectAfterLastTrackEnabled()) {
			guild.getAudioManager().closeAudioConnection();
		}
	}

	public boolean nextYTAutoPlay(AudioTrack track) {
		if (musicManager.isAutoPlay() && !(track == null) && track.getSourceManager().getSourceName() == "youtube") {
			try {
				String autoUrl = eyasm.getYTAutoPlayNextVideoId(track);
				if (autoUrl == null) {
					return false;
				} else {
					bot.getPlayerManager().loadItemOrdered(musicManager, autoUrl, new BasicResultHandler(player));
					return true;
				}
			} catch (IOException e) {
				System.out.println("Error on Youtube autoplay.");
				e.printStackTrace();
				return false;
			}
		} else {
			return false;
		}
	}

	@Override
	public void onTrackStart(AudioPlayer player, AudioTrack track) {
		if (player.isPaused()) {
			player.setPaused(false);
		}
	}

	public String getPlaylist() {
		StringBuilder builder = new StringBuilder();
		if (queue.isEmpty()) {
			builder.append("`Queue is empty.`");
		} else {
			builder.append("Tracks queued: `").append(queue.size()).append("`\n");
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

	public List<AudioTrack> getQueue() {
		List<AudioTrack> list = new ArrayList<AudioTrack>();
		for (AudioTrack track : queue) {
			list.add(track);
		}
		return list;
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