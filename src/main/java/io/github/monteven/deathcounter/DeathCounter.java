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
	
	public static DeathCounter plugin; // Constructor initialisation
	
	@Override
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(this, this); // Enabling listener to be able to use Events
		getCommand("deaths").setExecutor(new CommandDeaths(this)); // Registering commands
		getCommand("deathshelp").setExecutor(new CommandDeaths(this));
		getLogger().info("DeathCounter has been enabled.");
		createConfig(); // Running the createConfig() function
	}
	
	private void createConfig() {
	    try {
	        if (!getDataFolder().exists()) { // If the directory of DeathCounter does not exist
	            getDataFolder().mkdirs(); // Then make it
	        }
	        File file = new File(getDataFolder(), "config.yml"); // Initiating the creation a new config.yml file
	        if (!file.exists()) { // If a config.yml does not exist, create it
	            getLogger().info("Config.yml not found, creating!");
	            saveDefaultConfig(); // Save the provided config.yml in src/main/resources
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		String uuid = player.getUniqueId().toString();
		if (!getConfig().contains("Players." + uuid)) { // If the config.yml does not contain the player that has joined
			getConfig().set("Players." + uuid + ".Deaths", 0); // Write the default template for that player
			getConfig().set("Players." + uuid + ".DeathsByPlayer", 0);
			saveConfig();
		}
	}
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		String uuid = player.getUniqueId().toString();
		int deaths = getConfig().getInt("Players." + uuid + ".Deaths"); // Getting the amount of deaths of the player that died
		if (player.getKiller() instanceof Player) { // If they died to a player
			int pkdeaths = getConfig().getInt("Players." + uuid + ".DeathsByPlayer"); // Also get the amount of deaths by player
			getConfig().set("Players." + uuid + ".Deaths", deaths +1); // Add one to deaths
			getConfig().set("Players." + uuid + ".DeathsByPlayer", pkdeaths +1); // Add one to player deaths
			saveConfig();
		} else {
			getConfig().set("Players." + uuid + ".Deaths", deaths +1);
			saveConfig();
		}
		player.sendMessage(color("&cYou have died! Your total death count is now " + deaths+1 + ".")); // Print the number of deaths of that player
	}
	
	public String color(String msg) {
		return ChatColor.translateAlternateColorCodes('&', msg); // Enabling usage of short-hand colour codes when printing to user
	}
}
