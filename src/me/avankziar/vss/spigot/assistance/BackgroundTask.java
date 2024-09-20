package me.avankziar.vss.spigot.assistance;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.avankziar.vss.general.ChatApi;
import me.avankziar.vss.spigot.SaLE;
import me.avankziar.vss.spigot.database.MysqlHandler;
import me.avankziar.vss.spigot.database.MysqlHandler.Type;
import me.avankziar.vss.spigot.handler.Base64Handler;
import me.avankziar.vss.spigot.handler.ItemHologramHandler;
import me.avankziar.vss.spigot.listener.ShopPostTransactionListener;
import me.avankziar.vss.spigot.objects.ItemHologram;
import me.avankziar.vss.spigot.objects.PlayerData;
import me.avankziar.vss.spigot.objects.ShopLogVar;
import me.avankziar.vss.spigot.objects.SignShop;
import me.avankziar.vss.spigot.objects.SignShopDailyLog;
import me.avankziar.vss.spigot.objects.SignShopLog;
import me.avankziar.vss.spigot.objects.SignShopLog.WayType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class BackgroundTask
{
	private static SaLE plugin;
	
	public BackgroundTask(SaLE plugin)
	{
		BackgroundTask.plugin = plugin;
		initBackgroundTask();
	}
	
	public boolean initBackgroundTask()
	{
		cleanUpPlayerData(plugin.getYamlHandler().getConfig().getBoolean("CleanUpTask.Player.Active", false));
		cleanUpSignShopLog(plugin.getYamlHandler().getConfig().getBoolean("CleanUpTask.ShopLog.Active", false));
		cleanUpSignShopDailyLog(plugin.getYamlHandler().getConfig().getBoolean("CleanUpTask.ShopDailyLog.Active", false));
		cleanUpClientLog(plugin.getYamlHandler().getConfig().getBoolean("CleanUpTask.ClientLog.Active", false));
		cleanUpClientDailyLog(plugin.getYamlHandler().getConfig().getBoolean("CleanUpTask.ClientgDailyLog.Active", false));
		voidSignClear();
		removeShopItemHologram();
		msgTransactionMessageToShopOwnerTimer();
		transactionShopLogTimer();
		return true;
	}
	
	public void cleanUpPlayerData(boolean active)
	{
		if(!active)
		{
			return;
		}
		final long offlineSinceAtLeast = System.currentTimeMillis()
				-1000L*60*60*24*plugin.getYamlHandler().getConfig().getInt("CleanUpTask.Player.DeleteAfterXDaysOffline");
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				int playerCount = plugin.getMysqlHandler().getCount(MysqlHandler.Type.PLAYERDATA, "`last_login` < ?", offlineSinceAtLeast);
				if(playerCount <= 0)
				{
					return;
				}
				int signShopCount = 0;
				long itemLost = 0;
				int ClientLogCount = 0;
				int ClientDailyLogCount = 0;
				int signShopLogCount = 0;
				int signShopDailyLogCount = 0;
				int subscribedShop = 0;
				ArrayList<UUID> uuidlist = new ArrayList<>();
				ArrayList<Integer> ssIdList = new ArrayList<>();
				ArrayList<SignShop> ssList = new ArrayList<>();
				for(PlayerData pd : PlayerData.convert(plugin.getMysqlHandler().getFullList(MysqlHandler.Type.PLAYERDATA,
						"`id` ASC",	"`last_login` < ?", offlineSinceAtLeast)))
				{
					if(pd == null)
					{
						continue;
					}
					UUID owner = pd.getUUID();
					uuidlist.add(owner);
					ArrayList<SignShop> list = SignShop.convert(plugin.getMysqlHandler().getFullList(MysqlHandler.Type.SIGNSHOP,
							"`id` ASC", "`player_uuid` = ?", owner.toString()));
					ssList.addAll(list);
				}
				signShopCount = ssList.size();
				for(SignShop ss : ssList)
				{
					itemLost += ss.getItemStorageCurrent();
					ssIdList.add(ss.getId());
				}
				plugin.getMysqlHandler().deleteData(MysqlHandler.Type.PLAYERDATA,
						"`last_login` < ?", offlineSinceAtLeast);
				for(UUID uuid : uuidlist)
				{
					plugin.getMysqlHandler().deleteData(MysqlHandler.Type.SIGNSHOP,
							"`player_uuid` = ?", uuid.toString());
					subscribedShop += plugin.getMysqlHandler().deleteData(MysqlHandler.Type.SUBSCRIBEDSHOP,
							"`player_uuid` = ?", uuid.toString());
					ClientLogCount += plugin.getMysqlHandler().deleteData(MysqlHandler.Type.CLIENTLOG,
							"`player_uuid` = ?", uuid.toString());
					ClientDailyLogCount += plugin.getMysqlHandler().deleteData(MysqlHandler.Type.CLIENTDAILYLOG,
							"`player_uuid` = ?", uuid.toString());
				}
				for(int ssid : ssIdList)
				{
					subscribedShop += plugin.getMysqlHandler().deleteData(MysqlHandler.Type.SUBSCRIBEDSHOP,
							"`sign_shop_id` = ?", ssid);
					signShopLogCount += plugin.getMysqlHandler().deleteData(MysqlHandler.Type.SIGNSHOPLOG,
							"`sign_shop_id` = ?", ssid);
					signShopDailyLogCount += plugin.getMysqlHandler().deleteData(MysqlHandler.Type.SIGNSHOPDAILYLOG,
							"`sign_shop_id` = ?", ssid);
					plugin.getMysqlHandler().deleteData(MysqlHandler.Type.SIGNSHOP,
							"`id` = ?", ssid);
				}
				if(playerCount <= 0)
				{
					return;
				}
				plugin.getLogger().info("==========SaLE Database DeleteTask==========");
				plugin.getLogger().info("Deleted PlayerData: "+playerCount);
				plugin.getLogger().info("Deleted ClientLog: "+ClientLogCount);
				plugin.getLogger().info("Deleted ClientDailyLog: "+ClientDailyLogCount);
				plugin.getLogger().info("Deleted SubscribedStore: "+subscribedShop);
				plugin.getLogger().info("Deleted SignShop: "+signShopCount);
				plugin.getLogger().info("Lost ItemAmount: "+itemLost);
				plugin.getLogger().info("Deleted SignShopLog: "+signShopLogCount);
				plugin.getLogger().info("Deleted SignShopDailyLog: "+signShopDailyLogCount);
				plugin.getLogger().info("===========================================");
			}
		}.runTaskLaterAsynchronously(plugin, 20L*5);
	}
	
	public void cleanUpSignShopLog(boolean active)
	{
		if(!active)
		{
			return;
		}
		final long olderThanAtLeast = System.currentTimeMillis()
				-1000L*60*60*24*plugin.getYamlHandler().getConfig().getInt("CleanUpTask.ShopLog.DeleteAfterXDays", 365);
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				int signShopLogCount = plugin.getMysqlHandler().getCount(MysqlHandler.Type.SIGNSHOPLOG,
						"`date_time` < ?", olderThanAtLeast);
				if(signShopLogCount <= 0)
				{
					return;
				}
				plugin.getMysqlHandler().deleteData(MysqlHandler.Type.SIGNSHOPLOG,
						"`date_time` < ?", olderThanAtLeast);
				plugin.getLogger().info("==========SaLE Database DeleteTask==========");
				plugin.getLogger().info("Deleted SignShopLog: "+signShopLogCount);
				plugin.getLogger().info("===========================================");
			}
		}.runTaskLaterAsynchronously(plugin, 20L*6);
	}
	
	public void cleanUpSignShopDailyLog(boolean active)
	{
		if(!active)
		{
			return;
		}
		final long olderThanAtLeast = System.currentTimeMillis()
				-1000L*60*60*24*plugin.getYamlHandler().getConfig().getInt("CleanUpTask.ShopDailyLog.DeleteAfterXDays");
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				int signShopDailyLogCount = plugin.getMysqlHandler().getCount(MysqlHandler.Type.SIGNSHOPDAILYLOG,
						"`dates` < ?", olderThanAtLeast);
				if(signShopDailyLogCount <= 0)
				{
					return;
				}
				plugin.getMysqlHandler().deleteData(MysqlHandler.Type.SIGNSHOPDAILYLOG,
						"`dates` < ?", olderThanAtLeast);
				plugin.getLogger().info("==========SaLE Database DeleteTask==========");
				plugin.getLogger().info("Deleted SignShopDailyLog: "+signShopDailyLogCount);
				plugin.getLogger().info("===========================================");
			}
		}.runTaskLaterAsynchronously(plugin, 20L*7);
	}
	
	public void cleanUpClientLog(boolean active)
	{
		if(!active)
		{
			return;
		}
		final long olderThanAtLeast = System.currentTimeMillis()
				-1000L*60*60*24*plugin.getYamlHandler().getConfig().getInt("CleanUpTask.ClientLog.DeleteAfterXDays");
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				int ClientLogCount = plugin.getMysqlHandler().getCount(MysqlHandler.Type.CLIENTLOG,
						"`date_time` < ?", olderThanAtLeast);
				if(ClientLogCount <= 0)
				{
					return;
				}
				plugin.getMysqlHandler().deleteData(MysqlHandler.Type.CLIENTLOG,
						"`date_time` < ?", olderThanAtLeast);
				plugin.getLogger().info("==========SaLE Database DeleteTask==========");
				plugin.getLogger().info("Deleted ClientLog: "+ClientLogCount);
				plugin.getLogger().info("===========================================");
			}
		}.runTaskLaterAsynchronously(plugin, 20L*8);
	}
	
	public void cleanUpClientDailyLog(boolean active)
	{
		if(!active)
		{
			return;
		}
		final long olderThanAtLeast = System.currentTimeMillis()
				-1000L*60*60*24*plugin.getYamlHandler().getConfig().getInt("CleanUpTask.ClientDailyLog.DeleteAfterXDays");
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				int ClientDailyLogCount = plugin.getMysqlHandler().getCount(MysqlHandler.Type.CLIENTDAILYLOG,
						"`dates` < ?", olderThanAtLeast);
				if(ClientDailyLogCount <= 0)
				{
					return;
				}
				plugin.getMysqlHandler().deleteData(MysqlHandler.Type.CLIENTDAILYLOG,
						"`dates` < ?", olderThanAtLeast);
				plugin.getLogger().info("==========SaLE Database DeleteTask==========");
				plugin.getLogger().info("Deleted ClientDailyLog: "+ClientDailyLogCount);
				plugin.getLogger().info("===========================================");
			}
		}.runTaskLaterAsynchronously(plugin, 20L*9);
	}
	
	public void voidSignClear()
	{
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				ArrayList<SignShop> alss = SignShop.convert(plugin.getMysqlHandler().getFullList(
						Type.SIGNSHOP, "`id` ASC", "`server_name` = ?", SaLE.getPlugin().getServername()));
				new BukkitRunnable()
				{
					@Override
					public void run()
					{
						int i = 0;
						for(SignShop ss : alss)
						{
							Block block = null;
							try
							{
								block = Bukkit.getWorld(ss.getWorld()).getBlockAt(ss.getX(), ss.getY(), ss.getZ());
							} catch(Exception e)
							{
								SaLE.log.warning("World "+ss.getWorld()+" are not to be found on server "+SaLE.getPlugin().getServername()+"!");
								continue;
							}
							if(!(block.getState() instanceof org.bukkit.block.Sign))
							{
								i++;
								plugin.getMysqlHandler().deleteData(MysqlHandler.Type.SIGNSHOP, "`id` = ?", ss.getId());
							}
						}
						plugin.getLogger().info("==========SaLE Database DeleteTask==========");
						plugin.getLogger().info("Deleted Ingame Void SignShops: "+i);
						plugin.getLogger().info("===========================================");
					}
				}.runTask(plugin);
			}
		}.runTaskAsynchronously(plugin);
	}
	
	public void removeShopItemHologram()
	{
		long runEveryXSeconds = plugin.getYamlHandler().getConfig().getInt("SignShop.ItemHologram.RunTimerInSeconds", 5);
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				doRemoveShopItemHologram();
			}
		}.runTaskTimer(plugin, 0L, runEveryXSeconds*20L);
	}
	
	public void doRemoveShopItemHologram()
	{
		long now = System.currentTimeMillis();
		ArrayList<String> toDelete = new ArrayList<>();
		for(Entry<String, ItemHologram> e : ItemHologramHandler.taskMap.entrySet())
		{
			if(Long.parseLong(e.getKey()) < now)
			{
				e.getValue().despawn();
				toDelete.add(e.getKey());
			}
		}
		for(String l : toDelete)
		{
			ItemHologramHandler.taskMap.remove(l);
		}
	}
	
	public void msgTransactionMessageToShopOwnerTimer()
	{
		long runEveryXMin = plugin.getYamlHandler().getConfig().getInt("SignShop.TransactionSummary.MessageToShopOwner.RunTimerInMinutes", 5)*60;
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				ArrayList<UUID> del = new ArrayList<>();
				for(UUID shopOwner : ShopPostTransactionListener.maping.keySet())
				{
					LinkedHashMap<UUID, LinkedHashMap<String, ShopLogVar>> subB = ShopPostTransactionListener.maping.get(shopOwner);
					LinkedHashMap<UUID, List<String>> lhm = new LinkedHashMap<>(); //client, hover
					for(UUID client : subB.keySet())
					{
						LinkedHashMap<String, ShopLogVar> sub2 = subB.get(client);
						List<String> hov = new ArrayList<>();
						for(Entry<String, ShopLogVar> var : sub2.entrySet())
						{
							ItemStack is = new Base64Handler(var.getKey()).fromBase64();
							String shopname = var.getValue().shopname;
							String currency = var.getValue().currency;
							long bsamo = var.getValue().itemAmountBuy;
							double bcostTotal = var.getValue().costTotalBuy;
							long ssamo = var.getValue().itemAmountSell;
							double scostTotal = var.getValue().costTotalSell;
							if(bsamo > 0)
							{
								if(plugin.getIFHEco() != null)
								{
									hov.add(plugin.getYamlHandler().getLang().getString("ShopLog.MsgTimer.Buy")
											.replace("%amount%", String.valueOf(bsamo))
											.replace("%item%", is.getItemMeta().hasDisplayName() 
													? is.getItemMeta().getDisplayName() 
													: (SaLE.getPlugin().getEnumTl() != null
													  ? SaLE.getPlugin().getEnumTl().getLocalization(is.getType())
													  : is.getType().toString()))
											.replace("%shop%", shopname)
											.replace("%format%", plugin.getIFHEco().format(bcostTotal,
													SaLE.getPlugin().getIFHEco().getCurrency(currency))));
								} else
								{
									hov.add(plugin.getYamlHandler().getLang().getString("ShopLog.MsgTimer.Buy")
											.replace("%amount%", String.valueOf(bsamo))
											.replace("%item%", is.getItemMeta().hasDisplayName() 
													? is.getItemMeta().getDisplayName() 
													: (SaLE.getPlugin().getEnumTl() != null
												      ? SaLE.getPlugin().getEnumTl().getLocalization(is.getType())
													  : is.getType().toString()))
											.replace("%shop%", shopname)
											.replace("%format%", String.valueOf(bcostTotal)+" "+plugin.getVaultEco().currencyNamePlural()));
								}
							}
							if(ssamo > 0)
							{
								if(plugin.getIFHEco() != null)
								{
									hov.add(plugin.getYamlHandler().getLang().getString("ShopLog.MsgTimer.Sell")
											.replace("%amount%", String.valueOf(ssamo))
											.replace("%item%", is.getItemMeta().hasDisplayName() 
													? is.getItemMeta().getDisplayName() 
													: (SaLE.getPlugin().getEnumTl() != null
													  ? SaLE.getPlugin().getEnumTl().getLocalization(is.getType())
													  : is.getType().toString()))
											.replace("%shop%", shopname)
											.replace("%format%", plugin.getIFHEco().format(scostTotal,
													SaLE.getPlugin().getIFHEco().getCurrency(currency))));
								} else
								{
									hov.add(plugin.getYamlHandler().getLang().getString("ShopLog.MsgTimer.Sell")
											.replace("%amount%", String.valueOf(ssamo))
											.replace("%item%", is.getItemMeta().hasDisplayName() 
													? is.getItemMeta().getDisplayName() 
													: (SaLE.getPlugin().getEnumTl() != null
													  ? SaLE.getPlugin().getEnumTl().getLocalization(is.getType())
													  : is.getType().toString()))
											.replace("%shop%", shopname)
											.replace("%format%", String.valueOf(scostTotal)+" "+plugin.getVaultEco().currencyNamePlural()));
								}
							}
						}
						if(lhm.containsKey(client))
						{
							List<String> ls = lhm.get(client);
							ls.addAll(hov);
							lhm.put(client, ls);
						} else
						{
							lhm.put(client, hov);
						}
					}
					for(Entry<UUID, List<String>> li : lhm.entrySet())
					{
						StringBuilder sb = new StringBuilder();
						for(int i = 0; i < li.getValue().size(); i++)
						{
							sb.append(li.getValue().get(i));
							if(i+1 < li.getValue().size())
							{
								sb.append("~!~");
							}
						}
						String pn = Utility.convertUUIDToName(li.getKey().toString());
						TextComponent tc = ChatApi.hoverEvent(
								plugin.getYamlHandler().getLang().getString("ShopLog.MsgTimer.Msg").replace("%player%", pn),
								HoverEvent.Action.SHOW_TEXT, sb.toString());
						
						if(Bukkit.getPlayer(shopOwner) != null)
						{
							Bukkit.getPlayer(shopOwner).spigot().sendMessage(tc);
						} else
						{
							ArrayList<BaseComponent> list = new ArrayList<>();
							list.add(tc);
							ArrayList<ArrayList<BaseComponent>> listInList = new ArrayList<>();
							listInList.add(list);
							SaLE.getPlugin().getBctB().sendMessage(shopOwner, listInList);
						}
					}
					del.add(shopOwner);
				}
				for(UUID uuid : del)
				{
					ShopPostTransactionListener.maping.remove(uuid);
				}
			}
		}.runTaskTimerAsynchronously(plugin, 0L, runEveryXMin*20L);
	}
	
	public void transactionShopLogTimer()
	{
		long runEveryXMin = plugin.getYamlHandler().getConfig().getInt("SignShop.TransactionSummary.ShopLog.RunTimerInMinutes", 15)*60;
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				doShopLog();
			}
		}.runTaskTimerAsynchronously(plugin, 0L, runEveryXMin*20L);
	}
	
	public void doShopLog()
	{
		ArrayList<UUID> del = new ArrayList<>();
		for(UUID shopOwner : ShopPostTransactionListener.maping2.keySet())
		{
			LinkedHashMap<UUID, LinkedHashMap<String, ShopLogVar>> subB = ShopPostTransactionListener.maping2.get(shopOwner);
			for(UUID client : subB.keySet())
			{
				LinkedHashMap<String, ShopLogVar> sub2 = subB.get(client);
				for(Entry<String, ShopLogVar> var : sub2.entrySet())
				{
					ItemStack is = new Base64Handler(var.getKey()).fromBase64();
					int shopID = var.getValue().shopID;
					long bsamo = var.getValue().itemAmountBuy;
					double bcostTotal = var.getValue().costTotalBuy;
					long ssamo = var.getValue().itemAmountSell;
					double scostTotal = var.getValue().costTotalSell;
					long date = TimeHandler.getDate(TimeHandler.getDate(System.currentTimeMillis()));
					if(bsamo > 0)
					{
						SignShopLog ssl = new SignShopLog(0, shopID, System.currentTimeMillis(),
								is, is.getItemMeta().hasDisplayName() 
								? is.getItemMeta().getDisplayName() 
								: (SaLE.getPlugin().getEnumTl() != null
								  ? SaLE.getPlugin().getEnumTl().getLocalization(is.getType())
								  : is.getType().toString()),
							is.getType(), WayType.BUY, bcostTotal, (int) bsamo,
							client, shopOwner);
						plugin.getMysqlHandler().create(MysqlHandler.Type.SIGNSHOPLOG, ssl);
					}
					if(ssamo > 0)
					{
						SignShopLog ssl = new SignShopLog(0, shopID, System.currentTimeMillis(),
								is, is.getItemMeta().hasDisplayName() 
								? is.getItemMeta().getDisplayName() 
								: (SaLE.getPlugin().getEnumTl() != null
								  ? SaLE.getPlugin().getEnumTl().getLocalization(is.getType())
								  : is.getType().toString()),
							is.getType(), WayType.SELL, scostTotal, (int) ssamo,
							client, shopOwner);
						plugin.getMysqlHandler().create(MysqlHandler.Type.SIGNSHOPLOG, ssl);
					}
					SignShopDailyLog ssdl = (SignShopDailyLog) plugin.getMysqlHandler().getData(MysqlHandler.Type.SIGNSHOPDAILYLOG,
							"`sign_shop_id` = ? AND `dates` = ?", shopID, date);
					if(ssdl == null)
					{
						ssdl = new SignShopDailyLog(0, shopID, date, bcostTotal, scostTotal, (int) bsamo, (int) ssamo, shopOwner);
						plugin.getMysqlHandler().create(MysqlHandler.Type.SIGNSHOPDAILYLOG, ssdl);
					} else
					{
						ssdl.setBuyItemAmount(ssdl.getBuyItemAmount()+(int)bsamo);
						ssdl.setBuyAmount(ssdl.getBuyAmount()+bcostTotal);
						ssdl.setSellItemAmount(ssdl.getSellItemAmount()+(int)ssamo);
						ssdl.setSellAmount(ssdl.getSellAmount()+scostTotal);
						plugin.getMysqlHandler().updateData(MysqlHandler.Type.SIGNSHOPDAILYLOG, ssdl, 
								"`sign_shop_id` = ? AND `dates` = ?", shopID,
								date);
					}
				}
			}
			del.add(shopOwner);
		}
		for(UUID uuid : del)
		{
			ShopPostTransactionListener.maping2.remove(uuid);
		}
	}
}