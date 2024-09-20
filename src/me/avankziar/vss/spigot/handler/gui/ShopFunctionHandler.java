package me.avankziar.vss.spigot.handler.gui;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import me.avankziar.ifh.general.economy.account.AccountCategory;
import me.avankziar.ifh.general.economy.action.EconomyAction;
import me.avankziar.ifh.general.economy.action.OrdererType;
import me.avankziar.ifh.spigot.economy.account.Account;
import me.avankziar.ifh.spigot.economy.currency.EconomyCurrency;
import me.avankziar.vss.general.ChatApi;
import me.avankziar.vss.spigot.VSS;
import me.avankziar.vss.spigot.assistance.TimeHandler;
import me.avankziar.vss.spigot.database.MysqlHandler;
import me.avankziar.vss.spigot.database.MysqlHandler.Type;
import me.avankziar.vss.spigot.event.ShopPostTransactionEvent;
import me.avankziar.vss.spigot.event.ShopPreTransactionEvent;
import me.avankziar.vss.spigot.gui.objects.ClickFunctionType;
import me.avankziar.vss.spigot.gui.objects.GuiType;
import me.avankziar.vss.spigot.gui.objects.SettingsLevel;
import me.avankziar.vss.spigot.handler.ConfigHandler;
import me.avankziar.vss.spigot.handler.GuiHandler;
import me.avankziar.vss.spigot.handler.MessageHandler;
import me.avankziar.vss.spigot.handler.SignHandler;
import me.avankziar.vss.spigot.modifiervalueentry.Bypass;
import me.avankziar.vss.spigot.modifiervalueentry.ModifierValueEntry;
import me.avankziar.vss.spigot.objects.ClientDailyLog;
import me.avankziar.vss.spigot.objects.ClientLog;
import me.avankziar.vss.spigot.objects.SignShop;
import me.avankziar.vss.spigot.objects.SubscribedShop;
import me.avankziar.vss.spigot.objects.ClientLog.WayType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.milkbowl.vault.economy.EconomyResponse;

public class ShopFunctionHandler
{
	private static VSS plugin = VSS.getPlugin();
	private static List<Enchantment> enchs = Registry.ENCHANTMENT.stream().collect(Collectors.toList());
	@SuppressWarnings("deprecation")
	private static PotionEffectType[] poefty = PotionEffectType.values();
	
	public static void doClickFunktion(GuiType guiType, ClickFunctionType cft, Player player, SignShop ssh,
			Inventory openInv, SettingsLevel settingsLevel)
	{
		switch(cft)
		{
		default: return;
		case SHOP_BUY_1: buy(player, ssh, 1, openInv, settingsLevel); break;
		case SHOP_BUY_8: buy(player, ssh, 8, openInv, settingsLevel); break;
		case SHOP_BUY_16: buy(player, ssh, 16, openInv, settingsLevel); break;
		case SHOP_BUY_32: buy(player, ssh, 32, openInv, settingsLevel); break;
		case SHOP_BUY_64: buy(player, ssh, 64, openInv, settingsLevel); break;
		case SHOP_BUY_576: buy(player, ssh, 576, openInv, settingsLevel); break;
		case SHOP_BUY_1728: buy(player, ssh, 1728, openInv, settingsLevel); break;
		case SHOP_BUY_2304: buy(player, ssh, 2304, openInv, settingsLevel); break;
		case SHOP_SELL_1: sell(player, ssh, 1, openInv, settingsLevel); break;
		case SHOP_SELL_8: sell(player, ssh, 8, openInv, settingsLevel); break;
		case SHOP_SELL_16: sell(player, ssh, 16, openInv, settingsLevel); break;
		case SHOP_SELL_32: sell(player, ssh, 32, openInv, settingsLevel); break;
		case SHOP_SELL_64: sell(player, ssh, 64, openInv, settingsLevel); break;
		case SHOP_SELL_576: sell(player, ssh, 576, openInv, settingsLevel); break;
		case SHOP_SELL_1728: sell(player, ssh, 1728, openInv, settingsLevel); break;
		case SHOP_SELL_2304: sell(player, ssh, 2304, openInv, settingsLevel); break;
		case SHOP_TOGGLE_SUBSCRIBE: subscribe(player, ssh, openInv, settingsLevel); break;
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
	
	private static boolean isDiscount(SignShop ssh, long now)
	{
		return now >= ssh.getDiscountStart() && now < ssh.getDiscountEnd();
	}
	
	private static int emtpySlots(Player player)
	{
		int es = 0;
		for(int i = 0; i < player.getInventory().getStorageContents().length; i++)
		{
			ItemStack is = player.getInventory().getStorageContents()[i];
			if(is == null || is.getType() == Material.AIR)
			{
				es++;
			}
		}
		return es;
	}
	
	private static boolean doTransaction(Player player, Account from, Account to, double amount, EconomyCurrency ec,
			String category, String comment, Double taxation)
	{
		Account tax = plugin.getIFHEco().getDefaultAccount(from.getOwner().getUUID(), AccountCategory.TAX, ec);
		EconomyAction ea = null;
		if(taxation == null && category != null)
		{
			ea = plugin.getIFHEco().transaction(from, to, amount, OrdererType.PLAYER, player.getUniqueId().toString(), category, comment);
		} else
		{
			boolean taxAreExclusive = false;
			ea = plugin.getIFHEco().transaction(from, to, amount, taxation, taxAreExclusive, tax, 
					OrdererType.PLAYER, player.getUniqueId().toString(), category, comment);
		}
		if(!ea.isSuccess())
		{
			player.sendMessage(ChatApi.tl(ea.getDefaultErrorMessage()));
			return false;
		}
		ArrayList<String> list = new ArrayList<>();
		String wformat = plugin.getIFHEco().format(ea.getWithDrawAmount(), from.getCurrency());
		String dformat = plugin.getIFHEco().format(ea.getDepositAmount(), from.getCurrency());
		String tformat = plugin.getIFHEco().format(ea.getTaxAmount(), from.getCurrency());
		for(String s : plugin.getYamlHandler().getLang().getStringList("ShopFunctionHandler.Transaction"))
		{
			String a = s.replace("%fromaccount%", from.getAccountName())
			.replace("%toaccount%", to.getAccountName())
			.replace("%formatwithdraw%", wformat)
			.replace("%formatdeposit%", dformat)
			.replace("%formattax%", tformat)
			.replace("%category%", category != null ? category : "/")
			.replace("%comment%", comment != null ? comment : "/");
			list.add(a);
		}
		for(String s : list)
		{
			player.sendMessage(ChatApi.tl(s));
		}
		return true;
	}
	
	private static void buy(Player player, SignShop ssh, long amount, Inventory inv, SettingsLevel settingsLevel)
	{
		if(ssh.getItemStack() == null || ssh.getItemStack().getType() == Material.AIR)
		{
			return;
		}
		if(!ssh.canBuy())
		{
			return;
		}
		if(ssh.getItemStorageCurrent() <= 0 && !ssh.isUnlimitedBuy())
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("ShopFunctionHandler.Buy.NoGoodsInStock")));
			String msg = plugin.getYamlHandler().getLang().getString("ShopFunctionHandler.Buy.NoGoodsInStockII")
					.replace("%shopname%", ssh.getSignShopName());
			StringBuilder sb = new StringBuilder();
			int i = 0;
			for(String s : plugin.getYamlHandler().getLang().getStringList("ShopFunctionHandler.InfoHover"))
			{
				if(i > 0)
				{
					sb.append("~!~");
				}
				sb.append(s
						.replace("%client%", player.getName())
						.replace("%item%", ssh.getDisplayName())
						.replace("%amount%", String.valueOf(amount))
						.replace("%price%", isDiscount(ssh, System.currentTimeMillis()) 
								? String.valueOf(amount*ssh.getDiscountBuyAmount()) : String.valueOf(amount*ssh.getBuyAmount()))
						.replace("%server%", ssh.getServer())
						.replace("%world%", ssh.getWorld())
						.replace("%x%", String.valueOf(ssh.getX()))
						.replace("%y%", String.valueOf(ssh.getY()))
						.replace("%z%", String.valueOf(ssh.getZ()))
						);
				i++;
			}
			TextComponent tc1 = ChatApi.tctl(msg);
			TextComponent tc2 = ChatApi.hoverEvent(
					plugin.getYamlHandler().getLang().getString("ShopFunctionHandler.InfoAddition"),
					HoverEvent.Action.SHOW_TEXT, sb.toString());
			ArrayList<BaseComponent> list = new ArrayList<>();
			list.add(tc1);
			list.add(tc2);
			ArrayList<ArrayList<BaseComponent>> listInList = new ArrayList<>();
			listInList.add(list);
			new MessageHandler().sendMessageToOwnerAndMember(ssh, listInList);
			return;
		}
		long now = System.currentTimeMillis();
		Double d = 0.0;
		if(isDiscount(ssh, now))
		{
			d = ssh.getDiscountBuyAmount();
			if((ssh.getDiscountBuyAmount() == null && ssh.getBuyAmount() == null) 
					|| (ssh.getDiscountBuyAmount() < 0.0 && ssh.getBuyAmount() < 0.0))
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("ShopFunctionHandler.Buy.NotInit")));
				return;
			} else if(ssh.getDiscountBuyAmount() == null 
					|| ssh.getDiscountBuyAmount() < 0.0)
			{
				d = ssh.getBuyAmount();
			}
			if(ssh.getDiscountPossibleBuy() == 0)
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("ShopFunctionHandler.Buy.PossibleIsZero")));
				return;
			}
		} else
		{
			d = ssh.getBuyAmount();
			if(d == null || d < 0.0)
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("ShopFunctionHandler.Buy.NotInit")));
				return;
			}
			if(ssh.getPossibleBuy() == 0)
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("ShopFunctionHandler.Buy.PossibleIsZero")));
				return;
			}
		}
		ArrayList<ItemStack> islist = new ArrayList<>();
		int emptySlot = emtpySlots(player);
		if(emptySlot == 0)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("ShopFunctionHandler.NoEmptySlot")));
			return;
		}
		long postc = ssh.getItemStorageCurrent();
		long quantity = amount;
		if(quantity > ssh.getItemStorageCurrent())
		{
			if(!ssh.isUnlimitedBuy())
			{
				quantity = ssh.getItemStorageCurrent();
			}
		}
		if(isDiscount(ssh, now) && ssh.getDiscountPossibleBuy() > 0)
		{
			if(quantity > ssh.getDiscountPossibleBuy())
			{
				quantity = ssh.getDiscountPossibleBuy();
			}
		} else if(ssh.getPossibleBuy() > 0)
		{
			if(quantity > ssh.getPossibleBuy())
			{
				quantity = ssh.getPossibleBuy();
			}
		}
		long samo = quantity;
		while(emptySlot > 0)
		{
			ItemStack is = ssh.getItemStack().clone();
			if(quantity > is.getMaxStackSize())
			{
				is.setAmount(is.getMaxStackSize());
				islist.add(is);
				postc = postc - is.getMaxStackSize();
				quantity =  quantity - is.getMaxStackSize();
			} else if(is.getMaxStackSize() >= quantity)
			{
				is.setAmount((int) quantity);
				islist.add(is);
				postc = postc - quantity;
				quantity = 0;
				break;
			}
			emptySlot--;
		}
		if(quantity != 0)
		{
			samo = samo - quantity;
		}
		Double taxation = plugin.getYamlHandler().getConfig().get("SignShop.Tax.BuyInPercent") != null 
				? plugin.getYamlHandler().getConfig().getDouble("SignShop.Tax.BuyInPercent") : null;
		if(plugin.getModifier() != null)
		{
			taxation = plugin.getModifier().getResult(ssh.getOwner(), taxation, Bypass.Counter.SHOP_BUYING_TAX.getModification());
		}
		ShopPreTransactionEvent sprte = new ShopPreTransactionEvent(ssh, samo, d.doubleValue(), taxation, true, player);
		Bukkit.getPluginManager().callEvent(sprte);
		if(sprte.isCancelled())
		{
			return;
		}
		String category = plugin.getYamlHandler().getLang().getString("Economy.Buy.Category");
		String comment = plugin.getYamlHandler().getLang().getString("Economy.Buy.Comment")
				.replace("%amount%", String.valueOf(samo))
				.replace("%item%", ssh.getItemStack().getItemMeta().hasDisplayName() 
						? ssh.getItemStack().getItemMeta().getDisplayName() 
						: (plugin.getEnumTl() != null 
						  ? VSS.getPlugin().getEnumTl().getLocalization(ssh.getItemStack().getType())
						  : ssh.getItemStack().getType().toString()))
				.replace("%shop%", ssh.getSignShopName());
		if(plugin.getIFHEco() != null)
		{
			Account to = plugin.getIFHEco().getAccount(ssh.getAccountId());
			if(to == null)
			{
				player.sendMessage(ChatApi.tl(
						plugin.getYamlHandler().getLang().getString("ShopFunctionHandler.Buy.ShopHaveNotAccountReady")));
				return;
			}
			Account from = plugin.getIFHEco().getDefaultAccount(player.getUniqueId(), AccountCategory.MAIN, to.getCurrency());
			if(from == null)
			{
				player.sendMessage(ChatApi.tl(
						plugin.getYamlHandler().getLang().getString("ShopFunctionHandler.Buy.YouDontHaveAccountToWithdraw")));
				return;
			}
			if(!doTransaction(player, from, to, samo*d, to.getCurrency(), category, comment, taxation))
			{
				return;
			}
			comment = comment + plugin.getYamlHandler().getLang().getString("Economy.CommentAddition")
					.replace("%format%", plugin.getIFHEco().format(samo*d, from.getCurrency()));
		} else
		{
			if(ssh.getOwner().toString().equals(player.getUniqueId().toString()))
			{
				player.sendMessage(ChatApi.tl(
						plugin.getYamlHandler().getLang().getString("ShopFunctionHandler.CannotTradeInOwnShop")));
				return;
			}
			if(!plugin.getVaultEco().hasAccount(Bukkit.getOfflinePlayer(ssh.getOwner())))
			{
				player.sendMessage(ChatApi.tl(
						plugin.getYamlHandler().getLang().getString("ShopFunctionHandler.Buy.ShopHaveNotAccountReady")));
				return;
			}
			if(!plugin.getVaultEco().hasAccount(player))
			{
				player.sendMessage(ChatApi.tl(
						plugin.getYamlHandler().getLang().getString("ShopFunctionHandler.Buy.YouDontHaveAccountToWithdraw")));
				return;
			}
			taxation = taxation/100.0;
			double w = samo*d;
			if(taxation > 0.0 && taxation < 100.0)
			{
				w = samo*d + samo*d*taxation;
			}
			if(!plugin.getVaultEco().has(player, w))
			{
				player.sendMessage(ChatApi.tl(
						plugin.getYamlHandler().getLang().getString("NotEnought")));
				return;
			}
			EconomyResponse er = plugin.getVaultEco().withdrawPlayer(player, samo*d);
			if(!er.transactionSuccess())
			{
				if(er.errorMessage != null)
				{
					player.sendMessage(ChatApi.tl(er.errorMessage));
				}
				return;
			}
			er = plugin.getVaultEco().depositPlayer(Bukkit.getOfflinePlayer(ssh.getOwner()), w);
			if(!er.transactionSuccess())
			{
				plugin.getVaultEco().depositPlayer(player, w);
				if(er.errorMessage != null)
				{
					player.sendMessage(ChatApi.tl(er.errorMessage));
				}
				return;
			}
			comment = comment + plugin.getYamlHandler().getLang().getString("Economy.CommentAddition")
					.replace("%format%", String.valueOf(samo*d)+" "+ plugin.getVaultEco().currencyNamePlural());
		}
		if(!ssh.isUnlimitedBuy())
		{
			if(ssh.getPossibleBuy() >= 0 || ssh.getDiscountPossibleBuy() >= 0)
			{
				if(isDiscount(ssh, now) && ssh.getDiscountPossibleBuy() > 0)
				{
					ssh.setDiscountPossibleBuy(ssh.getDiscountPossibleBuy()-samo);
				} else if(ssh.getPossibleBuy() > 0)
				{
					ssh.setPossibleBuy(ssh.getPossibleBuy()-samo);
				}
			}
		}
		long date = TimeHandler.getDate(TimeHandler.getDate(System.currentTimeMillis()));
		ClientLog sl = new ClientLog(0, player.getUniqueId(), System.currentTimeMillis(),
				ssh.getItemStack(), ssh.getDisplayName(), ssh.getMaterial(), WayType.BUY, samo*d.doubleValue(), (int) samo,
				ssh.getId());
		plugin.getMysqlHandler().create(MysqlHandler.Type.CLIENTLOG, sl);
		ClientDailyLog sdl = (ClientDailyLog) plugin.getMysqlHandler().getData(MysqlHandler.Type.CLIENTDAILYLOG,
				"`player_uuid` = ? AND `dates` = ?", player.getUniqueId().toString(), date);
		if(sdl == null)
		{
			sdl = new ClientDailyLog(0, player.getUniqueId(), date, samo*d.doubleValue(), 0, (int) samo, 0);
			plugin.getMysqlHandler().create(MysqlHandler.Type.CLIENTDAILYLOG, sdl);
		} else
		{
			sdl.setBuyAmount(sdl.getBuyAmount()+samo*d.doubleValue());
			sdl.setBuyItemAmount(sdl.getBuyItemAmount()+(int) samo);
			plugin.getMysqlHandler().updateData(MysqlHandler.Type.CLIENTDAILYLOG, sdl, "`id` = ?", sdl.getId());
		}
		ShopPostTransactionEvent spote = new ShopPostTransactionEvent(ssh, samo, d.doubleValue(), true, player, category, comment);
		Bukkit.getPluginManager().callEvent(spote);
		if(!ssh.isUnlimitedBuy())
		{
			ssh.setItemStorageCurrent(postc);
		}
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.SIGNSHOP, ssh, "`id` = ?", ssh.getId());
		for(ItemStack is : islist)
		{
			player.getInventory().addItem(is);
		}
		GuiHandler.openShop(ssh, player, settingsLevel, inv, false);
	}
	
	private static void sell(Player player, SignShop ssh, long amount, Inventory inv, SettingsLevel settingsLevel)
	{
		if(ssh.getItemStack() == null || ssh.getItemStack().getType() == Material.AIR)
		{
			return;
		}
		if(!ssh.canSell())
		{
			return;
		}
		if(ssh.getItemStorageCurrent() >= ssh.getItemStorageTotal() && !ssh.isUnlimitedSell())
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("ShopFunctionHandler.Sell.ShopIsFull")));
			String msg = plugin.getYamlHandler().getLang().getString("ShopFunctionHandler.Sell.ShopIsFullII")
					.replace("%shopname%", ssh.getSignShopName());
			StringBuilder sb = new StringBuilder();
			int i = 0;
			for(String s : plugin.getYamlHandler().getLang().getStringList("ShopFunctionHandler.InfoHover"))
			{
				if(i > 0)
				{
					sb.append("~!~");
				}
				sb.append(s
						.replace("%client%", player.getName())
						.replace("%item%", ssh.getDisplayName())
						.replace("%amount%", String.valueOf(amount))
						.replace("%price%", isDiscount(ssh, System.currentTimeMillis()) 
								? String.valueOf(amount*ssh.getDiscountSellAmount()) : String.valueOf(amount*ssh.getSellAmount()))
						.replace("%server%", ssh.getServer())
						.replace("%world%", ssh.getWorld())
						.replace("%x%", String.valueOf(ssh.getX()))
						.replace("%y%", String.valueOf(ssh.getY()))
						.replace("%z%", String.valueOf(ssh.getZ()))
						);
				i++;
			}
			TextComponent tc1 = ChatApi.tctl(msg);
			TextComponent tc2 = ChatApi.hoverEvent(
					plugin.getYamlHandler().getLang().getString("ShopFunctionHandler.InfoAddition"),
					HoverEvent.Action.SHOW_TEXT, sb.toString());
			ArrayList<BaseComponent> list = new ArrayList<>();
			list.add(tc1);
			list.add(tc2);
			ArrayList<ArrayList<BaseComponent>> listInList = new ArrayList<>();
			listInList.add(list);
			new MessageHandler().sendMessageToOwnerAndMember(ssh, listInList);
			return;
		}
		Double d = 0.0;
		long now = System.currentTimeMillis();
		if(isDiscount(ssh, now))
		{
			d = ssh.getDiscountSellAmount();
			if((ssh.getDiscountSellAmount() == null && ssh.getSellAmount() == null) 
					|| (ssh.getDiscountSellAmount() < 0.0 && ssh.getSellAmount() < 0.0))
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("ShopFunctionHandler.Sell.NotInit")));
				return;
			} else if(ssh.getDiscountSellAmount() == null || ssh.getDiscountSellAmount() < 0.0)
			{
				d = ssh.getSellAmount();
			}
			if(ssh.getDiscountPossibleSell() == 0)
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("ShopFunctionHandler.Sell.PossibleIsZero")));
				return;
			}
		} else
		{
			d = ssh.getSellAmount();
			if(d == null || d < 0.0)
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("ShopFunctionHandler.Sell.NotInit")));
				return;
			}
			if(ssh.getPossibleSell() == 0)
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("ShopFunctionHandler.Sell.PossibleIsZero")));
				return;
			}
		}
		ArrayList<ItemStack> islist = new ArrayList<>();
		long postc = ssh.getItemStorageCurrent();
		long quantity = amount;
		if(quantity > ssh.getItemStorageTotal() - ssh.getItemStorageCurrent())
		{
			if(!ssh.isUnlimitedSell())
			{
				quantity = ssh.getItemStorageTotal() - ssh.getItemStorageCurrent();
			}
		}
		if(isDiscount(ssh, now) && ssh.getDiscountPossibleSell() > 0)
		{
			if(quantity > ssh.getDiscountPossibleSell())
			{
				quantity = ssh.getDiscountPossibleSell();
			}
		} else if(ssh.getPossibleSell() > 0)
		{
			if(quantity > ssh.getPossibleSell())
			{
				quantity = ssh.getPossibleSell();
			}
		}
		long samo = quantity;
		long count = 0;
		for(int i = 0; i < player.getInventory().getStorageContents().length; i++)
		{
			ItemStack is = player.getInventory().getStorageContents()[i];
			if(is == null || is.getType() == Material.AIR)
			{
				continue;
			}
			ItemStack c = is.clone();
			c.setAmount(1);
			if(!isSimilar(ssh.getItemStack(), c))
			{
				continue;
			}
			if(quantity == 0)
			{
				break;
			}
			if(quantity > is.getAmount())
			{
				count += is.getAmount();
				postc = postc + is.getAmount();
				quantity = quantity - is.getAmount();
				ItemStack cc = is.clone();
				islist.add(cc);
				is.setAmount(0);
			} else if(quantity <= is.getAmount())
			{
				count += quantity;
				postc = postc + quantity;
				ItemStack cc = is.clone();
				cc.setAmount((int) quantity);
				islist.add(cc);
				is.setAmount(is.getAmount() - (int) quantity);
				quantity = 0;
				break;
			}
		}
		if(count == 0)
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("ShopFunctionHandler.Sell.NoItemInInventory")));
			return;
		}
		if(quantity != 0)
		{
			samo = samo - quantity;
		}
		Double taxation = plugin.getYamlHandler().getConfig().get("SignShop.Tax.SellInPercent") != null 
				? plugin.getYamlHandler().getConfig().getDouble("SignShop.Tax.SellInPercent") : null;
		if(!ssh.isUnlimitedSell())
		{
			if(ssh.getPossibleSell() >= 0 || ssh.getDiscountPossibleSell() >= 0)
			{
				if(isDiscount(ssh, now) && ssh.getDiscountPossibleSell() > 0)
				{
					ssh.setDiscountPossibleSell(ssh.getDiscountPossibleSell()-samo);
				} else if(ssh.getPossibleSell() > 0)
				{
					ssh.setPossibleSell(ssh.getPossibleSell()-samo);
				}
			}
		}
		if(plugin.getModifier() != null)
		{
			taxation = plugin.getModifier().getResult(player.getUniqueId(), taxation, Bypass.Counter.SHOP_SELLING_TAX.getModification());
		}
		ShopPreTransactionEvent sprte = new ShopPreTransactionEvent(ssh, samo, d.doubleValue(), taxation, false, player);
		Bukkit.getPluginManager().callEvent(sprte);
		if(sprte.isCancelled())
		{
			return;
		}
		String category = plugin.getYamlHandler().getLang().getString("Economy.Sell.Category");
		String comment = plugin.getYamlHandler().getLang().getString("Economy.Sell.Comment")
				.replace("%amount%", String.valueOf(samo))
				.replace("%item%", ssh.getItemStack().getItemMeta().hasDisplayName() 
						? ssh.getItemStack().getItemMeta().getDisplayName() 
						: (plugin.getEnumTl() != null 
						  ? VSS.getPlugin().getEnumTl().getLocalization(ssh.getItemStack().getType())
						  : ssh.getItemStack().getType().toString()))
				.replace("%shop%", ssh.getSignShopName());
		if(plugin.getIFHEco() != null)
		{
			Account from = plugin.getIFHEco().getAccount(ssh.getAccountId());
			if(from == null)
			{
				player.sendMessage(ChatApi.tl(
						plugin.getYamlHandler().getLang().getString("ShopFunctionHandler.Sell.ShopHaveNotAccountReady")));
				for(ItemStack is : islist)
				{
					player.getInventory().addItem(is);
				}
				return;
			}
			Account to = plugin.getIFHEco().getDefaultAccount(player.getUniqueId(), AccountCategory.MAIN, from.getCurrency());
			if(to == null)
			{
				player.sendMessage(ChatApi.tl(
						plugin.getYamlHandler().getLang().getString("ShopFunctionHandler.Sell.YouDontHaveAccountToWithdraw")));
				for(ItemStack is : islist)
				{
					player.getInventory().addItem(is);
				}
				return;
			}
			if(!doTransaction(player, from, to, d*samo, to.getCurrency(), category, comment, taxation))
			{
				for(ItemStack is : islist)
				{
					player.getInventory().addItem(is);
				}
				return;
			}
			comment = comment + plugin.getYamlHandler().getLang().getString("Economy.CommentAddition")
					.replace("%format%", plugin.getIFHEco().format(samo*d, from.getCurrency()));
		} else
		{
			if(ssh.getOwner().toString().equals(player.getUniqueId().toString()))
			{
				player.sendMessage(ChatApi.tl(
						plugin.getYamlHandler().getLang().getString("ShopFunctionHandler.CannotTradeInOwnShop")));
				for(ItemStack is : islist)
				{
					player.getInventory().addItem(is);
				}
				return;
			}
			if(!plugin.getVaultEco().hasAccount(Bukkit.getOfflinePlayer(ssh.getOwner())))
			{
				player.sendMessage(ChatApi.tl(
						plugin.getYamlHandler().getLang().getString("ShopFunctionHandler.Buy.ShopHaveNotAccountReady")));
				for(ItemStack is : islist)
				{
					player.getInventory().addItem(is);
				}
				return;
			}
			if(!plugin.getVaultEco().hasAccount(player))
			{
				player.sendMessage(ChatApi.tl(
						plugin.getYamlHandler().getLang().getString("ShopFunctionHandler.Buy.YouDontHaveAccountToWithdraw")));
				for(ItemStack is : islist)
				{
					player.getInventory().addItem(is);
				}
				return;
			}
			taxation = taxation/100.0;
			double w = samo*d;
			if(taxation > 0 && taxation < 100)
			{
				w = samo*d - samo*d*taxation;
			}
			if(!plugin.getVaultEco().has(Bukkit.getOfflinePlayer(ssh.getOwner()), samo*d))
			{
				player.sendMessage(ChatApi.tl(
						plugin.getYamlHandler().getLang().getString("ShopOwnerNotEnought")));
				for(ItemStack is : islist)
				{
					player.getInventory().addItem(is);
				}
				return;
			}
			EconomyResponse er = plugin.getVaultEco().withdrawPlayer(Bukkit.getOfflinePlayer(ssh.getOwner()), samo*d);
			if(!er.transactionSuccess())
			{
				if(er.errorMessage != null)
				{
					player.sendMessage(ChatApi.tl(er.errorMessage));
				}
				for(ItemStack is : islist)
				{
					player.getInventory().addItem(is);
				}
				return;
			}
			er = plugin.getVaultEco().depositPlayer(player, w);
			if(!er.transactionSuccess())
			{
				plugin.getVaultEco().depositPlayer(Bukkit.getOfflinePlayer(ssh.getOwner()), w);
				if(er.errorMessage != null)
				{
					player.sendMessage(ChatApi.tl(er.errorMessage));
				}
				for(ItemStack is : islist)
				{
					player.getInventory().addItem(is);
				}
				return;
			}
			comment = comment + plugin.getYamlHandler().getLang().getString("Economy.CommentAddition")
					.replace("%format%", String.valueOf(samo*d)+" "+plugin.getVaultEco().currencyNamePlural());
		}
		long date = TimeHandler.getDate(TimeHandler.getDate(now));
		ClientLog sl = new ClientLog(0, player.getUniqueId(), now,
				ssh.getItemStack(), ssh.getDisplayName(), ssh.getMaterial(), WayType.SELL, samo*d, (int) samo,
				ssh.getId());
		plugin.getMysqlHandler().create(MysqlHandler.Type.CLIENTLOG, sl);
		ClientDailyLog sdl = (ClientDailyLog) plugin.getMysqlHandler().getData(MysqlHandler.Type.CLIENTDAILYLOG,
				"`player_uuid` = ? AND `dates` = ?", player.getUniqueId().toString(), date);
		if(sdl == null)
		{
			sdl = new ClientDailyLog(0, player.getUniqueId(), date, 0, samo*d, 0, (int) samo);
			plugin.getMysqlHandler().create(MysqlHandler.Type.CLIENTDAILYLOG, sdl);
		} else
		{
			sdl.setSellAmount(sdl.getSellAmount()+samo*d);
			sdl.setSellItemAmount(sdl.getSellItemAmount()+(int) samo);
			plugin.getMysqlHandler().updateData(MysqlHandler.Type.CLIENTDAILYLOG, sdl, "`id` = ?", sdl.getId());
		}
		ShopPostTransactionEvent spote = new ShopPostTransactionEvent(ssh, samo, d.doubleValue(), false, player, category, comment);
		Bukkit.getPluginManager().callEvent(spote);
		if(!ssh.isUnlimitedSell())
		{
			ssh.setItemStorageCurrent(postc);
		}
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.SIGNSHOP, ssh, "`id` = ?", ssh.getId());
		GuiHandler.openShop(ssh, player, settingsLevel, inv, false);
	}
	
	private static void subscribe(Player player, SignShop ssh, Inventory inv, SettingsLevel settingsLevel)
	{
		int defaultmax = new ConfigHandler().getDefaulMaxSubscribeShops();
		SubscribedShop subs = (SubscribedShop) plugin.getMysqlHandler().getData(MysqlHandler.Type.SUBSCRIBEDSHOP, 
				"`player_uuid` = ? AND `sign_shop_id` = ?", player.getUniqueId().toString(), ssh.getId());
		if(subs != null)
		{
			plugin.getMysqlHandler().deleteData(MysqlHandler.Type.SUBSCRIBEDSHOP, "`id` = ?", subs.getId());
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("ShopFunctionHandler.Unsubscribe")
					.replace("%shop%", ssh.getSignShopName())));
		} else
		{
			int has = plugin.getMysqlHandler().getCount(Type.SUBSCRIBEDSHOP, "`player_uuid` = ?", player.getUniqueId().toString());
			if(has >= defaultmax)
			{
				int add = defaultmax + ModifierValueEntry.getResult(player, Bypass.Counter.SHOP_SUBSCRIPTION_);
				if(has >= add)
				{
					player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("ShopFunctionHandler.Subscribes.HasMoreAsAllowed")
							.replace("%has%", String.valueOf(has))
							.replace("%allowed%", String.valueOf(add))));
					return;
				}
			}
			subs = new SubscribedShop(0, player.getUniqueId(), ssh.getId(), System.currentTimeMillis());
			plugin.getMysqlHandler().create(MysqlHandler.Type.SUBSCRIBEDSHOP, subs);
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("ShopFunctionHandler.Subscribe")
					.replace("%shop%", ssh.getSignShopName())));
		}
		GuiHandler.openShop(ssh, player, settingsLevel, inv, false);
	}
	
	public static boolean isSimilar(ItemStack item, ItemStack filter)
	{
		if (item == null || filter == null) 
        {
            return true;
        }
        final ItemStack i = item.clone();
        final ItemStack f = filter.clone();
        i.setAmount(1);
        f.setAmount(1);
        return VSS.getPlugin().getItemStackComparison().isSimilar(i, f);
	}
	
	public static String toBase64(ItemStack is)
	{
		if(is == null)
		{
			return null;
		}
		try 
		{
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeObject(is);
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) 
		{
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
	}
	
	public static ItemMeta orderEnchantments(ItemMeta i)
	{
		ItemMeta ri = i.clone();
		for(Enchantment enchan : i.getEnchants().keySet())
		{
			ri.removeEnchant(enchan);
		}
		for(Enchantment enchan : enchs)
		{
			if(i.hasEnchant(enchan))
			{
				ri.addEnchant(enchan, i.getEnchantLevel(enchan), true);
			}
		}
		return ri;
	}
	
	public static EnchantmentStorageMeta orderStorageEnchantments(EnchantmentStorageMeta esm)
	{
		EnchantmentStorageMeta resm = esm.clone();
		for(Enchantment enchan : esm.getStoredEnchants().keySet())
		{
			resm.removeStoredEnchant(enchan);
		}
		for(Enchantment enchan : enchs)
		{
			if(esm.hasStoredEnchant(enchan))
			{
				resm.addStoredEnchant(enchan, esm.getStoredEnchantLevel(enchan), true);
			}
		}
		return resm;
	}
	
	public static PotionMeta orderCustomEffects(PotionMeta p)
	{
		PotionMeta pm = p.clone();
		LinkedHashMap<PotionEffectType, PotionEffect> pel = new LinkedHashMap<>();
		for(PotionEffect pe : p.getCustomEffects())
		{
			pel.put(pe.getType(), pe);
			pm.removeCustomEffect(pe.getType());
		}
		for(PotionEffectType pet : poefty)
		{
			if(pel.containsKey(pet))
			{
				pm.addCustomEffect(pel.get(pet), true);
			}
		}
		return pm;
	}
}