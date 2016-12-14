package com.dre.tmnt.npcs;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_9_R2.util.CraftChatMessage;
import org.bukkit.entity.Player;

import com.dre.tmnt.util.GameProfileBuilder;
import com.dre.tmnt.util.UUIDFetcher;
import com.mojang.authlib.GameProfile;

import net.minecraft.server.v1_9_R2.DataWatcher;
import net.minecraft.server.v1_9_R2.DataWatcherObject;
import net.minecraft.server.v1_9_R2.DataWatcherRegistry;
import net.minecraft.server.v1_9_R2.PacketPlayOutEntity.PacketPlayOutEntityLook;
import net.minecraft.server.v1_9_R2.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_9_R2.PacketPlayOutEntityHeadRotation;
import net.minecraft.server.v1_9_R2.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_9_R2.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_9_R2.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_9_R2.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import net.minecraft.server.v1_9_R2.PacketPlayOutPlayerInfo.PlayerInfoData;
import net.minecraft.server.v1_9_R2.WorldSettings.EnumGamemode;

public class Npc extends Reflection{

	int entityID;
	int npcID;
	Location location;
	GameProfile gameprofile;
	String name;
	String skinName;
	List <String> playerList = new ArrayList<String>();
	
	
	

	public Npc(String name, Location location, Integer npcID) {
		entityID = new Random().nextInt(1000000);
		this.name = name;
		this.skinName = name;
		this.location = location;
		this.npcID = npcID;
		setGameProfile();
	}
	
	public Npc(String name, String skinName, Location location, Integer npcID){
		entityID = new Random().nextInt(1000000);
		this.name = name;
		this.skinName = skinName;
		this.location = location;
		this.npcID = npcID;
		setGameProfile();
	}
	
	
	public void spawn(Player player){
		PacketPlayOutNamedEntitySpawn packet = new PacketPlayOutNamedEntitySpawn();
		
		setValue(packet, "a", entityID);
		setValue(packet, "b", gameprofile.getId());
		setValue(packet, "c", location.getX());
		setValue(packet, "d", location.getY());
		setValue(packet, "e", location.getZ());
		
		DataWatcher w = new DataWatcher(null);
		w.register(new DataWatcherObject<>(6, DataWatcherRegistry.c), 20.0F);
		w.register(new DataWatcherObject<>(10, DataWatcherRegistry.a), (byte)127);
		w.register(new DataWatcherObject<>(13, DataWatcherRegistry.a), (byte) 40);
		setValue(packet, "h", w);
		
		addToTablist(player);
		sendPacket(packet, player);
		headRotation(location.getYaw(), location.getPitch());
	}
	
	
	public void spawn(){
		for(Player player: Bukkit.getOnlinePlayers()){
			spawn(player);
			
		}
	}
	
	
	public void teleport(Location location){
		PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport();
		setValue(packet, "a", entityID);
		setValue(packet, "b", location.getX());
		setValue(packet, "c", location.getY());
		setValue(packet, "d", location.getZ());		
		setValue(packet, "e", (location.getYaw()));
		setValue(packet, "f", (location.getPitch()));
		
		sendPacket(packet);
		headRotation(location.getYaw(), location.getPitch());
		this.location = location;
	}
	
	
	public void headRotation(float yaw, float pitch){
		location.setYaw(yaw);
		location.setPitch(pitch);
		PacketPlayOutEntityLook packet = new PacketPlayOutEntityLook(entityID, getFixRotation(yaw), getFixRotation(pitch), true);
		PacketPlayOutEntityHeadRotation packetHead = new PacketPlayOutEntityHeadRotation();
		setValue(packetHead, "a", entityID);
		setValue(packetHead, "b", getFixRotation(yaw));
		
		sendPacket(packet);
		sendPacket(packetHead);
	}
	
	
	public void destroy(){
		PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(new int[] {entityID});
		sendPacket(packet);
	}
	
	public void destroy(Player player){
		PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(new int[] {entityID});
		sendPacket(packet, player);
	}
	
	
	public void addToTablist(Player player) {
		PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo();
		PacketPlayOutPlayerInfo.PlayerInfoData data = packet.new PlayerInfoData(gameprofile, 1, EnumGamemode.NOT_SET, CraftChatMessage.fromString(name)[0]);
		@SuppressWarnings("unchecked") 
		List<PlayerInfoData> players = (List<PlayerInfoData>) getValue(packet, "b");
		players.add(data);
		
		setValue(packet, "a", EnumPlayerInfoAction.ADD_PLAYER);
		setValue(packet, "b", players);
		sendPacket(packet, player);

	}
	
	
	public void removeFromTablist(Player player) {
		
		Boolean isOnline = false;
		for(Player p : Bukkit.getOnlinePlayers()){
			if(gameprofile.getName().equals(p.getName())){
				isOnline = true;
			}
		}
		if(!isOnline){
			PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo();
			PacketPlayOutPlayerInfo.PlayerInfoData data = packet.new PlayerInfoData(gameprofile, 1, EnumGamemode.NOT_SET, CraftChatMessage.fromString(name)[0]);
			@SuppressWarnings("unchecked")
			List<PlayerInfoData> players = (List<PlayerInfoData>) getValue(packet, "b");
			players.add(data);
			
			setValue(packet, "a", EnumPlayerInfoAction.REMOVE_PLAYER);
			setValue(packet, "b", players);
			sendPacket(packet, player);	
		}
		
	}
	
	
	public void changeSkin(String skinName) {
		this.skinName = skinName;
		setGameProfile();
		spawn();
		return;
	}
	
	
	private void setGameProfile(){
		try {
			gameprofile = GameProfileBuilder.fetch(UUIDFetcher.getUUID(skinName), name);
		} catch (Exception e) {
			gameprofile = new GameProfile(UUID.randomUUID(), name);
		}
	}
	
	
	public byte getFixRotation(float yawpitch){
		return (byte) ((int) (yawpitch * 256.0F / 360.0F)); 
	}
	
	
	public int getEntityID() {
		return this.entityID;
	}
	
	
	public Location getLocation() {
		return location;
	}
	
	
	public String getName(){
		return name;
	}
	
	
	public int getNpcID(){
		return npcID;
	}
	
	
	public GameProfile getGameProfile(){
		return gameprofile;
	}

	
	public String getSkinName() {
		return skinName;
	}

	
	public void setNpcId(int i){
		npcID = i;
	}
	
	
	public List<String> getPlayerList() {
		return playerList;
	}

	
	public void setPlayerToList(String name) {
		playerList.add(name);
	}
	
	public void removePlayerFromList(String name) {
		playerList.remove(name);
	}
}
