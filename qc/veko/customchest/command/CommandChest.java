package qc.veko.customchest.command;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import qc.veko.customchest.CustomChest;
import qc.veko.customchest.manager.SaveChest;
import qc.veko.customchest.listener.admin.EventCustomChestAdmin;
import qc.veko.customchest.manager.ConfigManager;

public class CommandChest implements Listener{
	
	public static HashMap<String, ItemStack[]> chest = new HashMap<>();

	private static CommandChest instance;
	private ConfigManager manager;
	
	public CommandChest(ConfigManager config) {
		instance = this;
		manager = config;
	}
	
	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent e) {
		String cmd = e.getMessage();
		Player p = e.getPlayer();
		if (cmd.equalsIgnoreCase(manager.getCommand())) {
			if (p.hasPermission("cchest.use")) {
				p.openInventory(getChest(p));
				e.setCancelled(true);
			} else {
				p.sendMessage(manager.getPrefix() + manager.getpermissionMessage());
			}
		} else if (cmd.contains(" ")) {
			if (p.hasPermission("cchest.admin")) {
				if (cmd.startsWith(manager.getCommand())){
					String[] args = cmd.split(" ");
					if (verif(args[1], p)) {
						p.openInventory(EventCustomChestAdmin.getInstance().getChest(args[1], p));
					} else {
						p.sendMessage("Ce joueur n'as jamais ouvert son chest");
						e.setCancelled(true);
					}
					e.setCancelled(true);
				} 
			}
		}

	}
	
	
	private boolean verif(String p, Player player) {
	    SaveChest save = new SaveChest(manager);
		if (!save.fileExist(p)) {
	    	player.sendMessage("le joueur " + p + " n'est pas dans les fichiers");
	    	return false;
        } else {
        	if (chest.containsKey(p)) {
        		return true;
        	} else {
        		save.action_load(p);
        		return true;
        	}
        }
	}
	
    private FileConfiguration getConfig(String name) {
        File file = new File("plugins/CustomChest/chest/" + name + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        return config;
    }
    private File config(String name) {
    	File file = new File("plugins/CustomChest/chest/" + name + ".yml");
    	return file;
    }
    
    private void setFile(String name) {
        File file = new File("plugins/CustomChest/chest/" + name + ".yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException var4) {
                var4.printStackTrace();
            }
        }

    }
    
    
    @EventHandler
    public void ClickOnEnderChest(PlayerInteractEvent e) {
    	if (manager.getUseOfEnderChest()) {
    		if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType() == Material.ENDER_CHEST) {
    			e.getPlayer().openInventory(getChest(e.getPlayer()));
    			e.setCancelled(true);
    		}
    	}
    }
	
	public Inventory getChest(Player p) {
		    SaveChest save = new SaveChest(manager);
		    Inventory inv = Bukkit.createInventory(null, manager.getNumberOfRow()*9, manager.getNameOfChest());
		    if (!chest.containsKey(p.getName())) {
		    	if (!config(p.getName()).exists()) {
		    		setFile(p.getName());
			    	getConfig(p.getName()).createSection(p.getName());
		        	chest.put(p.getName(), new ItemStack[manager.getNumberOfRow()*9]); 
		        	saveCustomFile(config(p.getName()));
		        } else {
		        	save.action_load(p.getName());
		        	inv.setContents(chest.get(p.getName()));
		        }
		    }
		    if (!ConfigManager.level.containsKey(p.getName())) {
		    	CustomChest.getInstance().chestLevelFile.set(p.getName(), 0);
		    	ConfigManager.level.put(p.getName(), 0);
		    	saveFile();
		    }
		    inv.setContents(chest.get(p.getName()));
		    
	        ItemStack Bar = new ItemStack(Material.IRON_FENCE);
	        ItemMeta BarMeta = Bar.getItemMeta();
		    BarMeta.setDisplayName("�c�nIndisponible");
	        Bar.setItemMeta(BarMeta);
	        
	        ItemStack Green = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5);
	        ItemMeta GreenMeta = Green.getItemMeta();
		    GreenMeta.setDisplayName("�2�nAcheter");
	        ArrayList<String> lore = new ArrayList<String>();
	        
	        int level = ConfigManager.level.get(p.getName());
	        lore.add(String.valueOf(manager.price.get(level)));
	        
		    GreenMeta.setLore(lore);
	        Green.setItemMeta(GreenMeta);
	        
	        ItemStack Yellow = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 4);
	        ItemMeta YellowMeta = Yellow.getItemMeta();
		    YellowMeta.setDisplayName("�4�nNon Disponible");
	        Yellow.setItemMeta(YellowMeta);
		    int green = 0;
		    int green2 = 0;
		    int plus = (level == 0) ? 8:9;
		    for(int i = 0; i < manager.getNumberOfRow()*9; ++i){
		    	if (i< 9 * (level)) {
			    	int set = 9 * (level);
			    	green = set;
			    	green2 = set+1;
		    	} else if (i == green) {
	    			inv.setItem(i, Green);
		    		} else if (green2 != green + plus) {
		    			inv.setItem(i, Yellow);
		    			green2 ++;
		    		} else {
		    			inv.setItem(i, Bar);
		    		}
		    	}
		    
		    return inv;
		  }
	  
		private static void saveFile() {
	    	try {
	    		CustomChest.getInstance().chestLevelFile.save(CustomChest.getInstance().chestLevel);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		private void saveCustomFile(File file) {
			FileConfiguration config = YamlConfiguration.loadConfiguration(file);
			try {
				config.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		public static CommandChest getInstance() {
			return instance;
		}
	
}
