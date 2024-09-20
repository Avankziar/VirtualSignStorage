package me.avankziar.vss.spigot.listener;

import java.util.LinkedHashMap;
import java.util.UUID;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import me.avankziar.vss.spigot.VSS;
import me.avankziar.vss.spigot.event.ShopPostTransactionEvent;
import me.avankziar.vss.spigot.handler.Base64Handler;
import me.avankziar.vss.spigot.objects.ShopLogVar;
import me.avankziar.vss.spigot.objects.SignShop;

public class ShopPostTransactionListener implements Listener
{
	public static LinkedHashMap<UUID, LinkedHashMap<UUID, LinkedHashMap<String, ShopLogVar>>> maping = new LinkedHashMap<>(); //shopowner, client, is as Base64, iamount, 5 min Timer
	public static LinkedHashMap<UUID, LinkedHashMap<UUID, LinkedHashMap<String, ShopLogVar>>> maping2 = new LinkedHashMap<>(); //shopowner, client, is as Base64, iamount, 15 min timer
	
	@EventHandler
	public void onShopPostTransaction(ShopPostTransactionEvent event)
	{
		final SignShop ssh = event.getSignShop();
		final UUID client = event.getClient().getUniqueId();
		final long iamount = event.getItemAmount();
		final double costPerItem = event.getCostPerItem();
		doMap(maping, ssh, client, iamount, costPerItem, event.isBuying());
		doMap(maping2, ssh, client, iamount, costPerItem, event.isBuying());
	}
	
	private void doMap(LinkedHashMap<UUID, LinkedHashMap<UUID, LinkedHashMap<String, ShopLogVar>>> base,
			SignShop ssh, UUID client, long iamount, double costPerItem, boolean isBuy)
	{
		UUID owner = ssh.getOwner();
		ItemStack is = ssh.getItemStack();
		LinkedHashMap<UUID, LinkedHashMap<String, ShopLogVar>> sub = base.containsKey(owner) ? base.get(owner) : new LinkedHashMap<>();
		LinkedHashMap<String, ShopLogVar> sub2 = sub.containsKey(client) ? sub.get(client) : new LinkedHashMap<>();
		String b64 = new Base64Handler(is).toBase64();
		ShopLogVar slv = null;
		if(isBuy)
		{
			if(VSS.getPlugin().getIFHEco() != null)
			{
				slv = (sub2.containsKey(b64) 
						? sub2.get(b64).addBuy(iamount, costPerItem) 
								: new ShopLogVar(ssh.getId(), ssh.getSignShopName(),
										VSS.getPlugin().getIFHEco().getAccount(ssh.getAccountId()).getCurrency().getUniqueName(), 
										iamount, costPerItem, 0, 0));
			} else
			{
				slv = (sub2.containsKey(b64) 
						? sub2.get(b64).addBuy(iamount, costPerItem) 
								: new ShopLogVar(ssh.getId(), ssh.getSignShopName(),
										VSS.getPlugin().getVaultEco().currencyNamePlural(), 
										iamount, costPerItem, 0, 0));
			}
		} else 
		{
			if(VSS.getPlugin().getIFHEco() != null)
			{
				slv = (sub2.containsKey(b64) 
						? sub2.get(b64).addSell(iamount, costPerItem) 
								: new ShopLogVar(ssh.getId(), ssh.getSignShopName(),
										VSS.getPlugin().getIFHEco().getAccount(ssh.getAccountId()).getCurrency().getUniqueName(), 
										0, 0, iamount, costPerItem));
			} else
			{
				slv = (sub2.containsKey(b64) 
						? sub2.get(b64).addSell(iamount, costPerItem) 
								: new ShopLogVar(ssh.getId(), ssh.getSignShopName(),
										VSS.getPlugin().getVaultEco().currencyNamePlural(), 
										0, 0, iamount, costPerItem));
			}
		}
		sub2.put(b64, slv);
		sub.put(client, sub2);
		base.put(owner, sub);
	}
}