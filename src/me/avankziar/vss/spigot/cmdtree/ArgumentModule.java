package me.avankziar.vss.spigot.cmdtree;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.bukkit.command.CommandSender;

import me.avankziar.vss.spigot.SaLE;

public abstract class ArgumentModule
{
	public ArgumentConstructor argumentConstructor;

    public ArgumentModule(ArgumentConstructor argumentConstructor)
    {
       this.argumentConstructor = argumentConstructor;
       SaLE.getPlugin().getArgumentMap().put(argumentConstructor.getPath(), this);
    }
    
    private LinkedHashMap<UUID, Long> cooldown = new LinkedHashMap<>();;
	
	public boolean isOnCooldown(UUID uuid)
	{
		Long c = cooldown.get(uuid);
		return c == null ? false : c.longValue() > System.currentTimeMillis();
	}
	
	public void setCooldown(UUID uuid, long duration, TimeUnit timeUnit)
	{
		cooldown.put(uuid, timeUnit.convert(duration, TimeUnit.MILLISECONDS)+System.currentTimeMillis());
	}
	
	public void removeCooldown(UUID uuid)
	{
		cooldown.remove(uuid);
	}
    
    //This method will process the command.
    public abstract void run(CommandSender sender, String[] args) throws IOException;

}
