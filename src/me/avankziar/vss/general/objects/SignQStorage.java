package me.avankziar.vss.general.objects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.avankziar.vss.general.database.QueryType;
import me.avankziar.vss.spigot.database.MysqlHandable;
import me.avankziar.vss.spigot.database.MysqlHandler;
import me.avankziar.vss.spigot.handler.Base64Handler;

public class SignQStorage implements MysqlHandable
{
	private int id;
	private UUID owner;
	private String signStorageName; //Default the id, later re-nameable
	private int accountId;
	private long creationDateTime;
	//item
	private ItemStack itemStack;
	private String displayName;
	private Material material;
	//Storage of the shop
	private long itemStorageTotal; //The amount how much items can hold the shop. 1 = 1 Item, 64 = 1 complete Stack
	private long itemStorageCurrent; //The amount how much items at the moment are in the shop.
	//pos of sign
	private String server;
	private String world;
	private int x;
	private int y;
	private int z;
	//admin
	private boolean unlimited;
	private String numText; //Input all number for sell/buy etc.
	private boolean signGlowing;
	private ListedType listedType;
	private boolean itemHologram;
	private long itemOutput; //How much Items per rightclick from storage called
	private long itemShiftOutput; //How much items per shift-rightclick from storage called
	private long itemInput; //How much item per leftclick to storage goes.
	private long itemShiftInput;
	
	public SignQStorage(){}
	
	public SignQStorage(int id, UUID owner, String signStorageName, int accountId, long creationDateTime,
			ItemStack itemStack, String displayName, Material mat,
			long itemStorageTotal, long itemStorageCurrent,
			String server, String world, int x, int y, int z,
			boolean unlimited,
			String numText, boolean signGlowing, ListedType listedType,
			boolean itemHologram,
			long itemOutput, long itemShiftOutput, long itemInput, long itemShiftInput)
	{
		setId(id);
		setOwner(owner);
		setSignStorageName(signStorageName);
		setAccountId(accountId);
		setCreationDateTime(creationDateTime);
		setItemStack(itemStack);
		setDisplayName(displayName);
		setMaterial(mat);
		setItemStorageTotal(itemStorageTotal);
		setItemStorageCurrent(itemStorageCurrent);
		setServer(server);
		setWorld(world);
		setX(x);
		setY(y);
		setZ(z);
		setUnlimited(unlimited);
		setNumText(numText);
		setSignGlowing(signGlowing);
		setListedType(listedType);
		setItemHologram(itemHologram);
		setItemOutput(itemOutput);
		setItemShiftOutput(itemShiftOutput);
		setItemInput(itemInput);
		setItemShiftInput(itemShiftInput);
	}
	
	private SignQStorage(int id, UUID owner, String signStorageName, int accountId, long creationDateTime,
			String itemStack, String displayName, String mat,
			long itemStorageTotal, long itemStorageCurrent,
			String server, String world, int x, int y, int z,
			boolean unlimited,
			String numText, boolean signGlowing, String listedType,
			boolean itemHover,
			long itemOutput, long itemShiftOutput, long itemInput, long itemShiftInput)
	{
		setId(id);
		setOwner(owner);
		setSignStorageName(signStorageName);
		setAccountId(accountId);
		setCreationDateTime(creationDateTime);
		setItemStack(new Base64Handler(itemStack).fromBase64());
		setDisplayName(displayName);
		setMaterial(Material.valueOf(mat));
		setItemStorageTotal(itemStorageTotal);
		setItemStorageCurrent(itemStorageCurrent);
		setServer(server);
		setWorld(world);
		setX(x);
		setY(y);
		setZ(z);
		setUnlimited(unlimited);
		setNumText(numText);
		setSignGlowing(signGlowing);
		setListedType(ListedType.valueOf(listedType));
		setItemHologram(itemHover);
		setItemOutput(itemOutput);
		setItemShiftOutput(itemShiftOutput);
		setItemInput(itemInput);
		setItemShiftInput(itemShiftInput);
	}
	
	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public UUID getOwner()
	{
		return owner;
	}

	public void setOwner(UUID owner)
	{
		this.owner = owner;
	}

	public String getSignStorageName()
	{
		return signStorageName;
	}

	public void setSignStorageName(String signStorageName)
	{
		this.signStorageName = signStorageName;
	}

	public int getAccountId()
	{
		return accountId;
	}

	public void setAccountId(int accountId)
	{
		this.accountId = accountId;
	}

	public long getCreationDateTime()
	{
		return creationDateTime;
	}

	public void setCreationDateTime(long creationDateTime)
	{
		this.creationDateTime = creationDateTime;
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

	public long getItemStorageTotal()
	{
		return itemStorageTotal;
	}

	public void setItemStorageTotal(long itemStorageTotal)
	{
		this.itemStorageTotal = itemStorageTotal;
	}

	public long getItemStorageCurrent()
	{
		return itemStorageCurrent;
	}

	public void setItemStorageCurrent(long itemStorageCurrent)
	{
		this.itemStorageCurrent = itemStorageCurrent;
	}

	public String getServer()
	{
		return server;
	}

	public void setServer(String server)
	{
		this.server = server;
	}

	public String getWorld()
	{
		return world;
	}

	public void setWorld(String world)
	{
		this.world = world;
	}

	public int getX()
	{
		return x;
	}

	public void setX(int x)
	{
		this.x = x;
	}

	public int getY()
	{
		return y;
	}

	public void setY(int y)
	{
		this.y = y;
	}

	public int getZ()
	{
		return z;
	}

	public void setZ(int z)
	{
		this.z = z;
	}

	public boolean isUnlimited() 
	{
		return unlimited;
	}

	public void setUnlimited(boolean unlimited) 
	{
		this.unlimited = unlimited;
	}

	public String getNumText()
	{
		return numText;
	}

	public void setNumText(String numText)
	{
		this.numText = numText;
	}

	public boolean isSignGlowing()
	{
		return signGlowing;
	}

	public void setSignGlowing(boolean signGlowing)
	{
		this.signGlowing = signGlowing;
	}

	public ListedType getListedType()
	{
		return listedType;
	}

	public void setListedType(ListedType listedType)
	{
		this.listedType = listedType;
	}

	public boolean isItemHologram()
	{
		return itemHologram;
	}

	public void setItemHologram(boolean itemHologram)
	{
		this.itemHologram = itemHologram;
	}

	public long getItemOutput() {
		return itemOutput;
	}

	public void setItemOutput(long itemOutput) {
		this.itemOutput = itemOutput;
	}

	public long getItemShiftOutput() {
		return itemShiftOutput;
	}

	public void setItemShiftOutput(long itemShiftOutput) {
		this.itemShiftOutput = itemShiftOutput;
	}

	public long getItemInput() {
		return itemInput;
	}

	public void setItemInput(long itemInput) {
		this.itemInput = itemInput;
	}

	public long getItemShiftInput() {
		return itemShiftInput;
	}

	public void setItemShiftInput(long itemShiftInput) {
		this.itemShiftInput = itemShiftInput;
	}

	@Override
	public boolean create(Connection conn, String tablename)
	{
		try
		{
			String sql = "INSERT INTO `" + tablename
					+ "`(`player_uuid`, `sign_storage_name`, `account_id`, `creation_date_time`, "
					+ "`itemstack_base64`, `display_name`, `material`, "
					+ "`item_storage_total`, `item_storage_current`, "
					+ "`server_name`, `world`, `x`, `y`, `z`,"
					+ "`unlimited`"
					+ "`can_buy`, `can_sell`,"
					+ "`num_text`, `sign_glowing`, `listed_type`, `item_hologram`,"
					+ "`item_output`, `item_shiftoutput`, `item_input`, `item_shiftinput`) " 
					+ "VALUES("
					+ "?, ?, ?, ?, "
					+ "?, ?, ?, "
					+ "?, ?, "
					+ "?, ?, ?, ?, ?, "
					+ "?, "
					+ "?, ?, "
					+ "?, ?, ?, ?,"
					+ "?, ?, ?, ?"
					+ ")";
			PreparedStatement ps = conn.prepareStatement(sql);
	        ps.setString(1, getOwner().toString());
	        ps.setString(2, getSignStorageName());
	        ps.setInt(3, getAccountId());
	        ps.setLong(4, getCreationDateTime());
	        ps.setString(5, new Base64Handler(getItemStack()).toBase64());
	        ps.setString(6, getDisplayName());
	        ps.setString(7, getMaterial().toString());
	        ps.setLong(8, getItemStorageTotal());
	        ps.setLong(9, getItemStorageCurrent());
	        ps.setString(10, getServer());
	        ps.setString(11, getWorld());
	        ps.setInt(12, getX());
	        ps.setInt(13, getY());
	        ps.setInt(14, getZ());
	        ps.setBoolean(15, isUnlimited());
	        ps.setString(16, getNumText());
	        ps.setBoolean(17, isSignGlowing());
	        ps.setString(18, getListedType().toString());
	        ps.setBoolean(19, isItemHologram());
	        ps.setLong(20, getItemOutput());
	        ps.setLong(21, getItemShiftOutput());
	        ps.setLong(22, getItemInput());
	        ps.setLong(23, getItemShiftInput());
	        int i = ps.executeUpdate();
	        MysqlHandler.addRows(QueryType.INSERT, i);
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
				+ "` SET `player_uuid` = ?, `sign_storage_name` = ?, `account_id` = ?, `creation_date_time` = ?,"
				+ "`itemstack_base64` = ?, `display_name` = ?, `material` = ?, "
				+ "`item_storage_total` = ?, `item_storage_current` = ?, "
				+ "`server_name` = ?, `world` = ?, `x` = ?, `y` = ?, `z` = ?, "
				+ "`unlimited` = ? "
				+ "`num_text` = ?, `sign_glowing` = ?, `listed_type` = ?, "
				+ "`item_hologram` = ?,"
				+ "`item_output` = ?, `item_shiftoutput` = ?, `item_input` = ?, `item_shiftinput` = ?" 
				+ " WHERE "+whereColumn;
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, getOwner().toString());
	        ps.setString(2, getSignStorageName());
	        ps.setInt(3, getAccountId());
	        ps.setLong(4, getCreationDateTime());
	        ps.setString(5, new Base64Handler(getItemStack()).toBase64());
	        ps.setString(6, getDisplayName());
	        ps.setString(7, getMaterial().toString());
	        ps.setLong(8, getItemStorageTotal());
	        ps.setLong(9, getItemStorageCurrent());
	        ps.setString(10, getServer());
	        ps.setString(11, getWorld());
	        ps.setInt(12, getX());
	        ps.setInt(13, getY());
	        ps.setInt(14, getZ());
	        ps.setBoolean(15, isUnlimited());
	        ps.setString(16, getNumText());
	        ps.setBoolean(17, isSignGlowing());
	        ps.setString(18, getListedType().toString());
	        ps.setBoolean(19, isItemHologram());
	        ps.setLong(20, getItemOutput());
	        ps.setLong(21, getItemShiftOutput());
	        ps.setLong(22, getItemInput());
	        ps.setLong(23, getItemShiftInput());
			int i = 24;
			for(Object o : whereObject)
			{
				ps.setObject(i, o);
				i++;
			}			
			int u = ps.executeUpdate();
			MysqlHandler.addRows(QueryType.UPDATE, u);
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
			MysqlHandler.addRows(QueryType.READ, rs.getMetaData().getColumnCount());
			ArrayList<Object> al = new ArrayList<>();
			while (rs.next()) 
			{
				al.add(new SignQStorage(rs.getInt("id"),
						UUID.fromString(rs.getString("player_uuid")),
						rs.getString("sign_storage_name"),
						rs.getInt("account_id"),
						rs.getLong("creation_date_time"),
						rs.getString("itemstack_base64"),
						rs.getString("display_name"),
						rs.getString("material"),
						rs.getLong("item_storage_total"),
						rs.getLong("item_storage_current"),
						rs.getString("server_name"),
						rs.getString("world"),
						rs.getInt("x"),
						rs.getInt("y"),
						rs.getInt("z"),
						rs.getBoolean("unlimited"),
						rs.getString("num_text"),
						rs.getBoolean("sign_glowing"),
						rs.getString("listed_type"),
						rs.getBoolean("item_hologram"),
						rs.getLong("item_output"),
						rs.getLong("item_shiftoutput"),
						rs.getLong("item_input"),
						rs.getLong("item_shiftoutput")));
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
			
			MysqlHandler.addRows(QueryType.READ, rs.getMetaData().getColumnCount());
			ArrayList<Object> al = new ArrayList<>();
			while (rs.next()) 
			{
				al.add(new SignQStorage(rs.getInt("id"),
						UUID.fromString(rs.getString("player_uuid")),
						rs.getString("sign_storage_name"),
						rs.getInt("account_id"),
						rs.getLong("creation_date_time"),
						rs.getString("itemstack_base64"),
						rs.getString("display_name"),
						rs.getString("material"),
						rs.getLong("item_storage_total"),
						rs.getLong("item_storage_current"),
						rs.getString("server_name"),
						rs.getString("world"),
						rs.getInt("x"),
						rs.getInt("y"),
						rs.getInt("z"),
						rs.getBoolean("unlimited"),
						rs.getString("num_text"),
						rs.getBoolean("sign_glowing"),
						rs.getString("listed_type"),
						rs.getBoolean("item_hologram"),
						rs.getLong("item_output"),
						rs.getLong("item_shiftoutput"),
						rs.getLong("item_input"),
						rs.getLong("item_shiftoutput")));
			}
			return al;
		} catch (SQLException e)
		{
			this.log(Level.WARNING, "SQLException! Could not get a "+this.getClass().getSimpleName()+" Object!", e);
		}
		return new ArrayList<>();
	}
	
	public static ArrayList<SignQStorage> convert(ArrayList<Object> arrayList)
	{
		ArrayList<SignQStorage> l = new ArrayList<>();
		for(Object o : arrayList)
		{
			if(o instanceof SignQStorage)
			{
				l.add((SignQStorage) o);
			}
		}
		return l;
	}
}