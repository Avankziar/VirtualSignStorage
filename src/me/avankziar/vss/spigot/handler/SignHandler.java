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
import me.avankziar.vss.general.objects.SignStorage;
import me.avankziar.vss.spigot.VSS;
import me.avankziar.vss.spigot.handler.gui._ShopFunctionHandler;

public class SignHandler
{
	private static VSS plugin = VSS.getPlugin();
	public static ArrayList<String> bypassToggle = new ArrayList<>();
	public static ArrayList<String> breakToggle = new ArrayList<>();
	
	public static String getSignLine(int index, SignStorage ssh, Block b)
	{
		switch(index)
		{
		default:
		case 0:
			if(ssh.getMaterial() == Material.AIR)
			{
				return plugin.getYamlHandler().getLang().getString("SignChangeListener.MaterialIsAir");
			}
			return MaterialHandler.getMaterial(ssh.getMaterial(), b != null ? b.getType() : Material.ACACIA_BOAT);
		case 1:
				
		case 2:
			
		case 3:
			StringBuilder sb = new StringBuilder();
			String color = "";
			boolean calInStack = plugin.getYamlHandler().getConfig().getBoolean("SignShop.Sign.Line4CalculateInStack", false);
			long has = ssh.getItemStorageCurrent();
			long can = ssh.getItemStorageTotal()-ssh.getItemStorageCurrent();
			if(calInStack)
			{
				has = has/64;
				can = can/64;
			}
			//normal possibleBuy/sell
			if(ssh.isUnlimited()) 
			{
				color = getPercentColor(100, 100);
				sb.append(color+"ꝏ");
			} else
			{
				color = getPercentColor(ssh.getItemStorageTotal(), has);
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
		if(perc >= 100.0){return plugin.getYamlHandler().getLang().getString("SignHandler.PercentColor.100AndAbove");}
		else if(perc < 100.0 && perc >= 75.0){return plugin.getYamlHandler().getLang().getString("SignHandler.PercentColor.Between100And75");}
		else if(perc < 75.0 && perc >= 50.0){return plugin.getYamlHandler().getLang().getString("SignHandler.PercentColor.Between75And50");}
		else if(perc < 50.0 && perc >= 25.0){return plugin.getYamlHandler().getLang().getString("SignHandler.PercentColor.Between50And25");}
		else if(perc < 25.0 && perc >= 10.0){return plugin.getYamlHandler().getLang().getString("SignHandler.PercentColor.Between25And10");}
		else if(perc < 10.0 && perc > 0.0){return plugin.getYamlHandler().getLang().getString("SignHandler.PercentColor.Between10And0");}
		else {return plugin.getYamlHandler().getLang().getString("SignHandler.PercentColor.0AndLess");}
	}
	
	public static boolean isOwner(SignStorage ssh, UUID uuid)
	{
		return ssh != null ? ssh.getOwner().toString().equals(uuid.toString()) : false;
	}
	
	
	public static boolean isBypassToggle(UUID uuid)
	{
		return bypassToggle.contains(uuid.toString());
	}
	
	public static boolean isBreakToggle(UUID uuid)
	{
		return breakToggle.contains(uuid.toString());
	}
	
	public static boolean isListed(ListedType listedType, SignStorage ssh, UUID uuid)
	{
		return plugin.getMysqlHandler().exist(MysqlType.STORAGEACCESSTYPE, 
				"`player_uuid` = ? AND `sign_shop_id` = ? AND `listed_type` = ?",
				uuid.toString(), ssh.getId(), listedType.toString());
	}
	
	public static boolean putInItemIntoShop(SignStorage ssh, Player player, ItemStack toPutIn)
	{
		if(ssh.getItemStorageCurrent() >= ssh.getItemStorageTotal())
		{
			return false;
		}
		final boolean isShift = player.isSneaking();
		ItemStack c = toPutIn.clone();
		c.setAmount(1);
		int amount = 0;
		if(isShift)
		{
			if(!ssh.getItemStack().toString().equals(c.toString()))
			{
				return false;
			}
			for(int i = 0; i < player.getInventory().getStorageContents().length; i++)
			{
				ItemStack is = player.getInventory().getStorageContents()[i];
				if(is == null || is.getType() == Material.AIR)
				{
					continue;
				}
				ItemStack cc = is.clone();
				cc.setAmount(1);
				if(!_ShopFunctionHandler.isSimilar(ssh.getItemStack(), cc))
				{
					continue;
				}
				if(ssh.getItemStorageTotal() <= ssh.getItemStorageCurrent() + amount + is.getAmount())
				{
					long v = ssh.getItemStorageTotal() - ssh.getItemStorageCurrent() - amount;
					amount += v;
					is.setAmount(is.getAmount() - (int) v);
					break;
				}
				amount += is.getAmount();
				is.setAmount(0);
			}
			putInItemIntoShopMsg(ssh, amount, player);
		} else
		{
			if(toPutIn == null || toPutIn.getType() == Material.AIR)
			{
				return false;
			}
			if(!_ShopFunctionHandler.isSimilar(toPutIn, ssh.getItemStack()))
			{
				return false;
			}
			if(ssh.getItemStorageTotal() < ssh.getItemStorageCurrent())
			{
				return false;
			} else if(ssh.getItemStorageTotal() < ssh.getItemStorageCurrent() + toPutIn.getAmount())
			{
				long v = ssh.getItemStorageTotal() - ssh.getItemStorageCurrent();
				amount += v;
				toPutIn.setAmount(toPutIn.getAmount()-(int) v);
				putInItemIntoShopMsg(ssh, amount, player);
				
			} else
			{
				amount = toPutIn.getAmount();
				toPutIn.setAmount(0);
				putInItemIntoShopMsg(ssh, amount, player);
			}
		}
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				SignHandler.updateSign(ssh);
			}
		}.runTask(plugin);
		return true;
	}
	
	private static void putInItemIntoShopMsg(SignStorage ssh, long amount, Player player)
	{
		ssh.setItemStorageCurrent(ssh.getItemStorageCurrent()+((long) amount));
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("SignHandler.ItemsAddedToShop")
				.replace("%amount%", String.valueOf(amount))
				.replace("%now%", String.valueOf(ssh.getItemStorageCurrent())+" / "+String.valueOf(ssh.getItemStorageTotal()))));
		plugin.getMysqlHandler().updateData(MysqlType.SIGNSTORAGE, ssh, "`id` = ?", ssh.getId());
	}
	
	public static void takeOutItemFromShop(SignStorage ssh, Player player)
	{
		final boolean isShift = player.isSneaking();
		if(ssh.getItemStack() == null || ssh.getItemStack().getType() == Material.AIR)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("SignHandler.NoItemsIsSetUp")));
			return;
		}
		int amount = 0;
		if(isShift)
		{
			ArrayList<ItemStack> list = new ArrayList<>();
			for(int i = 0; i < player.getInventory().getStorageContents().length; i++)
			{
				ItemStack is = player.getInventory().getStorageContents()[i];
				if(is != null)
				{
					continue;
				}
				ItemStack out = ssh.getItemStack().clone();
				int amo = out.getMaxStackSize();
				if(out.getMaxStackSize() > ssh.getItemStorageCurrent())
				{
					amo = (int) ssh.getItemStorageCurrent();
					out.setAmount(amo);
					amount += amo;
					ssh.setItemStorageCurrent(ssh.getItemStorageCurrent()-amo);
					list.add(out);
					break;
				} else
				{
					out.setAmount(amo);
					amount += amo;
					ssh.setItemStorageCurrent(ssh.getItemStorageCurrent()-amo);
					list.add(out);
				}
			}
			for(ItemStack is : list)
			{
				player.getInventory().addItem(is);
			}
			takeOutItemFromShopMsg(ssh, amount, player);
		} else
		{
			ItemStack out = ssh.getItemStack().clone();
			amount = out.getMaxStackSize();
			if(amount > ssh.getItemStorageCurrent())
			{
				amount = (int) ssh.getItemStorageCurrent();
				out.setAmount(amount);
				ssh.setItemStorageCurrent(0);
			} else
			{
				out.setAmount(amount);
				ssh.setItemStorageCurrent(ssh.getItemStorageCurrent()-amount);
			}
			player.getInventory().setItemInMainHand(out);
			takeOutItemFromShopMsg(ssh, amount, player);		
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
	
	private static void takeOutItemFromShopMsg(SignStorage ssh, long amount, Player player)
	{
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("SignHandler.ItemsRemovedToShop")
				.replace("%amount%", String.valueOf(amount))
				.replace("%now%", String.valueOf(ssh.getItemStorageCurrent())+" / "+String.valueOf(ssh.getItemStorageTotal()))));
		plugin.getMysqlHandler().updateData(MysqlType.SIGNSTORAGE, ssh, "`id` = ?", ssh.getId());
	}
	
	public static void updateSign(SignStorage ssh)
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
		front.setLine(0, ChatApi.tl(SignHandler.getSignLine(0, ssh, b)));
		front.setLine(1, ChatApi.tl(SignHandler.getSignLine(1, ssh, b)));
		front.setLine(2, ChatApi.tl(SignHandler.getSignLine(2, ssh, b)));
		front.setLine(3, ChatApi.tl(SignHandler.getSignLine(3, ssh, b)));
		front.setGlowingText(ssh.isSignGlowing());
		SignSide back = sign.getSide(Side.BACK);
		back.setLine(0, ChatApi.tl(SignHandler.getSignLine(0, ssh, b)));
		back.setLine(1, ChatApi.tl(SignHandler.getSignLine(1, ssh, b)));
		back.setLine(2, ChatApi.tl(SignHandler.getSignLine(2, ssh, b)));
		back.setLine(3, ChatApi.tl(SignHandler.getSignLine(3, ssh, b)));
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
	
	public static Locale locale = null;
	
	static
	{
		
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