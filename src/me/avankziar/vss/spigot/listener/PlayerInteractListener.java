package me.avankziar.vss.spigot.listener;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.avankziar.vss.general.ChatApi;
import me.avankziar.vss.spigot.SaLE;
import me.avankziar.vss.spigot.database.MysqlHandler;
import me.avankziar.vss.spigot.gui.objects.SettingsLevel;
import me.avankziar.vss.spigot.handler.GuiHandler;
import me.avankziar.vss.spigot.handler.ItemHologramHandler;
import me.avankziar.vss.spigot.handler.SignHandler;
import me.avankziar.vss.spigot.objects.ListedType;
import me.avankziar.vss.spigot.objects.PlayerData;
import me.avankziar.vss.spigot.objects.SignShop;

public class PlayerInteractListener implements Listener
{
	private SaLE plugin;
	private static LinkedHashMap<String, Long> cooldown = new LinkedHashMap<>();
	
	public PlayerInteractListener(SaLE plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		if(event.getAction() != Action.LEFT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_BLOCK)
		{
			return;
		}
		Block b = event.getClickedBlock();
		if(b == null)
		{
			return;
		}
		BlockState bs = b.getState();
		if(!(bs instanceof Sign))
		{
			return;
		}
		final Player player = event.getPlayer();
		final Action action = event.getAction();
		final SignShop ssh = (SignShop) plugin.getMysqlHandler().getData(MysqlHandler.Type.SIGNSHOP,
				"`server_name` = ? AND `world` = ? AND `x` = ? AND `y` = ? AND `z` = ?",
				plugin.getServername(), player.getWorld().getName(),
				b.getX(), b.getY(), b.getZ());
		if(ssh != null)
		{
			event.setCancelled(true);
		} else
		{
			return;
		}
		dodo(player, ssh, b, bs, action);
	}
	
	public void dodo(Player player, SignShop ssh, Block b, BlockState bs, Action action)
	{		
		if(SignHandler.isBreakToggle(player.getUniqueId()))
		{
			return;
		}
		if(isOnCooldown(player))
		{
			return;
		}
		PlayerData pd = (PlayerData) plugin.getMysqlHandler().getData(
				MysqlHandler.Type.PLAYERDATA, "`player_uuid` = ?", player.getUniqueId().toString());
		if((ssh.getMaterial() == Material.AIR)
				&& (SignHandler.isOwner(ssh, player.getUniqueId())
				|| SignHandler.isListed(ListedType.MEMBER, ssh, player.getUniqueId())
				|| SignHandler.isBypassToggle(player.getUniqueId())))
		{
			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					GuiHandler.openInputInfo(ssh, player, pd.getLastSettingLevel(), true);
				}
			}.runTaskAsynchronously(plugin);
			return;
		}
		if(action == Action.LEFT_CLICK_BLOCK)
		{
			if(SignHandler.isOwner(ssh, player.getUniqueId()) || SignHandler.isListed(ListedType.MEMBER, ssh, player.getUniqueId()))
			{
				if(player.getInventory().getItemInMainHand() == null || player.getInventory().getItemInMainHand().getType() == Material.AIR)
				{
					SignHandler.takeOutItemFromShop(ssh, player);
					return;
				}
			}
			if(ssh.isItemHologram())
			{
				ItemHologramHandler.spawnHologram(ssh);
			}
			return;
		} else if(action == Action.RIGHT_CLICK_BLOCK)
		{
			if(SignHandler.isOwner(ssh, player.getUniqueId())
					|| SignHandler.isListed(ListedType.MEMBER, ssh, player.getUniqueId())
					|| SignHandler.isBypassToggle(player.getUniqueId()))
			{
				if(ssh.getItemStack() != null)
				{
					if(player.getInventory().getItemInMainHand() == null || player.getInventory().getItemInMainHand().getType() == Material.AIR)
					{
						if(ssh.getItemStack() == null || ssh.getItemStack().getType() == Material.AIR)
						{
							player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("PlayerInteractListener.ShopItemIsNull")
									.replace("%name%", ssh.getDisplayName())));
							return;
						}
						new BukkitRunnable()
						{
							@Override
							public void run()
							{
								GuiHandler.openAdministration(ssh, player,
										plugin.getYamlHandler().getConfig().getBoolean("SignShop.Gui.ForceSettingsLevel", false)
										? SettingsLevel.valueOf(plugin.getYamlHandler().getConfig().getString("SignShop.Gui.ToBeForcedSettingsLevel", "BASE"))
										: pd.getLastSettingLevel(), true);
							}
						}.runTaskAsynchronously(plugin);
						SignHandler.updateSign(ssh);
						return;
					} else
					{
						if(SignHandler.putInItemIntoShop(ssh, player, player.getInventory().getItemInMainHand()))
						{
							return;
						}
					}
				}
			}
		}
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				if((ssh.getListedType() == ListedType.BLACKLIST && SignHandler.isListed(ListedType.BLACKLIST, ssh, player.getUniqueId()))
						|| (ssh.getListedType() == ListedType.WHITELIST && !SignHandler.isListed(ListedType.WHITELIST, ssh, player.getUniqueId()))
						|| (ssh.getListedType() == ListedType.MEMBER && !SignHandler.isListed(ListedType.MEMBER, ssh, player.getUniqueId()))
						|| (ssh.getListedType() == ListedType.CUSTOM && !SignHandler.isListed(ListedType.CUSTOM, ssh, player.getUniqueId()))
					)
				{
					switch(ssh.getListedType())
					{
					case ALL:
						break;
					case BLACKLIST:
						player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("PlayerInteractListener.IsBlackList")
								.replace("%name%", ssh.getSignShopName())));
						break;
					case WHITELIST:
						player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("PlayerInteractListener.IsNotWhiteList")
								.replace("%name%", ssh.getSignShopName())));
						break;
					case CUSTOM:
						player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("PlayerInteractListener.IsNotCustomList")
								.replace("%name%", ssh.getSignShopName())));
						break;
					case MEMBER:
						player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("PlayerInteractListener.IsNotMember")
								.replace("%name%", ssh.getSignShopName())));
						break;
					}
					//event.setCancelled(true);
					return;
				}
				GuiHandler.openShop(ssh, player, pd.getLastSettingLevel(), false);
			}
		}.runTaskAsynchronously(plugin);
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				SignHandler.updateSign(ssh);
			}
		}.runTask(plugin);
	}
	
	@EventHandler
	public void OnEditBook(PlayerEditBookEvent event)
	{
		Set<Material> set = new HashSet<>();
		set.add(Material.ACACIA_SIGN);
		set.add(Material.ACACIA_WALL_SIGN);
		set.add(Material.BIRCH_SIGN);
		set.add(Material.BIRCH_WALL_SIGN);
		set.add(Material.CRIMSON_SIGN);
		set.add(Material.CRIMSON_WALL_SIGN);
		set.add(Material.DARK_OAK_SIGN);
		set.add(Material.DARK_OAK_WALL_SIGN);
		set.add(Material.JUNGLE_SIGN);
		set.add(Material.JUNGLE_WALL_SIGN);
		set.add(Material.MANGROVE_SIGN);
		set.add(Material.MANGROVE_WALL_SIGN);
		set.add(Material.OAK_SIGN);
		set.add(Material.OAK_WALL_SIGN);
		set.add(Material.SPRUCE_SIGN);
		set.add(Material.SPRUCE_WALL_SIGN);
		set.add(Material.WARPED_SIGN);
		set.add(Material.WARPED_WALL_SIGN);
		Block b = event.getPlayer().getTargetBlock(set, 4);
		if(b == null)
		{
			return;
		}
		BlockState bs = b.getState();
		if(!(bs instanceof Sign))
		{
			return;
		}
		Player player = event.getPlayer();
		SignShop ssh = (SignShop) plugin.getMysqlHandler().getData(MysqlHandler.Type.SIGNSHOP,
				"`server_name` = ? AND `world` = ? AND `x` = ? AND `y` = ? AND `z` = ?",
				plugin.getServername(), player.getWorld().getName(),
				b.getX(), b.getY(), b.getZ());
		if(ssh == null)
		{
			return;
		}
		event.setCancelled(true);
	}
	
	private boolean isOnCooldown(Player player)
	{
		if(cooldown.containsKey(player.getUniqueId().toString()))
		{
			long c = cooldown.get(player.getUniqueId().toString());
			if(c > System.currentTimeMillis())
			{
				return true;
			}
		}
		addCooldown(player);
		return false;
	}
	
	private void addCooldown(Player player)
	{
		cooldown.put(player.getUniqueId().toString(), System.currentTimeMillis()+1500L);
	}
}