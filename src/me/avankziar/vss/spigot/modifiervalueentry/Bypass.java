package me.avankziar.vss.spigot.modifiervalueentry;

import java.util.LinkedHashMap;

import me.avankziar.vss.spigot.VSS;

public class Bypass
{
	public enum Permission
	{
		STORAGE_CREATION,
		STORAGE_CREATION_WORLDGUARD,
		STORAGE_GUI_BYPASS;
		
		public String getValueLable()
		{
			return VSS.getPlugin().pluginname.toLowerCase()+"-"+this.toString().toLowerCase();
		}
	}
	private static LinkedHashMap<Bypass.Permission, String> mapPerm = new LinkedHashMap<>();
	
	public static void set(Bypass.Permission bypass, String perm)
	{
		mapPerm.put(bypass, perm);
	}
	
	public static String get(Bypass.Permission bypass)
	{
		return mapPerm.get(bypass);
	}
	
	public enum Counter
	{
		STORAGE_CREATION_AMOUNT_,
		SHOP_ITEMSTORAGE_AMOUNT_,
		COST_ADDING_STORAGE(false),
		SHOP_BUYING_TAX(false),
		SHOP_SELLING_TAX(false),
		SHOP_SUBSCRIPTION_;
		
		private boolean forPermission;
		
		Counter()
		{
			this.forPermission = true;
		}
		
		Counter(boolean forPermission)
		{
			this.forPermission = forPermission;
		}
	
		public boolean forPermission()
		{
			return this.forPermission;
		}
		
		public String getModification()
		{
			return VSS.getPlugin().pluginname.toLowerCase()+"-"+this.toString().toLowerCase();
		}
	}
	private static LinkedHashMap<Bypass.Counter, String> mapCount = new LinkedHashMap<>();
	
	public static void set(Bypass.Counter bypass, String perm)
	{
		mapCount.put(bypass, perm);
	}
	
	public static String get(Bypass.Counter bypass)
	{
		return mapCount.get(bypass);
	}
}