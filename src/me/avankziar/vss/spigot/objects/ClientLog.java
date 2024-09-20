package me.avankziar.vss.spigot.objects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.avankziar.vss.spigot.database.MysqlHandable;
import me.avankziar.vss.spigot.database.MysqlHandler;
import me.avankziar.vss.spigot.handler.Base64Handler;

public class ClientLog implements MysqlHandable
{
	public enum WayType
	{
		BUY, SELL;
	}
	
	private int id;
	private int signShopId;
	private UUID owner; //shoppinglog owner
	private long dateTime;
	private ItemStack itemStack;
	private String displayName;
	private Material material;
	private WayType wayType; //If it was from the client buyed or selled
	private double amount;
	private int itemAmount;
	
	public ClientLog(){}
	
	public ClientLog(int id, UUID owner, long dateTime,
			ItemStack itemStack, String displayName, Material material,
			WayType wayType, double amount, int itemAmount, int signShopId)
	{
		setId(id);
		setOwner(owner);
		setDateTime(dateTime);
		setItemStack(itemStack);
		setDisplayName(displayName);
		setMaterial(material);
		setWayType(wayType);
		setAmount(amount);
		setItemAmount(itemAmount);
		setSignShopId(signShopId);
	}
	
	public ClientLog(int id, UUID owner, long dateTime,
			String itemStack, String displayName, String material,
			String wayType, double amount, int itemAmount, int signShopId)
	{
		setId(id);
		setOwner(owner);
		setDateTime(dateTime);
		setItemStack(new Base64Handler(itemStack).fromBase64());
		setDisplayName(displayName);
		setMaterial(Material.valueOf(material));
		setWayType(WayType.valueOf(wayType));
		setAmount(amount);
		setItemAmount(itemAmount);
		setSignShopId(signShopId);
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

	public UUID getOwner()
	{
		return owner;
	}

	public void setOwner(UUID owner)
	{
		this.owner = owner;
	}

	public long getDateTime()
	{
		return dateTime;
	}

	public void setDateTime(long dateTime)
	{
		this.dateTime = dateTime;
	}

	public ItemStack getItemStack()
	{
		return itemStack;
	}

	public void setItemStack(ItemStack itemStack)
	{
		this.itemStack = itemStack;
	}

	public String getDisplayName()
	{
		return displayName;
	}

	public void setDisplayName(String displayName)
	{
		this.displayName = displayName;
	}

	public Material getMaterial()
	{
		return material;
	}

	public void setMaterial(Material material)
	{
		this.material = material;
	}

	public WayType getWayType()
	{
		return wayType;
	}

	public void setWayType(WayType wayType)
	{
		this.wayType = wayType;
	}

	public double getAmount()
	{
		return amount;
	}

	public void setAmount(double amount)
	{
		this.amount = amount;
	}

	public int getItemAmount()
	{
		return itemAmount;
	}

	public void setItemAmount(int itemAmount)
	{
		this.itemAmount = itemAmount;
	}
	
	@Override
	public boolean create(Connection conn, String tablename)
	{
		try
		{
			String sql = "INSERT INTO `" + tablename
					+ "`(`player_uuid`, `date_time`, "
					+ "`itemstack_base64`, `display_name`, `material`, "
					+ "`way_type`, `amount`, `item_amount`, `sign_shop_id`) " 
					+ "VALUES("
					+ "?, ?, "
					+ "?, ?, ?, "
					+ "?, ?, ?, ?"
					+ ")";
			PreparedStatement ps = conn.prepareStatement(sql);
	        ps.setString(1, getOwner().toString());
	        ps.setLong(2, getDateTime());
	        ps.setString(3, new Base64Handler(getItemStack()).toBase64());
	        ps.setString(4, getDisplayName());
	        ps.setString(5, getMaterial().toString());
	        ps.setString(6, getWayType().toString());
	        ps.setDouble(7, getAmount());
	        ps.setLong(8, getItemAmount());
	        ps.setInt(9, getSignShopId());
	        
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
				+ "` SET `player_uuid` = ?, `date_time` = ?, "
				+ "`itemstack_base64` = ?, `display_name` = ?, `material` = ?, "
				+ "`way_type` = ?, `amount` = ?, `item_amount` = ?, `sign_shop_id` = ?" 
				+ " WHERE "+whereColumn;
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, getOwner().toString());
	        ps.setLong(2, getDateTime());
	        ps.setString(3, new Base64Handler(getItemStack()).toBase64());
	        ps.setString(4, getDisplayName());
	        ps.setString(5, getMaterial().toString());
	        ps.setString(6, getWayType().toString());
	        ps.setDouble(7, getAmount());
	        ps.setInt(8, getItemAmount());
	        ps.setInt(9, getSignShopId());
			int i = 10;
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
				al.add(new ClientLog(
						rs.getInt("id"),
						UUID.fromString(rs.getString("player_uuid")),
						rs.getLong("date_time"),
						rs.getString("itemstack_base64"),
						rs.getString("display_name"),
						rs.getString("material"),
						rs.getString("way_type"),
						rs.getDouble("amount"),
						rs.getInt("item_amount"),
						rs.getInt("sign_shop_id")));
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
				al.add(new ClientLog(
						rs.getInt("id"),
						UUID.fromString(rs.getString("player_uuid")),
						rs.getLong("date_time"),
						rs.getString("itemstack_base64"),
						rs.getString("display_name"),
						rs.getString("material"),
						rs.getString("way_type"),
						rs.getDouble("amount"),
						rs.getInt("item_amount"),
						rs.getInt("sign_shop_id")));
			}
			return al;
		} catch (SQLException e)
		{
			this.log(Level.WARNING, "SQLException! Could not get a "+this.getClass().getSimpleName()+" Object!", e);
		}
		return new ArrayList<>();
	}
	
	public static ArrayList<ClientLog> convert(ArrayList<Object> arrayList)
	{
		ArrayList<ClientLog> l = new ArrayList<>();
		for(Object o : arrayList)
		{
			if(o instanceof ClientLog)
			{
				l.add((ClientLog) o);
			}
		}
		return l;
	}
}