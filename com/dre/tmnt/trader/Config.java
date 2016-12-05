package com.dre.tmnt.trader;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Config {

	public  Map<String, Object> config = new HashMap<String, Object>();
	
	public static List<String> defaultTraderPermissions = new ArrayList<String>();
	public static List<String> defaultNotTraderGroups = new ArrayList<String>();
	public static List<String> defaultTradermarkLore = new ArrayList<String>();
	
	
	
	final public static String PERMISSION_TRADER = "trader.state";
	//keys
	final public static String ITEM_DROP_ENABLE = "Trader.ItemDropEnable"; 
	final public static String ADVENTURE_MODE = "Trader.AdventureMode"; 
	final public static String MESSAGE_TRADER_ON = "Trader.Messages.on";
	final public static String MESSAGE_TRADER_OFF = "Trader.Messages.off";
	final public static String MESSAGE_NOT_A_TRADER_OFF = "Trader.Messages.offWhileNotATrader";
	final public static String MESSAGE_FALSE_GROUP = "Trader.Messages.FalseGroup";
	final public static String PERMISSIONS = "Trader.Permissions";
	final public static String NOT_TRADER_GROUPS = "Trader.NotTraderGroups";
	
	
	
	
	
	
	public Config(){
		
		defaultTraderPermissions.add("-essentials.sethome");
		defaultTraderPermissions.add("-essentials.home");
		
		defaultNotTraderGroups.add("admin");
		defaultNotTraderGroups.add("supermod");
		
		defaultTradermarkLore.add("§2Erwerbe §7Handelsschein");
		defaultTradermarkLore.add("§2für §3100GM");
		defaultTradermarkLore.add("");
		defaultTradermarkLore.add("");
		
		config.put(ITEM_DROP_ENABLE, false);
		config.put(ADVENTURE_MODE, true);
		config.put(MESSAGE_TRADER_ON,"Ihr seid nun Händler!");
		config.put(MESSAGE_TRADER_OFF,"Ihr seid nun kein Händler mehr!");
		config.put(MESSAGE_NOT_A_TRADER_OFF,"Ihr seid kein Händler!");
		config.put(MESSAGE_FALSE_GROUP ,"Ihr habt dazu keine Berechtigung!");
		config.put("Trader.Items.Tradermark.Name", "§4Handelsschein");
		config.put("Trader.Items.Tradermark.Lore", defaultTradermarkLore);
		config.put(PERMISSIONS, defaultTraderPermissions);
		config.put(NOT_TRADER_GROUPS, defaultNotTraderGroups);
	}
	
	
	public Boolean getBoolean(String key){
		return (Boolean) config.get(key);
	}
	
	public String getString(String key){
		return (String) config.get(key);
	}
	

	@SuppressWarnings("unchecked")
	public List<String> getStringList(String key){
		return (List<String>) config.get(key);
	}
	
	public Map<String, Object> getConfig(){
		return config;
	}
	
	public void setConfig(Map<String, Object> config){
		this.config = config;
	}
}
