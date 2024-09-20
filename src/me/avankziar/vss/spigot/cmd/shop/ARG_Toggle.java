package me.avankziar.vss.spigot.cmd.shop;

import java.io.IOException;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.avankziar.vss.general.ChatApi;
import me.avankziar.vss.spigot.VSS;
import me.avankziar.vss.spigot.cmdtree.ArgumentConstructor;
import me.avankziar.vss.spigot.cmdtree.ArgumentModule;
import me.avankziar.vss.spigot.handler.SignHandler;

public class ARG_Toggle extends ArgumentModule
{
	private VSS plugin;
	
	public ARG_Toggle(VSS plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
	}

	//sale shop toggle
	@Override
	public void run(CommandSender sender, String[] args) throws IOException
	{
		Player player = (Player) sender;
		if(SignHandler.bypassToggle.contains(player.getUniqueId().toString()))
		{
			SignHandler.bypassToggle.remove(player.getUniqueId().toString());
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Cmd.Toggle.Deactive")));
		} else
		{
			SignHandler.bypassToggle.add(player.getUniqueId().toString());
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Cmd.Toggle.Active")));
		}
	}
}