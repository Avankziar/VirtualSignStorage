package me.avankziar.vss.spigot.hook;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import me.avankziar.vss.spigot.modifiervalueentry.Bypass;
import me.avankziar.vss.spigot.modifiervalueentry.ModifierValueEntry;

public class WorldGuardHook
{
	public static StateFlag SHOP_CREATE;
	
	public static boolean init()
	{
		FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
		try 
		{
			StateFlag sc = new StateFlag("sale-shop-create", true);
	        registry.register(sc);
	        SHOP_CREATE = sc;
	    } catch (FlagConflictException e) 
		{
	        return false;
	    }
		return true;
	}
	
	public static boolean canCreateShop(Player player, Location pointOne)
	{
		RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
        com.sk89q.worldedit.util.Location loc1 = BukkitAdapter.adapt(pointOne);
        return ModifierValueEntry.hasPermission(player, Bypass.Permission.SHOP_CREATION_WORLDGUARD)
        		? true : query.testState(loc1, WorldGuardPlugin.inst().wrapPlayer(player), SHOP_CREATE);
	}
}