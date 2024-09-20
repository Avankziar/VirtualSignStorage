package me.avankziar.vss.spigot.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;

import me.avankziar.vss.spigot.handler.ItemHologramHandler;
import me.avankziar.vss.spigot.objects.ItemHologram;

public class PlayerArmorStandManipulateListener implements Listener
{
	@EventHandler
	public void onArmorStandManipulate(PlayerArmorStandManipulateEvent event)
	{
		for(ItemHologram ish : ItemHologramHandler.taskMap.values())
		{
			if(ish.cancelManipulateEvent(event.getRightClicked()))
			{
				event.setCancelled(true);
				break;
			}
		}
	}
}