package de.blockbuild.musikbot;

import java.util.Map;

import javax.security.auth.login.LoginException;

import com.jagrosh.jdautilities.commandclient.Command;
import com.jagrosh.jdautilities.commandclient.CommandClient;
import com.jagrosh.jdautilities.commandclient.CommandClientBuilder;

import de.blockbuild.musikbot.Listener.MessageListener;
import de.blockbuild.musikbot.Listener.ReadyListener;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Game.GameType;

public class Bot {
	private JDA jda;
	private CommandClientBuilder ccb;
	private CommandClient client;
	public static Permission[] RECOMMENDED_PERMS = new Permission[] { Permission.MESSAGE_READ, Permission.MESSAGE_WRITE,
			Permission.MESSAGE_HISTORY, Permission.MESSAGE_ADD_REACTION, Permission.MESSAGE_EMBED_LINKS,
			Permission.MESSAGE_ATTACH_FILES, Permission.MESSAGE_MANAGE, Permission.MESSAGE_EXT_EMOJI,
			Permission.MANAGE_CHANNEL, Permission.VOICE_CONNECT, Permission.VOICE_SPEAK, Permission.NICKNAME_CHANGE,
			Permission.MESSAGE_TTS };
	private Main main;
	//updateable map

	public Bot(Main main) {
		this.main = main;
		ccb = new CommandClientBuilder();
		start();
		initListeners();
		initCommandClient();
	}

	public boolean start() {
		try {
			String token = "NTIzOTI3MzY3NDY3NjYzNDAx.Dvh6cg.r6rrETJfOYRBJp2Xc3l-zPn_BuY";
			jda = new JDABuilder(AccountType.BOT)
					.setToken(token)
					.setGame(Game.of(GameType.DEFAULT, "starting...")).setAudioEnabled(true)
					.setStatus(OnlineStatus.DO_NOT_DISTURB).addEventListener(new ReadyListener()).build();
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
		jda.addEventListener(new ReadyListener());
	}
	
	public void initCommandClient() {
		String ownerID = "240566179880501250";
		String trigger = "!";
		ccb.setOwnerId(ownerID);
		ccb.setCoOwnerIds("240566179880501250");
		ccb.useHelpBuilder(false); //maybe later
		ccb.setEmojis("\uD83D\uDE03", "\uD83D\uDE2E", "\uD83D\uDE26");
		ccb.setPrefix(trigger);
		registerCommandModule();
		client = ccb.build();
		jda.addEventListener(client);
	}
	
	public void registerCommandModule(Command...commands) {
		for (Command c : commands) {
            ccb.addCommand(c);
        }
	}
	
	
	

    public CommandClient getClient() {
        return client;
    }

    public JDA getJda() {
        return jda;
    }

    public Main getMain() {
        return main;
    }
}
