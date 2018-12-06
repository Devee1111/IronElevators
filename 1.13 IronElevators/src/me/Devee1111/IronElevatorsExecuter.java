package me.Devee1111;

import java.io.File;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class IronElevatorsExecuter implements CommandExecutor {

	IronElevatorsMain instance;
	IronElevatorsMain main;
	FileConfiguration config = null;
	
	public IronElevatorsExecuter(IronElevatorsMain plugin) {
		this.instance = plugin;
		config = plugin.getConfig();
		main = plugin;
	}
	
	void reloadConfig() {
		File configFile = new File(main.getDataFolder(), "config.yml");
		config = YamlConfiguration.loadConfiguration(configFile);
		main.saveConfig();
	}
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("ier")) {
			//Player running the command
			if(sender instanceof Player) {
				//Getting the player object
				Player p = (Player) sender;
				//Making sure they have permission, if not send message end.
				if(p.hasPermission("ironelevators.reload")) {
					reloadConfig();
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("messages.reloaded")));
					return true;
				} else {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&',config.getString("messages.nopermission"));
					return true;
				}
			} else {
				//Most likely console running it reload, log, end
				reloadConfig();
				System.out.println("[IronElevators] Successfully reloaded the config!");
				return true;
			}
		}
		return false;
	}

}
