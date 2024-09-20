package me.avankziar.vss.spigot.cmd.client;

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
import me.avankziar.vss.spigot.modifiervalueentry.ModifierValueEntry;
import me.avankziar.vss.spigot.modifiervalueentry.Bypass.Permission;
import me.avankziar.vss.spigot.objects.ClientLog;
import me.avankziar.vss.spigot.objects.SignShop;
import me.avankziar.vss.spigot.objects.ClientLog.WayType;

public class ARGSPLog extends ArgumentModule
{
	private SaLE plugin;
	
	public ARGSPLog(SaLE plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
	}

	//sale shopping log [Zahl] [Spieler] [waytype = true = buy]
	@Override
	public void run(CommandSender sender, String[] args) throws IOException
	{
		Player player = (Player) sender;
		int page = 0;
		UUID otherplayer = player.getUniqueId();
		WayType wt = null;
		if(args.length >= 3 && MatchApi.isInteger(args[2]))
		{
			page = Integer.parseInt(args[2]);
		}
		ArrayList<String> pagination = new ArrayList<>();
		if(args.length >= 4)
		{
			if(args[3].equals(player.getName()) || ModifierValueEntry.hasPermission(player, Permission.CLIENT_LOG_OTHERPLAYER))
			{
				UUID u = Utility.convertNameToUUID(args[3]);
				if(u != null)
				{
					otherplayer = u;
					pagination.add(args[3]);
				}
			}
		}
		if(args.length >= 5 && MatchApi.isBoolean(args[4]))
		{
			boolean b = Boolean.parseBoolean(args[4]);
			wt = b ? WayType.BUY : WayType.SELL;
		}
		ArrayList<ClientLog> spll;
		if(wt != null)
		{
			spll = ClientLog.convert(plugin.getMysqlHandler().getList(
					MysqlHandler.Type.CLIENTLOG, "`date_time` DESC", page*10, 10,
					"`player_uuid` = ? AND `way_type` = ?", otherplayer.toString(), wt.toString()));
		} else
		{
			spll = ClientLog.convert(plugin.getMysqlHandler().getList(
					MysqlHandler.Type.CLIENTLOG, "`date_time` DESC", page*10, 10,
					"`player_uuid` = ?", otherplayer.toString()));
		}
		if(spll.size() == 0)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Cmd.ClientLog.NoLogs")));
			return;
		}
		ArrayList<String> msg = new ArrayList<>();
		msg.add(plugin.getYamlHandler().getLang().getString("Cmd.ClientLog.Headline")
				.replace("%page%", String.valueOf(page))
				.replace("%player%", Utility.convertUUIDToName(otherplayer.toString()))
				.replace("%waytype%", wt == null ? "-" : wt.toString()));
		for(ClientLog spl : spll)
		{
			String type;
			SignShop ssh = (SignShop) plugin.getMysqlHandler().getData(MysqlHandler.Type.SIGNSHOP, "`id` = ?", spl.getSignShopId());
			String shopname = ssh != null ? ssh.getSignShopName() : String.valueOf(spl.getSignShopId());
			
			int amo = spl.getItemAmount();
			long time = spl.getDateTime();
			double cost = spl.getAmount();
			ItemStack is = spl.getItemStack();
			if(spl.getWayType() == WayType.BUY)
			{
				type = "Cmd.ClientLog.Buy";
			} else
			{
				type = "Cmd.ClientLog.Sell";
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
		SaLECommandExecutor.pastNextPage(player, page, CommandExecuteType.SALE_CLIENT_LOG, pagination.toArray(new String[pagination.size()]));
	}
}