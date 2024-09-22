package me.avankziar.vss.spigot.handler;

import me.avankziar.vss.spigot.VSS;

public class ConfigHandler
{
	public enum CountType
	{
		HIGHEST, ADDUP;
	}
	
	public CountType getCountPermType()
	{
		String s = VSS.getPlugin().getYamlHandler().getConfig().getString("Mechanic.CountPerm", "HIGHEST");
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
		return VSS.getPlugin().getYamlHandler().getConfig().getBoolean("Enable.SignShop", false);
	}
	
	public boolean isAuctionEnabled()
	{
		return VSS.getPlugin().getYamlHandler().getConfig().getBoolean("Enable.Auction", false);
	}
	
	public boolean isMechanicModifierEnabled()
	{
		return VSS.getPlugin().getYamlHandler().getConfig().getBoolean("EnableMechanic.Modifier", false);
	}
	
	public boolean isMechanicValueEntryEnabled()
	{
		return VSS.getPlugin().getYamlHandler().getConfig().getBoolean("EnableMechanic.ValueEntry", false);
	}
	
	public String getSignShopInitLine()
	{
		return VSS.getPlugin().getYamlHandler().getConfig().getString("SignShop.SignInitializationLine", "[SaleShop]");
	}
	
	public String getSignShopCopyLine()
	{
		return VSS.getPlugin().getYamlHandler().getConfig().getString("SignShop.SignCopyLine", "[Copy]");
	}
	
	public String getSignShopMoveLine()
	{
		return VSS.getPlugin().getYamlHandler().getConfig().getString("SignShop.SignMoveLine", "[Move]");
	}
	
	public long getDefaulStartItemStorage()
	{
		return VSS.getPlugin().getYamlHandler().getConfig().getLong("SignShop.DefaultStartItemStorage", 3456);
	}
	
	public long getDefaultItemOutput()
	{
		return 0;//ADDME
	}
	
	public long getDefaultItemShiftOutput()
	{
		return 0;//ADDME
	}
	
	public long getDefaultItemInput()
	{
		return 0;//ADDME
	}
	
	public long getDefaultItemShiftInput()
	{
		return 0;//ADDME
	}
	
	public boolean shopCanTradeShulker()
	{
		return VSS.getPlugin().getYamlHandler().getConfig().getBoolean("SignShop.ShopCanTradeShulker", true);
	}
	
	public boolean fillNotDefineGuiSlots()
	{
		return VSS.getPlugin().getYamlHandler().getConfig().getBoolean("SignShop.Gui.FillNotDefineGuiSlots", true);
	}
	
	public int getDefaulMaxSubscribeShops()
	{
		return VSS.getPlugin().getYamlHandler().getConfig().getInt("SignShop.DefaultMaxSubscribtion", 45);
	}
}