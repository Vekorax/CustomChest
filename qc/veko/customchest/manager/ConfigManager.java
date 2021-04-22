package qc.veko.customchest.manager;

import com.google.common.collect.Maps;
import lombok.Data;
import org.bukkit.configuration.file.FileConfiguration;
import qc.veko.customchest.CustomChest;

import java.util.Map;

@Data
public class ConfigManager {

    private String command;
    private int numberOfRow;
    private String nameOfChest;
    private Map<Integer, Double> price = Maps.newHashMap();
    private Map<String, Integer> level = Maps.newHashMap();

    public void loadConfig() {
        FileConfiguration config = CustomChest.getInstance().getConfig();
        FileConfiguration level  = CustomChest.getInstance().getChestLevelFile();
        setCommand(config.getString("command"));
        for (int i = 1 ; i < 7 ; ++i) {
            getPrice().put(i, config.getDouble("PriceOfRow" + i));
        }

        for (String name : level.getKeys(false)){
            getLevel().put(name, level.getInt(name));
        }
    }

}
