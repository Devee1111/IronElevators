package me.Devee1111;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class IronElevatorsMain extends JavaPlugin {
	public static final int FILE_COPY_MAX_BYTE_SIZE = 1024;
	
	//default config values
	public int maxElevation = 64, minElevation = 2;
	public Material elevatorMaterial = Material.IRON_BLOCK;
	public Sound elevatorWhoosh = Sound.ENTITY_IRON_GOLEM_ATTACK;
	
	IronElevatorEventListener listener;
	FileConfiguration config;
	File configFile;
	
	public void onEnable(){
		//load config
		loadConfig();
		getConfigValues();
		loadMessages();
		
		//Register command executer
		getCommand("ier").setExecutor(new IronElevatorsExecuter(this));
		
		//Register event listener
		listener = new IronElevatorEventListener(this);
		getServer().getPluginManager().registerEvents(this.listener, this);
	}
	
	public void onDisable() {
		/*
		 * Removed saveConfig to allow drag and drop and restarts
		 */
	}
	
	void getConfigValues(){
		//integer values
		maxElevation = config.getInt("maxElevation");
		minElevation = config.getInt("minElevation");
		elevatorMaterial = Material.valueOf(config.getString("elevatorMaterial"));
		elevatorWhoosh = Sound.valueOf(config.getString("elevatorWhoosh"));
	}
	
	void loadMessages() {
		config = getConfig();
		if(!config.contains("useUpDownMessages")) {
			config.set("useUpDownMessages",false);
		}
		if(!config.contains("messages.reloaded")) {
			config.set("messages.reloaded","&aYou have successfully reloaded the config.");
		}
		if(!config.contains("messages.up")) {
			config.set("messages.up","&aYou have ascended a level.");
		}
		if(!config.contains("messages.down")) {
			config.set("messages.down", "&cYou have descended a level.");
		}
		if(!config.contains("messages.nopermission")) {
			config.set("messages.nopermission","&cYou do not have permission to do this.");
		}
	}
	
	void loadConfig(){
		try{
			config = new YamlConfiguration();
			configFile = new File(getDataFolder(), "config.yml");
			if(!configFile.exists()){
				configFile.getParentFile().mkdirs();
				//copy(getResource("config.yml"), configFile);
				
				//build config from defaults
				config.set("minElevation", minElevation);
				config.set("maxElevation", maxElevation);
				config.set("elevatorMaterial", elevatorMaterial.toString());
				config.set("elevatorWhoosh", elevatorWhoosh.toString());
				config.set("useUpDownMessages",false);
				config.set("messages.reloaded","&aYou have successfully reloaded the config.");
				config.set("messages.up","&aYou have ascended a level.");
				config.set("messages.down", "&cYou have descended a level.");
				config.set("messages.nopermission", "&cYou do not have permission to do this.");
				config.save(configFile);
			}
			config.load(configFile);
		}catch(Exception e){
			Bukkit.getLogger().warning(ChatColor.RED + "Exception " + Color.white + "when loading configuration file.\n" + e.getMessage());
		}
	}
	
	public static void copy(InputStream in, File file){
		try{
			OutputStream out = new FileOutputStream(file);
			byte[] buf = new byte[FILE_COPY_MAX_BYTE_SIZE];
			int len;
			while((len=in.read(buf))>0){
				out.write(buf,0,len);
			}
			out.close();
			in.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
