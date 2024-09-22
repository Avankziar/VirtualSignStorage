package me.avankziar.vss.spigot.gui.listener;

import org.bukkit.Material;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;

import me.avankziar.vss.general.ChatApi;
import me.avankziar.vss.general.database.MysqlType;
import me.avankziar.vss.general.objects.SignStorage;
import me.avankziar.vss.spigot.VSS;
import me.avankziar.vss.spigot.assistance.MatchApi;
import me.avankziar.vss.spigot.gui.GUIApi;
import me.avankziar.vss.spigot.gui.events.BottomGuiClickEvent;
import me.avankziar.vss.spigot.gui.objects.GuiType;
import me.avankziar.vss.spigot.gui.objects.SettingsLevel;
import me.avankziar.vss.spigot.handler.ConfigHandler;
import me.avankziar.vss.spigot.handler.GuiHandler;
import me.avankziar.vss.spigot.handler.SignHandler;

public class BottomListener implements Listener
{
	private VSS plugin;
	
	public BottomListener(VSS plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onBottomGui(BottomGuiClickEvent event)
	{
		if(!event.getPluginName().equals(plugin.pluginname))
		{
			return;
		}
		if(event.getEvent().getCurrentItem() == null || event.getEvent().getCurrentItem().getType() == Material.AIR)
		{
			return;
		}
		final ItemStack is = event.getEvent().getCurrentItem().clone();
		is.setAmount(1);
		if(!(event.getEvent().getWhoClicked() instanceof Player))
		{
			return;
		}
		Player player = (Player) event.getEvent().getWhoClicked();
		if(!GUIApi.isInGui(player.getUniqueId()))
		{
			return;
		}
		GuiType gt = GUIApi.getGuiType(player.getUniqueId());
		if(gt == GuiType.ADMINISTRATION)
		{
			SignStorage ssh = GUIApi.getGuiSSH(player.getUniqueId());
			if(ssh == null)
			{
				return;
			}
			if(ssh.getItemStorageCurrent() >= ssh.getItemStorageTotal())
			{
				return;
			}
			if(!ssh.getItemStack().toString().equals(is.toString()))
			{
				return;
			}
			final int amount;
			if(ssh.getItemStorageCurrent() + event.getEvent().getCurrentItem().getAmount() >= ssh.getItemStorageTotal())
			{
				amount = (int)(ssh.getItemStorageTotal() - ssh.getItemStorageCurrent());
				event.getEvent().getCurrentItem().setAmount(event.getEvent().getCurrentItem().getAmount()-amount);
			} else
			{
				amount = event.getEvent().getCurrentItem().getAmount();
				event.getEvent().getCurrentItem().setAmount(0);
			}
			ssh.setItemStorageCurrent(ssh.getItemStorageCurrent()+((long) amount));
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("SignHandler.ItemsAddedToShop")
					.replace("%amount%", String.valueOf(amount))
					.replace("%now%", String.valueOf(ssh.getItemStorageCurrent())+" / "+String.valueOf(ssh.getItemStorageTotal()))));
			plugin.getMysqlHandler().updateData(MysqlType.SIGNSTORAGE, ssh, "`id` = ?", ssh.getId());
			SignHandler.updateSign(ssh);
		} else if(gt == GuiType.ITEM_INPUT)
		{
			if(event.getEvent().getView().getTitle().startsWith("Shop:"))
			{
				input(event, player, is);
				return;
			}
		}
	}
	
	private void input(BottomGuiClickEvent event, Player player, ItemStack is)
	{
		String[] split = event.getEvent().getView().getTitle().split(":");
		if(split.length != 2)
		{
			return;
		}
		String i = split[1];
		if(!MatchApi.isInteger(i))
		{
			return;
		}
		int sshID = Integer.parseInt(i);
		SignStorage ssh = (SignStorage) plugin.getMysqlHandler().getData(MysqlType.SIGNSTORAGE, "`id` = ?", sshID);
		if(ssh == null)
		{
			return;
		}
		if(ssh.getItemStack() != null)
		{
			return;
		}
		if(isShulker(is))
		{
			if(!new ConfigHandler().shopCanTradeShulker())
			{
				return;
			}
		}
		ssh.setItemStack(is);
		ssh.setMaterial(is.getType());
		ItemMeta im = is.getItemMeta();
		if(im.hasDisplayName())
		{
			ssh.setDisplayName(im.getDisplayName());
		} else
		{
			ssh.setDisplayName(VSS.getPlugin().getEnumTl() != null
					  ? VSS.getPlugin().getEnumTl().getLocalization(is.getType())
					  : is.getType().toString());
		}
		if(plugin.getYamlHandler().getConfig().getBoolean("SignShop.ShopUseMaterialAsShopName"))
		{
			ssh.setSignStorageName(VSS.getPlugin().getEnumTl() != null
					  ? VSS.getPlugin().getEnumTl().getLocalization(is.getType())
					  : is.getType().toString());
		}
		plugin.getMysqlHandler().updateData(MysqlType.SIGNSTORAGE, ssh, "`id` = ?", ssh.getId());
		SignHandler.updateSign(ssh);
		player.closeInventory();
		GuiHandler.openAdministration(ssh, player, SettingsLevel.BASE, false);
	}
	
	private boolean isShulker(ItemStack is)
	{
		if(!is.hasItemMeta())
		{
			boolean boo = false;
			switch(is.getType())
			{
			default:
				break;
			case SHULKER_BOX:
			case BLACK_SHULKER_BOX:
			case BLUE_SHULKER_BOX:
			case BROWN_SHULKER_BOX:
			case CYAN_SHULKER_BOX:
			case GRAY_SHULKER_BOX:
			case GREEN_SHULKER_BOX:
			case LIGHT_BLUE_SHULKER_BOX:
			case LIGHT_GRAY_SHULKER_BOX:
			case LIME_SHULKER_BOX:
			case MAGENTA_SHULKER_BOX:
			case ORANGE_SHULKER_BOX:
			case PINK_SHULKER_BOX:
			case PURPLE_SHULKER_BOX:
			case RED_SHULKER_BOX:
			case WHITE_SHULKER_BOX:
			case YELLOW_SHULKER_BOX:
				boo = true;
				break;
			}
			return boo;
		}
		ItemMeta im = is.getItemMeta();
		if(!(im instanceof BlockStateMeta))
		{
			return false;
		}
		BlockStateMeta bsm = (BlockStateMeta) im;
		if(!(bsm.getBlockState() instanceof ShulkerBox))
		{
			return false;
		}
		return true;
	}
}