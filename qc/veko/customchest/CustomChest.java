package qc.veko.customchest;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import qc.veko.customchest.command.CommandChest;
import qc.veko.customchest.listener.EventCustomChest;
import qc.veko.customchest.listener.admin.EventCustomChestAdmin;
import qc.veko.customchest.manager.ConfigManager;
import qc.veko.customchest.manager.SaveChest;

public class CustomChest extends JavaPlugin{
	
	public File chestLevel = new File("plugins/CustomChest", "level.yml");
	public FileConfiguration chestLevelFile = YamlConfiguration.loadConfiguration(chestLevel);
	private ConfigManager config = new ConfigManager();
	private SaveChest save = new SaveChest(config);
	
	public Economy economy = null;
	
	private static @Getter CustomChest instance;
	public void onEnable() {
		instance = this;
		this.saveDefaultConfig();
		config.loadConfig();
		Bukkit.getPluginManager().registerEvents(new CommandChest(config), this);
		Bukkit.getPluginManager().registerEvents(new EventCustomChest(config), this);
		Bukkit.getPluginManager().registerEvents(new EventCustomChestAdmin(config), this);
		
		
        Bukkit.getScheduler().scheduleSyncRepeatingTask((Plugin)this, new Runnable() {
        	public void run() {
        		saveFile();
    	        CommandChest.chest.forEach((name, itemstack) -> {
    	        	try {
    					save.action_save(name, itemstack);
    				} catch (IOException e) {
    					e.printStackTrace();
    				}
    	        });
        	}
        }, 0, 20 * (30));
		
	}
	
	public void onDisable() {
	        saveFile();
	        CommandChest.chest.forEach((name, itemstack) -> {
	        	try {
					save.action_save(name, itemstack);
				} catch (IOException e) {
					e.printStackTrace();
				}
	        });
	}
	
	public Economy getEconomy() {
		return this.economy;
	}
    public boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null)
          return false; 
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null)
          return false; 
        this.economy = (Economy)rsp.getProvider();
        return (this.economy != null);
      }
    
	public void saveFile() {
		ConfigManager.level.forEach((name, level) -> {
        	chestLevelFile.set(name, level);
        });
    	try {
    		chestLevelFile.save(chestLevel);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
