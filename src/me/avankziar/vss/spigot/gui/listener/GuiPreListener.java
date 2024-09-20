package me.avankziar.vss.spigot.gui.listener;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import me.avankziar.vss.spigot.VSS;
import me.avankziar.vss.spigot.gui.GUIApi;
import me.avankziar.vss.spigot.gui.events.BottomGuiClickEvent;
import me.avankziar.vss.spigot.gui.events.UpperGuiClickEvent;
import me.avankziar.vss.spigot.gui.objects.ClickType;
import me.avankziar.vss.spigot.gui.objects.SettingsLevel;

public class GuiPreListener implements Listener
{
	private JavaPlugin plugin;
	
	public GuiPreListener(JavaPlugin plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onClickListener(final InventoryClickEvent event)
	{
		if(event.isCancelled())
		{
			return;
		}
		if(event.getResult() == Result.DENY)
		{
			return;
		}
		if(event.getClickedInventory() == null)
		{
			return;
		}
		if(event.getClickedInventory().getType() == InventoryType.CHEST)
		{
			getUpperGuiEvent(plugin, event);
		} else if(event.getClickedInventory().getType() == InventoryType.PLAYER)
		{
			getBottomGuiEvent(event);
		}
		return;
	}
	
	@EventHandler
	public void onGuiClose(InventoryCloseEvent event)
	{
		UUID uuid = event.getPlayer().getUniqueId();
		if(GUIApi.isInGui(uuid))
		{
			GUIApi.removeInGui(uuid);
		}
	}
	
	private void getBottomGuiEvent(InventoryClickEvent event)
	{
		UUID uuid = event.getWhoClicked().getUniqueId();
		if(!GUIApi.isInGui(uuid))
		{
			return;
		}
		event.setCancelled(true);
		event.setResult(Result.DENY);
		BottomGuiClickEvent gce = new BottomGuiClickEvent(
				event, 
				VSS.getPlugin().pluginName,
				GUIApi.getGui(uuid));
		Bukkit.getPluginManager().callEvent(gce);
	}
	
	private void getUpperGuiEvent(JavaPlugin plugin, InventoryClickEvent event)
	{
		if(event.getCurrentItem() == null)
		{
			return;
		}
		ItemStack i = event.getCurrentItem().clone();
		if(!i.hasItemMeta())
		{
			return;
		}
		NamespacedKey npluginName = new NamespacedKey(plugin, GUIApi.PLUGINNAME);
		NamespacedKey ninventoryIdentifier = new NamespacedKey(plugin, GUIApi.INVENTORYIDENTIFIER);
		NamespacedKey nclickEventCancel = new NamespacedKey(plugin, GUIApi.CLICKEVENTCANCEL);
		NamespacedKey nsettingslevel = new NamespacedKey(plugin, GUIApi.SETTINGLEVEL);
		PersistentDataContainer pdc = i.getItemMeta().getPersistentDataContainer();
		if(!pdc.has(npluginName, PersistentDataType.STRING)
				|| !pdc.has(ninventoryIdentifier, PersistentDataType.STRING)
				|| !pdc.has(nclickEventCancel, PersistentDataType.STRING)
				|| !pdc.has(nsettingslevel, PersistentDataType.STRING))
		{
			return;
		}
		if(event.getClick() == org.bukkit.event.inventory.ClickType.SWAP_OFFHAND)
		{
			event.setCancelled(true);
			event.setResult(Result.DENY);
		}
		boolean clickEventCancel = Boolean.parseBoolean(pdc.get(nclickEventCancel, PersistentDataType.STRING));
		if(clickEventCancel)
		{
			event.setCancelled(true);
			event.setResult(Result.DENY);
		}
		HashMap<ClickType, String> functionMap = getFunctionMap(plugin, pdc);
		UpperGuiClickEvent gce = new UpperGuiClickEvent(
				event, 
				pdc.get(npluginName, PersistentDataType.STRING),
				pdc.get(ninventoryIdentifier, PersistentDataType.STRING), 
				SettingsLevel.valueOf(pdc.get(nsettingslevel, PersistentDataType.STRING)),
				functionMap);
		for(NamespacedKey key : pdc.getKeys())
		{
			if(!key.getKey().contains("---"))
			{
				continue;
			}
			String[] split = key.getKey().split("---");
			String purekey = split[0];
			GUIApi.Type type = GUIApi.Type.valueOf(split[1].toUpperCase());
			switch(type)
			{
			case BYTE:
				if(pdc.has(key, PersistentDataType.BYTE))
				{
					gce.getValuesByte().put(purekey, pdc.get(key, PersistentDataType.BYTE));
				}
				continue;
			case BYTE_ARRAY:
				if(pdc.has(key, PersistentDataType.BYTE_ARRAY))
				{
					gce.getValuesByteArray().put(purekey, pdc.get(key, PersistentDataType.BYTE_ARRAY));
				}
				continue;
			case DOUBLE:
				if(pdc.has(key, PersistentDataType.DOUBLE))
				{
					gce.getValuesDouble().put(purekey, pdc.get(key, PersistentDataType.DOUBLE));
				}
				continue;
			case FLOAT:
				if(pdc.has(key, PersistentDataType.FLOAT))
				{
					gce.getValuesFloat().put(purekey, pdc.get(key, PersistentDataType.FLOAT));
				}
				continue;
			case INTEGER:
				if(pdc.has(key, PersistentDataType.INTEGER))
				{
					gce.getValuesInteger().put(purekey, pdc.get(key, PersistentDataType.INTEGER));
				}
				continue;
			case INTEGER_ARRAY:
				if(pdc.has(key, PersistentDataType.INTEGER_ARRAY))
				{
					gce.getValuesIntegerArray().put(purekey, pdc.get(key, PersistentDataType.INTEGER_ARRAY));
				}
				continue;
			case LONG:
				if(pdc.has(key, PersistentDataType.LONG))
				{
					gce.getValuesLong().put(purekey, pdc.get(key, PersistentDataType.LONG));
				}
				continue;
			case LONG_ARRAY:
				if(pdc.has(key, PersistentDataType.LONG_ARRAY))
				{
					gce.getValuesLongArray().put(purekey, pdc.get(key, PersistentDataType.LONG_ARRAY));
				}
				continue;
			case SHORT:
				if(pdc.has(key, PersistentDataType.SHORT))
				{
					gce.getValuesShort().put(purekey, pdc.get(key, PersistentDataType.SHORT));
				}
				continue;
			case STRING:
				if(pdc.has(key, PersistentDataType.STRING))
				{
					gce.getValuesString().put(purekey, pdc.get(key, PersistentDataType.STRING));
				}
				continue;
			}
		}
		Bukkit.getPluginManager().callEvent(gce);
	}
	
	private HashMap<ClickType, String> getFunctionMap(JavaPlugin plugin, PersistentDataContainer pdc)
	{
		HashMap<ClickType, String> map = new HashMap<>();
		NamespacedKey nl = new NamespacedKey(plugin, GUIApi.LEFT_FUNCTION);
		NamespacedKey nr = new NamespacedKey(plugin, GUIApi.RIGHT_FUNCTION);
		NamespacedKey nd = new NamespacedKey(plugin, GUIApi.DROP_FUNCTION);
		NamespacedKey nsl = new NamespacedKey(plugin, GUIApi.SHIFT_LEFT_FUNCTION);
		NamespacedKey nsr = new NamespacedKey(plugin, GUIApi.SHIFT_RIGHT_FUNCTION);
		NamespacedKey nsd = new NamespacedKey(plugin, GUIApi.SHIFT_DROP_FUNCTION);
		NamespacedKey nn1 = new NamespacedKey(plugin, GUIApi.NUMPAD_1_FUNCTION);
		NamespacedKey nn2 = new NamespacedKey(plugin, GUIApi.NUMPAD_2_FUNCTION);
		NamespacedKey nn3 = new NamespacedKey(plugin, GUIApi.NUMPAD_3_FUNCTION);
		NamespacedKey nn4 = new NamespacedKey(plugin, GUIApi.NUMPAD_4_FUNCTION);
		NamespacedKey nn5 = new NamespacedKey(plugin, GUIApi.NUMPAD_5_FUNCTION);
		NamespacedKey nn6 = new NamespacedKey(plugin, GUIApi.NUMPAD_6_FUNCTION);
		NamespacedKey nn7 = new NamespacedKey(plugin, GUIApi.NUMPAD_7_FUNCTION);
		NamespacedKey nn8 = new NamespacedKey(plugin, GUIApi.NUMPAD_8_FUNCTION);
		NamespacedKey nn9 = new NamespacedKey(plugin, GUIApi.NUMPAD_9_FUNCTION);
		if(pdc.has(nl, PersistentDataType.STRING)){map.put(ClickType.LEFT, pdc.get(nl, PersistentDataType.STRING));}
		if(pdc.has(nr, PersistentDataType.STRING)){map.put(ClickType.RIGHT, pdc.get(nr, PersistentDataType.STRING));}
		if(pdc.has(nd, PersistentDataType.STRING)){map.put(ClickType.DROP, pdc.get(nd, PersistentDataType.STRING));}
		if(pdc.has(nsl, PersistentDataType.STRING)){map.put(ClickType.SHIFT_LEFT, pdc.get(nsl, PersistentDataType.STRING));}
		if(pdc.has(nsr, PersistentDataType.STRING)){map.put(ClickType.SHIFT_RIGHT, pdc.get(nsr, PersistentDataType.STRING));}
		if(pdc.has(nsd, PersistentDataType.STRING)){map.put(ClickType.CTRL_DROP, pdc.get(nsd, PersistentDataType.STRING));}
		if(pdc.has(nn1, PersistentDataType.STRING)){map.put(ClickType.NUMPAD_1, pdc.get(nn1, PersistentDataType.STRING));}
		if(pdc.has(nn2, PersistentDataType.STRING)){map.put(ClickType.NUMPAD_2, pdc.get(nn2, PersistentDataType.STRING));}
		if(pdc.has(nn3, PersistentDataType.STRING)){map.put(ClickType.NUMPAD_3, pdc.get(nn3, PersistentDataType.STRING));}
		if(pdc.has(nn4, PersistentDataType.STRING)){map.put(ClickType.NUMPAD_4, pdc.get(nn4, PersistentDataType.STRING));}
		if(pdc.has(nn5, PersistentDataType.STRING)){map.put(ClickType.NUMPAD_5, pdc.get(nn5, PersistentDataType.STRING));}
		if(pdc.has(nn6, PersistentDataType.STRING)){map.put(ClickType.NUMPAD_6, pdc.get(nn6, PersistentDataType.STRING));}
		if(pdc.has(nn7, PersistentDataType.STRING)){map.put(ClickType.NUMPAD_7, pdc.get(nn7, PersistentDataType.STRING));}
		if(pdc.has(nn8, PersistentDataType.STRING)){map.put(ClickType.NUMPAD_8, pdc.get(nn8, PersistentDataType.STRING));}
		if(pdc.has(nn9, PersistentDataType.STRING)){map.put(ClickType.NUMPAD_9, pdc.get(nn9, PersistentDataType.STRING));}
		return map;
	}
}