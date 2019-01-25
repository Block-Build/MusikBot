package de.blockbuild.musikbot;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

	private Bot bot;

	@Override
	public void onEnable() {
		try {
			start();
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

	private void start() {
		Bukkit.getScheduler().runTaskLater(this, () -> {
			bot = new Bot(this);
		}, 1L);
	}
}
