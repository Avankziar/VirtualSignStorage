package me.avankziar.vss.spigot.cmd.shop;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.avankziar.ifh.general.economy.currency.CurrencyType;
import me.avankziar.ifh.spigot.economy.currency.EconomyCurrency;
import me.avankziar.vss.general.ChatApi;
import me.avankziar.vss.spigot.SaLE;
import me.avankziar.vss.spigot.assistance.MatchApi;
import me.avankziar.vss.spigot.assistance.TimeHandler;
import me.avankziar.vss.spigot.assistance.Utility;
import me.avankziar.vss.spigot.cmd.SaLECommandExecutor;
import me.avankziar.vss.spigot.cmdtree.ArgumentConstructor;
import me.avankziar.vss.spigot.cmdtree.ArgumentModule;
import me.avankziar.vss.spigot.cmdtree.CommandExecuteType;
import me.avankziar.vss.spigot.database.MysqlHandler;
import me.avankziar.vss.spigot.handler.SignHandler;
import me.avankziar.vss.spigot.modifiervalueentry.ModifierValueEntry;
import me.avankziar.vss.spigot.modifiervalueentry.Bypass.Permission;
import me.avankziar.vss.spigot.objects.ListedType;
import me.avankziar.vss.spigot.objects.SignShop;
import me.avankziar.vss.spigot.objects.SignShopLog;
import me.avankziar.vss.spigot.objects.SignShopLog.WayType;

public class ARG_SLog extends ArgumentModule
{
	private SaLE plugin;
	
	public ARG_SLog(SaLE plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
	}

	//sale shop log [Zahl] [shopid] [Spieler] [waytype = true = buy]
	@Override
	public void run(CommandSender sender, String[] args) throws IOException
	{
		Player player = (Player) sender;
		int page = 0;
		int shopid = 0;
		UUID otherplayer = player.getUniqueId();
		WayType wt = null;
		if(args.length >= 3 && MatchApi.isInteger(args[2]))
		{
			page = Integer.parseInt(args[2]);
		}
		ArrayList<String> pagination = new ArrayList<>();
		if(args.length >= 4 && MatchApi.isInteger(args[3]))
		{
			shopid = Integer.parseInt(args[3]);
			pagination.add(String.valueOf(shopid));
		}
		if(args.length >= 5)
		{
			if(args[4].equals(player.getName()))
			{
				UUID u = Utility.convertNameToUUID(args[4]);
				if(u != null)
				{
					otherplayer = u;
					pagination.add(args[4]);
				} else
				{
					pagination.add("n");
				}
			}
		}
		if(args.length >= 6 && MatchApi.isBoolean(args[5]))
		{
			boolean b = Boolean.parseBoolean(args[5]);
			wt = b ? WayType.BUY : WayType.SELL;
			pagination.add(String.valueOf(args[5]));
		}
		ArrayList<SignShopLog> ssll;
		if(shopid > 0 || !otherplayer.toString().equals(player.getUniqueId().toString()))
		{
			if(shopid > 0 && !otherplayer.toString().equals(player.getUniqueId().toString()))
			{
				SignShop ssh = (SignShop) plugin.getMysqlHandler().getData(MysqlHandler.Type.SIGNSHOP, "`id` = ?", shopid);
				if(!SignHandler.isListed(ListedType.MEMBER, ssh, player.getUniqueId()) && !SignHandler.isOwner(ssh, player.getUniqueId())
						&& !ModifierValueEntry.hasPermission(player, Permission.SHOP_LOG_OTHERPLAYER))
				{
					player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NotOwner")));
					return;
				}
			} else if(shopid > 0)
			{
				SignShop ssh = (SignShop) plugin.getMysqlHandler().getData(MysqlHandler.Type.SIGNSHOP, "`id` = ?", shopid);
				if(!SignHandler.isListed(ListedType.MEMBER, ssh, player.getUniqueId()) && !SignHandler.isOwner(ssh, player.getUniqueId())
						&& !ModifierValueEntry.hasPermission(player, Permission.SHOP_LOG_OTHERPLAYER))
				{
					player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NotOwner")));
					return;
				}
			} else if(!otherplayer.toString().equals(player.getUniqueId().toString()))
			{
				if(!ModifierValueEntry.hasPermission(player, Permission.SHOP_LOG_OTHERPLAYER))
				{
					player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NoPermission")));
					return;
				}
			}
		}
		if(wt != null && shopid == 0)
		{
			ssll = SignShopLog.convert(plugin.getMysqlHandler().getList(
					MysqlHandler.Type.SIGNSHOPLOG, "`date_time` DESC", page*10, 10,
					"`player_uuid` = ? AND `way_type` = ?", otherplayer.toString(), wt.toString()));
		} else if(wt != null && shopid > 0)
		{
			ssll = SignShopLog.convert(plugin.getMysqlHandler().getList(
					MysqlHandler.Type.SIGNSHOPLOG, "`date_time` DESC", page*10, 10,
					"`player_uuid` = ? AND `way_type` = ? AND `sign_shop_id` = ?", otherplayer.toString(), wt.toString(), shopid));
		} else if(wt == null && shopid > 0)
		{
			ssll = SignShopLog.convert(plugin.getMysqlHandler().getList(
					MysqlHandler.Type.SIGNSHOPLOG, "`date_time` DESC", page*10, 10,
					"`player_uuid` = ? AND `sign_shop_id` = ?", otherplayer.toString(), shopid));
		} else
		{
			ssll = SignShopLog.convert(plugin.getMysqlHandler().getList(
					MysqlHandler.Type.SIGNSHOPLOG, "`date_time` DESC", page*10, 10,
					"`player_uuid` = ?", otherplayer.toString()));
		}
		if(ssll.size() == 0)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Cmd.ShopLog.NoLogs")));
			return;
		}
		ArrayList<String> msg = new ArrayList<>();
		msg.add(plugin.getYamlHandler().getLang().getString("Cmd.ShopLog.Headline")
				.replace("%page%", String.valueOf(page))
				.replace("%shopid%", shopid == 0 ? "-" : String.valueOf(shopid))
				.replace("%player%", Utility.convertUUIDToName(otherplayer.toString()))
				.replace("%waytype%", wt == null ? "-" : wt.toString()));
		for(SignShopLog ssl : ssll)
		{
			String type;
			SignShop ssh = (SignShop) plugin.getMysqlHandler().getData(MysqlHandler.Type.SIGNSHOP, "`id` = ?", ssl.getSignShopId());
			String shopname = ssh != null ? ssh.getSignShopName() : String.valueOf(ssl.getSignShopId());
			int amo = ssl.getItemAmount();
			long time = ssl.getDateTime();
			double cost = ssl.getAmount();
			ItemStack is = ssl.getItemStack();
			String client = Utility.convertUUIDToName(ssl.getClient().toString());
			if(client == null)
			{
				client = "/";
			}
			if(ssl.getWayType() == WayType.BUY)
			{
				type = "Cmd.ShopLog.Buy";
			} else
			{
				type = "Cmd.ShopLog.Sell";
			}
			String s;
			if(plugin.getIFHEco() != null)
			{
				EconomyCurrency ec = ssh != null 
						? (plugin.getIFHEco().getAccount(ssh.getAccountId()) != null 
						? plugin.getIFHEco().getAccount(ssh.getAccountId()).getCurrency()
						: plugin.getIFHEco().getDefaultCurrency(CurrencyType.DIGITAL))
						: plugin.getIFHEco().getDefaultCurrency(CurrencyType.DIGITAL);
				s = plugin.getYamlHandler().getLang().getString(type)
						.replace("%player%", client)
						.replace("%time%", TimeHandler.getDateTime(time, plugin.getYamlHandler().getConfig().getString("SignShop.ShopLog.TimePattern")))
						.replace("%amount%", String.valueOf(amo))
						.replace("%item%", is.getItemMeta().hasDisplayName() 
								? is.getItemMeta().getDisplayName() 
								: (SaLE.getPlugin().getEnumTl() != null
								  ? SaLE.getPlugin().getEnumTl().getLocalization(is.getType())
								  : is.getType().toString()))
						.replace("%shop%", shopname)
						.replace("%format%", plugin.getIFHEco().format(cost, ec));
			} else
			{
				s = plugin.getYamlHandler().getLang().getString(type)
						.replace("%player%", client)
						.replace("%time%", TimeHandler.getDateTime(time, plugin.getYamlHandler().getConfig().getString("SignShop.ShopLog.TimePattern")))
						.replace("%amount%", String.valueOf(amo))
						.replace("%item%", is.getItemMeta().hasDisplayName() 
								? is.getItemMeta().getDisplayName() 
								: (SaLE.getPlugin().getEnumTl() != null
								  ? SaLE.getPlugin().getEnumTl().getLocalization(is.getType())
								  : is.getType().toString()))
						.replace("%shop%", shopname)
						.replace("%format%", String.valueOf(cost)+" "+plugin.getVaultEco().currencyNamePlural());
			}
			msg.add(s);
		}		
		for(String s : msg)
		{
			player.sendMessage(ChatApi.tl(s));
		}
		SaLECommandExecutor.pastNextPage(player, page, CommandExecuteType.SALE_SHOP_LOG, pagination.toArray(new String[pagination.size()]));
	}
}