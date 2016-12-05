package com.dre.tmnt.trader.inventorys;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class GetTraderInventory {
	Player player = null;
	

	public GetTraderInventory(Player player, String name, List<String> lore) {
		this.player = player;
		
		Inventory inv = player.getServer().createInventory(null, 18, "§6Materialverwalter");
		ItemStack item = new ItemStack(Material.PAPER);
		ItemMeta meta = item.getItemMeta();
		meta.addEnchant(Enchantment.DIG_SPEED, 1, false);
		meta.setDisplayName(name);
		meta.setLore(lore);
		item.setItemMeta(meta);
		inv.setItem(4, item);
		player.openInventory(inv);
	}	
	
}
