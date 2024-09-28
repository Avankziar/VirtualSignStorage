package me.avankziar.vss.spigot.handler;

import java.util.HashMap;

import org.bukkit.Material;

import me.avankziar.vss.spigot.VSS;

public class MaterialHandler
{
	private static HashMap<Material, String> materialLocalization = new HashMap<>();
	
	public static void init(VSS plugin)
	{
		for(Material m : Material.values())
		{
			materialLocalization.put(m, plugin.getYamlHandler().getMaterialLang().getString(m.toString(), m.toString()));
		}
	}
	
	public static String getMaterial(Material mat, Material sign)
	{
		String s = (VSS.getPlugin().getEnumTl() != null 
				? VSS.getPlugin().getEnumTl().getLocalization(mat)
				: mat.toString());
		switch(sign)
		{
		default: return s != null ? s : mat.toString();
		case ACACIA_SIGN:
		case ACACIA_WALL_SIGN:
		case ACACIA_HANGING_SIGN:
		case ACACIA_WALL_HANGING_SIGN:
		case BIRCH_SIGN:
		case BIRCH_WALL_SIGN:
		case BIRCH_HANGING_SIGN:
		case BIRCH_WALL_HANGING_SIGN:
		case CRIMSON_SIGN:
		case CRIMSON_WALL_SIGN:
		case CRIMSON_HANGING_SIGN:
		case CRIMSON_WALL_HANGING_SIGN:
		case DARK_OAK_SIGN:
		case DARK_OAK_WALL_SIGN:
		case DARK_OAK_HANGING_SIGN:
		case DARK_OAK_WALL_HANGING_SIGN:
		case JUNGLE_SIGN:
		case JUNGLE_WALL_SIGN:
		case JUNGLE_HANGING_SIGN:
		case JUNGLE_WALL_HANGING_SIGN:
		case MANGROVE_SIGN:
		case MANGROVE_WALL_SIGN:
		case MANGROVE_HANGING_SIGN:
		case MANGROVE_WALL_HANGING_SIGN:
		case OAK_SIGN:
		case OAK_WALL_SIGN:
		case OAK_HANGING_SIGN:
		case OAK_WALL_HANGING_SIGN:
		case SPRUCE_SIGN:
		case SPRUCE_WALL_SIGN:
		case SPRUCE_HANGING_SIGN:
		case SPRUCE_WALL_HANGING_SIGN:
		case WARPED_SIGN:
		case WARPED_WALL_SIGN:
		case WARPED_HANGING_SIGN:
		case WARPED_WALL_HANGING_SIGN:
			String c = VSS.getPlugin().getYamlHandler().getMaterialLang().getString(sign.toString()+"_SignStartColor", "");
			return s != null ? c+s : c+mat.toString();
		}
	}
	
	public static String getSignColor(Material sign)
	{
		switch(sign)
		{
		default: return "&r";
		case ACACIA_SIGN:
		case ACACIA_WALL_SIGN:
		case ACACIA_HANGING_SIGN:
		case ACACIA_WALL_HANGING_SIGN:
		case BIRCH_SIGN:
		case BIRCH_WALL_SIGN:
		case BIRCH_HANGING_SIGN:
		case BIRCH_WALL_HANGING_SIGN:
		case CRIMSON_SIGN:
		case CRIMSON_WALL_SIGN:
		case CRIMSON_HANGING_SIGN:
		case CRIMSON_WALL_HANGING_SIGN:
		case DARK_OAK_SIGN:
		case DARK_OAK_WALL_SIGN:
		case DARK_OAK_HANGING_SIGN:
		case DARK_OAK_WALL_HANGING_SIGN:
		case JUNGLE_SIGN:
		case JUNGLE_WALL_SIGN:
		case JUNGLE_HANGING_SIGN:
		case JUNGLE_WALL_HANGING_SIGN:
		case MANGROVE_SIGN:
		case MANGROVE_WALL_SIGN:
		case MANGROVE_HANGING_SIGN:
		case MANGROVE_WALL_HANGING_SIGN:
		case OAK_SIGN:
		case OAK_WALL_SIGN:
		case OAK_HANGING_SIGN:
		case OAK_WALL_HANGING_SIGN:
		case SPRUCE_SIGN:
		case SPRUCE_WALL_SIGN:
		case SPRUCE_HANGING_SIGN:
		case SPRUCE_WALL_HANGING_SIGN:
		case WARPED_SIGN:
		case WARPED_WALL_SIGN:
		case WARPED_HANGING_SIGN:
		case WARPED_WALL_HANGING_SIGN:
			String c = VSS.getPlugin().getYamlHandler().getMaterialLang().getString(sign.toString()+"_SignStartColor", "");
			return c;
		}
	}
}