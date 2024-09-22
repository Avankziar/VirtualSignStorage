package me.avankziar.vss.spigot.handler.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import me.avankziar.vss.general.ChatApi;
import me.avankziar.vss.general.database.MysqlType;
import me.avankziar.vss.general.objects.SignStorage;
import me.avankziar.vss.spigot.VSS;
import me.avankziar.vss.spigot.cmd.vss.ARGSubscribed;
import me.avankziar.vss.spigot.gui.objects.ClickFunctionType;
import me.avankziar.vss.spigot.handler.GuiHandler;

public class SubscribedFunctionHandler
{
	public static void doClickFunktion(ClickFunctionType cft, Player player, SignStorage ssh,
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
	
	private static void subscribed(Player player, SignStorage ssh)
	{
		if(!VSS.getPlugin().getServername().equals(ssh.getServer()))
		{
			player.sendMessage(ChatApi.tl(VSS.getPlugin().getYamlHandler().getLang().getString("Cmd.Search.TeleportIsNull")));
			List<String> list = GuiHandler.getLorePlaceHolder(ssh, player,
					VSS.getPlugin().getYamlHandler().getLang().getStringList("Cmd.Subscribed.LocationInfo"), player.getName());
			list.stream().forEach(x -> player.sendMessage(ChatApi.tl(x)));
			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					player.closeInventory();
				}
			}.runTask(VSS.getPlugin());
		} else
		{
			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					player.closeInventory();
					//GuiHandler.openShop(ssh, player, SettingsLevel.BASE, false);
				}
			}.runTask(VSS.getPlugin());
		}
	}
	
	private static void pagination(Player player, int page, String where, Inventory inv)
	{
		String sql = "SELECT * FROM `"+MysqlType.SIGNSTORAGE.getValue()+"` ";
		ArrayList<SignStorage> list = ARGSubscribed.getSubscribed(sql, where, page);
		GuiHandler.openSubscribed(list, player, page, where, true, inv);
	}
}