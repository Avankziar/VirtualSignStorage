package me.avankziar.vss.spigot.gui.events;

import java.util.HashMap;
import java.util.LinkedHashMap;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;

import me.avankziar.vss.spigot.gui.GUIApi;
import me.avankziar.vss.spigot.gui.objects.ClickType;
import me.avankziar.vss.spigot.gui.objects.SettingsLevel;

public class UpperGuiClickEvent extends Event
{	
	private static final HandlerList HANDLERS = new HandlerList();
	
	private boolean isCancelled;
	private InventoryClickEvent inventoryClickEvent;
	private String pluginName;
	private String inventoryIdentifier;
	private SettingsLevel settingsLevel;
	private HashMap<ClickType, String> functionMap = new HashMap<>();
	private LinkedHashMap<String, Byte> valuesByte = new LinkedHashMap<>();
	private LinkedHashMap<String, byte[]> valuesByteArray = new LinkedHashMap<>();
	private LinkedHashMap<String, Double> valuesDouble = new LinkedHashMap<>();
	private LinkedHashMap<String, Float> valuesFloat = new LinkedHashMap<>();
	private LinkedHashMap<String, Integer> valuesInteger = new LinkedHashMap<>();
	private LinkedHashMap<String, int[]> valuesIntegerArray = new LinkedHashMap<>();
	private LinkedHashMap<String, Long> valuesLong = new LinkedHashMap<>();
	private LinkedHashMap<String, long[]> valuesLongArray = new LinkedHashMap<>();
	private LinkedHashMap<String, Short> valuesShort = new LinkedHashMap<>();
	private LinkedHashMap<String, String> valuesString = new LinkedHashMap<>();
	
	public UpperGuiClickEvent(InventoryClickEvent inventoryClickEvent,
			String pluginName, String inventoryIdentifier,
			SettingsLevel settingsLevel,
			HashMap<ClickType, String> functionMap)
	{
		setCancelled(false);
		setInventoryClickEvent(inventoryClickEvent);
		setPluginName(pluginName);
		setInventoryIdentifier(inventoryIdentifier);
		setSettingsLevel(settingsLevel);
		setFunctionMap(functionMap);
	}
	
	public Object getValue(GUIApi.Type type, String key)
	{
		switch(type)
		{
		case BYTE:
			if(valuesByte.containsKey(key))
			{
				return valuesByte.get(key);
			}
			return null;
		case BYTE_ARRAY:
			if(valuesByteArray.containsKey(key))
			{
				return valuesByteArray.get(key);
			}
			return null;
		case DOUBLE:
			if(valuesDouble.containsKey(key))
			{
				return valuesDouble.get(key);
			}
			return null;
		case FLOAT:
			if(valuesFloat.containsKey(key))
			{
				return valuesFloat.get(key);
			}
			return null;
		case INTEGER:
			if(valuesInteger.containsKey(key))
			{
				return valuesInteger.get(key);
			}
			return null;
		case INTEGER_ARRAY: 
			if(valuesIntegerArray.containsKey(key))
			{
				return valuesIntegerArray.get(key);
			}
			return null;
		case LONG:
			if(valuesLong.containsKey(key))
			{
				return valuesLong.get(key);
			}
			return null;
		case LONG_ARRAY:
			if(valuesLongArray.containsKey(key))
			{
				return valuesLongArray.get(key);
			}
			return null;
		case SHORT:
			if(valuesShort.containsKey(key))
			{
				return valuesShort.get(key);
			}
			return null;
		case STRING:
			if(valuesDouble.containsKey(key))
			{
				return valuesShort.get(key);
			}
			return null;
		}
		return null;
	}
	
	public HandlerList getHandlers() 
    {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() 
    {
        return HANDLERS;
    }

	public boolean isCancelled()
	{
		return isCancelled;
	}

	public void setCancelled(boolean isCancelled)
	{
		this.isCancelled = isCancelled;
	}

	public InventoryClickEvent getEvent()
	{
		return inventoryClickEvent;
	}

	public void setInventoryClickEvent(InventoryClickEvent inventoryClickEvent)
	{
		this.inventoryClickEvent = inventoryClickEvent;
	}

	public String getPluginName()
	{
		return pluginName;
	}

	public void setPluginName(String pluginName)
	{
		this.pluginName = pluginName;
	}

	public String getInventoryIdentifier()
	{
		return inventoryIdentifier;
	}

	public void setInventoryIdentifier(String inventoryIdentifier)
	{
		this.inventoryIdentifier = inventoryIdentifier;
	}

	public SettingsLevel getSettingsLevel()
	{
		return settingsLevel;
	}

	public void setSettingsLevel(SettingsLevel settingsLevel)
	{
		this.settingsLevel = settingsLevel;
	}

	public LinkedHashMap<String, Byte> getValuesByte()
	{
		return valuesByte;
	}

	public void setValuesByte(LinkedHashMap<String, Byte> valuesByte)
	{
		this.valuesByte = valuesByte;
	}

	public LinkedHashMap<String, byte[]> getValuesByteArray()
	{
		return valuesByteArray;
	}

	public void setValuesByteArray(LinkedHashMap<String, byte[]> valuesByteArray)
	{
		this.valuesByteArray = valuesByteArray;
	}

	public LinkedHashMap<String, Double> getValuesDouble()
	{
		return valuesDouble;
	}

	public void setValuesDouble(LinkedHashMap<String, Double> valuesDouble)
	{
		this.valuesDouble = valuesDouble;
	}

	public LinkedHashMap<String, Float> getValuesFloat()
	{
		return valuesFloat;
	}

	public void setValuesFloat(LinkedHashMap<String, Float> valuesFloat)
	{
		this.valuesFloat = valuesFloat;
	}

	public LinkedHashMap<String, Integer> getValuesInteger()
	{
		return valuesInteger;
	}

	public void setValuesInteger(LinkedHashMap<String, Integer> valuesInteger)
	{
		this.valuesInteger = valuesInteger;
	}

	public LinkedHashMap<String, int[]> getValuesIntegerArray()
	{
		return valuesIntegerArray;
	}

	public void setValuesIntegerArray(LinkedHashMap<String, int[]> valuesIntegerArray)
	{
		this.valuesIntegerArray = valuesIntegerArray;
	}

	public LinkedHashMap<String, Long> getValuesLong()
	{
		return valuesLong;
	}

	public void setValuesLong(LinkedHashMap<String, Long> valuesLong)
	{
		this.valuesLong = valuesLong;
	}

	public LinkedHashMap<String, long[]> getValuesLongArray()
	{
		return valuesLongArray;
	}

	public void setValuesLongArray(LinkedHashMap<String, long[]> valuesLongArray)
	{
		this.valuesLongArray = valuesLongArray;
	}

	public LinkedHashMap<String, Short> getValuesShort()
	{
		return valuesShort;
	}

	public void setValuesShort(LinkedHashMap<String, Short> valuesShort)
	{
		this.valuesShort = valuesShort;
	}

	public LinkedHashMap<String, String> getValuesString()
	{
		return valuesString;
	}

	public void setValuesString(LinkedHashMap<String, String> valuesString)
	{
		this.valuesString = valuesString;
	}
	
	private void setFunctionMap(HashMap<ClickType, String> functionMap)
	{
		this.functionMap = functionMap;
	}
	
	public HashMap<ClickType, String> getFunctionMap()
	{
		return functionMap;
	}
	
	public String getFunction(ClickType clickType)
	{
		return functionMap.get(clickType);
	}
}
