package me.avankziar.vss.spigot.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.avankziar.vss.spigot.objects.SignShop;

public class ShopPostTransactionEvent extends Event
{
	private static final HandlerList HANDLERS = new HandlerList();
	
	private SignShop signShop;
	private long itemAmount;
	private double costPerItem;
	private boolean transactionType; //True for buying
	private Player client;
	private String transactionCategory;
	private String transactionComment;
	
	public ShopPostTransactionEvent(SignShop signShop, long itemAmount, double costPerItem, boolean transactionType, Player client,
			String transactionCategory, String transactionComment)
	{
		super(true);
		setSignShop(signShop);
		setItemAmount(itemAmount);
		setCostPerItem(costPerItem);
		this.transactionType = transactionType;
		setClient(client);
		setTransactionCategory(transactionCategory);
		setTransactionComment(transactionComment);
	}
	
	public HandlerList getHandlers() 
    {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() 
    {
        return HANDLERS;
    }

	public SignShop getSignShop()
	{
		return signShop;
	}

	public void setSignShop(SignShop signShop)
	{
		this.signShop = signShop;
	}

	public long getItemAmount()
	{
		return itemAmount;
	}

	public void setItemAmount(long itemAmount)
	{
		this.itemAmount = itemAmount;
	}
	
	public double getCostPerItem()
	{
		return costPerItem;
	}

	public void setCostPerItem(double costPerItem)
	{
		this.costPerItem = costPerItem;
	}

	public boolean isSelling()
	{
		return !transactionType;
	}
	
	public boolean isBuying()
	{
		return transactionType;
	}
	
	public Player getClient()
	{
		return client;
	}

	public void setClient(Player client)
	{
		this.client = client;
	}

	public String getTransactionCategory()
	{
		return transactionCategory;
	}

	public void setTransactionCategory(String transactionCategory)
	{
		this.transactionCategory = transactionCategory;
	}

	public String getTransactionComment()
	{
		return transactionComment;
	}

	public void setTransactionComment(String transactionComment)
	{
		this.transactionComment = transactionComment;
	}
}