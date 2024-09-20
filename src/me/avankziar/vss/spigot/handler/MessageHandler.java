package me.avankziar.vss.spigot.handler;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;

import me.avankziar.vss.general.ChatApi;
import me.avankziar.vss.spigot.VSS;
import me.avankziar.vss.spigot.database.MysqlHandler;
import me.avankziar.vss.spigot.objects.ListedType;
import me.avankziar.vss.spigot.objects.ShopAccessType;
import me.avankziar.vss.spigot.objects.SignShop;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

public class MessageHandler
{
	private VSS plugin;
	
	public MessageHandler()
	{
		this.plugin = VSS.getPlugin();
	}
	
	public void sendMessage(UUID uuid, String msg)
	{
		if(Bukkit.getPlayer(uuid) != null)
		{
			Bukkit.getPlayer(uuid).sendMessage(ChatApi.tl(msg));
		} else
		{
			if(plugin.getMtB() != null)
			{
				plugin.getMtB().sendMessage(uuid, msg);
			}
		}
	}
	
	public void sendMessageToOwnerAndMember(int shopid, String msg)
	{
		SignShop ssh = (SignShop) plugin.getMysqlHandler().getData(MysqlHandler.Type.SIGNSHOP, "`id` = ?", shopid);
		ArrayList<ShopAccessType> member = ShopAccessType.convert(plugin.getMysqlHandler().getFullList(MysqlHandler.Type.SHOPACCESSTYPE,
				"`id` ASC", "`sign_shop_id` = ? AND `listed_type` = ?", ssh.getId(), ListedType.MEMBER.toString()));
		sendMessage(ssh.getOwner(), msg);
		for(ShopAccessType sat : member)
		{
			sendMessage(sat.getUUID(), msg);
		}
	}
	
	public void sendMessageToOwnerAndMember(SignShop ssh, String msg)
	{
		sendMessageToOwnerAndMember(ssh.getId(), msg);
	}
	
	public void sendMessage(UUID uuid, ArrayList<ArrayList<BaseComponent>> listInList)
	{
		if(Bukkit.getPlayer(uuid) != null)
		{
			for(ArrayList<BaseComponent> list : listInList)
			{
				TextComponent tc = ChatApi.tc("");
				tc.setExtra(list);
				Bukkit.getPlayer(uuid).spigot().sendMessage(tc);
			}
			
		} else
		{
			if(plugin.getMtB() != null)
			{
				plugin.getBctB().sendMessage(uuid, listInList);
			}
		}
	}
	
	public void sendMessageToOwnerAndMember(int shopid, ArrayList<ArrayList<BaseComponent>> listInList)
	{
		SignShop ssh = (SignShop) plugin.getMysqlHandler().getData(MysqlHandler.Type.SIGNSHOP, "`id` = ?", shopid);
		ArrayList<ShopAccessType> member = ShopAccessType.convert(plugin.getMysqlHandler().getFullList(MysqlHandler.Type.SHOPACCESSTYPE,
				"`id` ASC", "`sign_shop_id` = ? AND `listed_type` = ?", ssh.getId(), ListedType.MEMBER.toString()));
		sendMessage(ssh.getOwner(), listInList);
		for(ShopAccessType sat : member)
		{
			sendMessage(sat.getUUID(), listInList);
		}
	}
	
	public void sendMessageToOwnerAndMember(SignShop ssh, ArrayList<ArrayList<BaseComponent>> listInList)
	{
		sendMessageToOwnerAndMember(ssh.getId(), listInList);
	}
}