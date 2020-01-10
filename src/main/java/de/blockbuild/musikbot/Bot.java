package de.blockbuild.musikbot;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.security.auth.login.LoginException;

import org.apache.commons.io.FileUtils;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.command.Command.Category;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;

import de.blockbuild.musikbot.Listener.MessageListener;
import de.blockbuild.musikbot.Listener.VoiceChannelListener;
import de.blockbuild.musikbot.commands.connection.JoinCommand;
import de.blockbuild.musikbot.commands.connection.PingCommand;
import de.blockbuild.musikbot.commands.connection.QuitCommand;
import de.blockbuild.musikbot.commands.general.InfoCommand;
import de.blockbuild.musikbot.commands.general.VersionCommand;
import de.blockbuild.musikbot.commands.music.ChooseCommand;
import de.blockbuild.musikbot.commands.music.FlushQueue;
import de.blockbuild.musikbot.commands.music.NextCommand;
import de.blockbuild.musikbot.commands.music.PauseCommand;
import de.blockbuild.musikbot.commands.music.PlayCommand;
import de.blockbuild.musikbot.commands.music.PlaylistCommand;
import de.blockbuild.musikbot.commands.music.QueueCommand;
import de.blockbuild.musikbot.commands.music.ResumeCommand;
import de.blockbuild.musikbot.commands.music.ShuffleCommand;
import de.blockbuild.musikbot.commands.music.SkipCommand;
import de.blockbuild.musikbot.commands.music.StopCommand;
import de.blockbuild.musikbot.commands.music.VolumeCommand;
import de.blockbuild.musikbot.commands.music.YTAutoPlayCommand;
import de.blockbuild.musikbot.commands.radio.RadioBobCommand;
import de.blockbuild.musikbot.commands.radio.RadioBonnRheinSiegCommand;
import de.blockbuild.musikbot.commands.radio.RadioMnmCommand;
import de.blockbuild.musikbot.commands.radio.RautemusikCommand;
import de.blockbuild.musikbot.commands.setup.AutoConnectCommand;
import de.blockbuild.musikbot.commands.setup.AutoDisconnectCommand;
import de.blockbuild.musikbot.commands.setup.BlacklistCommand;
import de.blockbuild.musikbot.commands.setup.ConfigCommand;
import de.blockbuild.musikbot.commands.setup.DefaultTextChannelCommand;
import de.blockbuild.musikbot.commands.setup.DefaultVoiceChannelCommand;
import de.blockbuild.musikbot.commands.setup.ReloadCommand;
import de.blockbuild.musikbot.commands.setup.WhitelistCommand;
import de.blockbuild.musikbot.configuration.BotConfiguration;
import de.blockbuild.musikbot.core.GuildMusicManager;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Game.GameType;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Icon;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.core.entities.VoiceChannel;

public class Bot {
	public final static Permission[] BASIC_PERMS = new Permission[] { Permission.MESSAGE_READ, Permission.MESSAGE_WRITE,
			Permission.MESSAGE_HISTORY, Permission.MESSAGE_ADD_REACTION, Permission.MESSAGE_EMBED_LINKS,
			Permission.VOICE_CONNECT, Permission.VOICE_SPEAK, Permission.MESSAGE_TTS };
	private final Main main;
	private final AudioPlayerManager playerManager;
	private final Map<Long, GuildMusicManager> musicManagers;
	private JDA jda;
	public final BotConfiguration config;

	public Bot(Main main) {
		System.out.println("[" + main.getName() + "] Get started...");

		this.main = main;
		musicManagers = new HashMap<>();
		playerManager = new DefaultAudioPlayerManager();
		config = new BotConfiguration(this);

		try {
			FileUtils.copyInputStreamToFile(main.getResource("Sample_BotConfig.yml"),
					new File(main.getDataFolder(), "Sample_BotConfig.yml"));
			FileUtils.copyInputStreamToFile(main.getResource("Sample_GuildConfig.yml"),
					new File(main.getDataFolder(), "Sample_GuildConfig.yml"));
		} catch (IOException e) {
			System.err.println("[" + main.getName() + "] Can't write Sample_Configs.");
			e.printStackTrace();
		}

		if (start()) {
			initListeners();
			initCommandClient();
			System.out.println("[" + main.getName() + "] Started successfully");
		} else {
			System.out.println("[" + main.getName() + "] Shut down");
			main.onDisable();
		}
	}

	public boolean start() {
		try {
			String token = config.getToken();
			if (token == null || token.isEmpty()) {
				System.out.println("No token was provided. Please provide a vaild token.");
				System.out.println("Without a token the Bot will not be able to start.");
				return false;
			} else if (token.equals("Insert Token here")) {
				System.out.println("Token was left at default. Please provide a vaild token.");
				System.out.println("Without a token the Bot will not be able to start.");
				return false;
			}
			jda = new JDABuilder(AccountType.BOT).setToken(token).setGame(Game.of(GameType.DEFAULT, "starting..."))
					.setAudioEnabled(true).setStatus(OnlineStatus.DO_NOT_DISTURB).build();
			jda.awaitReady();
		} catch (LoginException e) {
			System.out.println("Invalid bot Token");
			return false;
		} catch (InterruptedException e) {
			// Should never triggered!
			e.printStackTrace();
			return false;
		}

		try {
			jda.getSelfUser().getManager().setAvatar(Icon.from(main.getResource("64.png"))).queue(unused -> {
			}, ignored -> {
			});
		} catch (IOException e) {
			System.err.println(e);
		}

		jda.getPresence().setPresence(OnlineStatus.ONLINE, Game.of(GameType.DEFAULT, config.getGame()));
		if (!jda.getSelfUser().getName().equalsIgnoreCase("MusikBot")) {
			jda.getSelfUser().getManager().setName("MusikBot").queue();
		}

		// Print invite token to console
		System.out.println("Invite Token:");
		String inviteURL = jda.asBot().getInviteUrl(Bot.BASIC_PERMS);
		System.out.println(inviteURL);
		config.setInviteLink(inviteURL);

		AudioSourceManagers.registerRemoteSources(playerManager);
		AudioSourceManagers.registerLocalSource(playerManager);

		jda.getGuilds().forEach((guild) -> {
			getGuildAudioPlayer(guild);
		});
		return true;
	}

	public void stop() {
		if (!(jda == null)) {
			jda.shutdown();
			jda = null;
		}
	}

	public synchronized GuildMusicManager getGuildAudioPlayer(Guild guild) {
		GuildMusicManager musicManager = musicManagers.get(guild.getIdLong());

		if (musicManager == null) {
			musicManager = new GuildMusicManager(playerManager, guild, this);
			musicManagers.put(guild.getIdLong(), musicManager);

			guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());
		}
		return musicManager;
	}

	public void initListeners() {
		jda.addEventListener(new MessageListener(this));
		jda.addEventListener(new VoiceChannelListener());
	}

	public void initCommandClient() {
		String ownerID = config.getOwnerID();
		String trigger = config.getTrigger();
		CommandClientBuilder ccb = new CommandClientBuilder();
		ccb.setOwnerId(ownerID);
		ccb.setCoOwnerIds("240566179880501250");
		ccb.useHelpBuilder(true);
		ccb.setHelpConsumer((event) -> {
			// Slightly changed default helpConsumer from JDA-Utilities.
			StringBuilder builder = new StringBuilder("**" + event.getSelfUser().getName() + "** commands:\n");
			Category category = null;
			for (Command command : event.getClient().getCommands()) {
				if (!command.isHidden() && (!command.isOwnerCommand() || event.isOwner())) {
					if (category == null || !(category.getName() == command.getCategory().getName())) {
						category = command.getCategory();
						builder.append("\n\n  __").append(category == null ? "No Category" : category.getName())
								.append("__:\n");
					}
					builder.append("\n`").append(event.getClient().getPrefix())
							.append(event.getClient().getPrefix() == null ? " " : "").append(command.getName())
							.append(command.getArguments() == null ? "`" : " " + command.getArguments() + "`")
							.append(" - ").append(command.getHelp());
				}
			}
			User owner = event.getJDA().getUserById(event.getClient().getOwnerIdLong());
			if (owner != null) {
				builder.append("\n\nFor additional help, contact **").append(owner.getName()).append("**#")
						.append(owner.getDiscriminator());
			}
			event.replyInDm(builder.toString(), unused -> {
				if (event.isFromType(ChannelType.TEXT))
					event.reactSuccess();
			}, t -> event.replyWarning("Help cannot be sent because you are blocking Direct Messages."));
		});
		ccb.setEmojis(config.getSuccess(), config.getWarning(), config.getError());
		ccb.setPrefix(trigger);
		ccb.setLinkedCacheSize(100);
		registerCommandModule(ccb,
				//Music
				new PlayCommand(this), 
				new QueueCommand(this),
				new NextCommand(this), 
				new SkipCommand(this),
				new ChooseCommand(this),
				new FlushQueue(this),
				new ShuffleCommand(this),
				new PlaylistCommand(this),
				new YTAutoPlayCommand(this),
				new VolumeCommand(this), 
				new PauseCommand(this),
				new ResumeCommand(this),
				new StopCommand(this), 
				
				//General
				new InfoCommand(this),
				new VersionCommand(this),
  
				//Radio
				new RadioBonnRheinSiegCommand(this), 
				new RautemusikCommand(this), 
				new RadioBobCommand(this),
				new RadioMnmCommand(this),
				
				//Connection
				new JoinCommand(this), 
				new QuitCommand(this),
				new PingCommand(this),
				
				//Setup
				new BlacklistCommand(this),
				new WhitelistCommand(this),
				new AutoDisconnectCommand(this),
				new AutoConnectCommand(this),
				new DefaultTextChannelCommand(this),
				new DefaultVoiceChannelCommand(this),
				new ConfigCommand(this),
				new ReloadCommand(this));

		jda.addEventListener(ccb.build());
	}

	public void registerCommandModule(CommandClientBuilder ccb, Command... commands) {
		for (Command c : commands) {
			ccb.addCommand(c);
		}
	}

	public boolean joinDiscordVoiceChannel(Guild guild) {
		for (int i = 0; i < guild.getVoiceChannels().size(); i++) {
			try {
				guild.getAudioManager().openAudioConnection(guild.getVoiceChannels().get(i));
				return true;
			} catch (IllegalArgumentException e) {
				System.out.println("no VoiceChannel");
			} catch (InsufficientPermissionException e) {
				System.out.println("Missing permission: " + e.getPermission() + " to join '"
						+ guild.getVoiceChannels().get(i).getName() + "'");
			} catch (Exception e) {
				System.err.println(e);
			}
		}
		return false;
	}

	public boolean joinDiscordVoiceChannel(Guild guild, Long id) {
		try {
			guild.getAudioManager().openAudioConnection(guild.getVoiceChannelById(id));
			return true;
		} catch (IllegalArgumentException e) {
			System.out.println(id + " is not a VoiceChannel");
		} catch (InsufficientPermissionException e) {
			System.out.println("Missing permission: " + e.getPermission() + " to join '" + id + "'");
		} catch (Exception e) {
			System.out.println(id + " isn't a vaild VoiceChannel");
		}
		return false;
	}

	public boolean joinDiscordVoiceChannel(Guild guild, String name) {
		try {
			guild.getAudioManager().openAudioConnection((VoiceChannel) guild.getVoiceChannelsByName(name, true).get(0));
			return true;
		} catch (IllegalArgumentException e) {
			System.out.println(name + " is not a VoiceChannel");
		} catch (InsufficientPermissionException e) {
			System.out.println("Missing permission: " + e.getPermission() + " to join '"
					+ guild.getVoiceChannels().get(0).getName() + "'");
		} catch (Exception e) {
			System.out.println(name + " isn't a vaild VoiceChannel");
		}
		return false;
	}

	public JDA getJda() {
		return jda;
	}

	public Main getMain() {
		return main;
	}

	public AudioPlayerManager getPlayerManager() {
		return playerManager;
	}

	public User getUserById(String id) {
		return this.jda.getUserById(id);
	}

	public User getUserById(Long id) {
		return this.jda.getUserById(id);
	}

	public String getUserNameById(String id) {
		User user = getUserById(id);
		if (user == null) {
			return "UNKNOWN";
		} else {
			return user.getName();
		}
	}

	public String getUserNameById(Long id) {
		User user = getUserById(id);
		if (user == null) {
			return "UNKNOWN";
		} else {
			return user.getName();
		}
	}

	public TextChannel getTextChannelById(Long id) {
		return this.jda.getTextChannelById(id);
	}

	public VoiceChannel getVoiceChannelById(Long id) {
		return this.jda.getVoiceChannelById(id);
	}

	public Guild getGuildById(String id) {
		try {
			return this.jda.getGuildById(id);
		} catch (Exception e) {
			return null;
		}
	}

	public Guild getGuildById(Long id) {
		try {
			return this.jda.getGuildById(id);
		} catch (Exception e) {
			return null;
		}
	}
}
