package com.dre.tmnt.trader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import com.dre.tmnt.npcs.Npc;
import com.dre.tmnt.npcs.NpcsYml;
import com.dre.tmnt.trader.inventorys.GetTraderInventory;
import com.dre.tmnt.trader.listeners.Events;

import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;


public class Main extends JavaPlugin implements Listener{
	

	public static String path = null;
	public static Config config = null;
	public static NpcsYml npcs = null;
	
	public Inventory inv = null;
	
	public static List<Player> playerSendNpcPackets =  new ArrayList<Player>();
	public static Map<String, Integer> SendPacketWithDelay = new HashMap<String, Integer>();
	
	
	
	
	
	public void onEnable(){
		path = getDataFolder().getAbsolutePath();
		System.out.println("[Trader] Plugin geladen!");
		getServer().getPluginManager().registerEvents(new Events(), this);		
		initConfig();
		npcHeadRotation();
		new NpcsYml();
	}
	
	
	public void onDisable(){
		npcs.save();
		System.out.println("[Trader] Plugin deaktiviert!");
	}
	
	
	private void initConfig(){
		npcs = new NpcsYml();
		config = new Config();
		npcs.reload();
		reloadConfig();
		getConfig().addDefaults(config.getConfig());
		getConfig().options().copyDefaults(true);
		saveConfig();
		reloadConfig();
		config.setConfig(getConfig().getValues(true));
	}
	
	
	public boolean onCommand (CommandSender sender, Command cmd, String label, String[] args){
		if(cmd.getName().equalsIgnoreCase("trader")){
			if (sender instanceof Player){
				Player player = (Player) sender;
				PermissionUser user = PermissionsEx.getUser(player);
				
				if (args.length == 0){
					player.sendMessage("/trader help");
					return true;
				}
				
				if (args[0].equalsIgnoreCase("on")){
					for(String group : config.getStringList(Config.NOT_TRADER_GROUPS)){
						if(user.inGroup(group)){
							player.sendMessage(config.getString(Config.MESSAGE_FALSE_GROUP));
							return true;
						}
					}
					if(!player.isOp()){
						player.sendMessage(config.getString(Config.MESSAGE_FALSE_GROUP));
						return true;
					}
					if (config.getBoolean(Config.ADVENTURE_MODE)){
						player.setGameMode(GameMode.ADVENTURE);
					}
					user.addPermission(Config.PERMISSION_TRADER);
					for(String permission : config.getStringList(Config.PERMISSIONS)){
						user.addPermission(permission);
					}
					sender.sendMessage(config.getString(Config.MESSAGE_TRADER_ON));
					return true;
				}
				
				else if (args[0].equalsIgnoreCase("off")){
					for(String group : config.getStringList(Config.NOT_TRADER_GROUPS)){
						if(user.inGroup(group)){
							player.sendMessage(config.getString(Config.MESSAGE_FALSE_GROUP));
							return true;
						}
					}
					if(user.has(Config.PERMISSION_TRADER)){
						player.setGameMode(GameMode.SURVIVAL);
						user.removePermission(Config.PERMISSION_TRADER);
						for(String permission : config.getStringList(Config.PERMISSIONS)){
							user.removePermission(permission);
						}
						sender.sendMessage(config.getString(Config.MESSAGE_TRADER_OFF));
						return true;
					}else{
						sender.sendMessage(config.getString(Config.MESSAGE_NOT_A_TRADER_OFF));
						return true;
					}
				}
				
				else if (args[0].equalsIgnoreCase("test")){
					new GetTraderInventory(player, config.getString("Trader.Items.Tradermark.Name"), config.getStringList("Trader.Items.Tradermark.Lore"));

					return true;
				}
				
				else if (args[0].equalsIgnoreCase("tpnpc")){
					if(args.length == 2){
						for(Npc npc : NpcsYml.npcs){
							if(npc.getName().equals(args[1])){
								npc.teleport(player.getLocation());
								return true;
							}
						}
						player.sendMessage(args[1] + " nicht vorhanden!");
						return true;
					}
					player.sendMessage("Zu viele Argumente!");
					return true;
				}
				
				else if (args[0].equalsIgnoreCase("skinnpc")){
					if(args.length == 3){
						for(Npc npc : NpcsYml.npcs){
							if(npc.getName().equals(args[1])){
								npc.changeSkin(args[2]);
								return true;
							}
						}
						player.sendMessage(args[1] + " nicht vorhanden!");
						return true;
					}
					player.sendMessage("/trader skinnpc <NPC name> <Skin name>");
					return true;
				}
				else if (args[0].equalsIgnoreCase("npc")){
					if(args.length == 2){
						Npc npc = new Npc(args[1], player.getLocation(), npcs.getNewNpcID());
						npc.spawn();
						npcs.addNpc(npc);
						return true;
					}
					return true;
				} else {
					player.sendMessage("/trader help");
					return true;
				}
			}
			else{
				sender.sendMessage("Du bist kein Spieler!");
				return true;
			}
		}
		return false;

	}
	
	
	public void npcHeadRotation() {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			
			@Override
			public void run() {
				
				for (Player player: playerSendNpcPackets){
					delayedRemoveFromTablist(player, SendPacketWithDelay.get(player.getName()));
					if(SendPacketWithDelay.get(player.getName()) >= 100){
						for(Npc npc: NpcsYml.npcs){
							npc.removeFromTablist(player);
						}
						delayedAddToTablist(player, 40);
						delayedRemoveFromTablist(player, 41);
						delayedAddToTablist(player, SendPacketWithDelay.get(player.getName())-1);
					}
				}
				playerSendNpcPackets.removeAll(playerSendNpcPackets);
				SendPacketWithDelay.clear();
				
				
				for(Npc npc : NpcsYml.npcs){
					Player closestPlayer = null;
					for(Player player : Bukkit.getOnlinePlayers()){
						
						if(player.getLocation().distance(npc.getLocation()) < 7){
							if(closestPlayer == null){
								closestPlayer = player;
							}
							if(player.getLocation().distance(npc.getLocation()) < closestPlayer.getLocation().distance(npc.getLocation())){
								closestPlayer = player;
							}
						}
					}
					
					if(closestPlayer != null){
						double xDif = (closestPlayer.getLocation().getX() - npc.getLocation().getX());
						double yDif = (closestPlayer.getLocation().getY() - npc.getLocation().getY());
						double zDif = (closestPlayer.getLocation().getZ() - npc.getLocation().getZ());
						double xzDis = Math.sqrt(Math.pow(xDif, 2) + Math.pow(zDif, 2));
						double xyzDis = Math.sqrt(Math.pow(xzDis, 2) + Math.pow(yDif, 2));
		
						float yaw;
						
						if((zDif / xzDis) <= 0){
							yaw = (float) (int)(Math.toDegrees(Math.asin(xDif/xzDis)) - 180);
						}else{
							yaw = (float) (int)( 0 - Math.toDegrees(Math.asin(xDif/xzDis)));
						}
						float pitch = (float) (int)(0 -  Math.toDegrees(Math.asin(yDif/xyzDis)));
						npc.headRotation(yaw, pitch);
					}

				}
			}
		}, 0, 1);
	}

	public void delayedRemoveFromTablist(Player player, int ticks){
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable(){
			
			@Override
			public void run() {
				
				for(Npc npc: NpcsYml.npcs){
					npc.removeFromTablist(player);
				}
			}
		},ticks);
		
	}
	
	public void delayedAddToTablist(Player player, int ticks){
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable(){
			
			@Override
			public void run() {
				
				for(Npc npc: NpcsYml.npcs){
					npc.spawn(player, -1);
				}
				
			}
		},ticks);
	}
}
