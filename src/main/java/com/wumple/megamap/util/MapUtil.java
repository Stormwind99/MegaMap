package com.wumple.megamap.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.storage.MapData;
import net.minecraft.world.storage.MapDecoration;

public class MapUtil
{
	public static boolean isExplorationMap(MapData mapData)
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
	
	public static boolean isMapScaleValid(byte scale)
	{
		return ((scale >= 0) && (scale <= 4));
	}
	
	public static void mapScaleDirection(ItemStack stack, int direction)
	{
		// TODO get map_scale_direction if present and modify it
		stack.getOrCreateTag().putInt("map_scale_direction", direction);
	}
	
	public static int extractMapScaleDirection(ItemStack stack)
	{
		int value = 0;
		
		CompoundNBT compoundnbt = stack.getTag();
		if (compoundnbt != null && compoundnbt.contains("map_scale_direction", 99))
		{
			value = compoundnbt.getInt("map_scale_direction");
			compoundnbt.remove("map_scale_direction");
		}
		
		return value;
	}
}
