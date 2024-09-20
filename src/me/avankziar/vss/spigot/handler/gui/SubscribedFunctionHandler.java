package me.avankziar.vss.spigot.handler.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import me.avankziar.vss.general.ChatApi;
import me.avankziar.vss.spigot.SaLE;
import me.avankziar.vss.spigot.cmd.sale.ARGSubscribed;
import me.avankziar.vss.spigot.database.MysqlHandler.Type;
import me.avankziar.vss.spigot.gui.objects.ClickFunctionType;
import me.avankziar.vss.spigot.gui.objects.SettingsLevel;
import me.avankziar.vss.spigot.handler.GuiHandler;
import me.avankziar.vss.spigot.objects.SignShop;

public class SubscribedFunctionHandler
{
	public static void doClickFunktion(ClickFunctionType cft, Player player, SignShop ssh,
			Inventory openInv, int page, String where)
	{
		switch(cft)
		{
		default: return;
		case SUBSCRIBED: subscribed(player, ssh); break;
		case SUBSCRIBED_PAST:
		case SUBSCRIBED_NEXT: pagination(player, page, where, openInv); break;			
		}
	}
	
	private static void subscribed(Player player, SignShop ssh)
	{
		if(!SaLE.getPlugin().getServername().equals(ssh.getServer()))
		{
			player.sendMessage(ChatApi.tl(SaLE.getPlugin().getYamlHandler().getLang().getString("Cmd.Search.TeleportIsNull")));
			List<String> list = GuiHandler.getLorePlaceHolder(ssh, player,
					SaLE.getPlugin().getYamlHandler().getLang().getStringList("Cmd.Subscribed.LocationInfo"), player.getName());
			list.stream().forEach(x -> player.sendMessage(ChatApi.tl(x)));
			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					player.closeInventory();
				}
			}.runTask(SaLE.getPlugin());
		} else
		{
			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					player.closeInventory();
					GuiHandler.openShop(ssh, player, SettingsLevel.BASE, false);
				}
			}.runTask(SaLE.getPlugin());
		}
	}
	
	private static void pagination(Player player, int page, String where, Inventory inv)
	{
		String sql = "SELECT * FROM `"+Type.SIGNSHOP.getValue()+"` ";
		ArrayList<SignShop> list = ARGSubscribed.getSubscribed(sql, where, page);
		GuiHandler.openSubscribed(list, player, page, where, true, inv);
	}
}