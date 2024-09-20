package me.avankziar.vss.spigot.cmdtree;

public enum CommandExecuteType
{
	SALE,
	SALE_SHOP, //Überbefehl für die statistischen Befehle
	//SALE_GUI, //Ruft ein Gui auf, zum öffnen von den SignShop Guis. ?Eventuell?
	SALE_DEBUG, //Only for Debugging Purpose
	SALE_SHOP_DELETE, //Admincmd zum delete
	SALE_SHOP_TOGGLE, //Toggle um in AdminGui
	SALE_SHOP_BREAKTOGGLE, //Breaktoggle um schilder schnell abzubauen.
	SALE_SHOP_SEARCHBUY,
	SALE_SHOP_SEARCHSELL,
	/*
	 * Aufrufen des globalen GUI, das es ermöglich alle Shops aller Spieler aus der Ferne zu erreichen.
	 * Auch soll man hier nach allen Shops filtern können, welches x material anbieten. (Tastaturpad a la NumPad gui?)
	 * Dazu soll das auktionsystem erreichbar sein.
	 */
	//TODO Befehl um alle Spieler auf den ListedType aufzulisten Vielleicht?
	//TODO Worldguard negativ flag einstellen, um zu verbieten das man shop erstellen darf.
	//TODO Worldguard shop search. mit angabe des Spielername und des grundstücknamens.
	SALE_SHOP_LOG,
	SALE_SHOP_DAILYLOG,
	SALE_CLIENT,
	SALE_CLIENT_LOG,
	SALE_CLIENT_DAILYLOG,
	SALE_SUBSCRIBED,
}