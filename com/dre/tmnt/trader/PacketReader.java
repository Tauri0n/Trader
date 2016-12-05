package com.dre.tmnt.trader;

import java.lang.reflect.Field;
import java.util.List;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import net.minecraft.server.v1_9_R2.Packet;

import org.bukkit.craftbukkit.v1_9_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.dre.tmnt.npcs.Npc;
import com.dre.tmnt.npcs.NpcsYml;
import com.dre.tmnt.trader.inventorys.GetTraderInventory;
import com.dre.tmnt.trader.inventorys.Inventory;

public class PacketReader {

	Player player;
	Channel channel;
	
	public PacketReader(Player player){
		this.player = player;
	}
	
	public void inject(){
		CraftPlayer cPlayer = (CraftPlayer)this.player;
		channel = cPlayer.getHandle().playerConnection.networkManager.channel;
		channel.pipeline().addAfter("decoder", "PacketInjector", new MessageToMessageDecoder<Packet<?>>() {@Override protected void decode(ChannelHandlerContext arg0, Packet<?> packet, List<Object> arg2) throws Exception{ arg2.add(packet); 
		readPacket(packet);
			}
		});
	}
	
	public void uninject(){
		if(channel.pipeline().get("PacketInjector") != null){
			channel.pipeline().remove("PacketInjector");
		}
	}
	
	public void readPacket(Packet<?> packet){
		
		if(packet.getClass().getSimpleName().equals("PacketPlayInUseEntity")){
			int id = (Integer)getValue(packet, "a");
			for(Npc npc : NpcsYml.npcs){
				if(npc.getEntityID() == id){
					if(getValue(packet, "action").toString().equalsIgnoreCase("ATTACK")){
						new GetTraderInventory(player, Main.config.getString("Trader.Items.Tradermark.Name"), Main.config.getStringList("Trader.Items.Tradermark.Lore"));
					}
					if(getValue(packet, "action").toString().equalsIgnoreCase("INTERACT_AT")){
						if(player.getOpenInventory().getTopInventory().getName() == "container.crafting"){
							new Inventory(player);
						}
					}
				}
			}
		}
	}

	public void setValue(Object obj, String name, Object value){
		try{
			Field field = obj.getClass().getDeclaredField(name);
			field.setAccessible(true);
			field.set(obj, value);
		}catch(Exception e){
			
		}
	}
	
	public Object getValue(Object obj, String name) {
		try{
			Field field = obj.getClass().getDeclaredField(name);
			field.setAccessible(true);
			return field.get(obj);
		}catch(Exception e){
			return null;
		}
		
	}

}
