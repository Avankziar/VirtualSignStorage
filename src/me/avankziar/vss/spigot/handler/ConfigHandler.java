package me.avankziar.vss.spigot.handler;

import me.avankziar.vss.spigot.cmdtree.BaseConstructor;

public class ConfigHandler
{
	public enum CountType
	{
		HIGHEST, ADDUP;
	}
	
	public CountType getCountPermType()
	{
		String s = BaseConstructor.getPlugin().getYamlHandler().getConfig().getString("Mechanic.CountPerm", "HIGHEST");
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
	
	public boolean isSignShopEnabled()
	{
		return BaseConstructor.getPlugin().getYamlHandler().getConfig().getBoolean("Enable.SignShop", false);
	}
	
	public boolean isAuctionEnabled()
	{
		return BaseConstructor.getPlugin().getYamlHandler().getConfig().getBoolean("Enable.Auction", false);
	}
	
	public boolean isMechanicModifierEnabled()
	{
		return BaseConstructor.getPlugin().getYamlHandler().getConfig().getBoolean("EnableMechanic.Modifier", false);
	}
	
	public boolean isMechanicValueEntryEnabled()
	{
		return BaseConstructor.getPlugin().getYamlHandler().getConfig().getBoolean("EnableMechanic.ValueEntry", false);
	}
	
	public String getSignShopInitLine()
	{
		return BaseConstructor.getPlugin().getYamlHandler().getConfig().getString("SignShop.SignInitializationLine", "[SaleShop]");
	}
	
	public String getSignShopCopyLine()
	{
		return BaseConstructor.getPlugin().getYamlHandler().getConfig().getString("SignShop.SignCopyLine", "[Copy]");
	}
	
	public String getSignShopMoveLine()
	{
		return BaseConstructor.getPlugin().getYamlHandler().getConfig().getString("SignShop.SignMoveLine", "[Move]");
	}
	
	public long getDefaulStartItemStorage()
	{
		return BaseConstructor.getPlugin().getYamlHandler().getConfig().getLong("SignShop.DefaultStartItemStorage", 3456);
	}
	
	public boolean shopCanTradeShulker()
	{
		return BaseConstructor.getPlugin().getYamlHandler().getConfig().getBoolean("SignShop.ShopCanTradeShulker", true);
	}
	
	public boolean fillNotDefineGuiSlots()
	{
		return BaseConstructor.getPlugin().getYamlHandler().getConfig().getBoolean("SignShop.Gui.FillNotDefineGuiSlots", true);
	}
	
	public int getDefaulMaxSubscribeShops()
	{
		return BaseConstructor.getPlugin().getYamlHandler().getConfig().getInt("SignShop.DefaultMaxSubscribtion", 45);
	}
}