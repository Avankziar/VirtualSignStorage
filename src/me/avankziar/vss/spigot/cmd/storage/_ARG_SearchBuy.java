package me.avankziar.vss.spigot.cmd.storage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.avankziar.vss.general.ChatApi;
import me.avankziar.vss.general.cmdtree.ArgumentConstructor;
import me.avankziar.vss.general.database.MysqlType;
import me.avankziar.vss.general.objects.SignQStorage;
import me.avankziar.vss.spigot.VSS;
import me.avankziar.vss.spigot.assistance.MatchApi;
import me.avankziar.vss.spigot.cmdtree.ArgumentModule;
import me.avankziar.vss.spigot.gui.objects.GuiType;
import me.avankziar.vss.spigot.gui.objects.SettingsLevel;
import me.avankziar.vss.spigot.handler.GuiHandler;

public class _ARG_SearchBuy extends ArgumentModule
{
	private VSS plugin;
	
	public _ARG_SearchBuy(VSS plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
	}

	//sale shop searchbuy [Material] [Displayname...]
	@Override
	public void run(CommandSender sender, String[] args) throws IOException
	{
		Player player = (Player) sender;
		if(isOnCooldown(player.getUniqueId()))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("OnCooldown")));
			return;
		}
		setCooldown(player.getUniqueId(), 15, TimeUnit.SECONDS);
		Material mat = null;
		String displayname = null;
		if(args.length == 2)
		{
			ItemStack mainhand = player.getInventory().getItemInMainHand();
			if(mainhand == null || mainhand.getType() == Material.AIR)
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Cmd.Search.NoItemInHand")));
				removeCooldown(player.getUniqueId());
				return;
			}
			mat = mainhand.getType();
		} else if(args.length >= 3)
		{
			try
			{
				mat = Material.valueOf(args[2]);
			} catch(Exception e)
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Cmd.Search.MaterialDontExist")));
				removeCooldown(player.getUniqueId());
				return;
			}
			if(args.length >= 4)
			{
				for(int i = 3; i < args.length; i++)
				{
					displayname += args[i];
					if(i+1 < args.length)
					{
						displayname += " ";
					}
				}
			}
		}
		Material searchMat = mat;
		String searchDisplayname = displayname;
		String searchradius = plugin.getYamlHandler().getConfig().getString("SignShop.Search.Radius"); //PROXY, SERVER, WORLD, Zahl für Radius
		switch(searchradius)
		{
		default:
			if(!MatchApi.isInteger(searchradius))
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Cmd.Search.SearchRadiusNoCorrectFormat")));
				return;
			}
		case "PROXY":
		case "SERVER":
		case "WORLD":
			break;
		}
		boolean price_OR_RandomSort = "PRICE".equals(plugin.getYamlHandler().getConfig().getString("SignShop.Search.SortType", "PRICE"));
		boolean teleport_OR_Location = "TELEPORT".equals(plugin.getYamlHandler().getConfig().getString("SignShop.Search.DoAfterGuiClick", "LOCATION"));
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				doAsync(player, searchMat, searchDisplayname, searchradius, price_OR_RandomSort, teleport_OR_Location);
			}
		}.runTaskAsynchronously(plugin);
	}
	
	private void doAsync(Player player, Material searchMat, String searchDisplayname,
			String searchRadius, boolean price_OR_RandomSort, boolean teleport_OR_Location)
	{
		String orderBy = null;
		if(price_OR_RandomSort)
		{
			orderBy = "`buy_amount` ASC, `item_storage_current` DESC";
		} else
		{
			orderBy = "rand(), `item_storage_current` DESC";
		}
		ArrayList<SignQStorage> list = new ArrayList<>();
		String s = null;
		String w = null;
		if(searchDisplayname == null)
		{
			switch(searchRadius)
			{
			default:
				int r = Integer.valueOf(searchRadius);
				int xmax = player.getLocation().getBlockX()+r;
				int xmin = player.getLocation().getBlockX()-r;
				int ymax = player.getLocation().getBlockY()+r;
				int ymin = player.getLocation().getBlockY()-r;
				int zmax = player.getLocation().getBlockZ()+r;
				int zmin = player.getLocation().getBlockZ()-r;
				s = plugin.getServername();
				w = player.getWorld().getName();
				list = SignQStorage.convert(plugin.getMysqlHandler().getList(MysqlType.SIGNQSTORAGE, orderBy, 0, 53,
						"`material` = ? AND `item_storage_current` > ? AND `can_buy` = ? AND `buy_amount` > ? "
						+ "AND `server_name` = ? AND `world` = ? "
						+ "AND `x` < ? AND `x` > ? "
						+ "AND `y` < ? AND `y` > ? "
						+ "AND `z` < ? AND `z` > ?",
						searchMat.toString(), 0, true, 0,
						s, w, xmax, xmin, ymax, ymin, zmax, zmin));
				break;
			case "PROXY":
				list = SignQStorage.convert(plugin.getMysqlHandler().getList(MysqlType.SIGNQSTORAGE, orderBy, 0, 53,
						"`material` = ? AND `item_storage_current` > ?  AND `can_buy` = ? AND `buy_amount` > ?",
						searchMat.toString(), 0, true, 0));
				break;
			case "SERVER":
				s = plugin.getServername();
				list = SignQStorage.convert(plugin.getMysqlHandler().getList(MysqlType.SIGNQSTORAGE, orderBy, 0, 53,
						"`material` = ? AND `item_storage_current` > ? AND `can_buy` = ? AND `buy_amount` > ? "
						+ "AND `server_name` = ?",
						searchMat.toString(), 0, true, 0, s));
				break;
			case "WORLD":
				s = plugin.getServername();
				w = player.getWorld().getName();
				list = SignQStorage.convert(plugin.getMysqlHandler().getList(MysqlType.SIGNQSTORAGE, orderBy, 0, 53,
						"`material` = ? AND `item_storage_current` > ? AND `can_buy` = ? AND `buy_amount` > ? "
						+ "AND `server_name` = ? AND `world` = ? ",
						searchMat.toString(), 0, true, 0, s, w));
				break;
			}
		} else
		{
			switch(searchRadius)
			{
			default:
				int r = Integer.valueOf(searchRadius);
				int xmax = player.getLocation().getBlockX()+r;
				int xmin = player.getLocation().getBlockX()-r;
				int ymax = player.getLocation().getBlockY()+r;
				int ymin = player.getLocation().getBlockY()-r;
				int zmax = player.getLocation().getBlockZ()+r;
				int zmin = player.getLocation().getBlockZ()-r;
				s = plugin.getServername();
				w = player.getWorld().getName();
				list = SignQStorage.convert(plugin.getMysqlHandler().getList(MysqlType.SIGNQSTORAGE, orderBy, 0, 53,
						"`material` = ? AND `display_name` LIKE ? AND `item_storage_current` > ? AND `can_buy` = ? AND `buy_amount` > ? "
						+ "AND `server_name` = ? AND `world` = ? "
						+ "AND `x` < ? AND `x` > ? "
						+ "AND `y` < ? AND `y` > ? "
						+ "AND `z` < ? AND `z` > ?",
						searchMat.toString(), "%"+searchDisplayname+"%", 0, true, 0, s, w, xmax, xmin, ymax, ymin, zmax, zmin));
				break;
			case "PROXY":
				list = SignQStorage.convert(plugin.getMysqlHandler().getList(MysqlType.SIGNQSTORAGE, orderBy, 0, 53,
						"`material` = ? AND `display_name` LIKE ? AND `item_storage_current` > ? AND `can_buy` = ? AND `buy_amount` > ?",
						searchMat.toString(), "%"+searchDisplayname+"%", 0, true, 0));
				break;
			case "SERVER":
				s = plugin.getServername();
				list = SignQStorage.convert(plugin.getMysqlHandler().getList(MysqlType.SIGNQSTORAGE, orderBy, 0, 53,
						"`material` = ? AND `display_name` LIKE ? AND `item_storage_current` > ? AND `can_buy` = ? AND `buy_amount` > ? "
						+ "AND `server_name` = ?",
						searchMat.toString(), "%"+searchDisplayname+"%", 0, true, 0, s));
				break;
			case "WORLD":
				s = plugin.getServername();
				w = player.getWorld().getName();
				list = SignQStorage.convert(plugin.getMysqlHandler().getList(MysqlType.SIGNQSTORAGE, orderBy, 0, 53,
						"`material` = ? AND `display_name` LIKE ? AND `item_storage_current` > ? AND `can_buy` = ? AND `buy_amount` > ? "
						+ "AND `server_name` = ? AND `world` = ?",
						searchMat.toString(), "%"+searchDisplayname+"%", 0, true, 0, s, w));
				break;
			}
		}
		if(list.isEmpty())
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Cmd.Search.SearchListEmpty")
					.replace("%mat%", searchMat.toString())
					.replace("%displayname%", (searchDisplayname == null ? "/" : searchDisplayname))));
			removeCooldown(player.getUniqueId());
			return;
		}
		GuiHandler.openSearch(list, player, GuiType.SEARCH_BUY, SettingsLevel.NOLEVEL, true, searchMat, teleport_OR_Location);
	}
}