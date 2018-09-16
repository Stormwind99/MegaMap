package com.wumple.megamap.api;

import com.wumple.megamap.ModConfig;
import com.wumple.megamap.ObjectHolder;
import com.wumple.megamap.megamap.ItemEmptyMegaMap;
import com.wumple.megamap.megamap.ItemMegaMap;
import com.wumple.util.base.misc.Util;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
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
    public ItemStack setupNewMap(World worldIn, double worldX, double worldZ, byte scale, boolean trackingPosition, boolean unlimitedTracking)
    {
        return ItemMegaMap.setupNewMap(worldIn, worldX, worldZ, scale, trackingPosition, unlimitedTracking);
    }

    @Override
    public boolean isEmptyMap(ItemStack itemstack1)
    {
        return (itemstack1.getItem() == Items.MAP) ||
                (itemstack1.getItem() instanceof ItemEmptyMegaMap);
    }

    @Override
    public boolean isFilledMap(ItemStack itemstack1)
    {
        return (itemstack1.getItem() == Items.FILLED_MAP)
                || (itemstack1.getItem() instanceof ItemMegaMap);
    }

    @Override
    public boolean isMapScaleValid(ItemStack itemstack, byte scale)
    {
        // byte range is -128 to 127
        if (itemstack.getItem() instanceof ItemMegaMap)
        {
            IItemMegaMap item = (IItemMegaMap) (itemstack.getItem());
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

    @Override
    public boolean isExplorationMap(MapData mapData)
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
    
    @Override
    public boolean isExplorationMap(ItemStack itemstack, World worldIn)
    {
        ItemMap item = Util.as(itemstack.getItem(), ItemMap.class);
        MapData mapdata = (item != null) ? item.getMapData(itemstack, worldIn) : null;
        
        return isExplorationMap(mapdata);
    }
    
    @Override
    public Item getFilledMegaMapItem()
    {
        return ObjectHolder.filled_megamap_item;
    }
    
    @Override
    public Item getEmptyMegaMapItem()
    {
        return ObjectHolder.empty_megamap_item;
    }
}
