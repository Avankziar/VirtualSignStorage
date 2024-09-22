package me.avankziar.vss.spigot.handler.gui;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.avankziar.ifh.general.economy.account.AccountCategory;
import me.avankziar.ifh.general.economy.account.AccountManagementType;
import me.avankziar.ifh.general.economy.action.EconomyAction;
import me.avankziar.ifh.general.economy.action.OrdererType;
import me.avankziar.ifh.general.economy.currency.CurrencyType;
import me.avankziar.ifh.spigot.economy.account.Account;
import me.avankziar.ifh.spigot.economy.currency.EconomyCurrency;
import me.avankziar.vss.general.ChatApi;
import me.avankziar.vss.general.database.MysqlType;
import me.avankziar.vss.general.objects.ListedType;
import me.avankziar.vss.general.objects.PlayerData;
import me.avankziar.vss.general.objects.SignStorage;
import me.avankziar.vss.general.objects.StorageAccessType;
import me.avankziar.vss.spigot.VSS;
import me.avankziar.vss.spigot.assistance.MatchApi;
import me.avankziar.vss.spigot.assistance.Utility;
import me.avankziar.vss.spigot.gui.objects.ClickFunctionType;
import me.avankziar.vss.spigot.gui.objects.GuiType;
import me.avankziar.vss.spigot.gui.objects.SettingsLevel;
import me.avankziar.vss.spigot.handler.GuiHandler;
import me.avankziar.vss.spigot.handler.SignHandler;
import me.avankziar.vss.spigot.modifiervalueentry.Bypass;
import me.avankziar.vss.spigot.modifiervalueentry.ModifierValueEntry;
import net.milkbowl.vault.economy.EconomyResponse;

public class AdminstrationFunctionHandler
{
	private static VSS plugin = VSS.getPlugin();
	
	public static void doClickFunktion(GuiType guiType, ClickFunctionType cft, Player player, SignStorage sst,
			Inventory openInv, SettingsLevel settingsLevel, UUID otheruuid)
	{
		switch(cft)
		{
		default: return;
		case ADMINISTRATION_ADDSTORAGE_1: addStorage(player, sst, 1, openInv, settingsLevel); break;
		case ADMINISTRATION_ADDSTORAGE_8: addStorage(player, sst, 8, openInv, settingsLevel); break;
		case ADMINISTRATION_ADDSTORAGE_16: addStorage(player, sst, 16, openInv, settingsLevel); break;
		case ADMINISTRATION_ADDSTORAGE_32: addStorage(player, sst, 32, openInv, settingsLevel); break;
		case ADMINISTRATION_ADDSTORAGE_64: addStorage(player, sst, 64, openInv, settingsLevel); break;
		case ADMINISTRATION_ADDSTORAGE_576: addStorage(player, sst, 576, openInv, settingsLevel); break;
		case ADMINISTRATION_ADDSTORAGE_1728: addStorage(player, sst, 1728, openInv, settingsLevel); break;
		case ADMINISTRATION_ADDSTORAGE_3456: addStorage(player, sst, 3456, openInv, settingsLevel); break;
		case ADMINISTRATION_ADDSTORAGE_6912: addStorage(player, sst, 6912, openInv, settingsLevel); break;
		case ADMINISTRATION_DELETE_ALL: deleteAll(player, sst); break;
		case ADMINISTRATION_DELETE_WITHOUT_ITEMS_IN_STORAGE: deleteSoft(player, sst); break;
		case ADMINISTRATION_ITEM_CLEAR: clearItem(player, sst); break;
		case ADMINISTRATION_NUMPAD_0: numpad(player, sst, "0", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_NUMPAD_1: numpad(player, sst, "1", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_NUMPAD_2: numpad(player, sst, "2", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_NUMPAD_3: numpad(player, sst, "3", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_NUMPAD_4: numpad(player, sst, "4", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_NUMPAD_5: numpad(player, sst, "5", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_NUMPAD_6: numpad(player, sst, "6", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_NUMPAD_7: numpad(player, sst, "7", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_NUMPAD_8: numpad(player, sst, "8", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_NUMPAD_9: numpad(player, sst, "9", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_NUMPAD_COLON: numpad(player, sst, ":", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_NUMPAD_DECIMAL: numpad(player, sst, ".", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_NUMPAD_CLEAR: setNumpadClear(player, sst, guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_NUMPAD_CANCEL: cancelNumpad(player, sst, openInv, settingsLevel); break;
		case ADMINISTRATION_NUMPAD_REMOVEONCE: numpadRemoveOnce(player, sst, guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_SETACCOUNT_DEFAULT: setAccountDefault(player, sst, openInv, settingsLevel); break;
		case ADMINISTRATION_SETACCOUNT_OPEN_NUMPAD: openNumpad(player, sst, GuiType.NUMPAD_ACCOUNT, openInv, settingsLevel); break;
		case ADMINISTRATION_SETACCOUNT_TAKEOVER: takeOver(player, sst, GuiType.NUMPAD_ACCOUNT, openInv, settingsLevel); break;
		case ADMINISTRATION_SETTINGSLEVEL_SETTO_ADVANCED: switchSettingsLevel(player, sst, null, openInv, SettingsLevel.ADVANCED); break;
		case ADMINISTRATION_SETTINGSLEVEL_SETTO_BASE: switchSettingsLevel(player, sst, null, openInv, SettingsLevel.BASE); break;
		case ADMINISTRATION_SETTINGSLEVEL_SETTO_EXPERT: switchSettingsLevel(player, sst, null, openInv, SettingsLevel.EXPERT); break;
		case ADMINISTRATION_SETTINGSLEVEL_SETTO_MASTER: switchSettingsLevel(player, sst, null, openInv, SettingsLevel.MASTER); break;
		case ADMINISTRATION_UNLIMITED_TOGGLE: setToggle(player, sst, "U", openInv, settingsLevel); break;
		case ADMINISTRATION_SETGLOWING: setGlowing(player, sst, openInv, settingsLevel, true); break;
		case ADMINISTRATION_SETUNGLOWING: setGlowing(player, sst, openInv, settingsLevel, false); break;
		case ADMINISTRATION_SETITEMHOLOGRAM_ACTIVE: setItemHover(player, sst, openInv, settingsLevel, true); break;
		case ADMINISTRATION_SETITEMHOLOGRAM_DEACTIVE: setItemHover(player, sst, openInv, settingsLevel, false); break;
		case ADMINISTRATION_SETLISTEDTYPE_WHITELIST: switchListType(player, sst, null, openInv, settingsLevel, ListedType.WHITELIST); break;
		case ADMINISTRATION_SETLISTEDTYPE_MEMBER: switchListType(player, sst, null, openInv, settingsLevel, ListedType.MEMBER); break;
		case ADMINISTRATION_KEYBOARD_0: keyboard(player, sst, "0", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_1: keyboard(player, sst, "1", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_2: keyboard(player, sst, "2", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_3: keyboard(player, sst, "3", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_4: keyboard(player, sst, "4", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_5: keyboard(player, sst, "5", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_6: keyboard(player, sst, "6", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_7: keyboard(player, sst, "7", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_8: keyboard(player, sst, "8", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_9: keyboard(player, sst, "9", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_A_CAPITAL: keyboard(player, sst, "A", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_A_SMALL: keyboard(player, sst, "a", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_B_CAPITAL: keyboard(player, sst, "B", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_B_SMALL: keyboard(player, sst, "b", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_C_CAPITAL: keyboard(player, sst, "C", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_C_SMALL: keyboard(player, sst, "c", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_D_CAPITAL: keyboard(player, sst, "D", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_D_SMALL: keyboard(player, sst, "d", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_E_CAPITAL: keyboard(player, sst, "E", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_E_SMALL: keyboard(player, sst, "e", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_F_CAPITAL: keyboard(player, sst, "F", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_F_SMALL: keyboard(player, sst, "f", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_G_CAPITAL: keyboard(player, sst, "G", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_G_SMALL: keyboard(player, sst, "g", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_H_CAPITAL: keyboard(player, sst, "H", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_H_SMALL: keyboard(player, sst, "h", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_I_CAPITAL: keyboard(player, sst, "I", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_I_SMALL: keyboard(player, sst, "i", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_J_CAPITAL: keyboard(player, sst, "J", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_J_SMALL: keyboard(player, sst, "j", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_K_CAPITAL: keyboard(player, sst, "K", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_K_SMALL: keyboard(player, sst, "k", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_L_CAPITAL: keyboard(player, sst, "L", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_L_SMALL: keyboard(player, sst, "l", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_M_CAPITAL: keyboard(player, sst, "M", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_M_SMALL: keyboard(player, sst, "m", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_N_CAPITAL: keyboard(player, sst, "N", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_N_SMALL: keyboard(player, sst, "n", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_O_CAPITAL: keyboard(player, sst, "O", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_O_SMALL: keyboard(player, sst, "o", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_P_CAPITAL: keyboard(player, sst, "P", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_P_SMALL: keyboard(player, sst, "p", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_Q_CAPITAL: keyboard(player, sst, "Q", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_Q_SMALL: keyboard(player, sst, "q", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_R_CAPITAL: keyboard(player, sst, "R", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_R_SMALL: keyboard(player, sst, "r", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_S_CAPITAL: keyboard(player, sst, "S", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_S_SMALL: keyboard(player, sst, "s", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_T_CAPITAL: keyboard(player, sst, "T", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_T_SMALL: keyboard(player, sst, "t", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_U_CAPITAL: keyboard(player, sst, "U", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_U_SMALL: keyboard(player, sst, "u", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_V_CAPITAL: keyboard(player, sst, "V", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_V_SMALL: keyboard(player, sst, "v", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_W_CAPITAL: keyboard(player, sst, "W", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_W_SMALL: keyboard(player, sst, "w", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_X_CAPITAL: keyboard(player, sst, "X", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_X_SMALL: keyboard(player, sst, "x", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_Y_CAPITAL: keyboard(player, sst, "Y", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_Y_SMALL: keyboard(player, sst, "y", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_Z_CAPITAL: keyboard(player, sst, "Z", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_Z_SMALL: keyboard(player, sst, "z", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD__: keyboard(player, sst, "_", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_REMOVEONCE: keyboardRemoveOnce(player, sst, guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_CLEAR: setKeyboardClear(player, sst, guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_CANCEL: cancelKeyboard(player, sst, openInv, settingsLevel); break;
		case ADMINISTRATION_SETSIGNSHOPNAME_OPENKEYBOARD: openKeyboard(player, sst, GuiType.KEYBOARD_SIGNSTORAGENAME, openInv, settingsLevel); break;
		case ADMINISTRATION_SETSIGNSHOPNAME_TAKEOVER: takeOver(player, sst, GuiType.KEYBOARD_SIGNSTORAGENAME, openInv, settingsLevel); break;
		case ADMINISTRATION_ADDLISTEDTYPE_PLAYER_OPENKEYBOARD_WHITELIST: openKeyboard(player, sst, GuiType.KEYBOARD_WHITELIST, openInv, settingsLevel); break;
		case ADMINISTRATION_ADDLISTEDTYPE_PLAYER_OPENKEYBOARD_MEMBER: openKeyboard(player, sst, GuiType.KEYBOARD_MEMBER, openInv, settingsLevel); break;
		case ADMINISTRATION_ADDLISTEDTYPE_PLAYER_WHITELIST_WORLD: addPlayerToList(player, sst, guiType, openInv, settingsLevel, ListedType.WHITELIST, otheruuid, false, true); break;
		case ADMINISTRATION_ADDLISTEDTYPE_PLAYER_WHITELIST_REMOVE: addPlayerToList(player, sst, guiType, openInv, settingsLevel, ListedType.WHITELIST, otheruuid, true, false); break;
		case ADMINISTRATION_ADDLISTEDTYPE_PLAYER_WHITELIST_REMOVE_WORLD: addPlayerToList(player, sst, guiType, openInv, settingsLevel, ListedType.WHITELIST, otheruuid, true, true); break;
		case ADMINISTRATION_ADDLISTEDTYPE_PLAYER_MEMBER: addPlayerToList(player, sst, guiType, openInv, settingsLevel, ListedType.MEMBER, otheruuid, false, false); break;
		case ADMINISTRATION_ADDLISTEDTYPE_PLAYER_MEMBER_WORLD: addPlayerToList(player, sst, guiType, openInv, settingsLevel, ListedType.MEMBER, otheruuid, false, true); break;
		case ADMINISTRATION_ADDLISTEDTYPE_PLAYER_MEMBER_REMOVE: addPlayerToList(player, sst, guiType, openInv, settingsLevel, ListedType.MEMBER, otheruuid, true, false); break;
		case ADMINISTRATION_ADDLISTEDTYPE_PLAYER_MEMBER_REMOVE_WORLD: addPlayerToList(player, sst, guiType, openInv, settingsLevel, ListedType.MEMBER, otheruuid, true, true); break;
		case ADMINISTRATION_LISTEDTYPE_PLAYER_OPENLIST_WHITELIST: sendPlayerOnList(player, sst, ListedType.WHITELIST); break;
		case ADMINISTRATION_LISTEDTYPE_PLAYER_OPENLIST_MEMBER: sendPlayerOnList(player, sst, ListedType.MEMBER); break;
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
	
	private static boolean isTooMuchShop(Player player, SignStorage sst)
	{
		if(!SignHandler.isOwner(sst, player.getUniqueId()) && !SignHandler.isBypassToggle(player.getUniqueId()))
		{
			return false;
		}
		int signShopAmount = plugin.getMysqlHandler().getCount(MysqlType.SIGNSTORAGE, "`player_uuid` = ?", player.getUniqueId().toString());
		int maxSignShopAmount = ModifierValueEntry.getResult(player, Bypass.Counter.SHOP_CREATION_AMOUNT_);
		if(signShopAmount > maxSignShopAmount)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("SignChangeListener.AlreadyHaveMaximalSignShop")
					.replace("%actual%", String.valueOf(signShopAmount))
					.replace("%max%", String.valueOf(maxSignShopAmount))
					));
			return true;
		}
		return false;
	}
	
	private static void addStorage(Player player, SignStorage sst, long amount, Inventory inv, SettingsLevel settingsLevel)
	{
		if(isTooMuchShop(player, sst))
		{
			return;
		}
		List<String> costPerOne = plugin.getYamlHandler().getConfig().getStringList("SignShop.CostToAdd1Storage");
		long maxStorage = sst.getItemStorageTotal();
		long maxPossibleStorage = (long) ModifierValueEntry.getResult(player, Bypass.Counter.SHOP_ITEMSTORAGE_AMOUNT_);
		if(maxStorage >= maxPossibleStorage)
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("AdminstrationFunctionHandler.AddStorage.TooManyAlreadyAsStorage")));
			return;
		}
		long ca = amount;
		if(maxPossibleStorage-maxStorage < amount)
		{
			ca = maxPossibleStorage - maxStorage;
		}
		boolean boo = false;
		if(plugin.getIFHEco() != null)
		{
			boo = addStorageIFH(player, sst, costPerOne, amount, ca);
		} else
		{
			boo = addStorageVault(player, sst, costPerOne, amount, ca);
		}
		if(!boo)
		{
			return;
		}
		sst.setItemStorageTotal(sst.getItemStorageTotal()+ca);
		plugin.getMysqlHandler().updateData(MysqlType.SIGNSTORAGE, sst, "`id` = ?", sst.getId());
		GuiHandler.openAdministration(sst, player, settingsLevel, inv, false);
	}
	
	private static boolean addStorageIFH(Player player, SignStorage sst, List<String> costPerOne, long amount, long ca)
	{
		LinkedHashMap<EconomyCurrency, Double> moneymap = new LinkedHashMap<>();
		for(String t : costPerOne)
		{
			String[] split = t.split(";");
			if(split.length != 2)
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("FileError")
						.replace("%file%", "config.yml | split.length != 2")));
				continue;
			}
			EconomyCurrency ec = plugin.getIFHEco().getCurrency(split[0]);
			if(ec == null)
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("FileError")
						.replace("%file%", "config.yml | EconomyCurrency == null")));
				continue;
			}
			if(!MatchApi.isDouble(split[1]))
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("FileError")
						.replace("%file%", "config.yml | "+split[1]+" != Double")));
				continue;
			}
			double d = Double.parseDouble(split[1]);
			if(plugin.getModifier() != null)
			{
				d = plugin.getModifier().getResult(player.getUniqueId(), d, Bypass.Counter.COST_ADDING_STORAGE.getModification());
			}
			if(plugin.getIFHEco().getDefaultAccount(player.getUniqueId(), AccountCategory.SHOP, ec) == null
					&& plugin.getIFHEco().getDefaultAccount(player.getUniqueId(), AccountCategory.MAIN, ec) == null)
			{
				player.sendMessage(ChatApi.tl(
						plugin.getYamlHandler().getLang().getString("AdminstrationFunctionHandler.AddStorage.YouDontHaveAccountToWithdraw")));
				return false;
			}
			double dd = d*ca;
			Account from = plugin.getIFHEco().getDefaultAccount(player.getUniqueId(), AccountCategory.SHOP, ec);
			if(from == null)
			{
				from = plugin.getIFHEco().getDefaultAccount(player.getUniqueId(), AccountCategory.MAIN, ec);
				if(from == null)
				{
					player.sendMessage(ChatApi.tl(
							plugin.getYamlHandler().getLang().getString("AdminstrationFunctionHandler.AddStorage.YouDontHaveAccountToWithdraw")));
					return false;
				}
			}
			if(!plugin.getIFHEco().canManageAccount(from, player.getUniqueId(), AccountManagementType.CAN_WITHDRAW))
			{
				player.sendMessage(ChatApi.tl(
						plugin.getYamlHandler().getLang().getString("AdminstrationFunctionHandler.AddStorage.YouCannotWithdraw")));
				return false;
			}
			if(from.getBalance() < dd)
			{
				player.sendMessage(ChatApi.tl(
						plugin.getYamlHandler().getLang().getString("AdminstrationFunctionHandler.AddStorage.NoEnoughMoney")));
				return false;
			}
			moneymap.put(ec, d);
		}
		String category = plugin.getYamlHandler().getLang().getString("Economy.AddStorage.Category");
		String comment = plugin.getYamlHandler().getLang().getString("Economy.AddStorage.Comment")
				.replace("%past%", String.valueOf(sst.getItemStorageTotal()))
				.replace("%now%", String.valueOf(sst.getItemStorageTotal()+amount))
				.replace("%amount%", String.valueOf(amount))
				.replace("%name%", sst.getSignStorageName());
		for(Entry<EconomyCurrency, Double> e : moneymap.entrySet())
		{
			EconomyCurrency ec = e.getKey();
			double d = e.getValue()*ca;
			Account from = plugin.getIFHEco().getDefaultAccount(player.getUniqueId(), AccountCategory.SHOP, ec);
			if(from == null)
			{
				from = plugin.getIFHEco().getDefaultAccount(player.getUniqueId(), AccountCategory.MAIN, ec);
				if(from == null)
				{
					player.sendMessage(ChatApi.tl(
							plugin.getYamlHandler().getLang().getString("AdminstrationFunctionHandler.AddStorage.YouDontHaveAccountToWithdraw")));
					return false;
				}
			}
			Account to = plugin.getIFHEco().getDefaultAccount(player.getUniqueId(), AccountCategory.VOID, ec);
			EconomyAction ea = plugin.getIFHEco().transaction(from, to, d, OrdererType.PLAYER, player.getUniqueId().toString(), category, comment);
			ArrayList<String> list = new ArrayList<>();
			if(to != null)
			{
				ea = plugin.getIFHEco().transaction(from, to, d, OrdererType.PLAYER, player.getUniqueId().toString(), category, comment);
				String wformat = plugin.getIFHEco().format(ea.getWithDrawAmount(), from.getCurrency());
				for(String s : plugin.getYamlHandler().getLang().getStringList("AdminstrationFunctionHandler.AddStorage.Transaction"))
				{
					String a = s.replace("%fromaccount%", from.getAccountName())
					.replace("%toaccount%", to.getAccountName())
					.replace("%formatwithdraw%", wformat)
					.replace("%category%", category != null ? category : "/")
					.replace("%comment%", comment != null ? comment : "/");
					list.add(a);
				}
			} else
			{
				ea = plugin.getIFHEco().withdraw(from, d, OrdererType.PLAYER, player.getUniqueId().toString(), category, comment);
				String wformat = plugin.getIFHEco().format(ea.getWithDrawAmount(), from.getCurrency());
				for(String s : plugin.getYamlHandler().getLang().getStringList("AdminstrationFunctionHandler.AddStorage.Withdraw"))
				{
					String a = s.replace("%fromaccount%", from.getAccountName())
					.replace("%formatwithdraw%", wformat)
					.replace("%category%", category != null ? category : "/")
					.replace("%comment%", comment != null ? comment : "/");
					list.add(a);
				}
			}
			if(!ea.isSuccess())
			{
				player.sendMessage(ChatApi.tl(ea.getDefaultErrorMessage()));
				return false;
			}
			for(String s : list)
			{
				player.sendMessage(ChatApi.tl(s));
			}
		}
		return true;
	}
	
	private static boolean addStorageVault(Player player, SignStorage sst, List<String> costPerOne, long amount, long ca)
	{
		double d = 0.0;
		for(String t : costPerOne)
		{
			String[] split = t.split(";");
			if(split.length != 2)
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("FileError")
						.replace("%file%", "config.yml | split.length != 2")));
				continue;
			}
			
			if(!MatchApi.isDouble(split[1]))
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("FileError")
						.replace("%file%", "config.yml | "+split[1]+" != Double")));
				continue;
			}
			d = Double.parseDouble(split[1]);
			if(plugin.getModifier() != null)
			{
				d = plugin.getModifier().getResult(player.getUniqueId(), d, Bypass.Counter.COST_ADDING_STORAGE.getModification());
			}
		}
		double dd = d*ca;
		if(plugin.getVaultEco().getBalance(player) < dd)
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("AdminstrationFunctionHandler.AddStorage.NoEnoughMoney")));
			return false;
		}
		EconomyResponse er = plugin.getVaultEco().withdrawPlayer(player, dd);
		if(!er.transactionSuccess())
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("AdminstrationFunctionHandler.AddStorage.NoEnoughMoney")));
			return false;
		}
		return true;
	}
	
	private static void clearItem(Player player, SignStorage sst)
	{
		if(isTooMuchShop(player, sst))
		{
			return;
		}
		if(!SignHandler.isOwner(sst, player.getUniqueId()) && !SignHandler.isBypassToggle(player.getUniqueId()))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NotOwner")));
			return;
		}
		if(sst.getItemStorageCurrent() > 0)
		{
			return;
		}
		sst.setItemStack(null);
		sst.setDisplayName(null);
		sst.setMaterial(Material.AIR);
		plugin.getMysqlHandler().updateData(MysqlType.SIGNSTORAGE, sst, "`id` = ?", sst.getId());
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("AdminstrationFunctionHandler.ItemClear")));
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				player.closeInventory();
				SignHandler.updateSign(sst);
			}
		}.runTask(plugin);
	}
	
	private static void deleteSoft(Player player, SignStorage sst)
	{
		if(sst.getItemStorageCurrent() > 0)
		{
			return;
		}
		deleteAll(player, sst);
	}
	
	private static void deleteAll(Player player, SignStorage sst)
	{
		if(!SignHandler.isOwner(sst, player.getUniqueId()) && !SignHandler.isBypassToggle(player.getUniqueId()))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NotOwner")));
			return;
		}
		final int sstid = sst.getId();
		final String sstname = sst.getSignStorageName();
		final ItemStack is = sst.getItemStack();
		final String displayname = is.getItemMeta().hasDisplayName() 
				? is.getItemMeta().getDisplayName() : 
					(plugin.getEnumTl() != null 
					? VSS.getPlugin().getEnumTl().getLocalization(is.getType())
					: is.getType().toString());
		final long amount = sst.getItemStorageCurrent();
		Block bl = null;
		if(Bukkit.getWorld(sst.getWorld()) != null)
		{
			bl = new Location(Bukkit.getWorld(sst.getWorld()), sst.getX(), sst.getY(), sst.getZ()).getBlock();
		}
		final Block block = bl;
		plugin.getMysqlHandler().deleteData(MysqlType.SIGNSTORAGE, "`id` = ?", sst.getId());
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("AdminstrationFunctionHandler.DeleteAll.Delete")
				.replace("%id%", String.valueOf(sstid))
				.replace("%signshop%", sstname)
				.replace("%displayname%", displayname)
				.replace("%amount%", String.valueOf(amount))));
		if(block != null)
		{
			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					player.closeInventory();
					SignHandler.clearSign(block);
				}
			}.runTask(plugin);
		}
		return;
	}
	
	private static void setAccountDefault(Player player, SignStorage sst, Inventory inv, SettingsLevel settingsLevel)
	{
		if(plugin.getIFHEco() == null)
		{
			return;
		}
		if(isTooMuchShop(player, sst))
		{
			return;
		}
		if(!SignHandler.isOwner(sst, player.getUniqueId()) && !SignHandler.isBypassToggle(player.getUniqueId()))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NotOwner")));
			return;
		}
		int acid = 0;
		Account ac = plugin.getIFHEco().getDefaultAccount(player.getUniqueId(), AccountCategory.SHOP, plugin.getIFHEco().getDefaultCurrency(CurrencyType.DIGITAL));
		if(ac == null)
		{
			ac = plugin.getIFHEco().getDefaultAccount(player.getUniqueId(), AccountCategory.MAIN, plugin.getIFHEco().getDefaultCurrency(CurrencyType.DIGITAL));
			if(ac == null)
			{
				sst.setAccountId(0);
			} else
			{
				acid = ac.getID();
			}
		} else
		{
			acid = ac.getID();
		}
		sst.setAccountId(acid);
		plugin.getMysqlHandler().updateData(MysqlType.SIGNSTORAGE, sst, "`id` = ?", sst.getId());
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("AdminstrationFunctionHandler.SetAccount.Set")));
		GuiHandler.openAdministration(sst, player, settingsLevel, inv, false);
	}
	
	private static void switchSettingsLevel(Player player, SignStorage sst, String type, Inventory inv, SettingsLevel settingsLevel)
	{
		PlayerData pd = (PlayerData) plugin.getMysqlHandler().getData(MysqlType.PLAYERDATA, "`player_uuid` = ?", player.getUniqueId().toString());
		pd.setLastSettingLevel(settingsLevel);
		plugin.getMysqlHandler().updateData(MysqlType.PLAYERDATA, pd, "`player_uuid` = ?", player.getUniqueId().toString());
		GuiHandler.openAdministration(sst, player, settingsLevel, inv, false);
	}
	
	private static void switchListType(Player player, SignStorage sst, String type, Inventory inv, SettingsLevel settingsLevel, ListedType listedType)
	{
		sst.setListedType(listedType);
		plugin.getMysqlHandler().updateData(MysqlType.SIGNSTORAGE, sst, "`id` = ?", sst.getId());
		GuiHandler.openAdministration(sst, player, settingsLevel, inv, false);
	}
	
	private static void setToggle(Player player, SignStorage sst, String type, Inventory inv, SettingsLevel settingsLevel)
	{
		switch(type)
		{
		default: break;
		case "U": sst.setUnlimited(!sst.isUnlimited()); break;
		}
		plugin.getMysqlHandler().updateData(MysqlType.SIGNSTORAGE, sst, "`id` = ?", sst.getId());
		GuiHandler.openAdministration(sst, player, settingsLevel, inv, false);
	}
	
	private static void setGlowing(Player player, SignStorage sst, Inventory inv, SettingsLevel settingsLevel, boolean glow)
	{
		sst.setSignGlowing(glow);
		plugin.getMysqlHandler().updateData(MysqlType.SIGNSTORAGE, sst, "`id` = ?", sst.getId());
		GuiHandler.openAdministration(sst, player, settingsLevel, inv, false);
	}
	
	private static void setItemHover(Player player, SignStorage sst, Inventory inv, SettingsLevel settingsLevel, boolean itemhover)
	{
		sst.setItemHologram(itemhover);
		plugin.getMysqlHandler().updateData(MysqlType.SIGNSTORAGE, sst, "`id` = ?", sst.getId());
		GuiHandler.openAdministration(sst, player, settingsLevel, inv, false);
	}
	
	private static void openNumpad(Player player, SignStorage sst, GuiType gt, Inventory inv, SettingsLevel settingsLevel)
	{
		if(isTooMuchShop(player, sst))
		{
			return;
		}
		if(!SignHandler.isOwner(sst, player.getUniqueId()) && !SignHandler.isBypassToggle(player.getUniqueId()))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NotOwner")));
			return;
		}
		GuiHandler.openKeyOrNumInput(sst, player, gt, settingsLevel, " Numpad", true);
	}
	
	private static void takeOver(Player player, SignStorage sst, GuiType gt, Inventory inv, SettingsLevel settingsLevel)
	{
		if(isTooMuchShop(player, sst))
		{
			return;
		}
		if(!SignHandler.isOwner(sst, player.getUniqueId()) && !SignHandler.isBypassToggle(player.getUniqueId()))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NotOwner")));
			return;
		}
		switch(gt)
		{
		default: break;
		case NUMPAD_ACCOUNT:
			if(plugin.getIFHEco() == null)
			{
				break;
			}
			if(!MatchApi.isInteger(sst.getNumText()))
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NoNumber")
						.replace("%value%", sst.getNumText())));
				break;
			}
			if(!MatchApi.isPositivNumber(Integer.parseInt(sst.getNumText())))
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("IsNegativ")
						.replace("%value%", sst.getNumText())));
				break;
			}
			Account ac = plugin.getIFHEco().getAccount(Integer.parseInt(sst.getNumText()));
			if(ac == null)
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("AccountNotExist")
						.replace("%value%", sst.getNumText())));
				break;
			}
			if(!plugin.getIFHEco().canManageAccount(ac, player.getUniqueId(), AccountManagementType.CAN_WITHDRAW))
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NoWithdrawRights")));
				break;
			}			
			sst.setAccountId(Integer.parseInt(sst.getNumText()));
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("AdminstrationFunctionHandler.SetAccount.Set")));
			break;
		case KEYBOARD_SIGNSTORAGENAME:
			if(sst.getNumText().isBlank() || sst.getNumText().isEmpty())
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("InputIsEmpty")
						.replace("%value%", sst.getNumText())));
				break;
			}
			sst.setSignStorageName(sst.getNumText());
			break;
		}
		sst.setNumText("");
		plugin.getMysqlHandler().updateData(MysqlType.SIGNSTORAGE, sst, "`id` = ?", sst.getId());
		GuiHandler.openAdministration(sst, player, settingsLevel, inv, true);
	}
	
	private static void setNumpadClear(Player player, SignStorage sst, GuiType gt, Inventory inv, SettingsLevel settingsLevel)
	{
		if(isTooMuchShop(player, sst))
		{
			return;
		}
		if(!SignHandler.isOwner(sst, player.getUniqueId()) && !SignHandler.isBypassToggle(player.getUniqueId()))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NotOwner")));
			return;
		}
		sst.setNumText("");
		plugin.getMysqlHandler().updateData(MysqlType.SIGNSTORAGE, sst, "`id` = ?", sst.getId());
		GuiHandler.openKeyOrNumInput(sst, player, gt, settingsLevel, " Numpad", false);
	}
	
	private static void numpadRemoveOnce(Player player, SignStorage sst, GuiType gt, Inventory inv, SettingsLevel settingsLevel)
	{
		if(isTooMuchShop(player, sst))
		{
			return;
		}
		if(!SignHandler.isOwner(sst, player.getUniqueId()) && !SignHandler.isBypassToggle(player.getUniqueId()))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NotOwner")));
			return;
		}
		if(!sst.getNumText().isEmpty())
		{
			sst.setNumText(sst.getNumText().substring(0, sst.getNumText().length()-1));
			plugin.getMysqlHandler().updateData(MysqlType.SIGNSTORAGE, sst, "`id` = ?", sst.getId());
			GuiHandler.openKeyOrNumInput(sst, player, gt, settingsLevel, " Numpad", false);
		}
	}
	
	private static void numpad(Player player, SignStorage sst, String num, GuiType gt, Inventory inv, SettingsLevel settingsLevel)
	{
		if(isTooMuchShop(player, sst))
		{
			return;
		}
		if(!SignHandler.isOwner(sst, player.getUniqueId()) && !SignHandler.isBypassToggle(player.getUniqueId()))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NotOwner")));
			return;
		}
		sst.setNumText(sst.getNumText()+num);
		plugin.getMysqlHandler().updateData(MysqlType.SIGNSTORAGE, sst, "`id` = ?", sst.getId());
		GuiHandler.openKeyOrNumInput(sst, player, gt, settingsLevel, " Numpad", false);
	}
	
	private static void cancelNumpad(Player player, SignStorage sst, Inventory inv, SettingsLevel settingsLevel)
	{
		sst.setNumText("");
		plugin.getMysqlHandler().updateData(MysqlType.SIGNSTORAGE, sst, "`id` = ?", sst.getId());
		GuiHandler.openAdministration(sst, player, settingsLevel, true);
	}
	
	private static void setKeyboardClear(Player player, SignStorage sst, GuiType gt, Inventory inv, SettingsLevel settingsLevel)
	{
		setNumpadClear(player, sst, gt, inv, settingsLevel);
	}
	
	private static void keyboardRemoveOnce(Player player, SignStorage sst, GuiType gt, Inventory inv, SettingsLevel settingsLevel)
	{
		numpadRemoveOnce(player, sst, gt, inv, settingsLevel);
	}
	
	private static void keyboard(Player player, SignStorage sst, String key, GuiType gt, Inventory inv, SettingsLevel settingsLevel)
	{
		if(isTooMuchShop(player, sst))
		{
			return;
		}
		if(!SignHandler.isOwner(sst, player.getUniqueId()) && !SignHandler.isBypassToggle(player.getUniqueId()))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NotOwner")));
			return;
		}
		sst.setNumText(sst.getNumText()+key);
		plugin.getMysqlHandler().updateData(MysqlType.SIGNSTORAGE, sst, "`id` = ?", sst.getId());
		GuiHandler.openKeyOrNumInput(sst, player, gt, settingsLevel, " Keybord", false);
	}
	
	private static void cancelKeyboard(Player player, SignStorage sst, Inventory inv, SettingsLevel settingsLevel)
	{
		cancelNumpad(player, sst, inv, settingsLevel);
	}
	
	private static void openKeyboard(Player player, SignStorage sst, GuiType gt, Inventory inv, SettingsLevel settingsLevel)
	{
		if(isTooMuchShop(player, sst))
		{
			return;
		}
		if(!SignHandler.isOwner(sst, player.getUniqueId()) && !SignHandler.isBypassToggle(player.getUniqueId()))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NotOwner")));
			return;
		}
		GuiHandler.openKeyOrNumInput(sst, player, gt, settingsLevel, " Keyboard", true);
	}
	
	private static void addPlayerToList(Player player, SignStorage sst, GuiType gt, Inventory inv, SettingsLevel settingsLevel,
			ListedType listType, UUID otheruuid, boolean remove, boolean world)
	{
		if(otheruuid == null)
		{
			return;
		}
		int a = 0;
		if(world)
		{
			ArrayList<SignStorage> sstAL = SignStorage.convert(plugin.getMysqlHandler().getFullList(MysqlType.SIGNSTORAGE,
					"`id` ASC", "`player_uuid` = ? AND `server_name` = ? AND `world` = ?",
					sst.getOwner().toString(), plugin.getServername(), player.getWorld().getName()));
			for(SignStorage ss : sstAL)
			{
				if(remove)
				{
					plugin.getMysqlHandler().deleteData(MysqlType.STORAGEACCESSTYPE,
							"`player_uuid` = ? AND `sign_shop_id` = ? AND `listed_type` = ?",
							otheruuid.toString(), ss.getId(), listType.toString());
				} else
				{
					if(!plugin.getMysqlHandler().exist(MysqlType.STORAGEACCESSTYPE,
							"`player_uuid` = ? AND `sign_shop_id` = ? AND `listed_type` = ?",
							otheruuid.toString(), ss.getId(), listType.toString()))
					{
						plugin.getMysqlHandler().create(MysqlType.STORAGEACCESSTYPE,
								new StorageAccessType(0, ss.getId(), otheruuid, listType));
					}
				}
				a++;
			}
		} else
		{
			if(remove)
			{
				plugin.getMysqlHandler().deleteData(MysqlType.STORAGEACCESSTYPE,
						"`player_uuid` = ? AND `sign_shop_id` = ? AND `listed_type` = ?",
						otheruuid.toString(), sst.getId(), listType.toString());
			} else
			{
				if(!plugin.getMysqlHandler().exist(MysqlType.STORAGEACCESSTYPE,
						"`player_uuid` = ? AND `sign_shop_id` = ? AND `listed_type` = ?",
						otheruuid.toString(), sst.getId(), listType.toString()))
				{
					plugin.getMysqlHandler().create(MysqlType.STORAGEACCESSTYPE,
							new StorageAccessType(0, sst.getId(), otheruuid, listType));
				}
			}
			a++;
		}
		if(remove)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("AdminstrationFunctionHandler.Listed.Remove")
					.replace("%amount%", String.valueOf(a))
					.replace("%list%", 
							plugin.getYamlHandler().getLang().getString("AdminstrationFunctionHandler.ListedType."+listType.toString()))));
		} else
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("AdminstrationFunctionHandler.Listed.Add")
					.replace("%amount%", String.valueOf(a))
					.replace("%list%", 
							plugin.getYamlHandler().getLang().getString("AdminstrationFunctionHandler.ListedType."+listType.toString()))));
		}
		GuiHandler.openKeyOrNumInput(sst, player, gt, settingsLevel, " Keyboard", false);
	}
	
	private static void sendPlayerOnList(Player player, SignStorage sst,
			ListedType listType)
	{
		List<String> players = StorageAccessType.convert(plugin.getMysqlHandler().getFullList(MysqlType.STORAGEACCESSTYPE,
				"`id` ASC", "`sign_shop_id` = ? AND `listed_type` = ?",	sst.getId(), listType.toString()))
				.stream()
				.map(x -> x.getUUID())
				.map(x -> Utility.convertUUIDToName(x.toString()))
				.filter(x -> x != null)
				.collect(Collectors.toList());
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("AdminstrationFunctionHandler.Listed.List")
				.replace("%players%", "["+String.join(", ", players)+"]")
				.replace("%list%", 
						plugin.getYamlHandler().getLang().getString("AdminstrationFunctionHandler.ListedType."+listType.toString()))));
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				player.closeInventory();
			}
		}.runTask(plugin);
		return;
	}
}