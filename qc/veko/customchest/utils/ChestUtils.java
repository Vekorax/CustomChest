package qc.veko.customchest.utils;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import qc.veko.customchest.CustomChest;

public class ChestUtils {

    public static void buyRow(Player player, double price) {
        if (!CustomChest.getInstance().setupEconomy())
            return;
        if  (CustomChest.getInstance().getEconomy().getBalance(player) < price) {
            player.sendMessage("Vous n'avez pas assez d'argent");
            return;
        }
        int level = CustomChest.getInstance().getConfigManager().getLevel().get(player.getName());
        CustomChest.getInstance().getConfigManager().getLevel().put(player.getName(), level+1);
        CustomChest.getInstance().getEconomy().withdrawPlayer(player, price);
        Inventory inv = CustomChest.getInstance().getChestInventory().getChestInventory(player);
        player.openInventory(inv);
    }

}
