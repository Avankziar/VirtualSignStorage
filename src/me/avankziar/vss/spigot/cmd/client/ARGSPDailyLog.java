package me.avankziar.vss.spigot.cmd.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.avankziar.vss.general.ChatApi;
import me.avankziar.vss.spigot.VSS;
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
import me.avankziar.vss.spigot.objects.ClientDailyLog;

public class ARGSPDailyLog extends ArgumentModule
{
	private VSS plugin;
	
	public ARGSPDailyLog(VSS plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
	}

	//sale shopping dailylog [Zahl] [Spieler]
	@Override
	public void run(CommandSender sender, String[] args) throws IOException
	{
		Player player = (Player) sender;
		int page = 0;
		UUID otherplayer = player.getUniqueId();
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
		ArrayList<ClientDailyLog> ssdll = ClientDailyLog.convert(plugin.getMysqlHandler().getList(
				MysqlHandler.Type.CLIENTDAILYLOG, "`dates` DESC", page*10, 10,
				"`player_uuid` = ?", otherplayer.toString()));
		if(ssdll.size() == 0)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Cmd.ClientDailyLog.NoLogs")));
			return;
		}
		ArrayList<String> msg = new ArrayList<>();
		msg.add(plugin.getYamlHandler().getLang().getString("Cmd.ClientDailyLog.Headline")
				.replace("%page%", String.valueOf(page))
				.replace("%player%", Utility.convertUUIDToName(otherplayer.toString())));
		for(ClientDailyLog ssdl : ssdll)
		{
			int bamo = ssdl.getBuyItemAmount();
			int samo = ssdl.getSellItemAmount();
			long date = ssdl.getDate();
			double bcost = ssdl.getBuyAmount();
			double scost = ssdl.getSellAmount();
			String s = plugin.getYamlHandler().getLang().getString("Cmd.ClientDailyLog.Log")
					.replace("%time%", TimeHandler.getDateTime(date,
							plugin.getYamlHandler().getConfig().getString("SignShop.ShopDailyLog.TimePattern")))
					.replace("%buyamo%", String.valueOf(bamo))
					.replace("%sellamo%", String.valueOf(samo))
					.replace("%buyformat%", String.valueOf(Utility.getNumberFormat(bcost, 2)))
					.replace("%sellformat%", String.valueOf(Utility.getNumberFormat(scost, 2)));
			msg.add(s);
		}
		for(String s : msg)
		{
			player.sendMessage(ChatApi.tl(s));
		}
		SaLECommandExecutor.pastNextPage(player, page, CommandExecuteType.SALE_CLIENT_DAILYLOG, pagination.toArray(new String[pagination.size()]));
	}
}