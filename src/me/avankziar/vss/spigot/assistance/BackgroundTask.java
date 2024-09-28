package me.avankziar.vss.spigot.assistance;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

import me.avankziar.vss.general.database.MysqlType;
import me.avankziar.vss.general.objects.ItemHologram;
import me.avankziar.vss.general.objects.PlayerData;
import me.avankziar.vss.general.objects.SignQStorage;
import me.avankziar.vss.spigot.VSS;
import me.avankziar.vss.spigot.handler.ConfigHandler;
import me.avankziar.vss.spigot.handler.ItemHologramHandler;

public class BackgroundTask
{
	private static VSS plugin;
	
	public BackgroundTask(VSS plugin)
	{
		BackgroundTask.plugin = plugin;
		initBackgroundTask();
	}
	
	public boolean initBackgroundTask()
	{
		cleanUpPlayerData(plugin.getYamlHandler().getConfig().getBoolean("CleanUpTask.Player.Active", false));
		voidSignClear();
		removeShopItemHologram();
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
				int playerCount = plugin.getMysqlHandler().getCount(MysqlType.PLAYERDATA, "`last_login` < ?", offlineSinceAtLeast);
				if(playerCount <= 0)
				{
					return;
				}
				int signStorageCount = 0;
				long itemLost = 0;
				ArrayList<UUID> uuidlist = new ArrayList<>();
				ArrayList<Integer> ssIdList = new ArrayList<>();
				ArrayList<SignQStorage> ssList = new ArrayList<>();
				for(PlayerData pd : PlayerData.convert(plugin.getMysqlHandler().getFullList(MysqlType.PLAYERDATA,
						"`id` ASC",	"`last_login` < ?", offlineSinceAtLeast)))
				{
					if(pd == null)
					{
						continue;
					}
					UUID owner = pd.getUUID();
					uuidlist.add(owner);
					ArrayList<SignQStorage> list = SignQStorage.convert(plugin.getMysqlHandler().getFullList(MysqlType.SIGNQSTORAGE,
							"`id` ASC", "`player_uuid` = ?", owner.toString()));
					ssList.addAll(list);
				}
				signStorageCount = ssList.size();
				for(SignQStorage ss : ssList)
				{
					itemLost += ss.getItemStorageCurrent();
					ssIdList.add(ss.getId());
				}
				plugin.getMysqlHandler().deleteData(MysqlType.PLAYERDATA,
						"`last_login` < ?", offlineSinceAtLeast);
				if(playerCount <= 0)
				{
					return;
				}
				plugin.getLogger().info("==========VSS Database DeleteTask==========");
				plugin.getLogger().info("Deleted PlayerData: "+playerCount);
				plugin.getLogger().info("Deleted SignStorage: "+signStorageCount);
				plugin.getLogger().info("Lost ItemAmount: "+itemLost);
				plugin.getLogger().info("===========================================");
			}
		}.runTaskLaterAsynchronously(plugin, 20L*5);
	}
	
	public void voidSignClear()
	{
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				ArrayList<SignQStorage> alss = SignQStorage.convert(plugin.getMysqlHandler().getFullList(
						MysqlType.SIGNQSTORAGE, "`id` ASC", "`server_name` = ?", VSS.getPlugin().getServername()));
				new BukkitRunnable()
				{
					@Override
					public void run()
					{
						int i = 0;
						for(SignQStorage ss : alss)
						{
							Block block = null;
							try
							{
								block = Bukkit.getWorld(ss.getWorld()).getBlockAt(ss.getX(), ss.getY(), ss.getZ());
							} catch(Exception e)
							{
								VSS.log.warning("World "+ss.getWorld()+" are not to be found on server "+VSS.getPlugin().getServername()+"!");
								continue;
							}
							if(!(block.getState() instanceof org.bukkit.block.Sign))
							{
								i++;
								plugin.getMysqlHandler().deleteData(MysqlType.SIGNQSTORAGE, "`id` = ?", ss.getId());
							}
						}
						plugin.getLogger().info("==========VSS Database DeleteTask==========");
						plugin.getLogger().info("Deleted Ingame Void SignStorage: "+i);
						plugin.getLogger().info("===========================================");
					}
				}.runTask(plugin);
			}
		}.runTaskAsynchronously(plugin);
	}
	
	public void removeShopItemHologram()
	{
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				doRemoveShopItemHologram();
			}
		}.runTaskTimer(plugin, 0L, ConfigHandler.getItemHologramRunTimer()*20L);
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
}