package me.avankziar.vss.spigot.database;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import me.avankziar.vss.spigot.VSS;
import me.avankziar.vss.spigot.database.Language.ISO639_2B;
import me.avankziar.vss.spigot.gui.objects.GuiType;

public class YamlHandler
{
	private VSS plugin;
	private File config = null;
	private YamlConfiguration cfg = new YamlConfiguration();
	
	private File commands = null;
	private YamlConfiguration com = new YamlConfiguration();
	
	private String languages;
	private File language = null;
	private YamlConfiguration lang = new YamlConfiguration();
	private File mvelanguage = null;
	private YamlConfiguration mvelang = new YamlConfiguration();
	
	private File matlanguage = null;
	private YamlConfiguration matlang = new YamlConfiguration();
	
	private LinkedHashMap<GuiType, YamlConfiguration> gui = new LinkedHashMap<>();

	public YamlHandler(VSS plugin)
	{
		this.plugin = plugin;
		loadYamlHandler();
	}
	
	public YamlConfiguration getConfig()
	{
		return cfg;
	}
	
	public YamlConfiguration getCommands()
	{
		return com;
	}
	
	public YamlConfiguration getLang()
	{
		return lang;
	}
	
	public YamlConfiguration getMVELang()
	{
		return mvelang;
	}
	
	public YamlConfiguration getMaterialLang()
	{
		return matlang;
	}
	
	public YamlConfiguration getGui(GuiType guiType)
	{
		return gui.get(guiType);
	}
	
	private YamlConfiguration loadYamlTask(File file, YamlConfiguration yaml)
	{
		try 
		{
			yaml.load(file);
		} catch (IOException | InvalidConfigurationException e) 
		{
			VSS.log.severe(
					"Could not load the %file% file! You need to regenerate the %file%! Error: ".replace("%file%", file.getName())
					+ e.getMessage());
			e.printStackTrace();
		}
		return yaml;
	}
	
	@SuppressWarnings("deprecation")
	private boolean writeFile(File file, YamlConfiguration yml, LinkedHashMap<String, Language> keyMap)
	{
		yml.options().header("For more explanation see \n https://www.spigotmc.org/resources/shop-a-lot-extra.106371/");
		for(String key : keyMap.keySet())
		{
			Language languageObject = keyMap.get(key);
			if(languageObject.languageValues.containsKey(plugin.getYamlManager().getLanguageType()) == true)
			{
				plugin.getYamlManager().setFileInput(yml, keyMap, key,plugin.getYamlManager().getLanguageType());
			} else if(languageObject.languageValues.containsKey(plugin.getYamlManager().getDefaultLanguageType()) == true)
			{
				plugin.getYamlManager().setFileInput(yml, keyMap, key, plugin.getYamlManager().getDefaultLanguageType());
			}
		}
		try
		{
			yml.save(file);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return true;
	}
	
	public boolean loadYamlHandler()
	{
		plugin.setYamlManager(new YamlManager());
		if(!mkdirStaticFiles())
		{
			return false;
		}
		if(!mkdirDynamicFiles())
		{
			return false;
		}
		return true;
	}
	
	public boolean mkdirStaticFiles()
	{
		File directory = new File(plugin.getDataFolder()+"");
		if(!directory.exists())
		{
			directory.mkdir();
		}
		config = new File(plugin.getDataFolder(), "config.yml");
		if(!config.exists()) 
		{
			VSS.log.info("Create config.yml...");
			try(InputStream in = plugin.getResource("default.yml"))
			{
				Files.copy(in, config.toPath());
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		cfg = loadYamlTask(config, cfg);
		if (cfg == null)
		{
			return false;
		}
		writeFile(config, cfg, plugin.getYamlManager().getConfigSpigotKey());
		
		languages = plugin.getAdministration() == null 
				? cfg.getString("Language", "ENG").toUpperCase() 
				: plugin.getAdministration().getLanguage();
		commands = new File(plugin.getDataFolder(), "commands.yml");
		
		if(!commands.exists()) 
		{
			VSS.log.info("Create commands.yml...");
			try(InputStream in = plugin.getResource("default.yml"))
			{
				Files.copy(in, commands.toPath());
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		com = loadYamlTask(commands, com);
		if (com == null)
		{
			return false;
		}
		writeFile(commands, com, plugin.getYamlManager().getCommandsKey());
		return true;
	}
	
	private boolean mkdirDynamicFiles()
	{
		List<Language.ISO639_2B> types = new ArrayList<Language.ISO639_2B>(EnumSet.allOf(Language.ISO639_2B.class));
		ISO639_2B languageType = ISO639_2B.ENG;
		for(ISO639_2B type : types)
		{
			if(type.toString().equals(languages))
			{
				languageType = type;
				break;
			}
		}
		plugin.getYamlManager().setLanguageType(languageType);
		if(!mkdirLanguage())
		{
			return false;
		}
		if(!mkdirGUIs())
		{
			return false;
		}
		return true;
	}
	
	private boolean mkdirLanguage()
	{
		String languageString = plugin.getYamlManager().getLanguageType().toString().toLowerCase();
		File directory = new File(plugin.getDataFolder()+"/Languages/");
		if(!directory.exists())
		{
			directory.mkdir();
		}
		language = new File(directory.getPath(), languageString+".yml");
		if(!language.exists()) 
		{
			VSS.log.info("Create %lang%.yml...".replace("%lang%", languageString));
			try(InputStream in = plugin.getResource("default.yml"))
			{
				Files.copy(in, language.toPath());
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		lang = loadYamlTask(language, lang);
		if (lang == null)
		{
			return false;
		}
		writeFile(language, lang, plugin.getYamlManager().getLanguageKey());
		mvelanguage = new File(directory.getPath(), "mve_"+languageString+".yml");
		if(!mvelanguage.exists()) 
		{
			VSS.log.info("Create mve_%lang%.yml...".replace("%lang%", languageString));
			try(InputStream in = plugin.getResource("default.yml"))
			{
				Files.copy(in, mvelanguage.toPath());
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		mvelang = loadYamlTask(mvelanguage, mvelang);
		if(mvelang == null)
		{
			return false;
		}
		writeFile(mvelanguage, mvelang, plugin.getYamlManager().getModifierValueEntryLanguageKey());
		
		matlanguage = new File(directory.getPath(), languageString+"_material.yml");
		if(!matlanguage.exists()) 
		{
			VSS.log.info("Create %lang%_material.yml...".replace("%lang%", languageString));
			try(InputStream in = plugin.getResource("default.yml"))
			{
				Files.copy(in, matlanguage.toPath());
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		matlang = loadYamlTask(matlanguage, matlang);
		if(matlang == null)
		{
			return false;
		}
		writeFile(matlanguage, matlang, plugin.getYamlManager().getMaterialLanguageKey());
		return true;
	}
	
	private boolean mkdirGUIs()
	{
		String languageString = plugin.getYamlManager().getLanguageType().toString().toLowerCase();
		File directory = new File(plugin.getDataFolder()+"/Gui/");
		if(!directory.exists())
		{
			directory.mkdir();
		}
		List<GuiType> list = new ArrayList<GuiType>(EnumSet.allOf(GuiType.class));
		for(GuiType g : list)
		{
			File gf = new File(directory.getPath(), languageString+"_"+g.toString()+".yml");
			if(gf.exists())
			{
				YamlConfiguration gui = loadYamlTask(gf, new YamlConfiguration());
				if (gui == null)
				{
					return false;
				}
				if(plugin.getYamlManager().getGuiKey(g) == null)
				{
					return false;
				}
				this.gui.put(g, gui);
				continue;
			}
			VSS.log.info("Create %lang%.yml...".replace("%lang%", languageString+"_"+g.toString()));
			try(InputStream in = plugin.getResource("default.yml"))
			{
				Files.copy(in, gf.toPath());
			} catch (IOException e)
			{
				e.printStackTrace();
			}
			YamlConfiguration gui = loadYamlTask(gf, new YamlConfiguration());
			if (gui == null)
			{
				return false;
			}
			if(plugin.getYamlManager().getGuiKey(g) == null)
			{
				return false;
			}
			writeFile(gf, gui, plugin.getYamlManager().getGuiKey(g));
			this.gui.put(g, gui);
		}
		return true;
	}
}