package me.avankziar.vss.spigot.gui;

import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.UUID;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import me.avankziar.vss.general.database.MysqlType;
import me.avankziar.vss.general.objects.SignStorage;
import me.avankziar.vss.spigot.VSS;
import me.avankziar.vss.spigot.gui.events.ClickFunction;
import me.avankziar.vss.spigot.gui.objects.GuiType;
import me.avankziar.vss.spigot.gui.objects.SettingsLevel;

public class GUIApi
{
	public enum Type
	{
		STRING, BYTE, BYTE_ARRAY, DOUBLE, FLOAT, INTEGER, INTEGER_ARRAY, LONG, LONG_ARRAY, SHORT
	}	
	
	public static final String PLUGINNAME = "pluginname";
	public static final String INVENTORYIDENTIFIER = "inventoryidentifier";
	public static final String CLICKEVENTCANCEL = "clickeventcancel";
	
	public static final String RIGHT_FUNCTION = "right_function";
	public static final String LEFT_FUNCTION = "left_function";
	public static final String DROP_FUNCTION = "drop_function";
	public static final String SHIFT_RIGHT_FUNCTION = "shift_right_function";
	public static final String SHIFT_LEFT_FUNCTION = "shift_left_function";
	public static final String SHIFT_DROP_FUNCTION = "shift_drop_function";
	public static final String NUMPAD_1_FUNCTION = "numpad_1_function";
	public static final String NUMPAD_2_FUNCTION = "numpad_2_function";
	public static final String NUMPAD_3_FUNCTION = "numpad_3_function";
	public static final String NUMPAD_4_FUNCTION = "numpad_4_function";
	public static final String NUMPAD_5_FUNCTION = "numpad_5_function";
	public static final String NUMPAD_6_FUNCTION = "numpad_6_function";
	public static final String NUMPAD_7_FUNCTION = "numpad_7_function";
	public static final String NUMPAD_8_FUNCTION = "numpad_8_function";
	public static final String NUMPAD_9_FUNCTION = "numpad_9_function";
	
	public static final String SETTINGLEVEL = "settinglevel";
	
	private Inventory inventory;
	private String pluginName;
	private String inventoryIdentifier;
	private static JavaPlugin plugin = VSS.getPlugin();
	private SettingsLevel settingsLevel;
	
	public GUIApi(String pluginName, String inventoryIdentifier, InventoryHolder owner, int row, String title, SettingsLevel settingsLevel)
	{
		if(row > 6) row = 6;
		this.inventory = Bukkit.createInventory(owner, row*9, title);
		this.pluginName = pluginName;
		this.inventoryIdentifier = inventoryIdentifier;
		this.settingsLevel = settingsLevel;
	}
	
	public GUIApi(String pluginName, Inventory inventory, 
			String inventoryIdentifier, SettingsLevel settingsLevel)
	{
		this.inventory = inventory;
		this.inventory.clear();
		this.pluginName = pluginName;
		this.inventoryIdentifier = inventoryIdentifier;
		this.settingsLevel = settingsLevel;
	}
	
	/**
	 * Recreate a itemstack with the needed pdc values.
	 * @param itemstack
	 * @param pluginName
	 * @param inventoryIdentifier
	 * @param function
	 * @param settingsLevel
	 * @param clickEventCancel
	 * @param values
	 * @return
	 */
	public static ItemStack recreate(
			ItemStack itemstack, 
			String pluginName, String inventoryIdentifier,
			SettingsLevel settingsLevel, boolean clickEventCancel,
			@Nullable LinkedHashMap<String, Entry<Type, Object>> values,
			ClickFunction... clickFunction)
	{
		ItemStack i = itemstack.clone();
		ItemMeta im = i.getItemMeta();
		PersistentDataContainer pdc = im.getPersistentDataContainer();
		pdc.set(new NamespacedKey(plugin, PLUGINNAME), PersistentDataType.STRING, pluginName);
		pdc.set(new NamespacedKey(plugin, INVENTORYIDENTIFIER), PersistentDataType.STRING, inventoryIdentifier);
		pdc.set(new NamespacedKey(plugin, CLICKEVENTCANCEL), PersistentDataType.STRING, String.valueOf(clickEventCancel));
		pdc.set(new NamespacedKey(plugin, SETTINGLEVEL), PersistentDataType.STRING, settingsLevel.getName());
		for(ClickFunction cf : clickFunction)
		{
			switch(cf.getClickType())
			{
			case DROP:
				pdc.set(new NamespacedKey(plugin, DROP_FUNCTION), PersistentDataType.STRING, cf.getFunction()); break;
			case LEFT:
				pdc.set(new NamespacedKey(plugin, LEFT_FUNCTION), PersistentDataType.STRING, cf.getFunction()); break;
			case RIGHT:
				pdc.set(new NamespacedKey(plugin, RIGHT_FUNCTION), PersistentDataType.STRING, cf.getFunction()); break;
			case NUMPAD_1:
				pdc.set(new NamespacedKey(plugin, NUMPAD_1_FUNCTION), PersistentDataType.STRING, cf.getFunction()); break;
			case NUMPAD_2:
				pdc.set(new NamespacedKey(plugin, NUMPAD_2_FUNCTION), PersistentDataType.STRING, cf.getFunction()); break;
			case NUMPAD_3:
				pdc.set(new NamespacedKey(plugin, NUMPAD_3_FUNCTION), PersistentDataType.STRING, cf.getFunction()); break;
			case NUMPAD_4:
				pdc.set(new NamespacedKey(plugin, NUMPAD_4_FUNCTION), PersistentDataType.STRING, cf.getFunction()); break;
			case NUMPAD_5:
				pdc.set(new NamespacedKey(plugin, NUMPAD_5_FUNCTION), PersistentDataType.STRING, cf.getFunction()); break;
			case NUMPAD_6:
				pdc.set(new NamespacedKey(plugin, NUMPAD_6_FUNCTION), PersistentDataType.STRING, cf.getFunction()); break;
			case NUMPAD_7:
				pdc.set(new NamespacedKey(plugin, NUMPAD_7_FUNCTION), PersistentDataType.STRING, cf.getFunction()); break;
			case NUMPAD_8:
				pdc.set(new NamespacedKey(plugin, NUMPAD_8_FUNCTION), PersistentDataType.STRING, cf.getFunction()); break;
			case NUMPAD_9:
				pdc.set(new NamespacedKey(plugin, NUMPAD_9_FUNCTION), PersistentDataType.STRING, cf.getFunction()); break;
			case CTRL_DROP:
				pdc.set(new NamespacedKey(plugin, SHIFT_DROP_FUNCTION), PersistentDataType.STRING, cf.getFunction()); break;
			case SHIFT_LEFT:
				pdc.set(new NamespacedKey(plugin, SHIFT_LEFT_FUNCTION), PersistentDataType.STRING, cf.getFunction()); break;
			case SHIFT_RIGHT:
				pdc.set(new NamespacedKey(plugin, SHIFT_RIGHT_FUNCTION), PersistentDataType.STRING, cf.getFunction()); break;
			}
		}
		if(values != null)
		{
			for(String key : values.keySet())
			{
				Entry<Type, Object> value = values.get(key);
				String fullkey = key+"---"+value.getKey().toString();
				switch(value.getKey())
				{
				case BYTE:
					if(value.getValue() instanceof Byte)
					{
						pdc.set(new NamespacedKey(plugin, fullkey), PersistentDataType.BYTE, (byte) value.getValue());
					}
					break;
				case BYTE_ARRAY:
					if(value.getValue() instanceof byte[])
					{
						pdc.set(new NamespacedKey(plugin, fullkey), PersistentDataType.BYTE_ARRAY, (byte[]) value.getValue());
					}
					break;
				case DOUBLE:
					if(value.getValue() instanceof Double)
					{
						pdc.set(new NamespacedKey(plugin, fullkey), PersistentDataType.DOUBLE, (double) value.getValue());
					}
					break;
				case FLOAT:
					if(value.getValue() instanceof Float)
					{
						pdc.set(new NamespacedKey(plugin, fullkey), PersistentDataType.FLOAT, (float) value.getValue());
					}
					break;
				case INTEGER:
					if(value.getValue() instanceof Integer)
					{
						pdc.set(new NamespacedKey(plugin, fullkey), PersistentDataType.INTEGER, (int) value.getValue());
					}
					break;
				case INTEGER_ARRAY: 
					if(value.getValue() instanceof int[])
					{
						pdc.set(new NamespacedKey(plugin, fullkey), PersistentDataType.INTEGER_ARRAY, (int[]) value.getValue());
					}
					break;
				case LONG:
					if(value.getValue() instanceof Long)
					{
						pdc.set(new NamespacedKey(plugin, fullkey), PersistentDataType.LONG, (long) value.getValue());
					}
					break;
				case LONG_ARRAY:
					if(value.getValue() instanceof long[])
					{
						pdc.set(new NamespacedKey(plugin, fullkey), PersistentDataType.LONG_ARRAY, (long[]) value.getValue());
					}
					break;
				case SHORT:
					if(value.getValue() instanceof Short)
					{
						pdc.set(new NamespacedKey(plugin, fullkey), PersistentDataType.SHORT, (short) value.getValue());
					}
					break;
				case STRING:
					if(value.getValue() instanceof String)
					{
						pdc.set(new NamespacedKey(plugin, fullkey), PersistentDataType.STRING, (String) value.getValue());
					}
					break;
				}
			}
		}
		i.setItemMeta(im);
		return i;
	}
	
	public void add(int slot, ItemStack itemstack, 
			SettingsLevel settingsLevel, boolean clickEventCancel,
			@Nullable LinkedHashMap<String, Entry<Type, Object>> values,
			ClickFunction...clickFunction)
	{
		if(itemstack == null)
		{
			return;
		}
		ItemStack i = itemstack.clone();
		ItemMeta im = i.getItemMeta();
		PersistentDataContainer pdc = im.getPersistentDataContainer();
		pdc.set(new NamespacedKey(plugin, PLUGINNAME), PersistentDataType.STRING, this.pluginName);
		pdc.set(new NamespacedKey(plugin, INVENTORYIDENTIFIER), PersistentDataType.STRING, this.inventoryIdentifier);
		pdc.set(new NamespacedKey(plugin, CLICKEVENTCANCEL), PersistentDataType.STRING, String.valueOf(clickEventCancel));
		pdc.set(new NamespacedKey(plugin, SETTINGLEVEL), PersistentDataType.STRING, settingsLevel.getName());
		for(ClickFunction cf : clickFunction)
		{
			switch(cf.getClickType())
			{
			case DROP:
				pdc.set(new NamespacedKey(plugin, DROP_FUNCTION), PersistentDataType.STRING, cf.getFunction()); break;
			case LEFT:
				pdc.set(new NamespacedKey(plugin, LEFT_FUNCTION), PersistentDataType.STRING, cf.getFunction()); break;
			case RIGHT:
				pdc.set(new NamespacedKey(plugin, RIGHT_FUNCTION), PersistentDataType.STRING, cf.getFunction()); break;
			case NUMPAD_1:
				pdc.set(new NamespacedKey(plugin, NUMPAD_1_FUNCTION), PersistentDataType.STRING, cf.getFunction()); break;
			case NUMPAD_2:
				pdc.set(new NamespacedKey(plugin, NUMPAD_2_FUNCTION), PersistentDataType.STRING, cf.getFunction()); break;
			case NUMPAD_3:
				pdc.set(new NamespacedKey(plugin, NUMPAD_3_FUNCTION), PersistentDataType.STRING, cf.getFunction()); break;
			case NUMPAD_4:
				pdc.set(new NamespacedKey(plugin, NUMPAD_4_FUNCTION), PersistentDataType.STRING, cf.getFunction()); break;
			case NUMPAD_5:
				pdc.set(new NamespacedKey(plugin, NUMPAD_5_FUNCTION), PersistentDataType.STRING, cf.getFunction()); break;
			case NUMPAD_6:
				pdc.set(new NamespacedKey(plugin, NUMPAD_6_FUNCTION), PersistentDataType.STRING, cf.getFunction()); break;
			case NUMPAD_7:
				pdc.set(new NamespacedKey(plugin, NUMPAD_7_FUNCTION), PersistentDataType.STRING, cf.getFunction()); break;
			case NUMPAD_8:
				pdc.set(new NamespacedKey(plugin, NUMPAD_8_FUNCTION), PersistentDataType.STRING, cf.getFunction()); break;
			case NUMPAD_9:
				pdc.set(new NamespacedKey(plugin, NUMPAD_9_FUNCTION), PersistentDataType.STRING, cf.getFunction()); break;
			case CTRL_DROP:
				pdc.set(new NamespacedKey(plugin, SHIFT_DROP_FUNCTION), PersistentDataType.STRING, cf.getFunction()); break;
			case SHIFT_LEFT:
				pdc.set(new NamespacedKey(plugin, SHIFT_LEFT_FUNCTION), PersistentDataType.STRING, cf.getFunction()); break;
			case SHIFT_RIGHT:
				pdc.set(new NamespacedKey(plugin, SHIFT_RIGHT_FUNCTION), PersistentDataType.STRING, cf.getFunction()); break;
			}
		}
		if(values != null)
		{
			for(String key : values.keySet())
			{
				Entry<Type, Object> value = values.get(key);
				String fullkey = key+"---"+value.getKey().toString();
				switch(value.getKey())
				{
				case BYTE:
					if(value.getValue() instanceof Byte)
					{
						pdc.set(new NamespacedKey(plugin, fullkey), PersistentDataType.BYTE, (byte) value.getValue());
					}
					break;
				case BYTE_ARRAY:
					if(value.getValue() instanceof byte[])
					{
						pdc.set(new NamespacedKey(plugin, fullkey), PersistentDataType.BYTE_ARRAY, (byte[]) value.getValue());
					}
					break;
				case DOUBLE:
					if(value.getValue() instanceof Double)
					{
						pdc.set(new NamespacedKey(plugin, fullkey), PersistentDataType.DOUBLE, (double) value.getValue());
					}
					break;
				case FLOAT:
					if(value.getValue() instanceof Float)
					{
						pdc.set(new NamespacedKey(plugin, fullkey), PersistentDataType.FLOAT, (float) value.getValue());
					}
					break;
				case INTEGER:
					if(value.getValue() instanceof Integer)
					{
						pdc.set(new NamespacedKey(plugin, fullkey), PersistentDataType.INTEGER, (int) value.getValue());
					}
					break;
				case INTEGER_ARRAY: 
					if(value.getValue() instanceof int[])
					{
						pdc.set(new NamespacedKey(plugin, fullkey), PersistentDataType.INTEGER_ARRAY, (int[]) value.getValue());
					}
					break;
				case LONG:
					if(value.getValue() instanceof Long)
					{
						pdc.set(new NamespacedKey(plugin, fullkey), PersistentDataType.LONG, (long) value.getValue());
					}
					break;
				case LONG_ARRAY:
					if(value.getValue() instanceof long[])
					{
						pdc.set(new NamespacedKey(plugin, fullkey), PersistentDataType.LONG_ARRAY, (long[]) value.getValue());
					}
					break;
				case SHORT:
					if(value.getValue() instanceof Short)
					{
						pdc.set(new NamespacedKey(plugin, fullkey), PersistentDataType.SHORT, (short) value.getValue());
					}
					break;
				case STRING:
					if(value.getValue() instanceof String)
					{
						pdc.set(new NamespacedKey(plugin, fullkey), PersistentDataType.STRING, (String) value.getValue());
					}
					break;
				}
			}
		}
		i.setItemMeta(im);
		if(this.inventory != null)
		{
			this.inventory.setItem(slot, i);
		}
	}

	public void open(Player player, GuiType gt, int sshid) 
	{
		if(this.inventory != null) player.openInventory(this.inventory);
		addInGui(player.getUniqueId(), inventoryIdentifier, gt, settingsLevel, sshid);
	}
	
	//Key == playeruuid
	//Value == InventoryIdentifier
	private static LinkedHashMap<UUID, String> playerInGui = new LinkedHashMap<>();
	private static LinkedHashMap<UUID, GuiType> playerInGuiType = new LinkedHashMap<>();
	private static LinkedHashMap<UUID, Integer> playerInGuiSSHID = new LinkedHashMap<>();
	//Key == playeruuid
	//Value == Player actual SettingsLevel
	private static LinkedHashMap<UUID, SettingsLevel> playerGuiSettingsLevel = new LinkedHashMap<>();
	
	public static boolean isInGui(UUID uuid)
    {
    	return playerInGui.containsKey(uuid);
    }
	
	public static String getGui(UUID uuid)
	{
		return playerInGui.get(uuid);
	}
	
	public static GuiType getGuiType(UUID uuid)
	{
		return playerInGuiType.get(uuid);
	}
	
	public static SignStorage getGuiSST(UUID uuid)
	{
		int sshID = playerInGuiSSHID.get(uuid);
		return (SignStorage) VSS.getPlugin().getMysqlHandler().getData(MysqlType.SIGNSTORAGE, "`id` = ?", sshID);
	}
	
	public static SettingsLevel getSettingsLevel(UUID uuid)
	{
		return playerGuiSettingsLevel.get(uuid);
	}
    
	public static void addInGui(UUID uuid, String inventoryIdentifier, GuiType gt, SettingsLevel settingsLevel, int sstid)
    {
		playerInGui.put(uuid, inventoryIdentifier);
		playerInGuiType.put(uuid, gt);
		playerInGuiSSHID.put(uuid, sstid);
		playerGuiSettingsLevel.put(uuid, settingsLevel);
    }
    
	public static void removeInGui(UUID uuid)
    {
		playerInGui.remove(uuid);
		playerInGuiType.remove(uuid);
		playerInGuiSSHID.remove(uuid);
		playerGuiSettingsLevel.remove(uuid);
    }
}
