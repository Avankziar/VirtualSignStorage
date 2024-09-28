package me.avankziar.vss.spigot.handler;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;

import me.avankziar.vss.spigot.gui.objects.SettingsLevel;

public class ConfigHandler
{
	public enum CountType
	{
		HIGHEST, ADDUP;
	}
	
	public CountType getCountPermType()
	{
		String s = config.getString("Mechanic.CountPerm", "HIGHEST");
		CountType ct;
		try
		{
			ct = CountType.valueOf(s);
		} catch (Exception e)
		{
			ct = CountType.HIGHEST;
		}
		return ct;
	}
	
	public static YamlConfiguration config = null;
	
	public static boolean isSignShopEnabled()
	{
		return config.getBoolean("Enable.SignStorage", false);
	}
	
	public boolean isAuctionEnabled()
	{
		return config.getBoolean("Enable.Auction", false);
	}
	
	public static boolean isMechanicModifierEnabled()
	{
		return config.getBoolean("EnableMechanic.Modifier", false);
	}
	
	public static boolean isMechanicValueEntryEnabled()
	{
		return config.getBoolean("EnableMechanic.ValueEntry", false);
	}
	
	public static String getSignQuantityStorageInitLine()
	{
		return config.getString("SignStorage.SignQuantityInitializationLine", "[VssQuantity]");
	}
	
	public static String getSignVariousStorageInitLine()
	{
		return config.getString("SignStorage.SignVariousInitializationLine", "[VssVarious]");
	}
	
	public static String getSignStorageCopyLine()
	{
		return config.getString("SignStorage.SignCopyLine", "[Copy]");
	}
	
	public static String getSignStorageMoveLine()
	{
		return config.getString("SignStorage.SignMoveLine", "[Move]");
	}
	
	public static long getDefaulStartItemStorageQuantity()
	{
		return config.getLong("SignShop.SignStorageStartItemStorageQuantity", 17280);
	}
	
	public static List<String> getCostToAdd1Storage()
	{
		return config.getStringList("SignStorage.CostToAdd1Storage");
	}
	
	public static List<String> getForbiddenWorld()
	{
		return config.getStringList("SignStorage.ForbiddenWorld");
	}
	
	public static boolean isLine4CalculateInStack()
	{
		return config.getBoolean("SignStorage.Sign.Line4CalculateInStack", false);
	}
	
	public static long getDefaultItemOutput()
	{
		return config.getLong("SignStorage.Sign.DefaultOutput", 1);
	}
	
	public static long getDefaultItemShiftOutput()
	{
		return config.getLong("SignStorage.Sign.DefaultShiftOutput", 64);
	}
	
	public static long getDefaultItemInput()
	{
		return config.getLong("SignStorage.Sign.DefaultInput", 1);
	}
	
	public static long getDefaultItemShiftInput()
	{
		return config.getLong("SignStorage.Sign.DefaultShiftInput", 64);
	}
	
	public static boolean canItemHologramSpawn()
	{
		return config.getBoolean("SignStorage.ItemHologram.CanSpawn", true);
	}
	
	public static long getItemHologramRunTimer()
	{
		return config.getLong("SignStorage.ItemHologram.RunTimerInSeconds", 2);
	}
	
	public static long getItemHologramVisibilityTime()
	{
		return config.getLong("SignStorage.ItemHologram.VisibilityTimeInSeconds", 3);
	}
	
	public static boolean canStoreShulker()
	{
		return config.getBoolean("SignStorage.CanStoreShulker", true);
	}
	
	public static boolean useMaterialAsStorageName()
	{
		return config.getBoolean("SignStorage.StorageUseMaterialAsStorageName", true);
	}
	
	public static boolean isForceSettingsLevel()
	{
		return config.getBoolean("SignStorage.Gui.ForceSettingsLevel", false);
	}
	
	public static long getGuiClickCooldown()
	{
		return config.getLong("SignStorage.Gui.ClickCooldown", 500);
	}
	
	public static SettingsLevel getForcedSettingsLevel()
	{
		try
		{
			return SettingsLevel.valueOf(config.getString("SignStorage.Gui.ToBeForcedSettingsLevel"));
		} catch(Exception e)
		{
			return SettingsLevel.BASE;
		}
	}
	
	public static boolean fillNotDefineGuiSlots()
	{
		return config.getBoolean("SignStorage.Gui.FillNotDefineGuiSlots", true);
	}
	
	public static Material fillerMaterial()
	{
		try
		{
			return Material.valueOf(config.getString("SignStorage.Gui.FillerItemMaterial"));
		} catch(Exception e)
		{
			return Material.LIGHT_GRAY_STAINED_GLASS_PANE;
		}
	}
}