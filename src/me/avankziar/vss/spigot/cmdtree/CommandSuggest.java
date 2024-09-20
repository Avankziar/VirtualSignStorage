package me.avankziar.vss.spigot.cmdtree;

import java.util.LinkedHashMap;

public class CommandSuggest
{
	public static LinkedHashMap<CommandExecuteType, String> map = new LinkedHashMap<>();
	
	public static void set(CommandExecuteType ces, String s)
	{
		map.put(ces, s);
	}
	
	public static String get(CommandExecuteType ces)
	{
		return map.get(ces);
	}
}
