package com.wumple.megamap.megamap;

import com.wumple.megamap.ModConfig;
import com.wumple.megamap.ObjectHolder;
import com.wumple.util.base.misc.Util;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;
import net.minecraft.world.storage.MapDecoration;

public class MegaMapUtil
{

    public static boolean isEmptyMap(ItemStack itemstack1)
    {
        return (itemstack1.getItem() == Items.MAP) ||
                (itemstack1.getItem() instanceof ItemEmptyMegaMap);
    }

    public static boolean isFilledMap(ItemStack itemstack1)
    {
        return (itemstack1.getItem() == Items.FILLED_MAP)
                || (itemstack1.getItem() instanceof ItemMegaMap);
    }

    public static boolean isMapScaleValid(ItemStack itemstack, byte scale)
    {
        // byte range is -128 to 127
        if (itemstack.getItem() instanceof ItemMegaMap)
        {
            ItemMegaMap item = (ItemMegaMap) (itemstack.getItem());
            return item.isMyMapScaleValid(scale);
        }
        else
        {
            return ((scale >= 0) && (scale <= 4));
        }
    }

    public static ItemStack copyMap(ItemStack itemstack, int i)
    {
        Item srcItem = itemstack.getItem();
        if (ModConfig.disableVanillaRecipes)
        {
            srcItem = ObjectHolder.filled_megamap_item;
        }
        ItemStack itemstack2 = new ItemStack(srcItem, i + 1, itemstack.getMetadata());

        if (itemstack.hasDisplayName())
        {
            itemstack2.setStackDisplayName(itemstack.getDisplayName());
        }

        if (itemstack.hasTagCompound())
        {
            itemstack2.setTagCompound(itemstack.getTagCompound());
        }

        return itemstack2;
    }

    public static boolean isExplorationMap(MapData mapData)
    {
        if ((mapData != null) && (mapData.mapDecorations != null))
        {
            for (MapDecoration mapdecoration : mapData.mapDecorations.values())
            {
                if (mapdecoration.getType() == MapDecoration.Type.MANSION || mapdecoration.getType() == MapDecoration.Type.MONUMENT)
                {
                    return true;
                }
            }
        }

        return false;
    }
    
    public static boolean isExplorationMap(ItemStack itemstack, World worldIn)
    {
        ItemMap item = Util.as(itemstack.getItem(), ItemMap.class);
        MapData mapdata = (item != null) ? item.getMapData(itemstack, worldIn) : null;
        
        return isExplorationMap(mapdata);
    }
}
