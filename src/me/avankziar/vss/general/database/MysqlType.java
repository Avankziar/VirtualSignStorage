package me.avankziar.vss.general.database;

import me.avankziar.vss.general.objects.PlayerData;
import me.avankziar.vss.general.objects.SignStorage;
import me.avankziar.vss.general.objects.StorageAccessType;

public enum MysqlType
{
	PLAYERDATA("vssPlayerData", new PlayerData(), ServerType.ALL,
			"CREATE TABLE IF NOT EXISTS `%%tablename%%"
			+ "` (id int AUTO_INCREMENT PRIMARY KEY,"
			+ " player_uuid char(36) NOT NULL UNIQUE,"
			+ " player_name text,"
			+ " last_setting_level text,"
			+ " last_login BIGINT);"),
	SIGNSTORAGE("vssSignStorage", new SignStorage(), ServerType.SPIGOT,
			"CREATE TABLE IF NOT EXISTS `%%tablename%%"
			+ "` (id int AUTO_INCREMENT PRIMARY KEY,"
			+ " player_uuid char(36) NOT NULL,"
			+ " sign_storage_name text,"
			+ " account_id int,"
			+ " creation_date_time BIGINT,"
			+ " itemstack_base64 LONGTEXT,"
			+ " display_name text,"
			+ " material text,"
			+ " item_storage_total BIGINT,"
			+ " item_storage_current BIGINT,"
			+ " server_name text,"
			+ " world text,"
			+ " x int,"
			+ " y int,"
			+ " z int,"
			+ " unlimited boolean,"
			+ " num_text text,"
			+ " sign_glowing boolean,"
			+ " listed_type text,"
			+ " item_hologram boolean,"
			+ " item_output BIGINT,"
			+ " item_shiftoutput BIGINT,"
			+ " item_input BIGINT,"
			+ " item_shiftinput BIGINT);"),
	STORAGEACCESSTYPE("vssStorageAccessType", new StorageAccessType(), ServerType.SPIGOT,
			"CREATE TABLE IF NOT EXISTS `%%tablename%%"
			+ "` (id int AUTO_INCREMENT PRIMARY KEY,"
			+ " player_uuid char(36) NOT NULL,"
			+ " sign_storage_id int,"
			+ " listed_type text);")
	;
	
	private MysqlType(String tableName, Object object, ServerType usedOnServer, String setupQuery)
	{
		this.tableName = tableName;
		this.object = object;
		this.usedOnServer = usedOnServer;
		this.setupQuery = setupQuery.replace("%%tablename%%", tableName);
	}
	
	private final String tableName;
	private final Object object;
	private final ServerType usedOnServer;
	private final String setupQuery;

	public String getValue()
	{
		return tableName;
	}
	
	public Object getObject()
	{
		return object;
	}
	
	public ServerType getUsedOnServer()
	{
		return usedOnServer;
	}
	
	public String getSetupQuery()
	{
		return setupQuery;
	}
}