package me.avankziar.vss.spigot.handler;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;

import me.avankziar.vss.general.ChatApi;
import me.avankziar.vss.general.database.MysqlType;
import me.avankziar.vss.general.objects.ListedType;
import me.avankziar.vss.general.objects.SignStorage;
import me.avankziar.vss.general.objects.StorageAccessType;
import me.avankziar.vss.spigot.VSS;
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
		SignStorage ssh = (SignStorage) plugin.getMysqlHandler().getData(MysqlType.SIGNSTORAGE, "`id` = ?", shopid);
		ArrayList<StorageAccessType> member = StorageAccessType.convert(
				plugin.getMysqlHandler().getFullList(MysqlType.STORAGEACCESSTYPE,
				"`id` ASC", "`sign_shop_id` = ? AND `listed_type` = ?", ssh.getId(), ListedType.MEMBER.toString()));
		sendMessage(ssh.getOwner(), msg);
		for(StorageAccessType sat : member)
		{
			sendMessage(sat.getUUID(), msg);
		}
	}
	
	public void sendMessageToOwnerAndMember(SignStorage ssh, String msg)
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
		SignStorage ssh = (SignStorage) plugin.getMysqlHandler().getData(MysqlType.SIGNSTORAGE, "`id` = ?", shopid);
		ArrayList<StorageAccessType> member = StorageAccessType.convert(
				plugin.getMysqlHandler().getFullList(MysqlType.STORAGEACCESSTYPE,
				"`id` ASC", "`sign_shop_id` = ? AND `listed_type` = ?", ssh.getId(), ListedType.MEMBER.toString()));
		sendMessage(ssh.getOwner(), listInList);
		for(StorageAccessType sat : member)
		{
			sendMessage(sat.getUUID(), listInList);
		}
	}
	
	public void sendMessageToOwnerAndMember(SignStorage ssh, ArrayList<ArrayList<BaseComponent>> listInList)
	{
		sendMessageToOwnerAndMember(ssh.getId(), listInList);
	}
}