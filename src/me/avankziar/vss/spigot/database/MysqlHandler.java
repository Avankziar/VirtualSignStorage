package me.avankziar.vss.spigot.database;

import me.avankziar.vss.general.database.MysqlBaseHandler;
import me.avankziar.vss.spigot.VSS;

public class MysqlHandler extends MysqlBaseHandler
{	
	public MysqlHandler(VSS plugin)
	{
		super(plugin.getLogger(), plugin.getMysqlSetup());
	}
}