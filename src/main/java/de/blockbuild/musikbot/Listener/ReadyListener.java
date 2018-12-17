package de.blockbuild.musikbot.Listener;

import de.blockbuild.musikbot.Bot;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Game.GameType;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class ReadyListener extends ListenerAdapter{

	@Override
	public void onReady(ReadyEvent event) {
		JDA jda = event.getJDA();
		
		jda.getPresence().setPresence(OnlineStatus.ONLINE, Game.of(GameType.DEFAULT, "Ready for playing music. !Play"));
		System.out.println("Invite Token:");
		System.out.println(jda.asBot().getInviteUrl(Bot.RECOMMENDED_PERMS));
	}
	
}
