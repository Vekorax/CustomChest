package qc.veko.customchest.manager;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import qc.veko.customchest.CustomChest;

import java.io.File;
import java.io.IOException;

public class ChestFileManager {

    public void action_save(String player, ItemStack[] content) throws IOException {
        File file = new File(CustomChest.getInstance().getDataFolder() + "chest/" + player + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.createSection(player);
        for (int i = 0; i < content.length; i++) {
            if (content[i] != null && content[i].getType() != Material.AIR && checkItem(content[i])) {
                config.set(player + "." + i, content[i]);
            } else {
                config.set(player + "." + i, null);
            }
        }
        config.save(file);
    }

    public void action_load(String player) {
        File file = new File("plugins/CustomChest/chest/" + player + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        ConfigManager manager = CustomChest.getInstance().getConfigManager();
        if (!file.exists()) {
            return;
        }
        ItemStack[] contents = new ItemStack[manager.getNumberOfRow()*9];
        for (int i = 0; i < contents.length; i++) {
            ItemStack x = config.getItemStack(player + "." + i, contents[i]);
            if (x != null)
                contents[i] = x;
        }
        CustomChest.getInstance().getChestMap().put(player, contents);
    }

    private boolean checkItem(ItemStack i) {
        if (!i.getItemMeta().hasDisplayName())
            return true;
        if (!i.getItemMeta().getDisplayName().startsWith("§"))
            return true;
        switch(i.getType()) {
            case IRON_FENCE:
            case STAINED_GLASS_PANE:
                return false;
            default:
                return true;
        }
    }
}
