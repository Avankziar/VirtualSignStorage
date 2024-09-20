package me.avankziar.vss.spigot.gui.events;

import me.avankziar.vss.spigot.gui.objects.ClickFunctionType;
import me.avankziar.vss.spigot.gui.objects.ClickType;

public class ClickFunction
{
	private ClickType clickType;
	private String function;
	
	public ClickFunction(ClickType clickFunctionType, String function)
	{
		setClickType(clickFunctionType);
		setFunction(function);
	}
	
	public ClickFunction(ClickType clickFunctionType, ClickFunctionType function)
	{
		setClickType(clickFunctionType);
		setFunction(function.toString());
	}

	public ClickType getClickType()
	{
		return clickType;
	}

	public void setClickType(ClickType clickType)
	{
		this.clickType = clickType;
	}

	public String getFunction()
	{
		return function;
	}

	public void setFunction(String function)
	{
		this.function = function;
	}
	
	@Override
	public String toString()
	{
		return clickType.toString()+";"+function;
	}
}
