package com.dre.tmnt.trader;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Config {

	public  Map<String, Object> config = new HashMap<String, Object>();
	
	// diverse Listen in der Config
	public static List<String> defaultTraderPermissions = new ArrayList<String>();
	public static List<String> defaultNotTraderGroups = new ArrayList<String>();
	public static List<String> defaultTradermarkLore = new ArrayList<String>();
	
	// trader Status
	final public static String PERMISSION_TRADER = "trader.state";
	
	// Schlüssel
	final public static String ITEM_DROP_ENABLE = "Trader.ItemDropEnable"; 
	final public static String ADVENTURE_MODE = "Trader.AdventureMode"; 
	final public static String MESSAGE_TO_MANY_ARGS = "Trader.Messages.toManyArgs";
	final public static String MESSAGE_TRADER_ON = "Trader.Messages.on";
	final public static String MESSAGE_TRADER_OFF = "Trader.Messages.off";
	final public static String MESSAGE_NOT_A_TRADER_OFF = "Trader.Messages.offWhileNotATrader";
	final public static String MESSAGE_FALSE_GROUP = "Trader.Messages.FalseGroup";
	final public static String PERMISSIONS = "Trader.Permissions";
	final public static String NOT_TRADER_GROUPS = "Trader.NotTraderGroups";
	// help Schlüssel
	final public static String TRADER_ON = "Trader.Messages.Commands.TraderOn";
	final public static String TRADER_OFF = "Trader.Messages.Commands.TraderOff";
	final public static String TRADER_NPC = "Trader.Messages.Commands.TraderNpc";
	
	
	
	
	
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
		config.put(MESSAGE_TO_MANY_ARGS,"Zu viele Argumente!");
		config.put(MESSAGE_TRADER_ON,"Ihr seid nun Händler!");
		config.put(MESSAGE_TRADER_OFF,"Ihr seid nun kein Händler mehr!");
		config.put(MESSAGE_NOT_A_TRADER_OFF,"Ihr seid kein Händler!");
		config.put(MESSAGE_FALSE_GROUP ,"Ihr habt dazu keine Berechtigung!");
		config.put(TRADER_ON , "");
		config.put("Trader.Items.Tradermark.Name", "§4Handelsschein");
		config.put("Trader.Items.Tradermark.Lore", defaultTradermarkLore);
		config.put(PERMISSIONS, defaultTraderPermissions);
		config.put(NOT_TRADER_GROUPS, defaultNotTraderGroups);
		config.put(TRADER_ON, "§b/trader on §3<Spieler> §1Aktiviert den Händler Status.");
		config.put(TRADER_OFF, "§b/trader off §3<Spieler> §1Deaktiviert den Händler Status.");
		config.put(TRADER_NPC,"§b/trader npc §1Erstellt einen Npc an deiner Position.");
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
	
	public String getHelp(Integer page){
		if (page == 0){
			return (String) config.get(TRADER_ON) +"\n"+ 
							config.get(TRADER_OFF)+"\n"+
							config.get(TRADER_NPC);
		}
		return (String) config.get(TRADER_ON) +"\n"+ config.get(TRADER_OFF);
	}
	public String getHelp(){
		return getHelp(0);
	}
}
