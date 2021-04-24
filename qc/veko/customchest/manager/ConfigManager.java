package qc.veko.customchest.manager;

import com.google.common.collect.Maps;
import lombok.Data;
import org.bukkit.configuration.file.FileConfiguration;
import qc.veko.customchest.CustomChest;

import java.util.Map;

@Data
public class ConfigManager {

    private String prefix;
    private String command;
    private String nameOfChest;
    private String permissionMessage;
    private String notEnoughMoney;
    private String boughtRowMessage;
    private String playerDoesNotHaveChest;
    private String ReloadMessage;

    private boolean useAsEnderChest;

    private int numberOfRow;

    private Map<Integer, Double> price = Maps.newHashMap();
    private Map<String, Integer> level = Maps.newHashMap();

    public void loadConfig() {
        FileConfiguration config = CustomChest.getInstance().getConfig();
        FileConfiguration level  = CustomChest.getInstance().getChestLevelFile();

        prefix = (config.getBoolean("EnablePrefix")) ? getColor("Prefix", config) : "";
        setCommand(config.getString("Command"));
        setNameOfChest(getColor("NameOfChest", config));
        setNumberOfRow(config.getInt("NumberOfRow"));
        setPermissionMessage(getColor("PermissionMessage", config));
        setNotEnoughMoney(getColor("NotEnoughMoney", config));
        setBoughtRowMessage(getColor("BoughtRowMessage", config));
        setPlayerDoesNotHaveChest(getColor("PlayerDoesNotHaveChest", config));
        setReloadMessage(getColor("ReloadMessage", config));
        setUseAsEnderChest(config.getBoolean("UseAsEnderChest"));

        for (int i = 1 ; i < 7 ; ++i) {
            getPrice().put(i, config.getDouble("PriceOfRow" + i));
        }
        for (String name : level.getKeys(false)){
            getLevel().put(name, level.getInt(name));
        }
    }

    private String getColor(String path, FileConfiguration config) {
        String text = config.getString(path);
        if (text.contains("&"))
            return text.replaceAll("&", "§");
        return text;
    }

}
