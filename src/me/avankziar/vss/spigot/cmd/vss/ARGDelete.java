package me.avankziar.vss.spigot.cmd.vss;

import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.avankziar.vss.general.ChatApi;
import me.avankziar.vss.general.cmdtree.ArgumentConstructor;
import me.avankziar.vss.general.database.MysqlType;
import me.avankziar.vss.general.objects.SignQStorage;
import me.avankziar.vss.spigot.VSS;
import me.avankziar.vss.spigot.assistance.MatchApi;
import me.avankziar.vss.spigot.cmdtree.ArgumentModule;
import me.avankziar.vss.spigot.handler.Base64Handler;

public class ARGDelete extends ArgumentModule
{
	private VSS plugin;
	
	public ARGDelete(VSS plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
	}

	//vss delete [xxx:yyy...]
	@Override
	public void run(CommandSender sender, String[] args) throws IOException
	{
		Player player = (Player) sender;
		String query = "";
		ArrayList<Object> ol = new ArrayList<>();
		for(int i = 1; i < args.length; i++)
		{
			String[] split = args[i].split(":");
			if(split.length != 2)
			{
				continue;
			}
			String identifier = split[0];
			String value = split[1];
			if(i > 1)
			{
				query += " AND ";
			}
			switch(identifier)
			{
			default:
				break;
			case "id":
				query += "`id` = ?";
				ol.add(value);
				break;
			case "world":
				query += "`world` = ?";
				ol.add(value);
				break;
			case "server":
				query += "`server_name` = ?";
				ol.add(value);
				break;
			case "player":
				query += "`player_uuid` = ?";
				ol.add(value);
				break;
			case "item":
				if(player.getInventory().getItemInMainHand() == null || player.getInventory().getItemInMainHand().getType() == Material.AIR)
				{
					continue;
				}
				query += "`itemstack_base64` = ?";
				ol.add(new Base64Handler(player.getInventory().getItemInMainHand()).toBase64());
				break;
			case "radius":
				if(!MatchApi.isInteger(value))
				{
					continue;
				}
				String server = plugin.getServername();
				String world = player.getWorld().getName();
				int v = Integer.parseInt(value);
				int xmax = player.getLocation().getBlockX() + v;
				int xmin = player.getLocation().getBlockX() - v;
				int ymax = player.getLocation().getBlockY() + v;
				int ymin = player.getLocation().getBlockY() - v;
				int zmax = player.getLocation().getBlockZ() + v;
				int zmin = player.getLocation().getBlockZ() - v;
				query += "`server_name` = ? AND `world` = ? AND `x` > ? AND `x` <= ? AND `y` > ? AND `y` <= ? AND z` > ? AND `z` <= ?";
				ol.add(server);
				ol.add(world);
				ol.add(xmax);
				ol.add(xmin);
				ol.add(ymax);
				ol.add(ymin);
				ol.add(zmax);
				ol.add(zmin);
				break;
			}
		}
		if(query.isEmpty())
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Cmd.Delete.NoFoundToDelete")));
			return;
		}
		ArrayList<SignQStorage> sshl = SignQStorage.convert(plugin.getMysqlHandler().getFullList(MysqlType.SIGNQSTORAGE,
				"`id` ASC", query, ol.toArray(new Object[ol.size()])));
		final int sshla = sshl.size();
		if(sshla <= 0)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Cmd.Delete.NoFoundToDelete")));
			return;
		}
		long itemLost = 0;
		for(SignQStorage ssh : sshl)
		{
			itemLost += ssh.getItemStorageCurrent();
			plugin.getMysqlHandler().deleteData(MysqlType.SIGNQSTORAGE, "`id` = ?", ssh.getId());
		}
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Cmd.Delete.Delete")
				.replace("%storageamount%", String.valueOf(sshla))
				.replace("%itemlost%", String.valueOf(itemLost))
				));
		return;
	}
}