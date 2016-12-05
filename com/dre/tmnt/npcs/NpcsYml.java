package com.dre.tmnt.npcs;


import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import com.dre.tmnt.trader.Main;



public class NpcsYml {
	private static YamlConfiguration yaml = new YamlConfiguration();
	
	public static List <Npc> npcs = new ArrayList<Npc>();
	
	public static final String NAME = ".Name";
	public static final String SKIN_NAME = ".Skin Name";
	public static final String LOCATION_WORLD = ".Location.world";
	public static final String LOCATION_X = ".Location.x";
	public static final String LOCATION_Y = ".Location.y";
	public static final String LOCATION_Z = ".Location.z";
	
	
	
	public NpcsYml(){
		
	}
	
	
	public void save(){
		for(Npc npc: npcs){
			yaml.set(npc.getNpcID() + NAME, npc.getName());
			yaml.set(npc.getNpcID() + SKIN_NAME, npc.getSkinName());
			yaml.set(npc.getNpcID() + LOCATION_WORLD, npc.getLocation().getWorld().getName());
			yaml.set(npc.getNpcID() + LOCATION_X, npc.getLocation().getX());
			yaml.set(npc.getNpcID() + LOCATION_Y, npc.getLocation().getY());
			yaml.set(npc.getNpcID() + LOCATION_Z, npc.getLocation().getZ());
		}
		try {
			yaml.save(Main.path + "/npcs.yml");
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	

	public void reload(){
		
		
		try {
			yaml.load(Main.path + "/npcs.yml");
			for (Npc npc: npcs){
				npc.destroy();
			}
			npcs.removeAll(npcs);
			
			for(int i = 0; yaml.contains("" + i); i++){
				String name = yaml.getString(i + NAME);
				String SkinName = yaml.getString(i + SKIN_NAME);
				Location location = new Location(Bukkit.getWorld(yaml.getString(i + LOCATION_WORLD)), yaml.getDouble(i + LOCATION_X), yaml.getDouble(i + LOCATION_Y), yaml.getDouble(i + LOCATION_Z));				
				Npc npc = new Npc(name,SkinName,location,getNewNpcID());
				npc.spawn();
				npcs.add(npc);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
	}
	
	
	public int getNewNpcID(){
		int result = 0;
		result = npcs.size();
		return result;
	}
	
	
	public void addNpc(Npc npc){
		npcs.add(npc);
	}
	
	
	public void removeNpc(Npc npc){
		npcs.remove(npc);
		int i = 0;
		for(Npc n: npcs){
			n.setNpcId(i);
			i++;
		}
	}
}
