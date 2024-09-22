package me.avankziar.vss.spigot.cmd.vss;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.avankziar.vss.general.ChatApi;
import me.avankziar.vss.general.cmdtree.ArgumentConstructor;
import me.avankziar.vss.general.database.MysqlType;
import me.avankziar.vss.general.objects.SignStorage;
import me.avankziar.vss.spigot.VSS;
import me.avankziar.vss.spigot.assistance.MatchApi;
import me.avankziar.vss.spigot.assistance.Utility;
import me.avankziar.vss.spigot.cmdtree.ArgumentModule;
import me.avankziar.vss.spigot.handler.GuiHandler;
import me.avankziar.vss.spigot.objects.SubscribedShop;

public class ARGSubscribed extends ArgumentModule
{
	private VSS plugin;
	
	public ARGSubscribed(VSS plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
	}

	//sale abo [page] [type...]
	/*
	 * buycost><X
	 * sellcost><X
	 * storage><X
	 * material=X
	 * displayname=X
	 * player=X
	 * sameserver
	 * sameworld
	 */
	@Override
	public void run(CommandSender sender, String[] args) throws IOException
	{
		Player player = (Player) sender;
		int pages = 0;
		if(args.length >= 2)
		{
			if(MatchApi.isInteger(args[1]))
			{
				pages = Integer.valueOf(args[1]);
			}
		}
		int page = pages;
		if(isOnCooldown(player.getUniqueId()))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("OnCooldown")));
			return;
		}
		setCooldown(player.getUniqueId(), 5, TimeUnit.SECONDS);
		ArrayList<SubscribedShop> subshops = SubscribedShop.convert(
				plugin.getMysqlHandler().getFullList(Type.SUBSCRIBEDSHOP,
				"`id` ASC", "`player_uuid` = ?", player.getUniqueId().toString()));
		if(subshops.isEmpty())
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Cmd.Subscribed.NoSubscribes")));
			removeCooldown(player.getUniqueId());
			return;
		}
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				doAsync(player, args, page, subshops);
			}
		}.runTaskAsynchronously(plugin);
	}
	
	private void doAsync(Player player, String[] args, int page, ArrayList<SubscribedShop> subshops)
	{
		String IN_Function = "WHERE `id` IN("+String.join(",", subshops.stream()
				.map(x -> String.valueOf(x.getSignShopId()))
				.collect(Collectors.toList()))+") AND ";
		StringBuilder whereQuery = new StringBuilder();
		String sql = "SELECT * FROM `"+MysqlType.SIGNSTORAGE.getValue()+"` ";
		for(int i = 2; i < args.length; i++)
		{
			String a = args[i];
			String charc = "";
			String c = "";
			switch(a)
			{
			default:
				if(a.startsWith("buycost") || a.startsWith("sellcost") || a.startsWith("storage"))
				{
					charc = "";
					c = "";
					if(a.contains(">"))
					{
						if(a.split(">").length != 2)
						{
							continue;
						}
						charc = ">";
						c = a.split(">")[1];
					} else if(a.contains("<"))
					{
						if(a.split("<").length != 2)
						{
							continue;
						}
						charc = "<";
						c = a.split("<")[1];
					} else
					{
						continue;
					}
					if(!MatchApi.isDouble(c))
					{
						continue;
					}
					if(a.startsWith("buycost"))
					{
						if(whereQuery.isEmpty())
						{
							whereQuery.append("`buy_amount` "+charc+" '"+c+"' AND `can_buy` = true");
						} else
						{
							whereQuery.append(" AND `buy_amount` "+charc+" '"+c+"' AND `can_buy` = true");
						}
					} else if(a.startsWith("sellcost"))
					{
						if(whereQuery.isEmpty())
						{
							whereQuery.append("`sell_amount` "+charc+" '"+c+"' AND `can_sell` = true");
						} else
						{
							whereQuery.append(" AND `sell_amount` "+charc+" '"+c+"' AND `can_sell` = true");
						}
					} else if(a.startsWith("storage"))
					{
						if(whereQuery.isEmpty())
						{
							whereQuery.append("`item_storage_current` "+charc+" '"+c+"'");
						} else
						{
							whereQuery.append(" AND `item_storage_current` "+charc+" '"+c+"'");
						}
					}
				} else if(a.startsWith("material") || a.startsWith("displayname") || a.startsWith("player"))
				{
					c = "";
					if(a.contains("="))
					{
						if(a.split("=").length != 2)
						{
							continue;
						}
						c = a.split("=")[1];
					}
					if(a.startsWith("material"))
					{
						if(whereQuery.isEmpty())
						{
							whereQuery.append("`material` = '"+c+"'");
						} else
						{
							whereQuery.append(" AND `material` = '"+c+"'");
						}
					} else if(a.startsWith("displayname"))
					{
						if(whereQuery.isEmpty())
						{
							whereQuery.append("`display_name` LIKE '%"+c+"%'");
						} else
						{
							whereQuery.append(" AND `display_name` LIKE '%"+c+"%'");
						}
					} else if(a.startsWith("player"))
					{
						UUID other = Utility.convertNameToUUID(c);
						if(other == null)
						{
							continue;
						}
						if(whereQuery.isEmpty())
						{
							whereQuery.append("`player_uuid` = '"+other.toString()+"'");
						} else
						{
							whereQuery.append(" AND `player_uuid` = '"+other.toString()+"'");
						}
					}
				}
				break;
			case "sameserver":
				if(whereQuery.isEmpty())
				{
					whereQuery.append("`server_name` = '"+plugin.getServername()+"'");
				} else
				{
					whereQuery.append(" AND `server_name` = '"+plugin.getServername()+"'");
				}
				break;
			case "sameworld":
				if(whereQuery.isEmpty())
				{
					whereQuery.append("`server_name` = '"+plugin.getServername()+"' AND `world` = '"+player.getWorld().getName()+"'");
				} else
				{
					whereQuery.append(" AND `server_name` = '"+plugin.getServername()+"' AND `world` = '"+player.getWorld().getName()+"'");
				}
				break;
			case "usehanditem":
				ItemStack mainhand = player.getInventory().getItemInMainHand();
				if(mainhand == null || mainhand.getType() == Material.AIR)
				{
					continue;
				}
				if(whereQuery.isEmpty())
				{
					whereQuery.append("`material` = '"+mainhand.getType().toString()+"'");
				} else
				{
					whereQuery.append(" AND `material` = '"+mainhand.getType().toString()+"'");
				}
				break;
			}
		}
		if(whereQuery.isEmpty())
		{
			whereQuery.append("`id` > '0'");
		}
		String where = IN_Function + whereQuery.toString();
		ArrayList<SignStorage> list = getSubscribed(sql, where, page);
		removeCooldown(player.getUniqueId());
		if(list.isEmpty())
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Cmd.Subscribed.NoSubscribesFound")));
			return;
		}
		GuiHandler.openSubscribed(list, player, page, where, true, null);
	}
	
	public static ArrayList<SignStorage> getSubscribed(String sql, String where, int page)
	{
		String orderby = " ORDER BY `id` ASC LIMIT "+(page*45)+", 44";
		VSS.getPlugin().getLogger().info("SQL: "+sql+where+orderby);
		return SignStorage.convert(VSS.getPlugin().getMysqlHandler().getSQL(MysqlType.SIGNSTORAGE, sql+where+orderby));
	}
}