package io.github.monteven.deathcounter;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class CommandDeaths implements CommandExecutor {
	
	private final DeathCounter plugin; // Constructor to link to the super class DeathCounter
	
	public CommandDeaths(DeathCounter plugin) { // To link this class to DeathCounter as being in the same plugin
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("deaths") && sender instanceof Player && sender.hasPermission("deathcounter.deaths")) { // If they send /deaths, ignoring case, is a player, and has permission to do so
			
			if (args.length < 1) { // If it has zero arguments
				Player player = (Player) sender;
				String pUUID = player.getUniqueId().toString();
				String pkUUID = player.getUniqueId().toString();
				int pdeaths = plugin.getConfig().getInt("Players." + pUUID + ".Deaths"); // Get number of deaths
				int pkdeaths = plugin.getConfig().getInt("Players." + pkUUID + ".DeathsByPlayer"); // Get number of deaths by players
				player.sendMessage(color("&3You&7 have a total of &3" + pdeaths + " deaths&7,&3 " + pkdeaths + " &7of which are from &cenemy players&7.")); // Print the number of deaths, and deaths by players
				return true;
			
			} else if (args.length == 1) { // If there is exactly one argument (which will be a name)
				Player player = (Bukkit.getServer().getPlayer(args[0]));
				if (player == null) { // If the name is not on the server
					sender.sendMessage(color("&3" + args[0] + " &7is not online!")); // State that their data cannot be retrieved as they are not online (don't know why I did this, maybe laziness)
					return true;
				} else { // If the player is online
					String pUUID = player.getUniqueId().toString();
					String pkUUID = player.getUniqueId().toString();
					int pdeaths = plugin.getConfig().getInt("Players." + pUUID + ".Deaths");
					int pkdeaths = plugin.getConfig().getInt("Players." + pkUUID + ".DeathsByPlayer");
					sender.sendMessage(color("&7The player&3 " + player.getName() + " &7has a total of&3 " + pdeaths + " deaths&7,&3 " + pkdeaths + " &7of which are from &cenemy players&7.")); // Print their number of deaths, and deaths by players
					return true;
				}
			}
		} else if (cmd.getName().equalsIgnoreCase("deathshelp") && sender instanceof Player && sender.hasPermission("deathcounter.help")) { // If they send /deathshelp, ignoring case, is a player, and has permission to do so
			Player player = (Player) sender;
			player.sendMessage(color("&3Commands:\n/deaths &7- provides you with your total amount of deaths\n&3/deathshelp &7- provides you with help about Death Counter")); // Print help
			return true;
		}
		return false;
	}
	
	public String color(String msg) {
		return ChatColor.translateAlternateColorCodes('&', msg);
	}
}
