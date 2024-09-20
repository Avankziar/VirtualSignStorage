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

public class SignShop implements MysqlHandable
{
	private int id;
	private UUID owner;
	private String signShopName; //Default the id, later re-nameable
	private int accountId;
	private long creationDateTime;
	//item
	private ItemStack itemStack;
	private String displayName;
	private Material material;
	//Storage of the shop
	private long itemStorageTotal; //The amount how much items can hold the shop. 1 = 1 Item, 64 = 1 complete Stack
	private long itemStorageCurrent; //The amount how much items at the moment are in the shop.
	//Buy and sell
	private Double buyAmount; //How much the client must pay to buy 1 item. If Null no buying possible
	private Double sellAmount; //How much the client receive by sell 1 item. If Null no selling possible
	private long possibleBuy; //How much items are possible to buy. If < 0 == infinite (how much items are available)
	private long possibleSell; //How much item are possible to sell. If < 0 == infinite (how much storage is available)
	//Discount
	private long discountStart; //When the discount starts.
	private long discountEnd; //When the discount ends.
	private Double discountBuyAmount; //How much the client must pay to buy 1 item if the discount is active. If Null no buying possible
	private Double discountSellAmount; //How much the client receive by sell 1 item if the discount is active. If Null no selling possible
	private long discountPossibleBuy; //How much items are possible to buy. If < 0 == infinite (how much items are available) (Applies only on discount)
	private long discountPossibleSell; //How much item are possible to sell. If < 0 == infinite (how much storage is available) (Applies only on discount)
	//pos of sign
	private String server;
	private String world;
	private int x;
	private int y;
	private int z;
	//Ash
	private int storageID; //ID of the distributionchest
	//admin
	private boolean unlimitedBuy;
	private boolean unlimitedSell;
	private boolean canBuy;
	private boolean canSell;
	private String numText; //Input all number for sell/buy etc.
	private boolean signGlowing;
	private ListedType listedType;
	private boolean itemHologram;
	
	public SignShop(){}
	
	public SignShop(int id, UUID owner, String signShopName, int accountId, long creationDateTime,
			ItemStack itemStack, String displayName, Material mat,
			long itemStorageTotal, long itemStorageCurrent,
			Double buyAmount, Double sellAmount, long possibleBuy, long possibleSell,
			long discountStart, long discountEnd, Double discountBuyAmount, Double discountSellAmount,
			long discountPossibleBuy, long discountPossibleSell,
			String server, String world, int x, int y, int z, int storageId,
			boolean unlimitedBuy, boolean unlimitedSell,
			boolean canBuy, boolean canSell, String numText, boolean signGlowing, ListedType listedType,
			boolean itemHologram)
	{
		setId(id);
		setOwner(owner);
		setSignShopName(signShopName);
		setAccountId(accountId);
		setCreationDateTime(creationDateTime);
		setItemStack(itemStack);
		setDisplayName(displayName);
		setMaterial(mat);
		setItemStorageTotal(itemStorageTotal);
		setItemStorageCurrent(itemStorageCurrent);
		setBuyAmount(buyAmount);
		setSellAmount(sellAmount);
		setPossibleBuy(possibleBuy);
		setPossibleSell(possibleSell);
		setDiscountStart(discountStart);
		setDiscountEnd(discountEnd);
		setDiscountBuyAmount(discountBuyAmount);
		setDiscountSellAmount(discountSellAmount);
		setDiscountPossibleBuy(discountPossibleBuy);
		setDiscountPossibleSell(discountPossibleSell);
		setServer(server);
		setWorld(world);
		setX(x);
		setY(y);
		setZ(z);
		setStorageID(storageId);
		setUnlimitedBuy(unlimitedBuy);
		setUnlimitedSell(unlimitedSell);
		setCanBuy(canBuy);
		setCanSell(canSell);
		setNumText(numText);
		setSignGlowing(signGlowing);
		setListedType(listedType);
		setItemHologram(itemHologram);
	}
	
	private SignShop(int id, UUID owner, String signShopName, int accountId, long creationDateTime,
			String itemStack, String displayName, String mat,
			long itemStorageTotal, long itemStorageCurrent,
			Double buyAmount, Double sellAmount, long possibleBuy, long possibleSell,
			long discountStart, long discountEnd, Double discountBuyAmount, Double discountSellAmount,
			long discountPossibleBuy, long discountPossibleSell,
			String server, String world, int x, int y, int z, int storageId,
			boolean unlimitedBuy, boolean unlimitedSell,
			boolean canBuy, boolean canSell, String numText, boolean signGlowing, String listedType,
			boolean itemHover)
	{
		setId(id);
		setOwner(owner);
		setSignShopName(signShopName);
		setAccountId(accountId);
		setCreationDateTime(creationDateTime);
		setItemStack(new Base64Handler(itemStack).fromBase64());
		setDisplayName(displayName);
		setMaterial(Material.valueOf(mat));
		setItemStorageTotal(itemStorageTotal);
		setItemStorageCurrent(itemStorageCurrent);
		setBuyAmount(buyAmount);
		setSellAmount(sellAmount);
		setPossibleBuy(possibleBuy);
		setPossibleSell(possibleSell);
		setDiscountStart(discountStart);
		setDiscountEnd(discountEnd);
		setDiscountBuyAmount(discountBuyAmount);
		setDiscountSellAmount(discountSellAmount);
		setDiscountPossibleBuy(discountPossibleBuy);
		setDiscountPossibleSell(discountPossibleSell);
		setServer(server);
		setWorld(world);
		setX(x);
		setY(y);
		setZ(z);
		setStorageID(storageId);
		setUnlimitedBuy(unlimitedBuy);
		setUnlimitedSell(unlimitedSell);
		setCanBuy(canBuy);
		setCanSell(canSell);
		setNumText(numText);
		setSignGlowing(signGlowing);
		setListedType(ListedType.valueOf(listedType));
		setItemHologram(itemHover);
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

	public String getSignShopName()
	{
		return signShopName;
	}

	public void setSignShopName(String signShopName)
	{
		this.signShopName = signShopName;
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

	public Double getBuyAmount()
	{
		return buyAmount;
	}

	public void setBuyAmount(Double buyAmount)
	{
		this.buyAmount = buyAmount;
	}

	public Double getSellAmount()
	{
		return sellAmount;
	}

	public void setSellAmount(Double sellAmount)
	{
		this.sellAmount = sellAmount;
	}

	public long getPossibleBuy()
	{
		return possibleBuy;
	}

	public void setPossibleBuy(long possibleBuy)
	{
		this.possibleBuy = possibleBuy;
	}

	public long getPossibleSell()
	{
		return possibleSell;
	}

	public void setPossibleSell(long possibleSell)
	{
		this.possibleSell = possibleSell;
	}

	public long getDiscountStart()
	{
		return discountStart;
	}

	public void setDiscountStart(long discountStart)
	{
		this.discountStart = discountStart;
	}

	public long getDiscountEnd()
	{
		return discountEnd;
	}

	public void setDiscountEnd(long discountEnd)
	{
		this.discountEnd = discountEnd;
	}

	public Double getDiscountBuyAmount()
	{
		return discountBuyAmount;
	}

	public void setDiscountBuyAmount(Double discountBuyAmount)
	{
		this.discountBuyAmount = discountBuyAmount;
	}

	public Double getDiscountSellAmount()
	{
		return discountSellAmount;
	}

	public void setDiscountSellAmount(Double discountSellAmount)
	{
		this.discountSellAmount = discountSellAmount;
	}

	public long getDiscountPossibleBuy()
	{
		return discountPossibleBuy;
	}

	public void setDiscountPossibleBuy(long discountPossibleBuy)
	{
		this.discountPossibleBuy = discountPossibleBuy;
	}

	public long getDiscountPossibleSell()
	{
		return discountPossibleSell;
	}

	public void setDiscountPossibleSell(long discountPossibleSell)
	{
		this.discountPossibleSell = discountPossibleSell;
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

	public int getStorageID()
	{
		return storageID;
	}

	public void setStorageID(int storageID)
	{
		this.storageID = storageID;
	}

	public boolean isUnlimitedBuy()
	{
		return unlimitedBuy;
	}

	public void setUnlimitedBuy(boolean unlimitedBuy)
	{
		this.unlimitedBuy = unlimitedBuy;
	}

	public boolean isUnlimitedSell()
	{
		return unlimitedSell;
	}

	public void setUnlimitedSell(boolean unlimitedSell)
	{
		this.unlimitedSell = unlimitedSell;
	}

	public boolean canBuy()
	{
		return canBuy;
	}

	public void setCanBuy(boolean canBuy)
	{
		this.canBuy = canBuy;
	}

	public boolean canSell()
	{
		return canSell;
	}

	public void setCanSell(boolean canSell)
	{
		this.canSell = canSell;
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

	@Override
	public boolean create(Connection conn, String tablename)
	{
		try
		{
			String sql = "INSERT INTO `" + tablename
					+ "`(`player_uuid`, `sign_shop_name`, `account_id`, `creation_date_time`, "
					+ "`itemstack_base64`, `display_name`, `material`, "
					+ "`item_storage_total`, `item_storage_current`, "
					+ "`buy_amount`, `sell_amount`, `possible_buy`, `possible_sell`, "
					+ "`discount_start`, `discount_end`, `discount_buy_amount`, `discount_sell_amount`, "
					+ "`discount_possible_buy`, `discount_possible_sell`, "
					+ "`server_name`, `world`, `x`, `y`, `z`,"
					+ "`storage_id`,"
					+ "`unlimited_buy`, `unlimited_sell`,"
					+ "`can_buy`, `can_sell`,"
					+ "`num_text`, `sign_glowing`, `listed_type`, `item_hologram`) " 
					+ "VALUES("
					+ "?, ?, ?, ?, "
					+ "?, ?, ?, "
					+ "?, ?, "
					+ "?, ?, ?, ?, "
					+ "?, ?, ?, ?, "
					+ "?, ?, "
					+ "?, ?, ?, ?, ?, "
					+ "?,"
					+ "?, ?,"
					+ "?, ?, "
					+ "?, ?, ?, ?"
					+ ")";
			PreparedStatement ps = conn.prepareStatement(sql);
	        ps.setString(1, getOwner().toString());
	        ps.setString(2, getSignShopName());
	        ps.setInt(3, getAccountId());
	        ps.setLong(4, getCreationDateTime());
	        ps.setString(5, new Base64Handler(getItemStack()).toBase64());
	        ps.setString(6, getDisplayName());
	        ps.setString(7, getMaterial().toString());
	        ps.setLong(8, getItemStorageTotal());
	        ps.setLong(9, getItemStorageCurrent());
	        ps.setDouble(10, getBuyAmount());
	        ps.setDouble(11, getSellAmount());
	        ps.setLong(12, getPossibleBuy());
	        ps.setLong(13, getPossibleSell());
	        ps.setLong(14, getDiscountStart());
	        ps.setLong(15, getDiscountEnd());
	        ps.setDouble(16, getDiscountBuyAmount());
	        ps.setDouble(17, getDiscountSellAmount());
	        ps.setLong(18, getDiscountPossibleBuy());
	        ps.setLong(19, getDiscountPossibleSell());
	        ps.setString(20, getServer());
	        ps.setString(21, getWorld());
	        ps.setInt(22, getX());
	        ps.setInt(23, getY());
	        ps.setInt(24, getZ());
	        ps.setInt(25, getStorageID());
	        ps.setBoolean(26, isUnlimitedBuy());
	        ps.setBoolean(27, isUnlimitedSell());
	        ps.setBoolean(28, canBuy());
	        ps.setBoolean(29, canSell());
	        ps.setString(30, getNumText());
	        ps.setBoolean(31, isSignGlowing());
	        ps.setString(32, getListedType().toString());
	        ps.setBoolean(33, isItemHologram());
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
				+ "` SET `player_uuid` = ?, `sign_shop_name` = ?, `account_id` = ?, `creation_date_time` = ?,"
				+ "`itemstack_base64` = ?, `display_name` = ?, `material` = ?, "
				+ "`item_storage_total` = ?, `item_storage_current` = ?, "
				+ "`buy_amount` = ?, `sell_amount` = ?, `possible_buy` = ?, `possible_sell` = ?, "
				+ "`discount_start` = ?, `discount_end` = ?, `discount_buy_amount` = ?, `discount_sell_amount` = ?, "
				+ "`discount_possible_buy` = ?, `discount_possible_sell` = ?, "
				+ "`server_name` = ?, `world` = ?, `x` = ?, `y` = ?, `z` = ?, "
				+ "`storage_id` = ?, `unlimited_buy` = ?, `unlimited_sell` = ?, "
				+ "`can_buy` = ?, `can_sell` = ?, `num_text` = ?, `sign_glowing` = ?, `listed_type` = ?, "
				+ "`item_hologram` = ?" 
				+ " WHERE "+whereColumn;
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, getOwner().toString());
	        ps.setString(2, getSignShopName());
	        ps.setInt(3, getAccountId());
	        ps.setLong(4, getCreationDateTime());
	        ps.setString(5, new Base64Handler(getItemStack()).toBase64());
	        ps.setString(6, getDisplayName());
	        ps.setString(7, getMaterial().toString());
	        ps.setLong(8, getItemStorageTotal());
	        ps.setLong(9, getItemStorageCurrent());
	        ps.setDouble(10, getBuyAmount());
	        ps.setDouble(11, getSellAmount());
	        ps.setLong(12, getPossibleBuy());
	        ps.setLong(13, getPossibleSell());
	        ps.setLong(14, getDiscountStart());
	        ps.setLong(15, getDiscountEnd());
	        ps.setDouble(16, getDiscountBuyAmount());
	        ps.setDouble(17, getDiscountSellAmount());
	        ps.setLong(18, getDiscountPossibleBuy());
	        ps.setLong(19, getDiscountPossibleSell());
	        ps.setString(20, getServer());
	        ps.setString(21, getWorld());
	        ps.setInt(22, getX());
	        ps.setInt(23, getY());
	        ps.setInt(24, getZ());
	        ps.setInt(25, getStorageID());
	        ps.setBoolean(26, isUnlimitedBuy());
	        ps.setBoolean(27, isUnlimitedSell());
	        ps.setBoolean(28, canBuy());
	        ps.setBoolean(29, canSell());
	        ps.setString(30, getNumText());
	        ps.setBoolean(31, isSignGlowing());
	        ps.setString(32, getListedType().toString());
	        ps.setBoolean(33, isItemHologram());
			int i = 34;
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
				al.add(new SignShop(rs.getInt("id"),
						UUID.fromString(rs.getString("player_uuid")),
						rs.getString("sign_shop_name"),
						rs.getInt("account_id"),
						rs.getLong("creation_date_time"),
						rs.getString("itemstack_base64"),
						rs.getString("display_name"),
						rs.getString("material"),
						rs.getLong("item_storage_total"),
						rs.getLong("item_storage_current"),
						rs.getDouble("buy_amount"),
						rs.getDouble("sell_amount"),
						rs.getLong("possible_buy"),
						rs.getLong("possible_sell"),
						rs.getLong("discount_start"),
						rs.getLong("discount_end"),
						rs.getDouble("discount_buy_amount"),
						rs.getDouble("discount_sell_amount"),
						rs.getLong("discount_possible_buy"),
						rs.getLong("discount_possible_sell"),
						rs.getString("server_name"),
						rs.getString("world"),
						rs.getInt("x"),
						rs.getInt("y"),
						rs.getInt("z"),
						rs.getInt("storage_id"),
						rs.getBoolean("unlimited_buy"),
						rs.getBoolean("unlimited_sell"),
						rs.getBoolean("can_buy"),
						rs.getBoolean("can_sell"),
						rs.getString("num_text"),
						rs.getBoolean("sign_glowing"),
						rs.getString("listed_type"),
						rs.getBoolean("item_hologram")));
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
				al.add(new SignShop(rs.getInt("id"),
						UUID.fromString(rs.getString("player_uuid")),
						rs.getString("sign_shop_name"),
						rs.getInt("account_id"),
						rs.getLong("creation_date_time"),
						rs.getString("itemstack_base64"),
						rs.getString("display_name"),
						rs.getString("material"),
						rs.getLong("item_storage_total"),
						rs.getLong("item_storage_current"),
						rs.getDouble("buy_amount"),
						rs.getDouble("sell_amount"),
						rs.getLong("possible_buy"),
						rs.getLong("possible_sell"),
						rs.getLong("discount_start"),
						rs.getLong("discount_end"),
						rs.getDouble("discount_buy_amount"),
						rs.getDouble("discount_sell_amount"),
						rs.getLong("discount_possible_buy"),
						rs.getLong("discount_possible_sell"),
						rs.getString("server_name"),
						rs.getString("world"),
						rs.getInt("x"),
						rs.getInt("y"),
						rs.getInt("z"),
						rs.getInt("storage_id"),
						rs.getBoolean("unlimited_buy"),
						rs.getBoolean("unlimited_sell"),
						rs.getBoolean("can_buy"),
						rs.getBoolean("can_sell"),
						rs.getString("num_text"),
						rs.getBoolean("sign_glowing"),
						rs.getString("listed_type"),
						rs.getBoolean("item_hologram")));
			}
			return al;
		} catch (SQLException e)
		{
			this.log(Level.WARNING, "SQLException! Could not get a "+this.getClass().getSimpleName()+" Object!", e);
		}
		return new ArrayList<>();
	}
	
	public static ArrayList<SignShop> convert(ArrayList<Object> arrayList)
	{
		ArrayList<SignShop> l = new ArrayList<>();
		for(Object o : arrayList)
		{
			if(o instanceof SignShop)
			{
				l.add((SignShop) o);
			}
		}
		return l;
	}
}