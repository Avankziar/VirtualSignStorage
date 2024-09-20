package me.avankziar.vss.spigot.gui.listener;

import me.avankziar.vss.spigot.VSS;

public class FunctionHandler
{
	private VSS plugin;
	
	public FunctionHandler(VSS plugin)
	{
		this.plugin = plugin;
	}
	
	/**
	 * default function handler
	 */
	public void bottomFunction1()
	{
		plugin.getLogger().info("");
		return;
	}
	
	public void upperFunction1()
	{
		plugin.getLogger().info("");
		return;
	}
}