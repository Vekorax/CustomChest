package qc.veko.customchest.manager;

import java.io.File;
import java.io.IOException;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import qc.veko.customchest.command.CommandChest;

public class SaveChest {
	
	private ConfigManager manager;
	
	public SaveChest(ConfigManager config) {
		manager = config;
	}

	public void action_save(String player, ItemStack[] content) throws IOException {
		File file = new File("plugins/CustomChest/chest/" + player + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
	    if (!file.exists()) {
	    	file.createNewFile();
	    	config.createSection(player);
	    }
	    ItemStack[] contents = content;
	    for (int i = 0; i < contents.length; i++) {
	      if (contents[i] != null && contents[i].getType() != Material.AIR && checkItem(contents[i])) {
	    	  config.set(player + "." + i, contents[i]);
	      } else {
	    	  config.set(player + "." + i, null);
	      } 
	    } 
	    config.save(file);
	  }
	
	public void action_load(String player) {
		File file = new File("plugins/CustomChest/chest/" + player + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		if (!file.exists()) {
	      return;
	    } 
	    ItemStack[] contents = new ItemStack[manager.getNumberOfRow()*9];
	    for (int i = 0; i < contents.length; i++) {
	      ItemStack x = config.getItemStack(player + "." + i, contents[i]);
	      if (x != null)
	        contents[i] = x; 
	    } 
	    CommandChest.chest.put(player, contents);
	  }
	
	public boolean fileExist(String player) {
		File file = new File("plugins/CustomChest/chest/" + player + ".yml");
		if (!file.exists()) {
	      return false;
	    } else {
	    	return true;
	    }
	}
	
	
	private boolean checkItem(ItemStack i) {
		switch(i.getType()) {
		case IRON_FENCE:
        	if (i.getItemMeta().getDisplayName().equals("�c�nIndisponible")) {
        		return false;
        	} else {
        		return true;
        	}
        case STAINED_GLASS_PANE:
        	if (i.getDurability() == 5) {
            	if (i.getItemMeta().getDisplayName().equals("�2�nAcheter")) {
            		return false;
            	} else {
            		return true;
            	}
            } else if (i.getDurability() == 4) {
            	if (i.getItemMeta().getDisplayName().equals("�4�nNon Disponible")) {
            		return false;
            	} else {
            		return true;
            	}
            } else {
            	return true;
            }
        default:
        	return true;
		}
	}
	
}
