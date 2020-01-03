package com.wumple.megamap.api;

import com.wumple.megamap.ConfigManager;
import com.wumple.megamap.ModObjectHolder;
import com.wumple.megamap.megamap.FilledMegaMapItem;
import com.wumple.megamap.util.Util;

import net.minecraft.item.FilledMapItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;
import net.minecraft.world.storage.MapDecoration;

public class MegaMapAPI implements IMegaMapAPI
{
	public static IMegaMapAPI instance;

	public static IMegaMapAPI getInstance()
	{
		if (instance == null)
		{
			instance = new MegaMapAPI();
		}
		return instance;
	}

	@Override
	public ItemStack setupNewMap(World worldIn, int worldX, int worldZ, byte scale, boolean trackingPosition,
			boolean unlimitedTracking)
	{
		return FilledMegaMapItem.setupNewMap(worldIn, worldX, worldZ, scale, trackingPosition, unlimitedTracking);
	}

	@Override
	public boolean isEmptyMap(ItemStack itemstack1)
	{
		return ItemTags.getCollection().getOrCreate(new ResourceLocation("forge", "empty_maps")).contains(itemstack1.getItem());
	}

	@Override
	public boolean isFilledMap(ItemStack itemstack1)
	{
		return ItemTags.getCollection().getOrCreate(new ResourceLocation("forge", "filled_maps")).contains(itemstack1.getItem());
	}

	@Override
	public boolean isMapScaleValid(ItemStack itemstack, byte scale)
	{
		// byte range is -128 to 127
		if (itemstack.getItem() instanceof IFilledMegaMapItem)
		{
			IFilledMegaMapItem item = (IFilledMegaMapItem) (itemstack.getItem());
			return item.isAMapScaleValid(scale);
		}
		else
		{
			return ((scale >= 0) && (scale <= 4));
		}
	}

	@Override
	public ItemStack copyMap(ItemStack itemstack, int i)
	{
		// MAYBE check if itemstack is a valid map and return EMPTY if not?
		
		Item srcItem = itemstack.getItem();
		ItemStack itemstack2;

		if (ConfigManager.General.disableVanillaRecipes.get() == true)
		{
			srcItem = ModObjectHolder.filled_megamap_item;
			// from ItemStack.copy
			itemstack2 = new ItemStack(srcItem, i);
			itemstack2.setAnimationsToGo(itemstack.getAnimationsToGo());
			if (itemstack.hasTag()) itemstack2.setTag(itemstack.getTag());
		}
		else
		{
			itemstack2 = itemstack.copy();
			itemstack2.setCount(i);
		}

		return itemstack2;
	}

	@Override
	public boolean isExplorationMap(MapData mapData)
	{
		if ((mapData == null) || (mapData.mapDecorations != null))
		{
			for (MapDecoration mapdecoration : mapData.mapDecorations.values())
			{
				if (mapdecoration.getType() == MapDecoration.Type.MANSION
						|| mapdecoration.getType() == MapDecoration.Type.MONUMENT)
				{
					return true;
				}
			}
		}

		return false;
	}

	@Override
	public boolean isExplorationMap(ItemStack itemstack, World worldIn)
	{
		FilledMapItem item = Util.as(itemstack.getItem(), FilledMapItem.class);
		MapData mapdata = (item != null) ? FilledMapItem.getMapData(itemstack, worldIn) : null;

		return isExplorationMap(mapdata);
	}

	@Override
	public Item getFilledMegaMapItem()
	{
		return ModObjectHolder.filled_megamap_item;
	}

	@Override
	public Item getEmptyMegaMapItem()
	{
		return ModObjectHolder.empty_megamap_item;
	}
	
	@Override
	public MapData getMapData(ItemStack itemstack, World worldIn)
	{
		MapData mapdata;
		
		// use MegaMapItem if possible to get MegaMapData instead of just MapData
		if (itemstack.getItem() instanceof IFilledMegaMapItem)
		{
			IFilledMegaMapItem item = Util.as(itemstack.getItem(), IFilledMegaMapItem.class);
			mapdata = item.getMyMapData(itemstack, worldIn);
		}
		// otherwise if normal map, fallback
		else
		{
			mapdata = FilledMapItem.getMapData(itemstack, worldIn);
		}
		
		return mapdata;
	}
}
