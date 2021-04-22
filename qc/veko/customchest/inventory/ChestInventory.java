package qc.veko.customchest.inventory;

import jdk.nashorn.internal.objects.annotations.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import qc.veko.customchest.CustomChest;
import qc.veko.customchest.manager.ConfigManager;
import qc.veko.customchest.utils.ItemBuilder;

public class ChestInventory {

    public Inventory getChestInventory(Player player) {
        ConfigManager config = CustomChest.getInstance().getConfigManager();
        Inventory inv = Bukkit.createInventory(null, config.getNumberOfRow()*9, config.getNameOfChest());

        int level = CustomChest.getInstance().getConfigManager().getLevel().get(player.getName());

        ItemBuilder green = new ItemBuilder(Material.STAINED_GLASS_PANE, (short) 5).setName("§2§nAcheter").setLore("§6Prix : " + config.getPrice().get(level+1));
        ItemBuilder yellow = new ItemBuilder(Material.STAINED_GLASS_PANE, (short) 4).setName("§4§nNon Disponible");
        ItemBuilder bar = new ItemBuilder(Material.IRON_FENCE).setName("§c§nIndisponible");

        for (int i = 0 ; i < config.getNumberOfRow()*9; ++i) {
            if (i >= (level+1)*9)
                inv.setItem(i, yellow.toItemStack());
            if (i >= ((level+1)*9) + 9)
                inv.setItem(i, bar.toItemStack());
            if (i == ((level+1)*9) + 1)
                inv.setItem(i, green.toItemStack());
        }

        return inv;
    }
}
