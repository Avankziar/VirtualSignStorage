package me.avankziar.vss.spigot.gui.listener;

import me.avankziar.vss.spigot.SaLE;

public class FunctionHandler
{
	private SaLE plugin;
	
	public FunctionHandler(SaLE plugin)
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