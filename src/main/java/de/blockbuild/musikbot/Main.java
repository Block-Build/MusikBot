package de.blockbuild.musikbot;

import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import de.blockbuild.musikbot.metrics.Metrics;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;

public class Main extends JavaPlugin {

	private Bot bot;

	@Override
	public void onEnable() {
		try {
			start();
			Metrics metrics = new Metrics(this);
			
			metrics.addCustomChart(new Metrics.SingleLineChart("guilds_lin", () -> bot.getJda().getGuilds().size()));

			metrics.addCustomChart(
					new Metrics.SimplePie("guilds_pie", () -> String.valueOf(bot.getJda().getGuilds().size())));

			metrics.addCustomChart(new Metrics.SingleLineChart("streams_lin", () -> {
				int counter = 0;
				for (Guild guild : bot.getJda().getGuilds())
					if (bot.getGuildAudioPlayer(guild).getAudioPlayer().getPlayingTrack() != null) {
						counter++;
					}
				return counter;
			}));

			metrics.addCustomChart(new Metrics.SingleLineChart("users_lin", () -> bot.getJda().getUsers().size()));

			metrics.addCustomChart(new Metrics.SingleLineChart("users_online_lin", () -> {
				int counter = 0;
				ArrayList<Member> members = new ArrayList<Member>();
				for (Guild guild : bot.getJda().getGuilds()) {
					members.addAll(guild.getMembers());
				}

				for (Member member : members) {
					if (member.getOnlineStatus() != OnlineStatus.OFFLINE
							&& member.getOnlineStatus() != OnlineStatus.UNKNOWN) {
						counter++;
					}
				}
				return counter;
			}));

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	@Override
	public void onDisable() {
		if (bot != null) {
			bot.stop();
		}
	}

	public void reload() {
		Plugin plugin = this.getServer().getPluginManager().getPlugin(this.getName());
		this.getServer().getPluginManager().disablePlugin(plugin);
		this.getServer().getPluginManager().enablePlugin(plugin);
	}

	private void start() {
		Bukkit.getScheduler().runTaskLater(this, () -> {
			bot = new Bot(this);
		}, 1L);
	}
}
