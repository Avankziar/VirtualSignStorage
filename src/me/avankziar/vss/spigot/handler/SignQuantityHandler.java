package me.avankziar.vss.spigot.handler;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.block.sign.SignSide;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.avankziar.vss.general.ChatApi;
import me.avankziar.vss.general.database.MysqlType;
import me.avankziar.vss.general.objects.ListedType;
import me.avankziar.vss.general.objects.SignQStorage;
import me.avankziar.vss.general.objects.StorageAccessType.StorageType;
import me.avankziar.vss.spigot.VSS;

public class SignQuantityHandler
{
	private static VSS plugin = VSS.getPlugin();
	public static ArrayList<String> bypassToggle = new ArrayList<>();
	public static ArrayList<String> breakToggle = new ArrayList<>();
	
	public static String getSignLine(int index, SignQStorage sst, Block b)
	{
		switch(index)
		{
		default:
		case 0:
			if(sst.getMaterial() == Material.AIR)
			{
				return plugin.getYamlHandler().getLang().getString("SignChangeListener.MaterialIsAir");
			}
			return MaterialHandler.getMaterial(sst.getMaterial(), b != null ? b.getType() : Material.ACACIA_BOAT);
		case 1:
			String c = MaterialHandler.getSignColor( b != null ? b.getType() : Material.ACACIA_BOAT);
			c = plugin.getYamlHandler().getLang().getString("SignChangeListener.Input") 
					+ c + String.valueOf(sst.getItemInput()) + " / " + String.valueOf(sst.getItemShiftInput());
			return c;
		case 2:
			String a = MaterialHandler.getSignColor( b != null ? b.getType() : Material.ACACIA_BOAT);
			a = plugin.getYamlHandler().getLang().getString("SignChangeListener.Output") 
					+ a + String.valueOf(sst.getItemOutput()) + " / " + String.valueOf(sst.getItemShiftOutput());
			return a;
		case 3:
			StringBuilder sb = new StringBuilder();
			String color = "";
			boolean calInStack = ConfigHandler.isLine4CalculateInStack();
			long has = sst.getItemStorageCurrent();
			if(calInStack)
			{
				has = has/64;
			}
			if(sst.isUnlimited()) 
			{
				color = getPercentColor(100, 100);
				sb.append(color+"ꝏ");
			} else
			{
				color = getPercentColor(sst.getItemStorageTotal(), has);
				if(has > 99999)
				{
					sb.append(color+"99999+");
				} else
				{
					sb.append(color+has);
				}
			}
			return sb.toString();
		}
	}
	
	public static String getPercentColor(long max, long actual)
	{
		double perc = ((double) actual)/((double) max) * 100.0;
		if(perc >= 100.0){return plugin.getYamlHandler().getLang().getString("SignQuantityHandler.PercentColor.100AndAbove");}
		else if(perc < 100.0 && perc >= 75.0){return plugin.getYamlHandler().getLang().getString("SignQuantityHandler.PercentColor.Between100And75");}
		else if(perc < 75.0 && perc >= 50.0){return plugin.getYamlHandler().getLang().getString("SignQuantityHandler.PercentColor.Between75And50");}
		else if(perc < 50.0 && perc >= 25.0){return plugin.getYamlHandler().getLang().getString("SignQuantityHandler.PercentColor.Between50And25");}
		else if(perc < 25.0 && perc >= 10.0){return plugin.getYamlHandler().getLang().getString("SignQuantityHandler.PercentColor.Between25And10");}
		else if(perc < 10.0 && perc > 0.0){return plugin.getYamlHandler().getLang().getString("SignQuantityHandler.PercentColor.Between10And0");}
		else {return plugin.getYamlHandler().getLang().getString("SignQuantityHandler.PercentColor.0AndLess");}
	}
	
	public static boolean isOwner(SignQStorage sst, UUID uuid)
	{
		return sst != null ? sst.getOwner().toString().equals(uuid.toString()) : false;
	}
	
	
	public static boolean isBypassToggle(UUID uuid)
	{
		return bypassToggle.contains(uuid.toString());
	}
	
	public static boolean isBreakToggle(UUID uuid)
	{
		return breakToggle.contains(uuid.toString());
	}
	
	public static boolean isListed(ListedType listedType, SignQStorage sst, UUID uuid)
	{
		return plugin.getMysqlHandler().exist(MysqlType.STORAGEACCESSTYPE, 
				"`player_uuid` = ? AND `sign_storage_id` = ? AND `storage_type` = ? AND `listed_type` = ?",
				uuid.toString(), sst.getId(), StorageType.QUANTITY.toString(), listedType.toString());
	}
	
	public static boolean putInItemIntoStorage(SignQStorage sst, Player player, ItemStack toPutIn)
	{
		if(sst.getItemStorageCurrent() >= sst.getItemStorageTotal())
		{
			return false;
		}
		final boolean isShift = player.isSneaking();
		ItemStack c = toPutIn.clone();
		c.setAmount(1);
		int amount = 0;
		if(isShift)
		{
			amount = (int) sst.getItemShiftInput();
		} else
		{
			amount = (int) sst.getItemInput();
		}
		int amo = 0;
		for(int i = 0; i < player.getInventory().getStorageContents().length; i++)
		{
			ItemStack is = player.getInventory().getStorageContents()[i];
			if(is == null || is.getType() == Material.AIR)
			{
				continue;
			}
			ItemStack cc = is.clone();
			cc.setAmount(1);
			if(!ItemAndInvHandler.isSimilar(cc, sst.getItemStack()))
			{
				continue;
			}
			if(amo < amount)
			{
				int a = amount - amo;
				if(a >= is.getAmount())
				{
					a = is.getAmount();
				}
				if(sst.getItemStorageTotal() <= sst.getItemStorageCurrent() + a)
				{
					int diff = (int) (sst.getItemStorageTotal() - sst.getItemStorageTotal());
					sst.setItemStorageCurrent(sst.getItemStorageTotal());
					is.setAmount(is.getAmount() - diff);
					amo += diff;
					break;
				} else
				{
					sst.setItemStorageCurrent(sst.getItemStorageCurrent() + a);
					is.setAmount(is.getAmount()-a);
					amo += a;
				}
			}
		}
		putInItemIntoStorageMsg(sst, amo, player);
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				SignQuantityHandler.updateSign(sst);
			}
		}.runTask(plugin);
		return true;
	}
	
	private static void putInItemIntoStorageMsg(SignQStorage sst, long amount, Player player)
	{
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("SignQuantityHandler.ItemsAddedToStorage")
				.replace("%amount%", String.valueOf(amount))
				.replace("%now%", String.valueOf(sst.getItemStorageCurrent())+" / "+String.valueOf(sst.getItemStorageTotal()))));
		plugin.getMysqlHandler().updateData(MysqlType.SIGNQSTORAGE, sst, "`id` = ?", sst.getId());
	}
	
	public static void takeOutItemFromStorage(SignQStorage sst, Player player)
	{
		final boolean isShift = player.isSneaking();
		if(sst.getItemStack() == null || sst.getItemStack().getType() == Material.AIR)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("SignQuantityHandler.NoItemsIsSetUp")));
			return;
		}
		int amount = 0;
		if(isShift)
		{
			amount = (int)sst.getItemShiftOutput();
		} else
		{
			amount = (int)sst.getItemOutput();
		}
		ArrayList<ItemStack> list = new ArrayList<>();
		int amo = 0;
		for(int i = 0; i < player.getInventory().getStorageContents().length; i++)
		{
			ItemStack is = player.getInventory().getStorageContents()[i];
			if(is != null)
			{
				continue;
			}			
			if(amo < amount)
			{
				ItemStack out = sst.getItemStack().clone();
				int a = amount - amo;
				if(a > out.getMaxStackSize())
				{
					a = out.getMaxStackSize();
				}
				if(a >= sst.getItemStorageCurrent())
				{
					out.setAmount((int) sst.getItemStorageCurrent());
					amo += (int) sst.getItemStorageCurrent();
					sst.setItemStorageCurrent(0);
					list.add(out);
					break;
				} else
				{
					out.setAmount(a);
					amo += a;
					sst.setItemStorageCurrent(sst.getItemStorageCurrent()-a);
					list.add(out);
				}
				continue;
			}
			break;
		}
		for(ItemStack is : list)
		{
			player.getInventory().addItem(is);
		}
		takeOutItemFromStorageMsg(sst, amo, player);
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				SignQuantityHandler.updateSign(sst);
			}
		}.runTask(plugin);
	}
	
	private static void takeOutItemFromStorageMsg(SignQStorage sst, long amount, Player player)
	{
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("SignQuantityHandler.ItemsRemovedToStorage")
				.replace("%amount%", String.valueOf(amount))
				.replace("%now%", String.valueOf(sst.getItemStorageCurrent())+" / "+String.valueOf(sst.getItemStorageTotal()))));
		plugin.getMysqlHandler().updateData(MysqlType.SIGNQSTORAGE, sst, "`id` = ?", sst.getId());
	}
	
	public static void updateSign(SignQStorage ssh)
	{
		World w = Bukkit.getWorld(ssh.getWorld());
		if(w == null)
		{
			return;
		}
		Block b = w.getBlockAt(ssh.getX(), ssh.getY(), ssh.getZ());
		if(b == null)
		{
			return;
		}
		BlockState bs = b.getState();
		if(!(bs instanceof Sign))
		{
			return;
		}
		Sign sign = (Sign) bs;
		sign.setWaxed(true);
		SignSide front = sign.getSide(Side.FRONT);
		front.setLine(0, ChatApi.tl(SignQuantityHandler.getSignLine(0, ssh, b)));
		front.setLine(1, ChatApi.tl(SignQuantityHandler.getSignLine(1, ssh, b)));
		front.setLine(2, ChatApi.tl(SignQuantityHandler.getSignLine(2, ssh, b)));
		front.setLine(3, ChatApi.tl(SignQuantityHandler.getSignLine(3, ssh, b)));
		front.setGlowingText(ssh.isSignGlowing());
		SignSide back = sign.getSide(Side.BACK);
		back.setLine(0, ChatApi.tl(SignQuantityHandler.getSignLine(0, ssh, b)));
		back.setLine(1, ChatApi.tl(SignQuantityHandler.getSignLine(1, ssh, b)));
		back.setLine(2, ChatApi.tl(SignQuantityHandler.getSignLine(2, ssh, b)));
		back.setLine(3, ChatApi.tl(SignQuantityHandler.getSignLine(3, ssh, b)));
		back.setGlowingText(ssh.isSignGlowing());
		sign.update();
		//Here NO Block Metadata set!!!
	}
	
	public static void clearSign(Block block)
	{
		if(!(block.getState() instanceof Sign))
		{
			return;
		}
		Sign sign = (Sign) block.getState();
		SignSide front = sign.getSide(Side.FRONT);
		front.setLine(0, "");
		front.setLine(1, "");
		front.setLine(2, "");
		front.setLine(3, "");
		front.setGlowingText(false);
		SignSide back = sign.getSide(Side.BACK);
		back.setLine(0, "");
		back.setLine(1, "");
		back.setLine(2, "");
		back.setLine(3, "");
		back.setGlowingText(false);
		sign.update();
	}
	
	public static String formatDouble(double d)
	{
		//String locale = SaLE.getPlugin().getYamlHandler().getConfig().getString("SignShop.Sign.LocaleForDecimalAndThousandSeperator", "ENGLISH");
		DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.getDefault());
		formatter.setMaximumFractionDigits(3);
		formatter.setMinimumFractionDigits(0);
		return formatter.format(d);
	}
}