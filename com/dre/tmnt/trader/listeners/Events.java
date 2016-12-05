package com.dre.tmnt.trader.listeners;




import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import com.dre.tmnt.npcs.Npc;
import com.dre.tmnt.npcs.NpcsYml;
import com.dre.tmnt.trader.Config;
import com.dre.tmnt.trader.Main;
import com.dre.tmnt.trader.PacketReader;

import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class Events implements Listener{

	@EventHandler
	public void onDeath(PlayerDeathEvent e){
		PermissionUser user = PermissionsEx.getUser(e.getEntity());
		if(user.has("trader.state")){
			e.getEntity().sendMessage("HaHa!");
			Item item = null;
			e.getDrops().contains(item);
		}
	}
	
	
	@EventHandler
	public void onDropItem(PlayerDropItemEvent e){
		if (!Main.config.getBoolean(Config.ITEM_DROP_ENABLE)){
			PermissionUser user = PermissionsEx.getUser(e.getPlayer());
			for (String group : Main.config.getStringList(Config.NOT_TRADER_GROUPS)){
				if (user.inGroup(group)){
					return;
				}
			}
			if(user.has(Config.PERMISSION_TRADER)){
				e.setCancelled(true);
			}
		}
	}
	
	
	@EventHandler
	public void onOpenInventory (InventoryOpenEvent e){
		PermissionUser user = PermissionsEx.getUser(e.getPlayer().getName());
		for (String group : Main.config.getStringList(Config.NOT_TRADER_GROUPS)){
			if (user.inGroup(group)){
				return;
			}
		}
		if(user.has(Config.PERMISSION_TRADER)){
			if (!e.getInventory().getName().equals("§6Materialverwalter")){
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onInvClick(InventoryClickEvent e){
		if(e.getSlotType() == SlotType.CONTAINER){
			if (e.getView().getTitle().equals("§6Materialverwalter")){
				e.setCancelled(true);
				if(e.getCurrentItem().getType() == Material.PAPER){
				}
			}
		}
	}
	
	@EventHandler
	public void onJoin (PlayerJoinEvent e){

		PacketReader pr = new PacketReader(e.getPlayer());
		pr.inject();
		
		for (Npc npc: NpcsYml.npcs){
			npc.spawn(e.getPlayer(), 150);
		}		
	}	
	
	
	
}




























