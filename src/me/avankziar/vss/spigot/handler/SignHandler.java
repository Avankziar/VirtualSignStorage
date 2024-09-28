package me.avankziar.vss.spigot.handler;

import org.bukkit.entity.Player;

import me.avankziar.vss.general.database.MysqlType;
import me.avankziar.vss.spigot.VSS;

public class SignHandler 
{
	public static int getAmountOfStorage(Player player)
	{
		int a = VSS.getPlugin().getMysqlHandler().getCount(
				MysqlType.SIGNQSTORAGE, "`player_uuid` = ?", player.getUniqueId().toString());
		//ADDME SignVStorage
		return a;
	}
}