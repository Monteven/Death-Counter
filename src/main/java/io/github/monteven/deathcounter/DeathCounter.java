package io.github.monteven.deathcounter;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

public final class DeathCounter extends JavaPlugin implements Listener {
	
	public static DeathCounter plugin;
	
	@Override
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(this, this);
		getCommand("deaths").setExecutor(new CommandDeaths(this));
		getCommand("deathshelp").setExecutor(new CommandDeaths(this));
		getLogger().info("DeathCounter has been enabled.");
		createConfig();
	}
	
	private void createConfig() {
	    try {
	        if (!getDataFolder().exists()) {
	            getDataFolder().mkdirs();
	        }
	        File file = new File(getDataFolder(), "config.yml");
	        if (!file.exists()) {
	            getLogger().info("Config.yml not found, creating!");
	            saveDefaultConfig();
	        } else {
	            getLogger().info("Config.yml found, loading!");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();

	    }

	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		String uuid = player.getUniqueId().toString();
		if (!getConfig().contains("Players." + uuid)) {
			getConfig().set("Players." + uuid + ".Deaths", 0);
			getConfig().set("Players." + uuid + ".DeathsByPlayer", 0);
			saveConfig();
		}
	}
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		String uuid = player.getUniqueId().toString();
		int deaths = getConfig().getInt("Players." + uuid + ".Deaths");
		if (player.getKiller() instanceof Player) {
			int pkdeaths = getConfig().getInt("Players." + uuid + ".DeathsByPlayer");
			getConfig().set("Players." + uuid + ".Deaths", deaths +1);
			getConfig().set("Players." + uuid + ".DeathsByPlayer", pkdeaths +1);
			saveConfig();
			player.sendMessage(color("&cYou have died! Your total death count is now " + deaths+1 + "."));
		} else {
			getConfig().set("Players." + uuid + ".Deaths", deaths +1);
			saveConfig();
			player.sendMessage(color("&cYou have died! Your total death count is now " + deaths+1 + "."));
		}
	}
	
	public String color(String msg) {
		return ChatColor.translateAlternateColorCodes('&', msg);
	}
}
