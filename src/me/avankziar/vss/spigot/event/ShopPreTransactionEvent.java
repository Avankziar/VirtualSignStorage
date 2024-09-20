package me.avankziar.vss.spigot.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.avankziar.vss.spigot.objects.SignShop;

public class ShopPreTransactionEvent extends Event
{
	private static final HandlerList HANDLERS = new HandlerList();
	
	private boolean isCancelled;
	private SignShop signShop;
	private long itemAmount;
	private double costPerItem;
	private double taxPerItem;
	private boolean transactionType; //True for buying
	private Player client;
	
	public ShopPreTransactionEvent(SignShop signShop, long itemAmount,
			double costPerItem, double taxPerItem, boolean transactionType, Player client)
	{
		super(true);
		setCancelled(false);
		setSignShop(signShop);
		setItemAmount(itemAmount);
		setCostPerItem(costPerItem);
		setTaxPerItem(taxPerItem);
		this.transactionType = transactionType;
		setClient(client);
	}
	
	public HandlerList getHandlers() 
    {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() 
    {
        return HANDLERS;
    }
    
    public boolean isCancelled()
	{
		return isCancelled;
	}

	public void setCancelled(boolean isCancelled)
	{
		this.isCancelled = isCancelled;
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

	public double getTaxPerItem()
	{
		return taxPerItem;
	}

	public void setTaxPerItem(double taxPerItem)
	{
		this.taxPerItem = taxPerItem;
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
}