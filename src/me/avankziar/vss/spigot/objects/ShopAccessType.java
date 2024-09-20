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

public class ShopAccessType implements MysqlHandable
{
	private int id;
	private int signShopID;
	private UUID uUID;
	private ListedType listedType;
	
	public ShopAccessType()
	{
		
	}
	
	public ShopAccessType(int id, int signShopID, UUID uUID, ListedType listedType)
	{
		setId(id);
		setSignShopID(signShopID);
		setUUID(uUID);
		setListedType(listedType);
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public int getSignShopID()
	{
		return signShopID;
	}

	public void setSignShopID(int signShopID)
	{
		this.signShopID = signShopID;
	}

	public UUID getUUID()
	{
		return uUID;
	}

	public void setUUID(UUID uUID)
	{
		this.uUID = uUID;
	}

	public ListedType getListedType()
	{
		return listedType;
	}

	public void setListedType(ListedType listedType)
	{
		this.listedType = listedType;
	}
	
	@Override
	public boolean create(Connection conn, String tablename)
	{
		try
		{
			String sql = "INSERT INTO `" + tablename
					+ "`(`player_uuid`, `sign_shop_id`, `listed_type`) " 
					+ "VALUES("
					+ "?, ?, ?"
					+ ")";
			PreparedStatement ps = conn.prepareStatement(sql);
	        ps.setString(1, getUUID().toString());
	        ps.setInt(2, getSignShopID());
	        ps.setString(3, getListedType().toString());
	        
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
				+ "` SET `player_uuid` = ?, `sign_shop_id` = ?, `listed_type` = ?"
				+ " WHERE "+whereColumn;
			PreparedStatement ps = conn.prepareStatement(sql);
		    ps.setString(1, getUUID().toString());
		    ps.setInt(2, getSignShopID());
		    ps.setString(3, getListedType().toString());
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
				al.add(new ShopAccessType(rs.getInt("id"),
						rs.getInt("sign_shop_id"),
						UUID.fromString(rs.getString("player_uuid")),
						ListedType.valueOf(rs.getString("listed_type"))));
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
				al.add(new ShopAccessType(rs.getInt("id"),
						rs.getInt("sign_shop_id"),
						UUID.fromString(rs.getString("player_uuid")),
						ListedType.valueOf(rs.getString("listed_type"))));
			}
			return al;
		} catch (SQLException e)
		{
			this.log(Level.WARNING, "SQLException! Could not get a "+this.getClass().getSimpleName()+" Object!", e);
		}
		return new ArrayList<>();
	}
	
	public static ArrayList<ShopAccessType> convert(ArrayList<Object> arrayList)
	{
		ArrayList<ShopAccessType> l = new ArrayList<>();
		for(Object o : arrayList)
		{
			if(o instanceof ShopAccessType)
			{
				l.add((ShopAccessType) o);
			}
		}
		return l;
	}
}