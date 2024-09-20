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

public class SignShopDailyLog implements MysqlHandable
{
	private int id;
	private int signShopId;
	private long date; //The date dd.mm.yyyy of the log
	private double buyAmount; //money amount of client buying items
	private double sellAmount; //money amount of client sell;
	private int buyItemAmount; //amount items of client buying
	private int sellItemAmount; //amount item of client selling
	private UUID player;
	
	public SignShopDailyLog(){}
	
	public SignShopDailyLog(int id, int signShopId,
			long date, double buyAmount, double sellAmount, int buyItemAmount, int sellItemAmount, UUID player)
	{
		setId(id);
		setSignShopId(signShopId);
		setDate(date);
		setBuyAmount(buyAmount);
		setSellAmount(sellAmount);
		setBuyItemAmount(buyItemAmount);
		setSellItemAmount(sellItemAmount);
		setPlayer(player);
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public int getSignShopId()
	{
		return signShopId;
	}

	public void setSignShopId(int signShopId)
	{
		this.signShopId = signShopId;
	}

	public long getDate()
	{
		return date;
	}

	public void setDate(long date)
	{
		this.date = date;
	}

	public double getBuyAmount()
	{
		return buyAmount;
	}

	public void setBuyAmount(double buyAmount)
	{
		this.buyAmount = buyAmount;
	}

	public double getSellAmount()
	{
		return sellAmount;
	}

	public void setSellAmount(double sellAmount)
	{
		this.sellAmount = sellAmount;
	}

	public int getBuyItemAmount()
	{
		return buyItemAmount;
	}

	public void setBuyItemAmount(int buyItemAmount)
	{
		this.buyItemAmount = buyItemAmount;
	}

	public int getSellItemAmount()
	{
		return sellItemAmount;
	}

	public void setSellItemAmount(int sellItemAmount)
	{
		this.sellItemAmount = sellItemAmount;
	}
	
	public UUID getPlayer()
	{
		return player;
	}

	public void setPlayer(UUID player)
	{
		this.player = player;
	}

	@Override
	public boolean create(Connection conn, String tablename)
	{
		try
		{
			String sql = "INSERT INTO `" + tablename
					+ "`(`sign_shop_id`, `dates`, "
					+ "`buy_amount`, `sell_amount`, "
					+ "`buy_item_amount`, `sell_item_amount`, `player_uuid`) " 
					+ "VALUES("
					+ "?, ?, "
					+ "?, ?, "
					+ "?, ?, ?"
					+ ")";
			PreparedStatement ps = conn.prepareStatement(sql);
	        ps.setInt(1, getSignShopId());
	        ps.setLong(2, getDate());
	        ps.setDouble(3, getBuyAmount());
	        ps.setDouble(4, getSellAmount());
	        ps.setInt(5, getBuyItemAmount());
	        ps.setInt(6, getSellItemAmount());
	        ps.setString(7, getPlayer().toString());
	        
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
				+ "` SET `sign_shop_id` = ?, `dates` = ?, "
				+ "`buy_amount` = ?, `sell_amount` = ?, "
				+ "`buy_item_amount` = ?, `sell_item_amount` = ?, `player_uuid` = ?" 
				+ " WHERE "+whereColumn;
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, getSignShopId());
	        ps.setLong(2, getDate());
	        ps.setDouble(3, getBuyAmount());
	        ps.setDouble(4, getSellAmount());
	        ps.setInt(5, getBuyItemAmount());
	        ps.setInt(6, getSellItemAmount());
	        ps.setString(7, getPlayer().toString());
			int i = 8;
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
				al.add(new SignShopDailyLog(
						rs.getInt("id"),
						rs.getInt("sign_shop_id"),
						rs.getLong("dates"),
						rs.getDouble("buy_amount"),
						rs.getDouble("sell_amount"),
						rs.getInt("buy_item_amount"),
						rs.getInt("sell_item_amount"),
						UUID.fromString(rs.getString("player_uuid"))));
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
				al.add(new SignShopDailyLog(
						rs.getInt("id"),
						rs.getInt("sign_shop_id"),
						rs.getLong("dates"),
						rs.getDouble("buy_amount"),
						rs.getDouble("sell_amount"),
						rs.getInt("buy_item_amount"),
						rs.getInt("sell_item_amount"),
						UUID.fromString(rs.getString("player_uuid"))));
			}
			return al;
		} catch (SQLException e)
		{
			this.log(Level.WARNING, "SQLException! Could not get a "+this.getClass().getSimpleName()+" Object!", e);
		}
		return new ArrayList<>();
	}
	
	public static ArrayList<SignShopDailyLog> convert(ArrayList<Object> arrayList)
	{
		ArrayList<SignShopDailyLog> l = new ArrayList<>();
		for(Object o : arrayList)
		{
			if(o instanceof SignShopDailyLog)
			{
				l.add((SignShopDailyLog) o);
			}
		}
		return l;
	}
}