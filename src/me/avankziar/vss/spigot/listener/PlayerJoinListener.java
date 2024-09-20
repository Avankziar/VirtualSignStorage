package me.avankziar.vss.spigot.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.avankziar.vss.spigot.VSS;
import me.avankziar.vss.spigot.database.MysqlHandler;
import me.avankziar.vss.spigot.gui.objects.SettingsLevel;
import me.avankziar.vss.spigot.objects.PlayerData;

public class PlayerJoinListener implements Listener
{
	private VSS plugin;
	
	public PlayerJoinListener(VSS plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event)
	{
		final Player player = event.getPlayer();
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				PlayerData pd = (PlayerData) plugin.getMysqlHandler().getData(MysqlHandler.Type.PLAYERDATA, "`player_uuid` = ?", player.getUniqueId().toString());
				if(pd == null)
				{
					pd = new PlayerData(0, player.getUniqueId(), player.getName(), SettingsLevel.BASE, System.currentTimeMillis());
					plugin.getMysqlHandler().create(MysqlHandler.Type.PLAYERDATA, pd);
				} else
				{
					pd.setName(player.getName());
					pd.setLastLogin(System.currentTimeMillis());
					plugin.getMysqlHandler().updateData(MysqlHandler.Type.PLAYERDATA, pd, "`player_uuid` = ?", player.getUniqueId().toString());
				}
			}
		}.runTaskAsynchronously(plugin);
	}
}