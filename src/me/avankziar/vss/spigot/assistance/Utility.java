package me.avankziar.vss.spigot.assistance;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.UUID;

import me.avankziar.vss.spigot.VSS;
import me.avankziar.vss.spigot.database.MysqlHandler;
import me.avankziar.vss.spigot.objects.PlayerData;

public class Utility
{
	private static VSS plugin;
	
	public Utility(VSS plugin)
	{
		Utility.plugin = plugin;
	}
	
	public static double getNumberFormat(double d)//FIN
	{
		BigDecimal bd = new BigDecimal(d).setScale(1, RoundingMode.HALF_UP);
		double newd = bd.doubleValue();
		return newd;
	}
	
	public static double getNumberFormat(double d, int scale)
	{
		BigDecimal bd = new BigDecimal(d).setScale(scale, RoundingMode.HALF_UP);
		double newd = bd.doubleValue();
		return newd;
	}
	
	public static String convertUUIDToName(String uuid)
	{
		if(plugin.getMysqlHandler().exist(MysqlHandler.Type.PLAYERDATA, "player_uuid = ?", uuid))
		{
			return ((PlayerData) plugin.getMysqlHandler().getData(MysqlHandler.Type.PLAYERDATA, "player_uuid = ?", uuid)).getName();
		}
		return null;
	}
	
	public static UUID convertNameToUUID(String playername)
	{
		if(plugin.getMysqlHandler().exist(MysqlHandler.Type.PLAYERDATA, "`player_name` = ?", playername))
		{
			return ((PlayerData) plugin.getMysqlHandler().getData(MysqlHandler.Type.PLAYERDATA, "`player_name` = ?", playername)).getUUID();
		}
		return null;
	}
	
	public boolean existMethod(Class<?> externclass, String method)
	{
	    try 
	    {
	    	Method[] mtds = externclass.getMethods();
	    	for(Method methods : mtds)
	    	{
	    		if(methods.getName().equalsIgnoreCase(method))
	    		{
	    	    	return true;
	    		}
	    	}
	    	return false;
	    } catch (Exception e) 
	    {
	    	return false;
	    }
	}
	
	public static String serialised(LocalDateTime dt)
	{
		String MM = "";
		int month = 0;
		if(dt.getMonthValue()<10)
		{
			MM+=month;
		}
		MM += dt.getMonthValue();
		String dd = "";
		int day = 0;
		if(dt.getDayOfMonth()<10)
		{
			dd+=day;
		}
		dd +=dt.getDayOfMonth();
		String hh = "";
		int hour = 0;
		if(dt.getHour()<10)
		{
			hh+=hour;
		}
		hh += dt.getHour();
		String mm = "";
		int min = 0;
		if(dt.getMinute()<10)
		{
			mm+=min;
		}
		mm += dt.getMinute();
		return dd+"."+MM+"."+dt.getYear()+" "+hh+":"+mm;
	}
	
	public static double round(double value, int places) 
	{
	    if (places < 0) throw new IllegalArgumentException();
	    try
	    {
	    	BigDecimal bd = BigDecimal.valueOf(value);
		    bd = bd.setScale(places, RoundingMode.HALF_UP);
		    return bd.doubleValue();
	    } catch (NumberFormatException e)
	    {
	    	return 0;
	    }
	}
}