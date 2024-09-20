package me.avankziar.vss.spigot.modifiervalueentry;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

import me.avankziar.vss.spigot.cmdtree.BaseConstructor;
import me.avankziar.vss.spigot.handler.ConfigHandler;
import me.avankziar.vss.spigot.handler.ConfigHandler.CountType;

public class ModifierValueEntry
{
	public static boolean hasPermission(Player player, BaseConstructor bc)
	{
		if(BaseConstructor.getPlugin().getValueEntry() != null)
		{
			Boolean ss = BaseConstructor.getPlugin().getValueEntry().getBooleanValueEntry(
					player.getUniqueId(),
					bc.getValueEntryPath(),
					BaseConstructor.getPlugin().getServername(),
					player.getWorld().getName());
			if(ss == null)
			{
				if(BaseConstructor.getPlugin().getYamlHandler().getConfig().getBoolean("ValueEntry.OverrulePermission", false))
				{
					return false;
				} else
				{
					return player.hasPermission(bc.getPermission());
				}
			}
			if(BaseConstructor.getPlugin().getYamlHandler().getConfig().getBoolean("ValueEntry.OverrulePermission", false))
			{
				return ss;
			} else
			{
				if(ss || player.hasPermission(bc.getPermission()))
				{
					return true;
				}
			}
			return false;
		}
		return player.hasPermission(bc.getPermission());
	}
	
	public static boolean hasPermission(Player player, Bypass.Permission bypassPermission)
	{
		if(BaseConstructor.getPlugin().getValueEntry() != null)
		{
			Boolean ss = BaseConstructor.getPlugin().getValueEntry().getBooleanValueEntry(
					player.getUniqueId(),
					bypassPermission.getValueLable(),
					BaseConstructor.getPlugin().getServername(),
					player.getWorld().getName());
			if(ss == null)
			{
				if(BaseConstructor.getPlugin().getYamlHandler().getConfig().getBoolean("ValueEntry.OverrulePermission", false))
				{
					return false;
				} else
				{
					return player.hasPermission(Bypass.get(bypassPermission));
				}
			}
			if(BaseConstructor.getPlugin().getYamlHandler().getConfig().getBoolean("ValueEntry.OverrulePermission", false))
			{
				return ss;
			} else
			{
				if(ss || player.hasPermission(Bypass.get(bypassPermission)))
				{
					return true;
				}
			}
			return false;
		}
		return player.hasPermission(Bypass.get(bypassPermission));
	}
	
	public static boolean hasPermission(Player player, Bypass.Permission bypassPermission, String addition)
	{
		if(BaseConstructor.getPlugin().getValueEntry() != null)
		{
			Boolean ss = BaseConstructor.getPlugin().getValueEntry().getBooleanValueEntry(
					player.getUniqueId(),
					bypassPermission.getValueLable(),
					BaseConstructor.getPlugin().getServername(),
					player.getWorld().getName());
			if(ss == null)
			{
				if(BaseConstructor.getPlugin().getYamlHandler().getConfig().getBoolean("ValueEntry.OverrulePermission", false))
				{
					return false;
				} else
				{
					return player.hasPermission(Bypass.get(bypassPermission)+addition);
				}
			}
			if(BaseConstructor.getPlugin().getYamlHandler().getConfig().getBoolean("Condition.ConditionOverrulePermission", false))
			{
				return ss;
			} else
			{
				if(ss || player.hasPermission(Bypass.get(bypassPermission)+addition))
				{
					return true;
				}
			}
			return false;
		}
		return player.hasPermission(Bypass.get(bypassPermission));
	}
	
	public static int getResult(@NonNull Player player, Bypass.Counter countPermission)
	{
		return getResult(player, 0.0, countPermission);
	}
	
	public static int getResult(@NonNull Player player, double value, Bypass.Counter countPermission)
	{
		if(player.hasPermission(Bypass.get(countPermission)+"*"))
		{
			return Integer.MAX_VALUE;
		}
		int possibleAmount = 0;
		CountType ct = new ConfigHandler().getCountPermType();
		switch(ct)
		{
		case ADDUP:
			for(int i = 1000; i >= 0; i--)
			{
				if(player.hasPermission(Bypass.get(countPermission)+i))
				{
					possibleAmount += i;
				}
			}
			break;
		case HIGHEST:
			for(int i = 1000; i >= 0; i--)
			{
				if(player.hasPermission(Bypass.get(countPermission)+i))
				{
					possibleAmount = i;
					break;
				}
			}
			break;
		}
		possibleAmount += (int) value;
		if(BaseConstructor.getPlugin().getModifier() != null)
		{
			return (int) BaseConstructor.getPlugin().getModifier().getResult(
					player.getUniqueId(),
					possibleAmount,
					countPermission.getModification(),
					BaseConstructor.getPlugin().getServername(),
					player.getWorld().getName());
		}
		return possibleAmount;
	}
	
	public static double getResult(UUID uuid, double value, Bypass.Counter countPermission)
	{
		double possibleAmount = value;
		if(BaseConstructor.getPlugin().getModifier() != null)
		{
			return BaseConstructor.getPlugin().getModifier().getResult(
					uuid,
					possibleAmount,
					countPermission.getModification());
		}
		return possibleAmount;
	}
}