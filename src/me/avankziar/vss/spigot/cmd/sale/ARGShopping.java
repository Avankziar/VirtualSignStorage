package me.avankziar.vss.spigot.cmd.sale;

import java.io.IOException;

import org.bukkit.command.CommandSender;

import me.avankziar.vss.general.ChatApi;
import me.avankziar.vss.spigot.SaLE;
import me.avankziar.vss.spigot.cmdtree.ArgumentConstructor;
import me.avankziar.vss.spigot.cmdtree.ArgumentModule;

public class ARGShopping extends ArgumentModule
{
	private SaLE plugin;
	
	public ARGShopping(SaLE plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
	}

	//sale shopping ... 
	@Override
	public void run(CommandSender sender, String[] args) throws IOException
	{
		sender.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("Cmd.OtherCmd")));
	}
}