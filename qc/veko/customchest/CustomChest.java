package qc.veko.customchest;

import com.google.common.collect.Maps;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import qc.veko.customchest.commands.ChestCommand;
import qc.veko.customchest.inventory.ChestInventory;
import qc.veko.customchest.listener.AdminInventoryListener;
import qc.veko.customchest.listener.EnderChestListener;
import qc.veko.customchest.listener.InventoryListener;
import qc.veko.customchest.manager.ChestFileManager;
import qc.veko.customchest.manager.ConfigManager;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class CustomChest extends JavaPlugin {

    @Getter private static CustomChest instance;

    @Getter private ConfigManager configManager = new ConfigManager();
    @Getter private ChestFileManager chestFileManager = new ChestFileManager();
    @Getter private ChestInventory chestInventory = new ChestInventory();

    @Getter private Map<String, ItemStack[]> chestMap = Maps.newHashMap();

    @Getter private File chestLevel = new File("plugins/CustomChest", "level.yml");
    @Getter private FileConfiguration chestLevelFile = YamlConfiguration.loadConfiguration(chestLevel);

    @Getter private Economy economy = null;

    public void onEnable() {
        instance = this;
        this.saveDefaultConfig();
        configManager.loadConfig();

        Bukkit.getPluginManager().registerEvents(new ChestCommand(), this);
        Bukkit.getPluginManager().registerEvents(new InventoryListener(), this);
        Bukkit.getPluginManager().registerEvents(new AdminInventoryListener(), this);
        Bukkit.getPluginManager().registerEvents(new EnderChestListener(), this);

        Bukkit.getScheduler().runTaskTimer(this, this::save, 0, 20 * (60 * 30));
    }

    public void onDisable() {
        save();
    }

    public boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null)
            return false;
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null)
            return false;
        this.economy = (Economy)rsp.getProvider();
        return (this.economy != null);
    }

    private void save() {
        configManager.getLevel().forEach(chestLevelFile::set);
        getChestMap().forEach((name, items) ->{
            try {
                chestFileManager.action_save(name, items);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        try {
            getChestLevelFile().save(getChestLevel());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
