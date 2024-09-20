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
import me.avankziar.vss.spigot.SaLE;
import me.avankziar.vss.spigot.database.MysqlHandler;
import me.avankziar.vss.spigot.handler.gui.ShopFunctionHandler;
import me.avankziar.vss.spigot.objects.ListedType;
import me.avankziar.vss.spigot.objects.SignShop;

public class SignHandler
{
	private static SaLE plugin = SaLE.getPlugin();
	public static ArrayList<String> bypassToggle = new ArrayList<>();
	public static ArrayList<String> breakToggle = new ArrayList<>();
	
	public static boolean isDiscount(SignShop ssh, long now)
	{
		return now >= ssh.getDiscountStart() && now < ssh.getDiscountEnd();
	}
	
	public static String getSignLine(int index, SignShop ssh, Block b)
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
			if(!ssh.canBuy())
			{
				return plugin.getYamlHandler().getLang().getString("SignHandler.Line1")
						.replace("%amount%", "&4--");
			}
			if(isDiscount(ssh, System.currentTimeMillis()))
			{ 
				if(ssh.getDiscountBuyAmount() == null || ssh.getDiscountBuyAmount() < 0.0)
				{
					if(ssh.getBuyAmount() != null && ssh.getBuyAmount() > 0.0)
					{
						return plugin.getYamlHandler().getLang().getString("SignHandler.Line1")
								.replace("%amount%", MaterialHandler.getSignColor(b.getType())+String.valueOf(formatDouble(ssh.getBuyAmount())));
					}
					return plugin.getYamlHandler().getLang().getString("SignHandler.Line1")
							.replace("%amount%", "&4--");
				}
				return plugin.getYamlHandler().getLang().getString("SignHandler.Line1Discount")
						.replace("%amount%", String.valueOf(ssh.getDiscountBuyAmount()));
			} else
			{
				if(ssh.getBuyAmount() == null || ssh.getBuyAmount() < 0.0)
				{
					return plugin.getYamlHandler().getLang().getString("SignHandler.Line1")
							.replace("%amount%", "&4--");
				}
				return plugin.getYamlHandler().getLang().getString("SignHandler.Line1")
						.replace("%amount%", MaterialHandler.getSignColor(b.getType())+String.valueOf(formatDouble(ssh.getBuyAmount())));
			}			
		case 2:
			if(!ssh.canSell())
			{
				return plugin.getYamlHandler().getLang().getString("SignHandler.Line2")
						.replace("%amount%", "&4--");
			}
			if(isDiscount(ssh, System.currentTimeMillis()))
			{
				if(ssh.getDiscountSellAmount() == null || ssh.getDiscountSellAmount() < 0.0)
				{
					if(ssh.getSellAmount() != null && ssh.getSellAmount() > 0.0)
					{
						return plugin.getYamlHandler().getLang().getString("SignHandler.Line2")
								.replace("%amount%", MaterialHandler.getSignColor(b.getType())+String.valueOf(formatDouble(ssh.getSellAmount())));
					}
					return plugin.getYamlHandler().getLang().getString("SignHandler.Line2")
							.replace("%amount%", "&4--");
				}
				return plugin.getYamlHandler().getLang().getString("SignHandler.Line2Discount")
						.replace("%amount%", String.valueOf(ssh.getDiscountSellAmount()));
			} else
			{
				if(ssh.getSellAmount() == null || ssh.getSellAmount() < 0.0)
				{
					return plugin.getYamlHandler().getLang().getString("SignHandler.Line2")
							.replace("%amount%", "&4--");
				}
				return plugin.getYamlHandler().getLang().getString("SignHandler.Line2")
						.replace("%amount%", MaterialHandler.getSignColor(b.getType())+String.valueOf(formatDouble(ssh.getSellAmount())));
			}
		case 3:
			StringBuilder sb = new StringBuilder();
			String colorB = "";
			String colorS = "";
			boolean calInStack = plugin.getYamlHandler().getConfig().getBoolean("SignShop.Sign.Line4CalculateInStack", false);
			long now = System.currentTimeMillis();
			long buy = ssh.getItemStorageCurrent();
			long sell = ssh.getItemStorageTotal()-ssh.getItemStorageCurrent();
			if(calInStack)
			{
				buy = buy/64;
				sell = sell/64;
			}
			if(ssh.getDiscountStart() < now && ssh.getDiscountEnd() > now)
			{
				//DiscountpossibleBuy/sell
				if(ssh.isUnlimitedBuy()) 
				{
					colorB = getPercentColor(100, 100);
					sb.append(colorB+"ꝏ"); //https://fsymbols.com/de/zeichen/unendlichkeit/
				} else if(ssh.getDiscountPossibleBuy() >= 0)
				{
					colorB = getPercentColor(
							Math.max(buy, ssh.getDiscountPossibleBuy()),
							Math.min(buy, ssh.getDiscountPossibleBuy()));
					buy = Math.min(buy, ssh.getDiscountPossibleBuy());
					if(buy > 99999)
					{
						sb.append(colorB+"99999+");
					} else
					{
						sb.append(colorB+String.valueOf(buy));
					}
				} else
				{
					colorB = getPercentColor(ssh.getItemStorageTotal(), buy);
					if(buy > 99999)
					{
						sb.append(colorB+"99999+");
					} else
					{
						sb.append(colorB+buy);
					}
				}
				sb.append(" &r/ ");
				if(ssh.isUnlimitedSell()) 
				{
					colorS = getPercentColor(100, 100);
					sb.append(colorS+"ꝏ");
				} else if(ssh.getDiscountPossibleSell() >= 0)
				{
					colorS = getPercentColor(
							Math.max(sell, ssh.getDiscountPossibleSell()),
							Math.min(sell, ssh.getDiscountPossibleSell()));
					sell = Math.min(sell, ssh.getDiscountPossibleSell());
					if(sell > 99999)
					{
						sb.append(colorS+"99999+");
					} else
					{
						sb.append(colorS+String.valueOf(sell));
					}
				} else
				{
					colorS = getPercentColor(ssh.getItemStorageTotal(), sell);
					if(sell > 99999)
					{
						sb.append(colorS+"99999+");
					} else
					{
						sb.append(colorS+sell);
					}
				}
				return sb.toString();
			} else
			{
				//normal possibleBuy/sell
				if(ssh.isUnlimitedBuy()) 
				{
					colorB = getPercentColor(100, 100);
					sb.append(colorB+"ꝏ");
				} else if(ssh.getPossibleBuy() >= 0)
				{
					colorB = getPercentColor(
							Math.max(buy, ssh.getPossibleBuy()),
							Math.min(buy, ssh.getPossibleBuy()));
					buy = Math.min(buy, ssh.getPossibleBuy());
					if(buy > 99999)
					{
						sb.append(colorB+"99999+");
					} else
					{
						sb.append(colorB+String.valueOf(buy));
					}
				} else
				{
					colorB = getPercentColor(ssh.getItemStorageTotal(), buy);
					if(buy > 99999)
					{
						sb.append(colorB+"99999+");
					} else
					{
						sb.append(colorB+buy);
					}
				}
				sb.append(" &r/ ");
				if(ssh.isUnlimitedSell()) 
				{
					colorS = getPercentColor(100, 100);
					sb.append(colorS+"ꝏ");
				} else if(ssh.getPossibleSell() >= 0)
				{
					colorS = getPercentColor(
							Math.max(sell, ssh.getPossibleSell()),
							Math.min(sell, ssh.getPossibleSell()));
					sell = Math.min(sell, ssh.getPossibleSell());
					if(sell > 99999)
					{
						sb.append(colorS+"99999+");
					} else
					{
						sb.append(colorS+String.valueOf(sell));
					}
				} else
				{
					colorS = getPercentColor(ssh.getItemStorageTotal(), sell);
					if(sell > 99999)
					{
						sb.append(colorS+"99999+");
					} else
					{
						sb.append(colorS+sell);
					}
				}
				return sb.toString();
			}
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
	
	public static boolean isOwner(SignShop ssh, UUID uuid)
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
	
	public static boolean isListed(ListedType listedType, SignShop ssh, UUID uuid)
	{
		return plugin.getMysqlHandler().exist(MysqlHandler.Type.SHOPACCESSTYPE, 
				"`player_uuid` = ? AND `sign_shop_id` = ? AND `listed_type` = ?",
				uuid.toString(), ssh.getId(), listedType.toString());
	}
	
	public static boolean putInItemIntoShop(SignShop ssh, Player player, ItemStack toPutIn)
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
				if(!ShopFunctionHandler.isSimilar(ssh.getItemStack(), cc))
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
			if(!ShopFunctionHandler.isSimilar(toPutIn, ssh.getItemStack()))
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
	
	private static void putInItemIntoShopMsg(SignShop ssh, long amount, Player player)
	{
		ssh.setItemStorageCurrent(ssh.getItemStorageCurrent()+((long) amount));
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("SignHandler.ItemsAddedToShop")
				.replace("%amount%", String.valueOf(amount))
				.replace("%now%", String.valueOf(ssh.getItemStorageCurrent())+" / "+String.valueOf(ssh.getItemStorageTotal()))));
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.SIGNSHOP, ssh, "`id` = ?", ssh.getId());
	}
	
	public static void takeOutItemFromShop(SignShop ssh, Player player)
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
	
	private static void takeOutItemFromShopMsg(SignShop ssh, long amount, Player player)
	{
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("SignHandler.ItemsRemovedToShop")
				.replace("%amount%", String.valueOf(amount))
				.replace("%now%", String.valueOf(ssh.getItemStorageCurrent())+" / "+String.valueOf(ssh.getItemStorageTotal()))));
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.SIGNSHOP, ssh, "`id` = ?", ssh.getId());
	}
	
	public static void updateSign(SignShop ssh)
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