package me.avankziar.vss.spigot.gui.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;

public class BottomGuiClickEvent extends Event
{
	private static final HandlerList HANDLERS = new HandlerList();
	
	private boolean isCancelled;
	private InventoryClickEvent inventoryClickEvent;
	private String pluginName;
	private String inventoryIdentifier;
	
	public BottomGuiClickEvent(InventoryClickEvent inventoryClickEvent,
			String pluginName, String inventoryIdentifier)
	{
		setCancelled(false);
		setInventoryClickEvent(inventoryClickEvent);
		setPluginName(pluginName);
		setInventoryIdentifier(inventoryIdentifier);
	}
	
	public HandlerList getHandlers() 
    {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() 
    {
        return HANDLERS;
    }

	public boolean isCancelled()
	{
		return isCancelled;
	}

	public void setCancelled(boolean isCancelled)
	{
		this.isCancelled = isCancelled;
	}

	public InventoryClickEvent getEvent()
	{
		return inventoryClickEvent;
	}

	public void setInventoryClickEvent(InventoryClickEvent inventoryClickEvent)
	{
		this.inventoryClickEvent = inventoryClickEvent;
	}

	public String getPluginName()
	{
		return pluginName;
	}

	public void setPluginName(String pluginName)
	{
		this.pluginName = pluginName;
	}

	public String getInventoryIdentifier()
	{
		return inventoryIdentifier;
	}

	public void setInventoryIdentifier(String inventoryIdentifier)
	{
		this.inventoryIdentifier = inventoryIdentifier;
	}
}