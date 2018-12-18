package de.blockbuild.musikbot.Listener;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import de.blockbuild.musikbot.Bot;
import de.blockbuild.musikbot.Main;
import de.blockbuild.musikbot.core.AudioPlayerSendHandler;
import de.blockbuild.musikbot.core.TrackScheduler;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Game.GameType;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class ReadyListener extends ListenerAdapter {
	Main main;
	Bot bot;

	public ReadyListener(Main main, Bot bot) {
		this.main = main;
		this.bot = bot;

	}

	@Override
	public void onReady(ReadyEvent event) {
		JDA jda = event.getJDA();
		AudioPlayer player;
		AudioPlayerManager playerManager = new DefaultAudioPlayerManager();

		jda.getPresence().setPresence(OnlineStatus.ONLINE, Game.of(GameType.DEFAULT, "Ready for playing music. !Play"));
		System.out.println("Invite Token:");
		System.out.println(jda.asBot().getInviteUrl(Bot.RECOMMENDED_PERMS));

		/*
		 * jda.getGuilds().forEach((guild) -> { for (VoiceChannel voice :
		 * guild.getVoiceChannels()) { System.out.println("Name: " + voice.getName() +
		 * "| ID: " + voice.getId().toString()); } });
		 */

		bot.joinDiscordTextChannel(jda, null);
		bot.joinDiscordVoiceChannel(jda, null);

		AudioSourceManagers.registerRemoteSources(playerManager);
		AudioSourceManagers.registerLocalSource(playerManager);
		player = playerManager.createPlayer();
		TrackScheduler trackScheduler = new TrackScheduler(bot.getDefaultTextChannel(), player);
		// AudioSendHandler sendHandler = new AudioPlayerSendHandler(player);
		player.addListener(trackScheduler);
		main.getBot().setScheduler(trackScheduler);
		main.getBot().setPlayerManager(playerManager);

		jda.getGuilds().forEach((guild) -> {
			try {
				guild.getAudioManager().setSendingHandler(new AudioPlayerSendHandler(player));
				// guild.getAudioManager().setSendingHandler(this);
			} catch (Exception ex) {
				System.err.println(ex);
			}
		});

		playerManager.loadItem("https://www.youtube.com/watch?v=UQnFHwz_mUg", new AudioLoadResultHandler() {
			@Override
			public void trackLoaded(AudioTrack track) {
				trackScheduler.playTrack(track, null);
			}

			@Override
			public void playlistLoaded(AudioPlaylist playlist) {
				AudioTrack firstTrack = playlist.getSelectedTrack();

				if (firstTrack == null) {
					firstTrack = playlist.getTracks().get(0);
				}
				trackScheduler.playTrack(firstTrack, null);
			}

			@Override
			public void noMatches() {
				// Notify the user that we've got nothing
			}

			@Override
			public void loadFailed(FriendlyException throwable) {
				// Notify the user that everything exploded
			}
		});
	}
}
