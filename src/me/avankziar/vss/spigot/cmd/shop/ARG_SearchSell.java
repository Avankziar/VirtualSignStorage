package me.avankziar.vss.spigot.cmd.shop;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.avankziar.vss.general.ChatApi;
import me.avankziar.vss.spigot.VSS;
import me.avankziar.vss.spigot.assistance.MatchApi;
import me.avankziar.vss.spigot.cmdtree.ArgumentConstructor;
import me.avankziar.vss.spigot.cmdtree.ArgumentModule;
import me.avankziar.vss.spigot.database.MysqlHandler.Type;
import me.avankziar.vss.spigot.gui.objects.GuiType;
import me.avankziar.vss.spigot.gui.objects.SettingsLevel;
import me.avankziar.vss.spigot.handler.GuiHandler;
import me.avankziar.vss.spigot.objects.SignShop;

public class ARG_SearchSell extends ArgumentModule
{
	private VSS plugin;
	
	public ARG_SearchSell(VSS plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
	}

	//sale shop searchsell [Material] [Displayname...]
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
				removeCooldown(player.getUniqueId());
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
			orderBy = "`sell_amount` DESC, `item_storage_current` ASC";
		} else
		{
			orderBy = "rand(), `item_storage_current` ASC";
		}
		ArrayList<SignShop> list = new ArrayList<>();
		String s = null;
		String w = null;
		//-----Für den SearchSell teil
		//SELECT *, cast(item_storage_total - item_storage_current as SIGNED) as blubb FROM `salesignshop` WHERE `material` = "WOODEN_SWORD" HAVING blubb > 0 ORDER BY `sell_amount` DESC;
		//SELECT *, cast(item_storage_total - item_storage_current as SIGNED) as blubb FROM `salesignshop` WHERE `material` = "WOODEN_SWORD" HAVING blubb > 0 ORDER BY rand();
		String sql = null;
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
				sql = "SELECT *, cast(item_storage_total - item_storage_current as SIGNED) as blubb FROM `" 
							+ Type.SIGNSHOP.getValue()+
							"` WHERE `material` = ? AND `can_sell` = ? AND `sell_amount` > ? "
							+ "AND `server_name` = ? AND `world` = ? "
							+ "AND `x` < ? AND `x` > ? "
							+ "AND `y` < ? AND `y` > ? "
							+ "AND `z` < ? AND `z` > ? "
							+ "HAVING blubb > 0 ORDER BY "+orderBy+" LIMIT 0, 53";
				list = SignShop.convert(plugin.getMysqlHandler().getSQL(Type.SIGNSHOP, sql,
						searchMat.toString(), true, 0,
						s, w, xmax, xmin, ymax, ymin, zmax, zmin));
				break;
			case "PROXY":
				sql = "SELECT *, cast(item_storage_total - item_storage_current as SIGNED) as blubb FROM `" 
						+ Type.SIGNSHOP.getValue()+
						"` WHERE `material` = ? AND `can_sell` = ? AND `sell_amount` > ? "
						+ "HAVING blubb > 0 ORDER BY "+orderBy+" LIMIT 0, 53";
				list = SignShop.convert(plugin.getMysqlHandler().getSQL(Type.SIGNSHOP, sql,
						searchMat.toString(), true, 0));
				break;
			case "SERVER":
				sql = "SELECT *, cast(item_storage_total - item_storage_current as SIGNED) as blubb FROM `" 
						+ Type.SIGNSHOP.getValue()+
						"` WHERE `material` = ? AND `can_sell` = ? AND `sell_amount` > ? "
						+ "AND `server_name` = ? "
						+ "HAVING blubb > 0 ORDER BY "+orderBy+" LIMIT 0, 53";
				s = plugin.getServername();
				list = SignShop.convert(plugin.getMysqlHandler().getSQL(Type.SIGNSHOP, sql,
						searchMat.toString(), true, 0, s));
				break;
			case "WORLD":
				sql = "SELECT *, cast(item_storage_total - item_storage_current as SIGNED) as blubb FROM `" 
						+ Type.SIGNSHOP.getValue()+
						"` WHERE `material` = ? AND `can_sell` = ? AND `sell_amount` > ? "
						+ "AND `server_name` = ? AND `world` = ? "
						+ "HAVING blubb > 0 ORDER BY "+orderBy+" LIMIT 0, 53";
				s = plugin.getServername();
				w = player.getWorld().getName();
				list = SignShop.convert(plugin.getMysqlHandler().getSQL(Type.SIGNSHOP, orderBy, sql,
						searchMat.toString(), true, 0, s, w));
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
				sql = "SELECT *, cast(item_storage_total - item_storage_current as SIGNED) as blubb FROM `" 
						+ Type.SIGNSHOP.getValue()+
						"` WHERE `material` = ? AND `display_name` LIKE ? AND `can_sell` = ? AND `sell_amount` > ? "
						+ "AND `server_name` = ? AND `world` = ? "
						+ "AND `x` < ? AND `x` > ? "
						+ "AND `y` < ? AND `y` > ? "
						+ "AND `z` < ? AND `z` > ? "
						+ "HAVING blubb > 0 ORDER BY "+orderBy+" LIMIT 0, 53";
				s = plugin.getServername();
				w = player.getWorld().getName();
				list = SignShop.convert(plugin.getMysqlHandler().getSQL(Type.SIGNSHOP, sql,
						searchMat.toString(), "%"+searchDisplayname+"%", true, 0, s, w, xmax, xmin, ymax, ymin, zmax, zmin));
				break;
			case "PROXY":
				sql = "SELECT *, cast(item_storage_total - item_storage_current as SIGNED) as blubb FROM `" 
						+ Type.SIGNSHOP.getValue()+
						"` WHERE `material` = ? AND `display_name` LIKE ? AND `can_sell` = ? AND `sell_amount` > ? "
						+ "HAVING blubb > 0 ORDER BY "+orderBy+" LIMIT 0, 53";
				list = SignShop.convert(plugin.getMysqlHandler().getSQL(Type.SIGNSHOP, sql,
						searchMat.toString(), "%"+searchDisplayname+"%", true, 0));
				break;
			case "SERVER":
				sql = "SELECT *, cast(item_storage_total - item_storage_current as SIGNED) as blubb FROM `" 
						+ Type.SIGNSHOP.getValue()+
						"` WHERE `material` = ? AND `display_name` LIKE ? AND `can_sell` = ? AND `sell_amount` > ? "
						+ "AND `server_name` = ? "
						+ "HAVING blubb > 0 ORDER BY "+orderBy+" LIMIT 0, 53";
				s = plugin.getServername();
				list = SignShop.convert(plugin.getMysqlHandler().getSQL(Type.SIGNSHOP, sql,
						searchMat.toString(), "%"+searchDisplayname+"%", true, 0, s));
				break;
			case "WORLD":
				sql = "SELECT *, cast(item_storage_total - item_storage_current as SIGNED) as blubb FROM `" 
						+ Type.SIGNSHOP.getValue()+
						"` WHERE `material` = ? AND `display_name` LIKE ? AND `can_sell` = ? AND `sell_amount` > ? "
						+ "AND `server_name` = ? AND `world` = ? "
						+ "HAVING blubb > 0 ORDER BY "+orderBy+" LIMIT 0, 53";
				s = plugin.getServername();
				w = player.getWorld().getName();
				list = SignShop.convert(plugin.getMysqlHandler().getSQL(Type.SIGNSHOP, sql,
						searchMat.toString(), "%"+searchDisplayname+"%", 1, true, 0, s, w));
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
		GuiHandler.openSearch(list, player, GuiType.SEARCH_SELL, SettingsLevel.NOLEVEL, true, searchMat, teleport_OR_Location);
	}
}