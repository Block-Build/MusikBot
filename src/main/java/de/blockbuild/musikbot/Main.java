package de.blockbuild.musikbot;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import de.blockbuild.musikbot.metrics.Metrics;

public class Main extends JavaPlugin {

	private Bot bot;

	@Override
	public void onEnable() {
		try {
			start();
			Metrics metrics = new Metrics(this);
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
