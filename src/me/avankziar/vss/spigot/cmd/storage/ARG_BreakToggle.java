package me.avankziar.vss.spigot.cmd.storage;

import java.io.IOException;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.avankziar.vss.general.ChatApi;
import me.avankziar.vss.general.cmdtree.ArgumentConstructor;
import me.avankziar.vss.spigot.VSS;
import me.avankziar.vss.spigot.cmdtree.ArgumentModule;
import me.avankziar.vss.spigot.handler.SignQuantityHandler;

public class ARG_BreakToggle extends ArgumentModule
{
	private VSS plugin;
	
	public ARG_BreakToggle(VSS plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
	}

	//vss shop breaktoggle
	@Override
	public void run(CommandSender sender, String[] args) throws IOException
	{
		Player player = (Player) sender;
		if(SignQuantityHandler.isBreakToggle(player.getUniqueId()))
		{
			SignQuantityHandler.breakToggle.remove(player.getUniqueId().toString());
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Cmd.BreakToggle.Deactive")));
		} else
		{
			SignQuantityHandler.breakToggle.add(player.getUniqueId().toString());
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Cmd.BreakToggle.Active")));
		}
	}
}