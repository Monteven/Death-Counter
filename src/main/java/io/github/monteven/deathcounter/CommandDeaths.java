package io.github.monteven.deathcounter;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class CommandDeaths implements CommandExecutor {
	private final DeathCounter plugin;
	
	public CommandDeaths(DeathCounter plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("deaths") && sender instanceof Player && sender.hasPermission("deathcounter.deaths")) {
			
			if (args.length < 1) {
				Player player = (Player) sender;
				String pUUID = player.getUniqueId().toString();
				String pkUUID = player.getUniqueId().toString();
				int pdeaths = plugin.getConfig().getInt("Players." + pUUID + ".Deaths");
				int pkdeaths = plugin.getConfig().getInt("Players." + pkUUID + ".DeathsByPlayer");
				player.sendMessage(color("&3You&7 have a total of &3" + pdeaths + " deaths&7,&3 " + pkdeaths + " &7of which are from &cenemy players&7."));
				return true;
			
			} else if (args.length == 1) {
				Player player = (Bukkit.getServer().getPlayer(args[0]));
				if (player == null) {
					sender.sendMessage(color("&3" + args[0] + " &7is not online!"));
					return true;
				
				} else {
					String pUUID = player.getUniqueId().toString();
					String pkUUID = player.getUniqueId().toString();
					int pdeaths = plugin.getConfig().getInt("Players." + pUUID + ".Deaths");
					int pkdeaths = plugin.getConfig().getInt("Players." + pkUUID + ".DeathsByPlayer");
					sender.sendMessage(color("&7The player&3 " + player.getName() + " &7has a total of&3 " + pdeaths + " deaths&7,&3 " + pkdeaths + " &7of which are from &cenemy players&7."));
					return true;
				}
			}
		} else if (cmd.getName().equalsIgnoreCase("deathshelp") && sender instanceof Player && sender.hasPermission("deathcounter.help")) {
			Player player = (Player) sender;
			player.sendMessage(color("&3Commands:\n/deaths &7- provides you with your total amount of deaths\n&3/deathshelp &7- provides you with help about Death Counter"));
			return true;
		}
		return false;
	}
	
	public String color(String msg) {
		return ChatColor.translateAlternateColorCodes('&', msg);
	}
}
