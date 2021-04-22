package qc.veko.customchest.commands;


import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.Inventory;
import qc.veko.customchest.CustomChest;

public class ChestCommand implements Listener {

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        String [] args = e.getMessage().split(" ");
        if (!args[0].equals(CustomChest.getInstance().getConfigManager().getCommand()))
            return;
        if (!e.getPlayer().hasPermission("chest.use")) {
            e.getPlayer().sendMessage("vous n'avez pas la persmission pour cela");
            e.setCancelled(true);
            return;
        }
        Inventory inv = CustomChest.getInstance().getChestInventory().getChestInventory(e.getPlayer());
        e.getPlayer().openInventory(inv);
    }

}
