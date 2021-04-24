package qc.veko.customchest.listener;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import qc.veko.customchest.CustomChest;

public class EnderChestListener implements Listener {

    @EventHandler
    public void ClickOnEnderChest(PlayerInteractEvent e) {
        if (!CustomChest.getInstance().getConfigManager().getUseAsEnderChest())
            return;
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType() == Material.ENDER_CHEST ) {
            e.setCancelled(true);
            Inventory inv = CustomChest.getInstance().getChestInventory().getChestInventory(e.getPlayer().getName(), false);
            e.getPlayer().openInventory(inv);
        }
    }

}
