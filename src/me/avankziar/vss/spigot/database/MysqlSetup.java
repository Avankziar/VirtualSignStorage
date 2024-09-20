package me.avankziar.vss.spigot.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;

import me.avankziar.vss.spigot.VSS;

public class MysqlSetup 
{
	private String host;
	private int port;
	private String database;
	private String user;
	private String password;
	private boolean isAutoConnect;
	private boolean isVerifyServerCertificate;
	private boolean isSSLEnabled;
	
	public MysqlSetup(VSS plugin, boolean adm, String path)
	{
		if(adm)
		{
			plugin.getLogger().log(Level.INFO, "Using IFH Administration");
		}
		host = adm ? plugin.getAdministration().getHost(path)
				: plugin.getYamlHandler().getConfig().getString("Mysql.Host");
		port = adm ? plugin.getAdministration().getPort(path)
				: plugin.getYamlHandler().getConfig().getInt("Mysql.Port", 3306);
		database = adm ? plugin.getAdministration().getDatabase(path)
				: plugin.getYamlHandler().getConfig().getString("Mysql.DatabaseName");
		user = adm ? plugin.getAdministration().getUsername(path)
				: plugin.getYamlHandler().getConfig().getString("Mysql.User");
		password = adm ? plugin.getAdministration().getPassword(path)
				: plugin.getYamlHandler().getConfig().getString("Mysql.Password");
		isAutoConnect = adm ? plugin.getAdministration().isAutoReconnect(path)
				: plugin.getYamlHandler().getConfig().getBoolean("Mysql.AutoReconnect", true);
		isVerifyServerCertificate = adm ? plugin.getAdministration().isVerifyServerCertificate(path)
				: plugin.getYamlHandler().getConfig().getBoolean("Mysql.VerifyServerCertificate", false);
		isSSLEnabled = adm ? plugin.getAdministration().useSSL(path)
				: plugin.getYamlHandler().getConfig().getBoolean("Mysql.SSLEnabled", false);
		loadMysqlSetup();
	}
	
	public boolean connectToDatabase() 
	{
		VSS.log.info("Connecting to the database...");
		try
		{
			getConnection();
			VSS.log.info("Database connection successful!");
			return true;
		} catch(Exception e) 
		{
			VSS.log.log(Level.WARNING, "Could not connect to Database!", e);
			return false;
		}		
	}
	
	public Connection getConnection() throws SQLException
	{
		return reConnect();
	}
	
	private Connection reConnect() throws SQLException
	{
		boolean bool = false;
	    try
	    {
	    	// Load new Drivers for papermc
	    	Class.forName("com.mysql.cj.jdbc.Driver");
	    	bool = true;
	    } catch (Exception e)
	    {
	    	bool = false;
	    } 
	    if (bool == false)
    	{
    		// Load old Drivers for spigot
    		try
    		{
    			Class.forName("com.mysql.jdbc.Driver");
    		}  catch (Exception e) {}
    	}
        Properties properties = new Properties();
        properties.setProperty("user", user);
        properties.setProperty("password", password);
        properties.setProperty("autoReconnect", String.valueOf(isAutoConnect));
        properties.setProperty("verifyServerCertificate", String.valueOf(isVerifyServerCertificate));
        properties.setProperty("useSSL", String.valueOf(isSSLEnabled));
        properties.setProperty("requireSSL", String.valueOf(isSSLEnabled));
        //Connect to database
        Connection conn = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, properties);
        return conn;
	}
	
	private boolean baseSetup(String data) 
	{
		try (Connection conn = getConnection(); PreparedStatement query = conn.prepareStatement(data))
		{
			query.execute();
		} catch (SQLException e) 
		{
			VSS.log.log(Level.WARNING, "Could not build data source. Or connection is null", e);
		}
		return true;
	}
	
	public boolean loadMysqlSetup()
	{
		if(!connectToDatabase())
		{
			return false;
		}
		if(!setupDatabaseI())
		{
			return false;
		}
		if(!setupDatabaseII())
		{
			return false;
		}
		if(!setupDatabaseIII())
		{
			return false;
		}
		if(!setupDatabaseIV())
		{
			return false;
		}
		if(!setupDatabaseV())
		{
			return false;
		}
		if(!setupDatabaseVI())
		{
			return false;
		}
		if(!setupDatabaseVII())
		{
			return false;
		}
		if(!setupDatabaseVIII())
		{
			return false;
		}
		return true;
	}
	
	public boolean setupDatabaseI() 
	{
		String data = "CREATE TABLE IF NOT EXISTS `" + MysqlHandler.Type.PLAYERDATA.getValue()
		+ "` (id int AUTO_INCREMENT PRIMARY KEY,"
		+ " player_uuid char(36) NOT NULL UNIQUE,"
		+ " player_name varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,"
		+ " last_setting_level text,"
		+ " last_login BIGINT);";
		baseSetup(data);
		return true;
	}
	
	public boolean setupDatabaseII() 
	{
		String data = "CREATE TABLE IF NOT EXISTS `" + MysqlHandler.Type.SIGNSHOP.getValue()
		+ "` (id int AUTO_INCREMENT PRIMARY KEY,"
		+ " player_uuid char(36) NOT NULL,"
		+ " sign_shop_name text,"
		+ " account_id int,"
		+ " creation_date_time BIGINT,"
		+ " itemstack_base64 LONGTEXT,"
		+ " display_name text,"
		+ " material text,"
		+ " item_storage_total BIGINT,"
		+ " item_storage_current BIGINT,"
		+ " buy_amount double,"
		+ " sell_amount double,"
		+ " possible_buy BIGINT,"
		+ " possible_sell BIGINT,"
		+ " discount_start BIGINT,"
		+ " discount_end BIGINT,"
		+ " discount_buy_amount double,"
		+ " discount_sell_amount double,"
		+ " discount_possible_buy BIGINT,"
		+ " discount_possible_sell BIGINT,"
		+ " server_name text,"
		+ " world text,"
		+ " x int,"
		+ " y int,"
		+ " z int,"
		+ " storage_id int,"
		+ " unlimited_buy boolean,"
		+ " unlimited_sell boolean,"
		+ " can_buy boolean,"
		+ " can_sell boolean,"
		+ " num_text text,"
		+ " sign_glowing boolean,"
		+ " listed_type text,"
		+ " item_hologram boolean);";
		baseSetup(data);
		return true;
	}
	
	public boolean setupDatabaseIII() 
	{
		String data = "CREATE TABLE IF NOT EXISTS `" + MysqlHandler.Type.SIGNSHOPLOG.getValue()
		+ "` (id int AUTO_INCREMENT PRIMARY KEY,"
		+ " sign_shop_id int,"
		+ " player_uuid text,"
		+ " date_time BIGINT,"
		+ " itemstack_base64 LONGTEXT,"
		+ " display_name text,"
		+ " material text,"
		+ " way_type text,"
		+ " amount double,"
		+ " item_amount int,"
		+ " client text);";
		baseSetup(data);
		return true;
	}
	
	public boolean setupDatabaseIV() 
	{
		String data = "CREATE TABLE IF NOT EXISTS `" + MysqlHandler.Type.SIGNSHOPDAILYLOG.getValue()
		+ "` (id int AUTO_INCREMENT PRIMARY KEY,"
		+ " sign_shop_id int,"
		+ " player_uuid text,"
		+ " dates BIGINT,"
		+ " buy_amount double,"
		+ " sell_amount double,"
		+ " buy_item_amount BIGINT,"
		+ " sell_item_amount BIGINT);";
		baseSetup(data);
		return true;
	}
	
	public boolean setupDatabaseV() 
	{
		String data = "CREATE TABLE IF NOT EXISTS `" + MysqlHandler.Type.CLIENTLOG.getValue()
		+ "` (id int AUTO_INCREMENT PRIMARY KEY,"
		+ " player_uuid char(36) NOT NULL,"
		+ " date_time BIGINT,"
		+ " itemstack_base64 LONGTEXT,"
		+ " display_name text,"
		+ " material text,"
		+ " way_type text,"
		+ " amount double,"
		+ " item_amount int,"
		+ " sign_shop_id int);";
		baseSetup(data);
		return true;
	}
	
	public boolean setupDatabaseVI() 
	{
		String data = "CREATE TABLE IF NOT EXISTS `" + MysqlHandler.Type.CLIENTDAILYLOG.getValue()
		+ "` (id int AUTO_INCREMENT PRIMARY KEY,"
		+ " player_uuid char(36) NOT NULL,"
		+ " dates BIGINT,"
		+ " buy_amount double,"
		+ " sell_amount double,"
		+ " buy_item_amount BIGINT,"
		+ " sell_item_amount BIGINT);";
		baseSetup(data);
		return true;
	}
	
	public boolean setupDatabaseVII() 
	{
		String data = "CREATE TABLE IF NOT EXISTS `" + MysqlHandler.Type.SUBSCRIBEDSHOP.getValue()
		+ "` (id int AUTO_INCREMENT PRIMARY KEY,"
		+ " player_uuid char(36) NOT NULL,"
		+ " sign_shop_id int,"
		+ " subscribed_date_time BIGINT);";
		baseSetup(data);
		return true;
	}
	
	public boolean setupDatabaseVIII() 
	{
		String data = "CREATE TABLE IF NOT EXISTS `" + MysqlHandler.Type.SHOPACCESSTYPE.getValue()
		+ "` (id int AUTO_INCREMENT PRIMARY KEY,"
		+ " player_uuid char(36) NOT NULL,"
		+ " sign_shop_id int,"
		+ " listed_type text);";
		baseSetup(data);
		return true;
	}
}