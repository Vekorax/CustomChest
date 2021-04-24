package qc.veko.customchest.inventory;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import qc.veko.customchest.CustomChest;
import qc.veko.customchest.manager.ConfigManager;
import qc.veko.customchest.utils.ItemBuilder;

public class ChestInventory {

    public Inventory getChestInventory(String player, boolean admin) {
        ConfigManager config = CustomChest.getInstance().getConfigManager();
        String nameOfInv = (admin) ? player + " : " + config.getNameOfChest() : config.getNameOfChest();
        Inventory inv = Bukkit.createInventory(null, config.getNumberOfRow()*9, nameOfInv);

        int level = CustomChest.getInstance().getConfigManager().getLevel().get(player);

        ItemBuilder green = new ItemBuilder(Material.STAINED_GLASS_PANE,1, (short) 5).setName("§2§nAcheter").setLore("§6Prix : " + config.getPrice().get(level+1));
        ItemBuilder yellow = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (short) 4).setName("§4§nNon Disponible");
        ItemBuilder bar = new ItemBuilder(Material.IRON_FENCE).setName("§c§nIndisponible");

        if (!CustomChest.getInstance().getChestMap().containsKey(player))
            CustomChest.getInstance().getChestFileManager().action_load(player);
        if (CustomChest.getInstance().getChestMap().containsKey(player))
            inv.setContents(CustomChest.getInstance().getChestMap().get(player));

        for (int i = 0 ; i < config.getNumberOfRow()*9; ++i) {
            if (i >= (level)*9)
                inv.setItem(i, yellow.toItemStack());
            if (i >= (level*9) + 9)
                inv.setItem(i, bar.toItemStack());
            if (i == (level*9))
                inv.setItem(i, green.toItemStack());
        }

        return inv;
    }
}
