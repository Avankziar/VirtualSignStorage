package me.avankziar.vss.spigot.handler;

import java.io.ByteArrayOutputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import me.avankziar.vss.spigot.VSS;

public class ItemAndInvHandler 
{
	private static List<Enchantment> enchs = Registry.ENCHANTMENT.stream().collect(Collectors.toList());
	@SuppressWarnings("deprecation")
	private static PotionEffectType[] poefty = PotionEffectType.values();
	
	public static int emtpySlots(Player player)
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