package me.avankziar.vss.spigot.handler;

import java.lang.reflect.Field;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.ShulkerBox;
import org.bukkit.block.banner.Pattern;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.AxolotlBucketMeta;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.Repairable;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.inventory.meta.SpawnEggMeta;
import org.bukkit.inventory.meta.SuspiciousStewMeta;
import org.bukkit.inventory.meta.TropicalFishBucketMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.scheduler.BukkitRunnable;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import me.avankziar.ifh.spigot.economy.account.Account;
import me.avankziar.vss.general.ChatApi;
import me.avankziar.vss.spigot.SaLE;
import me.avankziar.vss.spigot.assistance.TimeHandler;
import me.avankziar.vss.spigot.assistance.Utility;
import me.avankziar.vss.spigot.database.MysqlHandler;
import me.avankziar.vss.spigot.gui.GUIApi;
import me.avankziar.vss.spigot.gui.events.ClickFunction;
import me.avankziar.vss.spigot.gui.objects.ClickFunctionType;
import me.avankziar.vss.spigot.gui.objects.ClickType;
import me.avankziar.vss.spigot.gui.objects.GuiType;
import me.avankziar.vss.spigot.gui.objects.SettingsLevel;
import me.avankziar.vss.spigot.modifiervalueentry.Bypass;
import me.avankziar.vss.spigot.modifiervalueentry.ModifierValueEntry;
import me.avankziar.vss.spigot.objects.ListedType;
import me.avankziar.vss.spigot.objects.PlayerData;
import me.avankziar.vss.spigot.objects.SignShop;

public class GuiHandler
{
	private static SaLE plugin = SaLE.getPlugin();
	public static String SIGNSHOP_ID = "signshop_id";
	public static String PLAYER_UUID = "player_uuid";
	public static String SEARCH_TELEPORT_OR_LOCATION = "search_teleport_or_location";
	public static String PAGE = "page";
	public static String WHERE = "where";
	
	public static void openAdministration(SignShop ssh, Player player, SettingsLevel settingsLevel, boolean closeInv)
	{
		GuiType gt = GuiType.ADMINISTRATION;
		GUIApi gui = new GUIApi(plugin.pluginName, gt.toString(), null, 6, "Shop: "+ssh.getSignShopName(), 
				settingsLevel == null ? SettingsLevel.BASE : settingsLevel);
		SignShop ssh2 = (SignShop) plugin.getMysqlHandler().getData(MysqlHandler.Type.SIGNSHOP, "`id` = ?", ssh.getId());
		openGui(ssh2, player, gt, gui, settingsLevel, closeInv);
	}
	
	public static void openAdministration(SignShop ssh, Player player, SettingsLevel settingsLevel, Inventory inv, boolean closeInv)
	{
		GuiType gt = GuiType.ADMINISTRATION;
		GUIApi gui = new GUIApi(plugin.pluginName, inv, gt.toString(), 
				settingsLevel == null ? SettingsLevel.BASE : settingsLevel);
		SignShop ssh2 = (SignShop) plugin.getMysqlHandler().getData(MysqlHandler.Type.SIGNSHOP, "`id` = ?", ssh.getId());
		openGui(ssh2, player, gt, gui, settingsLevel, closeInv);
	}
	
	public static void openShop(SignShop ssh, Player player, SettingsLevel settingsLevel, boolean closeInv)
	{
		GuiType gt = GuiType.SHOP;
		GUIApi gui = new GUIApi(plugin.pluginName, gt.toString(), null, 6, "Shop "+ssh.getSignShopName(), settingsLevel);
		SignShop ssh2 = (SignShop) plugin.getMysqlHandler().getData(MysqlHandler.Type.SIGNSHOP, "`id` = ?", ssh.getId());
		openGui(ssh2, player, gt, gui, settingsLevel, closeInv);
	}
	
	public static void openShop(SignShop ssh, Player player, SettingsLevel settingsLevel, Inventory inv, boolean closeInv)
	{
		GuiType gt = GuiType.SHOP;
		GUIApi gui = new GUIApi(plugin.pluginName, inv, gt.toString(), settingsLevel);
		SignShop ssh2 = (SignShop) plugin.getMysqlHandler().getData(MysqlHandler.Type.SIGNSHOP, "`id` = ?", ssh.getId());
		openGui(ssh2, player, gt, gui, settingsLevel, closeInv);
	}
	
	public static void openInputInfo(SignShop ssh, Player player, SettingsLevel settingsLevel, boolean closeInv)
	{
		GuiType gt = GuiType.ITEM_INPUT;
		GUIApi gui = new GUIApi(plugin.pluginName, gt.toString(), null, 6, "Shop:"+String.valueOf(ssh.getId()), settingsLevel);
		SignShop ssh2 = (SignShop) plugin.getMysqlHandler().getData(MysqlHandler.Type.SIGNSHOP, "`id` = ?", ssh.getId());
		openGui(ssh2, player, gt, gui, settingsLevel, closeInv);
	}
	
	public static void openKeyOrNumInput(SignShop ssh, Player player, GuiType gt, SettingsLevel settingsLevel, String keyboardOrNumpad, boolean closeInv)
	{
		GUIApi gui = new GUIApi(plugin.pluginName, gt.toString(), null, 6, ssh.getSignShopName()+keyboardOrNumpad, settingsLevel);
		SignShop ssh2 = (SignShop) plugin.getMysqlHandler().getData(MysqlHandler.Type.SIGNSHOP, "`id` = ?", ssh.getId());
		openGui(ssh2, player, gt, gui, settingsLevel, closeInv);
	}
	
	public static void openSearch(ArrayList<SignShop> list, Player player, GuiType gt, SettingsLevel settingsLevel, boolean closeInv,
			Material searchMat, boolean teleport_OR_Location)
	{
		GUIApi gui = new GUIApi(plugin.pluginName, gt.toString(), null, 6,
				ChatApi.tl(plugin.getYamlHandler().getLang().getString("SearchFunctionHandler.Title")
				.replace("%mat%", searchMat.toString())), settingsLevel);
		openSearchGui(list, player, gt, gui, settingsLevel, closeInv, searchMat, teleport_OR_Location);
	}
	
	public static void openSubscribed(ArrayList<SignShop> list, Player player, int page, String where, boolean closeInv, Inventory inv)
	{
		GuiType gt = GuiType.SUBSCIBED;
		GUIApi gui = null;
		if(inv == null)
		{
			gui = new GUIApi(plugin.pluginName, gt.toString(), null, 6,
					ChatApi.tl(plugin.getYamlHandler().getLang().getString("SubscribedFunctionHandler.Title")
							.replace("%player%", player.getName())),
					SettingsLevel.BASE);
		} else
		{
			inv.clear();
			gui = new GUIApi(plugin.pluginName, inv, gt.toString(), SettingsLevel.BASE);
		}
		openListGui(list, player, gt, gui, closeInv, page, where);
	}
	
	private static void openGui(SignShop ssh, Player player, GuiType gt, GUIApi gui, SettingsLevel settingsLevel, boolean closeInv)
	{
		if(plugin.getIFHEco() != null)
		{
			Account ac = plugin.getIFHEco().getAccount(ssh.getAccountId());
			if(ac == null)
			{
				player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("GuiHandler.AccountNotExist")
						.replace("%player%", Utility.convertUUIDToName(ssh.getOwner().toString()))));
				if(gt == GuiType.SHOP)
				{
					return;
				}
			}
		} else if(plugin.getVaultEco() != null)
		{
			if(!plugin.getVaultEco().hasAccount(Bukkit.getOfflinePlayer(ssh.getOwner())))
			{
				player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("GuiHandler.AccountNotExist")
						.replace("%player%", Utility.convertUUIDToName(ssh.getOwner().toString()))));
				if(gt == GuiType.SHOP)
				{
					return;
				}
			}
		}
		boolean fillNotDefineGuiSlots = new ConfigHandler().fillNotDefineGuiSlots();
		Material filler = Material.valueOf(plugin.getConfig().getString("SignShop.Gui.FillerItemMaterial", "LIGHT_GRAY_STAINED_GLASS_PANE"));
		YamlConfiguration y = plugin.getYamlHandler().getGui(gt);
		for(int i = 0; i < 54; i++)
		{
			if(y.get(i+".IsInfoItem") != null && y.getBoolean(i+".IsInfoItem"))
			{
				ItemStack is = ssh.getItemStack();
				LinkedHashMap<String, Entry<GUIApi.Type, Object>> map = new LinkedHashMap<>();
				map.put(SIGNSHOP_ID, new AbstractMap.SimpleEntry<GUIApi.Type, Object>(GUIApi.Type.INTEGER, ssh.getId()));
				gui.add(i, is, settingsLevel, true, map, getClickFunction(y, String.valueOf(i)));
				continue;
			}
			if(y.get(i+".Material") == null && y.get(i+".Material."+settingsLevel.toString()) == null)
			{
				if(fillNotDefineGuiSlots)
				{
					filler(gui, ssh.getId(), i, filler);
				}
				continue;
			}			
			SettingsLevel itemSL = SettingsLevel.valueOf(y.getString(i+".SettingLevel"));
			if(y.get(i+".SettingLevel") == null)
			{
				itemSL = SettingsLevel.NOLEVEL;
			}
			if(settingsLevel.getOrdinal() < itemSL.getOrdinal())
			{
				if(fillNotDefineGuiSlots)
				{
					filler(gui, ssh.getId(), i, filler);
				}
				continue;
			}
			if(y.get(i+".Permission") != null)
			{
				if(!ModifierValueEntry.hasPermission(player, Bypass.Permission.SHOP_GUI_BYPASS, y.getString(i+".Permission")))
				{
					if(fillNotDefineGuiSlots)
					{
						filler(gui, ssh.getId(), i, filler);
					}
					continue;
				}
			}
			if(y.get(i+".IFHDepend") != null)
			{
				if(y.getBoolean(i+".IFHDepend"))
				{
					if(plugin.getIFHEco() == null)
					{
						if(fillNotDefineGuiSlots)
						{
							filler(gui, ssh.getId(), i, filler);
						}
						continue;
					}
				}
			}
			if(y.get(i+".CanBuy") != null)
			{
				if(y.getBoolean(i+".CanBuy"))
				{
					if(!ssh.canBuy())
					{
						if(fillNotDefineGuiSlots)
						{
							filler(gui, ssh.getId(), i, filler);
						}
						continue;
					}
					if(gt == GuiType.SHOP)
					{
						if(SignHandler.isDiscount(ssh, System.currentTimeMillis()))
						{
							if((ssh.getBuyAmount() == null || ssh.getBuyAmount() < 0.0)
									&& (ssh.getDiscountBuyAmount() == null || ssh.getDiscountBuyAmount() < 0.0))
							{
								if(fillNotDefineGuiSlots)
								{
									filler(gui, ssh.getId(), i, filler);
								}
								continue;
							}
						} else
						{
							if(ssh.getBuyAmount() == null || ssh.getBuyAmount() < 0.0)
							{
								if(fillNotDefineGuiSlots)
								{
									filler(gui, ssh.getId(), i, filler);
								}
								continue;
							}
						}
					}
				}
			}
			if(y.get(i+".CanSell") != null)
			{
				if(y.getBoolean(i+".CanSell"))
				{
					if(!ssh.canSell())
					{
						if(fillNotDefineGuiSlots)
						{
							filler(gui, ssh.getId(), i, filler);
						}
						continue;
					}
					if(gt == GuiType.SHOP)
					{
						if(SignHandler.isDiscount(ssh, System.currentTimeMillis()))
						{
							if((ssh.getSellAmount() == null || ssh.getSellAmount() < 0.0)
									&& (ssh.getDiscountSellAmount() == null || ssh.getDiscountSellAmount() < 0.0))
							{
								if(fillNotDefineGuiSlots)
								{
									filler(gui, ssh.getId(), i, filler);
								}
								continue;
							}
						} else
						{
							if(ssh.getSellAmount() == null || ssh.getSellAmount() < 0.0)
							{
								if(fillNotDefineGuiSlots)
								{
									filler(gui, ssh.getId(), i, filler);
								}
								continue;
							}
						}
					}
				}
			}
			Material mat = null;
			ItemStack is = null;
			if(y.get(i+".Material."+settingsLevel.toString()) != null)
			{
				mat = Material.valueOf(y.getString(i+".Material."+settingsLevel.toString()));
				if(mat == Material.PLAYER_HEAD && y.getString(i+"."+settingsLevel.toString()+".PlayerHeadTexture") != null)
				{
					is = getSkull(y.getString(i+"."+settingsLevel.getName()+".PlayerHeadTexture"));
				}
			} else
			{
				try
				{
					mat = Material.valueOf(y.getString(i+".Material"));
					if(mat == Material.PLAYER_HEAD && y.getString(i+".HeadTexture") != null)
					{
						is = getSkull(y.getString(i+".HeadTexture"));
					}
				} catch(Exception e)
				{
					if(fillNotDefineGuiSlots)
					{
						filler(gui, ssh.getId(), i, filler);
					}
					continue;
				}
			}
			String playername = null;
			UUID otheruuid = null;
			if(y.get(i+".PlayerSearchNum") != null)
			{
				if(ssh.getNumText().isBlank() || ssh.getNumText().isEmpty())
				{
					if(fillNotDefineGuiSlots)
					{
						filler(gui, ssh.getId(), i, filler);
					}
					continue;
				}
				int num = y.getInt(i+".PlayerSearchNum");
				ArrayList<Object> l = plugin.getMysqlHandler().getList(
						MysqlHandler.Type.PLAYERDATA, "`player_name` ASC", num, 1, "`player_name` like ?", "%"+ssh.getNumText()+"%");
				if(l == null || l.isEmpty())
				{
					if(fillNotDefineGuiSlots)
					{
						filler(gui, ssh.getId(), i, filler);
					}
					continue;
				}
				PlayerData pd = PlayerData.convert(l).get(0);
				playername = pd.getName();
				otheruuid = pd.getUUID();
				is = new ItemStack(Material.PLAYER_HEAD);
				ItemMeta im = is.getItemMeta();
				if(!(im instanceof SkullMeta))
				{
					if(fillNotDefineGuiSlots)
					{
						filler(gui, ssh.getId(), i, filler);
					}
					continue;
				}
				SkullMeta sm = (SkullMeta) im;
				try
				{
					sm.setOwningPlayer(Bukkit.getOfflinePlayer(pd.getUUID()));
				} catch(Exception e)
				{
					PlayerProfile profile = Bukkit.createPlayerProfile(pd.getUUID(), "");
					sm.setOwnerProfile(profile);
				}
				
				is.setItemMeta(sm);
			}
			int amount = 1;
			if(y.get(i+".Amount") != null)
			{
				amount = y.getInt(i+".Amount");
			}
			ArrayList<String> lore = null;
			if(y.get(i+".Lore."+settingsLevel.toString()) != null)
			{
				lore = (ArrayList<String>) y.getStringList(i+".Lore."+settingsLevel.toString());
			} else
			{
				if(y.get(i+".Lore") != null)
				{
					lore = (ArrayList<String>) y.getStringList(i+".Lore");
				}
			}
			if(lore != null)
			{
				lore = (ArrayList<String>) getLorePlaceHolder(ssh, player, lore, playername);
			}
			
			if(y.get(i+".InfoLore") != null && y.getBoolean(i+".InfoLore"))
			{
				if(lore == null)
				{
					lore = new ArrayList<>();
				}
				ArrayList<String> infoLore = getStringPlaceHolder(ssh.getItemStack(), ssh.getOwner());
				for(String s : infoLore)
				{
					lore.add(ChatApi.tl(s));
				}
			}
			String displayname = y.get(i+".Displayname") != null 
					? y.getString(i+".Displayname") 
					: (playername != null ? playername 
					: (SaLE.getPlugin().getEnumTl() != null
							  ? SaLE.getPlugin().getEnumTl().getLocalization(mat)
							  : is.getType().toString()));
			displayname = getStringPlaceHolder(ssh, player, displayname, playername);
			if(is == null)
			{
				is = new ItemStack(mat, amount);
			} else
			{
				is.setAmount(amount);
			}
			ItemMeta im = is.getItemMeta();
			im.setDisplayName(displayname);
			if(lore != null)
			{
				im.setLore(lore);
			}
			is.setItemMeta(im);
			LinkedHashMap<String, Entry<GUIApi.Type, Object>> map = new LinkedHashMap<>();
			map.put(SIGNSHOP_ID, new AbstractMap.SimpleEntry<GUIApi.Type, Object>(GUIApi.Type.INTEGER, ssh.getId()));
			if(otheruuid != null)
			{
				map.put(PLAYER_UUID, new AbstractMap.SimpleEntry<GUIApi.Type, Object>(GUIApi.Type.STRING, otheruuid.toString()));
			}
			gui.add(i, is, settingsLevel, true, map, getClickFunction(y, String.valueOf(i)));
		}
		new BukkitRunnable()
		{			
			@Override
			public void run()
			{
				if(closeInv)
				{
					player.closeInventory();
				}
				gui.open(player, gt, ssh.getId());
			}
		}.runTask(plugin);
		
	}
	
	private static void openSearchGui(ArrayList<SignShop> list, Player player, GuiType gt, GUIApi gui, SettingsLevel settingsLevel, boolean closeInv,
			Material searchMat, boolean teleport_OR_Location)
	{
		boolean fillNotDefineGuiSlots = new ConfigHandler().fillNotDefineGuiSlots();
		Material filler = Material.valueOf(plugin.getConfig().getString("SignShop.Gui.FillerItemMaterial", "LIGHT_GRAY_STAINED_GLASS_PANE"));
		YamlConfiguration y = plugin.getYamlHandler().getGui(gt);
		int i = 0;
		for(SignShop ssh : list)
		{
			ArrayList<String> lore = null;
			if(y.get("Lore."+settingsLevel.toString()) != null)
			{
				lore = (ArrayList<String>) y.getStringList("Lore."+settingsLevel.toString());
			} else
			{
				if(y.get("Lore") != null)
				{
					lore = (ArrayList<String>) y.getStringList("Lore");
				}
			}
			if(lore != null)
			{
				lore = (ArrayList<String>) getLorePlaceHolder(ssh, player, lore, player.getName());
			}
			String displayname = y.get("Displayname") != null 
					? y.getString("Displayname") 
					: (SaLE.getPlugin().getEnumTl() != null
							  ? SaLE.getPlugin().getEnumTl().getLocalization(searchMat)
							  : searchMat.toString());
			displayname = getStringPlaceHolder(ssh, player, displayname, player.getName());
			ItemStack is = new ItemStack(Material.PLAYER_HEAD);
			ItemMeta im = is.getItemMeta();
			SkullMeta sm = (SkullMeta) im;
			try
			{
				sm.setOwningPlayer(Bukkit.getOfflinePlayer(ssh.getOwner()));
			} catch(Exception e)
			{
				PlayerProfile profile = Bukkit.createPlayerProfile(ssh.getOwner(), "");
				sm.setOwnerProfile(profile);
			}
			im.setDisplayName(displayname);
			if(lore != null)
			{
				im.setLore(lore);
			}
			is.setItemMeta(im);
			LinkedHashMap<String, Entry<GUIApi.Type, Object>> map = new LinkedHashMap<>();
			map.put(SIGNSHOP_ID, new AbstractMap.SimpleEntry<GUIApi.Type, Object>(GUIApi.Type.INTEGER, ssh.getId()));
			map.put(SEARCH_TELEPORT_OR_LOCATION, new AbstractMap.SimpleEntry<GUIApi.Type, Object>(GUIApi.Type.STRING, String.valueOf(teleport_OR_Location)));
			gui.add(i, is, settingsLevel, true, map, getClickFunction(y, null));
			i++;
		}
		for(int j = i; j < 53; j++)
		{
			if(fillNotDefineGuiSlots)
			{
				filler(gui, 0, j, filler);
			}
		}
		new BukkitRunnable()
		{			
			@Override
			public void run()
			{
				if(closeInv)
				{
					player.closeInventory();
				}
				gui.open(player, gt, 0);
			}
		}.runTask(plugin);
	}
	
	private static void openListGui(ArrayList<SignShop> list, Player player, GuiType gt, GUIApi gui, boolean closeInv, int page, String whereQuery)
	{
		boolean fillNotDefineGuiSlots = new ConfigHandler().fillNotDefineGuiSlots();
		Material filler = Material.valueOf(plugin.getConfig().getString("SignShop.Gui.FillerItemMaterial", "LIGHT_GRAY_STAINED_GLASS_PANE"));
		YamlConfiguration y = plugin.getYamlHandler().getGui(gt);
		int i = 0;
		for(SignShop ssh : list)
		{
			ArrayList<String> lore = null;
			if(y.get("Lore") != null)
			{
				lore = (ArrayList<String>) y.getStringList("Lore");
			}
			if(lore != null)
			{
				lore = (ArrayList<String>) getLorePlaceHolder(ssh, player, lore, player.getName());
			}
			String displayname = y.get("Displayname") != null 
					? y.getString("Displayname") 
					: (SaLE.getPlugin().getEnumTl() != null
							  ? SaLE.getPlugin().getEnumTl().getLocalization(ssh.getMaterial())
							  : ssh.getMaterial().toString());
			displayname = getStringPlaceHolder(ssh, player, displayname, player.getName());
			ItemStack is = new ItemStack(ssh.getMaterial() == null ? Material.WIND_CHARGE : ssh.getMaterial());
			ItemMeta im = is.getItemMeta();
			im.setDisplayName(displayname);
			if(lore != null)
			{
				im.setLore(lore);
			}
			is.setItemMeta(im);
			LinkedHashMap<String, Entry<GUIApi.Type, Object>> map = new LinkedHashMap<>();
			map.put(SIGNSHOP_ID, new AbstractMap.SimpleEntry<GUIApi.Type, Object>(GUIApi.Type.INTEGER, ssh.getId()));
			gui.add(i, is, SettingsLevel.NOLEVEL, true, map, getClickFunction(y, null));
			i++;
		}
		for(int j = 0; j < 53; j++)
		{
			if(y.get(i+".Material") == null)
			{
				if(fillNotDefineGuiSlots)
				{
					filler(gui, 0, i, filler);
				}
				continue;
			}
			Material mat = null;
			ItemStack is = null;
			try
			{
				mat = Material.valueOf(y.getString(i+".Material"));
				if(mat == Material.PLAYER_HEAD && y.getString(i+".HeadTexture") != null)
				{
					is = getSkull(y.getString(i+".HeadTexture"));
				}
			} catch(Exception e)
			{
				if(fillNotDefineGuiSlots)
				{
					filler(gui, 0, i, filler);
				}
				continue;
			}
			int amount = 1;
			ArrayList<String> lore = null;
			if(y.get(i+".Lore") != null)
			{
				lore = (ArrayList<String>) y.getStringList(i+".Lore");
			}
			String displayname = y.get(i+".Displayname") != null 
					? y.getString(i+".Displayname")
					: (SaLE.getPlugin().getEnumTl() != null
							  ? SaLE.getPlugin().getEnumTl().getLocalization(mat)
							  : is.getType().toString());
			if(is == null)
			{
				is = new ItemStack(mat, amount);
			} else
			{
				is.setAmount(amount);
			}
			ItemMeta im = is.getItemMeta();
			im.setDisplayName(displayname);
			if(lore != null)
			{
				im.setLore(lore);
			}
			is.setItemMeta(im);
			LinkedHashMap<String, Entry<GUIApi.Type, Object>> map = new LinkedHashMap<>();
			map.put(SIGNSHOP_ID, new AbstractMap.SimpleEntry<GUIApi.Type, Object>(GUIApi.Type.INTEGER, 0));
			if(y.getString(i+".Pagination").equalsIgnoreCase("Next"))
			{
				map.put(PAGE, new AbstractMap.SimpleEntry<GUIApi.Type, Object>(GUIApi.Type.INTEGER, page+1));
				gui.add(i, is, SettingsLevel.BASE, true, map, getClickFunction(y, String.valueOf(i)));
			} else if(y.getString(i+".Pagination").equalsIgnoreCase("Past"))
			{
				if(page > 0)
				{
					map.put(PAGE, new AbstractMap.SimpleEntry<GUIApi.Type, Object>(GUIApi.Type.INTEGER, page-1));
					gui.add(i, is, SettingsLevel.BASE, true, map, getClickFunction(y, String.valueOf(i)));
				}
			} else
			{
				if(fillNotDefineGuiSlots)
				{
					filler(gui, 0, i, filler);
				}
			}
		}
		new BukkitRunnable()
		{			
			@Override
			public void run()
			{
				if(closeInv)
				{
					player.closeInventory();
				}
				gui.open(player, gt, 0);
			}
		}.runTask(plugin);
	}
	
	private static void filler(GUIApi gui, int sshId, int i, Material mat)
	{
		ItemStack is = new ItemStack(mat, 1);
		ItemMeta im = is.getItemMeta();
		im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		im.addItemFlags(ItemFlag.HIDE_DESTROYS);
		im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		im.setDisplayName(ChatApi.tl("&0"));
		im.setLore(new ArrayList<>());
		is.setItemMeta(im);
		LinkedHashMap<String, Entry<GUIApi.Type, Object>> map = new LinkedHashMap<>();
		map.put(SIGNSHOP_ID, new AbstractMap.SimpleEntry<GUIApi.Type, Object>(GUIApi.Type.INTEGER, sshId));
		gui.add(i, is, SettingsLevel.NOLEVEL, true, map, new ClickFunction[0]);
	}
	
	/*@SuppressWarnings("deprecation")
	public static ItemStack getSkull(String paramString) 
	{
		ItemStack is = new ItemStack(Material.PLAYER_HEAD);
		SkullMeta paramSkullMeta = (SkullMeta) is.getItemMeta();
	    try 
	    {
	    	UUID uuid = UUID.randomUUID();
	        PlayerProfile playerProfile = Bukkit.createPlayerProfile(uuid, uuid.toString());
	        playerProfile.getTextures().setSkin(new URL(paramString));
	        paramSkullMeta.setOwnerProfile(playerProfile);
	    } catch (IllegalArgumentException|SecurityException|java.net.MalformedURLException illegalArgumentException) {
	      illegalArgumentException.printStackTrace();
	    }
	    is.setItemMeta(paramSkullMeta);
	    return is;
	}*/
	
	public static ItemStack getSkull(String url) 
	{
		return getSkull(url, 1);
	}
	
	@SuppressWarnings("deprecation")
	public static ItemStack getSkull(String url, int amount) 
	{
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD, amount, (short) 3);
        if (url == null || url.isEmpty())
            return skull;
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), "null");
        byte[] encodedData = org.apache.commons.codec.binary.Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
        Field profileField = null;
        try {
            profileField = skullMeta.getClass().getDeclaredField("profile");
        } catch (NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
        profileField.setAccessible(true);
        try {
            profileField.set(skullMeta, profile);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        skull.setItemMeta(skullMeta);
        return skull;
    }
	
	public static List<String> getLorePlaceHolder(SignShop ssh, Player player, List<String> lore, String playername)
	{
		List<String> list = new ArrayList<>();
		for(String s : lore)
		{
			String a = getStringPlaceHolder(ssh, player, s, playername);
			if(plugin.getIFHEco() != null)
			{
				Account ac = plugin.getIFHEco().getAccount(ssh.getAccountId());
				if(ac != null)
				{
					int dg = ac == null ? 0 : plugin.getIFHEco().getDefaultGradationQuantity(ac.getCurrency());
					boolean useSI = ac == null ? false : plugin.getIFHEco().getDefaultUseSIPrefix(ac.getCurrency());
					boolean useSy = ac == null ? false : plugin.getIFHEco().getDefaultUseSymbol(ac.getCurrency());
					String ts = ac == null ? "." : plugin.getIFHEco().getDefaultThousandSeperator(ac.getCurrency());
					String ds = ac == null ? "," : plugin.getIFHEco().getDefaultDecimalSeperator(ac.getCurrency());
					a = getStringPlaceHolderIFH(ssh, player, a, ac, dg, useSI, useSy, ts, ds, playername);
				}
			} else
			{
				a = getStringPlaceHolderVault(ssh, player, a, playername);
			}
			list.add(a);
		}
		return list;
	}
	
	private static ArrayList<String> getStringPlaceHolder(ItemStack is, UUID uuid)
	{
		if(is == null)
		{
			return new ArrayList<>();
		}
		ItemMeta im = is.getItemMeta();
		ArrayList<String> list = new ArrayList<>();
		YamlConfiguration y = plugin.getYamlHandler().getLang();
		list.add(ChatApi.tl(y.getString("GuiHandler.InfoLore.Owner") 
				+ (Utility.convertUUIDToName(uuid.toString()) == null 
				? "/" : Utility.convertUUIDToName(uuid.toString()))));
		PotionType ptd = PotionType.WATER;
		PotionMeta pmd = null;
		if(im instanceof PotionMeta)
		{
			pmd = (PotionMeta) im;
			ptd = pmd.getBasePotionType();
		}
		list.add(ChatApi.tl(y.getString("GuiHandler.InfoLore.Displayname") 
				+ (is.getItemMeta().hasDisplayName() 
				? is.getItemMeta().getDisplayName() 
				: (ptd != null && pmd != null
					? plugin.getEnumTl().getLocalization(ptd, pmd)
					: (plugin.getEnumTl() != null
					  ? plugin.getEnumTl().getLocalization(is.getType())
					  : is.getType())))));
		list.add(ChatApi.tl(y.getString("GuiHandler.InfoLore.Material") +
				(plugin.getEnumTl() != null 
				? plugin.getEnumTl().getLocalization(is.getType())
				: is.getType().toString())));
		if(im instanceof Damageable)
		{
			Damageable dam = (Damageable) im;
			int dama = getMaxDamage(is.getType())-dam.getDamage();
			if(dama > 0)
			{
				list.add(ChatApi.tl(y.getString("GuiHandler.InfoLore.Damageable") + dama));
			}			
		}
		if(im instanceof Repairable)
		{
			Repairable rep = (Repairable) im;
			if(rep.hasRepairCost())
			{
				list.add(ChatApi.tl(y.getString("GuiHandler.InfoLore.Repairable") + rep.getRepairCost()));
			}
		}
		if(im.getItemFlags().size() > 0)
		{
			list.add(y.getString("GuiHandler.InfoLore.ItemFlag"));
			for(ItemFlag itf : im.getItemFlags())
			{
				list.add(ChatApi.tl("&7"+
						(plugin.getEnumTl() != null 
						? plugin.getEnumTl().getLocalization(itf)
						: itf.toString())));
			}
		}		
		if(Material.ENCHANTED_BOOK != is.getType())
		{
			if(im.hasEnchants())
			{
				list.add(y.getString("GuiHandler.InfoLore.Enchantment"));
				for(Entry<Enchantment, Integer> en : is.getEnchantments().entrySet())
				{
					int level = en.getValue();
					list.add(ChatApi.tl("&7"+
							(plugin.getEnumTl() != null 
							? plugin.getEnumTl().getLocalization(en.getKey())
							: en.getKey().getKey().getKey())
					+" "+GuiHandler.IntegerToRomanNumeral(level)));
				}
			}
		} else
		{
			if(im instanceof EnchantmentStorageMeta)
			{
				EnchantmentStorageMeta esm = (EnchantmentStorageMeta) im;
				if(esm.hasStoredEnchants())
				{
					list.add(y.getString("GuiHandler.InfoLore.StorageEnchantment"));
					for(Entry<Enchantment, Integer> en : esm.getStoredEnchants().entrySet())
					{
						int level = en.getValue();
						list.add(ChatApi.tl("&7"+
								(plugin.getEnumTl() != null 
								? plugin.getEnumTl().getLocalization(en.getKey())
								: en.getKey().getKey().getKey())
						+" "+GuiHandler.IntegerToRomanNumeral(level)));
					}
				}
			}
		}
		if(im instanceof PotionMeta)
		{
			PotionMeta pm = (PotionMeta) im;
			if(pm.hasCustomEffects())
			{
				for(PotionEffect pe : pm.getCustomEffects())
				{
					int level = pe.getAmplifier()+1;
					long dur = pe.getDuration()*50;
					String color = GuiHandler.getPotionColor(pe);
					if(pe.getType() == PotionEffectType.INSTANT_HEALTH 
							|| pe.getType() == PotionEffectType.INSTANT_DAMAGE)
					{
						list.add(ChatApi.tl(color+
								(plugin.getEnumTl() != null 
								? SaLE.getPlugin().getEnumTl().getLocalization(pe.getType())
								: pe.getType().toString())
								+" "+GuiHandler.IntegerToRomanNumeral(level)));
					} else
					{
						list.add(ChatApi.tl(color+
								(plugin.getEnumTl() != null 
								? SaLE.getPlugin().getEnumTl().getLocalization(pe.getType())
								: pe.getType())
								+" "+GuiHandler.IntegerToRomanNumeral(level)+" >> "+TimeHandler.getDateTime(dur, "mm:ss")));
					}
				}
			} else
			{
				/* Checken ob es für die 1.20.4 klappt
				int pv = 0;
				if(is.getType() == Material.POTION) {pv = 1;}
				else if(is.getType() == Material.SPLASH_POTION) {pv = 2;}
				else if(is.getType() == Material.LINGERING_POTION) {pv = 3;}
				else if(is.getType() == Material.TIPPED_ARROW) {pv = 4;}*/
				for(PotionEffect pe : pm.getBasePotionType().getPotionEffects())
				{
					int level = pe.getAmplifier()+1;
					long dur = pe.getDuration()*50;
					String color = GuiHandler.getPotionColor(pe);
					if(pe.getType() == PotionEffectType.INSTANT_HEALTH || pe.getType() == PotionEffectType.INSTANT_DAMAGE)
					{
						list.add(ChatApi.tl(color+
								(plugin.getEnumTl() != null 
								? SaLE.getPlugin().getEnumTl().getLocalization(pe.getType())
								: pe.getType())
								+" "+GuiHandler.IntegerToRomanNumeral(level)));
					} else
					{
						list.add(ChatApi.tl(color+
								(plugin.getEnumTl() != null 
								? plugin.getEnumTl().getLocalization(pe.getType())
								: pe.getType().toString())
								+" "+GuiHandler.IntegerToRomanNumeral(level)+" >> "+TimeHandler.getDateTime(dur, "mm:ss")));
					}
				}
			}
		}
		if(im instanceof SkullMeta)
		{
			SkullMeta sm = (SkullMeta) im;
			if(sm.getOwningPlayer() != null)
			{
				list.add(ChatApi.tl("&7"+sm.getOwningPlayer().getName()));
			}			
		}
		if(im instanceof AxolotlBucketMeta)
		{
			AxolotlBucketMeta abm = (AxolotlBucketMeta) im;
			try
			{
				if(abm.getVariant() != null)
				{
					list.add(ChatApi.tl(y.getString("GuiHandler.InfoLore.AxolotlBucketMeta") + abm.getVariant().toString()));
				}
			} catch(Exception e) {}
		}
		if(im instanceof BannerMeta)
		{
			BannerMeta bm = (BannerMeta) im;
			list.add(ChatApi.tl(y.getString("GuiHandler.InfoLore.BannerMeta")));
			for(Pattern pa : bm.getPatterns())
			{
				list.add(ChatApi.tl("&7"+
						(plugin.getEnumTl() != null 
						? plugin.getEnumTl().getLocalization(pa.getColor(), pa.getPattern())
						: pa.getColor().toString()+"_"+pa.getPattern().toString())));
			}
		}
		if(im instanceof BlockStateMeta)
		{
			BlockStateMeta bsm = (BlockStateMeta) im;
			if(bsm.getBlockState() instanceof ShulkerBox)
			{
				ShulkerBox sh = (ShulkerBox) bsm.getBlockState();
				LinkedHashMap<String, Integer> lhm = new LinkedHashMap<>(); //B64, itemamount
				for(ItemStack its : sh.getSnapshotInventory())
				{
					if(its == null || its.getType() == Material.AIR)
					{
						continue;
					}
					ItemStack c = its.clone();
					c.setAmount(1);
					String b64 = new Base64Handler(c).toBase64();
					int amount = its.getAmount() + (lhm.containsKey(b64) ? lhm.get(b64) : 0);
					lhm.put(b64, amount);
				}
				for(Entry<String, Integer> e : lhm.entrySet())
				{
					ItemStack ist = new Base64Handler(e.getKey()).fromBase64();
					list.add(ChatApi.tl("&7"+
							(plugin.getEnumTl() != null 
							? SaLE.getPlugin().getEnumTl().getLocalization(ist.getType())
							: ist.getType().toString())+ " x"+e.getValue()));
				}
			}
		}
		if(im instanceof BookMeta)
		{
			BookMeta bm = (BookMeta) im;
			if(bm.getTitle() != null)
			{
				list.add(ChatApi.tl(y.getString("GuiHandler.InfoLore.BookMeta.Title") + bm.getTitle()));
			}
			if(bm.getAuthor() != null)
			{
				list.add(ChatApi.tl(y.getString("GuiHandler.InfoLore.BookMeta.Author") + bm.getAuthor()));
			}
			list.add(ChatApi.tl(y.getString("GuiHandler.InfoLore.BookMeta.Page") + bm.getPageCount()));
			if(bm.getGeneration() != null)
			{
				list.add(ChatApi.tl(y.getString("GuiHandler.InfoLore.BookMeta.Generation") 
						+ (plugin.getEnumTl() != null 
						? SaLE.getPlugin().getEnumTl().getLocalization(bm.getGeneration())
						: bm.getGeneration().toString())));
			}
		}
		if(im instanceof LeatherArmorMeta)
		{
			LeatherArmorMeta lam = (LeatherArmorMeta) im;
			list.add(ChatApi.tl(y.getString("GuiHandler.InfoLore.LeatherArmorMeta")+ 
					String.format("#%02x%02x%02x", lam.getColor().getRed(), lam.getColor().getGreen(), lam.getColor().getBlue())
					.toUpperCase()));
		}
		if(im instanceof SpawnEggMeta)
		{
			SpawnEggMeta sem = (SpawnEggMeta) im;
			try
			{
				if(sem.getSpawnedEntity().getEntityType() != null)
				{
					list.add(ChatApi.tl(y.getString("GuiHandler.InfoLore.SpawnEggMeta") 
							+ (plugin.getEnumTl() != null 
							? SaLE.getPlugin().getEnumTl().getLocalization(sem.getSpawnedEntity().getEntityType())
							: sem.getSpawnedEntity().getEntityType().toString())));
				}				
			} catch(Exception e)
			{
				list.add(ChatApi.tl(y.getString("GuiHandler.InfoLore.SpawnEggMeta") 
						+ getSpawnEggType(is.getType())));
			}
			
		}
		if(im instanceof SuspiciousStewMeta)
		{
			SuspiciousStewMeta ssm = (SuspiciousStewMeta) im;
			for(PotionEffect pe : ssm.getCustomEffects())
			{
				int level = pe.getAmplifier()+1;
				long dur = pe.getDuration();
				String color = getPotionColor(pe);
				list.add(ChatApi.tl(color+
						(plugin.getEnumTl() != null 
						? plugin.getEnumTl().getLocalization(pe.getType())
						: pe.getType())
				+" "+GuiHandler.IntegerToRomanNumeral(level)+" >> "+TimeHandler.getDateTime(dur, "mm:ss")));
			}
		}
		if(im instanceof TropicalFishBucketMeta)
		{
			TropicalFishBucketMeta tfbm = (TropicalFishBucketMeta) im;
			list.add(ChatApi.tl(y.getString("GuiHandler.InfoLore.TropicalFishBucketMeta") 
					+ (plugin.getEnumTl() != null 
					? SaLE.getPlugin().getEnumTl().getLocalization(tfbm.getBodyColor(), tfbm.getPattern(), tfbm.getPatternColor())
					: tfbm.getBodyColor().toString()+"_"+tfbm.getPattern().toString()+"_"+tfbm.getPatternColor().toString())));
		}
		return list;
	}
	
	/* Seit der 1.20.4 Veraltet. TODO Checken ob es so korrekt läuft
	public static List<PotionEffect> getBasePotion(PotionData pd, int pv) //pv PotionVariation, 1 Normal, 2 Splash, 3 Linger
	{
		PotionType pt = pd.getType();
		boolean ex = pd.isExtended();
		List<PotionEffect> list = new ArrayList<>();
		int amp = pd.isUpgraded() ? 1 : 0;
		int dur = 0;
		switch(pt)
		{
		case AWKWARD:
		case MUNDANE:
		case UNCRAFTABLE:
		case WATER:
		case THICK:
			break;
		case INVISIBILITY:
		case NIGHT_VISION:
		case FIRE_RESISTANCE:
		case WATER_BREATHING:
			if(amp == 0 && !ex && pv == 1) {dur = 3*60*20;}
			else if(amp == 0 && ex && pv == 1) {dur = 8*60*20;}
			
			else if(amp == 0 && !ex && pv == 2) {dur = 3*60*20;}
			else if(amp == 0 && ex && pv == 2) {dur = 8*60*20;}
			
			else if(amp == 0 && !ex && pv == 3) {dur = 45*20;}
			else if(amp == 0 && ex && pv == 3) {dur = 2*60*20;}
			
			else if(amp == 0 && !ex && pv == 4) {dur = 22*20;}
			else if(amp == 0 && ex && pv == 4) {dur = 60*20;}
			list.add(pt.getEffectType().createEffect(dur, amp));
			break;
		case INSTANT_DAMAGE:
		case INSTANT_HEAL:
			list.add(pt.getEffectType().createEffect(10, amp));
			break;
		case JUMP:
		case SPEED:
		case STRENGTH:
			if(amp == 0 && !ex && pv == 1) {dur = 3*60*20;}
			else if(amp == 0 && ex && pv == 1) {dur = 8*60*20;}
			else if(amp == 1 && !ex && pv == 1) {dur = 90*20;}
			
			else if(amp == 0 && !ex && pv == 2) {dur = 3*60*20;}
			else if(amp == 0 && ex && pv == 2) {dur = 8*60*20;}
			else if(amp == 1 && !ex && pv == 2) {dur = 90*20;}
			
			else if(amp == 0 && !ex && pv == 3) {dur = 45*20;}
			else if(amp == 0 && ex && pv == 3) {dur = 2*60*20;}
			else if(amp == 1 && !ex && pv == 3) {dur = 22*20;}
			
			else if(amp == 0 && !ex && pv == 4) {dur = 22*20;}
			else if(amp == 0 && ex && pv == 4) {dur = 60*20;}
			else if(amp == 1 && !ex && pv == 4) {dur = 11*20;}
			list.add(pt.getEffectType().createEffect(dur, amp));
			break;
		case POISON:
		case REGEN:
			if(amp == 0 && !ex && pv == 1) {dur = 90*20;}
			else if(amp == 0 && ex && pv == 1) {dur = 4*60*20;}
			else if(amp == 1 && !ex && pv == 1) {dur = 22*20;}
			
			else if(amp == 0 && !ex && pv == 2) {dur = 90*20;}
			else if(amp == 0 && ex && pv == 2) {dur = 4*60*20;}
			else if(amp == 1 && !ex && pv == 2) {dur = 22*20;}
			
			else if(amp == 0 && !ex && pv == 3) {dur = 45*20;}
			else if(amp == 0 && ex && pv == 3) {dur = 2*60*20;}
			else if(amp == 1 && !ex && pv == 3) {dur = 22*20;}
			
			else if(amp == 0 && !ex && pv == 3) {dur = 5*20;}
			else if(amp == 0 && ex && pv == 3) {dur = 11*20;}
			else if(amp == 1 && !ex && pv == 3) {dur = 2*20;}
			list.add(pt.getEffectType().createEffect(dur, amp));
			break;
		case SLOW_FALLING:
		case WEAKNESS:
			if(amp == 0 && !ex && pv == 1) {dur = 90*20;}
			else if(amp == 0 && ex && pv == 1) {dur = 4*60*20;}
			
			else if(amp == 0 && !ex && pv == 2) {dur = 90*20;}
			else if(amp == 0 && ex && pv == 2) {dur = 4*60*20;}
			
			else if(amp == 0 && !ex && pv == 3) {dur = 22*20;}
			else if(amp == 0 && ex && pv == 3) {dur = 60*20;}
			
			else if(amp == 0 && !ex && pv == 3) {dur = 11*20;}
			else if(amp == 0 && ex && pv == 3) {dur = 30*20;}
			list.add(pt.getEffectType().createEffect(dur, amp));
			break;
		case SLOWNESS:
			amp = pd.isUpgraded() ? 3 : 0;
			if(amp == 0 && !ex && pv == 1) {dur = 90*20;}
			else if(amp == 0 && ex && pv == 1) {dur = 4*60*20;}
			else if(amp == 3 && !ex && pv == 1) {dur = 20*20;}
			
			else if(amp == 0 && !ex && pv == 2) {dur = 90*20;}
			else if(amp == 0 && ex && pv == 2) {dur = 4*60*20;}
			else if(amp == 3 && !ex && pv == 2) {dur = 20*20;}
			
			else if(amp == 0 && !ex && pv == 3) {dur = 22*20;}
			else if(amp == 0 && ex && pv == 3) {dur = 60*20;}
			else if(amp == 3 && !ex && pv == 3) {dur = 5*20;}
			
			else if(amp == 0 && !ex && pv == 3) {dur = 11*20;}
			else if(amp == 0 && ex && pv == 3) {dur = 30*20;}
			else if(amp == 3 && !ex && pv == 3) {dur = 2*20;}
			list.add(pt.getEffectType().createEffect(dur, amp));
			break;
		case TURTLE_MASTER:
			amp = pd.isUpgraded() ? 5 : 3;
			if(amp == 3 && !ex && pv == 1) {dur = 20*20;}
			else if(amp == 3 && ex && pv == 1) {dur = 40*20;}
			else if(amp == 5 && !ex && pv == 1) {dur = 20*20;}
			
			else if(amp == 3 && !ex && pv == 2) {dur = 20*20;}
			else if(amp == 3 && ex && pv == 2) {dur = 40*20;}
			else if(amp == 5 && !ex && pv == 2) {dur = 20*20;}
			
			else if(amp == 3 && !ex && pv == 3) {dur = 5*20;}
			else if(amp == 3 && ex && pv == 3) {dur = 10*20;}
			else if(amp == 5 && !ex && pv == 3) {dur = 5*20;}
			
			else if(amp == 3 && !ex && pv == 4) {dur = 2*20;}
			else if(amp == 3 && ex && pv == 4) {dur = 5*20;}
			else if(amp == 5 && !ex && pv == 4) {dur = 2*20;}
			list.add(new PotionEffect(PotionEffectType.SLOW, dur, amp));
			amp = pd.isUpgraded() ? 3 : 2;
			if(amp == 2 && !ex && pv == 1) {dur = 20*20;}
			else if(amp == 2 && ex && pv == 1) {dur = 40*20;}
			else if(amp == 3 && !ex && pv == 1) {dur = 20*20;}
			
			else if(amp == 2 && !ex && pv == 2) {dur = 20*20;}
			else if(amp == 2 && ex && pv == 2) {dur = 40*20;}
			else if(amp == 3 && !ex && pv == 2) {dur = 20*20;}
			
			else if(amp == 2 && !ex && pv == 3) {dur = 5*20;}
			else if(amp == 2 && ex && pv == 3) {dur = 10*20;}
			else if(amp == 3 && !ex && pv == 3) {dur = 5*20;}
			
			else if(amp == 2 && !ex && pv == 4) {dur = 2*20;}
			else if(amp == 2 && ex && pv == 4) {dur = 5*20;}
			else if(amp == 3 && !ex && pv == 4) {dur = 2*20;}
			list.add(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, dur, amp));
			break;
		case LUCK:
			if(amp == 0 && !ex && pv == 1) {dur = 5*60*20;}
			
			else if(amp == 0 && !ex && pv == 2) {dur = 5*60*20;}
			
			else if(amp == 0 && !ex && pv == 3) {dur = 75*20;}
			
			else if(amp == 0 && !ex && pv == 4) {dur = 37*20;}
			list.add(pt.getEffectType().createEffect(dur, amp));
			break;
		}
		return list;
	}*/
	
	public static String getPotionColor(PotionEffect pe)
	{
		String color = "";
		if(pe.getType() == PotionEffectType.ABSORPTION || pe.getType() == PotionEffectType.CONDUIT_POWER
				|| pe.getType() == PotionEffectType.RESISTANCE || pe.getType() == PotionEffectType.DOLPHINS_GRACE
				|| pe.getType() == PotionEffectType.HASTE || pe.getType() == PotionEffectType.FIRE_RESISTANCE
				|| pe.getType() == PotionEffectType.INSTANT_HEALTH || pe.getType() == PotionEffectType.HEALTH_BOOST
				|| pe.getType() == PotionEffectType.HERO_OF_THE_VILLAGE || pe.getType() == PotionEffectType.STRENGTH
				|| pe.getType() == PotionEffectType.INVISIBILITY || pe.getType() == PotionEffectType.JUMP_BOOST
				|| pe.getType() == PotionEffectType.LUCK || pe.getType() == PotionEffectType.NIGHT_VISION
				|| pe.getType() == PotionEffectType.REGENERATION || pe.getType() == PotionEffectType.SATURATION
				|| pe.getType() == PotionEffectType.SLOW_FALLING || pe.getType() == PotionEffectType.SPEED
				|| pe.getType() == PotionEffectType.WATER_BREATHING)
		{
			color = "&9";
		} else if(pe.getType() == PotionEffectType.BAD_OMEN || pe.getType() == PotionEffectType.BLINDNESS
				|| pe.getType() == PotionEffectType.BLINDNESS || pe.getType() == PotionEffectType.DARKNESS
				|| pe.getType() == PotionEffectType.INSTANT_DAMAGE || pe.getType() == PotionEffectType.HUNGER
				|| pe.getType() == PotionEffectType.LEVITATION || pe.getType() == PotionEffectType.POISON
				|| pe.getType() == PotionEffectType.SLOWNESS || pe.getType() == PotionEffectType.MINING_FATIGUE
				|| pe.getType() == PotionEffectType.SLOW_FALLING || pe.getType() == PotionEffectType.UNLUCK
				|| pe.getType() == PotionEffectType.WEAKNESS || pe.getType() == PotionEffectType.WITHER)
		{
			color = "&c";
		} else if(pe.getType() == PotionEffectType.GLOWING)
		{
			color = "&7";
		}
		return color;
	}
	
	public static int getMaxDamage(Material material)
	{
		int damage = 0;
		switch(material)
		{
		case WOODEN_AXE: //Fallthrough
		case WOODEN_HOE:
		case WOODEN_PICKAXE:
		case WOODEN_SHOVEL:
		case WOODEN_SWORD:
			damage = 60;
			break;
		case LEATHER_BOOTS:
			damage = 65;
			break;
		case LEATHER_CHESTPLATE:
			damage = 80;
			break;
		case LEATHER_HELMET:
			damage = 55;
			break;
		case LEATHER_LEGGINGS:
			damage = 75;
			break;
		case STONE_AXE:
		case STONE_HOE:
		case STONE_PICKAXE:
		case STONE_SHOVEL:
		case STONE_SWORD:
			damage = 132;
			break;
		case CHAINMAIL_BOOTS:
			damage = 196;
			break;
		case CHAINMAIL_CHESTPLATE:
			damage = 241;
			break;
		case CHAINMAIL_HELMET:
			damage = 166;
			break;
		case CHAINMAIL_LEGGINGS:
			damage = 226;
			break;
		case GOLDEN_AXE:
		case GOLDEN_HOE:
		case GOLDEN_PICKAXE:
		case GOLDEN_SHOVEL:
		case GOLDEN_SWORD:
			damage = 33;
			break;
		case GOLDEN_BOOTS:
			damage = 91;
			break;
		case GOLDEN_CHESTPLATE:
			damage = 112;
			break;
		case GOLDEN_HELMET:
			damage = 77;
			break;
		case GOLDEN_LEGGINGS:
			damage = 105;
			break;
		case IRON_AXE:
		case IRON_HOE:
		case IRON_PICKAXE:
		case IRON_SHOVEL:
		case IRON_SWORD:
			damage = 251;
			break;
		case IRON_BOOTS:
			damage = 195;
			break;
		case IRON_CHESTPLATE:
			damage = 40;
			break;
		case IRON_HELMET:
			damage = 165;
			break;
		case IRON_LEGGINGS:
			damage = 225;
			break;
		case DIAMOND_AXE:
		case DIAMOND_HOE:
		case DIAMOND_PICKAXE:
		case DIAMOND_SHOVEL:
		case DIAMOND_SWORD:
			damage = 1562;
			break;
		case DIAMOND_BOOTS:
			damage = 429;
			break;
		case DIAMOND_CHESTPLATE:
			damage = 528;
			break;
		case DIAMOND_HELMET:
			damage = 363;
			break;
		case DIAMOND_LEGGINGS:
			damage = 495;
			break;
		case NETHERITE_AXE:
		case NETHERITE_HOE:
		case NETHERITE_PICKAXE:
		case NETHERITE_SHOVEL:
		case NETHERITE_SWORD:
			damage = 2031;
			break;
		case NETHERITE_BOOTS:
			damage = 482;
			break;
		case NETHERITE_CHESTPLATE:
			damage = 592;
			break;
		case NETHERITE_HELMET:
			damage = 408;
			break;
		case NETHERITE_LEGGINGS:
			damage = 556;
			break;
		case SHIELD:
			damage = 337;
			break;
		case TURTLE_HELMET:
			damage = 276;
			break;
		case TRIDENT:
			damage = 251;
			break;
		case FISHING_ROD:
			damage = 65;
			break;
		case CARROT_ON_A_STICK:
			damage = 26;
			break;
		case WARPED_FUNGUS_ON_A_STICK:
			damage = 100;
			break;
		case ELYTRA:
			damage = 432;
			break;
		case SHEARS:
			damage = 238;
			break;
		case BOW:
			damage = 385;
			break;
		case CROSSBOW:
			damage = 326;
			break;
		case FLINT_AND_STEEL:
			damage = 65;
			break;
		default:
			damage = 0;
			break;
		}
		return damage;
	}
	
	//thanks https://stackoverflow.com/questions/12967896/converting-integers-to-roman-numerals-java
	public static String IntegerToRomanNumeral(int input) 
	{
	    if (input < 1 || input > 3999)
	        return String.valueOf(input);
	    String s = "";
	    while (input >= 1000) {
	        s += "M";
	        input -= 1000;        }
	    while (input >= 900) {
	        s += "CM";
	        input -= 900;
	    }
	    while (input >= 500) {
	        s += "D";
	        input -= 500;
	    }
	    while (input >= 400) {
	        s += "CD";
	        input -= 400;
	    }
	    while (input >= 100) {
	        s += "C";
	        input -= 100;
	    }
	    while (input >= 90) {
	        s += "XC";
	        input -= 90;
	    }
	    while (input >= 50) {
	        s += "L";
	        input -= 50;
	    }
	    while (input >= 40) {
	        s += "XL";
	        input -= 40;
	    }
	    while (input >= 10) {
	        s += "X";
	        input -= 10;
	    }
	    while (input >= 9) {
	        s += "IX";
	        input -= 9;
	    }
	    while (input >= 5) {
	        s += "V";
	        input -= 5;
	    }
	    while (input >= 4) {
	        s += "IV";
	        input -= 4;
	    }
	    while (input >= 1) {
	        s += "I";
	        input -= 1;
	    }    
	    return s;
	}
	
	public static String getStringPlaceHolder(SignShop ssh, Player player, String text, String playername)
	{
		String s = text;
		if(text.contains("%owner%"))
		{
			if(ssh.getOwner() == null)
			{
				s = s.replace("%owner%", "/");
			} else
			{
				s = s.replace("%owner%", Utility.convertUUIDToName(ssh.getOwner().toString()) == null 
						? "/" : Utility.convertUUIDToName(ssh.getOwner().toString()));
			}
		}
		if(text.contains("%material%"))
		{
			s = s.replace("%material%", (ssh.getMaterial() == null ? "/" : ssh.getMaterial().toString()));
		}
		if(text.contains("%isonblacklist%"))
		{
			if(playername != null)
			{
				UUID uuid = Utility.convertNameToUUID(playername);
				s = s.replace("%isonblacklist%", 
						uuid == null ? "/" :
							getBoolean(plugin.getMysqlHandler().exist(MysqlHandler.Type.SHOPACCESSTYPE,
								"`player_uuid` = ? AND `sign_shop_id` = ? AND `listed_type` = ?",
								uuid.toString(), ssh.getId(), ListedType.BLACKLIST.toString()))
						);
			} else
			{
				s = s.replace("%isonblacklist%", "/");
			}
		}
		if(text.contains("%isonwhitelist%"))
		{
			if(playername != null)
			{
				UUID uuid = Utility.convertNameToUUID(playername);
				s = s.replace("%isonwhitelist%", 
						uuid == null ? "/" :
							getBoolean(plugin.getMysqlHandler().exist(MysqlHandler.Type.SHOPACCESSTYPE,
								"`player_uuid` = ? AND `sign_shop_id` = ? AND `listed_type` = ?",
								uuid.toString(), ssh.getId(), ListedType.WHITELIST.toString()))
						);
			} else
			{
				s = s.replace("%isonwhitelist%", "/");
			}
		}
		if(text.contains("%ismember%"))
		{
			if(playername != null)
			{
				UUID uuid = Utility.convertNameToUUID(playername);
				s = s.replace("%ismember%", 
						uuid == null ? "/" :
							getBoolean(plugin.getMysqlHandler().exist(MysqlHandler.Type.SHOPACCESSTYPE,
								"`player_uuid` = ? AND `sign_shop_id` = ? AND `listed_type` = ?",
								uuid.toString(), ssh.getId(), ListedType.MEMBER.toString()))
						);
			} else
			{
				s = s.replace("%ismember%", "/");
			}
		}
		if(text.contains("%isoncustom%"))
		{
			if(playername != null)
			{
				UUID uuid = Utility.convertNameToUUID(playername);
				s = s.replace("%isoncustom%", 
						uuid == null ? "/" :
							getBoolean(plugin.getMysqlHandler().exist(MysqlHandler.Type.SHOPACCESSTYPE,
								"`player_uuid` = ? AND `sign_shop_id` = ? AND `listed_type` = ?",
								uuid.toString(), ssh.getId(), ListedType.CUSTOM.toString()))
						);
			} else
			{
				s = s.replace("%isoncustom%", "/");
			}
		}
		if(text.contains("%id%"))
		{
			s = s.replace("%id%", String.valueOf(ssh.getId()));
		}
		if(text.contains("%subscribe%"))
		{
			s = s.replace("%subscribe%", getBoolean(plugin.getMysqlHandler().exist(MysqlHandler.Type.SUBSCRIBEDSHOP,
					"`player_uuid` = ? AND `sign_shop_id` = ?", player.getUniqueId().toString(), ssh.getId())));
		}
		if(text.contains("%numtext%"))
		{
			s = s.replace("%numtext%", "'"+ssh.getNumText()+"'");
		}
		if(text.contains("%player%"))
		{
			s = s.replace("%player%", player.getName());
		}
		if(text.contains("%displayname%"))
		{
			s = s.replace("%displayname%", ssh.getDisplayName() == null ? "/" : ssh.getDisplayName());
		}
		if(text.contains("%signshopname%"))
		{
			s = s.replace("%signshopname%", ssh.getSignShopName());
		}
		if(text.contains("%server%"))
		{
			s = s.replace("%server%", ssh.getServer());
		}
		if(text.contains("%world%"))
		{
			s = s.replace("%world%", ssh.getWorld());
		}
		if(text.contains("%x%"))
		{
			s = s.replace("%x%", String.valueOf(ssh.getX()));
		}
		if(text.contains("%y%"))
		{
			s = s.replace("%y%", String.valueOf(ssh.getY()));
		}
		if(text.contains("%z%"))
		{
			s = s.replace("%z%", String.valueOf(ssh.getZ()));
		}
		if(text.contains("%accountid%"))
		{
			s = s.replace("%accountid%", String.valueOf(ssh.getAccountId()));
		}
		if(text.contains("%storageid%"))
		{
			s = s.replace("%storageid%", ssh.getStorageID() == 0 ? "/" : String.valueOf(ssh.getStorageID()));
		}
		if(text.contains("%creationdate%"))
		{
			s = s.replace("%creationdate%", TimeHandler.getDateTime(ssh.getCreationDateTime()));
		}
		if(text.contains("%discountstart%"))
		{
			s = s.replace("%discountstart%", ssh.getDiscountStart() == 0 ? "/" : TimeHandler.getDateTime(ssh.getDiscountStart()));
		}
		if(text.contains("%discountend%"))
		{
			s = s.replace("%discountend%", ssh.getDiscountEnd() == 0 ? "/" :TimeHandler.getDateTime(ssh.getDiscountEnd()));
		}
		if(text.contains("%possiblebuy%"))
		{
			s = s.replace("%possiblebuy%", ssh.getPossibleBuy() < 0 ? "/" : String.valueOf(ssh.getPossibleBuy()));
		}
		if(text.contains("%possiblesell%"))
		{
			s = s.replace("%possiblesell%", ssh.getPossibleSell() < 0 ? "/" : String.valueOf(ssh.getPossibleSell()));
		}
		if(text.contains("%discountpossiblebuy%"))
		{
			s = s.replace("%discountpossiblebuy%", ssh.getDiscountPossibleBuy() < 0 ? "/" : String.valueOf(ssh.getDiscountPossibleBuy()));
		}
		if(text.contains("%discountpossiblesell%"))
		{
			s = s.replace("%discountpossiblesell%", ssh.getDiscountPossibleSell() < 0 ? "/" : String.valueOf(ssh.getDiscountPossibleSell()));
		}
		if(text.contains("%itemstoragecurrent%"))
		{
			s = s.replace("%itemstoragecurrent%", String.valueOf(ssh.getItemStorageCurrent()));
		}
		if(text.contains("%itemstoragetotal%"))
		{
			s = s.replace("%itemstoragetotal%", String.valueOf(ssh.getItemStorageTotal()));
		}
		if(text.contains("%buytoggle%"))
		{
			s = s.replace("%buytoggle%", getBoolean(ssh.canBuy()));
		}
		if(text.contains("%selltoggle%"))
		{
			s = s.replace("%selltoggle%", getBoolean(ssh.canSell()));
		}
		if(text.contains("%unlimitedbuy%"))
		{
			s = s.replace("%unlimitedbuy%", getBoolean(ssh.isUnlimitedBuy()));
		}
		if(text.contains("%unlimitedsell%"))
		{
			s = s.replace("%unlimitedsell%", getBoolean(ssh.isUnlimitedSell()));
		}
		if(text.contains("%glow%"))
		{
			s = s.replace("%glow%", getBoolean(ssh.isSignGlowing()));
		}
		if(text.contains("%listtype%"))
		{
			s = s.replace("%listtype%", 
					plugin.getYamlHandler().getLang().getString("AdminstrationFunctionHandler.ListedType."+ssh.getListedType().toString()));
		}
		if(text.contains("%hologram%"))
		{
			s = s.replace("%hologram%", getBoolean(ssh.isItemHologram()));
		}
		return ChatApi.tl(s);
	}
	
	private static String getStringPlaceHolderIFH(SignShop ssh, Player player, String text,
			Account ac, int dg, boolean useSI, boolean useSy, String ts, String ds, String playername)
	{
		int buyFrac = 0;
		if(ssh.getBuyAmount() != null)
		{
			buyFrac = String.valueOf(ssh.getBuyAmount().doubleValue()).split("\\.")[1].length();
		}
		int sellFrac = 0;
		if(ssh.getSellAmount() != null)
		{
			sellFrac = String.valueOf(ssh.getSellAmount().doubleValue()).split("\\.")[1].length();
		}
		int dbuyFrac = 0;
		if(ssh.getDiscountBuyAmount() != null)
		{
			dbuyFrac = String.valueOf(ssh.getDiscountBuyAmount().doubleValue()).split("\\.")[1].length();
		}
		int dsellFrac = 0;
		if(ssh.getDiscountSellAmount() != null)
		{
			dsellFrac = String.valueOf(ssh.getDiscountSellAmount().doubleValue()).split("\\.")[1].length();
		}
		boolean inDiscount = System.currentTimeMillis() >= ssh.getDiscountStart() && System.currentTimeMillis() < ssh.getDiscountEnd();
		String s = text;
		if(text.contains("%accountname%"))
		{
			s = s.replace("%accountname%", (ac == null || ac.getID() == 0) ? "/" : ac.getAccountName());
		}
		if(text.contains("%buyraw1%"))
		{
			s = s.replace("%buyraw1%", (ssh.getBuyAmount() == null || ssh.getBuyAmount() < 0.0) ? "/" : 
				plugin.getIFHEco().format(ssh.getBuyAmount(), ac.getCurrency(), dg, buyFrac, useSI, useSy, ts, ds));
		}
		if(text.contains("%sellraw1%"))
		{
			s = s.replace("%sellraw1%", (ssh.getSellAmount() == null || ssh.getSellAmount() < 0.0) ? "/" : 
				plugin.getIFHEco().format(ssh.getSellAmount(), ac.getCurrency(), dg, sellFrac, useSI, useSy, ts, ds));		
		}
		if(text.contains("%buy1%"))
		{
			if(!inDiscount)
			{
				s = s.replace("%buy1%", (ssh.getBuyAmount() == null || ssh.getBuyAmount() < 0.0) ? "/" : 
					plugin.getIFHEco().format(ssh.getBuyAmount(), ac.getCurrency(), dg, buyFrac, useSI, useSy, ts, ds));
			} else
			{
				String b = (ssh.getDiscountBuyAmount() == null || ssh.getDiscountBuyAmount() < 0.0)
						? (ssh.getBuyAmount() == null || ssh.getBuyAmount() < 0.0
							? "/"
							: plugin.getIFHEco().format(ssh.getBuyAmount(), ac.getCurrency(), dg, buyFrac, useSI, useSy, ts, ds)) 
						: plugin.getIFHEco().format(ssh.getDiscountBuyAmount(), ac.getCurrency(), dg, buyFrac, useSI, useSy, ts, ds);
				s = s.replace("%buy1%", b);
			}
		}
		if(text.contains("%buy8%"))
		{
			if(!inDiscount)
			{
				s = s.replace("%buy8%", (ssh.getBuyAmount() == null || ssh.getBuyAmount() < 0.0) ? "/" : 
					plugin.getIFHEco().format(ssh.getBuyAmount()*8, ac.getCurrency(), dg, buyFrac, useSI, useSy, ts, ds));
			} else
			{
				String b = (ssh.getDiscountBuyAmount() == null || ssh.getDiscountBuyAmount() < 0.0)
						? (ssh.getBuyAmount() == null || ssh.getBuyAmount() < 0.0
							? "/"
							: plugin.getIFHEco().format(ssh.getBuyAmount()*8, ac.getCurrency(), dg, buyFrac, useSI, useSy, ts, ds)) 
						: plugin.getIFHEco().format(ssh.getDiscountBuyAmount()*8, ac.getCurrency(), dg, buyFrac, useSI, useSy, ts, ds);
				s = s.replace("%buy8%", b);
			}
		}
		if(text.contains("%buy16%"))
		{
			if(!inDiscount)
			{
				s = s.replace("%buy16%", (ssh.getBuyAmount() == null || ssh.getBuyAmount() < 0.0) ? "/" : 
					plugin.getIFHEco().format(ssh.getBuyAmount()*16, ac.getCurrency(), dg, buyFrac, useSI, useSy, ts, ds));
			} else
			{
				String b = (ssh.getDiscountBuyAmount() == null || ssh.getDiscountBuyAmount() < 0.0)
						? (ssh.getBuyAmount() == null || ssh.getBuyAmount() < 0.0
							? "/"
							: plugin.getIFHEco().format(ssh.getBuyAmount()*16, ac.getCurrency(), dg, buyFrac, useSI, useSy, ts, ds)) 
						: plugin.getIFHEco().format(ssh.getDiscountBuyAmount()*16, ac.getCurrency(), dg, buyFrac, useSI, useSy, ts, ds);
				s = s.replace("%buy16%", b);
			}
		}
		if(text.contains("%buy32%"))
		{
			if(!inDiscount)
			{
				s = s.replace("%buy32%", (ssh.getBuyAmount() == null || ssh.getBuyAmount() < 0.0) ? "/" : 
					plugin.getIFHEco().format(ssh.getBuyAmount()*32, ac.getCurrency(), dg, buyFrac, useSI, useSy, ts, ds));
			} else
			{
				String b = (ssh.getDiscountBuyAmount() == null || ssh.getDiscountBuyAmount() < 0.0)
						? (ssh.getBuyAmount() == null || ssh.getBuyAmount() < 0.0
							? "/"
							: plugin.getIFHEco().format(ssh.getBuyAmount()*32, ac.getCurrency(), dg, buyFrac, useSI, useSy, ts, ds)) 
						: plugin.getIFHEco().format(ssh.getDiscountBuyAmount()*32, ac.getCurrency(), dg, buyFrac, useSI, useSy, ts, ds);
				s = s.replace("%buy32%", b);
			}
		}
		if(text.contains("%buy64%"))
		{
			if(!inDiscount)
			{
				s = s.replace("%buy64%", (ssh.getBuyAmount() == null || ssh.getBuyAmount() < 0.0) ? "/" : 
					plugin.getIFHEco().format(ssh.getBuyAmount()*64, ac.getCurrency(), dg, buyFrac, useSI, useSy, ts, ds));
			} else
			{
				String b = (ssh.getDiscountBuyAmount() == null || ssh.getDiscountBuyAmount() < 0.0)
						? (ssh.getBuyAmount() == null || ssh.getBuyAmount() < 0.0
							? "/"
							: plugin.getIFHEco().format(ssh.getBuyAmount()*64, ac.getCurrency(), dg, buyFrac, useSI, useSy, ts, ds)) 
						: plugin.getIFHEco().format(ssh.getDiscountBuyAmount()*64, ac.getCurrency(), dg, buyFrac, useSI, useSy, ts, ds);
				s = s.replace("%buy64%", b);
			}
		}
		if(text.contains("%buy576%"))
		{
			if(!inDiscount)
			{
				s = s.replace("%buy576%", (ssh.getBuyAmount() == null || ssh.getBuyAmount() < 0.0) ? "/" : 
					plugin.getIFHEco().format(ssh.getBuyAmount()*576, ac.getCurrency(), dg, buyFrac, useSI, useSy, ts, ds));
			} else
			{
				String b = (ssh.getDiscountBuyAmount() == null || ssh.getDiscountBuyAmount() < 0.0)
						? (ssh.getBuyAmount() == null || ssh.getBuyAmount() < 0.0
							? "/"
							: plugin.getIFHEco().format(ssh.getBuyAmount()*576, ac.getCurrency(), dg, buyFrac, useSI, useSy, ts, ds)) 
						: plugin.getIFHEco().format(ssh.getDiscountBuyAmount()*576, ac.getCurrency(), dg, buyFrac, useSI, useSy, ts, ds);
				s = s.replace("%buy576%", b);
			}			
		}
		if(text.contains("%buy1728%"))
		{
			if(!inDiscount)
			{
				s = s.replace("%buy1728%", (ssh.getBuyAmount() == null || ssh.getBuyAmount() < 0.0) ? "/" : 
					plugin.getIFHEco().format(ssh.getBuyAmount()*1728, ac.getCurrency(), dg, buyFrac, useSI, useSy, ts, ds));
			} else
			{
				String b = (ssh.getDiscountBuyAmount() == null || ssh.getDiscountBuyAmount() < 0.0)
						? (ssh.getBuyAmount() == null || ssh.getBuyAmount() < 0.0
							? "/"
							: plugin.getIFHEco().format(ssh.getBuyAmount()*1728, ac.getCurrency(), dg, buyFrac, useSI, useSy, ts, ds)) 
						: plugin.getIFHEco().format(ssh.getDiscountBuyAmount()*1728, ac.getCurrency(), dg, buyFrac, useSI, useSy, ts, ds);
				s = s.replace("%buy1728%", b);
			}			
		}
		if(text.contains("%buy2304%"))
		{
			if(!inDiscount)
			{
				s = s.replace("%buy2304%", (ssh.getBuyAmount() == null || ssh.getBuyAmount() < 0.0) ? "/" : 
					plugin.getIFHEco().format(ssh.getBuyAmount()*2304, ac.getCurrency(), dg, buyFrac, useSI, useSy, ts, ds));
			} else
			{
				String b = (ssh.getDiscountBuyAmount() == null || ssh.getDiscountBuyAmount() < 0.0)
						? (ssh.getBuyAmount() == null || ssh.getBuyAmount() < 0.0
							? "/"
							: plugin.getIFHEco().format(ssh.getBuyAmount()*2304, ac.getCurrency(), dg, buyFrac, useSI, useSy, ts, ds)) 
						: plugin.getIFHEco().format(ssh.getDiscountBuyAmount()*2304, ac.getCurrency(), dg, buyFrac, useSI, useSy, ts, ds);
				s = s.replace("%buy2304%", b);
			}			
		}
		if(text.contains("%sell1%"))
		{
			if(!inDiscount)
			{
				s = s.replace("%sell1%", (ssh.getSellAmount() == null || ssh.getSellAmount() < 0.0) ? "/" : 
					plugin.getIFHEco().format(ssh.getSellAmount(), ac.getCurrency(), dg, sellFrac, useSI, useSy, ts, ds));
			} else
			{
				String v = (ssh.getDiscountSellAmount() == null || ssh.getDiscountSellAmount() < 0.0)
						? (ssh.getSellAmount() == null || ssh.getSellAmount() < 0.0
							? "/"
							: plugin.getIFHEco().format(ssh.getSellAmount(), ac.getCurrency(), dg, sellFrac, useSI, useSy, ts, ds)) 
						: plugin.getIFHEco().format(ssh.getDiscountSellAmount(), ac.getCurrency(), dg, dsellFrac, useSI, useSy, ts, ds);
				s = s.replace("%sell1%", v);
			}			
		}
		if(text.contains("%sell8%"))
		{
			if(!inDiscount)
			{
				s = s.replace("%sell8%", (ssh.getSellAmount() == null || ssh.getSellAmount() < 0.0) ? "/" : 
					plugin.getIFHEco().format(ssh.getSellAmount()*8, ac.getCurrency(), dg, sellFrac, useSI, useSy, ts, ds));
			} else
			{
				String v = (ssh.getDiscountSellAmount() == null || ssh.getDiscountSellAmount() < 0.0)
						? (ssh.getSellAmount() == null || ssh.getSellAmount() < 0.0
							? "/"
							: plugin.getIFHEco().format(ssh.getSellAmount()*8, ac.getCurrency(), dg, sellFrac, useSI, useSy, ts, ds)) 
						: plugin.getIFHEco().format(ssh.getDiscountSellAmount()*8, ac.getCurrency(), dg, dsellFrac, useSI, useSy, ts, ds);
				s = s.replace("%sell8%", v);
			}			
		}
		if(text.contains("%sell16%"))
		{
			if(!inDiscount)
			{
				s = s.replace("%sell16%", (ssh.getSellAmount() == null || ssh.getSellAmount() < 0.0) ? "/" : 
					plugin.getIFHEco().format(ssh.getSellAmount()*16, ac.getCurrency(), dg, sellFrac, useSI, useSy, ts, ds));
			} else
			{
				String v = (ssh.getDiscountSellAmount() == null || ssh.getDiscountSellAmount() < 0.0)
						? (ssh.getSellAmount() == null || ssh.getSellAmount() < 0.0
							? "/"
							: plugin.getIFHEco().format(ssh.getSellAmount()*16, ac.getCurrency(), dg, sellFrac, useSI, useSy, ts, ds)) 
						: plugin.getIFHEco().format(ssh.getDiscountSellAmount()*16, ac.getCurrency(), dg, dsellFrac, useSI, useSy, ts, ds);
				s = s.replace("%sell16%", v);
			}			
		}
		if(text.contains("%sell32%"))
		{
			if(!inDiscount)
			{
				s = s.replace("%sell32%", (ssh.getSellAmount() == null || ssh.getSellAmount() < 0.0) ? "/" : 
					plugin.getIFHEco().format(ssh.getSellAmount()*32, ac.getCurrency(), dg, sellFrac, useSI, useSy, ts, ds));
			} else
			{
				String v = (ssh.getDiscountSellAmount() == null || ssh.getDiscountSellAmount() < 0.0)
						? (ssh.getSellAmount() == null || ssh.getSellAmount() < 0.0
							? "/"
							: plugin.getIFHEco().format(ssh.getSellAmount()*32, ac.getCurrency(), dg, sellFrac, useSI, useSy, ts, ds)) 
						: plugin.getIFHEco().format(ssh.getDiscountSellAmount()*32, ac.getCurrency(), dg, sellFrac, useSI, useSy, ts, ds);
				s = s.replace("%sell32%", v);
			}			
		}
		if(text.contains("%sell64%"))
		{
			if(!inDiscount)
			{
				s = s.replace("%sell64%", (ssh.getSellAmount() == null || ssh.getSellAmount() < 0.0) ? "/" : 
					plugin.getIFHEco().format(ssh.getSellAmount()*64, ac.getCurrency(), dg, sellFrac, useSI, useSy, ts, ds));
			} else
			{
				String v = (ssh.getDiscountSellAmount() == null || ssh.getDiscountSellAmount() < 0.0)
						? (ssh.getSellAmount() == null || ssh.getSellAmount() < 0.0
							? "/"
							: plugin.getIFHEco().format(ssh.getSellAmount()*64, ac.getCurrency(), dg, sellFrac, useSI, useSy, ts, ds)) 
						: plugin.getIFHEco().format(ssh.getDiscountSellAmount()*64, ac.getCurrency(), dg, sellFrac, useSI, useSy, ts, ds);
				s = s.replace("%sell64%", v);
			}			
		}
		if(text.contains("%sell576%"))
		{
			if(!inDiscount)
			{
				s = s.replace("%sell576%", (ssh.getSellAmount() == null || ssh.getSellAmount() < 0.0) ? "/" : 
					plugin.getIFHEco().format(ssh.getSellAmount()*576, ac.getCurrency(), dg, sellFrac, useSI, useSy, ts, ds));
			} else
			{
				String v = (ssh.getDiscountSellAmount() == null || ssh.getDiscountSellAmount() < 0.0)
						? (ssh.getSellAmount() == null || ssh.getSellAmount() < 0.0
							? "/"
							: plugin.getIFHEco().format(ssh.getSellAmount()*576, ac.getCurrency(), dg, sellFrac, useSI, useSy, ts, ds)) 
						: plugin.getIFHEco().format(ssh.getDiscountSellAmount()*576, ac.getCurrency(), dg, dsellFrac, useSI, useSy, ts, ds);
				s = s.replace("%sell576%", v);
			}			
		}
		if(text.contains("%sell1728%"))
		{
			if(!inDiscount)
			{
				s = s.replace("%sell1728%", (ssh.getSellAmount() == null || ssh.getSellAmount() < 0.0) ? "/" : 
					plugin.getIFHEco().format(ssh.getSellAmount()*1728, ac.getCurrency(), dg, sellFrac, useSI, useSy, ts, ds));
			} else
			{
				String v = (ssh.getDiscountSellAmount() == null || ssh.getDiscountSellAmount() < 0.0)
						? (ssh.getSellAmount() == null || ssh.getSellAmount() < 0.0
							? "/"
							: plugin.getIFHEco().format(ssh.getSellAmount()*1728, ac.getCurrency(), dg, sellFrac, useSI, useSy, ts, ds)) 
						: plugin.getIFHEco().format(ssh.getDiscountSellAmount()*1728, ac.getCurrency(), dg, dsellFrac, useSI, useSy, ts, ds);
				s = s.replace("%sell1728%", v);
			}			
		}
		if(text.contains("%sell2304%"))
		{
			if(!inDiscount)
			{
				s = s.replace("%sell2304%", (ssh.getSellAmount() == null || ssh.getSellAmount() < 0.0) ? "/" : 
					plugin.getIFHEco().format(ssh.getSellAmount()*2304, ac.getCurrency(), dg, sellFrac, useSI, useSy, ts, ds));
			} else
			{
				String v = (ssh.getDiscountSellAmount() == null || ssh.getDiscountSellAmount() < 0.0)
						? (ssh.getSellAmount() == null || ssh.getSellAmount() < 0.0
							? "/"
							: plugin.getIFHEco().format(ssh.getSellAmount()*2304, ac.getCurrency(), dg, sellFrac, useSI, useSy, ts, ds)) 
						: plugin.getIFHEco().format(ssh.getDiscountSellAmount()*2304, ac.getCurrency(), dg, dsellFrac, useSI, useSy, ts, ds);
				s = s.replace("%sell2304%", v);
			}			
		}
		if(text.contains("%discountbuy1%"))
		{
			s = s.replace("%discountbuy1%", (ssh.getDiscountBuyAmount() == null || ssh.getDiscountBuyAmount() < 0.0) ? "/" : 
				plugin.getIFHEco().format(ssh.getDiscountBuyAmount(), ac.getCurrency(), dg, dbuyFrac, useSI, useSy, ts, ds));
		}
		if(text.contains("%discountbuy8%"))
		{
			s = s.replace("%discountbuy8%", (ssh.getDiscountBuyAmount() == null || ssh.getDiscountBuyAmount() < 0.0) ? "/" : 
				plugin.getIFHEco().format(ssh.getDiscountBuyAmount()*8, ac.getCurrency(), dg, dbuyFrac, useSI, useSy, ts, ds));
		}
		if(text.contains("%discountbuy16%"))
		{
			s = s.replace("%discountbuy16%", (ssh.getDiscountBuyAmount() == null || ssh.getDiscountBuyAmount() < 0.0) ? "/" : 
				plugin.getIFHEco().format(ssh.getDiscountBuyAmount()*16, ac.getCurrency(), dg, dbuyFrac, useSI, useSy, ts, ds));
		}
		if(text.contains("%discountbuy32%"))
		{
			s = s.replace("%discountbuy32%", (ssh.getDiscountBuyAmount() == null || ssh.getDiscountBuyAmount() < 0.0) ? "/" : 
				plugin.getIFHEco().format(ssh.getDiscountBuyAmount()*32, ac.getCurrency(), dg, dbuyFrac, useSI, useSy, ts, ds));
		}
		if(text.contains("%discountbuy64%"))
		{
			s = s.replace("%discountbuy64%", (ssh.getDiscountBuyAmount() == null || ssh.getDiscountBuyAmount() < 0.0) ? "/" : 
				plugin.getIFHEco().format(ssh.getDiscountBuyAmount()*64, ac.getCurrency(), dg, dbuyFrac, useSI, useSy, ts, ds));
		}
		if(text.contains("%discountbuy576%"))
		{
			s = s.replace("%discountbuy576%", (ssh.getDiscountBuyAmount() == null || ssh.getDiscountBuyAmount() < 0.0) ? "/" : 
				plugin.getIFHEco().format(ssh.getDiscountBuyAmount()*576, ac.getCurrency(), dg, dbuyFrac, useSI, useSy, ts, ds));
		}
		if(text.contains("%discountbuy1728%"))
		{
			s = s.replace("%discountbuy1728%", (ssh.getDiscountBuyAmount() == null || ssh.getDiscountBuyAmount() < 0.0) ? "/" : 
				plugin.getIFHEco().format(ssh.getDiscountBuyAmount()*1728, ac.getCurrency(), dg, dbuyFrac, useSI, useSy, ts, ds));
		}
		if(text.contains("%discountbuy2304%"))
		{
			s = s.replace("%discountbuy2304%", (ssh.getDiscountBuyAmount() == null || ssh.getDiscountBuyAmount() < 0.0) ? "/" : 
				plugin.getIFHEco().format(ssh.getDiscountBuyAmount()*2304, ac.getCurrency(), dg, dbuyFrac, useSI, useSy, ts, ds));
		}
		if(text.contains("%discountsell1%"))
		{
			s = s.replace("%discountsell1%", (ssh.getDiscountSellAmount() == null || ssh.getDiscountSellAmount() < 0.0) ? "/" : 
				plugin.getIFHEco().format(ssh.getDiscountSellAmount(), ac.getCurrency(), dg, dsellFrac, useSI, useSy, ts, ds));
		}
		if(text.contains("%discountsell8%"))
		{
			s = s.replace("%discountsell8%", (ssh.getDiscountSellAmount() == null || ssh.getDiscountSellAmount() < 0.0) ? "/" : 
				plugin.getIFHEco().format(ssh.getDiscountSellAmount()*8, ac.getCurrency(), dg, dsellFrac, useSI, useSy, ts, ds));
		}
		if(text.contains("%discountsell16%"))
		{
			s = s.replace("%discountsell16%", (ssh.getDiscountSellAmount() == null || ssh.getDiscountSellAmount() < 0.0) ? "/" : 
				plugin.getIFHEco().format(ssh.getDiscountSellAmount()*16, ac.getCurrency(), dg, dsellFrac, useSI, useSy, ts, ds));
		}
		if(text.contains("%discountsell32%"))
		{
			s = s.replace("%discountsell32%", (ssh.getDiscountSellAmount() == null || ssh.getDiscountSellAmount() < 0.0) ? "/" : 
				plugin.getIFHEco().format(ssh.getDiscountSellAmount()*32, ac.getCurrency(), dg, dsellFrac, useSI, useSy, ts, ds));
		}
		if(text.contains("%discountsell64%"))
		{
			s = s.replace("%discountsell64%", (ssh.getDiscountSellAmount() == null || ssh.getDiscountSellAmount() < 0.0) ? "/" : 
				plugin.getIFHEco().format(ssh.getDiscountSellAmount()*64, ac.getCurrency(), dg, dsellFrac, useSI, useSy, ts, ds));
		}
		if(text.contains("%discountsell576%"))
		{
			s = s.replace("%discountsell576%", (ssh.getDiscountSellAmount() == null || ssh.getDiscountSellAmount() < 0.0) ? "/" : 
				plugin.getIFHEco().format(ssh.getDiscountSellAmount()*576, ac.getCurrency(), dg, dsellFrac, useSI, useSy, ts, ds));
		}
		if(text.contains("%discountsell1728%"))
		{
			s = s.replace("%discountsell1728%", (ssh.getDiscountSellAmount() == null || ssh.getDiscountSellAmount() < 0.0) ? "/" : 
				plugin.getIFHEco().format(ssh.getDiscountSellAmount()*1728, ac.getCurrency(), dg, dsellFrac, useSI, useSy, ts, ds));
		}
		if(text.contains("%discountsell2304%"))
		{
			s = s.replace("%discountsell2304%", (ssh.getDiscountSellAmount() == null || ssh.getDiscountSellAmount() < 0.0) ? "/" : 
				plugin.getIFHEco().format(ssh.getDiscountSellAmount()*2304, ac.getCurrency(), dg, dsellFrac, useSI, useSy, ts, ds));
		}
		return ChatApi.tl(s);
	}
	
	private static String getStringPlaceHolderVault(SignShop ssh, Player player, String text, String playername)
	{
		boolean inDiscount = System.currentTimeMillis() >= ssh.getDiscountStart() && System.currentTimeMillis() < ssh.getDiscountEnd();
		String s = text;
		if(text.contains("%accountname%"))
		{
			String n = Utility.convertUUIDToName(ssh.getOwner().toString());
			s = s.replace("%accountname%", n != null ? n : "/");
		}
		if(text.contains("%buyraw1%"))
		{
			s = s.replace("%buyraw1%", (ssh.getBuyAmount() == null || ssh.getBuyAmount() < 0.0) ? "/" : 
				String.valueOf(ssh.getBuyAmount())+" "+ plugin.getVaultEco().currencyNamePlural());
		}
		if(text.contains("%sellraw1%"))
		{
			s = s.replace("%sellraw1%", (ssh.getSellAmount() == null || ssh.getSellAmount() < 0.0) ? "/" : 
				String.valueOf(ssh.getSellAmount())+" "+ plugin.getVaultEco().currencyNamePlural());
		}
		if(text.contains("%buy1%"))
		{
			if(!inDiscount)
			{
				s = s.replace("%buy1%", (ssh.getBuyAmount() == null || ssh.getBuyAmount() < 0.0) ? "/" : 
					String.valueOf(ssh.getBuyAmount())+" "+ plugin.getVaultEco().currencyNamePlural());
			} else
			{
				String b = (ssh.getDiscountBuyAmount() == null || ssh.getDiscountBuyAmount() < 0.0)
						? (ssh.getBuyAmount() == null || ssh.getBuyAmount() < 0.0
							? "/"
							: String.valueOf(ssh.getBuyAmount())+" "+ plugin.getVaultEco().currencyNamePlural()) 
						: String.valueOf(ssh.getBuyAmount())+" "+ plugin.getVaultEco().currencyNamePlural();
				s = s.replace("%buy1%", b);
			}
		}
		if(text.contains("%buy8%"))
		{
			if(!inDiscount)
			{
				s = s.replace("%buy8%", (ssh.getBuyAmount() == null || ssh.getBuyAmount() < 0.0) ? "/" : 
					String.valueOf(ssh.getBuyAmount()*8)+" "+ plugin.getVaultEco().currencyNamePlural());
			} else
			{
				String b = (ssh.getDiscountBuyAmount() == null || ssh.getDiscountBuyAmount() < 0.0)
						? (ssh.getBuyAmount() == null || ssh.getBuyAmount() < 0.0
							? "/"
							: String.valueOf(ssh.getBuyAmount()*8)+" "+ plugin.getVaultEco().currencyNamePlural()) 
						: String.valueOf(ssh.getBuyAmount()*8)+" "+ plugin.getVaultEco().currencyNamePlural();
				s = s.replace("%buy8%", b);
			}
		}
		if(text.contains("%buy16%"))
		{
			if(!inDiscount)
			{
				s = s.replace("%buy16%", (ssh.getBuyAmount() == null || ssh.getBuyAmount() < 0.0) ? "/" : 
					String.valueOf(ssh.getBuyAmount()*16)+" "+ plugin.getVaultEco().currencyNamePlural());
			} else
			{
				String b = (ssh.getDiscountBuyAmount() == null || ssh.getDiscountBuyAmount() < 0.0)
						? (ssh.getBuyAmount() == null || ssh.getBuyAmount() < 0.0
							? "/"
							: String.valueOf(ssh.getBuyAmount()*16)+" "+ plugin.getVaultEco().currencyNamePlural()) 
						: String.valueOf(ssh.getDiscountBuyAmount()*16)+" "+ plugin.getVaultEco().currencyNamePlural();
				s = s.replace("%buy16%", b);
			}
		}
		if(text.contains("%buy32%"))
		{
			if(!inDiscount)
			{
				s = s.replace("%buy32%", (ssh.getBuyAmount() == null || ssh.getBuyAmount() < 0.0) ? "/" : 
					String.valueOf(ssh.getBuyAmount()*32)+" "+ plugin.getVaultEco().currencyNamePlural());
			} else
			{
				String b = (ssh.getDiscountBuyAmount() == null || ssh.getDiscountBuyAmount() < 0.0)
						? (ssh.getBuyAmount() == null || ssh.getBuyAmount() < 0.0
							? "/"
							: String.valueOf(ssh.getBuyAmount()*32)+" "+ plugin.getVaultEco().currencyNamePlural()) 
						: String.valueOf(ssh.getBuyAmount()*32)+" "+ plugin.getVaultEco().currencyNamePlural();
				s = s.replace("%buy32%", b);
			}
		}
		if(text.contains("%buy64%"))
		{
			if(!inDiscount)
			{
				s = s.replace("%buy64%", (ssh.getBuyAmount() == null || ssh.getBuyAmount() < 0.0) ? "/" : 
					String.valueOf(ssh.getBuyAmount()*64)+" "+ plugin.getVaultEco().currencyNamePlural());
			} else
			{
				String b = (ssh.getDiscountBuyAmount() == null || ssh.getDiscountBuyAmount() < 0.0)
						? (ssh.getBuyAmount() == null || ssh.getBuyAmount() < 0.0
							? "/"
							: String.valueOf(ssh.getBuyAmount()*64)+" "+ plugin.getVaultEco().currencyNamePlural()) 
						: String.valueOf(ssh.getDiscountBuyAmount()*64)+" "+ plugin.getVaultEco().currencyNamePlural();
				s = s.replace("%buy64%", b);
			}
		}
		if(text.contains("%buy576%"))
		{
			if(!inDiscount)
			{
				s = s.replace("%buy576%", (ssh.getBuyAmount() == null || ssh.getBuyAmount() < 0.0) ? "/" : 
					String.valueOf(ssh.getBuyAmount()*576)+" "+ plugin.getVaultEco().currencyNamePlural());
			} else
			{
				String b = (ssh.getDiscountBuyAmount() == null || ssh.getDiscountBuyAmount() < 0.0)
						? (ssh.getBuyAmount() == null || ssh.getBuyAmount() < 0.0
							? "/"
							: String.valueOf(ssh.getBuyAmount()*576)+" "+ plugin.getVaultEco().currencyNamePlural()) 
						: String.valueOf(ssh.getDiscountBuyAmount()*576)+" "+ plugin.getVaultEco().currencyNamePlural();
				s = s.replace("%buy576%", b);
			}			
		}
		if(text.contains("%buy1728%"))
		{
			if(!inDiscount)
			{
				s = s.replace("%buy1728%", (ssh.getBuyAmount() == null || ssh.getBuyAmount() < 0.0) ? "/" : 
					String.valueOf(ssh.getBuyAmount()*1728)+" "+ plugin.getVaultEco().currencyNamePlural());
			} else
			{
				String b = (ssh.getDiscountBuyAmount() == null || ssh.getDiscountBuyAmount() < 0.0)
						? (ssh.getBuyAmount() == null || ssh.getBuyAmount() < 0.0
							? "/"
							: String.valueOf(ssh.getBuyAmount()*1728)+" "+ plugin.getVaultEco().currencyNamePlural()) 
						: String.valueOf(ssh.getDiscountBuyAmount()*1728)+" "+ plugin.getVaultEco().currencyNamePlural();
				s = s.replace("%buy1728%", b);
			}			
		}
		if(text.contains("%buy2304%"))
		{
			if(!inDiscount)
			{
				s = s.replace("%buy2304%", (ssh.getBuyAmount() == null || ssh.getBuyAmount() < 0.0) ? "/" : 
					String.valueOf(ssh.getBuyAmount()*2304)+" "+ plugin.getVaultEco().currencyNamePlural());
			} else
			{
				String b = (ssh.getDiscountBuyAmount() == null || ssh.getDiscountBuyAmount() < 0.0)
						? (ssh.getBuyAmount() == null || ssh.getBuyAmount() < 0.0
							? "/"
							: String.valueOf(ssh.getBuyAmount()*2304)+" "+ plugin.getVaultEco().currencyNamePlural()) 
						: String.valueOf(ssh.getDiscountBuyAmount()*2304)+" "+ plugin.getVaultEco().currencyNamePlural();
				s = s.replace("%buy2304%", b);
			}			
		}
		if(text.contains("%sell1%"))
		{
			if(!inDiscount)
			{
				s = s.replace("%sell1%", (ssh.getSellAmount() == null || ssh.getSellAmount() < 0.0) ? "/" : 
					String.valueOf(ssh.getSellAmount())+" "+ plugin.getVaultEco().currencyNamePlural());
			} else
			{
				String v = (ssh.getDiscountSellAmount() == null || ssh.getDiscountSellAmount() < 0.0)
						? (ssh.getSellAmount() == null || ssh.getSellAmount() < 0.0
							? "/"
							: String.valueOf(ssh.getSellAmount())+" "+ plugin.getVaultEco().currencyNamePlural()) 
						: String.valueOf(ssh.getDiscountSellAmount())+" "+ plugin.getVaultEco().currencyNamePlural();
				s = s.replace("%sell1%", v);
			}			
		}
		if(text.contains("%sell8%"))
		{
			if(!inDiscount)
			{
				s = s.replace("%sell8%", (ssh.getSellAmount() == null || ssh.getSellAmount() < 0.0) ? "/" : 
					String.valueOf(ssh.getSellAmount()*8)+" "+ plugin.getVaultEco().currencyNamePlural());
			} else
			{
				String v = (ssh.getDiscountSellAmount() == null || ssh.getDiscountSellAmount() < 0.0)
						? (ssh.getSellAmount() == null || ssh.getSellAmount() < 0.0
							? "/"
							: String.valueOf(ssh.getSellAmount()*8)+" "+ plugin.getVaultEco().currencyNamePlural()) 
						: String.valueOf(ssh.getDiscountSellAmount()*8)+" "+ plugin.getVaultEco().currencyNamePlural();
				s = s.replace("%sell8%", v);
			}			
		}
		if(text.contains("%sell16%"))
		{
			if(!inDiscount)
			{
				s = s.replace("%sell16%", (ssh.getSellAmount() == null || ssh.getSellAmount() < 0.0) ? "/" : 
					String.valueOf(ssh.getSellAmount()*16)+" "+ plugin.getVaultEco().currencyNamePlural());
			} else
			{
				String v = (ssh.getDiscountSellAmount() == null || ssh.getDiscountSellAmount() < 0.0)
						? (ssh.getSellAmount() == null || ssh.getSellAmount() < 0.0
							? "/"
							: String.valueOf(ssh.getSellAmount()*16)+" "+ plugin.getVaultEco().currencyNamePlural()) 
						: String.valueOf(ssh.getDiscountSellAmount()*16)+" "+ plugin.getVaultEco().currencyNamePlural();
				s = s.replace("%sell16%", v);
			}			
		}
		if(text.contains("%sell32%"))
		{
			if(!inDiscount)
			{
				s = s.replace("%sell32%", (ssh.getSellAmount() == null || ssh.getSellAmount() < 0.0) ? "/" : 
					String.valueOf(ssh.getSellAmount()*32)+" "+ plugin.getVaultEco().currencyNamePlural());
			} else
			{
				String v = (ssh.getDiscountSellAmount() == null || ssh.getDiscountSellAmount() < 0.0)
						? (ssh.getSellAmount() == null || ssh.getSellAmount() < 0.0
							? "/"
							: String.valueOf(ssh.getSellAmount()*32)+" "+ plugin.getVaultEco().currencyNamePlural()) 
						: String.valueOf(ssh.getDiscountSellAmount()*32)+" "+ plugin.getVaultEco().currencyNamePlural();
				s = s.replace("%sell32%", v);
			}			
		}
		if(text.contains("%sell64%"))
		{
			if(!inDiscount)
			{
				s = s.replace("%sell64%", (ssh.getSellAmount() == null || ssh.getSellAmount() < 0.0) ? "/" : 
					String.valueOf(ssh.getSellAmount()*64)+" "+ plugin.getVaultEco().currencyNamePlural());
			} else
			{
				String v = (ssh.getDiscountSellAmount() == null || ssh.getDiscountSellAmount() < 0.0)
						? (ssh.getSellAmount() == null || ssh.getSellAmount() < 0.0
							? "/"
							: String.valueOf(ssh.getSellAmount()*64)+" "+ plugin.getVaultEco().currencyNamePlural()) 
						: String.valueOf(ssh.getDiscountSellAmount()*64)+" "+ plugin.getVaultEco().currencyNamePlural();
				s = s.replace("%sell64%", v);
			}			
		}
		if(text.contains("%sell576%"))
		{
			if(!inDiscount)
			{
				s = s.replace("%sell576%", (ssh.getSellAmount() == null || ssh.getSellAmount() < 0.0) ? "/" : 
					String.valueOf(ssh.getSellAmount()*576)+" "+ plugin.getVaultEco().currencyNamePlural());
			} else
			{
				String v = (ssh.getDiscountSellAmount() == null || ssh.getDiscountSellAmount() < 0.0)
						? (ssh.getSellAmount() == null || ssh.getSellAmount() < 0.0
							? "/"
							: String.valueOf(ssh.getSellAmount()*576)+" "+ plugin.getVaultEco().currencyNamePlural()) 
						: String.valueOf(ssh.getDiscountSellAmount()*576)+" "+ plugin.getVaultEco().currencyNamePlural();
				s = s.replace("%sell576%", v);
			}			
		}
		if(text.contains("%sell1728%"))
		{
			if(!inDiscount)
			{
				s = s.replace("%sell1728%", (ssh.getSellAmount() == null || ssh.getSellAmount() < 0.0) ? "/" : 
					String.valueOf(ssh.getSellAmount()*1728)+" "+ plugin.getVaultEco().currencyNamePlural());
			} else
			{
				String v = (ssh.getDiscountSellAmount() == null || ssh.getDiscountSellAmount() < 0.0)
						? (ssh.getSellAmount() == null || ssh.getSellAmount() < 0.0
							? "/"
							: String.valueOf(ssh.getSellAmount()*1728)+" "+ plugin.getVaultEco().currencyNamePlural()) 
						: String.valueOf(ssh.getDiscountSellAmount()*1728)+" "+ plugin.getVaultEco().currencyNamePlural();
				s = s.replace("%sell1728%", v);
			}			
		}
		if(text.contains("%sell2304%"))
		{
			if(!inDiscount)
			{
				s = s.replace("%sell2304%", (ssh.getSellAmount() == null || ssh.getSellAmount() < 0.0) ? "/" : 
					String.valueOf(ssh.getSellAmount()*2304)+" "+ plugin.getVaultEco().currencyNamePlural());
			} else
			{
				String v = (ssh.getDiscountSellAmount() == null || ssh.getDiscountSellAmount() < 0.0)
						? (ssh.getSellAmount() == null || ssh.getSellAmount() < 0.0
							? "/"
							: String.valueOf(ssh.getSellAmount()*2304)+" "+ plugin.getVaultEco().currencyNamePlural()) 
						: String.valueOf(ssh.getDiscountSellAmount()*2304)+" "+ plugin.getVaultEco().currencyNamePlural();
				s = s.replace("%sell2304%", v);
			}			
		}
		if(text.contains("%discountbuy1%"))
		{
			s = s.replace("%discountbuy1%", (ssh.getDiscountBuyAmount() == null || ssh.getDiscountBuyAmount() < 0.0) ? "/" : 
				String.valueOf(ssh.getDiscountBuyAmount())+" "+ plugin.getVaultEco().currencyNamePlural());
		}
		if(text.contains("%discountbuy8%"))
		{
			s = s.replace("%discountbuy8%", (ssh.getDiscountBuyAmount() == null || ssh.getDiscountBuyAmount() < 0.0) ? "/" : 
				String.valueOf(ssh.getDiscountBuyAmount()*8)+" "+ plugin.getVaultEco().currencyNamePlural());
		}
		if(text.contains("%discountbuy16%"))
		{
			s = s.replace("%discountbuy16%", (ssh.getDiscountBuyAmount() == null || ssh.getDiscountBuyAmount() < 0.0) ? "/" : 
				String.valueOf(ssh.getDiscountBuyAmount()*16)+" "+ plugin.getVaultEco().currencyNamePlural());
		}
		if(text.contains("%discountbuy32%"))
		{
			s = s.replace("%discountbuy32%", (ssh.getDiscountBuyAmount() == null || ssh.getDiscountBuyAmount() < 0.0) ? "/" : 
				String.valueOf(ssh.getDiscountBuyAmount()*32)+" "+ plugin.getVaultEco().currencyNamePlural());
		}
		if(text.contains("%discountbuy64%"))
		{
			s = s.replace("%discountbuy64%", (ssh.getDiscountBuyAmount() == null || ssh.getDiscountBuyAmount() < 0.0) ? "/" : 
				String.valueOf(ssh.getDiscountBuyAmount()*64)+" "+ plugin.getVaultEco().currencyNamePlural());
		}
		if(text.contains("%discountbuy576%"))
		{
			s = s.replace("%discountbuy576%", (ssh.getDiscountBuyAmount() == null || ssh.getDiscountBuyAmount() < 0.0) ? "/" : 
				String.valueOf(ssh.getDiscountBuyAmount()*576)+" "+ plugin.getVaultEco().currencyNamePlural());
		}
		if(text.contains("%discountbuy1728%"))
		{
			s = s.replace("%discountbuy1728%", (ssh.getDiscountBuyAmount() == null || ssh.getDiscountBuyAmount() < 0.0) ? "/" : 
				String.valueOf(ssh.getDiscountBuyAmount()*1728)+" "+ plugin.getVaultEco().currencyNamePlural());
		}
		if(text.contains("%discountbuy2304%"))
		{
			s = s.replace("%discountbuy2304%", (ssh.getDiscountBuyAmount() == null || ssh.getDiscountBuyAmount() < 0.0) ? "/" : 
				String.valueOf(ssh.getDiscountBuyAmount()*2304)+" "+ plugin.getVaultEco().currencyNamePlural());
		}
		if(text.contains("%discountsell1%"))
		{
			s = s.replace("%discountsell1%", (ssh.getDiscountSellAmount() == null || ssh.getDiscountSellAmount() < 0.0) ? "/" : 
				String.valueOf(ssh.getDiscountSellAmount())+" "+ plugin.getVaultEco().currencyNamePlural());
		}
		if(text.contains("%discountsell8%"))
		{
			s = s.replace("%discountsell8%", (ssh.getDiscountSellAmount() == null || ssh.getDiscountSellAmount() < 0.0) ? "/" : 
				String.valueOf(ssh.getDiscountSellAmount()*8)+" "+ plugin.getVaultEco().currencyNamePlural());
		}
		if(text.contains("%discountsell16%"))
		{
			s = s.replace("%discountsell16%", (ssh.getDiscountSellAmount() == null || ssh.getDiscountSellAmount() < 0.0) ? "/" : 
				String.valueOf(ssh.getDiscountSellAmount()*16)+" "+ plugin.getVaultEco().currencyNamePlural());
		}
		if(text.contains("%discountsell32%"))
		{
			s = s.replace("%discountsell32%", (ssh.getDiscountSellAmount() == null || ssh.getDiscountSellAmount() < 0.0) ? "/" : 
				String.valueOf(ssh.getDiscountSellAmount()*32)+" "+ plugin.getVaultEco().currencyNamePlural());
		}
		if(text.contains("%discountsell64%"))
		{
			s = s.replace("%discountsell64%", (ssh.getDiscountSellAmount() == null || ssh.getDiscountSellAmount() < 0.0) ? "/" : 
				String.valueOf(ssh.getDiscountSellAmount()*64)+" "+ plugin.getVaultEco().currencyNamePlural());
		}
		if(text.contains("%discountsell576%"))
		{
			s = s.replace("%discountsell576%", (ssh.getDiscountSellAmount() == null || ssh.getDiscountSellAmount() < 0.0) ? "/" : 
				String.valueOf(ssh.getDiscountSellAmount()*576)+" "+ plugin.getVaultEco().currencyNamePlural());
		}
		if(text.contains("%discountsell1728%"))
		{
			s = s.replace("%discountsell1728%", (ssh.getDiscountSellAmount() == null || ssh.getDiscountSellAmount() < 0.0) ? "/" : 
				String.valueOf(ssh.getDiscountSellAmount()*1728)+" "+ plugin.getVaultEco().currencyNamePlural());
		}
		if(text.contains("%discountsell2304%"))
		{
			s = s.replace("%discountsell2304%", (ssh.getDiscountSellAmount() == null || ssh.getDiscountSellAmount() < 0.0) ? "/" : 
				String.valueOf(ssh.getDiscountSellAmount()*2304)+" "+ plugin.getVaultEco().currencyNamePlural());
		}
		return ChatApi.tl(s);
	}
	
	private static String getBoolean(boolean boo)
	{
		return boo ? plugin.getYamlHandler().getLang().getString("IsTrue") : plugin.getYamlHandler().getLang().getString("IsFalse");
	}
	
	private static ClickFunction[] getClickFunction(YamlConfiguration y, String pathBase)
	{
		ArrayList<ClickFunction> ctar = new ArrayList<>();
		List<ClickType> list = new ArrayList<ClickType>(EnumSet.allOf(ClickType.class));
		for(ClickType ct : list)
		{
			if(pathBase == null)
			{
				if(y.get("ClickFunction."+ct.toString()) == null)
				{
					continue;
				}
				ClickFunctionType cft = null;
				try
				{
					cft = ClickFunctionType.valueOf(y.getString("ClickFunction."+ct.toString()));
				} catch(Exception e)
				{
					continue;
				}
				ctar.add(new ClickFunction(ct, cft));
			} else
			{
				if(y.get(pathBase+".ClickFunction."+ct.toString()) == null)
				{
					continue;
				}
				ClickFunctionType cft = null;
				try
				{
					cft = ClickFunctionType.valueOf(y.getString(pathBase+".ClickFunction."+ct.toString()));
				} catch(Exception e)
				{
					continue;
				}
				ctar.add(new ClickFunction(ct, cft));
			}
		}
		return ctar.toArray(new ClickFunction[ctar.size()]);
	}
	
	public static String getSpawnEggType(Material mat)
	{
		String s = "";
		switch(mat)
		{
		default: break;
		case ALLAY_SPAWN_EGG:
		case AXOLOTL_SPAWN_EGG:
		case BAT_SPAWN_EGG:
		case BEE_SPAWN_EGG:
		case BLAZE_SPAWN_EGG:
		case CAT_SPAWN_EGG:
		case CAVE_SPIDER_SPAWN_EGG:
		case CHICKEN_SPAWN_EGG:
		case COD_SPAWN_EGG:
		case COW_SPAWN_EGG:
		case CREEPER_SPAWN_EGG:
		case DOLPHIN_SPAWN_EGG:
		case DONKEY_SPAWN_EGG:
		case DROWNED_SPAWN_EGG:
		case ELDER_GUARDIAN_SPAWN_EGG:
		case ENDERMAN_SPAWN_EGG:
		case ENDERMITE_SPAWN_EGG:
		case EVOKER_SPAWN_EGG:
		case FOX_SPAWN_EGG:
		case FROG_SPAWN_EGG:
		case FROGSPAWN:
		case GHAST_SPAWN_EGG:
		case GLOW_SQUID_SPAWN_EGG:
		case GOAT_SPAWN_EGG:
		case GUARDIAN_SPAWN_EGG:
		case HOGLIN_SPAWN_EGG:
		case HORSE_SPAWN_EGG:
		case HUSK_SPAWN_EGG:
		case LLAMA_SPAWN_EGG:
		case MAGMA_CUBE_SPAWN_EGG:
		case MOOSHROOM_SPAWN_EGG:
		case MULE_SPAWN_EGG:
		case OCELOT_SPAWN_EGG:
		case PANDA_SPAWN_EGG:
		case PARROT_SPAWN_EGG:
		case PHANTOM_SPAWN_EGG:
		case PIG_SPAWN_EGG:
		case PIGLIN_BRUTE_SPAWN_EGG:
		case PIGLIN_SPAWN_EGG:
		case PILLAGER_SPAWN_EGG:
		case POLAR_BEAR_SPAWN_EGG:
		case PUFFERFISH_SPAWN_EGG:
		case RABBIT_SPAWN_EGG:
		case RAVAGER_SPAWN_EGG:
		case SALMON_SPAWN_EGG:
		case SHEEP_SPAWN_EGG:
		case SHULKER_SPAWN_EGG:
		case SILVERFISH_SPAWN_EGG:
		case SKELETON_HORSE_SPAWN_EGG:
		case SKELETON_SPAWN_EGG:
		case SLIME_SPAWN_EGG:
		case SPIDER_SPAWN_EGG:
		case SQUID_SPAWN_EGG:
		case STRAY_SPAWN_EGG:
		case STRIDER_SPAWN_EGG:
		case TADPOLE_SPAWN_EGG:
		case TRADER_LLAMA_SPAWN_EGG:
		case TROPICAL_FISH_SPAWN_EGG:
		case TURTLE_SPAWN_EGG:
		case VEX_SPAWN_EGG:
		case VILLAGER_SPAWN_EGG:
		case VINDICATOR_SPAWN_EGG:
		case WANDERING_TRADER_SPAWN_EGG:
		case WARDEN_SPAWN_EGG:
		case WITCH_SPAWN_EGG:
		case WITHER_SKELETON_SPAWN_EGG:
		case WOLF_SPAWN_EGG:
		case ZOGLIN_SPAWN_EGG:
		case ZOMBIE_HORSE_SPAWN_EGG:
		case ZOMBIE_SPAWN_EGG:
		case ZOMBIE_VILLAGER_SPAWN_EGG:
		case ZOMBIFIED_PIGLIN_SPAWN_EGG:
			s = (plugin.getEnumTl() != null 
				? SaLE.getPlugin().getEnumTl().getLocalization(mat)
				: mat.toString()); break;
		}
		return s;
	}
}