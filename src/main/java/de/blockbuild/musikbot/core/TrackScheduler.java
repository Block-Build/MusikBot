package de.blockbuild.musikbot.core;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.github.breadmoirai.discordemoji.Emoji;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventListener;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import de.blockbuild.musikbot.Bot;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;

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

	@Deprecated
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

	public int queueTrack(AudioTrack track) {
		if (!player.startTrack(track, true)) {
			queue.offer(track);
			return queue.size();
		} else {
			return 0;
		}
	}

	@Deprecated
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

	@Deprecated
	public void queueSilent(AudioTrack track) {
		if (!player.startTrack(track, true)) {
			queue.offer(track);
		}
	}

	@Deprecated
	public void playTrack(AudioTrack track, CommandEvent event) {
		if (!(event == null)) {
			StringBuilder builder = new StringBuilder(event.getClient().getSuccess());
			builder.append(" Successfully added: `").append(track.getInfo().title).append("`");
			event.reply(builder.toString());
		}
		player.startTrack(track, false);
	}

	public boolean playTrack(AudioTrack track) {
		return player.startTrack(track, false);
	}

	public void nextTrack(CommandEvent event) {
		StringBuilder builder = new StringBuilder();
		String url = nextYTAutoPlay(player.getPlayingTrack());
		if (!(url == null)) {
			bot.getPlayerManager().loadItemOrdered(musicManager, url,
					new BasicResultHandler(player, event, "Now Playing: `%s`."));
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

	public AudioTrack playNextTrack() {
		AudioTrack track = queue.peek();
		if (track == null) {
			return null;
		} else {
			playTrack(queue.poll());
			return track;
		}
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
			String url = nextYTAutoPlay(track);
			if (url == null) {
				player.playTrack(queue.poll());
			} else {
				bot.getPlayerManager().loadItemOrdered(musicManager, url, new BasicResultHandler(player, null, null));
			}
		}

		if (player.getPlayingTrack() == null && queue.isEmpty()
				&& musicManager.config.isDisconnectAfterLastTrackEnabled()) {
			guild.getAudioManager().closeAudioConnection();
		}
	}

	public String nextYTAutoPlay(AudioTrack track) {
		if (musicManager.isAutoPlay() && !(track == null) && track.getSourceManager().getSourceName() == "youtube") {
			try {
				String autoUrl = eyasm.getYTAutoPlayNextVideoId(track);
				if (autoUrl == null) {
					return null;
				} else {
					// bot.getPlayerManager().loadItemOrdered(musicManager, autoUrl, new
					// BasicResultHandler(player));
					return autoUrl;
				}
			} catch (IOException e) {
				System.out.println("Error on Youtube autoplay.");
				e.printStackTrace();
				return null;
			}
		} else {
			return null;
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

	public boolean isQueueEmpty() {
		return queue.isEmpty();
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

	// Messages temporary place
	public final String messagePlayTrack(AudioTrack track, Message m) {
		StringBuilder builder = new StringBuilder(Emoji.ARROW_FORWARD.getUtf8());
		builder.append(" Successfully added: **").append(track.getInfo().title).append("**");
		builder.append(" (`").append(getTime(track.getDuration())).append("`)");
		m.editMessage(builder.toString()).queue();
		return builder.toString();
	}

	public final String messageQueueTrack(AudioTrack track, Message m, int position) {
		StringBuilder builder = new StringBuilder(Emoji.PENCIL.getUtf8());
		builder.append(" Successfully queued: **").append(track.getInfo().title).append("**");
		builder.append(" (`").append(getTime(track.getDuration())).append("`)");
		builder.append(" at position **" + position + "**");
		m.editMessage(builder.toString()).queue();
		return builder.toString();
	}

	public String getTime(long lng) {
		return (new SimpleDateFormat("mm:ss")).format(new Date(lng));
	}

	public String getTimeBig(long lng) {
		return (new SimpleDateFormat("hh:mm:ss")).format(new Date(lng));
	}
}