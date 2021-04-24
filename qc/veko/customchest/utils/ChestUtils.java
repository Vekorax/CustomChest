package qc.veko.customchest.utils;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import qc.veko.customchest.CustomChest;
import qc.veko.customchest.manager.ConfigManager;

import java.io.File;

public class ChestUtils {

    public static void buyRow(Player player) {
        ConfigManager config = CustomChest.getInstance().getConfigManager();
        int level = config.getLevel().get(player.getName());
        double price = config.getPrice().get(level+1);
        if (!CustomChest.getInstance().setupEconomy())
            return;
        if  (CustomChest.getInstance().getEconomy().getBalance(player) < price) {
            player.sendMessage(config.getPrefix() + config.getNotEnoughMoney());
            return;
        }
        config.getLevel().put(player.getName(), level+1);
        CustomChest.getInstance().getEconomy().withdrawPlayer(player, price);
        Inventory inv = CustomChest.getInstance().getChestInventory().getChestInventory(player.getName(), false);
        player.openInventory(inv);
        player.sendMessage(config.getPrefix() + config.getBoughtRowMessage());
    }

    public static boolean fileExist(String player) {
        File file = new File("plugins/CustomChest/chest/" + player + ".yml");
        return file.exists();
    }

}
