package me.avankziar.vss.spigot.database;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.logging.Level;

import me.avankziar.vss.spigot.VSS;

public interface MysqlHandable
{
	public boolean create(Connection conn, String tablename);
	
	public boolean update(Connection conn, String tablename, String whereColumn, Object... whereObject);
	
	public ArrayList<Object> get(Connection conn, String tablename, String orderby, String limit, String whereColumn, Object... whereObject);
	
	public ArrayList<Object> get(Connection conn, String tablename, String sql, Object... whereObject);
	
	default void log(Level level, String log, Exception e)
	{
		VSS.log.log(level, log, e);
	}
}