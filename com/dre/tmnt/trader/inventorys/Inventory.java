package com.dre.tmnt.trader.inventorys;

import org.bukkit.entity.Player;

public class Inventory {
	Player player = null;
	
	public Inventory(Player player){
		this.player = player;
		
		org.bukkit.inventory.Inventory inv = player.getServer().createInventory(null, 54, "test");
		player.openInventory(inv);
	}

}
