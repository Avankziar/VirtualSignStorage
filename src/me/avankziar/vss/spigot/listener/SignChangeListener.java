package me.avankziar.vss.spigot.listener;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import me.avankziar.ifh.general.economy.account.AccountCategory;
import me.avankziar.ifh.general.economy.currency.CurrencyType;
import me.avankziar.ifh.spigot.economy.account.Account;
import me.avankziar.vss.general.ChatApi;
import me.avankziar.vss.general.database.MysqlType;
import me.avankziar.vss.general.objects.ListedType;
import me.avankziar.vss.general.objects.SignQStorage;
import me.avankziar.vss.general.objects.StorageAccessType.StorageType;
import me.avankziar.vss.spigot.VSS;
import me.avankziar.vss.spigot.assistance.MatchApi;
import me.avankziar.vss.spigot.handler.ConfigHandler;
import me.avankziar.vss.spigot.handler.SignHandler;
import me.avankziar.vss.spigot.handler.SignQuantityHandler;
import me.avankziar.vss.spigot.hook.WorldGuardHook;
import me.avankziar.vss.spigot.modifiervalueentry.Bypass;
import me.avankziar.vss.spigot.modifiervalueentry.ModifierValueEntry;

public class SignChangeListener implements Listener
{
	private VSS plugin;
	
	public SignChangeListener(VSS plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onSignChange(SignChangeEvent event)
	{
		if(event.isCancelled())
		{
			return;
		}
		if(plugin.getMysqlHandler().exist(MysqlType.SIGNQSTORAGE,
				"`server_name` = ? AND `world` = ? AND `x` = ? AND `y` = ? AND `z` = ?",
				plugin.getServername(), event.getBlock().getWorld().getName(),
				event.getBlock().getX(), event.getBlock().getY(), event.getBlock().getZ()))
		{
			event.setCancelled(true);
			return;
		}
		//ADDME Exist Check for SignVStorage
		StorageType storageType = null;
		if(event.getLine(0).equalsIgnoreCase(ConfigHandler.getSignQuantityStorageInitLine()))
		{
			storageType = StorageType.QUANTITY;
		} else if(event.getLine(0).equalsIgnoreCase(ConfigHandler.getSignVariousStorageInitLine()))
		{
			storageType = StorageType.VARIOUS;
		}
		if(storageType == null)
		{
			return;
		}
		Player player = event.getPlayer();
		if(ConfigHandler.getForbiddenWorld().contains(event.getBlock().getWorld().getName()))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("SignHandler.ForbiddenWorld")));
			return;
		}
		if(!ModifierValueEntry.hasPermission(player, Bypass.Permission.STORAGE_CREATION))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NoPermission")));
			return;
		}
		if(VSS.getWorldGuard())
		{
			if(!WorldGuardHook.canCreateStorage(player, event.getBlock().getLocation()))
			{
				player.sendMessage(ChatApi.tl(
						plugin.getYamlHandler().getLang().getString("SignChangeListener.WorldGuardCreateDeny")));
				return;
			}
		}
		int signShopAmount = SignHandler.getAmountOfStorage(player);
		int maxSignShopAmount = ModifierValueEntry.getResult(player, Bypass.Counter.STORAGE_CREATION_AMOUNT_);
		if(signShopAmount > maxSignShopAmount)
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("SignChangeListener.AlreadyHaveMaximalSignStorage")
					.replace("%actual%", String.valueOf(signShopAmount))
					.replace("%max%", String.valueOf(maxSignShopAmount))
					));
			return;
		}
		if(storageType == StorageType.QUANTITY)
		{
			doQuantity(player, event);
		} else
		{
			//ADDME
		}
	}
	
	private void doQuantity(Player player, SignChangeEvent event)
	{
		if(event.getLine(1) != null && event.getLine(1).equalsIgnoreCase(ConfigHandler.getSignStorageMoveLine()))
		{
			doQuantityMoveOrCopy(player, event, true);
			return;
		} else if(event.getLine(1) != null && event.getLine(1).equalsIgnoreCase(ConfigHandler.getSignStorageCopyLine()))
		{
			doQuantityMoveOrCopy(player, event, false);
			return;
		}
		int lastnumber = plugin.getMysqlHandler().lastID(MysqlType.SIGNQSTORAGE)+1;
		int acid = 0;
		if(plugin.getIFHEco() != null)
		{
			Account ac = plugin.getIFHEco().getDefaultAccount(player.getUniqueId(),
					AccountCategory.MAIN, plugin.getIFHEco().getDefaultCurrency(CurrencyType.DIGITAL));
			acid = ac.getID();
		}
		SignQStorage sst = new SignQStorage(
				0, player.getUniqueId(),
				"Storage_"+lastnumber, acid, System.currentTimeMillis(), null, null, Material.AIR,
				ConfigHandler.getDefaulStartItemStorageQuantity(), 0,
				plugin.getServername(), player.getWorld().getName(),
				event.getBlock().getX(), event.getBlock().getY(), event.getBlock().getZ(), 
				false, "", false, ListedType.MEMBER, false,
				ConfigHandler.getDefaultItemOutput(), ConfigHandler.getDefaultItemShiftOutput(),
				ConfigHandler.getDefaultItemInput(), ConfigHandler.getDefaultItemShiftInput());
		plugin.getMysqlHandler().create(MysqlType.SIGNQSTORAGE, sst);
		signQUpdate(sst, event);
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("SignChangeListener.StorageCreated")
				.replace("%name%", sst.getSignStorageName())
				));
		return;
	}
	
	private void doQuantityMoveOrCopy(Player player, SignChangeEvent event, boolean moveOrCopy)
	{
		//Move SignShop
		String line2 = event.getLine(2);
		if(!MatchApi.isInteger(line2))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NoNumber")
					.replace("%value%", line2)));
			return;
		}
		int sstID = Integer.valueOf(line2);
		SignQStorage sst = (SignQStorage) plugin.getMysqlHandler().getData(MysqlType.SIGNQSTORAGE, "`id` = ?", sstID);
		if(sst == null)
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("SignChangeListener.StorageNotExists")
					.replace("%id%", line2)));
			return;
		}
		if(!sst.getOwner().equals(player.getUniqueId()))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NotOwner")));
			return;
		}
		SignQStorage morc = sst;
		morc.setServer(plugin.getServername());
		morc.setWorld(event.getBlock().getWorld().getName());
		morc.setX(event.getBlock().getX());
		morc.setY(event.getBlock().getY());
		morc.setZ(event.getBlock().getZ());
		if(moveOrCopy)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("SignChangeListener.StorageMoved")
					.replace("%id%", line2)
					.replace("%shopname%", morc.getSignStorageName())));
		} else
		{
			morc.setItemStorageCurrent(0);
			morc.setItemStorageTotal(ConfigHandler.getDefaulStartItemStorageQuantity());
			morc.setSignStorageName("Copy: "+morc.getSignStorageName());
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("SignChangeListener.StorageCopy")
					.replace("%id%", line2)
					.replace("%shopname%", morc.getSignStorageName())));
			plugin.getMysqlHandler().create(MysqlType.SIGNQSTORAGE, morc);
		}
		signQUpdate(morc, event);
		if(moveOrCopy)
		{
			plugin.getMysqlHandler().updateData(MysqlType.SIGNQSTORAGE, morc, "`id` = ?", morc.getId());
		}
		return;
	}
	
	private void signQUpdate(final SignQStorage sst, final SignChangeEvent event)
	{
		event.setLine(0, ChatApi.tl(SignQuantityHandler.getSignLine(0, sst, event.getBlock())));
		event.setLine(1, ChatApi.tl(SignQuantityHandler.getSignLine(1, sst, event.getBlock())));
		event.setLine(2, ChatApi.tl(SignQuantityHandler.getSignLine(2, sst, event.getBlock())));
		event.setLine(3, ChatApi.tl(SignQuantityHandler.getSignLine(3, sst, event.getBlock())));
		Block b = event.getBlock();
		if(b == null)
		{
			return;
		}
		BlockState bs = b.getState();
		if(!(bs instanceof Sign))
		{
			return;
		}
		Sign sign = (Sign) bs;
		sign.setWaxed(true);
	}
}