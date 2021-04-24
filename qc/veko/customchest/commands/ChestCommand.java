package qc.veko.customchest.commands;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.Inventory;
import qc.veko.customchest.CustomChest;
import qc.veko.customchest.manager.ConfigManager;
import qc.veko.customchest.utils.ChestUtils;

public class ChestCommand implements Listener {

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        String [] args = e.getMessage().split(" ");
        if (!args[0].equalsIgnoreCase(CustomChest.getInstance().getConfigManager().getCommand()))
            return;
        e.setCancelled(true);
        ConfigManager config = CustomChest.getInstance().getConfigManager();
        if (!e.getPlayer().hasPermission("chest.use")) {
            e.getPlayer().sendMessage(config.getPrefix() + config.getPermissionMessage());
            return;
        }

        if (args.length > 1) {
            if (!e.getPlayer().hasPermission("chest.admin")) {
                e.getPlayer().sendMessage(config.getPrefix() + config.getPermissionMessage());
                return;
            }
            if (args[1].equalsIgnoreCase("reload")) {
                e.getPlayer().sendMessage(config.getPrefix() + config.getReloadMessage());
                CustomChest.getInstance().reloadConfig();
                CustomChest.getInstance().getConfigManager().loadConfig();
                return;
            }
            adminChest(e.getPlayer(), args[1]);
            return;
        }
        if (!config.getLevel().containsKey(e.getPlayer().getName()))
            config.getLevel().put(e.getPlayer().getName(), 1);
        Inventory inv = CustomChest.getInstance().getChestInventory().getChestInventory(e.getPlayer().getName(), false);
        e.getPlayer().openInventory(inv);
    }

    private void adminChest(Player player, String targetName) {
        if (!verifyPlayer(targetName, player))
            return;
        Inventory inv = CustomChest.getInstance().getChestInventory().getChestInventory(targetName, true);
        player.getPlayer().openInventory(inv);
    }

    private boolean verifyPlayer(String p, Player player) {
        ConfigManager config = CustomChest.getInstance().getConfigManager();
        if (!ChestUtils.fileExist(p) && !CustomChest.getInstance().getChestMap().containsKey(p)) {
            player.sendMessage(config.getPrefix() + config.getPlayerDoesNotHaveChest());
            return false;
        } else {
            if (!CustomChest.getInstance().getChestMap().containsKey(p))
                CustomChest.getInstance().getChestFileManager().action_load(p);
            return true;
        }
    }
}
