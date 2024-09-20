package me.avankziar.vss.spigot.database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;

import me.avankziar.vss.spigot.database.Language.ISO639_2B;
import me.avankziar.vss.spigot.gui.objects.ClickFunctionType;
import me.avankziar.vss.spigot.gui.objects.ClickType;
import me.avankziar.vss.spigot.gui.objects.GuiType;
import me.avankziar.vss.spigot.gui.objects.SettingsLevel;
import me.avankziar.vss.spigot.modifiervalueentry.Bypass;
import me.avankziar.vss.spigot.objects.ListedType;

public class YamlManager
{
	private ISO639_2B languageType = ISO639_2B.GER;
	//The default language of your plugin. Mine is german.
	private ISO639_2B defaultLanguageType = ISO639_2B.GER;
	
	//Per Flatfile a linkedhashmap.
	private static LinkedHashMap<String, Language> configSpigotKeys = new LinkedHashMap<>();
	private static LinkedHashMap<String, Language> commandsKeys = new LinkedHashMap<>();
	private static LinkedHashMap<String, Language> languageKeys = new LinkedHashMap<>();
	private static LinkedHashMap<String, Language> mvelanguageKeys = new LinkedHashMap<>();
	private static LinkedHashMap<String, Language> matlanguageKeys = new LinkedHashMap<>();
	private static LinkedHashMap<GuiType, LinkedHashMap<String, Language>> guiKeys = new LinkedHashMap<>();
	
	public YamlManager()
	{
		initConfig();
		initCommands();
		initLanguage();
		initModifierValueEntryLanguage();
		initMaterialLanguage();
		initGuiAdministration();
		initGuiNumpad();
		initGuiShop();
		initGuiItemInput();
		initGuiKeyboard();
		initGuiSearchBuy();
		initGuiSearchSell();
		initGuiSubscribe();
	}
	
	public ISO639_2B getLanguageType()
	{
		return languageType;
	}

	public void setLanguageType(ISO639_2B languageType)
	{
		this.languageType = languageType;
	}
	
	public ISO639_2B getDefaultLanguageType()
	{
		return defaultLanguageType;
	}
	
	public LinkedHashMap<String, Language> getConfigSpigotKey()
	{
		return configSpigotKeys;
	}
	
	public LinkedHashMap<String, Language> getCommandsKey()
	{
		return commandsKeys;
	}
	
	public LinkedHashMap<String, Language> getLanguageKey()
	{
		return languageKeys;
	}
	
	public LinkedHashMap<String, Language> getModifierValueEntryLanguageKey()
	{
		return mvelanguageKeys;
	}
	
	public LinkedHashMap<String, Language> getMaterialLanguageKey()
	{
		return matlanguageKeys;
	}
	
	public LinkedHashMap<String, Language> getGuiKey(GuiType guiType)
	{
		return guiKeys.get(guiType);
	}
	
	public LinkedHashMap<GuiType, LinkedHashMap<String, Language>> getGuiKey()
	{
		return guiKeys;
	}
	
	/*
	 * The main methode to set all paths in the yamls.
	 */
	public void setFileInput(YamlConfiguration yml, LinkedHashMap<String, Language> keyMap, String key, ISO639_2B languageType)
	{
		if(!keyMap.containsKey(key))
		{
			return;
		}
		if(key.startsWith("#"))
		{
			//Comments
			String k = key.replace("#", "");
			if(yml.get(k) == null)
			{
				//return because no aktual key are present
				return;
			}
			if(yml.getComments(k) != null && !yml.getComments(k).isEmpty())
			{
				//Return, because the comments are already present, and there could be modified. F.e. could be comments from a admin.
				return;
			}
			if(keyMap.get(key).languageValues.get(languageType).length == 1)
			{
				if(keyMap.get(key).languageValues.get(languageType)[0] instanceof String)
				{
					String s = ((String) keyMap.get(key).languageValues.get(languageType)[0]).replace("\r\n", "");
					yml.setComments(k, Arrays.asList(s));
				}
			} else
			{
				List<Object> list = Arrays.asList(keyMap.get(key).languageValues.get(languageType));
				ArrayList<String> stringList = new ArrayList<>();
				if(list instanceof List<?>)
				{
					for(Object o : list)
					{
						if(o instanceof String)
						{
							stringList.add(((String) o).replace("\r\n", ""));
						}
					}
				}
				yml.setComments(k, (List<String>) stringList);
			}
			return;
		}
		if(yml.get(key) != null)
		{
			return;
		}
		if(keyMap.get(key).languageValues.get(languageType).length == 1)
		{
			if(keyMap.get(key).languageValues.get(languageType)[0] instanceof String)
			{
				yml.set(key, ((String) keyMap.get(key).languageValues.get(languageType)[0]).replace("\r\n", ""));
			} else
			{
				yml.set(key, keyMap.get(key).languageValues.get(languageType)[0]);
			}
		} else
		{
			List<Object> list = Arrays.asList(keyMap.get(key).languageValues.get(languageType));
			ArrayList<String> stringList = new ArrayList<>();
			if(list instanceof List<?>)
			{
				for(Object o : list)
				{
					if(o instanceof String)
					{
						stringList.add(((String) o).replace("\r\n", ""));
					} else
					{
						stringList.add(o.toString().replace("\r\n", ""));
					}
				}
			}
			yml.set(key, (List<String>) stringList);
		}
	}
	
	private void addComments(LinkedHashMap<String, Language> mapKeys, String path, Object[] o)
	{
		mapKeys.put(path, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, o));
	}
	
	private void addConfig(String path, Object[] c, Object[] o)
	{
		configSpigotKeys.put(path, new Language(new ISO639_2B[] {ISO639_2B.GER}, c));
		addComments(configSpigotKeys, "#"+path, o);
	}
	
	public void initConfig() //INFO:Config
	{
		addConfig("useIFHAdministration",
				new Object[] {
				true},
				new Object[] {
				"Boolean um auf das IFH Interface Administration zugreifen soll.",
				"Wenn 'true' eingegeben ist, aber IFH Administration ist nicht vorhanden, so werden automatisch die eigenen Configwerte genommen.",
				"Boolean to access the IFH Interface Administration.",
				"If 'true' is entered, but IFH Administration is not available, the own config values are automatically used."});
		addConfig("IFHAdministrationPath", 
				new Object[] {
				"sale"},
				new Object[] {
				"",
				"Diese Funktion sorgt dafür, dass das Plugin auf das IFH Interface Administration zugreifen kann.",
				"Das IFH Interface Administration ist eine Zentrale für die Daten von Sprache, Servername und Mysqldaten.",
				"Diese Zentralisierung erlaubt für einfache Änderung/Anpassungen genau dieser Daten.",
				"Sollte das Plugin darauf zugreifen, werden die Werte in der eigenen Config dafür ignoriert.",
				"",
				"This function ensures that the plugin can access the IFH Interface Administration.",
				"The IFH Interface Administration is a central point for the language, server name and mysql data.",
				"This centralization allows for simple changes/adjustments to precisely this data.",
				"If the plugin accesses it, the values in its own config are ignored."});
		
		addConfig("Language",
				new Object[] {
				"ENG"},
				new Object[] {
				"",
				"Die eingestellte Sprache. Von Haus aus sind 'ENG=Englisch' und 'GER=Deutsch' mit dabei.",
				"Falls andere Sprachen gewünsch sind, kann man unter den folgenden Links nachschauen, welchs Kürzel für welche Sprache gedacht ist.",
				"Siehe hier nach, sowie den Link, welche dort auch für Wikipedia steht.",
				"https://github.com/Avankziar/RootAdministration/blob/main/src/main/java/me/avankziar/roota/general/Language.java",
				"",
				"The set language. By default, ENG=English and GER=German are included.",
				"If other languages are required, you can check the following links to see which abbreviation is intended for which language.",
				"See here, as well as the link, which is also there for Wikipedia.",
				"https://github.com/Avankziar/RootAdministration/blob/main/src/main/java/me/avankziar/roota/general/Language.java"});
		addConfig("ServerName",
				new Object[] {
				"hub"},
				new Object[] {
				"",
				"Der Server steht für den Namen des Spigotservers, wie er in BungeeCord/Waterfall/Velocity config.yml unter dem Pfad 'servers' angegeben ist.",
				"Sollte kein BungeeCord/Waterfall oder andere Proxys vorhanden sein oder du nutzt IFH Administration, so kannst du diesen Bereich ignorieren.",
				"",
				"The server stands for the name of the spigot server as specified in BungeeCord/Waterfall/Velocity config.yml under the path 'servers'.",
				"If no BungeeCord/Waterfall or other proxies are available or you are using IFH Administration, you can ignore this area."});
		
		addConfig("Mysql.Status",
				new Object[] {
				false},
				new Object[] {
				"",
				"'Status' ist ein simple Sicherheitsfunktion, damit nicht unnötige Fehler in der Konsole geworfen werden.",
				"Stelle diesen Wert auf 'true', wenn alle Daten korrekt eingetragen wurden.",
				"",
				"'Status' is a simple security function so that unnecessary errors are not thrown in the console.",
				"Set this value to 'true' if all data has been entered correctly."});
		addComments(configSpigotKeys, "#Mysql", 
				new Object[] {
				"",
				"Mysql ist ein relationales Open-Source-SQL-Databaseverwaltungssystem, das von Oracle entwickelt und unterstützt wird.",
				"'My' ist ein Namenkürzel und 'SQL' steht für Structured Query Language. Eine Programmsprache mit der man Daten auf einer relationalen Datenbank zugreifen und diese verwalten kann.",
				"Link https://www.mysql.com/de/",
				"Wenn du IFH Administration nutzt, kann du diesen Bereich ignorieren.",
				"",
				"Mysql is an open source relational SQL database management system developed and supported by Oracle.",
				"'My' is a name abbreviation and 'SQL' stands for Structured Query Language. A program language that can be used to access and manage data in a relational database.",
				"Link https://www.mysql.com",
				"If you use IFH Administration, you can ignore this section."});
		addConfig("Mysql.Host",
				new Object[] {
				"127.0.0.1"},
				new Object[] {
				"",
				"Der Host, oder auch die IP. Sie kann aus einer Zahlenkombination oder aus einer Adresse bestehen.",
				"Für den Lokalhost, ist es möglich entweder 127.0.0.1 oder 'localhost' einzugeben. Bedenke, manchmal kann es vorkommen,",
				"das bei gehosteten Server die ServerIp oder Lokalhost möglich ist.",
				"",
				"The host, or IP. It can consist of a number combination or an address.",
				"For the local host, it is possible to enter either 127.0.0.1 or >localhost<.",
				"Please note that sometimes the serverIp or localhost is possible for hosted servers."});
		addConfig("Mysql.Port",
				new Object[] {
				3306},
				new Object[] {
				"",
				"Ein Port oder eine Portnummer ist in Rechnernetzen eine Netzwerkadresse,",
				"mit der das Betriebssystem die Datenpakete eines Transportprotokolls zu einem Prozess zuordnet.",
				"Ein Port für Mysql ist standart gemäß 3306.",
				"",
				"In computer networks, a port or port number ",
				"is a network address with which the operating system assigns the data packets of a transport protocol to a process.",
				"A port for Mysql is standard according to 3306."});
		addConfig("Mysql.DatabaseName",
				new Object[] {
				"mydatabase"},
				new Object[] {
				"",
				"Name der Datenbank in Mysql.",
				"",
				"Name of the database in Mysql."});
		addConfig("Mysql.SSLEnabled",
				new Object[] {
				false},
				new Object[] {
				"",
				"SSL ist einer der drei Möglichkeiten, welcher, solang man nicht weiß, was es ist, es so lassen sollte wie es ist.",
				"",
				"SSL is one of the three options which, as long as you don't know what it is, you should leave it as it is."});
		addConfig("Mysql.AutoReconnect",
				new Object[] {
				true},
				new Object[] {
				"",
				"AutoReconnect ist einer der drei Möglichkeiten, welcher, solang man nicht weiß, was es ist, es so lassen sollte wie es ist.",
				"",
				"AutoReconnect is one of the three options which, as long as you don't know what it is, you should leave it as it is."});
		addConfig("Mysql.VerifyServerCertificate",
				new Object[] {
				false},
				new Object[] {
				"",
				"VerifyServerCertificate ist einer der drei Möglichkeiten, welcher, solang man nicht weiß, was es ist, es so lassen sollte wie es ist.",
				"",
				"VerifyServerCertificate is one of the three options which, as long as you don't know what it is, you should leave it as it is."});
		addConfig("Mysql.User",
				new Object[] {
				"admin"},
				new Object[] {
				"",
				"Der User, welcher auf die Mysql zugreifen soll.",
				"",
				"The user who should access the Mysql."});
		addConfig("Mysql.Password",
				new Object[] {
				"not_0123456789"},
				new Object[] {
				"",
				"Das Passwort des Users, womit er Zugang zu Mysql bekommt.",
				"",
				"The user's password, with which he gets access to Mysql."});
		
		configSpigotKeys.put("Enable.SignShop"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		/*configSpigotKeys.put("Enable.Auction"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));*/
		configSpigotKeys.put("EnableMechanic.Modifier"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configSpigotKeys.put("EnableMechanic.ValueEntry"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configSpigotKeys.put("ValueEntry.OverrulePermission"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		
		addConfig("CleanUpTask.Player.Active",
				new Object[] {
				true},
				new Object[] {
				"",
				"Wenn 'true' dann ist der Aufräumtask für Datenbankeinträge für Spieler aktiv.",
				"",
				"If 'true' then the cleanup task for database entries for players is active."});
		addConfig("CleanUpTask.Player.DeleteAfterXDaysOffline",
				new Object[] {
				365},
				new Object[] {
				"",
				"Löscht alle Spieler, die nach X Tage nicht online sind.",
				"",
				"Deletes all players who are not online after X days."});
		addConfig("CleanUpTask.ShopLog.Active",
				new Object[] {
				true},
				new Object[] {
				"",
				"Wenn 'true' dann ist der Aufräumtask für Datenbankeinträge für Shoplog aktiv.",
				"",
				"If 'true' then the cleanup task for database entries for Shoplog is active."});
		addConfig("CleanUpTask.ShopLog.DeleteAfterXDays",
				new Object[] {
				365},
				new Object[] {
				"",
				"Löscht alle Shoplogs, die älter als X Tage sind.",
				"",
				"Deletes all store logs that are older than X days."});
		addConfig("CleanUpTask.ShopDailyLog.Active",
				new Object[] {
				true},
				new Object[] {
				"",
				"Wenn 'true' dann ist der Aufräumtask für Datenbankeinträge für Shopdailylog aktiv.",
				"",
				"If 'true' then the cleanup task for database entries for Shopdailylog is active."});
		addConfig("CleanUpTask.ShopDailyLog.DeleteAfterXDays",
				new Object[] {
				365},
				new Object[] {
				"",
				"Löscht alle Shopdailylogs, die älter als X Tage sind.",
				"",
				"If 'true' then the cleanup task for database entries for client log is active."});
		addConfig("CleanUpTask.ClientLog.Active",
				new Object[] {
				true},
				new Object[] {
				"",
				"Wenn 'true' dann ist der Aufräumtask für Datenbankeinträge für Clientlog aktiv.",
				"",
				""});
		addConfig("CleanUpTask.ClientLog.DeleteAfterXDays",
				new Object[] {
				365},
				new Object[] {
				"",
				"Löscht alle Clientlogs, die älter als X Tage sind.",
				"",
				"Deletes all client logs that are older than X days."});
		addConfig("CleanUpTask.ClientDailyLog.Active",
				new Object[] {
				true},
				new Object[] {
				"",
				"Wenn 'true' dann ist der Aufräumtask für Datenbankeinträge für Clientdailylog aktiv.",
				"",
				"Deletes all client dailylogs that are older than X days."});
		addConfig("CleanUpTask.ClientDailyLog.DeleteAfterXDays",
				new Object[] {
				365},
				new Object[] {
				"",
				"Löscht alle Clientdailylogs, die älter als X Tage sind.",
				"",
				""});
		
		configSpigotKeys.put("Mechanic.CountPerm"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"ADDUP"}));
		addConfig("SignShop.SignInitializationLine",
				new Object[] {
				"[SaleShop]"},
				new Object[] {
				"",
				"Zeile 0 welche zum initialisieren genutzt wird um ein SignShop zu gründen.",
				"",
				"Line which is used for initialization to create a SignShop."});
		addConfig("SignShop.SignCopyLine",
				new Object[] {
				"[Copy]"},
				new Object[] {
				"",
				"Zeile 1 welche zum Kopieren des angegeben Shops genutzt wird. Übernommen wird alles außer eingelagerte Items.",
				"",
				"Line 1 which is used to copy the specified store. Everything is copied except stored items."});
		addConfig("SignShop.SignMoveLine",
				new Object[] {
				"[Move]"},
				new Object[] {
				"",
				"Zeile 1 welche zum Verlegen des angegeben Shops genutzt wird. Altes Shopschild ist somit inaktiv. Wird aber nicht abgebaut.",
				"",
				"Line 1 which is used to relocate the specified store. The old store sign is therefore inactive. But will not be removed."});
		addConfig("SignShop.DefaultStartItemStorage",
				new Object[] {
				3456},
				new Object[] {
				"",
				"Die Anzahl an Standartlagerplatz für ein neuer Shop.",
				"",
				"The number of standard storage spaces for a new store."});
		addConfig("SignShop.CostToAdd1Storage",
				new Object[] {
				"dollar;1000.0",
				"token;99.0",
				"vault;100.0"},
				new Object[] {
				"",
				"Die Kosten pro Währung um den Lagerplatz eines Shops um 1 zu erhöhen.",
				"",
				"The cost per currency to increase the storage space of a store by 1."});
		addConfig("SignShop.DefaultMaxSubscribtion",
				new Object[] {
				45},
				new Object[] {
				"",
				"Die Standart maximale Anzahl an abonnierten Shops. Per Permission erhöhbar.",
				"",
				"The standard maximum number of subscribed transactions. Can be increased by permission."});
		addConfig("SignShop.ForbiddenWorld",
				new Object[] {
				"hubs", "spawns"},
				new Object[] {
				"",
				"Die Welten, wo es verboten ist, Shops zu erstellen.",
				"",
				"The worlds where it is forbidden to create stores."});
		addConfig("SignShop.DiscountTimePattern",
				new Object[] {
				"yyyy.MM.dd.HH:mm:ss"},
				new Object[] {
				"",
				"Das Zeitformat um Rabatt zu definieren.",
				"",
				"The time format to define discount."});
		addConfig("SignShop.Sign.Line4CalculateInStack",
				new Object[] {
				false},
				new Object[] {
				"",
				"Wenn 'true' dann wird die 4 Zeile des Schildes des Shops in Stacks berechnet, ansteller einzelner Items.",
				"",
				"If 'true' then the 4 line of the store's sign is calculated in stacks instead of individual items."});
		addConfig("SignShop.Tax.BuyInPercent",
				new Object[] {
				1.0},
				new Object[] {
				"",
				"Anzahl an Steuer in Prozent beim Kaufen in einem Shop.",
				"",
				"Number of tax in percent when buying in a store."});
		addConfig("SignShop.Tax.SellInPercent",
				new Object[] {
				1.1},
				new Object[] {
				"",
				"Anzahl an Steuer in Prozent beim Verkaufen in einem Shop.",
				"",
				"Number of tax in percent when selling in a store."});
		addConfig("SignShop.ItemHologram.CanSpawn",
				new Object[] {
				true},
				new Object[] {
				"",
				"Wenn 'true' dann können ItemHologram spawnen.",
				"",
				"If 'true' then ItemHologram can spawn."});
		addConfig("SignShop.ItemHologram.RunTimerInSeconds",
				new Object[] {
				2},
				new Object[] {
				"",
				"Wie lange die Wiederholung der Schedular des ItemHologram läuft.",
				"",
				"How long the repetition of the ItemHologram's Schedular runs."});
		addConfig("SignShop.ItemHologram.VisibilityTimeInSeconds",
				new Object[] {
				3},
				new Object[] {
				"",
				"Wie lange das ItemHologram sichtbar ist. Mit dem Schedular könnte die Sichtbarkeit länger sein.",
				"",
				"How long the ItemHologram is visible. With the Schedular, the visibility could be longer."});
		addConfig("SignShop.TransactionSummary.MessageToShopOwner.RunTimerInMinutes",
				new Object[] {
				5},
				new Object[] {
				"",
				"Anzahl an Minuten wie lange die Wiederholungszeit des Scheduals ist, welcher für die Nachrichten der Shopeigentümer zuständig ist.",
				"Dieser Schedular sammelt alle Nachrichten an die Shopeigentümer und fasst sie zusammen um sie an den Shopeigentümer zu schicken.",
				"",
				"Number of minutes how long the repetition time of the scheduler is, which is responsible for the messages of the store owners.",
				"This scheduler collects all messages to the store owners and summarizes them to send them to the store owner."});
		addConfig("SignShop.TransactionSummary.ShopLog.RunTimerInMinutes",
				new Object[] {
				15},
				new Object[] {
				"",
				"Anzahl in Minuten für die Wiederholungsrate für die Transaktionzusammenfassung Schedular.",
				"",
				"Number in minutes for the repetition rate for the transaction summary Schedular."});
		addConfig("SignShop.ShopLog.TimePattern",
				new Object[] {
				"dd-MM-yyyy/HH:mm"},
				new Object[] {
				"",
				"ZeitFormat für den Shoplog.",
				"",
				"TimeFormat for the shoplog."});
		addConfig("SignShop.ShopDailyLog.TimePattern",
				new Object[] {
				"dd-MM-yyyy"},
				new Object[] {
				"",
				"Zeitformat für den Shopdailylog.",
				"",
				"Time format for the shopdailylog."});
		addConfig("SignShop.ShopCanTradeShulker",
				new Object[] {
				false},
				new Object[] {
				"",
				"Erlaubt den Shop Shulker zu verkaufen.",
				"",
				"Allows the store to sell Shulker."});
		addConfig("SignShop.ShopUseMaterialAsShopName",
				new Object[] {
				true},
				new Object[] {
				"",
				"Wenn 'true' dann wird das Material als Shopname verwendet, wenn kein Displayname festgelegt wurde.",
				"",
				"If 'true' then the material is used as the store name if no display name has been defined."});
		configSpigotKeys.put("SignShop.Gui.ForceSettingsLevel"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		addConfig("SignShop.Gui.ForceSettingsLevel",
				new Object[] {
				false},
				new Object[] {
				"",
				"Wenn 'true' dann wird bei jedem öffnen des Shop & AdministrationGui das Settingslevel auf den unten angegeben Wert gesetzt.",
				"",
				"If 'true' then the settings level is set to the value specified below each time the Shop & AdministrationGui is opened."});
		addConfig("SignShop.Gui.ClickCooldown",
				new Object[] {
				500},
				new Object[] {
				"",
				"Cooldown beim Klicken in den Guis in Millisekunden.",
				"",
				"Cooldown when clicking in the guis in milliseconds."});
		addConfig("SignShop.Gui.ToBeForcedSettingsLevel",
				new Object[] {
				SettingsLevel.BASE.toString()},
				new Object[] {
				"",
				"SettingsLevel welches gesetzt wird, wenn es geforced wird.",
				"",
				"SettingsLevel which is set when it is forced."});
		addConfig("SignShop.Gui.FillNotDefineGuiSlots",
				new Object[] {
				true},
				new Object[] {
				"",
				"Wenn 'true' werden alle leeren Slots in einem Gui mit Füllitems aufgefüllt.",
				"",
				"If 'true', all empty slots in a gui are filled with filler items."});
		addConfig("SignShop.Gui.FillerItemMaterial",
				new Object[] {
				Material.LIGHT_GRAY_STAINED_GLASS_PANE.toString()},
				new Object[] {
				"",
				"Füllmaterial für die Guis",
				"",
				"Filling material for the guis"});
		addConfig("SignShop.Search.Radius",
				new Object[] {
				"PROXY"},
				new Object[] {
				"",
				"Der 'Suchradius' für die Suchbefehle. Er determiniert, in welchem Bezugsradius er suchen soll.",
				"PROXY - sucht auf allen Server nach den Shops.",
				"SERVER - sucht auf dem Server wo sich der Spieler befindet nach den Shops.",
				"WORLD - sucht auf der Welt wo sich der Spieler befindet nach den Shops.",
				"Ganzzahl - bspw. 50 sucht innerhalb von +50 und -50 Blöcken in allen Richtungen nach den Shops.",
				"",
				"The 'search radius' for the search commands. It determines the reference radius in which it should search.",
				"PROXY - searches for the stores on all servers.",
				"SERVER - searches for stores on the server where the player is located.",
				"WORLD - searches for stores in the world where the player is located.",
				"Integer - e.g. 50 searches for the stores within +50 and -50 blocks in all directions."});
		addConfig("SignShop.Search.SortType",
				new Object[] {
				"PRICE"},
				new Object[] {
				"",
				"Die Sortierungsmethode für die Reihenfolge. Möglich sind: PRICE oder RANDOM",
				"",
				"The sorting method for the sequence. The following are possible: PRICE or RANDOM"});
		addConfig("SignShop.Search.DoAfterGuiClick",
				new Object[] {
				"LOCATION"},
				new Object[] {
				"",
				"Determiniert, was bei dem Klicken auf die Shops von den Suchbefehlen Guis gemacht wird.",
				"Bei LOCATION wird nur die Location des Shops und ein paar andere Daten per Chat geschrieben. Bei TELEPORT wird der Spieler teleportiert.",
				"",
				"Determines what is done by the search commands Guis when clicking on the stores.",
				"With LOCATION, only the location of the store and a few other details are written via chat. With TELEPORT the player is teleported."});
	}
	
	//INFO:Commands
	public void initCommands()
	{
		comBypass();
		commandsInput("sale", "sale", "sale.cmd.sale", 
				"/sale [pagenumber]", "/sale ", false,
				"&c/sale &f| Infoseite für alle Befehle.",
				"&c/sale &f| Info page for all commands.",
				"&bBefehlsrecht für &f/sale",
				"&bCommandright for &f/sale",
				"&eBasisbefehl für das Sale Plugin.",
				"&eGroundcommand for the Sale Plugin.");
		String basePermission = "sale.cmd";
		argumentInput("sale_shop", "shop", basePermission,
				"/sale shop", "/sale shop ", false,
				"&c/sale shop &f| Zwischenbefehl.",
				"&c/sale shop &f| Intermediate command.",
				"&bBefehlsrecht für &f/sale shop",
				"&bCommandright for &f/sale shop",
				"&eBefehl für den Zwischenbefehl.",
				"&eCommand for the intermediate command.");
		argumentInput("sale_debug", "debug", basePermission,
				"/sale debug <xxx>", "/sale debug ", false,
				"&c/sale debug <xxx> &f| Zu Testzwecken des Plugin. Benutzung auf eigene Gefahr.",
				"&c/sale debug <xxx> &f| For testing the plugin. Use at your own risk.",
				"&bBefehlsrecht für &f/sale debug",
				"&bCommandright for &f/sale debug",
				"&eZu Testzwecken des Plugin. Benutzung auf eigene Gefahr.",
				"&eFor testing the plugin. Use at your own risk.");
		argumentInput("sale_shop_delete", "delete", basePermission,
				"/sale shop delete <xxx:yyy...>", "/sale shop delete ", false,
				"&c/sale shop delete <xxx:yyy...> &f| Löscht alle Shops nach den Parameter(xxx). Param. sind id, player, server, world, item, radius.",
				"&c/sale shop delete <xxx:yyy...> &f| Deletes all stores after the parameter(xxx). Param. are id, player, server, world, item, radius.",
				"&bBefehlsrecht für &f/sale shop delete",
				"&bCommandright for &f/sale shop delete",
				"&eBefehl zum Löschen von Shops über Parameterangaben.",
				"&eCommand to delete stores via parameter specifications.");
		argumentInput("sale_shop_breaktoggle", "breaktoggle", basePermission,
				"/sale shop breaktoggle", "/sale shop breaktoggle ", false,
				"&c/sale shop breaktoggle &f| Togglet ob man Shops direkt löschen durch das Abbauen kann.",
				"&c/sale shop breaktoggle &f| Togglet whether you can delete stores directly by dismantling.",
				"&bBefehlsrecht für &f/sale shop breaktoggle",
				"&bCommandright for &f/sale shop breaktoggle",
				"&eBefehl für die direkte",
				"&eCommand for the intermediate command.");
		argumentInput("sale_shop_toggle", "toggle", basePermission,
				"/sale shop toggle", "/sale shop toggle ", false,
				"&c/sale shop toggle &f| Togglet ob man fremde Shops durch das Gui administrieren kann.",
				"&c/sale shop toggle &f| Toggle whether you can administrate foreign stores through the gui.",
				"&bBefehlsrecht für &f/sale shop toggle",
				"&bCommandright for &f/sale shop toggle",
				"&eBefehl zum togglet ob man fremde Shops durch das Gui administrieren kann.",
				"&eCommand to toggle whether you can administrate foreign stores through the gui.");
		argumentInput("sale_shop_toggle", "toggle", basePermission,
				"/sale shop toggle", "/sale shop toggle ", false,
				"&c/sale shop toggle &f| Togglet ob man fremde Shops durch das Gui administrieren kann.",
				"&c/sale shop toggle &f| Toggle whether you can administrate foreign stores through the gui.",
				"&bBefehlsrecht für &f/sale shop toggle",
				"&bCommandright for &f/sale shop toggle",
				"&eBefehl zum togglet ob man fremde Shops durch das Gui administrieren kann.",
				"&eCommand to toggle whether you can administrate foreign stores through the gui.");
		argumentInput("sale_shop_log", "log", basePermission+".shop",
				"/sale shop log [number] [shopid] [player] [boolean]", "/sale shop log", false,
				"&c/sale shop log [Zahl] [Shopid] [Spieler] [boolean] &f| Zeigt die Shop Aktivitäten des Eigentümer an. True für Buy.",
				"&c/sale shop log [number] [shopid] [player] [boolean] &f| Displays the shop activities of the owner. True for Buy.",
				"&bBefehlsrecht für &f/sale shop log",
				"&bCommandright for &f/sale shop log",
				"&eBefehl zeigt die Shop Aktivitäten des Eigentümer an.",
				"&eCommand for displays the shop activities of the owner.");
		argumentInput("sale_shop_dailylog", "dailylog", basePermission+".signshop",
				"/sale shop dailylog [number] [shopid] [player]", "/sale shop dailylog", false,
				"&c/sale shop dailylog [Zahl] [Shopid] [Spieler] &f| Zeigt die Shop Tagesaktivitäten des Eigentümer an.",
				"&c/sale shop dailylog [number] [shopid] [player] &f| Displays the shop dailyactivities of the owner.",
				"&bBefehlsrecht für &f/sale shop dailylog",
				"&bCommandright for &f/sale shop dailylog",
				"&eBefehl zeigt die Shop Aktivitäten des Eigentümer an.",
				"&eCommand for displays the shop activities of the owner.");
		argumentInput("sale_shop_searchbuy", "searchbuy", basePermission+".signshop",
				"/sale shop searchbuy [Material] [Displayname...]", "/sale shop searchbuy", false,
				"&c/sale shop searchbuy [Material] [Displayname...] &f| Sucht alle Shops nach den angegebenen Parameter für Items zum kaufen.",
				"&c/sale shop searchbuy [Material] [Displayname...] &f| Searches all stores for the specified parameters for items to buy.",
				"&bBefehlsrecht für &f/sale shop searchbuy",
				"&bCommandright for &f/sale shop searchbuy",
				"&eSucht alle Shops nach den angegebenen Parameter für Items zum kaufen.",
				"&eSearches all stores for the specified parameters for items to buy.");
		argumentInput("sale_shop_searchsell", "searchsell", basePermission+".signshop",
				"/sale shop searchsell [Material] [Displayname...]", "/sale shop searchsell", false,
				"&c/sale shop searchsell [Material] [Displayname...] &f| Sucht alle Shops nach den angegebenen Parameter für Items zum verkaufen.",
				"&c/sale shop searchsell [Material] [Displayname...] &f| Searches all stores for the specified parameters for items to sell.",
				"&bBefehlsrecht für &f/sale shop searchsell",
				"&bCommandright for &f/sale shop searchsell",
				"&eSucht alle Shops nach den angegebenen Parameter für Items zum verkaufen.",
				"&eSearches all stores for the specified parameters for items to sell.");
		argumentInput("sale_client", "client", basePermission,
				"/sale client", "/sale client ", false,
				"&c/sale client &f| Zwischenbefehl.",
				"&c/sale client &f| Intermediate command.",
				"&bBefehlsrecht für &f/sale client",
				"&bCommandright for &f/sale client",
				"&eBefehl für den Zwischenbefehl.",
				"&eCommand for the intermediate command.");
		argumentInput("sale_client_log", "log", basePermission+".client",
				"/sale client log [number] [player] [boolean]", "/sale client log", false,
				"&c/sale client [Zahl] [Spieler] [boolean] &f| Zeigt die client Aktivitäten des Spielers an. True für Buy.",
				"&c/sale client [number] [player] [boolean] &f| Displays the client activities of the player. True for Buy.",
				"&bBefehlsrecht für &f/sale client log",
				"&bCommandright for &f/sale client log",
				"&eBefehl zeigt die client Aktivitäten des Spielers an.",
				"&eCommand for displays the client activities of the player.");
		argumentInput("sale_client_dailylog", "dailylog", basePermission+".client",
				"/sale client dailylog [number] [player]", "/sale client dailylog", false,
				"&c/sale client dailylog [Zahl] [Spieler] &f| Zeigt die client Tagesaktivitäten des Spielers an.",
				"&c/sale client dailylog [number] [player] &f| Displays the client dailyactivities of the player.",
				"&bBefehlsrecht für &f/sale client dailylog",
				"&bCommandright for &f/sale client dailylog",
				"&eBefehl zeigt die Shop Aktivitäten des Eigentümer an.",
				"&eCommand for displays the shop activities of the owner.");
		argumentInput("sale_subscribed", "subscribed", basePermission+".subscribed",
				"/sale subscribed [page] [searchparameter...]", "/sale subscribed", false,
				"&c/sale subscribed [Seitenzahl] [Suchparameter...] &f| Auflistung per Gui aller abonnierten Shops. Suchparameter erlauben größere Eingrenzung. X ist dabei den Wert den ihr suchen wollt.",
				"&c/sale subscribed [page] [searchparameter...] &f| Listing via Gui of all subscribed stores. Search parameters allow you to narrow down your search. X is the value you want to search for.",
				"&bBefehlsrecht für &f/sale subscribed",
				"&bCommandright for &f/sale subscribed",
				"&eAuflistung per Gui aller abonnierten Shops. Suchparameter erlauben größere Eingrenzung. X ist dabei den Wert den ihr suchen wollt.",
				"&eListing via Gui of all subscribed stores. Search parameters allow you to narrow down your search. X is the value you want to search for.");
	}
	
	private void comBypass() //INFO:ComBypass
	{
		List<Bypass.Permission> list = new ArrayList<Bypass.Permission>(EnumSet.allOf(Bypass.Permission.class));
		for(Bypass.Permission ept : list)
		{
			commandsKeys.put("Bypass."+ept.toString()
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"sale."+ept.toString().toLowerCase().replace("_", ".")}));
		}
		
		List<Bypass.Counter> list2 = new ArrayList<Bypass.Counter>(EnumSet.allOf(Bypass.Counter.class));
		for(Bypass.Counter ept : list2)
		{
			commandsKeys.put("Count."+ept.toString()
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"sale."+ept.toString().toLowerCase().replace("_", ".")}));
		}
	}
	
	private void commandsInput(String path, String name, String basePermission, 
			String suggestion, String commandString, boolean putUpCmdPermToValueEntrySystem,
			String helpInfoGerman, String helpInfoEnglish,
			String dnGerman, String dnEnglish,
			String exGerman, String exEnglish)
	{
		commandsKeys.put(path+".Name"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				name}));
		commandsKeys.put(path+".Permission"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				basePermission}));
		commandsKeys.put(path+".Suggestion"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				suggestion}));
		commandsKeys.put(path+".CommandString"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				commandString}));
		commandsKeys.put(path+".HelpInfo"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				helpInfoGerman,
				helpInfoEnglish}));
		commandsKeys.put(path+".ValueEntry.PutUpCommandPerm"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				putUpCmdPermToValueEntrySystem}));
		commandsKeys.put(path+".ValueEntry.Displayname"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				dnGerman,
				dnEnglish}));
		commandsKeys.put(path+".ValueEntry.Explanation"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				exGerman,
				exEnglish}));
	}
	
	private void argumentInput(String path, String argument, String basePermission, 
			String suggestion, String commandString, boolean putUpCmdPermToValueEntrySystem,
			String helpInfoGerman, String helpInfoEnglish,
			String dnGerman, String dnEnglish,
			String exGerman, String exEnglish)
	{
		commandsKeys.put(path+".Argument"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				argument}));
		commandsKeys.put(path+".Permission"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				basePermission+"."+argument}));
		commandsKeys.put(path+".Suggestion"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				suggestion}));
		commandsKeys.put(path+".CommandString"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				commandString}));
		commandsKeys.put(path+".HelpInfo"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				helpInfoGerman,
				helpInfoEnglish}));
		commandsKeys.put(path+".ValueEntry.PutUpCommandPerm"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				putUpCmdPermToValueEntrySystem}));
		commandsKeys.put(path+".ValueEntry.Displayname"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				dnGerman,
				dnEnglish}));
		commandsKeys.put(path+".ValueEntry.Explanation"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				exGerman,
				exEnglish}));
	}
	
	public void initLanguage() //INFO:Languages
	{
		languageKeys.put("InputIsWrong",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDeine Eingabe ist fehlerhaft! Klicke hier auf den Text, um weitere Infos zu bekommen!",
						"&cYour input is incorrect! Click here on the text to get more information!"}));
		languageKeys.put("NoPermission",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu hast dafür keine Rechte!",
						"&cYou dont not have the rights!"}));
		languageKeys.put("NoPlayerExist",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDer Spieler existiert nicht!",
						"&cThe player does not exist!"}));
		languageKeys.put("InputIsEmpty",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDie Eingabe darf nicht leer sein!",
						"&cThe input must not be empty!"}));
		languageKeys.put("NoNumber",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDas Argument &f%value% &cmuss eine ganze Zahl sein.",
						"&cThe argument &f%value% &must be an integer."}));
		languageKeys.put("NoDouble",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDas Argument &f%value% &cmuss eine Gleitpunktzahl sein!",
						"&cThe argument &f%value% &must be a floating point number!"}));
		languageKeys.put("IsNegativ",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDas Argument &f%value% &cmuss eine positive Zahl sein!",
						"&cThe argument &f%value% &must be a positive number!"}));
		languageKeys.put("NotEnought",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu hast nicht genug Geld!",
						"&cYou dont have enough money!"}));
		languageKeys.put("OnCooldown",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDer Befehl ist in Cooldown!",
						"&cThe command is in cooldown!"}));
		languageKeys.put("ShopOwnerNotEnought",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDer ShopEigentümer hat nicht genug Geld!",
						"&cThe shopowner dont have enough money!"}));
		languageKeys.put("NoWithdrawRights",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu hast für das angegeben Konto keine Abheberechte!",
						"&cYou have no withdrawal rights for the specified account!"}));
		languageKeys.put("GeneralHover",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eKlick mich!",
						"&eClick me!"}));
		languageKeys.put("Headline", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&e=====&7[&6SaLE&7]&e=====",
						"&e=====&7[&6SaLE&7]&e====="}));
		languageKeys.put("Next", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&e&nnächste Seite &e==>",
						"&e&nnext page &e==>"}));
		languageKeys.put("Past", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&e<== &nvorherige Seite",
						"&e<== &nprevious page"}));
		languageKeys.put("IsTrue", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&a✔",
						"&a✔"}));
		languageKeys.put("IsFalse", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&c✖",
						"&c✖"}));
		languageKeys.put("NotOwner"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu kannst diesen Wert nicht ändern, da dir der Shop nicht gehört.",
						"&cYou can't change this value because you don't own the store."}));
		languageKeys.put("SignChangeListener.WorldGuardCreateDeny", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cIn dieser Region dürfen keine Shops erstellt werden!",
						"&cNo shops may be created in this region!"}));
		languageKeys.put("Economy.AddStorage.Category", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"Lagerraumerweiterung",
						"Stockroomextension"}));
		languageKeys.put("Economy.AddStorage.Comment", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eEine Lagerraumerweiterung von &f%past% &eauf &f%now% (+%amount%) &eItems des Shops &f%name%&e.",
						"&eA stockroom extension from &f%past% &eto &f%now% (+%amount%) &eItems of the store &f%name%&e."}));
		languageKeys.put("Economy.Buy.Category", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"Shopkauf",
						"Shopsale"}));
		languageKeys.put("Economy.Buy.Comment", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&bBeim Shop &f%shop% &bwurden &r%item% &rx &e%amount% &bgekauft.",
						"&bAt the shop &f%shop% &bwere &r%item% &rx &e%amount% &bpurchased."}));
		languageKeys.put("Economy.Sell.Category", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"Shopverkauf",
						"Shoppurchase"}));
		languageKeys.put("Economy.Sell.Comment", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&bDer Shop &f%shop% &bhat &r%item% &rx &e%amount% &bankauft.",
						"&bThe shop &f%shop% &bhad &r%item% &rx &e%amount% &bpurchased."}));
		languageKeys.put("Economy.CommentAddition", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						" &bGesamtpreis: %format%",
						" &bTotalprice: %format%"}));
		languageKeys.put("FileError"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cEs existiert ein Fehler in der Datei: &f%file%",
						"&cThere is an error in the file: &f%file%"}));
		languageKeys.put("Mechanic.SignShopIsntEnabled", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDie SchilderShops Mechanik ist nicht aktiviert!",
						"&cThe Sign Shops mechanics is not activated!"}));
		languageKeys.put("PlayerInteractListener.ShopItemIsNull", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu kannst das Administrationsgui nicht aufrufen, da der Shop noch kein Item gesetzt hat!",
						"&cYou can not call the administration gui, because the store has not set an item yet!"}));
		languageKeys.put("SignChangeListener.MaterialIsAir", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&4Leer",
						"&4Empty"}));
		languageKeys.put("SignChangeListener.AlreadyHaveMaximalSignShop", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu hast schon die maximale Anzahl von SignShops erstellt. Lösche zuerst SignShops bevor neue erstellt werden! Aktuelle &f%actual% &cvon %max%",
						"&cYou have already created the maximum number of SignShops. First delete SignShops before creating new ones! Current &f%actual% &cof %max%"}));
		languageKeys.put("SignChangeListener.ShopCreated", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu hast den Shop &f%name% &eerstellt! &bKlicke auf das Schild und stelle dort das Item des Shops ein!",
						"&eYou have created the store &f%name%! &bClick on the sign and set the item of the store there!"}));
		languageKeys.put("SignChangeListener.ShopNotExists", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDer Shop %id% existiert nicht!",
						"&cThe store %id% does not exist!"}));
		languageKeys.put("SignChangeListener.ShopMoved", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDer Shops %id%-%shopname% wurde versetzt!",
						"&eThe store %id%-%shopname% has been moved!"}));
		languageKeys.put("SignChangeListener.ShopCopy", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDer Shops %id%-%shopname% wurde kopiert!",
						"&eThe store %id%-%shopname% has been copied!"}));
		languageKeys.put("PlayerInteractListener.IsBlackList", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu steht für diesen Shop auf der schwarzen Liste.",
						"&cYou are blacklisted for this store."}));
		languageKeys.put("PlayerInteractListener.IsNotWhiteList", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu steht für diesen Shop nicht auf der weißen Liste.",
						"&cYou are not on the white list for this store."}));
		languageKeys.put("PlayerInteractListener.IsNotCustomList", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu steht für diesen Shop nicht auf der benutzerdefinierten Liste.",
						"&cYou are not on the custom list for this store."}));
		languageKeys.put("PlayerInteractListener.IsNotMember", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu musst Mitglied dieses Shops sein um darauf zugreifen zu können.",
						"&cYou must be a member of this store to access it."}));
		languageKeys.put("SignHandler.Line1Discount", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&#FF8800K: &#ff00ff%amount%",
						"&#FF8800B: &#ff00ff%amount%"}));
		languageKeys.put("SignHandler.Line2Discount", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&#00f07eV: &#ff00ff%amount%",
						"&#00f07eS: &#ff00ff%amount%"}));
		languageKeys.put("SignHandler.Line1", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&#FF8800K: &r%amount%",
						"&#FF8800B: &r%amount%"}));
		languageKeys.put("SignHandler.Line2", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&#00f07eV: &r%amount%",
						"&#00f07eS: &r%amount%"}));
		languageKeys.put("SignHandler.PercentColor.100AndAbove", 
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&9"}));
		languageKeys.put("SignHandler.PercentColor.Between100And75", 
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&b"}));
		languageKeys.put("SignHandler.PercentColor.Between75And50", 
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&a"}));
		languageKeys.put("SignHandler.PercentColor.Between50And25", 
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&e"}));
		languageKeys.put("SignHandler.PercentColor.Between25And10", 
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&#FF8800"}));
		languageKeys.put("SignHandler.PercentColor.Between10And0", 
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&c"}));
		languageKeys.put("SignHandler.PercentColor.0AndLess", 
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&4"}));
		languageKeys.put("SignHandler.AttachedNewItemStackToShop", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&aDu hast das Item &r%name% &azum Shop &f%signshop% &ahinzugefügt!",
						"&aYou have added the item &r%name% &ato the store &f%signshop%&a!"}));
		languageKeys.put("SignHandler.ItemsAddedToShop", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu hast &f%amount% &eItems dem Shop hinzugefügt. Zz.: &r%now%",
						"&eYou have added &f%amount% &eitems to the store. Attn: &r%now%"}));
		languageKeys.put("SignHandler.NoItemsIsSetUp", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDer Shop hat noch kein Items eingesetzt, somit kann kein Item auf die Hand ausgegeben werden!",
						"&cThe store has not yet deployed an item, so no item can be spent on the hand!"}));
		languageKeys.put("SignHandler.ItemsRemovedToShop", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu hast &f%amount% &eItems aus dem Shop entfernt. Zz.: &r%now%",
						"&eYou have removed &f%amount% &eItems from the store. Attn: &r%now%"}));
		languageKeys.put("SignHandler.ForbiddenWorld", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cIn dieser Welt darfst du keine Signshop erstellt.",
						"&cIn this world you may not create a signshop."}));
		languageKeys.put("GuiHandler.AccountNotExist", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDer zugehörige Account des Shops des Spielers %player% existiert nicht mehr! Bitte wende dich an den Shopeigentümer oder falls nicht anders möglich an einen Admin.",
						"&cThe associated account of the store of the player %player% no longer exists! Please contact the store owner or if not otherwise possible an admin."}));
		languageKeys.put("GuiHandler.InfoLore.Owner", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cEigentümer: &f",
						"&cOwner: &f"}));
		languageKeys.put("GuiHandler.InfoLore.Displayname", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDisplayname: &f",
						"&cDisplayname: &f"}));
		languageKeys.put("GuiHandler.InfoLore.Material", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cMaterial: &f",
						"&cMaterial: &f"}));
		languageKeys.put("GuiHandler.InfoLore.ItemFlag", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cItemFlag: &f",
						"&cItemFlag: &f"}));
		languageKeys.put("GuiHandler.InfoLore.Enchantment", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cVerzauberungen: &f",
						"&cEnchantments: &f"}));
		languageKeys.put("GuiHandler.InfoLore.StorageEnchantment", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cGelagerte Verzauberungen: &f",
						"&cStored Enchantments: &f"}));
		languageKeys.put("GuiHandler.InfoLore.Damageable", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cHaltbarkeit: &f",
						"&cDurability: &f"}));
		languageKeys.put("GuiHandler.InfoLore.SkullMeta.Owner", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cSpielerkopf: &f",
						"&cPlayerhead: &f"}));
		languageKeys.put("GuiHandler.InfoLore.Repairable", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cReparaturkosten: &f",
						"&cRepair costs: &f"}));
		languageKeys.put("GuiHandler.InfoLore.AxolotlBucketMeta", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cAxolotl: &f",
						"&cAxolotl: &f"}));
		languageKeys.put("GuiHandler.InfoLore.BannerMeta", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cBannerMuster: &f",
						"&cBannerPattern: &f"}));
		languageKeys.put("GuiHandler.InfoLore.BookMeta.Title", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cBuchtitle: &f",
						"&cBooktitle: &f"}));
		languageKeys.put("GuiHandler.InfoLore.BookMeta.Author", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cBuchautor: &f",
						"&cBookauthor: &f"}));
		languageKeys.put("GuiHandler.InfoLore.BookMeta.Page", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cSeitenzahl: &f",
						"&cPagenumber: &f"}));
		languageKeys.put("GuiHandler.InfoLore.BookMeta.Generation", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cVersion: &f",
						"&cVersion: &f"}));
		languageKeys.put("GuiHandler.InfoLore.LeatherArmorMeta", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cLederrüstungfärbung: &f",
						"&cLeatherarmordye: &f"}));
		languageKeys.put("GuiHandler.InfoLore.SpawnEggMeta", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cSpawnEi EntityType: &f",
						"&cSpawnEgg EntityType: &f"}));
		languageKeys.put("GuiHandler.InfoLore.TropicalFishBucketMeta", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cTropenfischeimerfärbung: &f",
						"&cTropicalfishbucketdye: &f"}));
		languageKeys.put("GuiHandler.ItemHolo.BookMeta.Title", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&7Buchtitle: ",
						"&7Booktitle: "}));
		languageKeys.put("GuiHandler.ItemHolo.BookMeta.Author", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&7Buchautor: ",
						"&7Bookauthor: "}));
		languageKeys.put("GuiHandler.ItemHolo.BookMeta.Page", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&7Seitenzahl: ",
						"&7Pagenumber: "}));
		languageKeys.put("GuiHandler.ItemHolo.BookMeta.Generation", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&7Version: ",
						"&7Version: "}));
		languageKeys.put("GuiHandler.ItemHolo.AxolotlBucketMeta", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&7Axolotl: &f",
						"&7Axolotl: &f"}));
		languageKeys.put("AdminstrationFunctionHandler.AddStorage.YouDontHaveAccountToWithdraw"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu hast keinen Account um die Kosten abziehen zu können.",
						"&cYou do not have an account to be able to deduct the costs."}));
		languageKeys.put("AdminstrationFunctionHandler.AddStorage.YouCannotWithdraw"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu hast kein Abhebrecht des angestrebte Account!",
						"&cYou do not have the right to withdraw from the targeted account!"}));
		languageKeys.put("AdminstrationFunctionHandler.AddStorage.NoEnoughMoney"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDer angestrebte Account hat nicht genug Geld!",
						"&cThe targeted account does not have enough money!"}));
		languageKeys.put("AdminstrationFunctionHandler.AddStorage.TooManyAlreadyAsStorage"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDer Shop hat schon zu viel Lagerraum, als es ihm bzw. dir möglich ist!",
						"&cThe store already has too much storage space than it is possible for him or you!"}));
		languageKeys.put("AdminstrationFunctionHandler.AddStorage.Transaction"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&6Es wurden von dem Konto &e%fromaccount% &f%formatwithdraw% &6abgezogen und an &e%toaccount% &6überwiesen.",
						"&bKategorie: &f%category% &f| &bNotiz: &f%comment%",
						"&6It was deducted from the account &e%fromaccount% &f%formatwithdraw% &6and transferred to &e%toaccount%&6.",
						"&bCategory: &f%category% &f| &bComment: &f%comment%"}));
		languageKeys.put("AdminstrationFunctionHandler.AddStorage.Withdraw"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&6Es wurden von dem Konto &e%fromaccount% &f%formatwithdraw% &6abgezogen.",
						"&bKategorie: &f%category% &f| &bNotiz: &f%comment%",
						"&6It was deducted from the account &e%fromaccount% &f%formatwithdraw%&6.",
						"&bCategory: &f%category% &f| &bComment: &f%comment%"}));
		languageKeys.put("AdminstrationFunctionHandler.DeleteAll.NotOwner"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu kannst den Shop nicht auflösen, da er dir nicht gehört!",
						"&cYou can not dissolve the store, because it is not yours!"}));
		languageKeys.put("AdminstrationFunctionHandler.DeleteAll.Delete"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu hast den Shop &f%id%-%signshop% &caufgelöst. Es befanden sich noch &f%amount% &r%displayname% &cItems im Shop, welche nun verloren sind.",
						"&cYou have closed the store &f%id%-%signshop%&c. There were still &f%amount% &r%displayname% &citems in the store, which are now lost."}));
		languageKeys.put("AdminstrationFunctionHandler.SetAccount.Set"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eSignshop Account wurde gesetzt!",
						"&eSignshop account was set!"}));
		languageKeys.put("AdminstrationFunctionHandler.SetStorage.NotOwner"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu bist nicht der Eigentümer des Lagersystem!",
						"&cYou are not the owner of the storage system!"}));
		languageKeys.put("AdminstrationFunctionHandler.SetStorage.Set"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eSignshop ASH-Lager ID wurde gesetzt!",
						"&eSignshop ASH-storage ID was set!"}));
		languageKeys.put("AdminstrationFunctionHandler.ItemClear"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu hast das Item des Shop zurückgesetzt. Nun kann ein neues Item in den Shop eingetragen werden.",
						"&eYou have reset the item of the store. Now you can add a new item to the store."}));
		languageKeys.put("AdminstrationFunctionHandler.DiscountTimeNotFit"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDas eingestellte Datum/Uhrzeit &f%value% &cpasst nicht mit dem Muster &f%pattern% &czusammen!",
						"&cThe set date/time &f%value% &cdoes not match the pattern &f%pattern% &c!"}));
		languageKeys.put("AdminstrationFunctionHandler.ListedType."+ListedType.ALL.toString()
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&aAlle",
						"&aAlle"}));
		languageKeys.put("AdminstrationFunctionHandler.ListedType."+ListedType.BLACKLIST.toString()
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cBlackList",
						"&cBlacklist"}));
		languageKeys.put("AdminstrationFunctionHandler.ListedType."+ListedType.WHITELIST.toString()
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&#FF8800Whitelist",
						"&#FF8800Whitelist"}));
		languageKeys.put("AdminstrationFunctionHandler.ListedType."+ListedType.MEMBER.toString()
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&bMitglieder",
						"&bMember"}));
		languageKeys.put("AdminstrationFunctionHandler.ListedType."+ListedType.CUSTOM.toString()
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&dBenutzerdefiniert",
						"&dCustom"}));
		languageKeys.put("AdminstrationFunctionHandler.Listed.Add"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu hast %amount% Spieler/Einträge zur Liste %list% &ehinzugefügt.",
						"&eYou have added %amount% players/entrys to the %list% &elist."}));
		languageKeys.put("AdminstrationFunctionHandler.Listed.Remove"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu hast %amount% Spieler/Einträge von der Liste %list% &eentfernt.",
						"&eYou have removed %amount% players/entrys from the %list% &elist."}));
		languageKeys.put("AdminstrationFunctionHandler.Listed.List"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eFolgende Spieler stehen auf Liste %list%: %players%",
						"&eThe following players are on list %list%: %players%"}));
		languageKeys.put("AdminstrationFunctionHandler.DiscountStartWorld"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu hast von %amount% Shops die Rabattstartzeit gesetzt.",
						"&eYou have set the discount start time from %amount% shops."}));
		languageKeys.put("AdminstrationFunctionHandler.DiscountEndWorld"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu hast von %amount% Shops die Rabattendzeit gesetzt.",
						"&eYou have set the discount end time from %amount% shops."}));
		languageKeys.put("AdminstrationFunctionHandler.DiscountHourWorld"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu hast von %amount% Shops die Rabattaktion für %hour% gestartet.",
						"&eYou have started the discount promotion for %hour% from %amount% stores."}));
		languageKeys.put("AdminstrationFunctionHandler.SellHigherAsBuy"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cAchtung! Du kaufst mit einem höheren Wert die Waren an, als Ihn zuverkaufen!",
						"&cAttention! You buy the goods with a higher value than you sell them!"}));
		
		languageKeys.put("ShopFunctionHandler.NoEmptySlot"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu hast keinen freien Slot in deinem Inventar!",
						"&cYou have no free slot in your inventory!"}));
		languageKeys.put("ShopFunctionHandler.CannotTradeInOwnShop"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu kannst nicht in deinem eigenen Shop handeln!",
						"&cYou cant trade in your own store!"}));
		languageKeys.put("ShopFunctionHandler.Buy.NoGoodsInStock"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDer Shop hat keine Ware mehr zum Kaufen!",
						"&cThe store has no more goods for sale!"}));
		languageKeys.put("ShopFunctionHandler.Buy.NoGoodsInStockII"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDer Shop &f%shopname% &chat keine Ware mehr zum Kaufen!",
						"&cThe store &f%shopname% &chas no more goods for sale!"}));
		languageKeys.put("ShopFunctionHandler.InfoAddition"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						" &7Mehr Infos?",
						" &7More Infos?"}));
		languageKeys.put("ShopFunctionHandler.InfoHover"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cKunde: &f%client%",
						"&cItem: &r%item%",
						"&cMenge: &r%amount%",
						"&cPreis: &f%price%",
						"&cWo: &f%server%, %world%",
						"    &f%x%, %y% %z%",
						"&cClient: &f%client%",
						"&cItem: &r%item%",
						"&cAmount: &f%amount%",
						"&cPrice: &f%price%",
						"&cWhere: &f%server%, %world%",
						"      &f%x%, %y% %z%",}));
		languageKeys.put("ShopFunctionHandler.Buy.NotInit"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDer Shop hat noch keinen Kauf-Wert festgelegt! Somit kann nicht gekauft werden!",
						"&cThe store has not yet set a sale value! Thus can not be bought!"}));
		languageKeys.put("ShopFunctionHandler.Buy.PossibleIsZero"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDer Shop hat keine Ware mehr zum Kaufen ausgestellt!",
						"&cThe store has no more goods on display for purchase!"}));
		languageKeys.put("ShopFunctionHandler.Buy.ShopHaveNotAccountReady"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDer Shop hat kein zugewiesenes Konto!",
						"&cThe store has no assigned account!"}));
		languageKeys.put("ShopFunctionHandler.Buy.YouDontHaveAccountToWithdraw"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu hast keinen Account um die Kosten abziehen zu können.",
						"&cYou do not have an account to be able to deduct the costs."}));
		languageKeys.put("ShopFunctionHandler.Transaction"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&6Es wurden von dem Konto &e%fromaccount% &f%formatwithdraw% &6abgezogen und &f%formatdeposit% &6an &e%toaccount% &6überwiesen.",
						"&eGezahlte Steuern&7: &f%formattax%",
						"&bKategorie: &f%category% &f| &bNotiz: &f%comment%",
						"&6It was deducted from the account &e%fromaccount% &f%formatwithdraw% &6and transferred &f%formatdeposit% &6to &e%toaccount%&6.",
						"&eTaxes paid&7: &f%formattax%",
						"&bCategory: &f%category% &f| &bComment: &f%comment%"}));
		languageKeys.put("ShopFunctionHandler.Sell.ShopIsFull"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDer Shop ist voll!",
						"&cThe store is full!"}));
		languageKeys.put("ShopFunctionHandler.Sell.ShopIsFullII"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDer Shop &f%shopname% &cist voll!",
						"&cThe store &f%shopname% &cis full!"}));
		languageKeys.put("ShopFunctionHandler.Sell.NoItemInInventory"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu hast keine Items im Inventar zum Kaufen!",
						"&cYou have no items in your inventory to sell!"}));
		languageKeys.put("ShopFunctionHandler.Sell.NotInit"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDer Shop hat noch keinen Ankauf-Wert festgelegt! Somit kann nicht Kauft werden!",
						"&cThe store has not yet set a purchase value! Thus can not be sold!"}));
		languageKeys.put("ShopFunctionHandler.Sell.PossibleIsZero"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDer Shop hat keinen Platz zum Ankaufen ausgestellt!",
						"&cThe store has not issued a place to buy!"}));
		languageKeys.put("ShopFunctionHandler.Sell.ShopHaveNotAccountReady"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDer Shop hat kein zugewiesenes Konto!",
						"&cThe store has no assigned account!"}));
		languageKeys.put("ShopFunctionHandler.Sell.YouDontHaveAccountToWithdraw"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu hast keinen Account um den Gewinn einzuzahlen.",
						"&cYou do not have an account to deposit the winnings."}));
		languageKeys.put("ShopFunctionHandler.ASH.ChestNotFound"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDas Lagersystem %id% wurde nicht gefunden! Shop %ssh% setzt LagerID zurück!",
						"&cThe store system %id% was not found! Store %ssh% resets storageID!"}));
		languageKeys.put("ShopFunctionHandler.Subscribe"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu hast den Shop &a%shop% &eabonniert!",
						"&eYou have subscribed the shop &a%shop%&e!"}));
		languageKeys.put("ShopFunctionHandler.Subscribes.HasMoreAsAllowed"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu hast zu viele Shops abonniert! &f%has%/%allowed%",
						"&cYou have subscribed to too many stores! &f%has%/%allowed%"}));
		languageKeys.put("ShopFunctionHandler.Unsubscribe"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu hast den Shop &c%shop% &edeabonniert!",
						"&eYou have unsubscribed the shop &a%shop%&e!"}));
		
		languageKeys.put("SearchFunctionHandler.Title"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eSuche: &f%mat%",
						"&eSearch: &f%mat%"}));
		
		languageKeys.put("SubscribedFunctionHandler.Title"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eAbos von &f%player%",
						"&eSubscribes of &f%player%"}));
		
		languageKeys.put("SignShopProvider.GetOutOfStorage"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eVom Shop &f%shopname% &ewurden &f%amount% &eItems ins Lager verschoben.",
						"&eFrom the store &f%shopname% &ewere moved &f%amount% &eItems to the storehouse."}));
		languageKeys.put("SignShopProvider.PutIntoStorage"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eAus dem Lager wurden &f%amount% &eItems in den Shop &f%shopname% &everschoben.",
						"&eFrom the storehouse &f%amount% &eItems were moved to the store &f%shopname%&e."}));
		languageKeys.put("ShopLog.MsgTimer.Msg", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&bShopTransaktion von %player%",
						"&bShopTransaction from %player%"}));
		languageKeys.put("ShopLog.MsgTimer.Buy", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&7[&#FF8800K&7]&f%shop% &#FF8800>> &r%item% &rx &e%amount% &#FF8800>> %format%",
						"&7[&#FF8800B&7]&f%shop% &#FF8800>> &r%item% &rx &e%amount% &#FF8800>> %format%"}));
		languageKeys.put("ShopLog.MsgTimer.Sell", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&7[&aV&7]&f%shop% &a>> &r%item% &rx &e%amount% &a>> &r%format%",
						"&7[&aS&7]&f%shop% &a>> &r%item% &rx &e%amount% &a>> &r%format%"}));
		languageKeys.put("Cmd.OtherCmd",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cBitte nutze den Befehl mit einem weiteren Argument aus der Tabliste!",
						"&cPlease use the command with another argument from the tab list!"}));
		languageKeys.put("Cmd.Delete.NoFoundToDelete", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cUnter den angegeben Parameter sind keine Shops gefunden worden!",
						"&cNo stores have been found under the specified parameters!"}));
		languageKeys.put("Cmd.Delete.Delete", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu hast %shopamount% Shop gelöscht! &rVerlorene Items: %itemlost% | Gelöschte Logs: %log% | Gelöschte DailyLog: %dailylog% | Gelöschte Subs: %subs%",
						"&eYou have deleted %shopamount% shops! &rLost Items: %itemlost% | Deleted Logs: %log% | Deleted DailyLog: %dailylog% | Deleted Subs: %subs%"}));
		languageKeys.put("Cmd.BreakToggle.Active", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu kannst nun Shopschilder direkt abbauen!",
						"&eYou can now take down ShopSigns directly!"}));
		languageKeys.put("Cmd.BreakToggle.Deactive", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu kannst du nicht mehr Shopschilder direkt abbauen!",
						"&eYou cannot take down store signs directly anymore!"}));
		languageKeys.put("Cmd.Toggle.Active", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu kannst nun fremde Shopschilder administrieren!",
						"&eYou can now administrate foreign shop signs!"}));
		languageKeys.put("Cmd.Toggle.Deactive", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu kannst du nicht mehr fremde Shopschilder administrieren!",
						"&eYou cannot administrate other shop signs anymore!"}));
		languageKeys.put("Cmd.ShopLog.NoLogs", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cKeine Signshoplogs vorhanden!",
						"&cNo Signshoplogs available!"}));
		languageKeys.put("Cmd.ShopLog.Headline", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&f===&eShopLog %shopid%/%player%, Seite %page%, Type %waytype%&f===",
						"&f===&eShopLog %shopid%/%player%, page %page%, Type %waytype%&f==="}));
		languageKeys.put("Cmd.ShopLog.Buy", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&7%time% [&#FF8800K&7]&e%player% &#FF8800>> &f%shop% &#FF8800>> &r%item% &rx &e%amount% &#FF8800>> %format%",
						"&7%time% [&#FF8800B&7]&e%player% &#FF8800>> &f%shop% &#FF8800>> &r%item% &rx &e%amount% &#FF8800>> %format%"}));
		languageKeys.put("Cmd.ShopLog.Sell", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&7%time% [&aV&7]&e%player% &a>> &f%shop% &a>> &r%item% &rx &e%amount% &a>> &r%format%",
						"&7%time% [&aS&7]&e%player% &a>> &f%shop% &a>> &r%item% &rx &e%amount% &a>> &r%format%"}));
		languageKeys.put("Cmd.ShopDailyLog.NoLogs", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cKeine täglichen Signshoplogs vorhanden!",
						"&cNo daily Signshoplogs available!"}));
		languageKeys.put("Cmd.ShopDailyLog.Headline", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&f===&eShopDailyLog %shopid%/%player%, Seite %page%&f===",
						"&f===&eShopDailyLog %shopid%/%player%, page %page%&f==="}));
		languageKeys.put("Cmd.ShopDailyLog.Log", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&7%time% &e>> &f%shop% &e>> &7[&#FF8800K&7] &rx &e%buyamo% &e>> &r%buyformat% &r| &7[&aV&7] &rx &e%sellamo% &e>> &r%sellformat%",
						"&7%time% &e>> &f%shop% &e>> &7[&#FF8800B&7] &rx &e%buyamo% &e>> &r%buyformat% &r| &7[&aS&7] &rx &e%sellamo% &e>> &r%sellformat%"}));
		languageKeys.put("Cmd.ClientLog.NoLogs", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cKeine Kundenlogs vorhanden!",
						"&cNo clientlogs available!"}));
		languageKeys.put("Cmd.ClientLog.Headline", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&f===&eKundenLog %player%, Seite %page%, Type %waytype%&f===",
						"&f===&eClientLog %player%, page %page%, Type %waytype%&f==="}));
		languageKeys.put("Cmd.ClientLog.Buy", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&7%time% [&#FF8800K&7]&f%shop% &#FF8800>> &r%item% &rx &e%amount% &#FF8800>> %format%",
						"&7%time% [&#FF8800B&7]&f%shop% &#FF8800>> &r%item% &rx &e%amount% &#FF8800>> %format%"}));
		languageKeys.put("Cmd.ClientLog.Sell", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&7%time% [&aV&7]&f%shop% &a>> &r%item% &rx &e%amount% &a>> &r%format%",
						"&7%time% [&aS&7]&f%shop% &a>> &r%item% &rx &e%amount% &a>> &r%format%"}));
		languageKeys.put("Cmd.ClientDailyLog.NoLogs", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cKeine täglichen Kundenlogs vorhanden!",
						"&cNo daily Signshoplogs available!"}));
		languageKeys.put("Cmd.ClientDailyLog.Headline", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&f===&eKundenTagesLog %player%, Seite %page%&f===",
						"&f===&eShopLog %player%, page %page%&f==="}));
		languageKeys.put("Cmd.ClientDailyLog.Log", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&7%time% &e>> &7[&#FF8800K&7] &rx &e%buyamo% &e>> &r%buyformat% &r| &7[&aV&7] &rx &e%sellamo% &e>> &r%sellformat%",
						"&7%time% &e>> &7[&#FF8800B&7] &rx &e%buyamo% &e>> &r%buyformat% &r| &7[&aS&7] &rx &e%sellamo% &e>> &r%sellformat%"}));
		languageKeys.put("Cmd.Search.NoItemInHand", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu hast kein Item in HauptHand!",
						"&cYou have no item in your main hand!"}));
		languageKeys.put("Cmd.Search.MaterialDontExist", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDas angegebene Material existiert nicht!",
						"&cThe specified material does not exist!"}));
		languageKeys.put("Cmd.Search.SearchRadiusNoCorrectFormat", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cIn der Config ist der Suchradius für den Searchbefehl unkorrekt. Möglich ist: PROXY, SERVER; WORLD, oder eine Zahl für einen Blockradius.",
						"&cThe search radius for the search command is incorrect in the config. The following is possible: PROXY, SERVER; WORLD, or a number for a block radius."}));
		languageKeys.put("Cmd.Search.SearchListEmpty", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDeine Suchparameter ergab kein Ergebnis! Parameter: %mat%, %displayname%",
						"&cYour search parameters did not return any results! Parameters: %mat%, %displayname%"}));
		languageKeys.put("Cmd.Search.Buy.LocationInfo", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&7====================",
						"&eShop &f%signshopname% &e- Eigentümer &f%owner%",
						"&eItems: &f%itemstoragecurrent% / %itemstoragetotal%",
						"&eKaufkosten: &f%buyraw1%",
						"&eLocation: &f%server% - %world% | %x% %y% %z%",
						"&7====================",
						"&7====================",
						"&eShop &f%signshopname% &e- Owner &f%owner%",
						"&eItems: &f%itemstoragecurrent% / %itemstoragetotal%",
						"&eBuycosts: &f%buyraw1%",
						"&eLocation: &f%server% - %world% | %x% %y% %z%",
						"&7===================="}));
		languageKeys.put("Cmd.Search.Sell.LocationInfo", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&7====================",
						"&eShop &f%signshopname% &e- Eigentümer &f%owner%",
						"&eItems: &f%itemstoragecurrent% / %itemstoragetotal%",
						"&eVerkaufkosten: &f%sellraw1%",
						"&eLocation: &f%server% - %world% | %x% %y% %z%",
						"&7====================",
						"&7====================",
						"&eShop &f%signshopname% &e- Owner &f%owner%",
						"&eItems: &f%itemstoragecurrent% / %itemstoragetotal%",
						"&eSellcosts: &f%sellraw1%",
						"&eLocation: &f%server% - %world% | %x% %y% %z%",
						"&7===================="}));
		languageKeys.put("Cmd.Search.TeleportIsNull", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDas Plugin kann nicht auf Serverübergreifend teleportieren. API-Zugang fehlt.",
						"&cThe plugin cannot teleport across servers. API access is missing."}));
		languageKeys.put("Cmd.Subscribed.NoSubscribes", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu hast keine Shops abonniert!",
						"&cYou have not subscribed to any shops!"}));
		languageKeys.put("Cmd.Subscribed.NoSubscribesFound", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cEs wurden keine abonnierten Shops nach den Parameter gefunden!",
						"&cNo subscribed stores were found according to the parameters!"}));
		languageKeys.put("Cmd.Subscribed.NoSameServer", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDen Shop den du einsehen möchtest ist nicht auf dem gleichen Server!",
						"&cThe store you want to view is not on the same server!"}));
		languageKeys.put("Cmd.Subscribed.LocationInfo", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&7====================",
						"&eShop &f%signshopname% &e- Eigentümer &f%owner%",
						"&eItems: &f%itemstoragecurrent% / %itemstoragetotal%",
						"&eLocation: &f%server% - %world% | %x% %y% %z%",
						"&7====================",
						"&7====================",
						"&eShop &f%signshopname% &e- Owner &f%owner%",
						"&eItems: &f%itemstoragecurrent% / %itemstoragetotal%",
						"&eLocation: &f%server% - %world% | %x% %y% %z%",
						"&7===================="}));
	}
	
	public void initModifierValueEntryLanguage() //INFO:ModifierValueEntryLanguages
	{
		mvelanguageKeys.put(Bypass.Permission.SHOP_CREATION.toString()+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eShop-Erstellungsrecht",
						"&eShop creation right"}));
		mvelanguageKeys.put(Bypass.Permission.SHOP_CREATION.toString()+".Explanation",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&ePermission, welche erlaubt SchilderShop",
						"&eerstellen zu können.",
						"&ePermission, which allows",
						"&eto create SchilderShop."}));
		mvelanguageKeys.put(Bypass.Permission.SHOP_GUI_BYPASS.toString()+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eShop-Adminstrationbypass",
						"&eShop administration bypass"}));
		mvelanguageKeys.put(Bypass.Permission.SHOP_GUI_BYPASS.toString()+".Explanation",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&ePermission, welche spezifische",
						"&eAdministrative Rechte für Shops gibt.",
						"&ePermission, which gives specific",
						"&eadministrative rights for shops."}));
		mvelanguageKeys.put(Bypass.Permission.SHOP_LOG_OTHERPLAYER.toString()+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eShoplog andere Spieler Einsichtrecht",
						"&eShop log other players right of inspection"}));
		mvelanguageKeys.put(Bypass.Permission.SHOP_LOG_OTHERPLAYER.toString()+".Explanation",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&ePermission, welche erlaubt von anderen",
						"&eSpieler die Shoplog einzusehen.",
						"&ePermission, which allows other",
						"&eplayers to view the shoplog."}));
		mvelanguageKeys.put(Bypass.Permission.CLIENT_LOG_OTHERPLAYER.toString()+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eKundenlog andere Spieler Einsichtrecht",
						"&eClient log other players right of inspection"}));
		mvelanguageKeys.put(Bypass.Permission.CLIENT_LOG_OTHERPLAYER.toString()+".Explanation",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&ePermission, welche erlaubt von anderen",
						"&eSpieler die clientlog einzusehen.",
						"&ePermission, which allows other",
						"&eplayers to view the clientlog."}));
		mvelanguageKeys.put(Bypass.Counter.SHOP_CREATION_AMOUNT_.toString()+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eAnzahl zu erstellende Shops",
						"&eNumber of stores to be created"}));
		mvelanguageKeys.put(Bypass.Counter.SHOP_CREATION_AMOUNT_.toString()+".Explanation",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eZählpermission, welche die Anzahl",
						"&ezu erstellender definiert.",
						"&eCounting mission, which defines",
						"&ethe number to be created."}));
		mvelanguageKeys.put(Bypass.Counter.SHOP_ITEMSTORAGE_AMOUNT_.toString()+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eGröße des Shoplagerraums",
						"&eShop storageroom size"}));
		mvelanguageKeys.put(Bypass.Counter.SHOP_ITEMSTORAGE_AMOUNT_.toString()+".Explanation",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eZählpermission, welche die Größe",
						"&edes Shoplagerraums definiert.",
						"&eCounting mission, which defines the",
						"&esize of the shopstoreroom."}));
		mvelanguageKeys.put(Bypass.Counter.COST_ADDING_STORAGE.toString()+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eKosten für die Vergrößerung des Shoplagerraums",
						"&eCosts for the enlargement of the store storage room"}));
		mvelanguageKeys.put(Bypass.Counter.COST_ADDING_STORAGE.toString()+".Explanation",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eZählpermission, welche die Kosten für die",
						"&eVergrößerung des Shoplagerraums definiert.",
						"&eCount mission that defines the cost",
						"&eof increasing the store storage space."}));
		mvelanguageKeys.put(Bypass.Counter.SHOP_BUYING_TAX.toString()+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eProzentuale Kaufsteuer",
						"&ePercentage buy tax"}));
		mvelanguageKeys.put(Bypass.Counter.SHOP_BUYING_TAX.toString()+".Explanation",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eZählpermission, welche die prozentualen Kaufsteuer",
						"&edes Shops definiert. Zählt für den Shopeigentümer.",
						"&eCounting mission, which defines the percentage",
						"&ebuy tax of the shop. Counts for shop owner."}));
		mvelanguageKeys.put(Bypass.Counter.SHOP_SELLING_TAX.toString()+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eProzentuale Verkaufsteuer",
						"&ePercentage sell tax"}));
		mvelanguageKeys.put(Bypass.Counter.SHOP_SELLING_TAX.toString()+".Explanation",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eZählpermission, welche die prozentualen Verkaufsteuer",
						"&edes Shops definiert. Zählt für den Kunden.",
						"&eCounting mission, which defines the percentage",
						"&esell tax of the shop. Counts for shop owner."}));
	}
	
	public void initMaterialLanguage() //INFO:MaterialLanguages
	{		
		List<Material> signList = 
				Arrays.asList(new Material[] {
						Material.ACACIA_SIGN,
						Material.ACACIA_WALL_SIGN,
						Material.ACACIA_HANGING_SIGN,
						Material.BIRCH_SIGN,
						Material.BIRCH_WALL_SIGN,
						Material.BIRCH_HANGING_SIGN,
						Material.CHERRY_SIGN,
						Material.CHERRY_WALL_SIGN,
						Material.CHERRY_HANGING_SIGN,
						Material.CRIMSON_SIGN,
						Material.CRIMSON_WALL_SIGN,
						Material.CRIMSON_HANGING_SIGN,
						Material.DARK_OAK_SIGN,
						Material.DARK_OAK_WALL_SIGN,
						Material.DARK_OAK_HANGING_SIGN,
						Material.JUNGLE_SIGN,
						Material.JUNGLE_WALL_SIGN,
						Material.JUNGLE_HANGING_SIGN,
						Material.MANGROVE_SIGN,
						Material.MANGROVE_WALL_SIGN,
						Material.MANGROVE_HANGING_SIGN,
						Material.OAK_SIGN,
						Material.OAK_WALL_SIGN,
						Material.OAK_HANGING_SIGN,
						Material.SPRUCE_SIGN,
						Material.SPRUCE_WALL_SIGN,
						Material.SPRUCE_HANGING_SIGN,
						Material.WARPED_SIGN,
						Material.WARPED_WALL_SIGN,
						Material.WARPED_HANGING_SIGN
				});
		for(Material m : signList)
		{
			String c = "";
			switch(m)
			{
			default:
			case OAK_SIGN:
			case OAK_WALL_SIGN:
			case BIRCH_SIGN:
			case BIRCH_WALL_SIGN:
			case JUNGLE_SIGN:
			case JUNGLE_WALL_SIGN:
				c = "&0"; break;
			case SPRUCE_SIGN:
			case SPRUCE_WALL_SIGN:
			case SPRUCE_HANGING_SIGN:
			case ACACIA_SIGN:
			case ACACIA_WALL_SIGN:
			case ACACIA_HANGING_SIGN:
			case DARK_OAK_SIGN:
			case DARK_OAK_WALL_SIGN:
			case DARK_OAK_HANGING_SIGN:
			case MANGROVE_SIGN:
			case MANGROVE_WALL_SIGN:
			case MANGROVE_HANGING_SIGN:
			case CRIMSON_SIGN:
			case CRIMSON_WALL_SIGN:
			case CRIMSON_HANGING_SIGN:
				c = "&f"; break;
			case WARPED_SIGN:
			case WARPED_WALL_SIGN:
			case WARPED_HANGING_SIGN:
				c = "&e"; break;
			}
			matlanguageKeys.put(m.toString()+"_SignStartColor",
					new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
							c}));
		}
	}
	
	public void initGuiAdministration() //INFO:GuiAdministration
	{
		LinkedHashMap<String, Language> admin = new LinkedHashMap<>();
		String path = "4"; //InfoItem, wie es ist
		admin.put(path+".IsInfoItem",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						true}));
		path = "13"; //InfoZumShop
		admin.put(path+".SettingLevel",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						SettingsLevel.NOLEVEL.toString()}));
		admin.put(path+".Material",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.GOLD_ORE.toString()}));
		admin.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cInfo zum Shop &f%displayname%",
						"&cInfo from Shop &f%displayname%"}));
		admin.put(path+".Lore."+SettingsLevel.BASE.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cEigentümer: &f%owner%",
						"&cShopname: &f%signshopname%",
						"&cLager Aktuelle Items: &f%itemstoragecurrent%",
						"&cLager Gesamter Itemsplatz: &f%itemstoragetotal%",
						"&cKaufpreis: &f%buyraw1%",
						"&cVerkaufpreis: &f%sellraw1%",
						
						"&cOwner: &f%owner%",
						"&cShopname: &f%signshopname%",
						"&cStorage Items Actual: &f%itemstoragecurrent%",
						"&cStorage Items Total: &f%itemstoragetotal%",
						"&cBuyprice: &f%buyraw1%",
						"&cSellprice: &f%sellraw1%"}));
		admin.put(path+".Lore."+SettingsLevel.ADVANCED.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cEigentümer: &f%owner%",
						"&cShopname: &f%signshopname%",
						"&cErstellungsdatum: &f%creationdate%",
						"&cLeuchtendes Schild: &f%glow%",
						"&cLager Aktuelle Items: &f%itemstoragecurrent%",
						"&cLager Gesamter Itemsplatz: &f%itemstoragetotal%",
						"&cKauf Aktiv: &f%buytoggle%",
						"&cVerkauf Aktiv: &f%selltoggle%",
						"&cKaufpreis: &f%buyraw1%",
						"&cVerkaufpreis: &f%sellraw1%",
						"&cMöglicher Kauf/Ankauf: &f%possiblebuy% <> %possiblesell%",
						
						"&cOwner: &f%owner%",
						"&cShopname: &f%signshopname%",
						"&cCreationdatum: &f%creationdate%",
						"&cLuminous shield: &f%glow%",
						"&cStorage Items Actual: &f%itemstoragecurrent%",
						"&cStorage Items Total: &f%itemstoragetotal%",
						"&cBuy Active: &f%buytoggle%",
						"&cSell Active: &f%selltoggle%",
						"&cBuyprice: &f%buyraw1%",
						"&cSellprice: &f%sellraw1%",
						"&cPossible Buy/Sell: &f%possiblebuy% <> %possiblesell%"}));
		admin.put(path+".Lore."+SettingsLevel.EXPERT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cEigentümer: &f%owner%",
						"&cShopname: &f%signshopname%",
						"&cErstellungsdatum: &f%creationdate%",
						"&cLeuchtendes Schild: &f%glow%",
						"&cListenTyp: &f%listtype%",
						"&cItemHologram: &f%hologram%",
						"&cLager Aktuelle Items: &f%itemstoragecurrent%",
						"&cLager Gesamter Itemsplatz: &f%itemstoragetotal%",
						"&cLocation: &f%server%-%world%-&7%x%&f/&7%y%&f/&7%z%",
						"&cAccount: &f%accountid% - %accountname%",
						"&cKauf Aktiv: &f%buytoggle%",
						"&cVerkauf Aktiv: &f%selltoggle%",
						"&cKaufpreis: &f%buyraw1%",
						"&cVerkaufpreis: &f%sellraw1%",
						"&cMöglicher Kauf/Ankauf: &f%possiblebuy% <> %possiblesell%",
						"&cUnlimitierter Kauf: &f%unlimitedbuy%",
						"&cUnlimitierter Verkauf: &f%unlimitedsell%",
						
						"&cOwner: &f%owner%",
						"&cShopname: &f%signshopname%",
						"&cCreationdatum: &f%creationdate%",
						"&cLuminous shield: &f%glow%",
						"&cListType: &f%listtype%",
						"&cItemHologram: &f%hologram%",
						"&cStorage Items Actual: &f%itemstoragecurrent%",
						"&cStorage Items Total: &f%itemstoragetotal%",
						"&cLocation: &f%server%-%world%-&7%x%&f/&7%y%&f/&7%z%",
						"&cAccount: &f%accountid% - %accountname%",
						"&cBuy Active: &f%buytoggle%",
						"&cSell Active: &f%selltoggle%",
						"&cBuyprice: &f%buyraw1%",
						"&cSellprice: &f%sellraw1%",
						"&cPossible Buy/Sell: &f%possiblebuy% <> %possiblesell%",
						"&cUnlimited Buy: &f%unlimitedbuy%",
						"&cUnlimited Sell: &f%unlimitedsell%"}));
		admin.put(path+".Lore."+SettingsLevel.MASTER.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cId: &f%id%",
						"&cEigentümer: &f%owner%",
						"&cShopname: &f%signshopname%",
						"&cErstellungsdatum: &f%creationdate%",
						"&cLeuchtendes Schild: &f%glow%",
						"&cListenTyp: &f%listtype%",
						"&cItemHologram: &f%hologram%",
						"&cLager Aktuelle Items: &f%itemstoragecurrent%",
						"&cLager Gesamter Itemsplatz: &f%itemstoragetotal%",
						"&cLocation: &f%server%-%world%-&7%x%&f/&7%y%&f/&7%z%",
						"&cAccount: &f%accountid% - %accountname%",
						"&cKauf Aktiv: &f%buytoggle%",
						"&cVerkauf Aktiv: &f%selltoggle%",
						"&cKaufpreis: &f%buyraw1%",
						"&cVerkaufpreis: &f%sellraw1%",
						"&cMöglicher Kauf/Ankauf: &f%possiblebuy% <> %possiblesell%",
						"&cRabatt Start: &f%discountstart%",
						"&cRabatt Ende: %discountend%",
						"&cRabattkaufpreis: &f%discountbuy1%",
						"&cRabattVerkaufpreis: &f%discountsell1%",
						"&cMöglicher Rabatt Kauf: &f%discountpossiblebuy%",
						"&cMöglicher Rabatt Verkauf: &f%discountpossiblesell%",
						"&cLagersystemID: &f%storageid%",
						"&cUnlimitierter Kauf: &f%unlimitedbuy%",
						"&cUnlimitierter Verkauf: &f%unlimitedsell%",
						
						"&cId: &f%id%",
						"&cOwner: &f%owner%",
						"&cShopname: &f%signshopname%",
						"&cCreationdatum: &f%creationdate%",
						"&cLuminous shield: &f%glow%",
						"&cListType: &f%listtype%",
						"&cItemHologram: &f%hologram%",
						"&cStorage Items Actual: &f%itemstoragecurrent%",
						"&cStorage Items Total: &f%itemstoragetotal%",
						"&cLocation: &f%server%>%world%>&7%x%&f/&7%y%&f/&7%z%",
						"&cAccount: &f%accountid% - %accountname%",
						"&cBuy Active: &f%buytoggle%",
						"&cSell Active: &f%selltoggle%",
						"&cBuyprice: &f%buyraw1%",
						"&cSellprice: &f%sellraw1%",
						"&cPossible Buy/Sell: &f%possiblebuy% <> %possiblesell%",
						"&cDiscount Start: &f%discountstart%",
						"&cDiscount Ende: &f%discountend%",
						"&cDiscountbuyprice: &f%discountbuy1%",
						"&cDiscountsellprice: &f%discountsell1%",
						"&cPossible Discount Buy: &f%discountpossiblebuy%",
						"&cPossible Discount Sell: &f%discountpossiblesell%",
						"&cStoragesystemID: &f%storageid%",
						"&cUnlimited Buy: &f%unlimitedbuy%",
						"&cUnlimited Sell: &f%unlimitedsell%"}));
		path = "6"; //ItemStack clear
		admin.put(path+".SettingLevel",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						SettingsLevel.ADVANCED.toString()}));
		admin.put(path+".Material",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.COMPOSTER.toString()}));
		admin.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cItem zurücksetzen",
						"&cReset item"}));
		admin.put(path+".Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&dSetzt das Item des Shop zurück.",
						"&bFunktioniert nur, wenn alle Items",
						"&baus dem Lagerraum entfernt worden sind.",
						"&bDanach kann man ein neues Item setzen.",
						"&bResets the item of the shio.",
						"&bWorks only when all items have been",
						"&bremoved from the storage room.",
						"&bAfter that you can set a new item."}));
		admin.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_ITEM_CLEAR.toString()}));
		admin.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_ITEM_CLEAR.toString()}));
		path = "36"; //Toggle Buy
		admin.put(path+".SettingLevel",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						SettingsLevel.ADVANCED.toString()}));
		admin.put(path+".Material",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.GOLD_ORE.toString()}));
		admin.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&dToggel Kauf (zz. &r%buytoggle%&d)",
						"&dToggle Buying (atm. &r%buytoggle%&d)"}));
		admin.put(path+".Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&bToggelt den Kauf.",
						"&bSollte der Kauf ausgeschaltet sein,",
						"&bist der Kauf nicht mehr möglich.",
						"&bToggles the buying",
						"&bIf the buying is turned off,",
						"&bthe buying is no longer possible."}));
		admin.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_TOGGLE_BUY.toString()}));
		admin.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_TOGGLE_BUY.toString()}));
		path = "37"; //Toggle Sell
		admin.put(path+".SettingLevel",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						SettingsLevel.ADVANCED.toString()}));
		admin.put(path+".Material",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.IRON_ORE.toString()}));
		admin.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&dToggel Verkauf (zz. &r%selltoggle%&d)",
						"&dToggle Selling (atm. &r%selltoggle%&d)"}));
		admin.put(path+".Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&bToggelt den Verkauf.",
						"&bSollte der Verkauf ausgeschaltet sein,",
						"&bist der Verkauf nicht mehr möglich.",
						"&bToggles the selling",
						"&bIf the seling is turned off,",
						"&bthe selling is no longer possible."}));
		admin.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_TOGGLE_SELL.toString()}));
		admin.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_TOGGLE_SELL.toString()}));
		path = "45"; //SetBuy
		admin.put(path+".CanBuy",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						true}));
		admin.put(path+".SettingLevel",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						SettingsLevel.NOLEVEL.toString()}));
		admin.put(path+".Material",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.GOLD_INGOT.toString()}));
		admin.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&dEingabe des Kaufswertes für 1 Item",
						"&dEnter the buy value for 1 item"}));
		admin.put(path+".Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&bZz.: &f%buyraw1%",
						"&cQ &bfür das Entfernen des Preises.",
						"&cLinks-/Rechtsklick &bzum öffnen des Numpad Gui.",
						"&bAtm.: &f%buyraw1%",
						"&cQ &bfor removing the price.",
						"&cLinks-/Rechtsklick &bto open the numpad gui."}));
		admin.put(path+".ClickFunction."+ClickType.DROP.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_SETBUY_CLEAR.toString()}));
		admin.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_SETBUY_OPEN_NUMPAD.toString()}));
		admin.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_SETBUY_OPEN_NUMPAD.toString()}));
		path = "46"; //SetSell
		admin.put(path+".CanSell",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						true}));
		admin.put(path+".SettingLevel",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						SettingsLevel.NOLEVEL.toString()}));
		admin.put(path+".Material",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.IRON_INGOT.toString()}));
		admin.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&dEingabe des Verkaufswertes für 1 Item",
						"&dEnter the sell value for 1 item"}));
		admin.put(path+".Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&bZz.: &f%sellraw1%",
						"&cQ &bzum entfernen des Preises.",
						"&cLinks-/Rechtsklick &bzum öffnen des Numpad Gui.",
						"&bAtm.: &f%sell1%",
						"&cQ &bfor removing the price.",
						"&cLinks-/Rechtsklick &bto open the numpad gui."}));
		admin.put(path+".ClickFunction."+ClickType.DROP.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_SETSELL_CLEAR.toString()}));
		admin.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_SETSELL_OPEN_NUMPAD.toString()}));
		admin.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_SETSELL_OPEN_NUMPAD.toString()}));
		path = "0"; //SettingsLevelToggle
		admin.put(path+".SettingLevel",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						SettingsLevel.NOLEVEL.toString()}));
		admin.put(path+".Material."+SettingsLevel.NOLEVEL.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.OAK_WOOD.toString()}));
		admin.put(path+".Material."+SettingsLevel.BASE.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.OAK_WOOD.toString()}));
		admin.put(path+".Material."+SettingsLevel.ADVANCED.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.STONE.toString()}));
		admin.put(path+".Material."+SettingsLevel.EXPERT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.DIAMOND.toString()}));
		admin.put(path+".Material."+SettingsLevel.MASTER.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.NETHERITE_INGOT.toString()}));
		admin.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&dSwitcht die Gui-Level-Ansicht",
						"&dSwitch the Gui level view"}));
		admin.put(path+".Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cLinksklick &bfür das Basis Level.",
						"&cRechtsklick &bfür das Fortgeschrittene Level.",
						"&cShift Linksklick &bfür das Experten Level.",
						"&cShift Rechtsklick &bfür das Meister Level.",
						"&cLeftclick &bfor the basic level.",
						"&cRightclick &bfor the advanced level.",
						"&cShift-Leftclick  &bfor the expert level.",
						"&cShift-Rightclick &bfor the master level."}));
		admin.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_SETTINGSLEVEL_SETTO_BASE.toString()}));
		admin.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_SETTINGSLEVEL_SETTO_ADVANCED.toString()}));
		admin.put(path+".ClickFunction."+ClickType.SHIFT_LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_SETTINGSLEVEL_SETTO_EXPERT.toString()}));
		admin.put(path+".ClickFunction."+ClickType.SHIFT_RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_SETTINGSLEVEL_SETTO_MASTER.toString()}));
		path = "9"; //SetAccount
		admin.put(path+".IFHDepend",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						true}));
		admin.put(path+".SettingLevel",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						SettingsLevel.EXPERT.toString()}));
		admin.put(path+".Material",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.OAK_DOOR.toString()}));
		admin.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&dEingabe des Wirtschaftsaccount per ID",
						"&dEnter the economyaccount per ID"}));
		admin.put(path+".Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&bZz.: &f%accountid% - %accountname%",
						"&cQ &bzum stellen auf den Default Account.",
						"&cLinks-/Rechtsklick &bzum öffnen des Numpad Gui.",
						"&bAtm.: &f%accountid% - %accountname%",
						"&cQ &bto set to the default account.",
						"&cLeft/right click &to open the Numpad Gui."}));
		admin.put(path+".ClickFunction."+ClickType.DROP.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_SETACCOUNT_DEFAULT.toString()}));
		admin.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_SETACCOUNT_OPEN_NUMPAD.toString()}));
		admin.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_SETACCOUNT_OPEN_NUMPAD.toString()}));
		path = "18"; //AddStorage
		admin.put(path+".SettingLevel",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						SettingsLevel.NOLEVEL.toString()}));
		admin.put(path+".Material",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.CHEST.toString()}));
		admin.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&dErhöht den Lageraum des Shop",
						"&dSwitch the Gui level view"}));
		admin.put(path+".Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&bZz.: &f%itemstoragetotal%",
						"&cLinksklick &berhöht den Lagerraum um 1 Item.",
						"&cRechtsklick &berhöht den Lagerraum um 8 Items.",
						"&cQ &berhöht den Lagerraum um 16 Items.",
						"&cShift Linksklick &berhöht den Lagerraum um 32 Items.",
						"&cShift Rechtsklick &berhöht den Lagerraum um 64 Items",
						"&cCtrl Q &berhöht den Lagerraum um 576 Items",
						"&c1 &berhöht den Lagerraum um 1728 Items",
						"&c2 &berhöht den Lagerraum um 3456 Items",
						"&c3 &berhöht den Lagerraum um 6912 Items",
						"&bAtm.: &f%itemstoragetotal%",
						"&cLeftclick &bincreases the storage space by 1 item.",
						"&cRightclick &bincreases the storage space by 8 items.",
						"&cQ &bincreases the storage space by 16 item.",
						"&cShift-Leftclick  &bincreases the storage space by 32 items.",
						"&cShift-Rightclick &bincreases the storage space by 64 items.",
						"&cCtrl Q &bincreases the storage space by 576 items.",
						"&c1 &bincreases the storage space by 1728 items.",
						"&c2 &bincreases the storage space by 3456 items.",
						"&c3&bincreases the storage space by 6912 items.",}));
		admin.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_ADDSTORAGE_1.toString()}));
		admin.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_ADDSTORAGE_8.toString()}));
		admin.put(path+".ClickFunction."+ClickType.DROP.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_ADDSTORAGE_16.toString()}));
		admin.put(path+".ClickFunction."+ClickType.SHIFT_LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_ADDSTORAGE_32.toString()}));
		admin.put(path+".ClickFunction."+ClickType.SHIFT_RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_ADDSTORAGE_64.toString()}));
		admin.put(path+".ClickFunction."+ClickType.CTRL_DROP.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_ADDSTORAGE_576.toString()}));
		admin.put(path+".ClickFunction."+ClickType.NUMPAD_1.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_ADDSTORAGE_1728.toString()}));
		admin.put(path+".ClickFunction."+ClickType.NUMPAD_2.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_ADDSTORAGE_3456.toString()}));
		admin.put(path+".ClickFunction."+ClickType.NUMPAD_3.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_ADDSTORAGE_6912.toString()}));
		path = "1"; //SetShop Name
		admin.put(path+".SettingLevel",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						SettingsLevel.EXPERT.toString()}));
		admin.put(path+".Material",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.BARREL.toString()}));
		admin.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&dUmbenennung des Shops",
						"&dRename of the shop"}));
		admin.put(path+".Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&bZz.: &f%signshopname%",
						"&cLinks-/Rechtsklick &bzum öffnen des Tastatur Gui.",
						"&bAtm.: &f%signshopname%",
						"&cLeft/right click &to open the Keyboard Gui."}));
		admin.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_SETSIGNSHOPNAME_OPENKEYBOARD.toString()}));
		admin.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_SETSIGNSHOPNAME_OPENKEYBOARD.toString()}));
		path = "10"; //Setglowing
		admin.put(path+".SettingLevel",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						SettingsLevel.ADVANCED.toString()}));
		admin.put(path+".Material",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.GLOW_INK_SAC.toString()}));
		admin.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&dLeuchtfunktion des Schild",
						"&dGlow function of the sign"}));
		admin.put(path+".Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&bZz.: &f%glow%",
						"&cLinksklick &banstellen der Leuchtfunktion des Schildes.",
						"&cRechtsklick &babstellen der Leuchtfunktion des Schildes.",
						"&bAtm.: &f%glow%",
						"&cLeft click &bturn on the luminous function of the sign.",
						"&cRight click &bturn off the luminous function of the sign."}));
		admin.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_SETGLOWING.toString()}));
		admin.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_SETUNGLOWING.toString()}));
		path = "2"; //SetListType
		admin.put(path+".SettingLevel",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						SettingsLevel.EXPERT.toString()}));
		admin.put(path+".Material",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.LECTERN.toString()}));
		admin.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&dSetzen des Listfunktion des Shops",
						"&dSetting the list function of the store"}));
		admin.put(path+".Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&bZz.: &f%listtype%",
						"&bStellt den Zugang zum Shop auf eine",
						"&bspezielle Liste von Spieler ein.",
						"&bSollten Spieler nicht auf der jeweiligen Liste sein,",
						"&bhaben Sie kein Zugriff auf den Shop!",
						"&cLinksklick, &bdass alle Spieler Zugriff auf dem Shop haben.",
						"&cRechtsklick &bnur für Mitglieder.",
						"&cQ &bfür die Blacklist.",
						"&cCtrl Q &bfür die Whitelist.",
						"&c1 &bnur für benutzerdefinierte Spieler.",
						"&bAtm.: &f%listtype%",
						"&bSets access to the store to a",
						"&bspecial list of players.",
						"&bIf players are not on the respective list,",
						"&byou will not have access to the store!",
						"&cLeft click, &bthat all players have access to the store.",
						"&cRight click &bonly for member.",
						"&cQ &bfor the Blacklist.",
						"&cCtrl Q &bfor the Whitelist.",
						"&c1 &bfor custom players only."}));
		admin.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_SETLISTEDTYPE_ALL.toString()}));
		admin.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_SETLISTEDTYPE_MEMBER.toString()}));
		admin.put(path+".ClickFunction."+ClickType.DROP.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_SETLISTEDTYPE_BLACKLIST.toString()}));
		admin.put(path+".ClickFunction."+ClickType.CTRL_DROP.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_SETLISTEDTYPE_WHITELIST.toString()}));
		admin.put(path+".ClickFunction."+ClickType.NUMPAD_1.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_SETLISTEDTYPE_CUSTOM.toString()}));
		path = "11"; //AddPlayerToList
		admin.put(path+".SettingLevel",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						SettingsLevel.EXPERT.toString()}));
		admin.put(path+".Material",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.SKELETON_SKULL.toString()}));
		admin.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&d+ oder - von Spieler auf den Listen",
						"&dAdd or remove players from the lists"}));
		admin.put(path+".Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&bÖffnet einer der Keyboard Guis,",
						"&bum Spieler zu den jeweiligen Listen hinzuzufügen",
						"&boder zu entfernen. Hier können Mitglieder,",
						"&bBlack- und Whitelist sowie eine",
						"&bbenutzerdefinierte Liste bearbeitet werden.",
						"&cLinksklick &böffnet das Keyboard Gui für die Blacklist.",
						"&cRechtsklick &böffnet das Keyboard Gui für die Whitelist.",
						"&cShift-L.Klick &böffnet das Keyboard Gui für die Mitglieder.",
						"&cShift-R.Klick &böffnet das Keyboard Gui",
						"&bfür die benutzerdefinierte Liste.",
						
						"&bOpens one of the Keyboard Guis",
						"&bto add or remove players",
						"&bfrom the respective lists.",
						"&bMembers, blacklist, whitelist",
						"&band a custom list can be edited here.",
						"&cLeftclick &bopens the keyboard gui for the blacklist.",
						"&cRightclick &bopens the keyboard gui for the whitelist.",
						"&cShift-leftclick &bopens the keyboard Gui for the members.",
						"&cShift-rightclick &bopens the Keyboard Gui",
						"&bfor the user-defined list.",}));
		admin.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_ADDLISTEDTYPE_PLAYER_OPENKEYBOARD_BLACKLIST.toString()}));
		admin.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_ADDLISTEDTYPE_PLAYER_OPENKEYBOARD_WHITELIST.toString()}));
		admin.put(path+".ClickFunction."+ClickType.SHIFT_LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_ADDLISTEDTYPE_PLAYER_OPENKEYBOARD_MEMBER.toString()}));
		admin.put(path+".ClickFunction."+ClickType.SHIFT_RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_ADDLISTEDTYPE_PLAYER_OPENKEYBOARD_CUSTOM.toString()}));
		path = "12"; //PlayersOnList
		admin.put(path+".SettingLevel",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						SettingsLevel.EXPERT.toString()}));
		admin.put(path+".Material",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.ZOMBIE_HEAD.toString()}));
		admin.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&dSpieler auf den jeweiligen Listen",
						"&dPlayers on the respective lists"}));
		admin.put(path+".Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&bÖffnet einen Chateintrag,",
						"&bvon Spieler die auf den jeweiligen Listen sind.",
						"&cLinksklick &bfür die Blacklist.",
						"&cRechtsklick &bfür die Whitelist.",
						"&cShift-L.Klick &bfür die Mitglieder.",
						"&cShift-R.Klick &bfür die benutzerdefinierte Liste.",
						
						"&bOpens a chatentry",
						"&bof players from the respective lists.",
						"&cLeftclick &bfor the blacklist.",
						"&cRightclick &bfor the whitelist.",
						"&cShift-leftclick &bfor the members.",
						"&cShift-rightclick &bfor the user-defined list."}));
		admin.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_LISTEDTYPE_PLAYER_OPENLIST_BLACKLIST.toString()}));
		admin.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_LISTEDTYPE_PLAYER_OPENLIST_WHITELIST.toString()}));
		admin.put(path+".ClickFunction."+ClickType.SHIFT_LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_LISTEDTYPE_PLAYER_OPENLIST_MEMBER.toString()}));
		admin.put(path+".ClickFunction."+ClickType.SHIFT_RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_LISTEDTYPE_PLAYER_OPENLIST_CUSTOM.toString()}));
		path = "20"; //ToggleItemHologram
		admin.put(path+".Permission",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						".item.hologram"}));
		admin.put(path+".SettingLevel",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						SettingsLevel.EXPERT.toString()}));
		admin.put(path+".Material",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.END_CRYSTAL.toString()}));
		admin.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&dToggel ItemHologram",
						"&dToggle ItemHologram"}));
		admin.put(path+".Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&bZz.: &f%hologram%",
						"&bToggelt das ItemHologram.",
						"&bWenn aktiv, so erscheint mit einem Item in der Hand",
						"&bund einem Linksklick auf das Shopschild das ItemHologram.",
						"&cLinksklick &baktiviert das Hologram.",
						"&cRechtsklick &bdeaktiviert das Hologram.",
						"&bAtm.: &f%hologram%",
						"&bToggles the ItemHologram.",
						"&bIf active, with an item in hand",
						"&band a left click on the store sign, the ItemHologram will appear.",
						"&cLeftclick &bactivates the hologram.",
						"&cRightclick &bdeactivates the hologram."}));
		admin.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_SETITEMHOLOGRAM_ACTIVE.toString()}));
		admin.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_SETITEMHOLOGRAM_DEACTIVE.toString()}));
		path = "15"; //Open Shoplog
		admin.put(path+".SettingLevel",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						SettingsLevel.NOLEVEL.toString()}));
		admin.put(path+".Material",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.CHEST.toString()}));
		admin.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&dÖffnet das Shoplog",
						"&dOpen the shoplog"}));
		admin.put(path+".Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cLinks- und Rechtsklick &böffnet den Shoplog.",
						"&bEs erscheint im Chat der ShopLog.",
						"&cLeft/rightclick &bopen the shoplog.",
						"&bThe ShopLog appears in the chat."}));
		admin.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_OPEN_SHOPLOG.toString()}));
		admin.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_OPEN_SHOPLOG.toString()}));
		path = "40"; //Unlimited Toggle Buy
		admin.put(path+".Permission",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						".unlimited.buy"}));
		admin.put(path+".CanBuy",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						true}));
		admin.put(path+".SettingLevel",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						SettingsLevel.EXPERT.toString()}));
		admin.put(path+".Material",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.GOLD_BLOCK.toString()}));
		admin.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&dToggel Unlimitierter Kauf",
						"&dToggle unlimited Buying"}));
		admin.put(path+".Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&bZz. &f%unlimitedbuy%",
						"&bToggelt den unlimitierten Kauf.",
						"&bSollte der unlimitierter Kauf angeschaltet sein,",
						"&bkönnen unbegrenzt Items Kauft werden,",
						"&bohne jedweden Vorrat.",
						"&bAtm. &f%unlimitedbuy%",
						"&bToggles the unlimited buying",
						"&bIf the unlimited buying is turned on,",
						"&bunlimited items can be sold,",
						"&bwithout any stock."}));
		admin.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_UNLIMITED_TOGGLE_BUY.toString()}));
		admin.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_UNLIMITED_TOGGLE_BUY.toString()}));
		path = "49"; //Unlimited Toggle Sell
		admin.put(path+".Permission",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						".unlimited.sell"}));
		admin.put(path+".CanSell",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						true}));
		admin.put(path+".SettingLevel",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						SettingsLevel.EXPERT.toString()}));
		admin.put(path+".Material",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.GOLD_BLOCK.toString()}));
		admin.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&dToggel Unlimitierter Verkauf",
						"&dToggle unlimited Selling"}));
		admin.put(path+".Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&bZz. &f%unlimitedsell%",
						"&bToggelt den unlimitierten Verkauf.",
						"&bSollte der unlimitierter Verkauf angeschaltet sein,",
						"&bkönnen unbegrenzt Items verkauft werden,",
						"&bohne jedweden Platz im Lagerraum.",
						"&bAtm. &f%unlimitedsell%",
						"&bToggles the unlimited selling",
						"&bIf the unlimited selling is turned on,",
						"&bunlimited number of items can be purchased",
						"&bwithout any space in the storeroom."}));
		admin.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_UNLIMITED_TOGGLE_SELL.toString()}));
		admin.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_UNLIMITED_TOGGLE_SELL.toString()}));
		path = "21"; //Possible Buy
		admin.put(path+".CanBuy",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						true}));
		admin.put(path+".SettingLevel",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						SettingsLevel.ADVANCED.toString()}));
		admin.put(path+".Material",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.GOLD_NUGGET.toString()}));
		admin.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&dEingabe der Anzahl der verbleibenen Käufe",
						"&dEnter the number of buying remaining"}));
		admin.put(path+".Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&bZz.: &f%possiblebuy%",
						"&bVerbleibene Käufe, sind die Anzahl",
						"&ban Käufe die noch getätig werden können.",
						"&bIst die Zahl auf 0 können keine Items mehr gekauft werden.",
						"&cQ &bfür das Zurücksetzen.",
						"&cLinks/Rechtsklick &bzum öffnen des Numpad Gui.",
						"&bAtm.: &f%possiblebuy%",
						"&bRemaining sales, are the number of sales",
						"&bthat can still be made.",
						"&bIf the number is 0, no more items can be sold.",
						"&cQ &bfor resetting.",
						"&cLeft/right click &to open the Numpad Gui."}));
		admin.put(path+".ClickFunction."+ClickType.DROP.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_SETPOSSIBLE_BUY_CLEAR.toString()}));
		admin.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_SETPOSSIBLE_BUY_OPEN_NUMPAD.toString()}));
		admin.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_SETPOSSIBLE_BUY_OPEN_NUMPAD.toString()}));
		path = "23"; //Possible Sell
		admin.put(path+".CanSell",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						true}));
		admin.put(path+".SettingLevel",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						SettingsLevel.ADVANCED.toString()}));
		admin.put(path+".Material",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.IRON_NUGGET.toString()}));
		admin.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&dEingabe der Anzahl der verbleibenen Verkäufe",
						"&dEnter the number of purchase remaining"}));
		admin.put(path+".Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&bZz.: &f%possiblesell%",
						"&bVerbleibene Verkäufe, sind die Anzahl an",
						"&bVerkäufe die noch getätig werden können.",
						"&bIst die Zahl auf 0 können keine Items mehr ankauft werden.",
						"&cQ &bfür das Zurücksetzen.",
						"&cLinks/Rechtsklick &bzum öffnen des Numpad Gui.",
						"&bAtm.: &f%possiblesell%",
						"&bRemaining purchase, are the number of",
						"&bpurchase that can still be made.",
						"&bIf the number is 0, no more items can be purchase.",
						"&cQ &bfor resetting.",
						"&cLeft/right click &to open the Numpad Gui."}));
		admin.put(path+".ClickFunction."+ClickType.DROP.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_SETPOSSIBLE_SELL_CLEAR.toString()}));
		admin.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_SETPOSSIBLE_SELL_OPEN_NUMPAD.toString()}));
		admin.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_SETPOSSIBLE_SELL_OPEN_NUMPAD.toString()}));
		path = "8"; //Delete All but with no items in storage
		admin.put(path+".SettingLevel",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						SettingsLevel.NOLEVEL.toString()}));
		admin.put(path+".Material",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.BARRIER.toString()}));
		admin.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&dLöschung des Shops",
						"&dDeleting of the shop"}));
		admin.put(path+".Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cShift Links/Rechtsklick &blöscht den Shop.",
						"&bFunktioniert nur, wenn alle Items",
						"&baus dem Lagerraum entfernt worden sind.",
						"&cShift Left/rightclick &bdelete the shop.",
						"&bWorks only when all items have been",
						"&bremoved from the storage room."}));
		admin.put(path+".ClickFunction."+ClickType.SHIFT_LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_DELETE_WITHOUT_ITEMS_IN_STORAGE.toString()}));
		admin.put(path+".ClickFunction."+ClickType.SHIFT_RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_DELETE_WITHOUT_ITEMS_IN_STORAGE.toString()}));
		path = "17"; //Delete All
		admin.put(path+".SettingLevel",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						SettingsLevel.EXPERT.toString()}));
		admin.put(path+".Material",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.TNT.toString()}));
		admin.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&dLöschung des Shops",
						"&dDeleting of the shop"}));
		admin.put(path+".Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cShift Links/Rechtsklick &blöscht den Shop.",
						"&cAchtung!",
						"&bDurch das Klicken wird unverzüglich der gesamte Shop",
						"&bmit allen noch verbliebenen Items gelöscht!",
						"&cAchtung!",
						"&cShift Left/rightclick &bopen the shoplog.",
						"&cAttention!",
						"&bClicking immediately deletes the entire",
						"&bstore with all remaining items!",
						"&cAttention"}));
		admin.put(path+".ClickFunction."+ClickType.SHIFT_LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_DELETE_ALL.toString()}));
		admin.put(path+".ClickFunction."+ClickType.SHIFT_RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_DELETE_ALL.toString()}));
		path = "44"; //Discount Clear
		admin.put(path+".SettingLevel",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						SettingsLevel.MASTER.toString()}));
		admin.put(path+".Material",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.ENDER_EYE.toString()}));
		admin.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&dAbbrechen der Rabattaktion",
						"&dCancel of the discount promotion"}));
		admin.put(path+".Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cLinks- und Rechtsklick &bbricht die Rabattaktion ab.",
						"&bDabei wird nur der Rabattzeitraum zurückgesetzt.",
						"&cLeft - and rightclick &bcancels the discount promotion.",
						"&bThis only resets the discount period."}));
		admin.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_SETDISCOUNT_CLEAR.toString()}));
		admin.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_SETDISCOUNT_CLEAR.toString()}));
		path = "43"; //Discount Start/End
		admin.put(path+".SettingLevel",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						SettingsLevel.MASTER.toString()}));
		admin.put(path+".Material",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.CAKE.toString()}));
		admin.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&dEingabe des Zeitraums der Rabattaktion",
						"&dEnter the period of the discount promotion"}));
		admin.put(path+".Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"Zz.: %discountstart% - %discountend%",
						"&cLinksklick &böffnet das Numpad Gui für die Eingabe des Starts.",
						"&cRechtsklick &Böffnet das Numpad Gui für die Eingabe des Endes.",
						"&bDie Eingabe erfolgt Standart gemäß so: yyyy.MM.dd.HH:mm:ss",
						"Atm.: %discountstart% - %discountend%",
						"&cLeft click &open the Numpad Gui for entering the start.",
						"&cRight click &opens the Numpad Gui for entering the end.",
						"&bThe input is done according to the following standard: yyyy.MM.dd.HH:mm:ss"}));
		admin.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_SETDISCOUNT_START_OPEN_NUMPAD.toString()}));
		admin.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_SETDISCOUNT_END_OPEN_NUMPAD.toString()}));
		path = "42"; //Discount Hour
		admin.put(path+".SettingLevel",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						SettingsLevel.MASTER.toString()}));
		admin.put(path+".Material",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.CANDLE.toString()}));
		admin.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&dEingabe des Stundenzeitraums der Rabattaktion",
						"&dEnter the hour period of the discount promotion"}));
		admin.put(path+".Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"Zz.: %discountstart% - %discountend%",
						"&cLinks/Rechtsklick &böffnet das Numpad Gui","&bfür die Stundeneingabe.",
						"&bDie Eingabe erfolgt über eine Zahl,","&bwelche als Stundenwert genommen wird.",
						"&bNach der Eingabe ist dann die Rabattaktion","&bfür die x Stunden aktiv.",
						"Atm.: %discountstart% - %discountend%",
						"&cLeft click &open the Numpad Gui","&bfor entering the hours.",
						"&bThe input is made via a number,","&bwhich is taken as the hourly value.",
						"&bAfter entering, the discount action is then","&bactive for the x hours."}));
		admin.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_SETDISCOUNT_HOUR_OPEN_NUMPAD.toString()}));
		admin.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_SETDISCOUNT_HOUR_OPEN_NUMPAD.toString()}));
		path = "30"; //Discount Possible Buy
		admin.put(path+".CanBuy",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						true}));
		admin.put(path+".SettingLevel",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						SettingsLevel.MASTER.toString()}));
		admin.put(path+".Material",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.LAPIS_ORE.toString()}));
		admin.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&dEingabe der Anzahl der verbleibenen Rabattkäufe",
						"&dEnter the number of discount sales remaining"}));
		admin.put(path+".Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&bZz.: &f%discountpossiblebuy%",
						"&bVerbleibene Käufe, sind die Anzahl an",
						"&bRabattkäufe die noch getätig werden können.",
						"&bIst die Zahl auf 0 können keine Items mehr",
						"&bgekauft werden solange die Rabattaktion läuft.",
						"&cQ &bfür das Zurücksetzen.",
						"&cLinks/Rechtsklick &böffnet das Numpad Gui.",
						
						"&bAtm.: &f%discountpossiblebuy%",
						"&bRemaining sales, are the number of discountsales",
						"&bthat can still be made.",
						"&bIf the number is 0, no more items can be sold as",
						"&blong as the discount promotion is running.",
						"&cQ &bfor resetting.",
						"&cLeft/right click &opens the Numpad Gui."}));
		admin.put(path+".ClickFunction."+ClickType.DROP.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_SETDISCOUNTPOSSIBLE_BUY_CLEAR.toString()}));
		admin.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_SETDISCOUNTPOSSIBLE_BUY_OPEN_NUMPAD.toString()}));
		admin.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_SETDISCOUNTPOSSIBLE_BUY_OPEN_NUMPAD.toString()}));
		path = "32"; //Discount Possible Sell
		admin.put(path+".CanSell",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						true}));
		admin.put(path+".SettingLevel",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						SettingsLevel.MASTER.toString()}));
		admin.put(path+".Material",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.REDSTONE_ORE.toString()}));
		admin.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&dEingabe der Anzahl der verbleibenen Rabattverkäufe",
						"&dEnter the number of discount purchase remaining"}));
		admin.put(path+".Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&bZz.: &f%discountpossiblesell%",
						"&bVerbleibene Verkäufe, sind die Anzahl an",
						"&bVerkäufe die noch getätig werden können.",
						"&bIst die Zahl auf 0 können keine Items mehr",
						"&bverkauft werden solange die Rabattaktion läuft.",
						"&cQ &bfür das Zurücksetzen.",
						"&cLinks/Rechtsklick &böffnet das Numpad Gui.",
						"&bAtm.: &f%discountpossiblesell%",
						"&bRemaining purchase, are the number of purchase",
						"&bthat can still be made.",
						"&bIf the number is 0, no more items can be",
						"&bpurchased as long as the discount campaign is running.",
						"&cQ &bfor resetting.",
						"&cLeft/right click &opens the Numpad Gui."}));
		admin.put(path+".ClickFunction."+ClickType.DROP.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_SETDISCOUNTPOSSIBLE_SELL_CLEAR.toString()}));
		admin.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_SETDISCOUNTPOSSIBLE_SELL_OPEN_NUMPAD.toString()}));
		admin.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_SETDISCOUNTPOSSIBLE_SELL_OPEN_NUMPAD.toString()}));
		path = "52"; //Discount SetBuy
		admin.put(path+".CanBuy",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						true}));
		admin.put(path+".SettingLevel",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						SettingsLevel.MASTER.toString()}));
		admin.put(path+".Material",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.LAPIS_LAZULI.toString()}));
		admin.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&dEingabe des Rabattkaufswertes für 1 Item",
						"&dEnter the discount sale value for 1 item"}));
		admin.put(path+".Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&bZz.: &f%discountbuy1%",
						"&cQ &bfür das Entfernen des Preises.",
						"&cLinks-/Rechtsklick &bzum öffnen des Numpad Gui.",
						"&bAtm.: &f%discountbuy1%",
						"&cQ &bfor removing the price.",
						"&cLinks-/Rechtsklick &bto open the numpad gui."}));
		admin.put(path+".ClickFunction."+ClickType.DROP.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_SETDISCOUNTBUY_CLEAR.toString()}));
		admin.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_SETDISCOUNTBUY_OPEN_NUMPAD.toString()}));
		admin.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_SETDISCOUNTBUY_OPEN_NUMPAD.toString()}));
		path = "53"; //Discount SetSell
		admin.put(path+".CanSell",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						true}));
		admin.put(path+".SettingLevel",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						SettingsLevel.MASTER.toString()}));
		admin.put(path+".Material",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.REDSTONE.toString()}));
		admin.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&dEingabe des Rabattverkaufswertes für 1 Item",
						"&dEnter the discount purchase value for 1 item"}));
		admin.put(path+".Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&bZz.: &f%discountsell1%",
						"&cQ &bfür das Entfernen des Preises.",
						"&cLinks-/Rechtsklick &bzum öffnen des Numpad Gui.",
						
						"&bAtm.: &f%discountsell1%",
						"&cQ &bfor removing the price.",
						"&cLinks-/Rechtsklick &bto open the numpad gui."}));
		admin.put(path+".ClickFunction."+ClickType.DROP.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_SETDISCOUNTSELL_CLEAR.toString()}));
		admin.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_SETDISCOUNTSELL_OPEN_NUMPAD.toString()}));
		admin.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_SETDISCOUNTSELL_OPEN_NUMPAD.toString()}));
		guiKeys.put(GuiType.ADMINISTRATION, admin);
		
		//------------------------------------
		/*admin.put(path+".IsInfoItem",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						true}));
		admin.put(path+".Permission",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						true}));
		admin.put(path+".CanBuy",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						true}));
		admin.put(path+".CanSell",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						true}));
		admin.put(path+".UseASH",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						false}));
		admin.put(path+".SettingLevel",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						SettingsLevel.NOLEVEL.toString()}));
		admin.put(path+".Material",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						}));
		admin.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&d",
						"&d"}));
		admin.put(path+".Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&b",
						"&b",
						"&b",
						"&b"}));
		admin.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						""}));
		admin.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						""}));
		admin.put(path+".ClickFunction."+ClickType.DROP.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						""}));
		admin.put(path+".ClickFunction."+ClickType.SHIFT_LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						""}));
		admin.put(path+".ClickFunction."+ClickType.SHIFT_RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						""}));
		admin.put(path+".ClickFunction."+ClickType.CTRL_DROP.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						""}));
		admin.put(path+".ClickFunction."+ClickType.SWAP.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						""}));*/
	}
	
	public void initGuiNumpad() //INFO:GuiNumpad
	{
		LinkedHashMap<String, Language> numpad = new LinkedHashMap<>();
		Language lNSL = new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				SettingsLevel.NOLEVEL.toString()});
		Language lNMat = new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				Material.PLAYER_HEAD.toString()});
		Language lNLo = new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&bZz.: &f%numtext%",
				"&bFügt am Ende ein Zeichen an.",
				"&bAtm.: &f%numtext%",
				"&bAdds a character at the end."});
		String path = "12"; //7
		numpad.put(path+".SettingLevel", lNSL);
		numpad.put(path+".Material", lNMat);
		numpad.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/9e198fd831cb61f3927f21cf8a7463af5ea3c7e43bd3e8ec7d2948631cce879"}));
		numpad.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&f7 &7Numpad"}));
		numpad.put(path+".Lore", lNLo);
		numpad.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_NUMPAD_7.toString()}));
		numpad.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_NUMPAD_7.toString()}));
		path = "13"; //8
		numpad.put(path+".SettingLevel",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						SettingsLevel.NOLEVEL.toString()}));
		numpad.put(path+".Material",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.PLAYER_HEAD.toString()}));
		numpad.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/84ad12c2f21a1972f3d2f381ed05a6cc088489fcfdf68a713b387482fe91e2"}));
		numpad.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&f8 &7Numpad"}));
		numpad.put(path+".Lore", lNLo);
		numpad.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_NUMPAD_8.toString()}));
		numpad.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_NUMPAD_8.toString()}));
		path = "14"; //9
		numpad.put(path+".SettingLevel", lNSL);
		numpad.put(path+".Material", lNMat);
		numpad.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/9f7aa0d97983cd67dfb67b7d9d9c641bc9aa34d96632f372d26fee19f71f8b7"}));
		numpad.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&f9 &7Numpad"}));
		numpad.put(path+".Lore", lNLo);
		numpad.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_NUMPAD_9.toString()}));
		numpad.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_NUMPAD_9.toString()}));
		path = "21"; //4
		numpad.put(path+".SettingLevel", lNSL);
		numpad.put(path+".Material", lNMat);
		numpad.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/d198d56216156114265973c258f57fc79d246bb65e3c77bbe8312ee35db6"}));
		numpad.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&f4 &7Numpad"}));
		numpad.put(path+".Lore", lNLo);
		numpad.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_NUMPAD_4.toString()}));
		numpad.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_NUMPAD_4.toString()}));
		path = "22"; //5
		numpad.put(path+".SettingLevel", lNSL);
		numpad.put(path+".Material", lNMat);
		numpad.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/7fb91bb97749d6a6eed4449d23aea284dc4de6c3818eea5c7e149ddda6f7c9"}));
		numpad.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&f5 &7Numpad"}));
		numpad.put(path+".Lore", lNLo);
		numpad.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_NUMPAD_5.toString()}));
		numpad.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_NUMPAD_5.toString()}));
		path = "23"; //6
		numpad.put(path+".SettingLevel", lNSL);
		numpad.put(path+".Material", lNMat);
		numpad.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/9c613f80a554918c7ab2cd4a278752f151412a44a73d7a286d61d45be4eaae1"}));
		numpad.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&f6 &7Numpad"}));
		numpad.put(path+".Lore", lNLo);
		numpad.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_NUMPAD_6.toString()}));
		numpad.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_NUMPAD_6.toString()}));
		path = "30"; //1
		numpad.put(path+".SettingLevel", lNSL);
		numpad.put(path+".Material", lNMat);
		numpad.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/d2a6f0e84daefc8b21aa99415b16ed5fdaa6d8dc0c3cd591f49ca832b575"}));
		numpad.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&f1 &7Numpad"}));
		numpad.put(path+".Lore", lNLo);
		numpad.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_NUMPAD_1.toString()}));
		numpad.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_NUMPAD_1.toString()}));
		path = "31"; //2
		numpad.put(path+".SettingLevel", lNSL);
		numpad.put(path+".Material", lNMat);
		numpad.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/96fab991d083993cb83e4bcf44a0b6cefac647d4189ee9cb823e9cc1571e38"}));
		numpad.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&f2 &7Numpad"}));
		numpad.put(path+".Lore", lNLo);
		numpad.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_NUMPAD_2.toString()}));
		numpad.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_NUMPAD_2.toString()}));
		path = "32"; //3
		numpad.put(path+".SettingLevel", lNSL);
		numpad.put(path+".Material", lNMat);
		numpad.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/cd319b9343f17a35636bcbc26b819625a9333de3736111f2e932827c8e749"}));
		numpad.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&f3 &7Numpad"}));
		numpad.put(path+".Lore", lNLo);
		numpad.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_NUMPAD_3.toString()}));
		numpad.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_NUMPAD_3.toString()}));
		path = "40"; //0
		numpad.put(path+".SettingLevel", lNSL);
		numpad.put(path+".Material", lNMat);
		numpad.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/6d68343bd0b129de93cc8d3bba3b97a2faa7ade38d8a6e2b864cd868cfab"}));
		numpad.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&f0 &7Numpad"}));
		numpad.put(path+".Lore", lNLo);
		numpad.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_NUMPAD_0.toString()}));
		numpad.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_NUMPAD_0.toString()}));
		path = "38"; //.
		numpad.put(path+".SettingLevel", lNSL);
		numpad.put(path+".Material", lNMat);
		numpad.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/95f6e3383d128f17d73cf39af7b579889779c4e5f38d2c1ef85dba2f462f6840"}));
		numpad.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&f. &7Numpad"}));
		numpad.put(path+".Lore", lNLo);
		numpad.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_NUMPAD_DECIMAL.toString()}));
		numpad.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_NUMPAD_DECIMAL.toString()}));
		path = "42"; //:
		numpad.put(path+".SettingLevel", lNSL);
		numpad.put(path+".Material", lNMat);
		numpad.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/ac4c26963f8538c11eac6a8e437d27682dffea4bcc0f04afd5cda6d1d567"}));
		numpad.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&f: &7Numpad"}));
		numpad.put(path+".Lore", lNLo);
		numpad.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_NUMPAD_COLON.toString()}));
		numpad.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_NUMPAD_COLON.toString()}));
		path = "48"; //RemoveOnce
		numpad.put(path+".SettingLevel", lNSL);
		numpad.put(path+".Material", lNMat);
		numpad.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/864f779a8e3ffa231143fa69b96b14ee35c16d669e19c75fd1a7da4bf306c"}));
		numpad.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eUm ein Zeichen zurücksetzen",
						"&eReset by one character"}));
		numpad.put(path+".Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&bZz.: &f%numtext%",
						"&bSetzt die Eingabe um ein Zeichen zurück.",
						"&bAtm.: &f%numtext%",
						"&bResets the input by one character."}));
		numpad.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_NUMPAD_REMOVEONCE.toString()}));
		numpad.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_NUMPAD_REMOVEONCE.toString()}));
		path = "50"; //Clear
		numpad.put(path+".SettingLevel", lNSL);
		numpad.put(path+".Material", lNMat);
		numpad.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/118a2dd5bef0b073b13271a7eeb9cfea7afe8593c57a93821e43175572461812"}));
		numpad.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eZurücksetzen",
						"&eReset"}));
		numpad.put(path+".Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&bZz.: &f%numtext%",
						"&bSetzt die Eingabe zurück.",
						"&bAtm.: &f%numtext%",
						"&bResets the input."}));
		numpad.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_NUMPAD_CLEAR.toString()}));
		numpad.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_NUMPAD_CLEAR.toString()}));
		path = "45"; //Cancel
		numpad.put(path+".SettingLevel",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						SettingsLevel.NOLEVEL.toString()}));
		numpad.put(path+".Material",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.RED_BANNER.toString()}));
		numpad.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cZurück zum Administrations-Gui",
						"&cBack to the administration gui"}));
		numpad.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_NUMPAD_CANCEL.toString()}));
		numpad.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_NUMPAD_CANCEL.toString()}));
		LinkedHashMap<String, Language> numpad_ACCOUNT = new LinkedHashMap<>();
		numpad_ACCOUNT.putAll(numpad);
		LinkedHashMap<String, Language> numpad_BUY = new LinkedHashMap<>();
		numpad_BUY.putAll(numpad);
		LinkedHashMap<String, Language> numpad_SELL = new LinkedHashMap<>();
		numpad_SELL.putAll(numpad);
		LinkedHashMap<String, Language> numpad_POSSIBLEBUY = new LinkedHashMap<>();
		numpad_POSSIBLEBUY.putAll(numpad);
		LinkedHashMap<String, Language> numpad_POSSIBLESELL = new LinkedHashMap<>();
		numpad_POSSIBLESELL.putAll(numpad);
		LinkedHashMap<String, Language> numpad_DISCOUNTSTART = new LinkedHashMap<>();
		numpad_DISCOUNTSTART.putAll(numpad);
		LinkedHashMap<String, Language> numpad_DISCOUNTEND = new LinkedHashMap<>();
		numpad_DISCOUNTEND.putAll(numpad);
		LinkedHashMap<String, Language> numpad_DISCOUNTHOUR = new LinkedHashMap<>();
		numpad_DISCOUNTHOUR.putAll(numpad);
		LinkedHashMap<String, Language> numpad_DISCOUNTBUY = new LinkedHashMap<>();
		numpad_DISCOUNTBUY.putAll(numpad);
		LinkedHashMap<String, Language> numpad_DISCOUNTSELL = new LinkedHashMap<>();
		numpad_DISCOUNTSELL.putAll(numpad);
		LinkedHashMap<String, Language> numpad_DISCOUNTPOSSIBLEBUY = new LinkedHashMap<>();
		numpad_DISCOUNTPOSSIBLEBUY.putAll(numpad);
		LinkedHashMap<String, Language> numpad_DISCOUNTPOSSIBLESELL = new LinkedHashMap<>();
		numpad_DISCOUNTPOSSIBLESELL.putAll(numpad);
		path = "53"; //TakeOver
		String sSL = path+".SettingLevel";
		Language lSL = new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				SettingsLevel.NOLEVEL.toString()});
		String sMat = path+".Material";
		Language lMat = new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				Material.GREEN_BANNER.toString()});
		String sDN = path+".Displayname";
		Language lDN = new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&aÜbernahme der Eingabe",
				"&cAcceptance of the input"});
		String sLo = path+".Lore";
		Language lLo = new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&bZz.: &f%numtext%", "&bÜbernimmt die Eingabe und kehre zum Administration Gui zurück.",
				"&bAtm.: &f%numtext%", "&bAccept the input and return to the Administration Gui."});
		String sLC = path+".ClickFunction."+ClickType.LEFT.toString();
		Language lLC = new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				ClickFunctionType.ADMINISTRATION_SETACCOUNT_TAKEOVER.toString()});
		String sRC = path+".ClickFunction."+ClickType.RIGHT.toString();
		Language lRC = new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				ClickFunctionType.ADMINISTRATION_SETACCOUNT_TAKEOVER.toString()});
		String sSLC = path+".ClickFunction."+ClickType.SHIFT_LEFT.toString();
		Language lSLC = new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				ClickFunctionType.ADMINISTRATION_SETACCOUNT_TAKEOVER.toString()});
		String sSRC = path+".ClickFunction."+ClickType.SHIFT_RIGHT.toString();
		Language lSRC = new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				ClickFunctionType.ADMINISTRATION_SETACCOUNT_TAKEOVER.toString()});
		numpad_ACCOUNT.put(sSL, lSL);
		numpad_ACCOUNT.put(sMat, lMat);
		numpad_ACCOUNT.put(sDN, lDN);
		numpad_ACCOUNT.put(sLo, lLo);
		numpad_ACCOUNT.put(sLC, lLC);
		numpad_ACCOUNT.put(sRC, lRC);
		guiKeys.put(GuiType.NUMPAD_ACCOUNT, numpad_ACCOUNT);
		//-------------
		lLC = new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				ClickFunctionType.ADMINISTRATION_SETBUY_TAKEOVER.toString()});
		lRC = new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				ClickFunctionType.ADMINISTRATION_SETBUY_TAKEOVER.toString()});
		numpad_BUY.put(sSL, lSL);
		numpad_BUY.put(sMat, lMat);
		numpad_BUY.put(sDN, lDN);
		numpad_BUY.put(sLo, lLo);
		numpad_BUY.put(sLC, lLC);
		numpad_BUY.put(sRC, lRC);
		guiKeys.put(GuiType.NUMPAD_BUY, numpad_BUY);
		lLC = new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				ClickFunctionType.ADMINISTRATION_SETSELL_TAKEOVER.toString()});
		lRC = new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				ClickFunctionType.ADMINISTRATION_SETSELL_TAKEOVER.toString()});
		numpad_SELL.put(sSL, lSL);
		numpad_SELL.put(sMat, lMat);
		numpad_SELL.put(sDN, lDN);
		numpad_SELL.put(sLo, lLo);
		numpad_SELL.put(sLC, lLC);
		numpad_SELL.put(sRC, lRC);
		guiKeys.put(GuiType.NUMPAD_SELL, numpad_SELL);
		//-------------
		lLC = new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				ClickFunctionType.ADMINISTRATION_SETPOSSIBLE_BUY_TAKEOVER.toString()});
		lRC = new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				ClickFunctionType.ADMINISTRATION_SETPOSSIBLE_BUY_TAKEOVER.toString()});
		numpad_POSSIBLEBUY.put(sSL, lSL);
		numpad_POSSIBLEBUY.put(sMat, lMat);
		numpad_POSSIBLEBUY.put(sDN, lDN);
		numpad_POSSIBLEBUY.put(sLo, lLo);
		numpad_POSSIBLEBUY.put(sLC, lLC);
		numpad_POSSIBLEBUY.put(sRC, lRC);
		guiKeys.put(GuiType.NUMPAD_POSSIBLE_BUY, numpad_POSSIBLEBUY);
		lLC = new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				ClickFunctionType.ADMINISTRATION_SETPOSSIBLE_SELL_TAKEOVER.toString()});
		lRC = new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				ClickFunctionType.ADMINISTRATION_SETPOSSIBLE_SELL_TAKEOVER.toString()});
		numpad_POSSIBLESELL.put(sSL, lSL);
		numpad_POSSIBLESELL.put(sMat, lMat);
		numpad_POSSIBLESELL.put(sDN, lDN);
		numpad_POSSIBLESELL.put(sLo, lLo);
		numpad_POSSIBLESELL.put(sLC, lLC);
		numpad_POSSIBLESELL.put(sRC, lRC);
		guiKeys.put(GuiType.NUMPAD_POSSIBLE_SELL, numpad_POSSIBLESELL);
		//--------------
		lLC = new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				ClickFunctionType.ADMINISTRATION_SETDISCOUNT_START_TAKEOVER.toString()});
		lRC = new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				ClickFunctionType.ADMINISTRATION_SETDISCOUNT_START_TAKEOVER.toString()});
		lSLC = new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				ClickFunctionType.ADMINISTRATION_SETDISCOUNT_START_WORLD_TAKEOVER.toString()});
		lSRC = new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				ClickFunctionType.ADMINISTRATION_SETDISCOUNT_START_WORLD_TAKEOVER.toString()});
		lLo = new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&bZz.: &f%numtext%",
				"&cLinks/Rechtsklick &bübernimmt die Eingabe für diesen Shop.",
				"&cShift L./R. Klick &bübernimmt die Eingabe für alle Shop dieser Welt.",
				"&bAtm.: &f%numtext%",
				"&cShift L./R. Klick &baccept the input for this shop.",
				"&cShift L./R. click &baccept the input for all shop of this world."});
		numpad_DISCOUNTSTART.put(sSL, lSL);
		numpad_DISCOUNTSTART.put(sMat, lMat);
		numpad_DISCOUNTSTART.put(sDN, lDN);
		numpad_DISCOUNTSTART.put(sLo, lLo);
		numpad_DISCOUNTSTART.put(sLC, lLC);
		numpad_DISCOUNTSTART.put(sRC, lRC);
		numpad_DISCOUNTSTART.put(sSLC, lSLC);
		numpad_DISCOUNTSTART.put(sSRC, lSRC);
		guiKeys.put(GuiType.NUMPAD_DISCOUNT_START, numpad_DISCOUNTSTART);
		lLC = new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				ClickFunctionType.ADMINISTRATION_SETDISCOUNT_END_TAKEOVER.toString()});
		lRC = new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				ClickFunctionType.ADMINISTRATION_SETDISCOUNT_END_TAKEOVER.toString()});
		lSLC = new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				ClickFunctionType.ADMINISTRATION_SETDISCOUNT_END_WORLD_TAKEOVER.toString()});
		lSRC = new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				ClickFunctionType.ADMINISTRATION_SETDISCOUNT_END_WORLD_TAKEOVER.toString()});
		lLo = new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&bZz.: &f%numtext%",
				"&cLinks/Rechtsklick &bübernimmt die Eingabe für diesen Shop.",
				"&cShift L./R. Klick &bübernimmt die Eingabe für alle Shop dieser Welt.",
				"&bAtm.: &f%numtext%",
				"&cShift L./R. Klick &baccept the input for this shop.",
				"&cShift L./R. click &baccept the input for all shop of this world."});
		numpad_DISCOUNTEND.put(sSL, lSL);
		numpad_DISCOUNTEND.put(sMat, lMat);
		numpad_DISCOUNTEND.put(sDN, lDN);
		numpad_DISCOUNTEND.put(sLo, lLo);
		numpad_DISCOUNTEND.put(sLC, lLC);
		numpad_DISCOUNTEND.put(sRC, lRC);
		numpad_DISCOUNTEND.put(sSLC, lLC);
		numpad_DISCOUNTEND.put(sSRC, lRC);
		guiKeys.put(GuiType.NUMPAD_DISCOUNT_END, numpad_DISCOUNTEND);
		lLC = new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				ClickFunctionType.ADMINISTRATION_SETDISCOUNT_HOUR_TAKEOVER.toString()});
		lRC = new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				ClickFunctionType.ADMINISTRATION_SETDISCOUNT_HOUR_TAKEOVER.toString()});
		lSLC = new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				ClickFunctionType.ADMINISTRATION_SETDISCOUNT_HOUR_WORLD_TAKEOVER.toString()});
		lSRC = new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				ClickFunctionType.ADMINISTRATION_SETDISCOUNT_HOUR_WORLD_TAKEOVER.toString()});
		lLo = new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&bZz.: &f%numtext% &bStunden",
				"&cLinks/Rechtsklick &bübernimmt die Eingabe für diesen Shop.",
				"&cShift L./R. Klick &bübernimmt die Eingabe für alle Shop dieser Welt.",
				"&bAtm.: &f%numtext% &bHours",
				"&cShift L./R. Klick &baccept the input for this shop.",
				"&cShift L./R. click &baccept the input for all shop of this world."});
		numpad_DISCOUNTHOUR.put(sSL, lSL);
		numpad_DISCOUNTHOUR.put(sMat, lMat);
		numpad_DISCOUNTHOUR.put(sDN, lDN);
		numpad_DISCOUNTHOUR.put(sLo, lLo);
		numpad_DISCOUNTHOUR.put(sLC, lLC);
		numpad_DISCOUNTHOUR.put(sRC, lRC);
		numpad_DISCOUNTHOUR.put(sSLC, lSLC);
		numpad_DISCOUNTHOUR.put(sSRC, lSRC);
		guiKeys.put(GuiType.NUMPAD_DISCOUNT_HOUR, numpad_DISCOUNTHOUR);
		//----------------
		lLC = new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				ClickFunctionType.ADMINISTRATION_SETDISCOUNTBUY_TAKEOVER.toString()});
		lRC = new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				ClickFunctionType.ADMINISTRATION_SETDISCOUNTBUY_TAKEOVER.toString()});
		numpad_DISCOUNTBUY.put(sSL, lSL);
		numpad_DISCOUNTBUY.put(sMat, lMat);
		numpad_DISCOUNTBUY.put(sDN, lDN);
		numpad_DISCOUNTBUY.put(sLo, lLo);
		numpad_DISCOUNTBUY.put(sLC, lLC);
		numpad_DISCOUNTBUY.put(sRC, lRC);
		guiKeys.put(GuiType.NUMPAD_DISCOUNT_BUY, numpad_DISCOUNTBUY);
		lLC = new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				ClickFunctionType.ADMINISTRATION_SETDISCOUNTSELL_TAKEOVER.toString()});
		lRC = new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				ClickFunctionType.ADMINISTRATION_SETDISCOUNTSELL_TAKEOVER.toString()});
		numpad_DISCOUNTSELL.put(sSL, lSL);
		numpad_DISCOUNTSELL.put(sMat, lMat);
		numpad_DISCOUNTSELL.put(sDN, lDN);
		numpad_DISCOUNTSELL.put(sLo, lLo);
		numpad_DISCOUNTSELL.put(sLC, lLC);
		numpad_DISCOUNTSELL.put(sRC, lRC);
		guiKeys.put(GuiType.NUMPAD_DISCOUNT_SELL, numpad_DISCOUNTSELL);
		//----------------
		lLC = new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				ClickFunctionType.ADMINISTRATION_SETDISCOUNTPOSSIBLE_BUY_TAKEOVER.toString()});
		lRC = new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				ClickFunctionType.ADMINISTRATION_SETDISCOUNTPOSSIBLE_BUY_TAKEOVER.toString()});
		numpad_DISCOUNTPOSSIBLEBUY.put(sSL, lSL);
		numpad_DISCOUNTPOSSIBLEBUY.put(sMat, lMat);
		numpad_DISCOUNTPOSSIBLEBUY.put(sDN, lDN);
		numpad_DISCOUNTPOSSIBLEBUY.put(sLo, lLo);
		numpad_DISCOUNTPOSSIBLEBUY.put(sLC, lLC);
		numpad_DISCOUNTPOSSIBLEBUY.put(sRC, lRC);
		guiKeys.put(GuiType.NUMPAD_DISCOUNT_POSSIBLE_BUY, numpad_DISCOUNTPOSSIBLEBUY);
		lLC = new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				ClickFunctionType.ADMINISTRATION_SETDISCOUNTPOSSIBLE_SELL_TAKEOVER.toString()});
		lRC = new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				ClickFunctionType.ADMINISTRATION_SETDISCOUNTPOSSIBLE_SELL_TAKEOVER.toString()});
		numpad_DISCOUNTPOSSIBLESELL.put(sSL, lSL);
		numpad_DISCOUNTPOSSIBLESELL.put(sMat, lMat);
		numpad_DISCOUNTPOSSIBLESELL.put(sDN, lDN);
		numpad_DISCOUNTPOSSIBLESELL.put(sLo, lLo);
		numpad_DISCOUNTPOSSIBLESELL.put(sLC, lLC);
		numpad_DISCOUNTPOSSIBLESELL.put(sRC, lRC);
		guiKeys.put(GuiType.NUMPAD_DISCOUNT_POSSIBLE_SELL, numpad_DISCOUNTPOSSIBLESELL);
	}
	
	public void initGuiShop() //INFO:GuiShop
	{
		LinkedHashMap<String, Language> shop = new LinkedHashMap<>();
		String path = "4"; //InfoItem, wie es ist
		shop.put(path+".IsInfoItem",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						true}));
		path = "13"; //InfoButton, welcher alle Daten des Items anzeigt
		shop.put(path+".SettingLevel",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						SettingsLevel.NOLEVEL.toString()}));
		shop.put(path+".Material",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.PAPER.toString()}));
		shop.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&dHier folgt einer Auflistung aller Eigenschaften des Items",
						"&dHere follows a list of all properties of the item"}));
		shop.put(path+".InfoLore",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						true}));
		path = "8"; //Subscribe
		shop.put(path+".SettingLevel",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						SettingsLevel.NOLEVEL.toString()}));
		shop.put(path+".Material",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.LECTERN.toString()}));
		shop.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&dAbonniere den Shop!",
						"&dSubscribe to the store!"}));
		shop.put(path+".Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&bZz.: &r%subscribe%",
						"&bAbonniere oder deabonniere hier den Shop!",
						"&bAtm.: &r%subscribe%",
						"&bSubscribe or unsubscribe to the store here!"}));
		shop.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.SHOP_TOGGLE_SUBSCRIBE.toString()}));
		shop.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.SHOP_TOGGLE_SUBSCRIBE.toString()}));
		path = "36"; //Buy1
		shop.put(path+".CanBuy",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						true}));
		shop.put(path+".SettingLevel",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						SettingsLevel.NOLEVEL.toString()}));
		shop.put(path+".Material",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.SMALL_AMETHYST_BUD.toString()}));
		shop.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eKaufe x1 Item",
						"&eBuy x1 Item"}));
		shop.put(path+".Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&fPreis: &r%buy1%",
						"&bKaufe x1 Items",
						"&boder soviele noch im Inventar sind",
						"&boder der Shop noch vorrätig hat.",
						"&fPrice: &r%buy1%",
						"&bBuy x1 items",
						"&bor as many are still in inventory",
						"&bor the shop still in stock."}));
		shop.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.SHOP_BUY_1.toString()}));
		shop.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.SHOP_BUY_1.toString()}));
		path = "37"; //Buy8
		shop.put(path+".CanBuy",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						true}));
		shop.put(path+".SettingLevel",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						SettingsLevel.NOLEVEL.toString()}));
		shop.put(path+".Material",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.MEDIUM_AMETHYST_BUD.toString()}));
		shop.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eKaufe x8 Item",
						"&eBuy x8 Item"}));
		shop.put(path+".Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&fPreis: &r%buy8%",
						"&bKaufe x8 Items",
						"&boder soviele noch im Inventar sind",
						"&boder der Shop noch vorrätig hat.",
						"&fPrice: &r%buy8%",
						"&bBuy x8 items",
						"&bor as many are still in inventory",
						"&bor the shop still in stock."}));
		shop.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.SHOP_BUY_8.toString()}));
		shop.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.SHOP_BUY_8.toString()}));
		path = "38"; //Buy16
		shop.put(path+".CanBuy",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						true}));
		shop.put(path+".SettingLevel",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						SettingsLevel.NOLEVEL.toString()}));
		shop.put(path+".Material",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.LARGE_AMETHYST_BUD.toString()}));
		shop.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eKaufe x16 Item",
						"&eBuy x16 Item"}));
		shop.put(path+".Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&fPreis: &r%buy16%",
						"&bKaufe x16 Items",
						"&boder soviele noch im Inventar sind",
						"&boder der Shop noch vorrätig hat.",
						"&fPrice: &r%buy16%",
						"&bBuy x16 items",
						"&bor as many are still in inventory",
						"&bor the shop still in stock."}));
		shop.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.SHOP_BUY_16.toString()}));
		shop.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.SHOP_BUY_16.toString()}));
		path = "39"; //Buy32
		shop.put(path+".CanBuy",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						true}));
		shop.put(path+".SettingLevel",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						SettingsLevel.NOLEVEL.toString()}));
		shop.put(path+".Material",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.AMETHYST_CLUSTER.toString()}));
		shop.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eKaufe x32 Item",
						"&eBuy x32 Item"}));
		shop.put(path+".Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&fPreis: &r%buy32%",
						"&bKaufe x32 Items",
						"&boder soviele noch im Inventar sind",
						"&boder der Shop noch vorrätig hat.",
						"&fPrice: &r%buy32%",
						"&bBuy x32 items",
						"&bor as many are still in inventory",
						"&bor the shop still in stock."}));
		shop.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.SHOP_BUY_32.toString()}));
		shop.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.SHOP_BUY_32.toString()}));
		path = "45"; //Buy64
		shop.put(path+".CanBuy",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						true}));
		shop.put(path+".SettingLevel",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						SettingsLevel.NOLEVEL.toString()}));
		shop.put(path+".Material",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.RAW_GOLD.toString()}));
		shop.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eKaufe x64 Item",
						"&eBuy x64 Item"}));
		shop.put(path+".Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&fPreis: &r%buy64%",
						"&bKaufe x64 Items",
						"&boder soviele noch im Inventar sind",
						"&boder der Shop noch vorrätig hat.",
						"&fPrice: &r%buy64%",
						"&bBuy x64 items",
						"&bor as many are still in inventory",
						"&bor the shop still in stock."}));
		shop.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.SHOP_BUY_64.toString()}));
		shop.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.SHOP_BUY_64.toString()}));
		path = "46"; //Buy576
		shop.put(path+".CanBuy",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						true}));
		shop.put(path+".SettingLevel",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						SettingsLevel.NOLEVEL.toString()}));
		shop.put(path+".Material",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.GOLD_NUGGET.toString()}));
		shop.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eKaufe x576 Item",
						"&eBuy x576 Item"}));
		shop.put(path+".Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&fPreis: &r%buy576%",
						"&bKaufe x576 Items (9x Stack)",
						"&boder soviele noch im Inventar sind",
						"&boder der Shop noch vorrätig hat.",
						"&fPrice: &r%buy576%",
						"&bBuy x576 items (9x Stack)",
						"&bor as many are still in inventory",
						"&bor the shop still in stock."}));
		shop.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.SHOP_BUY_576.toString()}));
		shop.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.SHOP_BUY_576.toString()}));
		path = "47"; //Buy1728
		shop.put(path+".CanBuy",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						true}));
		shop.put(path+".SettingLevel",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						SettingsLevel.NOLEVEL.toString()}));
		shop.put(path+".Material",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.GOLD_INGOT.toString()}));
		shop.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eKaufe x1728 Item",
						"&eBuy x1728 Item"}));
		shop.put(path+".Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&fPreis: &r%buy1728%",
						"&bKaufe x1728 Items (27x Stack)",
						"&boder soviele noch im Inventar sind",
						"&boder der Shop noch vorrätig hat.",
						"&fPrice: &r%buy1728%",
						"&bBuy x1728 items (27x Stack)",
						"&bor as many are still in inventory",
						"&bor the shop still in stock."}));
		shop.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.SHOP_BUY_1728.toString()}));
		shop.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.SHOP_BUY_1728.toString()}));
		path = "48"; //Buy2304
		shop.put(path+".CanBuy",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						true}));
		shop.put(path+".SettingLevel",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						SettingsLevel.NOLEVEL.toString()}));
		shop.put(path+".Material",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.GOLD_BLOCK.toString()}));
		shop.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eKaufe x2304 Item",
						"&eBuy x2304 Item"}));
		shop.put(path+".Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&fPreis: &r%buy2304%",
						"&bKaufe x2304 Items (36x Stack)",
						"&boder soviele noch im Inventar sind",
						"&boder der Shop noch vorrätig hat.",
						"&fPrice: &r%buy2304%",
						"&bBuy x2304 items (36x Stack)",
						"&bor as many are still in inventory",
						"&bor the shop still in stock."}));
		shop.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.SHOP_BUY_2304.toString()}));
		shop.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.SHOP_BUY_2304.toString()}));
		path = "44"; //Sell1
		shop.put(path+".CanSell",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						true}));
		shop.put(path+".SettingLevel",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						SettingsLevel.NOLEVEL.toString()}));
		shop.put(path+".Material",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.SMALL_AMETHYST_BUD.toString()}));
		shop.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eVerkaufe x1 Item",
						"&eSell x1 Item"}));
		shop.put(path+".Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&fPreis: &r%sell1%",
						"&bVerkaufe x1 Items",
						"&boder soviele noch im Inventar sind",
						"&boder der Shop annimmt.",
						"&fPrice: &r%sell1%",
						"&bSell x1 items",
						"&bor as many are still in inventory",
						"&bor the store accepts."}));
		shop.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.SHOP_SELL_1.toString()}));
		shop.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.SHOP_SELL_1.toString()}));
		path = "43"; //Sell8
		shop.put(path+".CanSell",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						true}));
		shop.put(path+".SettingLevel",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						SettingsLevel.NOLEVEL.toString()}));
		shop.put(path+".Material",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.MEDIUM_AMETHYST_BUD.toString()}));
		shop.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eVerkaufe x8 Item",
						"&eSell x8 Item"}));
		shop.put(path+".Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&fPreis: &r%sell8%",
						"&bVerkaufe x8 Items",
						"&boder soviele noch im Inventar sind",
						"&boder der Shop annimmt.",
						"&fPrice: &r%sell8%",
						"&bSell x8 items",
						"&bor as many are still in inventory",
						"&bor the store accepts."}));
		shop.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.SHOP_SELL_8.toString()}));
		shop.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.SHOP_SELL_8.toString()}));
		path = "42"; //Sell16
		shop.put(path+".CanSell",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						true}));
		shop.put(path+".SettingLevel",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						SettingsLevel.NOLEVEL.toString()}));
		shop.put(path+".Material",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.LARGE_AMETHYST_BUD.toString()}));
		shop.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eVerkaufe x16 Item",
						"&eSell x16 Item"}));
		shop.put(path+".Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&fPreis: &r%sell16%",
						"&bVerkaufe x16 Items",
						"&boder soviele noch im Inventar sind",
						"&boder der Shop annimmt.",
						"&fPrice: &r%sell16%",
						"&bSell x16 items",
						"&bor as many are still in inventory",
						"&bor the store accepts."}));
		shop.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.SHOP_SELL_16.toString()}));
		shop.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.SHOP_SELL_16.toString()}));
		path = "41"; //Sell32
		shop.put(path+".CanSell",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						true}));
		shop.put(path+".SettingLevel",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						SettingsLevel.NOLEVEL.toString()}));
		shop.put(path+".Material",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.AMETHYST_CLUSTER.toString()}));
		shop.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eVerkaufe x32 Item",
						"&eSell x32 Item"}));
		shop.put(path+".Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&fPreis: &r%sell32%",
						"&bVerkaufe x32 Items",
						"&boder soviele noch im Inventar sind",
						"&boder der Shop annimmt.",
						"&fPrice: &r%sell32%",
						"&bSell x32 items",
						"&bor as many are still in inventory",
						"&bor the store accepts."}));
		shop.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.SHOP_SELL_32.toString()}));
		shop.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.SHOP_SELL_32.toString()}));
		path = "53"; //Sell64
		shop.put(path+".CanSell",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						true}));
		shop.put(path+".SettingLevel",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						SettingsLevel.NOLEVEL.toString()}));
		shop.put(path+".Material",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.RAW_IRON.toString()}));
		shop.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eVerkaufe x64 Item",
						"&eSell x64 Item"}));
		shop.put(path+".Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&fPreis: &r%sell64%",
						"&bVerkaufe x64 Items",
						"&boder soviele noch im Inventar sind",
						"&boder der Shop annimmt.",
						"&fPrice: &r%sell64%",
						"&bSell x64 items",
						"&bor as many are still in inventory",
						"&bor the store accepts."}));
		shop.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.SHOP_SELL_64.toString()}));
		shop.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.SHOP_SELL_64.toString()}));
		path = "52"; //Sell576
		shop.put(path+".CanSell",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						true}));
		shop.put(path+".SettingLevel",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						SettingsLevel.NOLEVEL.toString()}));
		shop.put(path+".Material",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.IRON_NUGGET.toString()}));
		shop.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eVerkaufe x576 Item",
						"&eSell x576 Item"}));
		shop.put(path+".Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&fPreis: &r%sell576%",
						"&bVerkaufe x576 Items (9x Stack)",
						"&boder soviele noch im Inventar sind",
						"&boder der Shop annimmt.",
						"&fPrice: &r%sell576%",
						"&bSell x576 items (9x Stack)",
						"&bor as many are still in inventory",
						"&bor the store accepts."}));
		shop.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.SHOP_SELL_576.toString()}));
		shop.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.SHOP_SELL_576.toString()}));
		path = "51"; //Sell1728
		shop.put(path+".CanSell",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						true}));
		shop.put(path+".SettingLevel",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						SettingsLevel.NOLEVEL.toString()}));
		shop.put(path+".Material",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.IRON_INGOT.toString()}));
		shop.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eVerkaufe x1728 Item",
						"&eSell x1728 Item"}));
		shop.put(path+".Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&fPreis: &r%sell1728%",
						"&bVerkaufe x1728 Items (27x Stack)",
						"&boder soviele noch im Inventar sind",
						"&boder der Shop annimmt.",
						"&fPrice: &r%sell1728%",
						"&bSell x1728 items (27x Stack)",
						"&bor as many are still in inventory",
						"&bor the store accepts."}));
		shop.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.SHOP_SELL_1728.toString()}));
		shop.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.SHOP_SELL_1728.toString()}));
		path = "50"; //Sell2304
		shop.put(path+".CanSell",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						true}));
		shop.put(path+".SettingLevel",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						SettingsLevel.NOLEVEL.toString()}));
		shop.put(path+".Material",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.IRON_BLOCK.toString()}));
		shop.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eVerkaufe x2304 Item",
						"&eSell x2304 Item"}));
		shop.put(path+".Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&fPreis: &r%sell2304%",
						"&bVerkaufe x2304 Items (36x Stack)",
						"&boder soviele noch im Inventar sind",
						"&boder der Shop annimmt.",
						"&fPrice: &r%sell2304%",
						"&bSell x2304 items (36x Stack)",
						"&bor as many are still in inventory",
						"&bor the store accepts."}));
		shop.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.SHOP_SELL_2304.toString()}));
		shop.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.SHOP_SELL_2304.toString()}));
		path = "31"; //DiscountInfo
		/*shop.put(path+".CanBuy",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						true}));
		shop.put(path+".CanSell",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						true}));
		shop.put(path+".SettingLevel",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						SettingsLevel.NOLEVEL.toString()}));
		shop.put(path+".Material",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						}));
		shop.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&d",
						"&d"}));
		shop.put(path+".Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&b",
						"&b",
						"&b",
						"&b"}));
		shop.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.SHOP_BUY_1.toString()}));
		shop.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.SHOP_BUY_1.toString()}));*/
		guiKeys.put(GuiType.SHOP, shop);
	}
	
	public void initGuiItemInput() //INFO:GuiItemInput
	{
		LinkedHashMap<String, Language> iinput = new LinkedHashMap<>();
		String path = "22"; //Information
		iinput.put(path+".SettingLevel",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						SettingsLevel.NOLEVEL.toString()}));
		iinput.put(path+".Material",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.PAPER.toString()}));
		iinput.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&dKlicke auf ein Item zum Hinzufügen des Shops.",
						"&dClick on an item to add the store."}));
		guiKeys.put(GuiType.ITEM_INPUT, iinput);
	}
	
	public void initGuiKeyboard() //INFO:GuiKeyBoard
	{
		LinkedHashMap<String, Language> keyboard = new LinkedHashMap<>();
		Language lBSL = new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				SettingsLevel.NOLEVEL.toString()});
		Language lBMat = new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				Material.PLAYER_HEAD.toString()});
		Language lBLo = new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&bZz.: &f%numtext%",
				"&bFügt am Ende ein Zeichen an.",
				"&bAtm.: &f%numtext%",
				"&bAdds a character at the end."});
		Language lB2Lo = new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&bZz.: &f%numtext%",
				"&cLinksklick &bfügt den Kleinbuchstabe am Ende hinzu.",
				"&cRechtsklick &bfügt den Großbuchstabe am Ende hinzu.",
				"&bAtm.: &f%numtext%",
				"&cLeftclick &badds the lowercase letter at the end.",
				"&cRightclick &badds the capital letter at the end."});
		String path = ""; //7
		path = "18"; //Q
		keyboard.put(path+".SettingLevel", lBSL);
		keyboard.put(path+".Material", lBMat);
		keyboard.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/ff72cceb4a565478de5b0b0e727946e549834e36f6e0ec8f7dd7f6327b15a"}));
		keyboard.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&fq/Q &7Numpad"}));
		keyboard.put(path+".Lore", lB2Lo);
		keyboard.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_Q_SMALL.toString()}));
		keyboard.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_Q_CAPITAL.toString()}));
		path = "19"; //W
		keyboard.put(path+".SettingLevel", lBSL);
		keyboard.put(path+".Material", lBMat);
		keyboard.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/79cbc465525e16a89441d789b72f554e8ff4ea5b393447aef3ff193f0465058"}));
		keyboard.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&fw/W &7Numpad"}));
		keyboard.put(path+".Lore", lB2Lo);
		keyboard.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_W_SMALL.toString()}));
		keyboard.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_W_CAPITAL.toString()}));
		path = "20"; //E
		keyboard.put(path+".SettingLevel", lBSL);
		keyboard.put(path+".Material", lBMat);
		keyboard.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/ced9f431a997fce0d8be1844f62090b1783ac569c9d2797528349d37c215fcc"}));
		keyboard.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&fe/E &7Numpad"}));
		keyboard.put(path+".Lore", lB2Lo);
		keyboard.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_E_SMALL.toString()}));
		keyboard.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_E_CAPITAL.toString()}));
		path = "21"; //R
		keyboard.put(path+".SettingLevel", lBSL);
		keyboard.put(path+".Material", lBMat);
		keyboard.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/3cb88225ee4ab39f7cbf581f22cbf08bdcc33884f1ff747689312841516c345"}));
		keyboard.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&fr/R &7Numpad"}));
		keyboard.put(path+".Lore", lB2Lo);
		keyboard.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_R_SMALL.toString()}));
		keyboard.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_R_CAPITAL.toString()}));
		path = "22"; //T
		keyboard.put(path+".SettingLevel", lBSL);
		keyboard.put(path+".Material", lBMat);
		keyboard.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/fc2fcbc24e7382ac112bb2c0d5eca27e9f48ffca5a157e502617a96d636f5c3"}));
		keyboard.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&ft/T &7Numpad"}));
		keyboard.put(path+".Lore", lB2Lo);
		keyboard.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_T_SMALL.toString()}));
		keyboard.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_T_CAPITAL.toString()}));
		path = "23"; //Y
		keyboard.put(path+".SettingLevel", lBSL);
		keyboard.put(path+".Material", lBMat);
		keyboard.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/a71071bef733f477021b3291dc3d47f0bdf0be2da1b165a119a8ff1594567"}));
		keyboard.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&fy/Y &7Numpad"}));
		keyboard.put(path+".Lore", lB2Lo);
		keyboard.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_Y_SMALL.toString()}));
		keyboard.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_Y_CAPITAL.toString()}));
		path = "24"; //U
		keyboard.put(path+".SettingLevel", lBSL);
		keyboard.put(path+".Material", lBMat);
		keyboard.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/9fdc4f321c78d67484135ae464af4fd925bd57d459383a4fe9d2f60a3431a79"}));
		keyboard.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&fu/U &7Numpad"}));
		keyboard.put(path+".Lore", lB2Lo);
		keyboard.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_U_SMALL.toString()}));
		keyboard.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_U_CAPITAL.toString()}));
		path = "25"; //I
		keyboard.put(path+".SettingLevel", lBSL);
		keyboard.put(path+".Material", lBMat);
		keyboard.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/c148a8865bc4afe0747f3415138b96bbb4e8bbb7261f45e5d11d7219f368e4"}));
		keyboard.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&fi/I &7Numpad"}));
		keyboard.put(path+".Lore", lB2Lo);
		keyboard.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_I_SMALL.toString()}));
		keyboard.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_I_CAPITAL.toString()}));
		path = "26"; //O
		keyboard.put(path+".SettingLevel", lBSL);
		keyboard.put(path+".Material", lBMat);
		keyboard.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/1c27235de3a55466b627459f1233596ab6a22c435cfc89a4454b47d32b199431"}));
		keyboard.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&fo/O &7Numpad"}));
		keyboard.put(path+".Lore", lB2Lo);
		keyboard.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_O_SMALL.toString()}));
		keyboard.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_O_CAPITAL.toString()}));
		path = "27"; //A
		keyboard.put(path+".SettingLevel", lBSL);
		keyboard.put(path+".Material", lBMat);
		keyboard.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/17dd34924d2b6a213a5ed46ae5783f95373a9ef5ce5c88f9d736705983b97"}));
		keyboard.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&fa/A &7Numpad"}));
		keyboard.put(path+".Lore", lB2Lo);
		keyboard.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_A_SMALL.toString()}));
		keyboard.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_A_CAPITAL.toString()}));
		path = "28"; //S
		keyboard.put(path+".SettingLevel", lBSL);
		keyboard.put(path+".Material", lBMat);
		keyboard.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/af22d7cd53d5bfe61eafbc2fb1ac94443eec24f455292139ac9fbdb83d0d09"}));
		keyboard.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&fs/S &7Numpad"}));
		keyboard.put(path+".Lore", lB2Lo);
		keyboard.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_S_SMALL.toString()}));
		keyboard.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_S_CAPITAL.toString()}));
		path = "29"; //D
		keyboard.put(path+".SettingLevel", lBSL);
		keyboard.put(path+".Material", lBMat);
		keyboard.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/59aa69229ffdfa182889bf3097d32215c1b2159d987103b1d5843646faac"}));
		keyboard.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&fd/D &7Numpad"}));
		keyboard.put(path+".Lore", lB2Lo);
		keyboard.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_D_SMALL.toString()}));
		keyboard.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_D_CAPITAL.toString()}));
		path = "30"; //F
		keyboard.put(path+".SettingLevel", lBSL);
		keyboard.put(path+".Material", lBMat);
		keyboard.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/9d714bafb0b5ab9cfa7db02efc8927aed1ef29797a595da066efc5c3efdc9"}));
		keyboard.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&ff/F &7Numpad"}));
		keyboard.put(path+".Lore", lB2Lo);
		keyboard.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_F_SMALL.toString()}));
		keyboard.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_F_CAPITAL.toString()}));
		path = "31"; //G
		keyboard.put(path+".SettingLevel", lBSL);
		keyboard.put(path+".Material", lBMat);
		keyboard.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/58c336dedfe197b434b5ab67988cbe9c2c9f285ec1871fdd1ba434855b"}));
		keyboard.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&fg/G &7Numpad"}));
		keyboard.put(path+".Lore", lB2Lo);
		keyboard.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_G_SMALL.toString()}));
		keyboard.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_G_CAPITAL.toString()}));
		path = "32"; //H
		keyboard.put(path+".SettingLevel", lBSL);
		keyboard.put(path+".Material", lBMat);
		keyboard.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/bde4a89be2197f86d2e6166a0ac541ccc21dce28b7854b788d329a39daec32"}));
		keyboard.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&fh/H &7Numpad"}));
		keyboard.put(path+".Lore", lB2Lo);
		keyboard.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_H_SMALL.toString()}));
		keyboard.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_H_CAPITAL.toString()}));
		path = "33"; //J
		keyboard.put(path+".SettingLevel", lBSL);
		keyboard.put(path+".Material", lBMat);
		keyboard.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/18c9dc3d38a56282e1d92337198fb19ea641b61a8c4e57fb4e27c1ba6a4b24c"}));
		keyboard.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&fj/J &7Numpad"}));
		keyboard.put(path+".Lore", lB2Lo);
		keyboard.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_J_SMALL.toString()}));
		keyboard.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_J_CAPITAL.toString()}));
		path = "34"; //K
		keyboard.put(path+".SettingLevel", lBSL);
		keyboard.put(path+".Material", lBMat);
		keyboard.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/12bfeb246f649b86f212feea87a9c216a655565d4b7992e80326b3918d923bd"}));
		keyboard.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&fk/K &7Numpad"}));
		keyboard.put(path+".Lore", lB2Lo);
		keyboard.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_K_SMALL.toString()}));
		keyboard.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_K_CAPITAL.toString()}));
		path = "35"; //L
		keyboard.put(path+".SettingLevel", lBSL);
		keyboard.put(path+".Material", lBMat);
		keyboard.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/cc58321d4bffbec2ddf66bf38cf2f9e9ddf3fa2f1387dc7d30c62b4d010c8"}));
		keyboard.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&fl/L &7Numpad"}));
		keyboard.put(path+".Lore", lB2Lo);
		keyboard.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_L_SMALL.toString()}));
		keyboard.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_L_CAPITAL.toString()}));
		path = "36"; //Z
		keyboard.put(path+".SettingLevel", lBSL);
		keyboard.put(path+".Material", lBMat);
		keyboard.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/c992c753bf9c625853ce2a0b7b174b89a6ec26bb5c3ccb473b6a2012496312"}));
		keyboard.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&fz/Z &7Numpad"}));
		keyboard.put(path+".Lore", lB2Lo);
		keyboard.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_Z_SMALL.toString()}));
		keyboard.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_Z_CAPITAL.toString()}));
		path = "37"; //X
		keyboard.put(path+".SettingLevel", lBSL);
		keyboard.put(path+".Material", lBMat);
		keyboard.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/c38ab145747b4bd09ce0354354948ce69ff6f41d9e098c6848b80e187e919"}));
		keyboard.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&fx/X &7Numpad"}));
		keyboard.put(path+".Lore", lB2Lo);
		keyboard.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_X_SMALL.toString()}));
		keyboard.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_X_CAPITAL.toString()}));
		path = "38"; //C
		keyboard.put(path+".SettingLevel", lBSL);
		keyboard.put(path+".Material", lBMat);
		keyboard.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/56b1486e1f576bc921b8f9f59fe6122ce6ce9dd70d75e2c92fdb8ab9897b5"}));
		keyboard.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&fc/C &7Numpad"}));
		keyboard.put(path+".Lore", lB2Lo);
		keyboard.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_C_SMALL.toString()}));
		keyboard.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_C_CAPITAL.toString()}));
		path = "39"; //V
		keyboard.put(path+".SettingLevel", lBSL);
		keyboard.put(path+".Material", lBMat);
		keyboard.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/2dd0143d8e449ad1ba97e1981712cee0f3fc297dbc17c83b05eea3338d659"}));
		keyboard.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&fv/V &7Numpad"}));
		keyboard.put(path+".Lore", lB2Lo);
		keyboard.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_V_SMALL.toString()}));
		keyboard.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_V_CAPITAL.toString()}));
		path = "40"; //B
		keyboard.put(path+".SettingLevel", lBSL);
		keyboard.put(path+".Material", lBMat);
		keyboard.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/9eca98befd0d7efca9b11ebf4b2da459cc19a378114b3cdde67d4067afb896"}));
		keyboard.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&fb/B &7Numpad"}));
		keyboard.put(path+".Lore", lB2Lo);
		keyboard.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_B_SMALL.toString()}));
		keyboard.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_B_CAPITAL.toString()}));
		path = "41"; //N
		keyboard.put(path+".SettingLevel", lBSL);
		keyboard.put(path+".Material", lBMat);
		keyboard.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/612c7afea48e53325e5129038a45aec51afe256abca941b6bc8206fae1cef"}));
		keyboard.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&fn/N &7Numpad"}));
		keyboard.put(path+".Lore", lB2Lo);
		keyboard.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_N_SMALL.toString()}));
		keyboard.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_N_CAPITAL.toString()}));
		path = "42"; //M
		keyboard.put(path+".SettingLevel", lBSL);
		keyboard.put(path+".Material", lBMat);
		keyboard.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/90376dc5e3c981b52960578afe4bfc41c1778789bcd80ec2c2d2fd460e5a51a"}));
		keyboard.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&fm/M &7Numpad"}));
		keyboard.put(path+".Lore", lB2Lo);
		keyboard.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_M_SMALL.toString()}));
		keyboard.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_M_CAPITAL.toString()}));
		path = "43"; //P
		keyboard.put(path+".SettingLevel", lBSL);
		keyboard.put(path+".Material", lBMat);
		keyboard.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/c584dc7ecf36b4f044f8262527985718bf24a9daef012de92e1e76d4586d96"}));
		keyboard.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&fp/P &7Numpad"}));
		keyboard.put(path+".Lore", lB2Lo);
		keyboard.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_P_SMALL.toString()}));
		keyboard.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_P_CAPITAL.toString()}));
		path = "44"; //_
		keyboard.put(path+".SettingLevel", lBSL);
		keyboard.put(path+".Material", lBMat);
		keyboard.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/979a465183a3ba63fe6ae272bc1bf1cd15f2c209ebbfcc5c521b9514682a43"}));
		keyboard.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&f_ &7Numpad"}));
		keyboard.put(path+".Lore", lBLo);
		keyboard.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD__.toString()}));
		keyboard.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD__.toString()}));
		path = "49"; //0-1
		keyboard.put(path+".SettingLevel", lBSL);
		keyboard.put(path+".Material", lBMat);
		keyboard.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/6d68343bd0b129de93cc8d3bba3b97a2faa7ade38d8a6e2b864cd868cfab"}));
		keyboard.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&f0/1 &7Numpad"}));
		keyboard.put(path+".Lore", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&bZz.: &f%numtext%",
						"&cLinksklick &bfügt eine 0 hinzu.",
						"&cRechtsklick &bfügt eine 1 hinzu.",
						"&bAtm.: &f%numtext%",
						"&cLeftclick &badds a 0.",
						"&cRightclick &badds a 1."}));
		keyboard.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_0.toString()}));
		keyboard.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_1.toString()}));
		path = "50"; //2-3
		keyboard.put(path+".SettingLevel", lBSL);
		keyboard.put(path+".Material", lBMat);
		keyboard.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/96fab991d083993cb83e4bcf44a0b6cefac647d4189ee9cb823e9cc1571e38"}));
		keyboard.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&f2/3 &7Numpad"}));
		keyboard.put(path+".Lore",  
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&bZz.: &f%numtext%",
						"&cLinksklick &bfügt eine 2 hinzu.",
						"&cRechtsklick &bfügt eine 3 hinzu.",
						"&bAtm.: &f%numtext%",
						"&cLeftclick &badds a 2.",
						"&cRightclick &badds a 3."}));
		keyboard.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_2.toString()}));
		keyboard.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_3.toString()}));
		path = "51"; //4-5
		keyboard.put(path+".SettingLevel", lBSL);
		keyboard.put(path+".Material", lBMat);
		keyboard.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/d198d56216156114265973c258f57fc79d246bb65e3c77bbe8312ee35db6"}));
		keyboard.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&f4/5 &7Numpad"}));
		keyboard.put(path+".Lore",  
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&bZz.: &f%numtext%",
						"&cLinksklick &bfügt eine 4 hinzu.",
						"&cRechtsklick &bfügt eine 5 hinzu.",
						"&bAtm.: &f%numtext%",
						"&cLeftclick &badds a 4.",
						"&cRightclick &badds a 5."}));
		keyboard.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_4.toString()}));
		keyboard.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_5.toString()}));
		path = "52"; //6-7
		keyboard.put(path+".SettingLevel", lBSL);
		keyboard.put(path+".Material", lBMat);
		keyboard.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/9c613f80a554918c7ab2cd4a278752f151412a44a73d7a286d61d45be4eaae1"}));
		keyboard.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&f6/7 &7Numpad"}));
		keyboard.put(path+".Lore",  
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&bZz.: &f%numtext%",
						"&cLinksklick &bfügt eine 6 hinzu.",
						"&cRechtsklick &bfügt eine 7 hinzu.",
						"&bAtm.: &f%numtext%",
						"&cLeftclick &badds a 6.",
						"&cRightclick &badds a 7."}));
		keyboard.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_6.toString()}));
		keyboard.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_7.toString()}));
		path = "53"; //8-9
		keyboard.put(path+".SettingLevel", lBSL);
		keyboard.put(path+".Material", lBMat);
		keyboard.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/84ad12c2f21a1972f3d2f381ed05a6cc088489fcfdf68a713b387482fe91e2"}));
		keyboard.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"&f8/9 &7Numpad"}));
		keyboard.put(path+".Lore",  
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&bZz.: &f%numtext%",
						"&cLinksklick &bfügt eine 8 hinzu.",
						"&cRechtsklick &bfügt eine 9 hinzu.",
						"&bAtm.: &f%numtext%",
						"&cLeftclick &badds a 8.",
						"&cRightclick &badds a 9."}));
		keyboard.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_8.toString()}));
		keyboard.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_KEYBOARD_9.toString()}));
		path = "46"; //RemoveOnce
		keyboard.put(path+".SettingLevel", lBSL);
		keyboard.put(path+".Material", lBMat);
		keyboard.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/864f779a8e3ffa231143fa69b96b14ee35c16d669e19c75fd1a7da4bf306c"}));
		keyboard.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eUm ein Zeichen zurücksetzen",
						"&eReset by one character"}));
		keyboard.put(path+".Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&bZz.: &f%numtext%",
						"&bSetzt die Eingabe um ein Zeichen zurück.",
						"&bAtm.: &f%numtext%",
						"&bResets the input by one character."}));
		keyboard.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_NUMPAD_REMOVEONCE.toString()}));
		keyboard.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_NUMPAD_REMOVEONCE.toString()}));
		path = "47"; //Clear
		keyboard.put(path+".SettingLevel", lBSL);
		keyboard.put(path+".Material", lBMat);
		keyboard.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"https://textures.minecraft.net/texture/118a2dd5bef0b073b13271a7eeb9cfea7afe8593c57a93821e43175572461812"}));
		keyboard.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eZurücksetzen",
						"&eReset"}));
		keyboard.put(path+".Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&bZz.: &f%numtext%",
						"&bSetzt die Eingabe zurück.",
						"&bAtm.: &f%numtext%",
						"&bResets the input."}));
		keyboard.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_NUMPAD_CLEAR.toString()}));
		keyboard.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_NUMPAD_CLEAR.toString()}));
		path = "45"; //Cancel
		keyboard.put(path+".SettingLevel", lBSL);
		keyboard.put(path+".Material",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.RED_BANNER.toString()}));
		keyboard.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cZurück zum Administrations-Gui",
						"&cBack to the administration gui"}));
		keyboard.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_NUMPAD_CANCEL.toString()}));
		keyboard.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_NUMPAD_CANCEL.toString()}));
		LinkedHashMap<String, Language> keyboard_signshopname = new LinkedHashMap<>();
		keyboard_signshopname.putAll(keyboard);
		LinkedHashMap<String, Language> keyboard_blacklist = new LinkedHashMap<>();
		keyboard_blacklist.putAll(keyboard);
		LinkedHashMap<String, Language> keyboard_whitelist = new LinkedHashMap<>();
		keyboard_whitelist.putAll(keyboard);
		LinkedHashMap<String, Language> keyboard_member = new LinkedHashMap<>();
		keyboard_member.putAll(keyboard);
		LinkedHashMap<String, Language> keyboard_custom = new LinkedHashMap<>();
		keyboard_custom.putAll(keyboard);
		path = "4"; //TakeOver
		keyboard_signshopname.put(path+".SettingLevel",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						SettingsLevel.NOLEVEL.toString()}));
		keyboard_signshopname.put(path+".Material",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.GREEN_BANNER.toString()}));
		keyboard_signshopname.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&aÜbernahme der Eingabe",
						"&cAcceptance of the input"}));
		keyboard_signshopname.put(path+".Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&bZz.: &f%numtext%", "&bÜbernimmt die Eingabe und","&bkehre zum Administration Gui zurück.",
						"&bAtm.: &f%numtext%", "&bAccept the input and","&breturn to the Administration Gui."}));
		keyboard_signshopname.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_SETSIGNSHOPNAME_TAKEOVER.toString()}));
		keyboard_signshopname.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.ADMINISTRATION_SETSIGNSHOPNAME_TAKEOVER.toString()}));
		guiKeys.put(GuiType.KEYBOARD_SIGNSHOPNAME, keyboard_signshopname);
		//-------------
		Language lSL = new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				SettingsLevel.NOLEVEL.toString()});
		Language lMat = new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				Material.PLAYER_HEAD.toString()});
		Language lLo_BL = new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&bZz.: &f%numtext%",
				"&bZz. auf der Blackliste: &f%isonblacklist%",
				"&cLinksklick &bfügt den Spieler",
				"&bzur Blacklist dieses Shops hinzu.",
				"&cRechtsklick &bfügt den Spieler",
				"&bzur Blacklist aller Shops dieser Welt hinzu.",
				"&cShift-Linksklick &bentfernt den Spieler",
				"&bvon der Blackliste dies Shops.",
				"&cShift-Rechtsklick &bentfernt den Spieler",
				"&bvon der Blackliste aller Shops dieser Welt.",
				"&bAtm.: &f%numtext%",
				"&bAtm. on the blacklist: &f%isonblacklist%",
				"&cLeftclick &badds the player to",
				"&bthe blacklist of this store.",
				"&cRightclick &badds the player to",
				"&bthe blacklist of all stores in the world.",
				"&cShift-Leftclick &bremoves the player",
				"&bfrom the blacklist of this store.",
				"&cShift-Rightclick &bremoves the player from",
				"&bthe blacklist of all stores in the world."});
		Language lLC = new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				ClickFunctionType.ADMINISTRATION_ADDLISTEDTYPE_PLAYER_BLACKLIST.toString()});
		Language lRC = new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				ClickFunctionType.ADMINISTRATION_ADDLISTEDTYPE_PLAYER_BLACKLIST_WORLD.toString()});
		Language lSLC = new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				ClickFunctionType.ADMINISTRATION_ADDLISTEDTYPE_PLAYER_BLACKLIST_REMOVE.toString()});
		Language lSRC = new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				ClickFunctionType.ADMINISTRATION_ADDLISTEDTYPE_PLAYER_BLACKLIST_REMOVE_WORLD.toString()});
		path = "0"; //PlayerHead
		keyboard_blacklist.put(path+".SettingLevel", lSL);
		keyboard_blacklist.put(path+".Material", lMat);
		keyboard_blacklist.put(path+".PlayerSearchNum", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {0}));
		keyboard_blacklist.put(path+".Lore", lLo_BL);
		keyboard_blacklist.put(path+".ClickFunction."+ClickType.LEFT.toString(), lLC);
		keyboard_blacklist.put(path+".ClickFunction."+ClickType.RIGHT.toString(), lRC);
		keyboard_blacklist.put(path+".ClickFunction."+ClickType.SHIFT_LEFT.toString(), lSLC);
		keyboard_blacklist.put(path+".ClickFunction."+ClickType.SHIFT_RIGHT.toString(), lSRC);
		path = "1"; //PlayerHead
		keyboard_blacklist.put(path+".SettingLevel", lSL);
		keyboard_blacklist.put(path+".Material", lMat);
		keyboard_blacklist.put(path+".PlayerSearchNum", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {1}));
		keyboard_blacklist.put(path+".Lore", lLo_BL);
		keyboard_blacklist.put(path+".ClickFunction."+ClickType.LEFT.toString(), lLC);
		keyboard_blacklist.put(path+".ClickFunction."+ClickType.RIGHT.toString(), lRC);
		keyboard_blacklist.put(path+".ClickFunction."+ClickType.SHIFT_LEFT.toString(), lSLC);
		keyboard_blacklist.put(path+".ClickFunction."+ClickType.SHIFT_RIGHT.toString(), lSRC);
		path = "2"; //PlayerHead
		keyboard_blacklist.put(path+".SettingLevel", lSL);
		keyboard_blacklist.put(path+".Material", lMat);
		keyboard_blacklist.put(path+".PlayerSearchNum", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {2}));
		keyboard_blacklist.put(path+".Lore", lLo_BL);
		keyboard_blacklist.put(path+".ClickFunction."+ClickType.LEFT.toString(), lLC);
		keyboard_blacklist.put(path+".ClickFunction."+ClickType.RIGHT.toString(), lRC);
		keyboard_blacklist.put(path+".ClickFunction."+ClickType.SHIFT_LEFT.toString(), lSLC);
		keyboard_blacklist.put(path+".ClickFunction."+ClickType.SHIFT_RIGHT.toString(), lSRC);
		path = "3"; //PlayerHead
		keyboard_blacklist.put(path+".SettingLevel", lSL);
		keyboard_blacklist.put(path+".Material", lMat);
		keyboard_blacklist.put(path+".PlayerSearchNum", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {3}));
		keyboard_blacklist.put(path+".Lore", lLo_BL);
		keyboard_blacklist.put(path+".ClickFunction."+ClickType.LEFT.toString(), lLC);
		keyboard_blacklist.put(path+".ClickFunction."+ClickType.RIGHT.toString(), lRC);
		keyboard_blacklist.put(path+".ClickFunction."+ClickType.SHIFT_LEFT.toString(), lSLC);
		keyboard_blacklist.put(path+".ClickFunction."+ClickType.SHIFT_RIGHT.toString(), lSRC);
		path = "4"; //PlayerHead
		keyboard_blacklist.put(path+".SettingLevel", lSL);
		keyboard_blacklist.put(path+".Material", lMat);
		keyboard_blacklist.put(path+".PlayerSearchNum", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {4}));
		keyboard_blacklist.put(path+".Lore", lLo_BL);
		keyboard_blacklist.put(path+".ClickFunction."+ClickType.LEFT.toString(), lLC);
		keyboard_blacklist.put(path+".ClickFunction."+ClickType.RIGHT.toString(), lRC);
		keyboard_blacklist.put(path+".ClickFunction."+ClickType.SHIFT_LEFT.toString(), lSLC);
		keyboard_blacklist.put(path+".ClickFunction."+ClickType.SHIFT_RIGHT.toString(), lSRC);
		path = "5"; //PlayerHead
		keyboard_blacklist.put(path+".SettingLevel", lSL);
		keyboard_blacklist.put(path+".Material", lMat);
		keyboard_blacklist.put(path+".PlayerSearchNum", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {5}));
		keyboard_blacklist.put(path+".Lore", lLo_BL);
		keyboard_blacklist.put(path+".ClickFunction."+ClickType.LEFT.toString(), lLC);
		keyboard_blacklist.put(path+".ClickFunction."+ClickType.RIGHT.toString(), lRC);
		keyboard_blacklist.put(path+".ClickFunction."+ClickType.SHIFT_LEFT.toString(), lSLC);
		keyboard_blacklist.put(path+".ClickFunction."+ClickType.SHIFT_RIGHT.toString(), lSRC);
		path = "6"; //PlayerHead
		keyboard_blacklist.put(path+".SettingLevel", lSL);
		keyboard_blacklist.put(path+".Material", lMat);
		keyboard_blacklist.put(path+".PlayerSearchNum", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {6}));
		keyboard_blacklist.put(path+".Lore", lLo_BL);
		keyboard_blacklist.put(path+".ClickFunction."+ClickType.LEFT.toString(), lLC);
		keyboard_blacklist.put(path+".ClickFunction."+ClickType.RIGHT.toString(), lRC);
		keyboard_blacklist.put(path+".ClickFunction."+ClickType.SHIFT_LEFT.toString(), lSLC);
		keyboard_blacklist.put(path+".ClickFunction."+ClickType.SHIFT_RIGHT.toString(), lSRC);
		path = "7"; //PlayerHead
		keyboard_blacklist.put(path+".SettingLevel", lSL);
		keyboard_blacklist.put(path+".Material", lMat);
		keyboard_blacklist.put(path+".PlayerSearchNum", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {7}));
		keyboard_blacklist.put(path+".Lore", lLo_BL);
		keyboard_blacklist.put(path+".ClickFunction."+ClickType.LEFT.toString(), lLC);
		keyboard_blacklist.put(path+".ClickFunction."+ClickType.RIGHT.toString(), lRC);
		keyboard_blacklist.put(path+".ClickFunction."+ClickType.SHIFT_LEFT.toString(), lSLC);
		keyboard_blacklist.put(path+".ClickFunction."+ClickType.SHIFT_RIGHT.toString(), lSRC);
		path = "8"; //PlayerHead
		keyboard_blacklist.put(path+".SettingLevel", lSL);
		keyboard_blacklist.put(path+".Material", lMat);
		keyboard_blacklist.put(path+".PlayerSearchNum", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {8}));
		keyboard_blacklist.put(path+".Lore", lLo_BL);
		keyboard_blacklist.put(path+".ClickFunction."+ClickType.LEFT.toString(), lLC);
		keyboard_blacklist.put(path+".ClickFunction."+ClickType.RIGHT.toString(), lRC);
		keyboard_blacklist.put(path+".ClickFunction."+ClickType.SHIFT_LEFT.toString(), lSLC);
		keyboard_blacklist.put(path+".ClickFunction."+ClickType.SHIFT_RIGHT.toString(), lSRC);
		guiKeys.put(GuiType.KEYBOARD_BLACKLIST, keyboard_blacklist);
		//-------------
		Language lLo_WL = new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&bZz.: &f%numtext%",
				"&bZz. auf der Whiteliste: &f%isonwhitelist%",
				"&cLinksklick &bfügt den Spieler",
				"&bzur Whitelist dieses Shops hinzu.",
				"&cRechtsklick &bfügt den Spieler",
				"&bzur Whitelist aller Shops dieser Welt hinzu.",
				"&cShift-Linksklick &bentfernt den Spieler",
				"&bvon der Whiteliste dieses Shops.",
				"&cShift-Rechtsklick &bentfernt den Spieler",
				"&bvon der Whiteliste aller Shops dieser Welt.",
				"&bAtm.: &f%numtext%",
				"&bAtm. on the whitelist: &f%isonwhitelist%",
				"&cLeftclick &badds the player to the",
				"&bwhitelist of this store.",
				"&cRightclick &badds the player to the",
				"&bwhitelist of all stores in the world.",
				"&cShift-Leftclick &bremoves the player",
				"&bfrom the whitelist of this store.",
				"&cShift-Rightclick &bremoves the player",
				"&bfrom the whitelist of all stores in the world."});
		lLC = new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				ClickFunctionType.ADMINISTRATION_ADDLISTEDTYPE_PLAYER_WHITELIST.toString()});
		lRC = new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				ClickFunctionType.ADMINISTRATION_ADDLISTEDTYPE_PLAYER_WHITELIST_WORLD.toString()});
		lSLC = new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				ClickFunctionType.ADMINISTRATION_ADDLISTEDTYPE_PLAYER_WHITELIST_REMOVE.toString()});
		lSRC = new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				ClickFunctionType.ADMINISTRATION_ADDLISTEDTYPE_PLAYER_WHITELIST_REMOVE_WORLD.toString()});
		path = "0"; //PlayerHead
		keyboard_whitelist.put(path+".SettingLevel", lSL);
		keyboard_whitelist.put(path+".Material", lMat);
		keyboard_whitelist.put(path+".PlayerSearchNum", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {0}));
		keyboard_whitelist.put(path+".Lore", lLo_WL);
		keyboard_whitelist.put(path+".ClickFunction."+ClickType.LEFT.toString(), lLC);
		keyboard_whitelist.put(path+".ClickFunction."+ClickType.RIGHT.toString(), lRC);
		keyboard_whitelist.put(path+".ClickFunction."+ClickType.SHIFT_LEFT.toString(), lSLC);
		keyboard_whitelist.put(path+".ClickFunction."+ClickType.SHIFT_RIGHT.toString(), lSRC);
		path = "1"; //PlayerHead
		keyboard_whitelist.put(path+".SettingLevel", lSL);
		keyboard_whitelist.put(path+".Material", lMat);
		keyboard_whitelist.put(path+".PlayerSearchNum", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {1}));
		keyboard_whitelist.put(path+".Lore", lLo_WL);
		keyboard_whitelist.put(path+".ClickFunction."+ClickType.LEFT.toString(), lLC);
		keyboard_whitelist.put(path+".ClickFunction."+ClickType.RIGHT.toString(), lRC);
		keyboard_whitelist.put(path+".ClickFunction."+ClickType.SHIFT_LEFT.toString(), lSLC);
		keyboard_whitelist.put(path+".ClickFunction."+ClickType.SHIFT_RIGHT.toString(), lSRC);
		path = "2"; //PlayerHead
		keyboard_whitelist.put(path+".SettingLevel", lSL);
		keyboard_whitelist.put(path+".Material", lMat);
		keyboard_whitelist.put(path+".PlayerSearchNum", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {2}));
		keyboard_whitelist.put(path+".Lore", lLo_WL);
		keyboard_whitelist.put(path+".ClickFunction."+ClickType.LEFT.toString(), lLC);
		keyboard_whitelist.put(path+".ClickFunction."+ClickType.RIGHT.toString(), lRC);
		keyboard_whitelist.put(path+".ClickFunction."+ClickType.SHIFT_LEFT.toString(), lSLC);
		keyboard_whitelist.put(path+".ClickFunction."+ClickType.SHIFT_RIGHT.toString(), lSRC);
		path = "3"; //PlayerHead
		keyboard_whitelist.put(path+".SettingLevel", lSL);
		keyboard_whitelist.put(path+".Material", lMat);
		keyboard_whitelist.put(path+".PlayerSearchNum", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {3}));
		keyboard_whitelist.put(path+".Lore", lLo_WL);
		keyboard_whitelist.put(path+".ClickFunction."+ClickType.LEFT.toString(), lLC);
		keyboard_whitelist.put(path+".ClickFunction."+ClickType.RIGHT.toString(), lRC);
		keyboard_whitelist.put(path+".ClickFunction."+ClickType.SHIFT_LEFT.toString(), lSLC);
		keyboard_whitelist.put(path+".ClickFunction."+ClickType.SHIFT_RIGHT.toString(), lSRC);
		path = "4"; //PlayerHead
		keyboard_whitelist.put(path+".SettingLevel", lSL);
		keyboard_whitelist.put(path+".Material", lMat);
		keyboard_whitelist.put(path+".PlayerSearchNum", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {4}));
		keyboard_whitelist.put(path+".Lore", lLo_WL);
		keyboard_whitelist.put(path+".ClickFunction."+ClickType.LEFT.toString(), lLC);
		keyboard_whitelist.put(path+".ClickFunction."+ClickType.RIGHT.toString(), lRC);
		keyboard_whitelist.put(path+".ClickFunction."+ClickType.SHIFT_LEFT.toString(), lSLC);
		keyboard_whitelist.put(path+".ClickFunction."+ClickType.SHIFT_RIGHT.toString(), lSRC);
		path = "5"; //PlayerHead
		keyboard_whitelist.put(path+".SettingLevel", lSL);
		keyboard_whitelist.put(path+".Material", lMat);
		keyboard_whitelist.put(path+".PlayerSearchNum", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {5}));
		keyboard_whitelist.put(path+".Lore", lLo_WL);
		keyboard_whitelist.put(path+".ClickFunction."+ClickType.LEFT.toString(), lLC);
		keyboard_whitelist.put(path+".ClickFunction."+ClickType.RIGHT.toString(), lRC);
		keyboard_whitelist.put(path+".ClickFunction."+ClickType.SHIFT_LEFT.toString(), lSLC);
		keyboard_whitelist.put(path+".ClickFunction."+ClickType.SHIFT_RIGHT.toString(), lSRC);
		path = "6"; //PlayerHead
		keyboard_whitelist.put(path+".SettingLevel", lSL);
		keyboard_whitelist.put(path+".Material", lMat);
		keyboard_whitelist.put(path+".PlayerSearchNum", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {6}));
		keyboard_whitelist.put(path+".Lore", lLo_WL);
		keyboard_whitelist.put(path+".ClickFunction."+ClickType.LEFT.toString(), lLC);
		keyboard_whitelist.put(path+".ClickFunction."+ClickType.RIGHT.toString(), lRC);
		keyboard_whitelist.put(path+".ClickFunction."+ClickType.SHIFT_LEFT.toString(), lSLC);
		keyboard_whitelist.put(path+".ClickFunction."+ClickType.SHIFT_RIGHT.toString(), lSRC);
		path = "7"; //PlayerHead
		keyboard_whitelist.put(path+".SettingLevel", lSL);
		keyboard_whitelist.put(path+".Material", lMat);
		keyboard_whitelist.put(path+".PlayerSearchNum", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {7}));
		keyboard_whitelist.put(path+".Lore", lLo_WL);
		keyboard_whitelist.put(path+".ClickFunction."+ClickType.LEFT.toString(), lLC);
		keyboard_whitelist.put(path+".ClickFunction."+ClickType.RIGHT.toString(), lRC);
		keyboard_whitelist.put(path+".ClickFunction."+ClickType.SHIFT_LEFT.toString(), lSLC);
		keyboard_whitelist.put(path+".ClickFunction."+ClickType.SHIFT_RIGHT.toString(), lSRC);
		path = "8"; //PlayerHead
		keyboard_whitelist.put(path+".SettingLevel", lSL);
		keyboard_whitelist.put(path+".Material", lMat);
		keyboard_whitelist.put(path+".PlayerSearchNum", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {8}));
		keyboard_whitelist.put(path+".Lore", lLo_WL);
		keyboard_whitelist.put(path+".ClickFunction."+ClickType.LEFT.toString(), lLC);
		keyboard_whitelist.put(path+".ClickFunction."+ClickType.RIGHT.toString(), lRC);
		keyboard_whitelist.put(path+".ClickFunction."+ClickType.SHIFT_LEFT.toString(), lSLC);
		keyboard_whitelist.put(path+".ClickFunction."+ClickType.SHIFT_RIGHT.toString(), lSRC);
		guiKeys.put(GuiType.KEYBOARD_WHITELIST, keyboard_whitelist);
		//-------------
		Language lLo_M = new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&bZz.: &f%numtext%",
				"&bZz. ist ein Mitglied: &f%ismember%",
				"&cLinksklick &bfügt den Spieler",
				"&bzur Mitgliedern dieses Shops hinzu.",
				"&cRechtsklick &bfügt den Spieler",
				"&bzur Mitgliedern aller Shops dieser Welt hinzu.",
				"&cShift-Linksklick &bentfernt den",
				"&bSpieler von den Mitgliedern dieses Shops.",
				"&cShift-Rechtsklick &bentfernt den Spieler",
				"&bvon den Mitgliedern aller Shops dieser Welt.",
				"&bAtm.: &f%numtext%",
				"&bAtm. is a member: &f%ismember%",
				"&cLeftclick &badds the player to",
				"&bthe members of this store.",
				"&cRightclick &badds the player to",
				"&bthe members of all stores in the world.",
				"&cShift-Leftclick &bremoves the",
				"&bplayer from the members of this store.",
				"&cShift-Rightclick &bremoves the player",
				"&bfrom the members of all stores in the world."});
		lLC = new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				ClickFunctionType.ADMINISTRATION_ADDLISTEDTYPE_PLAYER_MEMBER.toString()});
		lRC = new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				ClickFunctionType.ADMINISTRATION_ADDLISTEDTYPE_PLAYER_MEMBER_WORLD.toString()});
		lSLC = new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				ClickFunctionType.ADMINISTRATION_ADDLISTEDTYPE_PLAYER_MEMBER_REMOVE.toString()});
		lSRC = new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				ClickFunctionType.ADMINISTRATION_ADDLISTEDTYPE_PLAYER_MEMBER_REMOVE_WORLD.toString()});
		path = "0"; //PlayerHead
		keyboard_member.put(path+".SettingLevel", lSL);
		keyboard_member.put(path+".Material", lMat);
		keyboard_member.put(path+".PlayerSearchNum", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {0}));
		keyboard_member.put(path+".Lore", lLo_M);
		keyboard_member.put(path+".ClickFunction."+ClickType.LEFT.toString(), lLC);
		keyboard_member.put(path+".ClickFunction."+ClickType.RIGHT.toString(), lRC);
		keyboard_member.put(path+".ClickFunction."+ClickType.SHIFT_LEFT.toString(), lSLC);
		keyboard_member.put(path+".ClickFunction."+ClickType.SHIFT_RIGHT.toString(), lSRC);
		path = "1"; //PlayerHead
		keyboard_member.put(path+".SettingLevel", lSL);
		keyboard_member.put(path+".Material", lMat);
		keyboard_member.put(path+".PlayerSearchNum", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {1}));
		keyboard_member.put(path+".Lore", lLo_M);
		keyboard_member.put(path+".ClickFunction."+ClickType.LEFT.toString(), lLC);
		keyboard_member.put(path+".ClickFunction."+ClickType.RIGHT.toString(), lRC);
		keyboard_member.put(path+".ClickFunction."+ClickType.SHIFT_LEFT.toString(), lSLC);
		keyboard_member.put(path+".ClickFunction."+ClickType.SHIFT_RIGHT.toString(), lSRC);
		path = "2"; //PlayerHead
		keyboard_member.put(path+".SettingLevel", lSL);
		keyboard_member.put(path+".Material", lMat);
		keyboard_member.put(path+".PlayerSearchNum", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {2}));
		keyboard_member.put(path+".Lore", lLo_M);
		keyboard_member.put(path+".ClickFunction."+ClickType.LEFT.toString(), lLC);
		keyboard_member.put(path+".ClickFunction."+ClickType.RIGHT.toString(), lRC);
		keyboard_member.put(path+".ClickFunction."+ClickType.SHIFT_LEFT.toString(), lSLC);
		keyboard_member.put(path+".ClickFunction."+ClickType.SHIFT_RIGHT.toString(), lSRC);
		path = "3"; //PlayerHead
		keyboard_member.put(path+".SettingLevel", lSL);
		keyboard_member.put(path+".Material", lMat);
		keyboard_member.put(path+".PlayerSearchNum", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {3}));
		keyboard_member.put(path+".Lore", lLo_M);
		keyboard_member.put(path+".ClickFunction."+ClickType.LEFT.toString(), lLC);
		keyboard_member.put(path+".ClickFunction."+ClickType.RIGHT.toString(), lRC);
		keyboard_member.put(path+".ClickFunction."+ClickType.SHIFT_LEFT.toString(), lSLC);
		keyboard_member.put(path+".ClickFunction."+ClickType.SHIFT_RIGHT.toString(), lSRC);
		path = "4"; //PlayerHead
		keyboard_member.put(path+".SettingLevel", lSL);
		keyboard_member.put(path+".Material", lMat);
		keyboard_member.put(path+".PlayerSearchNum", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {4}));
		keyboard_member.put(path+".Lore", lLo_M);
		keyboard_member.put(path+".ClickFunction."+ClickType.LEFT.toString(), lLC);
		keyboard_member.put(path+".ClickFunction."+ClickType.RIGHT.toString(), lRC);
		keyboard_member.put(path+".ClickFunction."+ClickType.SHIFT_LEFT.toString(), lSLC);
		keyboard_member.put(path+".ClickFunction."+ClickType.SHIFT_RIGHT.toString(), lSRC);
		path = "5"; //PlayerHead
		keyboard_member.put(path+".SettingLevel", lSL);
		keyboard_member.put(path+".Material", lMat);
		keyboard_member.put(path+".PlayerSearchNum", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {5}));
		keyboard_member.put(path+".Lore", lLo_M);
		keyboard_member.put(path+".ClickFunction."+ClickType.LEFT.toString(), lLC);
		keyboard_member.put(path+".ClickFunction."+ClickType.RIGHT.toString(), lRC);
		keyboard_member.put(path+".ClickFunction."+ClickType.SHIFT_LEFT.toString(), lSLC);
		keyboard_member.put(path+".ClickFunction."+ClickType.SHIFT_RIGHT.toString(), lSRC);
		path = "6"; //PlayerHead
		keyboard_member.put(path+".SettingLevel", lSL);
		keyboard_member.put(path+".Material", lMat);
		keyboard_member.put(path+".PlayerSearchNum", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {6}));
		keyboard_member.put(path+".Lore", lLo_M);
		keyboard_member.put(path+".ClickFunction."+ClickType.LEFT.toString(), lLC);
		keyboard_member.put(path+".ClickFunction."+ClickType.RIGHT.toString(), lRC);
		keyboard_member.put(path+".ClickFunction."+ClickType.SHIFT_LEFT.toString(), lSLC);
		keyboard_member.put(path+".ClickFunction."+ClickType.SHIFT_RIGHT.toString(), lSRC);
		path = "7"; //PlayerHead
		keyboard_member.put(path+".SettingLevel", lSL);
		keyboard_member.put(path+".Material", lMat);
		keyboard_member.put(path+".PlayerSearchNum", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {7}));
		keyboard_member.put(path+".Lore", lLo_M);
		keyboard_member.put(path+".ClickFunction."+ClickType.LEFT.toString(), lLC);
		keyboard_member.put(path+".ClickFunction."+ClickType.RIGHT.toString(), lRC);
		keyboard_member.put(path+".ClickFunction."+ClickType.SHIFT_LEFT.toString(), lSLC);
		keyboard_member.put(path+".ClickFunction."+ClickType.SHIFT_RIGHT.toString(), lSRC);
		path = "8"; //PlayerHead
		keyboard_member.put(path+".SettingLevel", lSL);
		keyboard_member.put(path+".Material", lMat);
		keyboard_member.put(path+".PlayerSearchNum", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {8}));
		keyboard_member.put(path+".Lore", lLo_M);
		keyboard_member.put(path+".ClickFunction."+ClickType.LEFT.toString(), lLC);
		keyboard_member.put(path+".ClickFunction."+ClickType.RIGHT.toString(), lRC);
		keyboard_member.put(path+".ClickFunction."+ClickType.SHIFT_LEFT.toString(), lSLC);
		keyboard_member.put(path+".ClickFunction."+ClickType.SHIFT_RIGHT.toString(), lSRC);
		guiKeys.put(GuiType.KEYBOARD_MEMBER, keyboard_member);
		//-------------
		Language lLo_C = new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&bZz.: &f%numtext%",
				"&bZz. auf der benutzerdefinierten Liste: &f%isoncustom%",
				"&cLinksklick &bfügt den Spieler zur",
				"&bbenutzerdefinierte Liste dieses Shops hinzu.",
				"&cRechtsklick &bfügt den Spieler zur",
				"&bbenutzerdefinierte Liste aller Shops dieser Welt hinzu.",
				"&cShift-Linksklick &bentfernt den Spieler",
				"&bvon den benutzerdefinierte Liste dieses Shops.",
				"&cShift-Rechtsklick &bentfernt den Spieler",
				"&bvon den benutzerdefinierte Liste aller Shops dieser Welt.",
				"&bAtm.: &f%numtext%",
				"&bAtm. on the custom liste: &f%isoncustom%",
				"&cLeftclick &badds the player to",
				"&bthe custom list of this store.",
				"&cRightclick &badds the player to",
				"&bthe custom list of all stores in the world.",
				"&cShift-Leftclick &bremoves the",
				"&bplayer from the custom list of this store.",
				"&cShift-Rightclick &bremoves the",
				"&bplayer from the custom list of all stores in the world."});
		lLC = new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				ClickFunctionType.ADMINISTRATION_ADDLISTEDTYPE_PLAYER_CUSTOM.toString()});
		lRC = new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				ClickFunctionType.ADMINISTRATION_ADDLISTEDTYPE_PLAYER_CUSTOM_WORLD.toString()});
		lSLC = new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				ClickFunctionType.ADMINISTRATION_ADDLISTEDTYPE_PLAYER_CUSTOM_REMOVE.toString()});
		lSRC = new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				ClickFunctionType.ADMINISTRATION_ADDLISTEDTYPE_PLAYER_CUSTOM_REMOVE_WORLD.toString()});
		path = "0"; //PlayerHead
		keyboard_custom.put(path+".SettingLevel", lSL);
		keyboard_custom.put(path+".Material", lMat);
		keyboard_custom.put(path+".PlayerSearchNum", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {0}));
		keyboard_custom.put(path+".Lore", lLo_C);
		keyboard_custom.put(path+".ClickFunction."+ClickType.LEFT.toString(), lLC);
		keyboard_custom.put(path+".ClickFunction."+ClickType.RIGHT.toString(), lRC);
		keyboard_custom.put(path+".ClickFunction."+ClickType.SHIFT_LEFT.toString(), lSLC);
		keyboard_custom.put(path+".ClickFunction."+ClickType.SHIFT_RIGHT.toString(), lSRC);
		path = "1"; //PlayerHead
		keyboard_custom.put(path+".SettingLevel", lSL);
		keyboard_custom.put(path+".Material", lMat);
		keyboard_custom.put(path+".PlayerSearchNum", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {1}));
		keyboard_custom.put(path+".Lore", lLo_C);
		keyboard_custom.put(path+".ClickFunction."+ClickType.LEFT.toString(), lLC);
		keyboard_custom.put(path+".ClickFunction."+ClickType.RIGHT.toString(), lRC);
		keyboard_custom.put(path+".ClickFunction."+ClickType.SHIFT_LEFT.toString(), lSLC);
		keyboard_custom.put(path+".ClickFunction."+ClickType.SHIFT_RIGHT.toString(), lSRC);
		path = "2"; //PlayerHead
		keyboard_custom.put(path+".SettingLevel", lSL);
		keyboard_custom.put(path+".Material", lMat);
		keyboard_custom.put(path+".PlayerSearchNum", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {2}));
		keyboard_custom.put(path+".Lore", lLo_C);
		keyboard_custom.put(path+".ClickFunction."+ClickType.LEFT.toString(), lLC);
		keyboard_custom.put(path+".ClickFunction."+ClickType.RIGHT.toString(), lRC);
		keyboard_custom.put(path+".ClickFunction."+ClickType.SHIFT_LEFT.toString(), lSLC);
		keyboard_custom.put(path+".ClickFunction."+ClickType.SHIFT_RIGHT.toString(), lSRC);
		path = "3"; //PlayerHead
		keyboard_custom.put(path+".SettingLevel", lSL);
		keyboard_custom.put(path+".Material", lMat);
		keyboard_custom.put(path+".PlayerSearchNum", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {3}));
		keyboard_custom.put(path+".Lore", lLo_C);
		keyboard_custom.put(path+".ClickFunction."+ClickType.LEFT.toString(), lLC);
		keyboard_custom.put(path+".ClickFunction."+ClickType.RIGHT.toString(), lRC);
		keyboard_custom.put(path+".ClickFunction."+ClickType.SHIFT_LEFT.toString(), lSLC);
		keyboard_custom.put(path+".ClickFunction."+ClickType.SHIFT_RIGHT.toString(), lSRC);
		path = "4"; //PlayerHead
		keyboard_custom.put(path+".SettingLevel", lSL);
		keyboard_custom.put(path+".Material", lMat);
		keyboard_custom.put(path+".PlayerSearchNum", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {4}));
		keyboard_custom.put(path+".Lore", lLo_C);
		keyboard_custom.put(path+".ClickFunction."+ClickType.LEFT.toString(), lLC);
		keyboard_custom.put(path+".ClickFunction."+ClickType.RIGHT.toString(), lRC);
		keyboard_custom.put(path+".ClickFunction."+ClickType.SHIFT_LEFT.toString(), lSLC);
		keyboard_custom.put(path+".ClickFunction."+ClickType.SHIFT_RIGHT.toString(), lSRC);
		path = "5"; //PlayerHead
		keyboard_custom.put(path+".SettingLevel", lSL);
		keyboard_custom.put(path+".Material", lMat);
		keyboard_custom.put(path+".PlayerSearchNum", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {5}));
		keyboard_custom.put(path+".Lore", lLo_C);
		keyboard_custom.put(path+".ClickFunction."+ClickType.LEFT.toString(), lLC);
		keyboard_custom.put(path+".ClickFunction."+ClickType.RIGHT.toString(), lRC);
		keyboard_custom.put(path+".ClickFunction."+ClickType.SHIFT_LEFT.toString(), lSLC);
		keyboard_custom.put(path+".ClickFunction."+ClickType.SHIFT_RIGHT.toString(), lSRC);
		path = "6"; //PlayerHead
		keyboard_custom.put(path+".SettingLevel", lSL);
		keyboard_custom.put(path+".Material", lMat);
		keyboard_custom.put(path+".PlayerSearchNum", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {6}));
		keyboard_custom.put(path+".Lore", lLo_C);
		keyboard_custom.put(path+".ClickFunction."+ClickType.LEFT.toString(), lLC);
		keyboard_custom.put(path+".ClickFunction."+ClickType.RIGHT.toString(), lRC);
		keyboard_custom.put(path+".ClickFunction."+ClickType.SHIFT_LEFT.toString(), lSLC);
		keyboard_custom.put(path+".ClickFunction."+ClickType.SHIFT_RIGHT.toString(), lSRC);
		path = "7"; //PlayerHead
		keyboard_custom.put(path+".SettingLevel", lSL);
		keyboard_custom.put(path+".Material", lMat);
		keyboard_custom.put(path+".PlayerSearchNum", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {7}));
		keyboard_custom.put(path+".Lore", lLo_C);
		keyboard_custom.put(path+".ClickFunction."+ClickType.LEFT.toString(), lLC);
		keyboard_custom.put(path+".ClickFunction."+ClickType.RIGHT.toString(), lRC);
		keyboard_custom.put(path+".ClickFunction."+ClickType.SHIFT_LEFT.toString(), lSLC);
		keyboard_custom.put(path+".ClickFunction."+ClickType.SHIFT_RIGHT.toString(), lSRC);
		path = "8"; //PlayerHead
		keyboard_custom.put(path+".SettingLevel", lSL);
		keyboard_custom.put(path+".Material", lMat);
		keyboard_custom.put(path+".PlayerSearchNum", new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {8}));
		keyboard_custom.put(path+".Lore", lLo_C);
		keyboard_custom.put(path+".ClickFunction."+ClickType.LEFT.toString(), lLC);
		keyboard_custom.put(path+".ClickFunction."+ClickType.RIGHT.toString(), lRC);
		keyboard_custom.put(path+".ClickFunction."+ClickType.SHIFT_LEFT.toString(), lSLC);
		keyboard_custom.put(path+".ClickFunction."+ClickType.SHIFT_RIGHT.toString(), lSRC);
		guiKeys.put(GuiType.KEYBOARD_CUSTOM, keyboard_custom);
	}
	
	private void initGuiSearchBuy() //INFO:GuiSearchBuy
	{
		LinkedHashMap<String, Language> sbuy = new LinkedHashMap<>();
		sbuy.put("Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&e%signshopname% &f- &e%owner%",
						"&e%signshopname% &f- &e%owner%"}));
		sbuy.put("Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"",
						"&eItems: &f%itemstoragecurrent% / %itemstoragetotal%",
						"&eKosten: &f%buyraw1%",
						"",
						"&eItems: &f%itemstoragecurrent% / %itemstoragetotal%",
						"&eCosts: &f%buyraw1%",}));
		sbuy.put("ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.SEARCH_BUY.toString()}));
		sbuy.put("ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.SEARCH_BUY.toString()}));
		guiKeys.put(GuiType.SEARCH_BUY, sbuy);
	}
	
	private void initGuiSearchSell() //INFO:GuiSearchSell
	{
		LinkedHashMap<String, Language> ssell = new LinkedHashMap<>();
		ssell.put("Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&e%signshopname% &f- &e%owner%",
						"&e%signshopname% &f- &e%owner%"}));
		ssell.put("Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"",
						"&eItems: &f%itemstoragecurrent% / %itemstoragetotal%",
						"&eKosten: &f%sellraw1%",
						"",
						"&eItems: &f%itemstoragecurrent% / %itemstoragetotal%",
						"&eCosts: &f%sellraw1%",}));
		ssell.put("ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.SEARCH_SELL.toString()}));
		ssell.put("ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.SEARCH_SELL.toString()}));
		guiKeys.put(GuiType.SEARCH_SELL, ssell);
	}
	
	private void initGuiSubscribe() //INFO:GuiSubscribe
	{
		LinkedHashMap<String, Language> subs = new LinkedHashMap<>();
		subs.put("Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&e%signshopname% &f- &e%owner%",
						"&e%signshopname% &f- &e%owner%"}));
		subs.put("Lore",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"",
						"&eMaterial: &f%material%",
						"&eDisplayname: &f%displayname%",
						"&eItems: &f%itemstoragecurrent% / %itemstoragetotal%",
						"&eKaufkosten: &f%buyraw1%",
						"&eVerkaufskosten: &f%sellraw1%",
						"",
						"&eMaterial: &f%material%",
						"&eDisplayname: &f%displayname%",
						"&eItems: &f%itemstoragecurrent% / %itemstoragetotal%",
						"&eBuycosts: &f%buyraw1%",
						"&eSellcosts: &f%sellraw1%"}));
		subs.put("ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.SUBSCRIBED.toString()}));
		subs.put("ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.SUBSCRIBED.toString()}));
		String path = "45"; //Past
		subs.put(path+".Material",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.PLAYER_HEAD.toString()}));
		subs.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"http://textures.minecraft.net/texture/e35e42fc7060c223acc965f7c5996f272644af40a4723a372f5903f8e9f188e7"}));
		subs.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&7Klicke hier für die vorherige Seite!",
						"&7Click here for the past page!"}));
		subs.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.SUBSCRIBED_PAST.toString()}));
		subs.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.SUBSCRIBED_PAST.toString()}));
		path = "53"; //Next
		subs.put(path+".Material",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						Material.PLAYER_HEAD.toString()}));
		subs.put(path+".HeadTexture",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"http://textures.minecraft.net/texture/aee0f82fb33f6cfa5169b9f5eafe4dc1c73618c9783b131adada411d8f605505"}));
		subs.put(path+".Displayname",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&7Klicke hier für die nächste Seite!",
						"&7Click here for the next page!"}));
		subs.put(path+".ClickFunction."+ClickType.LEFT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.SUBSCRIBED_NEXT.toString()}));
		subs.put(path+".ClickFunction."+ClickType.RIGHT.toString(),
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						ClickFunctionType.SUBSCRIBED_NEXT.toString()}));
		guiKeys.put(GuiType.SUBSCIBED, subs);
	}
}