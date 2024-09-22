package me.avankziar.vss.general.objects;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.ShulkerBox;
import org.bukkit.block.banner.Pattern;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.ArmorStand.LockType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.AxolotlBucketMeta;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.inventory.meta.SpawnEggMeta;
import org.bukkit.inventory.meta.SuspiciousStewMeta;
import org.bukkit.inventory.meta.TropicalFishBucketMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import me.avankziar.vss.general.ChatApi;
import me.avankziar.vss.spigot.VSS;
import me.avankziar.vss.spigot.assistance.TimeHandler;
import me.avankziar.vss.spigot.handler.Base64Handler;
import me.avankziar.vss.spigot.handler.GuiHandler;

public class ItemHologram
{
	private ArrayList<UUID> entitys = new ArrayList<>();
	
	public ItemHologram(ItemStack is, Location loc)
	{
		ArrayList<String> lines = new ArrayList<>();
		ItemMeta im = is.getItemMeta();
		PotionType ptd = PotionType.WATER;
		PotionMeta pmd = null;
		if(im instanceof PotionMeta)
		{
			pmd = (PotionMeta) im;
			ptd = pmd.getBasePotionType();
		}
		lines.add(ChatApi.tl(im.hasDisplayName() 
				? im.getDisplayName() 
				: (ptd != null && pmd != null && is.getType() != Material.TIPPED_ARROW
					? (VSS.getPlugin().getEnumTl() != null 
					  ? VSS.getPlugin().getEnumTl().getLocalization(ptd, pmd)
					  : ptd.toString()+"_"+pmd.toString())
					: (VSS.getPlugin().getEnumTl() != null 
					  ? VSS.getPlugin().getEnumTl().getLocalization(is.getType())
					  : is.getType().toString()))));
		if(im.hasLore()) 
		{
			lines.addAll(im.getLore());
		} else
		{
			if(Material.ENCHANTED_BOOK != is.getType())
			{
				if(im.hasEnchants())
				{
					for(Entry<Enchantment, Integer> en : is.getEnchantments().entrySet())
					{
						String name = 
								(VSS.getPlugin().getEnumTl() != null 
								? VSS.getPlugin().getEnumTl().getLocalization(en.getKey())
								: en.getKey().toString());
						int level = en.getValue();
						lines.add(ChatApi.tl("&7"+name+" "+GuiHandler.IntegerToRomanNumeral(level)));
					}
				}
			} else
			{
				if(im instanceof EnchantmentStorageMeta)
				{
					EnchantmentStorageMeta esm = (EnchantmentStorageMeta) im;
					for(Entry<Enchantment, Integer> en : esm.getStoredEnchants().entrySet())
					{
						String name = 
								(VSS.getPlugin().getEnumTl() != null 
								? VSS.getPlugin().getEnumTl().getLocalization(en.getKey())
								: en.getKey().toString());
						int level = en.getValue();
						lines.add(ChatApi.tl("&7"+name+" "+GuiHandler.IntegerToRomanNumeral(level)));
					}
				}
			}
			if(im instanceof BannerMeta)
			{
				BannerMeta bm = (BannerMeta) im;
				for(Pattern p : bm.getPatterns())
				{
					lines.add(ChatApi.tl("&7"+
							(VSS.getPlugin().getEnumTl() != null 
							? VSS.getPlugin().getEnumTl().getLocalization(p.getColor(), p.getPattern())
							: p.getColor().toString()+"_"+p.getPattern().toString())));
				}
			}
			if(im instanceof SkullMeta)
			{
				SkullMeta sm = (SkullMeta) im;
				if(sm.getOwningPlayer() != null)
				{
					lines.add(ChatApi.tl("&7"+sm.getOwningPlayer().getName()));
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
						long dur = pe.getDuration();
						String color = GuiHandler.getPotionColor(pe);
						if(pe.getType() == PotionEffectType.INSTANT_HEALTH 
								|| pe.getType() == PotionEffectType.INSTANT_DAMAGE)
						{
							lines.add(ChatApi.tl(color+
									(VSS.getPlugin().getEnumTl() != null 
									? VSS.getPlugin().getEnumTl().getLocalization(pe.getType())
									: pe.getType())
									+" "+GuiHandler.IntegerToRomanNumeral(level)));
						} else
						{
							lines.add(ChatApi.tl(color+
									(VSS.getPlugin().getEnumTl() != null 
									? VSS.getPlugin().getEnumTl().getLocalization(pe.getType())
									: pe.getType().toString())
									+" "+GuiHandler.IntegerToRomanNumeral(level)+" >> "+TimeHandler.getDateTime(dur, "mm:ss")));
						}
					}
				} else
				{
					/*int pv = 0;
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
							lines.add(ChatApi.tl(color+
									(VSS.getPlugin().getEnumTl() != null 
									? VSS.getPlugin().getEnumTl().getLocalization(pe.getType())
									: pe.getType().toString())
									+" "+GuiHandler.IntegerToRomanNumeral(level)));
						} else
						{
							lines.add(ChatApi.tl(color+
									(VSS.getPlugin().getEnumTl() != null 
									? VSS.getPlugin().getEnumTl().getLocalization(pe.getType())
									: pe.getType().toString())
									+" "+GuiHandler.IntegerToRomanNumeral(level)+" >> "+TimeHandler.getDateTime(dur, "mm:ss")));
						}
					}
				}
			}
			if(im instanceof AxolotlBucketMeta)
			{
				AxolotlBucketMeta abm = (AxolotlBucketMeta) im;
				try
				{
					if(abm.getVariant() != null)
					{
						lines.add(ChatApi.tl(VSS.getPlugin().getYamlHandler().getLang().getString("GuiHandler.ItemLore.AxolotlBucketMeta") 
								+ abm.getVariant().toString()));
					}
				} catch(Exception e) {}
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
						lines.add(ChatApi.tl("&7"+
								(VSS.getPlugin().getEnumTl() != null 
								? VSS.getPlugin().getEnumTl().getLocalization(ist.getType())
								: ist.getType()) + " x"+e.getValue()));
					}
				}
			}
			if(im instanceof BookMeta)
			{
				BookMeta bm = (BookMeta) im;
				if(bm.getTitle() != null)
				{
					lines.add(ChatApi.tl(VSS.getPlugin().getYamlHandler().getLang().getString("GuiHandler.ItemHolo.BookMeta.Title") + bm.getTitle()));
				}
				if(bm.getAuthor() != null)
				{
					lines.add(ChatApi.tl(VSS.getPlugin().getYamlHandler().getLang().getString("GuiHandler.ItemHolo.BookMeta.Author") + bm.getAuthor()));
				}
				lines.add(ChatApi.tl(VSS.getPlugin().getYamlHandler().getLang().getString("GuiHandler.ItemHolo.BookMeta.Page") + bm.getPageCount()));
				if(bm.getGeneration() != null)
				{
					lines.add(ChatApi.tl(VSS.getPlugin().getYamlHandler().getLang().getString("GuiHandler.ItemHolo.BookMeta.Generation") 
							+ (VSS.getPlugin().getEnumTl() != null 
							? VSS.getPlugin().getEnumTl().getLocalization(bm.getGeneration())
							: bm.getGeneration().toString())));
				}
			}
			if(im instanceof LeatherArmorMeta)
			{
				LeatherArmorMeta lam = (LeatherArmorMeta) im;
				lines.add(ChatApi.tl("&7"+String.format("#%02x%02x%02x",
						lam.getColor().getRed(), lam.getColor().getGreen(), lam.getColor().getBlue())
						.toUpperCase()));
			}
			if(im instanceof SpawnEggMeta)
			{
				SpawnEggMeta sem = (SpawnEggMeta) im;
				try
				{
					if(sem.getSpawnedEntity().getEntityType() != null)
					{
						lines.add(ChatApi.tl("&7"+
								(VSS.getPlugin().getEnumTl() != null 
								? VSS.getPlugin().getEnumTl().getLocalization(sem.getSpawnedEntity().getEntityType())
								: sem.getSpawnedEntity().getEntityType().toString())));
					}
					
				} catch(Exception e)
				{
					lines.add(ChatApi.tl("&7"+GuiHandler.getSpawnEggType(is.getType())));
				}
			}
			if(im instanceof SuspiciousStewMeta)
			{
				SuspiciousStewMeta ssm = (SuspiciousStewMeta) im;
				for(PotionEffect pe : ssm.getCustomEffects())
				{
					int level = pe.getAmplifier()+1;
					long dur = pe.getDuration();
					String color = GuiHandler.getPotionColor(pe);
					lines.add(ChatApi.tl(color+
							(VSS.getPlugin().getEnumTl() != null 
							? VSS.getPlugin().getEnumTl().getLocalization(pe.getType())
							: pe.getType().toString())
					+" "+GuiHandler.IntegerToRomanNumeral(level)+" >> "+TimeHandler.getDateTime(dur, "mm:ss")));
				}
			}
			if(im instanceof TropicalFishBucketMeta)
			{
				TropicalFishBucketMeta tfbm = (TropicalFishBucketMeta) im;
				lines.add(ChatApi.tl("&7"+
						(VSS.getPlugin().getEnumTl() != null 
						? VSS.getPlugin().getEnumTl().getLocalization(tfbm.getBodyColor(), tfbm.getPattern(), tfbm.getPatternColor())
						: tfbm.getBodyColor().toString()+"_"+tfbm.getPattern().toString()+"_"+tfbm.getPatternColor().toString())));
			}
		}
		spawn(null, loc.add(0, 0.9, 0), is);
		loc.add(0, -0.9, 0);
		for(int i = 0; i < lines.size(); i++)
		{
			spawn(lines.get(i), loc.add(0, -0.23, 0), null);
		}
	}
	
	private void spawn(String text, Location loc, ItemStack is)
	{
		ArmorStand as = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
		as.setGravity(false);
		as.setVisible(false);
		if(is != null)
		{
			as.getEquipment().setHelmet(is);
		}
		as.addEquipmentLock(EquipmentSlot.HEAD, LockType.REMOVING_OR_CHANGING);
		as.setCanPickupItems(false);
		if(text != null)
		{
			as.setCustomName(text);
		} else
		{
			as.setCustomName(" ");
		}
		as.setCustomNameVisible(true);
		entitys.add(as.getUniqueId());
	}
	
	public void despawn()
	{
		for(UUID as : entitys)
		{
			if(as == null)
			{
				continue;
			}
			Entity e = Bukkit.getEntity(as);
			e.remove();
		}
	}
	
	public boolean cancelManipulateEvent(ArmorStand as)
	{
		String a = as.getLocation().getWorld().getName()+as.getLocation().getX()+as.getLocation().getY()+as.getLocation().getZ();
		for(UUID uu : entitys)
		{
			Entity e = Bukkit.getEntity(uu);
			if(!(e instanceof ArmorStand))
			{
				continue;
			}
			ArmorStand ar = (ArmorStand) e;
			String b = ar.getLocation().getWorld().getName()+ar.getLocation().getX()+ar.getLocation().getY()+ar.getLocation().getZ();
			return a.equals(b);
		}
		return false;
	}
}