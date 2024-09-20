package me.avankziar.vss.spigot.handler.gui;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import me.avankziar.vss.general.ChatApi;
import me.avankziar.vss.spigot.SaLE;
import me.avankziar.vss.spigot.gui.objects.ClickFunctionType;
import me.avankziar.vss.spigot.handler.GuiHandler;
import me.avankziar.vss.spigot.objects.SignShop;

public class SearchFunctionHandler
{

	public static void doClickFunktion(ClickFunctionType cft, Player player, SignShop ssh,
			Inventory openInv, boolean teleport_OR_location)
	{
		switch(cft)
		{
		default: return;
		case SEARCH_BUY: search(player, ssh, openInv, teleport_OR_location, "Buy"); break;
		case SEARCH_SELL:search(player, ssh, openInv, teleport_OR_location, "Sell"); break;
			
		}
	}
	
	public static void search(Player player, SignShop ssh, Inventory openInv, boolean teleport_OR_location, String buyOrSell)
	{
		if(teleport_OR_location)
		{
			if(SaLE.getPlugin().getServername().equals(ssh.getServer()))
			{
				final Location loc = new Location(Bukkit.getWorld(ssh.getWorld()), ssh.getX(), ssh.getY(), ssh.getZ());
				new BukkitRunnable()
				{
					@Override
					public void run()
					{
						player.closeInventory();
						player.teleport(loc);
					}
				}.runTask(SaLE.getPlugin());
			} else
			{
				if(SaLE.getPlugin().getTeleport() == null)
				{
					player.sendMessage(ChatApi.tl(SaLE.getPlugin().getYamlHandler().getLang().getString("Cmd.Search.TeleportIsNull")));
					List<String> list = GuiHandler.getLorePlaceHolder(ssh, player,
							SaLE.getPlugin().getYamlHandler().getLang().getStringList("Cmd.Search."+buyOrSell+".LocationInfo"), player.getName());
					list.stream().forEach(x -> player.sendMessage(ChatApi.tl(x)));
				} else
				{
					new BukkitRunnable()
					{
						@Override
						public void run()
						{
							player.closeInventory();
							SaLE.getPlugin().getTeleport().teleport(player, ssh.getServer(), ssh.getWorld(), ssh.getX(), ssh.getY(), ssh.getZ(), 0, 0);
						}
					}.runTask(SaLE.getPlugin());
				}
			}
		} else
		{
			List<String> list = GuiHandler.getLorePlaceHolder(ssh, player,
					SaLE.getPlugin().getYamlHandler().getLang().getStringList("Cmd.Search."+buyOrSell+".LocationInfo"), player.getName());
			list.stream().forEach(x -> player.sendMessage(ChatApi.tl(x)));
		}
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				player.closeInventory();
			}
		}.runTask(SaLE.getPlugin());
	}
}