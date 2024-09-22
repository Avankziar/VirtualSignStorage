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
import me.avankziar.vss.general.objects.SignStorage;
import me.avankziar.vss.spigot.VSS;
import me.avankziar.vss.spigot.assistance.MatchApi;
import me.avankziar.vss.spigot.handler.ConfigHandler;
import me.avankziar.vss.spigot.handler.SignHandler;
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
		if(plugin.getMysqlHandler().exist(MysqlType.SIGNSTORAGE,
				"`server_name` = ? AND `world` = ? AND `x` = ? AND `y` = ? AND `z` = ?",
				plugin.getServername(), event.getBlock().getWorld().getName(),
				event.getBlock().getX(), event.getBlock().getY(), event.getBlock().getZ()))
		{
			event.setCancelled(true);
			return;
		}
		if(!event.getLine(0).equalsIgnoreCase(new ConfigHandler().getSignShopInitLine()))
		{
			return;
		}
		Player player = event.getPlayer();
		if(!plugin.getYamlHandler().getConfig().getBoolean("Enable.SignShop", false))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Mechanic.SignShopIsntEnabled")));
			return;
		}
		if(plugin.getYamlHandler().getConfig().getStringList("SignShop.ForbiddenWorld").contains(event.getBlock().getWorld().getName()))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("SignHandler.ForbiddenWorld")));
			return;
		}
		if(!ModifierValueEntry.hasPermission(player, Bypass.Permission.SHOP_CREATION))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NoPermission")));
			return;
		}
		if(VSS.getWorldGuard())
		{
			if(!WorldGuardHook.canCreateShop(player, event.getBlock().getLocation()))
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("SignChangeListener.WorldGuardCreateDeny")));
				return;
			}
		}
		int signShopAmount = plugin.getMysqlHandler().getCount(MysqlType.SIGNSTORAGE, "`player_uuid` = ?", player.getUniqueId().toString());
		int maxSignShopAmount = ModifierValueEntry.getResult(player, Bypass.Counter.SHOP_CREATION_AMOUNT_);
		if(signShopAmount > maxSignShopAmount)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("SignChangeListener.AlreadyHaveMaximalSignShop")
					.replace("%actual%", String.valueOf(signShopAmount))
					.replace("%max%", String.valueOf(maxSignShopAmount))
					));
			return;
		}
		if(event.getLine(1).equalsIgnoreCase(new ConfigHandler().getSignShopMoveLine()))
		{
			//Move SignShop
			String line2 = event.getLine(2);
			if(!MatchApi.isInteger(line2))
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NoNumber")
						.replace("%value%", line2)));
				return;
			}
			int sshID = Integer.valueOf(line2);
			SignStorage ssh = (SignStorage) plugin.getMysqlHandler().getData(MysqlType.SIGNSTORAGE, "`id` = ?", sshID);
			if(ssh == null)
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("SignChangeListener.ShopNotExists")
						.replace("%id%", line2)));
				return;
			}
			if(!ssh.getOwner().equals(player.getUniqueId()))
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NotOwner")));
				return;
			}
			ssh.setServer(plugin.getServername());
			ssh.setWorld(event.getBlock().getWorld().getName());
			ssh.setX(event.getBlock().getX());
			ssh.setY(event.getBlock().getY());
			ssh.setZ(event.getBlock().getZ());
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("SignChangeListener.ShopMoved")
					.replace("%id%", line2)
					.replace("%shopname%", ssh.getSignStorageName())));
			event.setLine(0, ChatApi.tl(SignHandler.getSignLine(0, ssh, event.getBlock())));
			event.setLine(1, ChatApi.tl(SignHandler.getSignLine(1, ssh, event.getBlock())));
			event.setLine(2, ChatApi.tl(SignHandler.getSignLine(2, ssh, event.getBlock())));
			event.setLine(3, ChatApi.tl(SignHandler.getSignLine(3, ssh, event.getBlock())));
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
			plugin.getMysqlHandler().updateData(MysqlType.SIGNSTORAGE, ssh, "`id` = ?", ssh.getId());
			return;
		} else if(event.getLine(1).equalsIgnoreCase(new ConfigHandler().getSignShopCopyLine()))
		{
			//Copy SignShop
			String line2 = event.getLine(2);
			if(!MatchApi.isInteger(line2))
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NoNumber")
						.replace("%value%", line2)));
				return;
			}
			int sshID = Integer.valueOf(line2);
			SignStorage ssh = (SignStorage) plugin.getMysqlHandler().getData(MysqlType.SIGNSTORAGE, "`id` = ?", sshID);
			if(ssh == null)
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("SignChangeListener.ShopNotExists")
						.replace("%id%", line2)));
				return;
			}
			if(!ssh.getOwner().equals(player.getUniqueId()))
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NotOwner")));
				return;
			}
			SignStorage copy = ssh;
			copy.setServer(plugin.getServername());
			copy.setWorld(event.getBlock().getWorld().getName());
			copy.setX(event.getBlock().getX());
			copy.setY(event.getBlock().getY());
			copy.setZ(event.getBlock().getZ());
			copy.setItemStorageCurrent(0);
			copy.setItemStorageTotal(new ConfigHandler().getDefaulStartItemStorage());
			copy.setSignStorageName("Copy: "+copy.getSignStorageName());
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("SignChangeListener.ShopCopy")
					.replace("%id%", line2)
					.replace("%shopname%", ssh.getSignStorageName())));
			plugin.getMysqlHandler().create(MysqlType.SIGNSTORAGE, copy);
			event.setLine(0, ChatApi.tl(SignHandler.getSignLine(0, copy, event.getBlock())));
			event.setLine(1, ChatApi.tl(SignHandler.getSignLine(1, copy, event.getBlock())));
			event.setLine(2, ChatApi.tl(SignHandler.getSignLine(2, copy, event.getBlock())));
			event.setLine(3, ChatApi.tl(SignHandler.getSignLine(3, copy, event.getBlock())));
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
			return;
		}
		int lastnumber = plugin.getMysqlHandler().lastID(MysqlType.SIGNSTORAGE)+1;
		int acid = 0;
		if(plugin.getIFHEco() != null)
		{
			Account ac = plugin.getIFHEco().getDefaultAccount(player.getUniqueId(),
					AccountCategory.MAIN, plugin.getIFHEco().getDefaultCurrency(CurrencyType.DIGITAL));
			acid = ac.getID();
		}
		ConfigHandler cfh = new ConfigHandler();
		SignStorage ssh = new SignStorage(
				0, player.getUniqueId(),
				"Storage_"+lastnumber, acid, System.currentTimeMillis(), null, null, Material.AIR,
				cfh.getDefaulStartItemStorage(), 0,
				plugin.getServername(), player.getWorld().getName(),
				event.getBlock().getX(), event.getBlock().getY(), event.getBlock().getZ(), 
				false, "", false, ListedType.MEMBER, false,
				cfh.getDefaultItemOutput(), cfh.getDefaultItemShiftOutput(),
				cfh.getDefaultItemInput(), cfh.getDefaultItemShiftInput());
		plugin.getMysqlHandler().create(MysqlType.SIGNSTORAGE, ssh);
		event.setLine(0, ChatApi.tl(SignHandler.getSignLine(0, ssh, event.getBlock())));
		event.setLine(1, ChatApi.tl(SignHandler.getSignLine(1, ssh, event.getBlock())));
		event.setLine(2, ChatApi.tl(SignHandler.getSignLine(2, ssh, event.getBlock())));
		event.setLine(3, ChatApi.tl(SignHandler.getSignLine(3, ssh, event.getBlock())));
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
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("SignChangeListener.ShopCreated")
				.replace("%name%", ssh.getSignStorageName())
				));
		return;
	}
}