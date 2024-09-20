package me.avankziar.vss.spigot.handler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

public class Base64Handler
{
	private ItemStack is;
	private String data;
	
	public Base64Handler(ItemStack itemStack)
	{
		this.is = itemStack;
	}
	
	public Base64Handler(String data)
	{
		this.data = data;
	}
	
	public ItemStack fromBase64()
	{
		if(data == null)
		{
			return null;
		}
		try 
		{
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            Object o = dataInput.readObject();
            dataInput.close();
            return (ItemStack) o;
        } catch (IOException e)
		{
			e.printStackTrace();
		} catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
    	return null;
	}

	public String toBase64()
	{
		if(is == null)
		{
			return null;
		}
		try 
		{
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeObject(is);
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) 
		{
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
	}

}
