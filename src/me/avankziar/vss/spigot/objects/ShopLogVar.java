package me.avankziar.vss.spigot.objects;

public class ShopLogVar
{
	public int shopID;
	public String shopname;
	public String currency;
	public long itemAmountBuy;
	public double costTotalBuy;
	public long itemAmountSell;
	public double costTotalSell;
	
	public ShopLogVar(int shopID, String shopname, String currency,
			long itemAmountBuy, double costPerItemBuy, long itemAmountSell, double costPerItemSell)
	{
		this.shopID = shopID;
		this.shopname = shopname;
		this.currency = currency;
		this.itemAmountBuy = itemAmountBuy;
		this.costTotalBuy = itemAmountBuy*costPerItemBuy;
		this.itemAmountSell = itemAmountSell;
		this.costTotalSell = itemAmountSell*costPerItemSell;
	}
	
	public ShopLogVar addBuy(long itemAmount, double costPerItem)
	{
		this.itemAmountBuy += itemAmount;
		this.costTotalBuy += itemAmount*costPerItem;
		return this;
	}
	
	public ShopLogVar addSell(long itemAmount, double costPerItem)
	{
		this.itemAmountSell += itemAmount;
		this.costTotalSell += itemAmount*costPerItem;
		return this;
	}
}