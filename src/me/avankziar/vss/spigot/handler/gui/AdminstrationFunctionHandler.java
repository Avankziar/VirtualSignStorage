package me.avankziar.vss.spigot.handler.gui;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.UUID;

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
import me.avankziar.vss.spigot.VSS;
import me.avankziar.vss.spigot.assistance.MatchApi;
import me.avankziar.vss.spigot.assistance.TimeHandler;
import me.avankziar.vss.spigot.assistance.Utility;
import me.avankziar.vss.spigot.cmdtree.CommandExecuteType;
import me.avankziar.vss.spigot.cmdtree.CommandSuggest;
import me.avankziar.vss.spigot.database.MysqlHandler;
import me.avankziar.vss.spigot.gui.objects.ClickFunctionType;
import me.avankziar.vss.spigot.gui.objects.GuiType;
import me.avankziar.vss.spigot.gui.objects.SettingsLevel;
import me.avankziar.vss.spigot.handler.GuiHandler;
import me.avankziar.vss.spigot.handler.SignHandler;
import me.avankziar.vss.spigot.modifiervalueentry.Bypass;
import me.avankziar.vss.spigot.modifiervalueentry.ModifierValueEntry;
import me.avankziar.vss.spigot.objects.ListedType;
import me.avankziar.vss.spigot.objects.PlayerData;
import me.avankziar.vss.spigot.objects.ShopAccessType;
import me.avankziar.vss.spigot.objects.SignShop;
import net.milkbowl.vault.economy.EconomyResponse;

public class AdminstrationFunctionHandler
{
	private static VSS plugin = VSS.getPlugin();
	
	public static void doClickFunktion(GuiType guiType, ClickFunctionType cft, Player player, SignShop ssh,
			Inventory openInv, SettingsLevel settingsLevel, UUID otheruuid)
	{
		switch(cft)
		{
		default: return;
		case ADMINISTRATION_ADDSTORAGE_1: addStorage(player, ssh, 1, openInv, settingsLevel); break;
		case ADMINISTRATION_ADDSTORAGE_8: addStorage(player, ssh, 8, openInv, settingsLevel); break;
		case ADMINISTRATION_ADDSTORAGE_16: addStorage(player, ssh, 16, openInv, settingsLevel); break;
		case ADMINISTRATION_ADDSTORAGE_32: addStorage(player, ssh, 32, openInv, settingsLevel); break;
		case ADMINISTRATION_ADDSTORAGE_64: addStorage(player, ssh, 64, openInv, settingsLevel); break;
		case ADMINISTRATION_ADDSTORAGE_576: addStorage(player, ssh, 576, openInv, settingsLevel); break;
		case ADMINISTRATION_ADDSTORAGE_1728: addStorage(player, ssh, 1728, openInv, settingsLevel); break;
		case ADMINISTRATION_ADDSTORAGE_3456: addStorage(player, ssh, 3456, openInv, settingsLevel); break;
		case ADMINISTRATION_ADDSTORAGE_6912: addStorage(player, ssh, 6912, openInv, settingsLevel); break;
		case ADMINISTRATION_DELETE_ALL: deleteAll(player, ssh); break;
		case ADMINISTRATION_DELETE_WITHOUT_ITEMS_IN_STORAGE: deleteSoft(player, ssh); break;
		case ADMINISTRATION_ITEM_CLEAR: clearItem(player, ssh); break;
		case ADMINISTRATION_OPEN_SHOPLOG: openShopLog(player, ssh); break;
		case ADMINISTRATION_NUMPAD_0: numpad(player, ssh, "0", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_NUMPAD_1: numpad(player, ssh, "1", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_NUMPAD_2: numpad(player, ssh, "2", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_NUMPAD_3: numpad(player, ssh, "3", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_NUMPAD_4: numpad(player, ssh, "4", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_NUMPAD_5: numpad(player, ssh, "5", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_NUMPAD_6: numpad(player, ssh, "6", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_NUMPAD_7: numpad(player, ssh, "7", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_NUMPAD_8: numpad(player, ssh, "8", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_NUMPAD_9: numpad(player, ssh, "9", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_NUMPAD_COLON: numpad(player, ssh, ":", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_NUMPAD_DECIMAL: numpad(player, ssh, ".", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_NUMPAD_CLEAR: setNumpadClear(player, ssh, guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_NUMPAD_CANCEL: cancelNumpad(player, ssh, openInv, settingsLevel); break;
		case ADMINISTRATION_NUMPAD_REMOVEONCE: numpadRemoveOnce(player, ssh, guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_SETACCOUNT_DEFAULT: setAccountDefault(player, ssh, openInv, settingsLevel); break;
		case ADMINISTRATION_SETACCOUNT_OPEN_NUMPAD: openNumpad(player, ssh, GuiType.NUMPAD_ACCOUNT, openInv, settingsLevel); break;
		case ADMINISTRATION_SETACCOUNT_TAKEOVER: takeOver(player, ssh, GuiType.NUMPAD_ACCOUNT, openInv, settingsLevel); break;
		case ADMINISTRATION_SETBUY_CLEAR: setClear(player, ssh, GuiType.NUMPAD_BUY, openInv, settingsLevel); break;
		case ADMINISTRATION_SETBUY_OPEN_NUMPAD: openNumpad(player, ssh, GuiType.NUMPAD_BUY, openInv, settingsLevel); break;
		case ADMINISTRATION_SETBUY_TAKEOVER: takeOver(player, ssh, GuiType.NUMPAD_BUY, openInv, settingsLevel); break;
		case ADMINISTRATION_SETSELL_CLEAR: setClear(player, ssh, GuiType.NUMPAD_SELL, openInv, settingsLevel); break;
		case ADMINISTRATION_SETSELL_OPEN_NUMPAD: openNumpad(player, ssh, GuiType.NUMPAD_SELL, openInv, settingsLevel); break;
		case ADMINISTRATION_SETSELL_TAKEOVER: takeOver(player, ssh, GuiType.NUMPAD_SELL, openInv, settingsLevel); break;
		case ADMINISTRATION_SETPOSSIBLE_BUY_CLEAR: setClear(player, ssh, GuiType.NUMPAD_POSSIBLE_BUY,  openInv, settingsLevel); break;
		case ADMINISTRATION_SETPOSSIBLE_BUY_OPEN_NUMPAD: openNumpad(player, ssh, GuiType.NUMPAD_POSSIBLE_BUY, openInv, settingsLevel); break;
		case ADMINISTRATION_SETPOSSIBLE_BUY_TAKEOVER: takeOver(player, ssh, GuiType.NUMPAD_POSSIBLE_BUY, openInv, settingsLevel); break;
		case ADMINISTRATION_SETPOSSIBLE_SELL_CLEAR: setClear(player, ssh, GuiType.NUMPAD_POSSIBLE_SELL,  openInv, settingsLevel); break;
		case ADMINISTRATION_SETPOSSIBLE_SELL_OPEN_NUMPAD: openNumpad(player, ssh, GuiType.NUMPAD_POSSIBLE_SELL, openInv, settingsLevel); break;
		case ADMINISTRATION_SETPOSSIBLE_SELL_TAKEOVER: takeOver(player, ssh, GuiType.NUMPAD_POSSIBLE_SELL, openInv, settingsLevel); break;
		case ADMINISTRATION_SETDISCOUNT_CLEAR: setClear(player, ssh, GuiType.NUMPAD_DISCOUNT_START, openInv, settingsLevel); break;
		case ADMINISTRATION_SETDISCOUNT_START_OPEN_NUMPAD: openNumpad(player, ssh, GuiType.NUMPAD_DISCOUNT_START, openInv, settingsLevel); break;
		case ADMINISTRATION_SETDISCOUNT_START_TAKEOVER: takeOverDiscountTime(player, ssh, GuiType.NUMPAD_DISCOUNT_START, openInv, settingsLevel, false); break;
		case ADMINISTRATION_SETDISCOUNT_START_WORLD_TAKEOVER: takeOverDiscountTime(player, ssh, GuiType.NUMPAD_DISCOUNT_START, openInv, settingsLevel, true); break;
		case ADMINISTRATION_SETDISCOUNT_HOUR_OPEN_NUMPAD: openNumpad(player, ssh, GuiType.NUMPAD_DISCOUNT_HOUR, openInv, settingsLevel); break;
		case ADMINISTRATION_SETDISCOUNT_HOUR_TAKEOVER: takeOverDiscountTime(player, ssh, GuiType.NUMPAD_DISCOUNT_HOUR, openInv, settingsLevel, false); break;
		case ADMINISTRATION_SETDISCOUNT_HOUR_WORLD_TAKEOVER: takeOverDiscountTime(player, ssh, GuiType.NUMPAD_DISCOUNT_HOUR, openInv, settingsLevel, false); break;
		case ADMINISTRATION_SETDISCOUNT_END_OPEN_NUMPAD: openNumpad(player, ssh, GuiType.NUMPAD_DISCOUNT_END, openInv, settingsLevel); break;
		case ADMINISTRATION_SETDISCOUNT_END_TAKEOVER: takeOverDiscountTime(player, ssh, GuiType.NUMPAD_DISCOUNT_END, openInv, settingsLevel, false); break;
		case ADMINISTRATION_SETDISCOUNT_END_WORLD_TAKEOVER: takeOverDiscountTime(player, ssh, GuiType.NUMPAD_DISCOUNT_END, openInv, settingsLevel, true); break;
		case ADMINISTRATION_SETDISCOUNTBUY_CLEAR: setClear(player, ssh, GuiType.NUMPAD_DISCOUNT_BUY, openInv, settingsLevel); break;
		case ADMINISTRATION_SETDISCOUNTBUY_OPEN_NUMPAD: openNumpad(player, ssh, GuiType.NUMPAD_DISCOUNT_BUY, openInv, settingsLevel); break;
		case ADMINISTRATION_SETDISCOUNTBUY_TAKEOVER: takeOver(player, ssh, GuiType.NUMPAD_DISCOUNT_BUY, openInv, settingsLevel); break;
		case ADMINISTRATION_SETDISCOUNTSELL_CLEAR: setClear(player, ssh, GuiType.NUMPAD_DISCOUNT_SELL, openInv, settingsLevel); break;
		case ADMINISTRATION_SETDISCOUNTSELL_OPEN_NUMPAD: openNumpad(player, ssh, GuiType.NUMPAD_DISCOUNT_SELL, openInv, settingsLevel); break;
		case ADMINISTRATION_SETDISCOUNTSELL_TAKEOVER: takeOver(player, ssh, GuiType.NUMPAD_DISCOUNT_SELL, openInv, settingsLevel); break;
		case ADMINISTRATION_SETDISCOUNTPOSSIBLE_BUY_CLEAR: setClear(player, ssh, GuiType.NUMPAD_DISCOUNT_POSSIBLE_BUY, openInv, settingsLevel); break;
		case ADMINISTRATION_SETDISCOUNTPOSSIBLE_BUY_OPEN_NUMPAD: openNumpad(player, ssh, GuiType.NUMPAD_DISCOUNT_POSSIBLE_BUY, openInv, settingsLevel); break;
		case ADMINISTRATION_SETDISCOUNTPOSSIBLE_BUY_TAKEOVER: takeOver(player, ssh, GuiType.NUMPAD_DISCOUNT_POSSIBLE_BUY, openInv, settingsLevel); break;
		case ADMINISTRATION_SETDISCOUNTPOSSIBLE_SELL_CLEAR: setClear(player, ssh, GuiType.NUMPAD_DISCOUNT_POSSIBLE_SELL, openInv, settingsLevel); break;
		case ADMINISTRATION_SETDISCOUNTPOSSIBLE_SELL_OPEN_NUMPAD: openNumpad(player, ssh, GuiType.NUMPAD_DISCOUNT_POSSIBLE_SELL, openInv, settingsLevel); break;
		case ADMINISTRATION_SETDISCOUNTPOSSIBLE_SELL_TAKEOVER: takeOver(player, ssh, GuiType.NUMPAD_DISCOUNT_POSSIBLE_SELL, openInv, settingsLevel); break;
		case ADMINISTRATION_SETTINGSLEVEL_SETTO_ADVANCED: switchSettingsLevel(player, ssh, null, openInv, SettingsLevel.ADVANCED); break;
		case ADMINISTRATION_SETTINGSLEVEL_SETTO_BASE: switchSettingsLevel(player, ssh, null, openInv, SettingsLevel.BASE); break;
		case ADMINISTRATION_SETTINGSLEVEL_SETTO_EXPERT: switchSettingsLevel(player, ssh, null, openInv, SettingsLevel.EXPERT); break;
		case ADMINISTRATION_SETTINGSLEVEL_SETTO_MASTER: switchSettingsLevel(player, ssh, null, openInv, SettingsLevel.MASTER); break;
		case ADMINISTRATION_TOGGLE_BUY: setToggle(player, ssh, "BUY", openInv, settingsLevel); break;
		case ADMINISTRATION_TOGGLE_SELL: setToggle(player, ssh, "SELL", openInv, settingsLevel); break;
		case ADMINISTRATION_UNLIMITED_TOGGLE_BUY: setToggle(player, ssh, "UBUY", openInv, settingsLevel); break;
		case ADMINISTRATION_UNLIMITED_TOGGLE_SELL: setToggle(player, ssh, "USELL", openInv, settingsLevel); break;
		case ADMINISTRATION_SETGLOWING: setGlowing(player, ssh, openInv, settingsLevel, true); break;
		case ADMINISTRATION_SETUNGLOWING: setGlowing(player, ssh, openInv, settingsLevel, false); break;
		case ADMINISTRATION_SETITEMHOLOGRAM_ACTIVE: setItemHover(player, ssh, openInv, settingsLevel, true); break;
		case ADMINISTRATION_SETITEMHOLOGRAM_DEACTIVE: setItemHover(player, ssh, openInv, settingsLevel, false); break;
		case ADMINISTRATION_SETLISTEDTYPE_ALL: switchListType(player, ssh, null, openInv, settingsLevel, ListedType.ALL); break;
		case ADMINISTRATION_SETLISTEDTYPE_WHITELIST: switchListType(player, ssh, null, openInv, settingsLevel, ListedType.WHITELIST); break;
		case ADMINISTRATION_SETLISTEDTYPE_BLACKLIST: switchListType(player, ssh, null, openInv, settingsLevel, ListedType.BLACKLIST); break;
		case ADMINISTRATION_SETLISTEDTYPE_MEMBER: switchListType(player, ssh, null, openInv, settingsLevel, ListedType.MEMBER); break;
		case ADMINISTRATION_SETLISTEDTYPE_CUSTOM: switchListType(player, ssh, null, openInv, settingsLevel, ListedType.CUSTOM); break;
		case ADMINISTRATION_KEYBOARD_0: keyboard(player, ssh, "0", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_1: keyboard(player, ssh, "1", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_2: keyboard(player, ssh, "2", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_3: keyboard(player, ssh, "3", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_4: keyboard(player, ssh, "4", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_5: keyboard(player, ssh, "5", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_6: keyboard(player, ssh, "6", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_7: keyboard(player, ssh, "7", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_8: keyboard(player, ssh, "8", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_9: keyboard(player, ssh, "9", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_A_CAPITAL: keyboard(player, ssh, "A", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_A_SMALL: keyboard(player, ssh, "a", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_B_CAPITAL: keyboard(player, ssh, "B", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_B_SMALL: keyboard(player, ssh, "b", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_C_CAPITAL: keyboard(player, ssh, "C", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_C_SMALL: keyboard(player, ssh, "c", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_D_CAPITAL: keyboard(player, ssh, "D", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_D_SMALL: keyboard(player, ssh, "d", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_E_CAPITAL: keyboard(player, ssh, "E", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_E_SMALL: keyboard(player, ssh, "e", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_F_CAPITAL: keyboard(player, ssh, "F", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_F_SMALL: keyboard(player, ssh, "f", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_G_CAPITAL: keyboard(player, ssh, "G", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_G_SMALL: keyboard(player, ssh, "g", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_H_CAPITAL: keyboard(player, ssh, "H", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_H_SMALL: keyboard(player, ssh, "h", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_I_CAPITAL: keyboard(player, ssh, "I", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_I_SMALL: keyboard(player, ssh, "i", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_J_CAPITAL: keyboard(player, ssh, "J", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_J_SMALL: keyboard(player, ssh, "j", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_K_CAPITAL: keyboard(player, ssh, "K", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_K_SMALL: keyboard(player, ssh, "k", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_L_CAPITAL: keyboard(player, ssh, "L", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_L_SMALL: keyboard(player, ssh, "l", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_M_CAPITAL: keyboard(player, ssh, "M", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_M_SMALL: keyboard(player, ssh, "m", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_N_CAPITAL: keyboard(player, ssh, "N", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_N_SMALL: keyboard(player, ssh, "n", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_O_CAPITAL: keyboard(player, ssh, "O", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_O_SMALL: keyboard(player, ssh, "o", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_P_CAPITAL: keyboard(player, ssh, "P", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_P_SMALL: keyboard(player, ssh, "p", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_Q_CAPITAL: keyboard(player, ssh, "Q", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_Q_SMALL: keyboard(player, ssh, "q", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_R_CAPITAL: keyboard(player, ssh, "R", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_R_SMALL: keyboard(player, ssh, "r", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_S_CAPITAL: keyboard(player, ssh, "S", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_S_SMALL: keyboard(player, ssh, "s", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_T_CAPITAL: keyboard(player, ssh, "T", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_T_SMALL: keyboard(player, ssh, "t", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_U_CAPITAL: keyboard(player, ssh, "U", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_U_SMALL: keyboard(player, ssh, "u", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_V_CAPITAL: keyboard(player, ssh, "V", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_V_SMALL: keyboard(player, ssh, "v", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_W_CAPITAL: keyboard(player, ssh, "W", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_W_SMALL: keyboard(player, ssh, "w", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_X_CAPITAL: keyboard(player, ssh, "X", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_X_SMALL: keyboard(player, ssh, "x", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_Y_CAPITAL: keyboard(player, ssh, "Y", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_Y_SMALL: keyboard(player, ssh, "y", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_Z_CAPITAL: keyboard(player, ssh, "Z", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_Z_SMALL: keyboard(player, ssh, "z", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD__: keyboard(player, ssh, "_", guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_REMOVEONCE: keyboardRemoveOnce(player, ssh, guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_CLEAR: setKeyboardClear(player, ssh, guiType, openInv, settingsLevel); break;
		case ADMINISTRATION_KEYBOARD_CANCEL: cancelKeyboard(player, ssh, openInv, settingsLevel); break;
		case ADMINISTRATION_SETSIGNSHOPNAME_OPENKEYBOARD: openKeyboard(player, ssh, GuiType.KEYBOARD_SIGNSHOPNAME, openInv, settingsLevel); break;
		case ADMINISTRATION_SETSIGNSHOPNAME_TAKEOVER: takeOver(player, ssh, GuiType.KEYBOARD_SIGNSHOPNAME, openInv, settingsLevel); break;
		case ADMINISTRATION_ADDLISTEDTYPE_PLAYER_OPENKEYBOARD_BLACKLIST: openKeyboard(player, ssh, GuiType.KEYBOARD_BLACKLIST, openInv, settingsLevel); break;
		case ADMINISTRATION_ADDLISTEDTYPE_PLAYER_OPENKEYBOARD_WHITELIST: openKeyboard(player, ssh, GuiType.KEYBOARD_WHITELIST, openInv, settingsLevel); break;
		case ADMINISTRATION_ADDLISTEDTYPE_PLAYER_OPENKEYBOARD_MEMBER: openKeyboard(player, ssh, GuiType.KEYBOARD_MEMBER, openInv, settingsLevel); break;
		case ADMINISTRATION_ADDLISTEDTYPE_PLAYER_OPENKEYBOARD_CUSTOM: openKeyboard(player, ssh, GuiType.KEYBOARD_CUSTOM, openInv, settingsLevel); break;
		case ADMINISTRATION_ADDLISTEDTYPE_PLAYER_BLACKLIST: addPlayerToList(player, ssh, guiType, openInv, settingsLevel, ListedType.BLACKLIST, otheruuid, false, false); break;
		case ADMINISTRATION_ADDLISTEDTYPE_PLAYER_BLACKLIST_WORLD: addPlayerToList(player, ssh, guiType, openInv, settingsLevel, ListedType.BLACKLIST, otheruuid, false, true); break;
		case ADMINISTRATION_ADDLISTEDTYPE_PLAYER_BLACKLIST_REMOVE: addPlayerToList(player, ssh, guiType, openInv, settingsLevel, ListedType.BLACKLIST, otheruuid, true, false); break;
		case ADMINISTRATION_ADDLISTEDTYPE_PLAYER_BLACKLIST_REMOVE_WORLD: addPlayerToList(player, ssh, guiType, openInv, settingsLevel, ListedType.BLACKLIST, otheruuid, true, true); break;
		case ADMINISTRATION_ADDLISTEDTYPE_PLAYER_WHITELIST: addPlayerToList(player, ssh, guiType, openInv, settingsLevel, ListedType.WHITELIST, otheruuid, false, false); break;
		case ADMINISTRATION_ADDLISTEDTYPE_PLAYER_WHITELIST_WORLD: addPlayerToList(player, ssh, guiType, openInv, settingsLevel, ListedType.WHITELIST, otheruuid, false, true); break;
		case ADMINISTRATION_ADDLISTEDTYPE_PLAYER_WHITELIST_REMOVE: addPlayerToList(player, ssh, guiType, openInv, settingsLevel, ListedType.WHITELIST, otheruuid, true, false); break;
		case ADMINISTRATION_ADDLISTEDTYPE_PLAYER_WHITELIST_REMOVE_WORLD: addPlayerToList(player, ssh, guiType, openInv, settingsLevel, ListedType.WHITELIST, otheruuid, true, true); break;
		case ADMINISTRATION_ADDLISTEDTYPE_PLAYER_MEMBER: addPlayerToList(player, ssh, guiType, openInv, settingsLevel, ListedType.MEMBER, otheruuid, false, false); break;
		case ADMINISTRATION_ADDLISTEDTYPE_PLAYER_MEMBER_WORLD: addPlayerToList(player, ssh, guiType, openInv, settingsLevel, ListedType.MEMBER, otheruuid, false, true); break;
		case ADMINISTRATION_ADDLISTEDTYPE_PLAYER_MEMBER_REMOVE: addPlayerToList(player, ssh, guiType, openInv, settingsLevel, ListedType.MEMBER, otheruuid, true, false); break;
		case ADMINISTRATION_ADDLISTEDTYPE_PLAYER_MEMBER_REMOVE_WORLD: addPlayerToList(player, ssh, guiType, openInv, settingsLevel, ListedType.MEMBER, otheruuid, true, true); break;
		case ADMINISTRATION_ADDLISTEDTYPE_PLAYER_CUSTOM: addPlayerToList(player, ssh, guiType, openInv, settingsLevel, ListedType.CUSTOM, otheruuid, false, false); break;
		case ADMINISTRATION_ADDLISTEDTYPE_PLAYER_CUSTOM_WORLD: addPlayerToList(player, ssh, guiType, openInv, settingsLevel, ListedType.CUSTOM, otheruuid, false, true); break;
		case ADMINISTRATION_ADDLISTEDTYPE_PLAYER_CUSTOM_REMOVE: addPlayerToList(player, ssh, guiType, openInv, settingsLevel, ListedType.CUSTOM, otheruuid, true, false); break;
		case ADMINISTRATION_ADDLISTEDTYPE_PLAYER_CUSTOM_REMOVE_WORLD: addPlayerToList(player, ssh, guiType, openInv, settingsLevel, ListedType.CUSTOM, otheruuid, true, true); break;
		case ADMINISTRATION_LISTEDTYPE_PLAYER_OPENLIST_BLACKLIST: sendPlayerOnList(player, ssh, ListedType.BLACKLIST); break;
		case ADMINISTRATION_LISTEDTYPE_PLAYER_OPENLIST_WHITELIST: sendPlayerOnList(player, ssh, ListedType.WHITELIST); break;
		case ADMINISTRATION_LISTEDTYPE_PLAYER_OPENLIST_MEMBER: sendPlayerOnList(player, ssh, ListedType.MEMBER); break;
		case ADMINISTRATION_LISTEDTYPE_PLAYER_OPENLIST_CUSTOM: sendPlayerOnList(player, ssh, ListedType.CUSTOM); break;
		}
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				SignHandler.updateSign(ssh);
			}
		}.runTask(plugin);
	}
	
	private static boolean isTooMuchShop(Player player, SignShop ssh)
	{
		if(!SignHandler.isOwner(ssh, player.getUniqueId()) && !SignHandler.isBypassToggle(player.getUniqueId()))
		{
			return false;
		}
		int signShopAmount = plugin.getMysqlHandler().getCount(MysqlHandler.Type.SIGNSHOP, "`player_uuid` = ?", player.getUniqueId().toString());
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
	
	private static void addStorage(Player player, SignShop ssh, long amount, Inventory inv, SettingsLevel settingsLevel)
	{
		if(isTooMuchShop(player, ssh))
		{
			return;
		}
		List<String> costPerOne = plugin.getYamlHandler().getConfig().getStringList("SignShop.CostToAdd1Storage");
		long maxStorage = ssh.getItemStorageTotal();
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
			boo = addStorageIFH(player, ssh, costPerOne, amount, ca);
		} else
		{
			boo = addStorageVault(player, ssh, costPerOne, amount, ca);
		}
		if(!boo)
		{
			return;
		}
		ssh.setItemStorageTotal(ssh.getItemStorageTotal()+ca);
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.SIGNSHOP, ssh, "`id` = ?", ssh.getId());
		GuiHandler.openAdministration(ssh, player, settingsLevel, inv, false);
	}
	
	private static boolean addStorageIFH(Player player, SignShop ssh, List<String> costPerOne, long amount, long ca)
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
				.replace("%past%", String.valueOf(ssh.getItemStorageTotal()))
				.replace("%now%", String.valueOf(ssh.getItemStorageTotal()+amount))
				.replace("%amount%", String.valueOf(amount))
				.replace("%name%", ssh.getSignShopName());
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
	
	private static boolean addStorageVault(Player player, SignShop ssh, List<String> costPerOne, long amount, long ca)
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
	
	private static void clearItem(Player player, SignShop ssh)
	{
		if(isTooMuchShop(player, ssh))
		{
			return;
		}
		if(!SignHandler.isOwner(ssh, player.getUniqueId()) && !SignHandler.isBypassToggle(player.getUniqueId()))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NotOwner")));
			return;
		}
		if(ssh.getItemStorageCurrent() > 0)
		{
			return;
		}
		ssh.setItemStack(null);
		ssh.setDisplayName(null);
		ssh.setMaterial(Material.AIR);
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.SIGNSHOP, ssh, "`id` = ?", ssh.getId());
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("AdminstrationFunctionHandler.ItemClear")));
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				player.closeInventory();
				SignHandler.updateSign(ssh);
			}
		}.runTask(plugin);
	}
	
	private static void deleteSoft(Player player, SignShop ssh)
	{
		if(ssh.getItemStorageCurrent() > 0)
		{
			return;
		}
		deleteAll(player, ssh);
	}
	
	private static void deleteAll(Player player, SignShop ssh)
	{
		if(!SignHandler.isOwner(ssh, player.getUniqueId()) && !SignHandler.isBypassToggle(player.getUniqueId()))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NotOwner")));
			return;
		}
		final int sshid = ssh.getId();
		final String sshname = ssh.getSignShopName();
		final ItemStack is = ssh.getItemStack();
		final String displayname = is.getItemMeta().hasDisplayName() 
				? is.getItemMeta().getDisplayName() : 
					(plugin.getEnumTl() != null 
					? VSS.getPlugin().getEnumTl().getLocalization(is.getType())
					: is.getType().toString());
		final long amount = ssh.getItemStorageCurrent();
		Block bl = null;
		if(Bukkit.getWorld(ssh.getWorld()) != null)
		{
			bl = new Location(Bukkit.getWorld(ssh.getWorld()), ssh.getX(), ssh.getY(), ssh.getZ()).getBlock();
		}
		final Block block = bl;
		plugin.getMysqlHandler().deleteData(MysqlHandler.Type.SUBSCRIBEDSHOP, "`sign_shop_id` = ?", ssh.getId());
		plugin.getMysqlHandler().deleteData(MysqlHandler.Type.SIGNSHOP, "`id` = ?", ssh.getId());
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("AdminstrationFunctionHandler.DeleteAll.Delete")
				.replace("%id%", String.valueOf(sshid))
				.replace("%signshop%", sshname)
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
	
	private static void openShopLog(Player player, SignShop ssh)
	{
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				player.closeInventory();
				Bukkit.dispatchCommand(player, CommandSuggest.get(CommandExecuteType.SALE_SHOP_LOG).substring(1)+" "+0+" "+ssh.getId());
			}
		}.runTask(plugin);
		return;
	}
	
	private static void setAccountDefault(Player player, SignShop ssh, Inventory inv, SettingsLevel settingsLevel)
	{
		if(plugin.getIFHEco() == null)
		{
			return;
		}
		if(isTooMuchShop(player, ssh))
		{
			return;
		}
		if(!SignHandler.isOwner(ssh, player.getUniqueId()) && !SignHandler.isBypassToggle(player.getUniqueId()))
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
				ssh.setAccountId(0);
			} else
			{
				acid = ac.getID();
			}
		} else
		{
			acid = ac.getID();
		}
		ssh.setAccountId(acid);
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.SIGNSHOP, ssh, "`id` = ?", ssh.getId());
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("AdminstrationFunctionHandler.SetAccount.Set")));
		GuiHandler.openAdministration(ssh, player, settingsLevel, inv, false);
	}
	
	private static void setClear(Player player, SignShop ssh, GuiType gt, Inventory inv, SettingsLevel settingsLevel)
	{
		if(isTooMuchShop(player, ssh))
		{
			return;
		}
		if(!SignHandler.isOwner(ssh, player.getUniqueId()) && !SignHandler.isBypassToggle(player.getUniqueId()))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NotOwner")));
			return;
		}
		switch(gt)
		{
		default: break;
		case NUMPAD_BUY: ssh.setBuyAmount(-1.0); break;
		case NUMPAD_SELL: ssh.setSellAmount(-1.0); break;
		case NUMPAD_POSSIBLE_BUY: ssh.setPossibleBuy(-1); break;
		case NUMPAD_POSSIBLE_SELL: ssh.setPossibleSell(-1); break;
		case NUMPAD_DISCOUNT_START:
		case NUMPAD_DISCOUNT_END: ssh.setDiscountStart(0); ssh.setDiscountEnd(0); break;
		case NUMPAD_DISCOUNT_BUY: ssh.setDiscountBuyAmount(-1.0); break;
		case NUMPAD_DISCOUNT_SELL: ssh.setDiscountSellAmount(-1.0); break;
		case NUMPAD_DISCOUNT_POSSIBLE_BUY: ssh.setDiscountPossibleBuy(-1); break;
		case NUMPAD_DISCOUNT_POSSIBLE_SELL: ssh.setDiscountPossibleSell(-1); break;
		}
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.SIGNSHOP, ssh, "`id` = ?", ssh.getId());
		GuiHandler.openAdministration(ssh, player, settingsLevel, inv, false);
	}
	
	private static void switchSettingsLevel(Player player, SignShop ssh, String type, Inventory inv, SettingsLevel settingsLevel)
	{
		PlayerData pd = (PlayerData) plugin.getMysqlHandler().getData(MysqlHandler.Type.PLAYERDATA, "`player_uuid` = ?", player.getUniqueId().toString());
		pd.setLastSettingLevel(settingsLevel);
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.PLAYERDATA, pd, "`player_uuid` = ?", player.getUniqueId().toString());
		GuiHandler.openAdministration(ssh, player, settingsLevel, inv, false);
	}
	
	private static void switchListType(Player player, SignShop ssh, String type, Inventory inv, SettingsLevel settingsLevel, ListedType listedType)
	{
		ssh.setListedType(listedType);
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.SIGNSHOP, ssh, "`id` = ?", ssh.getId());
		GuiHandler.openAdministration(ssh, player, settingsLevel, inv, false);
	}
	
	private static void setToggle(Player player, SignShop ssh, String type, Inventory inv, SettingsLevel settingsLevel)
	{
		switch(type)
		{
		default: break;
		case "BUY": ssh.setCanBuy(!ssh.canBuy()); break;
		case "SELL": ssh.setCanSell(!ssh.canSell()); break;
		case "UBUY": ssh.setUnlimitedBuy(!ssh.isUnlimitedBuy()); break;
		case "USELL": ssh.setUnlimitedSell(!ssh.isUnlimitedSell()); break;
		}
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.SIGNSHOP, ssh, "`id` = ?", ssh.getId());
		GuiHandler.openAdministration(ssh, player, settingsLevel, inv, false);
	}
	
	private static void setGlowing(Player player, SignShop ssh, Inventory inv, SettingsLevel settingsLevel, boolean glow)
	{
		ssh.setSignGlowing(glow);
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.SIGNSHOP, ssh, "`id` = ?", ssh.getId());
		GuiHandler.openAdministration(ssh, player, settingsLevel, inv, false);
	}
	
	private static void setItemHover(Player player, SignShop ssh, Inventory inv, SettingsLevel settingsLevel, boolean itemhover)
	{
		ssh.setItemHologram(itemhover);
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.SIGNSHOP, ssh, "`id` = ?", ssh.getId());
		GuiHandler.openAdministration(ssh, player, settingsLevel, inv, false);
	}
	
	private static void openNumpad(Player player, SignShop ssh, GuiType gt, Inventory inv, SettingsLevel settingsLevel)
	{
		if(isTooMuchShop(player, ssh))
		{
			return;
		}
		if(!SignHandler.isOwner(ssh, player.getUniqueId()) && !SignHandler.isBypassToggle(player.getUniqueId()))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NotOwner")));
			return;
		}
		switch(gt)
		{
		default: break;
		case NUMPAD_DISCOUNT_START:
			if(ssh.getDiscountEnd() > 0)
			{
				ssh.setNumText(TimeHandler.getDateTime(ssh.getDiscountEnd(),
						plugin.getYamlHandler().getConfig().getString("SignShop.DiscountTimePattern", "yyyy.MM.dd.HH:mm:ss")));
				plugin.getMysqlHandler().updateData(MysqlHandler.Type.SIGNSHOP, ssh, "`id` = ?", ssh.getId());
			}
			break;
		case NUMPAD_DISCOUNT_END:
			if(ssh.getDiscountStart() > 0)
			{
				ssh.setNumText(TimeHandler.getDateTime(ssh.getDiscountStart(),
						plugin.getYamlHandler().getConfig().getString("SignShop.DiscountTimePattern", "yyyy.MM.dd.HH:mm:ss")));
				plugin.getMysqlHandler().updateData(MysqlHandler.Type.SIGNSHOP, ssh, "`id` = ?", ssh.getId());
			}
			break;
		}
		GuiHandler.openKeyOrNumInput(ssh, player, gt, settingsLevel, " Numpad", true);
	}
	
	private static void takeOver(Player player, SignShop ssh, GuiType gt, Inventory inv, SettingsLevel settingsLevel)
	{
		if(isTooMuchShop(player, ssh))
		{
			return;
		}
		if(!SignHandler.isOwner(ssh, player.getUniqueId()) && !SignHandler.isBypassToggle(player.getUniqueId()))
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
			if(!MatchApi.isInteger(ssh.getNumText()))
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NoNumber")
						.replace("%value%", ssh.getNumText())));
				break;
			}
			if(!MatchApi.isPositivNumber(Integer.parseInt(ssh.getNumText())))
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("IsNegativ")
						.replace("%value%", ssh.getNumText())));
				break;
			}
			Account ac = plugin.getIFHEco().getAccount(Integer.parseInt(ssh.getNumText()));
			if(ac == null)
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("AccountNotExist")
						.replace("%value%", ssh.getNumText())));
				break;
			}
			if(!plugin.getIFHEco().canManageAccount(ac, player.getUniqueId(), AccountManagementType.CAN_WITHDRAW))
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NoWithdrawRights")));
				break;
			}			
			ssh.setAccountId(Integer.parseInt(ssh.getNumText()));
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("AdminstrationFunctionHandler.SetAccount.Set")));
			break;
		case NUMPAD_BUY:
			if(!MatchApi.isDouble(ssh.getNumText()))
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NoDouble")
						.replace("%value%", ssh.getNumText())));
				break;
			}
			ssh.setBuyAmount(Double.parseDouble(ssh.getNumText()));
			if(ssh.getSellAmount() != null
					&& ssh.getBuyAmount() != null && ssh.getSellAmount() > ssh.getBuyAmount())
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("AdminstrationFunctionHandler.SellHigherAsBuy")));
			}
			break;
		case NUMPAD_SELL:
			if(!MatchApi.isDouble(ssh.getNumText()))
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NoDouble")
						.replace("%value%", ssh.getNumText())));
				break;
			}
			ssh.setSellAmount(Double.parseDouble(ssh.getNumText()));
			if(ssh.getSellAmount() != null
					&& ssh.getBuyAmount() != null && ssh.getSellAmount() > ssh.getBuyAmount())
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("AdminstrationFunctionHandler.SellHigherAsBuy")));
			}
			break;
		case NUMPAD_POSSIBLE_BUY:
			if(!MatchApi.isLong(ssh.getNumText()))
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NoNumber")
						.replace("%value%", ssh.getNumText())));
				break;
			}
			ssh.setPossibleBuy(Long.parseLong(ssh.getNumText()));
			break;
		case NUMPAD_POSSIBLE_SELL:
			if(!MatchApi.isLong(ssh.getNumText()))
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NoNumber")
						.replace("%value%", ssh.getNumText())));
				break;
			}
			ssh.setPossibleSell(Long.parseLong(ssh.getNumText()));
			break;
		case NUMPAD_DISCOUNT_BUY:
			if(!MatchApi.isDouble(ssh.getNumText()))
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NoDouble")
						.replace("%value%", ssh.getNumText())));
				break;
			}
			ssh.setDiscountBuyAmount(Double.parseDouble(ssh.getNumText()));
			break;
		case NUMPAD_DISCOUNT_SELL:
			if(!MatchApi.isDouble(ssh.getNumText()))
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NoDouble")
						.replace("%value%", ssh.getNumText())));
				break;
			}
			ssh.setDiscountSellAmount(Double.parseDouble(ssh.getNumText()));
			break;
		case NUMPAD_DISCOUNT_POSSIBLE_BUY:
			if(!MatchApi.isLong(ssh.getNumText()))
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NoNumber")
						.replace("%value%", ssh.getNumText())));
				break;
			}
			ssh.setDiscountPossibleBuy(Long.parseLong(ssh.getNumText()));
			break;
		case NUMPAD_DISCOUNT_POSSIBLE_SELL:
			if(!MatchApi.isLong(ssh.getNumText()))
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NoNumber")
						.replace("%value%", ssh.getNumText())));
				break;
			}
			ssh.setDiscountPossibleSell(Long.parseLong(ssh.getNumText()));
			break;
		case KEYBOARD_SIGNSHOPNAME:
			if(ssh.getNumText().isBlank() || ssh.getNumText().isEmpty())
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("InputIsEmpty")
						.replace("%value%", ssh.getNumText())));
				break;
			}
			ssh.setSignShopName(ssh.getNumText());
			break;
		}
		ssh.setNumText("");
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.SIGNSHOP, ssh, "`id` = ?", ssh.getId());
		GuiHandler.openAdministration(ssh, player, settingsLevel, inv, true);
	}
	
	private static void setNumpadClear(Player player, SignShop ssh, GuiType gt, Inventory inv, SettingsLevel settingsLevel)
	{
		if(isTooMuchShop(player, ssh))
		{
			return;
		}
		if(!SignHandler.isOwner(ssh, player.getUniqueId()) && !SignHandler.isBypassToggle(player.getUniqueId()))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NotOwner")));
			return;
		}
		ssh.setNumText("");
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.SIGNSHOP, ssh, "`id` = ?", ssh.getId());
		GuiHandler.openKeyOrNumInput(ssh, player, gt, settingsLevel, " Numpad", false);
	}
	
	private static void takeOverDiscountTime(Player player, SignShop ssh, GuiType gt, Inventory inv, SettingsLevel settingsLevel, boolean world)
	{
		if(isTooMuchShop(player, ssh))
		{
			return;
		}
		if(!SignHandler.isOwner(ssh, player.getUniqueId()) && !SignHandler.isBypassToggle(player.getUniqueId()))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NotOwner")));
			return;
		}
		switch(gt)
		{
		default: break;
		case NUMPAD_DISCOUNT_HOUR:
			if(!MatchApi.isInteger(ssh.getNumText()))
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NoNumber")
						.replace("%value%", ssh.getNumText())));
				break;
			}
			long h = Long.parseLong(ssh.getNumText());
			long now = System.currentTimeMillis();
			long than = now + h*60*60*1000;
			if(world)
			{
				int a = 0;
				ArrayList<SignShop> sshAL = SignShop.convert(plugin.getMysqlHandler().getFullList(MysqlHandler.Type.SIGNSHOP,
						"`id` ASC", "`player_uuid` = ? AND `server_name` = ? AND `world` = ?",
						ssh.getOwner().toString(), plugin.getServername(), player.getWorld().getName()));
				for(SignShop ss : sshAL)
				{
					ss.setDiscountStart(now);
					ss.setDiscountEnd(than);
					plugin.getMysqlHandler().updateData(MysqlHandler.Type.SIGNSHOP, ssh, "`id` = ?", ssh.getId());
					a++;
				}
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("AdminstrationFunctionHandler.DiscountHourWorld")
						.replace("%amount%", String.valueOf(a))
						.replace("%hour%", String.valueOf(h))));
			} else
			{
				ssh.setDiscountStart(now);
				ssh.setDiscountEnd(than);
			}
			ssh.setNumText("");
			plugin.getMysqlHandler().updateData(MysqlHandler.Type.SIGNSHOP, ssh, "`id` = ?", ssh.getId());
			break;
		case NUMPAD_DISCOUNT_START:
			String p0 = plugin.getYamlHandler().getConfig().getString("SignShop.DiscountTimePattern");
			if(!TimeHandler.isDateTime(ssh.getNumText(), p0))
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("AdminstrationFunctionHandler.DiscountTimeNotFit")
						.replace("%value%", ssh.getNumText())
						.replace("%pattern%", p0)));
				break;
			}
			if(world)
			{
				int a = 0;
				ArrayList<SignShop> sshAL = SignShop.convert(plugin.getMysqlHandler().getFullList(MysqlHandler.Type.SIGNSHOP,
						"`id` ASC", "`player_uuid` = ? AND `server_name` = ? AND `world` = ?",
						ssh.getOwner().toString(), plugin.getServername(), player.getWorld().getName()));
				for(SignShop ss : sshAL)
				{
					ss.setDiscountStart(TimeHandler.getDateTime(ssh.getNumText(),
							p0));
					plugin.getMysqlHandler().updateData(MysqlHandler.Type.SIGNSHOP, ssh, "`id` = ?", ssh.getId());
					a++;
				}
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("AdminstrationFunctionHandler.DiscountStartWorld")
						.replace("%amount%", String.valueOf(a))));
			} else
			{
				ssh.setDiscountStart(TimeHandler.getDateTime(ssh.getNumText(),
						p0));
			}
			ssh.setNumText("");
			plugin.getMysqlHandler().updateData(MysqlHandler.Type.SIGNSHOP, ssh, "`id` = ?", ssh.getId());
			break;
		case NUMPAD_DISCOUNT_END:
			String p1 = plugin.getYamlHandler().getConfig().getString("SignShop.DiscountTimePattern");
			if(!TimeHandler.isDateTime(ssh.getNumText(), p1))
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("AdminstrationFunctionHandler.DiscountTimeNotFit")
						.replace("%value%", ssh.getNumText())
						.replace("%pattern%", p1)));
				break;
			}
			if(world)
			{
				int a = 0;
				ArrayList<SignShop> sshAL = SignShop.convert(plugin.getMysqlHandler().getFullList(MysqlHandler.Type.SIGNSHOP,
						"`id` ASC", "`player_uuid` = ? AND `server_name` = ? AND `world` = ?",
						ssh.getOwner().toString(), plugin.getServername(), player.getWorld().getName()));
				for(SignShop ss : sshAL)
				{
					ss.setDiscountEnd(TimeHandler.getDateTime(ssh.getNumText(),
							p1));
					plugin.getMysqlHandler().updateData(MysqlHandler.Type.SIGNSHOP, ssh, "`id` = ?", ssh.getId());
					a++;
				}
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("AdminstrationFunctionHandler.DiscountEndtWorld")
						.replace("%amount%", String.valueOf(a))));
			} else
			{
				ssh.setDiscountEnd(TimeHandler.getDateTime(ssh.getNumText(),
						p1));
			}
			ssh.setNumText("");
			plugin.getMysqlHandler().updateData(MysqlHandler.Type.SIGNSHOP, ssh, "`id` = ?", ssh.getId());
			break;
		}
		GuiHandler.openAdministration(ssh, player, settingsLevel, inv, true);
	}
	
	private static void numpadRemoveOnce(Player player, SignShop ssh, GuiType gt, Inventory inv, SettingsLevel settingsLevel)
	{
		if(isTooMuchShop(player, ssh))
		{
			return;
		}
		if(!SignHandler.isOwner(ssh, player.getUniqueId()) && !SignHandler.isBypassToggle(player.getUniqueId()))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NotOwner")));
			return;
		}
		if(!ssh.getNumText().isEmpty())
		{
			ssh.setNumText(ssh.getNumText().substring(0, ssh.getNumText().length()-1));
			plugin.getMysqlHandler().updateData(MysqlHandler.Type.SIGNSHOP, ssh, "`id` = ?", ssh.getId());
			GuiHandler.openKeyOrNumInput(ssh, player, gt, settingsLevel, " Numpad", false);
		}
	}
	
	private static void numpad(Player player, SignShop ssh, String num, GuiType gt, Inventory inv, SettingsLevel settingsLevel)
	{
		if(isTooMuchShop(player, ssh))
		{
			return;
		}
		if(!SignHandler.isOwner(ssh, player.getUniqueId()) && !SignHandler.isBypassToggle(player.getUniqueId()))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NotOwner")));
			return;
		}
		ssh.setNumText(ssh.getNumText()+num);
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.SIGNSHOP, ssh, "`id` = ?", ssh.getId());
		GuiHandler.openKeyOrNumInput(ssh, player, gt, settingsLevel, " Numpad", false);
	}
	
	private static void cancelNumpad(Player player, SignShop ssh, Inventory inv, SettingsLevel settingsLevel)
	{
		ssh.setNumText("");
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.SIGNSHOP, ssh, "`id` = ?", ssh.getId());
		GuiHandler.openAdministration(ssh, player, settingsLevel, true);
	}
	
	private static void setKeyboardClear(Player player, SignShop ssh, GuiType gt, Inventory inv, SettingsLevel settingsLevel)
	{
		setNumpadClear(player, ssh, gt, inv, settingsLevel);
	}
	
	private static void keyboardRemoveOnce(Player player, SignShop ssh, GuiType gt, Inventory inv, SettingsLevel settingsLevel)
	{
		numpadRemoveOnce(player, ssh, gt, inv, settingsLevel);
	}
	
	private static void keyboard(Player player, SignShop ssh, String key, GuiType gt, Inventory inv, SettingsLevel settingsLevel)
	{
		if(isTooMuchShop(player, ssh))
		{
			return;
		}
		if(!SignHandler.isOwner(ssh, player.getUniqueId()) && !SignHandler.isBypassToggle(player.getUniqueId()))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NotOwner")));
			return;
		}
		ssh.setNumText(ssh.getNumText()+key);
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.SIGNSHOP, ssh, "`id` = ?", ssh.getId());
		GuiHandler.openKeyOrNumInput(ssh, player, gt, settingsLevel, " Keybord", false);
	}
	
	private static void cancelKeyboard(Player player, SignShop ssh, Inventory inv, SettingsLevel settingsLevel)
	{
		cancelNumpad(player, ssh, inv, settingsLevel);
	}
	
	private static void openKeyboard(Player player, SignShop ssh, GuiType gt, Inventory inv, SettingsLevel settingsLevel)
	{
		if(isTooMuchShop(player, ssh))
		{
			return;
		}
		if(!SignHandler.isOwner(ssh, player.getUniqueId()) && !SignHandler.isBypassToggle(player.getUniqueId()))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NotOwner")));
			return;
		}
		GuiHandler.openKeyOrNumInput(ssh, player, gt, settingsLevel, " Keyboard", true);
	}
	
	private static void addPlayerToList(Player player, SignShop ssh, GuiType gt, Inventory inv, SettingsLevel settingsLevel,
			ListedType listType, UUID otheruuid, boolean remove, boolean world)
	{
		if(otheruuid == null)
		{
			return;
		}
		int a = 0;
		if(world)
		{
			ArrayList<SignShop> sshAL = SignShop.convert(plugin.getMysqlHandler().getFullList(MysqlHandler.Type.SIGNSHOP,
					"`id` ASC", "`player_uuid` = ? AND `server_name` = ? AND `world` = ?",
					ssh.getOwner().toString(), plugin.getServername(), player.getWorld().getName()));
			for(SignShop ss : sshAL)
			{
				if(remove)
				{
					plugin.getMysqlHandler().deleteData(MysqlHandler.Type.SHOPACCESSTYPE,
							"`player_uuid` = ? AND `sign_shop_id` = ? AND `listed_type` = ?",
							otheruuid.toString(), ss.getId(), listType.toString());
				} else
				{
					if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.SHOPACCESSTYPE,
							"`player_uuid` = ? AND `sign_shop_id` = ? AND `listed_type` = ?",
							otheruuid.toString(), ss.getId(), listType.toString()))
					{
						plugin.getMysqlHandler().create(MysqlHandler.Type.SHOPACCESSTYPE,
								new ShopAccessType(0, ss.getId(), otheruuid, listType));
					}
				}
				a++;
			}
		} else
		{
			if(remove)
			{
				plugin.getMysqlHandler().deleteData(MysqlHandler.Type.SHOPACCESSTYPE,
						"`player_uuid` = ? AND `sign_shop_id` = ? AND `listed_type` = ?",
						otheruuid.toString(), ssh.getId(), listType.toString());
			} else
			{
				if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.SHOPACCESSTYPE,
						"`player_uuid` = ? AND `sign_shop_id` = ? AND `listed_type` = ?",
						otheruuid.toString(), ssh.getId(), listType.toString()))
				{
					plugin.getMysqlHandler().create(MysqlHandler.Type.SHOPACCESSTYPE,
							new ShopAccessType(0, ssh.getId(), otheruuid, listType));
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
		GuiHandler.openKeyOrNumInput(ssh, player, gt, settingsLevel, " Keyboard", false);
	}
	
	private static void sendPlayerOnList(Player player, SignShop ssh,
			ListedType listType)
	{
		List<String> players = ShopAccessType.convert(plugin.getMysqlHandler().getFullList(MysqlHandler.Type.SHOPACCESSTYPE,
				"`id` ASC", "`sign_shop_id` = ? AND `listed_type` = ?",	ssh.getId(), listType.toString()))
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