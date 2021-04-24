package qc.veko.customchest.listener;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import qc.veko.customchest.CustomChest;
import qc.veko.customchest.manager.ConfigManager;

public class AdminInventoryListener implements Listener {

    @EventHandler
    public void onGuiClick(InventoryClickEvent e) {
        ConfigManager config = CustomChest.getInstance().getConfigManager();
        if (e.getInventory().getName().equalsIgnoreCase(config.getNameOfChest()) || !e.getInventory().getName().contains(config.getNameOfChest()))
            return;
        if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR || !e.getCurrentItem().getItemMeta().hasDisplayName())
            return;
        switch (e.getCurrentItem().getType()){
            case IRON_FENCE:
            case STAINED_GLASS_PANE:
                e.setCancelled(true);
        }
    }
    @EventHandler
    public void onGUIClose(InventoryCloseEvent e) {
        ConfigManager config = CustomChest.getInstance().getConfigManager();
        if (e.getInventory().getName().equalsIgnoreCase(config.getNameOfChest()) || !e.getInventory().getName().contains(config.getNameOfChest()))
            return;
        String targetName = e.getInventory().getName().replace(" : " + config.getNameOfChest(), "");
        CustomChest.getInstance().getChestMap().put(targetName, e.getInventory().getContents());
    }

}
