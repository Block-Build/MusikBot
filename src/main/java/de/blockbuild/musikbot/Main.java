package de.blockbuild.musikbot;

import java.io.File;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import de.blockbuild.musikbot.metrics.Metrics;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;

public class Main extends JavaPlugin {

	private Bot bot;
	private String FilePath;

	@Override
	public void onEnable() {
		try {
			setFilePath();
			start();
			Metrics();
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

	private void Metrics() {
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
			Set<User> users = new HashSet<User>();
			for (Guild guild : bot.getJda().getGuilds()) {
				guild.getMembers().forEach((member) -> {
					if (member.getOnlineStatus() != OnlineStatus.OFFLINE
							&& member.getOnlineStatus() != OnlineStatus.UNKNOWN) {
						users.add(member.getUser());
					}
				});
			}
			return users.size();
		}));
	}

	private final void setFilePath() {
		FilePath = Paths.get("").toAbsolutePath().toString() + File.separator + "plugins" + File.separator
				+ getName().toString();
	}

	public final String getFilePath() {
		return FilePath;
	}
}
