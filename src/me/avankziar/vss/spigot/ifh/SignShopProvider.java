package me.avankziar.vss.spigot.ifh;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.avankziar.ifh.spigot.position.ServerLocation;
import me.avankziar.vss.spigot.VSS;
import me.avankziar.vss.spigot.database.MysqlHandler;
import me.avankziar.vss.spigot.handler.MessageHandler;
import me.avankziar.vss.spigot.handler.SignHandler;
import me.avankziar.vss.spigot.objects.ListedType;
import me.avankziar.vss.spigot.objects.SignShop;

public class SignShopProvider implements me.avankziar.ifh.spigot.shop.SignShop
{
	private VSS plugin;
	
	public SignShopProvider(VSS plugin)
	{
		this.plugin = plugin;
	}
	
	private SignShop getShop(int shopID)
	{
		return (SignShop) plugin.getMysqlHandler().getData(MysqlHandler.Type.SIGNSHOP, "`id` = ?", shopID);
	}
	
	public boolean isOwner(int shopID, UUID uuid)
	{
		SignShop ssh = getShop(shopID);
		return SignHandler.isOwner(ssh, uuid);
	}
	
	public boolean isMember(int shopID, UUID uuid)
	{
		SignShop ssh = getShop(shopID);
		return SignHandler.isListed(ListedType.MEMBER, ssh, uuid);
	}
	
	public boolean isBlackListed(int shopID, UUID uuid)
	{
		SignShop ssh = getShop(shopID);
		return SignHandler.isListed(ListedType.BLACKLIST, ssh, uuid);
	}
	
	public boolean isWhiteListed(int shopID, UUID uuid)
	{
		SignShop ssh = getShop(shopID);
		return SignHandler.isListed(ListedType.WHITELIST, ssh, uuid);
	}
	
	public boolean isCustomListed(int shopID, UUID uuid)
	{
		SignShop ssh = getShop(shopID);
		return SignHandler.isListed(ListedType.CUSTOM, ssh, uuid);
	}
	
	public UUID getOwner(int shopID)
	{
		SignShop ssh = getShop(shopID);
		return ssh != null ? ssh.getOwner() : null;
	}
	
	public String getShopName(int shopID)
	{
		SignShop ssh = getShop(shopID);
		return ssh != null ? ssh.getSignShopName() : null;
	}
	
	public int getAccountID(int shopID)
	{
		SignShop ssh = getShop(shopID);
		return ssh != null ? ssh.getAccountId() : 0;
	}
	
	public long getCreationDateTime(int shopID)
	{
		SignShop ssh = getShop(shopID);
		return ssh != null ? ssh.getCreationDateTime() : 0;
	}
	
	public ServerLocation getLocation(int shopID)
	{
		SignShop ssh = getShop(shopID);
		return ssh != null ? new ServerLocation(ssh.getServer(), ssh.getWorld(), ssh.getX(), ssh.getY(), ssh.getZ()) : null;
	}
	
	public Material getMaterial(int shopID)
	{
		SignShop ssh = getShop(shopID);
		return ssh != null ? ssh.getMaterial() : Material.AIR;
	}
	
	public String getDisplayName(int shopID)
	{
		SignShop ssh = getShop(shopID);
		return ssh != null ? ssh.getDisplayName() : null;
	}
	
	public ItemStack getItemStack(int shopID)
	{
		SignShop ssh = getShop(shopID);
		return ssh != null ? ssh.getItemStack() : null;
	}
	
	public boolean canBuy(int shopID)
	{
		SignShop ssh = getShop(shopID);
		return ssh != null ? ssh.canBuy() : false;
	}
	
	public Double getBuy(int shopID)
	{
		SignShop ssh = getShop(shopID);
		return ssh != null ? ssh.getBuyAmount() : null;
	}
	
	public boolean canSell(int shopID)
	{
		SignShop ssh = getShop(shopID);
		return ssh != null ? ssh.canSell() : false;
	}
	
	public Double getSell(int shopID)
	{
		SignShop ssh = getShop(shopID);
		return ssh != null ? ssh.getSellAmount() : null;
	}
	
	public long putIntoStorage(int shopID, ItemStack itemStack, long amountOfItems)
	{
		SignShop ssh = getShop(shopID);
		if(ssh == null)
		{
			return amountOfItems;
		}
		ItemStack c = itemStack.clone();
		c.setAmount(1);
		if(!ssh.getItemStack().toString().equals(c.toString()))
		{
			return amountOfItems;
		}
		long dif = ssh.getItemStorageTotal() - ssh.getItemStorageCurrent();
		long ramount = 0;
		if(dif >= amountOfItems)
		{
			ssh.setItemStorageCurrent(ssh.getItemStorageCurrent() + amountOfItems);
		} else
		{
			ramount = amountOfItems - dif;
			ssh.setItemStorageCurrent(ssh.getItemStorageTotal());
		}
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.SIGNSHOP, ssh, "`id` = ?", ssh.getId());
		SignHandler.updateSign(ssh);
		String msg = plugin.getYamlHandler().getLang().getString("SignShopProvider.PutIntoStorage")
				.replace("%shopname%", ssh.getSignShopName())
				.replace("%amount%", String.valueOf(amountOfItems));
		new MessageHandler().sendMessageToOwnerAndMember(ssh, msg);
		return ramount;
	}
	
	public ItemStack[] getOutOfStorage(int shopID, long amountOfItems)
	{
		SignShop ssh = getShop(shopID);
		if(ssh == null)
		{
			return null;
		}
		ArrayList<ItemStack> isa = new ArrayList<>();
		long isc = ssh.getItemStorageCurrent();
		long a = 0;
		int maxA = ssh.getItemStack().getMaxStackSize();
		while(a < amountOfItems)
		{
			ItemStack c = ssh.getItemStack().clone();
			if(isc - a - maxA < 0)
			{
				long dif = isc - a;
				c.setAmount((int) dif);
				isa.add(c);
				isc = 0;
				break;
			}
			if(amountOfItems >= a + maxA)
			{
				c.setAmount(maxA);
				isa.add(c);
				a += maxA;
				isc -= maxA;
			} else
			{
				long dif = amountOfItems - a;
				c.setAmount((int) dif);
				isa.add(c);
				isc -= dif;
				break;
			}
		}
		ssh.setItemStorageCurrent(isc);
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.SIGNSHOP, ssh, "`id` = ?", ssh.getId());
		SignHandler.updateSign(ssh);
		String msg = plugin.getYamlHandler().getLang().getString("SignShopProvider.GetOutOfStorage")
				.replace("%shopname%", ssh.getSignShopName())
				.replace("%amount%", String.valueOf(amountOfItems));
		new MessageHandler().sendMessageToOwnerAndMember(ssh, msg);
		return isa.toArray(new ItemStack[isa.size()]);
	}
}