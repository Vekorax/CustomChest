package qc.veko.customchest.listener.admin;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import qc.veko.customchest.command.CommandChest;
import qc.veko.customchest.manager.ConfigManager;

public class EventCustomChestAdmin implements Listener{

	private ConfigManager manager;
	private static EventCustomChestAdmin instance;
	
	public EventCustomChestAdmin(ConfigManager config) {
		manager = config;
		instance = this;
	}
	
	@EventHandler
	public void closeI(InventoryCloseEvent e) {
        if (e.getInventory().getName().contains(manager.getNameOfChest() + " Admin ")) {
        	String name = e.getInventory().getName().replace(manager.getNameOfChest() + " Admin ", "");
            CommandChest.chest.put(name, e.getInventory().getContents());
        }

    }
	
	@EventHandler
	public void clickEvent(InventoryClickEvent e){
    	Inventory inv = e.getInventory();
        	if(inv.getName().contains(manager.getNameOfChest() + " Admin ")){
            ItemStack current = e.getCurrentItem();
            
            if(current == null) return;
            if (!current.getItemMeta().hasDisplayName())
            	return;
            switch(e.getCurrentItem().getType()){
	            case IRON_FENCE:
	            	if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§c§nIndisponible"))
	            		e.setCancelled(true);
	            	break;
	            case STAINED_GLASS_PANE:
	            	if (e.getCurrentItem().getDurability() == 5) {
		            	if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§2§nAcheter"))
		            		e.setCancelled(true);
	            	} else if (e.getCurrentItem().getDurability() == 4) {
	            		if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§4§nNon Disponible"))
	            			e.setCancelled(true);
	            	}
	            	break;
	            default:
	            	break;
            }
        }
	}
	
	
	public Inventory getChest(String p, Player player) {
	    Inventory inv = Bukkit.createInventory(null, manager.getNumberOfRow()*9, manager.getNameOfChest() + " Admin " + p);

	    inv.setContents(CommandChest.chest.get(p));
	    
        ItemStack Bar = new ItemStack(Material.IRON_FENCE);
        ItemMeta BarMeta = Bar.getItemMeta();
	    BarMeta.setDisplayName("§c§nIndisponible");
        Bar.setItemMeta(BarMeta);
        
        ItemStack Green = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5);
        ItemMeta GreenMeta = Green.getItemMeta();
	    GreenMeta.setDisplayName("§2§nAcheter");
        ArrayList<String> lore = new ArrayList<String>();
        
        int level = ConfigManager.level.get(p);
        lore.add(String.valueOf(manager.price.get(level)));
        
	    GreenMeta.setLore(lore);
        Green.setItemMeta(GreenMeta);
        
        ItemStack Yellow = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 4);
        ItemMeta YellowMeta = Yellow.getItemMeta();
	    YellowMeta.setDisplayName("§4§nNon Disponible");
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
	
	public static EventCustomChestAdmin getInstance() {
		return instance;
	}
}
