package qc.veko.customchest.manager;

import java.util.HashMap;

import org.bukkit.configuration.file.FileConfiguration;

import qc.veko.customchest.CustomChest;

public class ConfigManager {
	
	private static ConfigManager instance;
	private static String prefix;
	private static String command;
	private static String nameOfChest;
	private static String permissionMessage;
	private static String notEnoughtMoney;
	private static String boughtRowMessage;
	private static boolean useAsEnderChest;
	private static int numberOfRow;
	
	public static HashMap <String, Integer> level = new HashMap<>();
	public HashMap <Integer, Integer> price = new HashMap<>();
	
	public ConfigManager() {
		instance = this;
	}
	
	public void loadConfig() {
		FileConfiguration config = CustomChest.getInstance().getConfig();
		nameOfChest = getColor("NameOfChest");
		command = config.getString("Command");
		permissionMessage = getColor("PermissionMessage");
		prefix = (config.getBoolean("EnablePrefix")) ? getColor("Prefix"): "";
		
		notEnoughtMoney = getColor("NotEnoughtMoney");
		boughtRowMessage = getColor("BoughtRowMessage");
		
		for(String string : CustomChest.getInstance().chestLevelFile.getKeys(false)){
			level.put(string, (int) CustomChest.getInstance().chestLevelFile.get(string));
		}
		for (int i = 1; i<7; ++i) {
			price.put(i-1, config.getInt("PriceOf"+i+"Row"));

		}
		useAsEnderChest = config.getBoolean("UseAsEnderChest");
		
		numberOfRow = config.getInt("NumberOfRow");
	}
	
	public static ConfigManager getInstance() {
		return instance;
	}
	
	private String getColor(String text) {
		FileConfiguration config = CustomChest.getInstance().getConfig();
		String color = config.getString(text);
		if (color.contains("&")) {
			String newPrefix = color.replaceAll("&", "�");
			return newPrefix;
		} else {
			return color;
		}
	}
	
	public boolean getUseOfEnderChest() {
		return useAsEnderChest;
	}
	
	public String getPrefix() {
		return prefix;
	}
	
	public String getCommand() {
		return command;
	}
	
	public String getNameOfChest() {
		return nameOfChest;
	}
	
	public String getpermissionMessage() {
		return permissionMessage;
	}
	
	public String getNotEnoughtMoney() {
		return notEnoughtMoney;
	}
	
	public String getBoughtRowMessage() {
		return boughtRowMessage;
	}
	
	public int getNumberOfRow() {
		return numberOfRow;
	}
}
