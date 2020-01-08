package com.wumple.megamap.megamap;

import com.wumple.megamap.ConfigManager;
import com.wumple.megamap.ModObjectHolder;
import com.wumple.util.misc.ItemStackUtil;
import com.wumple.util.xmap.IXMapAPI;
import com.wumple.util.xmap.XMapAPI;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;

public class MegaMapAPI extends XMapAPI
{
	public static void setup()
	{
		register(new MegaMapAPI());
	}
	
	public static IXMapAPI getInstance()
	{
		if (instance == null)
		{
			setup();
		}
		return instance;
	}
	
	@Override
	public ItemStack copyMapDeep(ItemStack itemstack, World worldIn)
	{
		// MAYBE check if itemstack is a valid map and return EMPTY if not?
		if (ConfigManager.General.disableVanillaRecipes.get() == true)
		{
			ItemStack newstack = ItemStackUtil.pseudoClone(itemstack, getFilledMapItem());
			cloneMapData(itemstack, newstack, worldIn);
			return newstack;
		}
		else
		{
			return super.copyMapDeep(itemstack, worldIn);
		}
	}
	
	@Override
	public byte getMaxScale()
	{
		return ConfigManager.General.maxScale.get().byteValue();
	}
	
	@Override
	public Item getFilledMapItem()
	{
		return ModObjectHolder.filled_megamap_item;
	}

	@Override
	public Item getEmptyMapItem()
	{
		return ModObjectHolder.empty_megamap_item;
	}
	
	@Override
	public MapData createMapData(String mapName)
	{
		return new MegaMapData(mapName);
	}
}