package me.avankziar.vss.spigot.gui.listener;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import me.avankziar.vss.spigot.SaLE;
import me.avankziar.vss.spigot.database.MysqlHandler;
import me.avankziar.vss.spigot.gui.events.UpperGuiClickEvent;
import me.avankziar.vss.spigot.gui.objects.ClickFunctionType;
import me.avankziar.vss.spigot.gui.objects.ClickType;
import me.avankziar.vss.spigot.gui.objects.GuiType;
import me.avankziar.vss.spigot.handler.GuiHandler;
import me.avankziar.vss.spigot.handler.gui.AdminstrationFunctionHandler;
import me.avankziar.vss.spigot.handler.gui.SearchFunctionHandler;
import me.avankziar.vss.spigot.handler.gui.ShopFunctionHandler;
import me.avankziar.vss.spigot.handler.gui.SubscribedFunctionHandler;
import me.avankziar.vss.spigot.objects.SignShop;

public class UpperListener implements Listener
{
	private SaLE plugin;
	private long dur = 1000;
	
	public UpperListener(SaLE plugin)
	{
		this.plugin = plugin;
		dur = plugin.getYamlHandler().getConfig().getLong("SignShop.Gui.ClickCooldown", 1000L);
	}
	
	private LinkedHashMap<UUID, Long> cooldown = new LinkedHashMap<>();;
	
	public boolean isOnCooldown(UUID uuid)
	{
		Long c = cooldown.get(uuid);
		return c == null ? false : c.longValue() > System.currentTimeMillis();
	}
	
	public void setCooldown(UUID uuid, long duration, TimeUnit timeUnit)
	{
		cooldown.put(uuid, timeUnit.convert(duration, TimeUnit.MILLISECONDS)+System.currentTimeMillis());
	}
	
	public void removeCooldown(UUID uuid)
	{
		cooldown.remove(uuid);
	}
	
	@EventHandler
	public void onUpperGui(UpperGuiClickEvent event) throws IOException
	{
		if(!event.getPluginName().equals(plugin.pluginName))
		{
			return;
		}
		if(!(event.getEvent().getWhoClicked() instanceof Player))
		{
			return;
		}
		Player player = (Player) event.getEvent().getWhoClicked();
		GuiType gut = null;
		try
		{
			gut = GuiType.valueOf(event.getInventoryIdentifier());
		} catch(Exception e)
		{
			return;
		}
		if(!event.getValuesInteger().containsKey(GuiHandler.SIGNSHOP_ID))
		{
			return;
		}
		if(isOnCooldown(player.getUniqueId()))
		{
			return;
		}
		setCooldown(player.getUniqueId(), dur, TimeUnit.MILLISECONDS);
		int sshID = event.getValuesInteger().get(GuiHandler.SIGNSHOP_ID);
		SignShop ssh = (SignShop) plugin.getMysqlHandler().getData(MysqlHandler.Type.SIGNSHOP, "`id` = ?", sshID);
		UUID ou = null;
		if(event.getValuesString().containsKey(GuiHandler.PLAYER_UUID))
		{
			ou = UUID.fromString(event.getValuesString().get(GuiHandler.PLAYER_UUID));
		}
		boolean teleport_OR_location = event.getValuesString().containsKey(GuiHandler.SEARCH_TELEPORT_OR_LOCATION)
				? Boolean.valueOf(event.getValuesString().get(GuiHandler.SEARCH_TELEPORT_OR_LOCATION))
				: false;
		int page = event.getValuesInteger().containsKey(GuiHandler.PAGE)
				? event.getValuesInteger().get(GuiHandler.PAGE)
				: -1;
		String where = event.getValuesString().containsKey(GuiHandler.SEARCH_TELEPORT_OR_LOCATION)
				? event.getValuesString().get(GuiHandler.SEARCH_TELEPORT_OR_LOCATION)
				: "";
		ClickType ct = getClickFunctionType(event.getEvent().getClick(), event.getEvent().getHotbarButton());
		if(ct == null)
		{
			return;
		}
		ClickFunctionType cfct = null;
		try
		{
			cfct = ClickFunctionType.valueOf(event.getFunction(ct));
		} catch(Exception e)
		{
			return;
		}
		if(cfct == null)
		{
			return;
		}
		final GuiType gt = gut;
		final ClickFunctionType cft = cfct;
		final UUID otheruuid = ou;
		switch(gt)
		{
		case ITEM_INPUT:
			break;
		case ADMINISTRATION:
		case NUMPAD_ACCOUNT:
		case NUMPAD_BUY:
		case NUMPAD_DISCOUNT_BUY:
		case NUMPAD_DISCOUNT_END:
		case NUMPAD_DISCOUNT_POSSIBLE_BUY:
		case NUMPAD_DISCOUNT_POSSIBLE_SELL:
		case NUMPAD_DISCOUNT_HOUR:
		case NUMPAD_DISCOUNT_SELL:
		case NUMPAD_DISCOUNT_START:
		case NUMPAD_POSSIBLE_BUY:
		case NUMPAD_POSSIBLE_SELL:
		case NUMPAD_SELL:
		case KEYBOARD_BLACKLIST:
		case KEYBOARD_CUSTOM:
		case KEYBOARD_MEMBER:
		case KEYBOARD_SIGNSHOPNAME:
		case KEYBOARD_WHITELIST:
			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					AdminstrationFunctionHandler.doClickFunktion(gt, cft, player, ssh,
							event.getEvent().getClickedInventory(), event.getSettingsLevel(), otheruuid);
				}
			}.runTaskAsynchronously(plugin);
			break;
		case SHOP:
			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					ShopFunctionHandler.doClickFunktion(gt, cft, player, ssh, event.getEvent().getClickedInventory(), event.getSettingsLevel());
				}
			}.runTaskAsynchronously(plugin);
			break;
		case SEARCH_BUY:
		case SEARCH_SELL:
			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					SearchFunctionHandler.doClickFunktion(cft, player, ssh, event.getEvent().getClickedInventory(), teleport_OR_location);
				}
			}.runTaskAsynchronously(plugin);
			break;
		case SUBSCIBED:
			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					SubscribedFunctionHandler.doClickFunktion(cft, player, ssh, event.getEvent().getClickedInventory(), page, where);
				}
			}.runTaskAsynchronously(plugin);
			break;
		}
	}
	
	private ClickType getClickFunctionType(org.bukkit.event.inventory.ClickType ct, int hotbarButton)
	{
		switch(ct)
		{
		default: return null;
		case LEFT: return ClickType.LEFT;
		case RIGHT: return ClickType.RIGHT;
		case DROP: return ClickType.DROP;
		case SHIFT_LEFT: return ClickType.SHIFT_LEFT;
		case SHIFT_RIGHT: return ClickType.SHIFT_RIGHT;
		case CONTROL_DROP: return ClickType.CTRL_DROP;
		case NUMBER_KEY:
			if(hotbarButton < 0)
			{
				return null;
			}
			int i = hotbarButton+1;
			switch(i)
			{
			default: return null;
			case 1: return ClickType.NUMPAD_1;
			case 2: return ClickType.NUMPAD_2;
			case 3: return ClickType.NUMPAD_3;
			case 4: return ClickType.NUMPAD_4;
			case 5: return ClickType.NUMPAD_5;
			case 6: return ClickType.NUMPAD_6;
			case 7: return ClickType.NUMPAD_7;
			case 8: return ClickType.NUMPAD_8;
			case 9: return ClickType.NUMPAD_9;
			}
		}
	}
}