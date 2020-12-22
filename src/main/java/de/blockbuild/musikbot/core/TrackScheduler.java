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
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import de.blockbuild.musikbot.Bot;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

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

	public int queueTrack(AudioTrack track) {
		if (!player.startTrack(track, true)) {
			queue.offer(track);
			return queue.size();
		} else {
			return 0;
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

	public boolean playNextTrack() {
		String url = nextYTAutoPlay(player.getPlayingTrack());
		if (!(url == null)) {
			bot.getPlayerManager().loadItemOrdered(musicManager, url, new BasicResultHandler(musicManager));
			// wait for loading track
			try {
				Thread.sleep(1000);
			} catch (InterruptedException ignore) {
			}
			return true;
		} else if (queue.isEmpty()) {
			player.stopTrack();
			return false;
		} else {
			// wait for loading next track
			player.playTrack(queue.poll());
			try {
				Thread.sleep(400);
			} catch (InterruptedException ignore) {
			}
			return true;
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
			if (playNextTrack()) {
				if (musicManager.config.isNowPlayingTrackEnabled()) {
					TextChannel tc = bot.getTextChannelById(musicManager.config.getNowPlayingTrackTextChannelId());
//						tc.sendMessage(Emoji.MAG_RIGHT.getUtf8() + " Loading...")
//						tc.queue(m -> messageNowPlayingTrackShort(player.getPlayingTrack(), m));
					tc.sendMessage(messageNowPlayingTrackShort(player.getPlayingTrack())).queue(m -> {
						if (musicManager.config.getMessageDeleteDelay() > 0) {
							musicManager.deleteMessageLater(tc, m, musicManager.config.getMessageDeleteDelay());
						}
					});
				}
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

	public int flushQueue(int amount) {
		int num = 0;
		int size = queue.size();
		for (int i = 0; i < amount && i < size; i++) {
			queue.poll();
			num++;
		}
		return num;
	}

	// Messages temporary place
	public final String messageAddTrack(AudioTrack track, Message m) {
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

	public final String messageNowPlayingTrackLong(AudioTrack track, Message m, String prefix) {
		if (track == null) {
			m.delete().queue();
			return null;
		}
		StringBuilder builder = new StringBuilder();
		if (prefix != null) {
			builder.append(prefix).append("\n");
		}
		builder.append(Emoji.NOTES.getUtf8());
		builder.append(" Now playing: **").append(track.getInfo().title).append("**. Left time: (`");
		builder.append(getTime(track.getDuration() - track.getPosition())).append("`) Minutes.");
		m.editMessage(builder.toString()).queue();
		return builder.toString();
	}

	public final String messageNowPlayingTrackShort(AudioTrack track, Message m) {
		if (track == null) {
			m.delete().queue();
			return null;
		}
		StringBuilder builder = new StringBuilder();

		builder.append(Emoji.NOTES.getUtf8());
		builder.append(" Now playing: **").append(track.getInfo().title).append("**.");
		m.editMessage(builder.toString()).queue();
		return builder.toString();
	}

	public final String messageNowPlayingTrackShort(AudioTrack track) {
		if (track == null) {
			return null;
		}
		StringBuilder builder = new StringBuilder();

		builder.append(Emoji.NOTES.getUtf8());
		builder.append(" Now playing: **").append(track.getInfo().title).append("**.");
		return builder.toString();
	}

	public String getTime(long lng) {
		return (new SimpleDateFormat("mm:ss")).format(new Date(lng));
	}

	public String getTimeBig(long lng) {
		return (new SimpleDateFormat("hh:mm:ss")).format(new Date(lng));
	}
}