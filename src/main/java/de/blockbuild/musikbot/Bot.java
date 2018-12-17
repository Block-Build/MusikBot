package de.blockbuild.musikbot;

import javax.security.auth.login.LoginException;

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
	public static Permission[] RECOMMENDED_PERMS = new Permission[] { Permission.MESSAGE_READ, Permission.MESSAGE_WRITE,
			Permission.MESSAGE_HISTORY, Permission.MESSAGE_ADD_REACTION, Permission.MESSAGE_EMBED_LINKS,
			Permission.MESSAGE_ATTACH_FILES, Permission.MESSAGE_MANAGE, Permission.MESSAGE_EXT_EMOJI,
			Permission.MANAGE_CHANNEL, Permission.VOICE_CONNECT, Permission.VOICE_SPEAK, Permission.NICKNAME_CHANGE, Permission.MESSAGE_TTS };

	public Bot() {
		start();
		initListener();
	}

	public boolean start() {
		try {
			jda = new JDABuilder(AccountType.BOT)
					.setToken("NTIzOTI3MzY3NDY3NjYzNDAx.Dvh6cg.r6rrETJfOYRBJp2Xc3l-zPn_BuY")
					.setGame(Game.of(GameType.DEFAULT, "starting...")).setAudioEnabled(true)
					.setStatus(OnlineStatus.DO_NOT_DISTURB)
					.addEventListener(new ReadyListener())
					.build();
			jda.awaitReady();
			System.out.println("Bot started successfully");
		} catch (LoginException e) {
			System.out.println("Invaild bot Token");
			return false;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean stop() {
		jda.shutdown();
		System.out.println("Bot has stopped");
		return true;
	}

	public void initListener() {
		jda.addEventListener(new MessageListener());
		jda.addEventListener(new ReadyListener());
	}
}
