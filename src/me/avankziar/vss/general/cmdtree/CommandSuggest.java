package me.avankziar.vss.general.cmdtree;

import java.util.LinkedHashMap;

public class CommandSuggest
{
	/**
	 * All Commands and their following arguments
	 */
	public enum Type 
	{
		VSS,
		VSS_STORAGE, //Überbefehl für die statistischen Befehle
		VSS_DEBUG, //Only for Debugging Purpose
		VSS_STORAGE_DELETE, //Admincmd zum delete
		VSS_STORAGE_TOGGLE, //Toggle um in AdminGui
		VSS_STORAGE_BREAKTOGGLE, //Breaktoggle um schilder schnell abzubauen.
		VSS_STORAGE_SEARCHBUY,
		VSS_STORAGE_SEARCHSELL,
		/*
		 * Aufrufen des globalen GUI, das es ermöglich alle Shops aller Spieler aus der Ferne zu erreichen.
		 * Auch soll man hier nach allen Shops filtern können, welches x material anbieten. (Tastaturpad a la NumPad gui?)
		 * Dazu soll das auktionsystem erreichbar sein.
		 */
		SALE_SUBSCRIBED, //REMOVEME
		;
	}
	
	public static LinkedHashMap<CommandSuggest.Type, BaseConstructor> map = new LinkedHashMap<>();
	
	public static void set(CommandSuggest.Type cst, BaseConstructor bc)
	{
		map.put(cst, bc);
	}
	
	public static BaseConstructor get(CommandSuggest.Type ces)
	{
		return map.get(ces);
	}
	
	public static String getCmdString(CommandSuggest.Type ces)
	{
		BaseConstructor bc = map.get(ces);
		return bc != null ? bc.getCommandString() : null;
	}
	
	
}
