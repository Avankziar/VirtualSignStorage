package me.avankziar.vss.spigot.handler;

import java.util.LinkedHashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Sign;
import org.bukkit.block.data.type.WallSign;

import me.avankziar.vss.spigot.SaLE;
import me.avankziar.vss.spigot.objects.ItemHologram;
import me.avankziar.vss.spigot.objects.SignShop;

public class ItemHologramHandler
{
	public static LinkedHashMap<String, ItemHologram> taskMap = new LinkedHashMap<>();
	
	public static void spawnHologram(SignShop ssh)
	{
		if(!SaLE.getPlugin().getYamlHandler().getConfig().getBoolean("SignShop.ItemHologram.CanSpawn", false))
		{
			return;
		}
		if(!SaLE.getPlugin().getServername().equals(ssh.getServer())
			|| Bukkit.getWorld(ssh.getWorld()) == null)
		{
			return;
		}
		Block b = new Location(Bukkit.getWorld(ssh.getWorld()), ssh.getX(), ssh.getY(), ssh.getZ()).getBlock();
		float yaw = 0;
		if(b.getBlockData() instanceof WallSign)
		{
			WallSign ws = (WallSign) b.getBlockData();
			yaw = getYaw(ws.getFacing());
		} else if(b.getBlockData() instanceof Sign)
		{
			Sign s = (Sign) b.getBlockData();
			yaw = getYaw(s.getRotation());
		}
		Location loc = new Location(Bukkit.getWorld(ssh.getWorld()), ssh.getX()+0.5, ssh.getY()-1, ssh.getZ()+0.5, yaw, 0);
		long timer = System.currentTimeMillis()
				+ SaLE.getPlugin().getYamlHandler().getConfig().getLong("SignShop.ItemHologram.VisibilityTimeInSeconds", 5)*1000;
		ItemHologram ish = new ItemHologram(ssh.getItemStack(), loc);
		taskMap.put(String.valueOf(timer), ish);
	}
	
	private static float getYaw(BlockFace bf)
	{
		switch(bf)
		{
		default:
		case SOUTH:
			return 0;
		case SOUTH_SOUTH_WEST:
			return 22.5F;
		case SOUTH_WEST:
			return 45;
		case WEST_SOUTH_WEST:
			return 67.5F;
		case WEST:
			return 90;
		case WEST_NORTH_WEST:
			return 112.5F;
		case NORTH_WEST:
			return 135;
		case NORTH_NORTH_WEST:
			return 157.5F;
		case NORTH:
			return 180;
		case NORTH_NORTH_EAST:
			return -157.5F;
		case NORTH_EAST:
			return -135;
		case EAST_NORTH_EAST:
			return -112.5F;
		case EAST:
			return -90;
		case EAST_SOUTH_EAST:
			return -67.5F;
		case SOUTH_EAST:
			return -45;
		case SOUTH_SOUTH_EAST:
			return -22.5F;
		}
	}
}