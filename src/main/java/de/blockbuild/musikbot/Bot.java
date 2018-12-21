package de.blockbuild.musikbot;

import javax.security.auth.login.LoginException;

import com.jagrosh.jdautilities.commandclient.Command;
import com.jagrosh.jdautilities.commandclient.CommandClient;
import com.jagrosh.jdautilities.commandclient.CommandClientBuilder;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

import de.blockbuild.musikbot.Listener.MessageListener;
import de.blockbuild.musikbot.Listener.ReadyListener;
import de.blockbuild.musikbot.commands.FlushQueue;
import de.blockbuild.musikbot.commands.InfoCommand;
import de.blockbuild.musikbot.commands.JoinCommand;
import de.blockbuild.musikbot.commands.NextCommand;
import de.blockbuild.musikbot.commands.PauseCommand;
import de.blockbuild.musikbot.commands.PingCommand;
import de.blockbuild.musikbot.commands.PlayCommand;
import de.blockbuild.musikbot.commands.QueueCommand;
import de.blockbuild.musikbot.commands.QuitCommand;
import de.blockbuild.musikbot.commands.RadioBonnRheinSiegCommand;
import de.blockbuild.musikbot.commands.RautemusikCommand;
import de.blockbuild.musikbot.commands.ResumeCommand;
import de.blockbuild.musikbot.commands.SkipCommand;
import de.blockbuild.musikbot.commands.StopCommand;
import de.blockbuild.musikbot.commands.VolumeCommand;
import de.blockbuild.musikbot.core.TrackScheduler;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Game.GameType;
import net.dv8tion.jda.core.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.VoiceChannel;

public class Bot {
	private JDA jda;
	private CommandClientBuilder ccb;
	private CommandClient client;
	public static Permission[] RECOMMENDED_PERMS = new Permission[] { Permission.MESSAGE_READ, Permission.MESSAGE_WRITE,
			Permission.MESSAGE_HISTORY, Permission.MESSAGE_ADD_REACTION, Permission.MESSAGE_EMBED_LINKS,
			Permission.MESSAGE_ATTACH_FILES, Permission.MESSAGE_MANAGE, Permission.MESSAGE_EXT_EMOJI,
			Permission.MANAGE_CHANNEL, Permission.VOICE_CONNECT, Permission.VOICE_SPEAK, Permission.NICKNAME_CHANGE,
			Permission.MESSAGE_TTS };
	public static Permission[] REQUIRED_PERMS = new Permission[] { Permission.MESSAGE_READ, Permission.MESSAGE_WRITE, Permission.MESSAGE_ADD_REACTION, Permission.MESSAGE_EMBED_LINKS,
			Permission.MESSAGE_MANAGE, Permission.MESSAGE_EXT_EMOJI, Permission.VOICE_CONNECT, Permission.VOICE_SPEAK,
			Permission.MESSAGE_TTS };
	private Main main;
	private TrackScheduler ts;
	private AudioPlayerManager playerManager;
	private TextChannel defaultChannel = null;
	// updateable map

	public Bot(Main main) {
		this.main = main;
		main.setBot(this);
		ccb = new CommandClientBuilder();
		start();
		initListeners();
		initCommandClient();
	}

	public boolean start() {
		try {
			String token = "NTIzOTI3MzY3NDY3NjYzNDAx.Dvh6cg.r6rrETJfOYRBJp2Xc3l-zPn_BuY";
			jda = new JDABuilder(AccountType.BOT).setToken(token).setGame(Game.of(GameType.DEFAULT, "starting..."))
					.setAudioEnabled(true).setStatus(OnlineStatus.DO_NOT_DISTURB)
					.addEventListener(new ReadyListener(main, this)).build();
			jda.awaitReady();
			return true;
		} catch (LoginException e) {
			System.out.println("Invaild bot Token");
			return false;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean stop() {
		jda.shutdown();
		System.out.println("Bot has stopped");
		return true;
	}

	public void initListeners() {
		jda.addEventListener(new MessageListener());
	}

	public void initCommandClient() {
		String ownerID = "240566179880501250";
		String trigger = "!";
		ccb.setOwnerId(ownerID);
		ccb.setCoOwnerIds("240566179880501250");
		ccb.useHelpBuilder(true); // maybe later
		ccb.setEmojis("\uD83D\uDE03", "\uD83D\uDE2E", "\uD83D\uDE26");
		ccb.setPrefix(trigger);
		ccb.setAlternativePrefix("-");
		registerCommandModule(new VolumeCommand(main), new PlayCommand(main), new QueueCommand(main), new SkipCommand(main), new FlushQueue(main), new NextCommand(main), new PauseCommand(main), new ResumeCommand(main), new RautemusikCommand(main), new RadioBonnRheinSiegCommand(main), new InfoCommand(main), new JoinCommand(main), new QuitCommand(main), new StopCommand(main), new PingCommand(main));
		
		/*missing commands:
		 * ##Playback commands##
		 * jump to time?
		 * shuffle?
		 * 
		 * ##setup commands##
		 * defaultTextChannel
		 * defaultVoiceCannel
		 * setDefaultVolume? or just save volume
		 * defaultPlaylist?
		 * setIcon?
		 * 
		 * ##other##
		 * auto pause?
		 */
		client = ccb.build();
		jda.addEventListener(client);
	}

	public void registerCommandModule(Command... commands) {
		for (Command c : commands) {
			ccb.addCommand(c);
		}
	}

	public void joinDiscordVoiceChannel(JDA jda, String nameOrID) {
		int name = -1;
		try {
			name = Integer.parseInt(nameOrID);
		} catch (NumberFormatException e) {
			// No err, just a string
		}
		if (nameOrID == null) {
			jda.getGuilds().forEach((guild) -> {
				try {
					guild.getAudioManager().openAudioConnection(guild.getVoiceChannels().get(0));
				} catch (IllegalArgumentException e) {
					System.out.println("no VoiceChannel");
				} catch (InsufficientPermissionException e) {
					System.out.println("Missing permission: " + e.getPermission() + " to join '"
							+ guild.getVoiceChannels().get(2).getName() + "'");
				} catch (Exception e) {
					System.err.println(e);
				}
			});
		} else if (name == -1) {
			jda.getGuilds().forEach((guild) -> {
				try {
					guild.getAudioManager()
							.openAudioConnection((VoiceChannel) guild.getVoiceChannelsByName(nameOrID, true).get(0));
				} catch (IllegalArgumentException e) {
					System.out.println("no VoiceChannel");
				} catch (InsufficientPermissionException e) {
					System.out.println("Missing permission: " + e.getPermission() + " to join '"
							+ guild.getVoiceChannels().get(2).getName() + "'");
				} catch (Exception e) {
					System.err.println(e);
				}
			});
		} else {
			jda.getGuilds().forEach((guild) -> {
				try {
					guild.getAudioManager().openAudioConnection(guild.getVoiceChannelById(nameOrID));
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

	public void joinDiscordTextChannel(JDA jda, String nameOrID) {
		int name = -1;
		try {
			name = Integer.parseInt(nameOrID);
		} catch (NumberFormatException e) {
			// No err, just a string
		}
		if (nameOrID == null) {
			jda.getGuilds().forEach((guild) -> {
				TextChannel textChannel = null;
				for (TextChannel tc : guild.getTextChannels()) {
					if (tc.canTalk(guild.getMemberById("523927367467663401"))) {
						textChannel = tc;
						break;
					}
				}

				if (textChannel == null) {
					System.out.println("No read an write permission on '" + guild.getName() + "'");
				} else {
					this.defaultChannel = textChannel;
				}
			});
		} else if (name == -1) {
			jda.getGuilds().forEach((guild) -> {
				TextChannel textChannel = null;

				for (TextChannel tc : guild.getTextChannelsByName(nameOrID, true)) {
					if (tc.canTalk(guild.getMemberById("523927367467663401"))) {
						textChannel = tc;
						break;
					}
				}

				if (textChannel == null) {
					System.out.println("No permissions or no matching TextChannel was found with '" + nameOrID
							+ "' on '" + guild.getName() + "'");
				} else {
					this.defaultChannel = textChannel;
				}
			});
		} else {
			jda.getGuilds().forEach((guild) -> {
				TextChannel textChannel = guild.getTextChannelById(nameOrID);
				if (textChannel == null) {
					System.out.println("No TextChannel with ID '" + nameOrID + "' found!");
				} else {
					this.defaultChannel = textChannel;
				}
			});
		}
	}

	// Get and set

	public CommandClient getClient() {
		return client;
	}

	public JDA getJda() {
		return jda;
	}

	public Main getMain() {
		return main;
	}

	public TrackScheduler getScheduler() {
		return ts;
	}

	public void setScheduler(TrackScheduler trackScheduler) {
		this.ts = trackScheduler;
	}

	public AudioPlayerManager getPlayerManager() {
		return playerManager;
	}

	public void setPlayerManager(AudioPlayerManager playerManager) {
		this.playerManager = playerManager;
	}

	public TextChannel getDefaultTextChannel() {
		return this.defaultChannel;
	}
}
