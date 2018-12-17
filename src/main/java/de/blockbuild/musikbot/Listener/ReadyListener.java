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
import de.blockbuild.musikbot.core.AudioPlayerSendHandler;
import de.blockbuild.musikbot.core.TrackScheduler;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.audio.AudioSendHandler;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Game.GameType;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class ReadyListener extends ListenerAdapter {

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

		joinDiscord(jda);
		AudioSourceManagers.registerRemoteSources(playerManager);
		player = playerManager.createPlayer();
		TrackScheduler trackScheduler = new TrackScheduler(player);
		AudioSendHandler sendHandler = new AudioPlayerSendHandler(player);
		player.addListener(trackScheduler);

		jda.getGuilds().forEach((guild) -> {
			try {
				guild.getAudioManager().setSendingHandler(sendHandler);
				// guild.getAudioManager().setSendingHandler(this);
			} catch (Exception ex) {
				System.err.println(ex);
			}
		});

		playerManager.loadItem("https://www.youtube.com/watch?v=UQnFHwz_mUg", new AudioLoadResultHandler() {
			@Override
			public void trackLoaded(AudioTrack track) {
				// trackScheduler.queue(track);
				trackScheduler.playTrack(track);
			}

			@Override
			public void playlistLoaded(AudioPlaylist playlist) {
				for (AudioTrack track : playlist.getTracks()) {
					// trackScheduler.queue(track);
					trackScheduler.playTrack(track);
				}
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

	private void joinDiscord(JDA jda) {
		jda.getGuilds().forEach((guild) -> {
			try {
				// guild.getAudioManager().openAudioConnection(guild.getVoiceChannelById("253303928345722880"));
				// guild.getAudioManager()
				// .openAudioConnection((VoiceChannel) guild.getVoiceChannelsByName("Sprechstube
				// 1", true).get(0));
				guild.getAudioManager().openAudioConnection(guild.getVoiceChannels().get(2));
			} catch (IllegalArgumentException e) {
				System.out.println("no VoiceChannel");
			} catch (InsufficientPermissionException e) {
				System.out.println("Missing permission: " + e.getPermission() + " to join '"
						+ guild.getVoiceChannels().get(2).getName() + "'");
			} catch (Exception e) {
				System.err.println(e);
			}
		});
	}
}
