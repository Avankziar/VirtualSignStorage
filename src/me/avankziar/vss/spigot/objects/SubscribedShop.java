package me.avankziar.vss.spigot.objects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;

import me.avankziar.vss.spigot.database.MysqlHandable;
import me.avankziar.vss.spigot.database.MysqlHandler;

public class SubscribedShop implements MysqlHandable
{
	private int id;
	private UUID player;
	private int signShopId;
	private long subscribedDateTime;
	
	public SubscribedShop(){}
	
	public SubscribedShop(int id, UUID player, int signShopId, long subscribedDateTime)
	{
		setId(id);
		setPlayer(player);
		setSignShopId(signShopId);
		setSubscribedDateTime(subscribedDateTime);
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public UUID getPlayer()
	{
		return player;
	}

	public void setPlayer(UUID player)
	{
		this.player = player;
	}

	public int getSignShopId()
	{
		return signShopId;
	}

	public void setSignShopId(int signShopId)
	{
		this.signShopId = signShopId;
	}

	public long getSubscribedDateTime()
	{
		return subscribedDateTime;
	}

	public void setSubscribedDateTime(long subscribedDateTime)
	{
		this.subscribedDateTime = subscribedDateTime;
	}
	
	@Override
	public boolean create(Connection conn, String tablename)
	{
		try
		{
			String sql = "INSERT INTO `" + tablename
					+ "`(`player_uuid`, `sign_shop_id`, `subscribed_date_time`) " 
					+ "VALUES("
					+ "?, ?, ?"
					+ ")";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, getPlayer().toString());
	        ps.setInt(2, getSignShopId());
	        ps.setLong(3, getSubscribedDateTime());
	        
	        int i = ps.executeUpdate();
	        MysqlHandler.addRows(MysqlHandler.QueryType.INSERT, i);
	        return true;
		} catch (SQLException e)
		{
			this.log(Level.WARNING, "SQLException! Could not create a "+this.getClass().getSimpleName()+" Object!", e);
		}
		return false;
	}

	@Override
	public boolean update(Connection conn, String tablename, String whereColumn, Object... whereObject)
	{
		try
		{
			String sql = "UPDATE `" + tablename
				+ "` SET `player_uuid` = ?, `sign_shop_id` = ?, `subscribed_date_time` = ?" 
				+ " WHERE "+whereColumn;
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, getPlayer().toString());
	        ps.setInt(2, getSignShopId());
	        ps.setLong(3, getSubscribedDateTime());
			int i = 4;
			for(Object o : whereObject)
			{
				ps.setObject(i, o);
				i++;
			}			
			int u = ps.executeUpdate();
			MysqlHandler.addRows(MysqlHandler.QueryType.UPDATE, u);
			return true;
		} catch (SQLException e)
		{
			this.log(Level.WARNING, "SQLException! Could not update a "+this.getClass().getSimpleName()+" Object!", e);
		}
		return false;
	}

	@Override
	public ArrayList<Object> get(Connection conn, String tablename, String orderby, String limit, String whereColumn, Object... whereObject)
	{
		try
		{
			String sql = "SELECT * FROM `" + tablename
				+ "` WHERE "+whereColumn+" ORDER BY "+orderby+limit;
			PreparedStatement ps = conn.prepareStatement(sql);
			int i = 1;
			for(Object o : whereObject)
			{
				ps.setObject(i, o);
				i++;
			}
			
			ResultSet rs = ps.executeQuery();
			MysqlHandler.addRows(MysqlHandler.QueryType.READ, rs.getMetaData().getColumnCount());
			ArrayList<Object> al = new ArrayList<>();
			while (rs.next()) 
			{
				al.add(new SubscribedShop(
						rs.getInt("id"),
						UUID.fromString(rs.getString("player_uuid")),
						rs.getInt("sign_shop_id"),
						rs.getLong("subscribed_date_time")));
			}
			return al;
		} catch (SQLException e)
		{
			this.log(Level.WARNING, "SQLException! Could not get a "+this.getClass().getSimpleName()+" Object!", e);
		}
		return new ArrayList<>();
	}
	
	@Override
	public ArrayList<Object> get(Connection conn, String tablename, String sql, Object... whereObject)
	{
		try
		{
			PreparedStatement ps = conn.prepareStatement(sql);
			int i = 1;
			for(Object o : whereObject)
			{
				ps.setObject(i, o);
				i++;
			}
			
			ResultSet rs = ps.executeQuery();
			MysqlHandler.addRows(MysqlHandler.QueryType.READ, rs.getMetaData().getColumnCount());
			ArrayList<Object> al = new ArrayList<>();
			while (rs.next()) 
			{
				al.add(new SubscribedShop(
						rs.getInt("id"),
						UUID.fromString(rs.getString("player_uuid")),
						rs.getInt("sign_shop_id"),
						rs.getLong("subscribed_date_time")));
			}
			return al;
		} catch (SQLException e)
		{
			this.log(Level.WARNING, "SQLException! Could not get a "+this.getClass().getSimpleName()+" Object!", e);
		}
		return new ArrayList<>();
	}
	
	public static ArrayList<SubscribedShop> convert(ArrayList<Object> arrayList)
	{
		ArrayList<SubscribedShop> l = new ArrayList<>();
		for(Object o : arrayList)
		{
			if(o instanceof SubscribedShop)
			{
				l.add((SubscribedShop) o);
			}
		}
		return l;
	}
}