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
import me.avankziar.vss.general.database.MysqlType;
import me.avankziar.vss.general.objects.ListedType;
import me.avankziar.vss.general.objects.PlayerData;
import me.avankziar.vss.general.objects.SignStorage;
import me.avankziar.vss.spigot.VSS;
import me.avankziar.vss.spigot.handler.ConfigHandler;
import me.avankziar.vss.spigot.handler.GuiHandler;
import me.avankziar.vss.spigot.handler.ItemHologramHandler;
import me.avankziar.vss.spigot.handler.SignHandler;

public class PlayerInteractListener implements Listener
{
	private VSS plugin;
	private static LinkedHashMap<String, Long> cooldown = new LinkedHashMap<>();
	
	public PlayerInteractListener(VSS plugin)
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
		final SignStorage ssh = (SignStorage) plugin.getMysqlHandler().getData(MysqlType.SIGNSTORAGE,
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
	
	public void dodo(Player player, SignStorage sst, Block b, BlockState bs, Action action)
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
				MysqlType.PLAYERDATA, "`player_uuid` = ?", player.getUniqueId().toString());
		if((sst.getMaterial() == Material.AIR)
				&& (SignHandler.isOwner(sst, player.getUniqueId())
				|| SignHandler.isListed(ListedType.MEMBER, sst, player.getUniqueId())
				|| SignHandler.isBypassToggle(player.getUniqueId())))
		{
			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					GuiHandler.openInputInfo(sst, player, pd.getLastSettingLevel(), true);
				}
			}.runTaskAsynchronously(plugin);
			return;
		}
		if(action == Action.LEFT_CLICK_BLOCK)
		{
			if(SignHandler.isOwner(sst, player.getUniqueId()) 
					|| SignHandler.isListed(ListedType.MEMBER, sst, player.getUniqueId()))
			{
				if(player.getInventory().getItemInMainHand() == null 
						|| player.getInventory().getItemInMainHand().getType() == Material.AIR)
				{
					SignHandler.takeOutItemFromShop(sst, player);
					return;
				}
			}
			if(sst.isItemHologram())
			{
				ItemHologramHandler.spawnHologram(sst);
			}
			return;
		} else if(action == Action.RIGHT_CLICK_BLOCK)
		{
			if(SignHandler.isOwner(sst, player.getUniqueId())
					|| SignHandler.isListed(ListedType.MEMBER, sst, player.getUniqueId())
					|| SignHandler.isBypassToggle(player.getUniqueId()))
			{
				if(sst.getItemStack() != null)
				{
					if(player.getInventory().getItemInMainHand() == null 
							|| player.getInventory().getItemInMainHand().getType() == Material.AIR)
					{
						if(sst.getItemStack() == null || sst.getItemStack().getType() == Material.AIR)
						{
							player.sendMessage(ChatApi.tl(
									plugin.getYamlHandler().getLang().getString("PlayerInteractListener.ShopItemIsNull")
									.replace("%name%", sst.getDisplayName())));
							return;
						}
						new BukkitRunnable()
						{
							@Override
							public void run()
							{
								GuiHandler.openAdministration(sst, player, ConfigHandler.isForceSettingsLevel()
										? ConfigHandler.getForcedSettingsLevel()
										: pd.getLastSettingLevel(), true);
							}
						}.runTaskAsynchronously(plugin);
						SignHandler.updateSign(sst);
						return;
					} else
					{
						if(SignHandler.putInItemIntoShop(sst, player, player.getInventory().getItemInMainHand()))
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
				SignHandler.updateSign(sst);
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
		SignStorage ssh = (SignStorage) plugin.getMysqlHandler().getData(MysqlType.SIGNSTORAGE,
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