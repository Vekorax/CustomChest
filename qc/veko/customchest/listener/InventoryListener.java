package qc.veko.customchest.listener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import qc.veko.customchest.CustomChest;
import qc.veko.customchest.manager.ConfigManager;
import qc.veko.customchest.utils.ChestUtils;

public class InventoryListener  implements Listener {

    @EventHandler
    public void onGuiClick(InventoryClickEvent e) {
        ConfigManager config = CustomChest.getInstance().getConfigManager();
        if (! e.getInventory().getName().equals(config.getNameOfChest()))
            return;
        if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR || !e.getCurrentItem().getItemMeta().hasDisplayName())
            return;
        switch (e.getCurrentItem().getType()){
            case IRON_FENCE:
            case STAINED_GLASS_PANE:
                e.setCancelled(true);
                if (e.getCurrentItem().getDurability() == 5)
                    ChestUtils.buyRow((Player)e.getWhoClicked());
        }
    }
    @EventHandler
    public void onGUIClose(InventoryCloseEvent e) {
        ConfigManager config = CustomChest.getInstance().getConfigManager();
        if (! e.getInventory().getName().equals(config.getNameOfChest()))
            return;
        CustomChest.getInstance().getChestMap().put(e.getPlayer().getName(), e.getInventory().getContents());
    }

}
