package com.wumple.megamap.api;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;

public interface IMegaMapAPI
{

    public ItemStack setupNewMap(World worldIn, int worldX, int worldZ, byte scale, boolean trackingPosition, boolean unlimitedTracking);

    boolean isEmptyMap(ItemStack itemstack1);

    boolean isFilledMap(ItemStack itemstack1);

    boolean isMapScaleValid(ItemStack itemstack, byte scale);

    ItemStack copyMap(ItemStack itemstack, int i);

    boolean isExplorationMap(MapData mapData);

    boolean isExplorationMap(ItemStack itemstack, World worldIn);

    Item getFilledMegaMapItem();

    Item getEmptyMegaMapItem();
    
    MapData getMapData(ItemStack itemstack, World worldIn);
}