package me.avankziar.vss.spigot.gui.objects;

/*
 * Als Einstellung zu verstehen, dass man bei bestimmten LEvel, nur bestimmte einstellmöglichkeiten sieht.
 * Um Spieler nicht zu überforndern
 */
public enum SettingsLevel
{
	NOLEVEL(0), BASE(1), ADVANCED(2), EXPERT(3), MASTER(4);
	
	private int ordinal;
	
	SettingsLevel(int ordinal)
	{
		this.ordinal = ordinal;
	}
	
	public int getOrdinal()
	{
		return this.ordinal;
	}
	
	public String getName()
	{
		switch(this)
		{
		case BASE:
			return "BASE";
		case ADVANCED:
			return "ADVANCED";
		case EXPERT:
			return "EXPERT";
		case MASTER:
			return "MASTER";
		case NOLEVEL:
			return "NOLEVEL";
		}
		return null;
	}
}