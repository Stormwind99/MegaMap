package com.wumple.megamap.megamap;

import javax.annotation.Nullable;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import com.wumple.megamap.ModConfig;
import com.wumple.megamap.ObjectHolder;
import com.wumple.util.misc.RegistrationHelpers;

import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockStone;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.storage.MapData;

public class ItemMegaMap extends ItemMap
{
    public static final String ID = "megamap:megamap_filled";

    public ItemMegaMap()
    {
    	super();

        setMaxStackSize(64);
        setCreativeTab(CreativeTabs.MISC);

        RegistrationHelpers.nameHelper(this, ID);
    }
    
    public static boolean isMapScaleValid(byte scale)
    {
    	return (scale >= 0) && (scale < ModConfig.maxScale);
    }
    
    public boolean isMyMapScaleValid(byte scale)
    {
    	return isMapScaleValid(scale);
    }
    
    public static ItemStack setupNewMap(World worldIn, double worldX, double worldZ, byte scale, boolean trackingPosition, boolean unlimitedTracking)
    {
        ItemStack itemstack = new ItemStack(ObjectHolder.filled_megamap_item, 1, worldIn.getUniqueDataId("map"));
        String s = "map_" + itemstack.getMetadata();
        MapData mapdata = new MegaMapData(s);
        worldIn.setData(s, mapdata);
        mapdata.scale = scale;
        mapdata.calculateMapCenter(worldX, worldZ, mapdata.scale);
        mapdata.dimension = worldIn.provider.getDimension();
        mapdata.trackingPosition = trackingPosition;
        mapdata.unlimitedTracking = unlimitedTracking;
        mapdata.markDirty();
        return itemstack;
    }
    
    protected static void scaleMap(ItemStack p_185063_0_, World p_185063_1_, int p_185063_2_)
    {
        MapData mapdata = getMyMapData(p_185063_0_, p_185063_1_);
        p_185063_0_.setItemDamage(p_185063_1_.getUniqueDataId("map"));
        MapData mapdata1 = new MegaMapData("map_" + p_185063_0_.getMetadata());

        if (mapdata != null)
        {
            mapdata1.scale = (byte)MathHelper.clamp(mapdata.scale + p_185063_2_, 0, ModConfig.maxScale);
            mapdata1.trackingPosition = mapdata.trackingPosition;
            mapdata1.calculateMapCenter((double)mapdata.xCenter, (double)mapdata.zCenter, mapdata1.scale);
            mapdata1.dimension = mapdata.dimension;
            mapdata1.markDirty();
            p_185063_1_.setData("map_" + p_185063_0_.getMetadata(), mapdata1);
        }
    }
    
    /**
     * Called when item is crafted/smelted. Used only by maps so far.
     */
    @Override
    public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn)
    {
        NBTTagCompound nbttagcompound = stack.getTagCompound();

        if (nbttagcompound != null)
        {
            if (nbttagcompound.hasKey("map_scale_direction", 99))
            {
                scaleMap(stack, worldIn, nbttagcompound.getInteger("map_scale_direction"));
                nbttagcompound.removeTag("map_scale_direction");
            }
            else if (nbttagcompound.getBoolean("map_tracking_position"))
            {
                enableMapTracking(stack, worldIn);
                nbttagcompound.removeTag("map_tracking_position");
            }
        }
    }
    
    @Override
    @Nullable
    public MapData getMapData(ItemStack stack, World worldIn)
    {
    	return getMyMapData(stack, worldIn);
    }

    @Nullable
    public static MapData getMyMapData(ItemStack stack, World worldIn)
    {
        String s = "map_" + stack.getMetadata();
        MapData mapdata = (MapData)worldIn.loadData(MegaMapData.class, s);

        if (mapdata == null && !worldIn.isRemote)
        {
            stack.setItemDamage(worldIn.getUniqueDataId("map"));
            s = "map_" + stack.getMetadata();
            mapdata = new MegaMapData(s);
            byte scale = ModConfig.defaultScale;
            mapdata.scale = scale;
            mapdata.calculateMapCenter((double)worldIn.getWorldInfo().getSpawnX(), (double)worldIn.getWorldInfo().getSpawnZ(), mapdata.scale);
            mapdata.dimension = worldIn.provider.getDimension();
            mapdata.markDirty();
            worldIn.setData(s, mapdata);
        }

        return mapdata;
    }
    
    @Override
    public void updateMapData(World worldIn, Entity viewer, MapData data)
    {
        if (worldIn.provider.getDimension() == data.dimension && viewer instanceof EntityPlayer)
        {
            int scaleNum = 1 << data.scale; // blocks per pixel?
            int xCenter = data.xCenter;
            int zCenter = data.zCenter;
            int viewerPixelX = MathHelper.floor(viewer.posX - (double)xCenter) / scaleNum + 64;
            int viewerPixelZ = MathHelper.floor(viewer.posZ - (double)zCenter) / scaleNum + 64;
            int pixelViewRange = 128 / scaleNum;

            if (worldIn.provider.isNether())
            {
                pixelViewRange /= 2;
            }
            
            // ADDED
            pixelViewRange = Math.max(1, pixelViewRange);

            MapData.MapInfo mapdata$mapinfo = data.getMapInfo((EntityPlayer)viewer);
            ++mapdata$mapinfo.step;
            boolean flag = false;

            for (int pixelX = viewerPixelX - pixelViewRange + 1; pixelX < viewerPixelX + pixelViewRange; ++pixelX)
            {
                if ((pixelX & 15) == (mapdata$mapinfo.step & 15) || flag)
                {
                    flag = false;
                    double d0 = 0.0D;

                    for (int pixelZ = viewerPixelZ - pixelViewRange - 1; pixelZ < viewerPixelZ + pixelViewRange; ++pixelZ)
                    {
                        if (pixelX >= 0 && pixelZ >= -1 && pixelX < 128 && pixelZ < 128)
                        {
                            int i2 = pixelX - viewerPixelX;
                            int j2 = pixelZ - viewerPixelZ;
                            boolean flag1 = i2 * i2 + j2 * j2 > (pixelViewRange - 2) * (pixelViewRange - 2);
                            int k2 = (xCenter / scaleNum + pixelX - 64) * scaleNum;
                            int l2 = (zCenter / scaleNum + pixelZ - 64) * scaleNum;
                            Multiset<MapColor> multiset = HashMultiset.<MapColor>create();
                            BlockPos pos = new BlockPos(k2, 0, l2);
                            Chunk chunk = null;
                            // ADDED
                            if (true) // (worldIn.isChunkGeneratedAt(pos.getX(), pos.getZ()))
                            // ADDED END
                            {
                            	chunk = worldIn.getChunk(pos);
                            }
                            
                            if ((chunk != null) && !chunk.isEmpty())
                            {
                                int i3 = k2 & 15;
                                int j3 = l2 & 15;
                                int k3 = 0;
                                double d1 = 0.0D;

                                if (worldIn.provider.isNether())
                                {
                                    int l3 = k2 + l2 * 231871;
                                    l3 = l3 * l3 * 31287121 + l3 * 11;

                                    if ((l3 >> 20 & 1) == 0)
                                    {
                                        multiset.add(Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT).getMapColor(worldIn, BlockPos.ORIGIN), 10);
                                    }
                                    else
                                    {
                                        multiset.add(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.STONE).getMapColor(worldIn, BlockPos.ORIGIN), 100);
                                    }

                                    d1 = 100.0D;
                                }
                                else
                                {
                                    BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

                                    for (int i4 = 0; i4 < scaleNum; ++i4)
                                    {
                                        for (int j4 = 0; j4 < scaleNum; ++j4)
                                        {
                                        	int x = i4 + i3;
                                        	int z = j4 + j3;
                                        	int chunkIndex = z << 4 | x;
                                            int k4 = 
                                            		// ADDED
                                            		(chunkIndex >= 256) ? 255 :
                                            	    // END ADDED
                                            		chunk.getHeightValue(i4 + i3, j4 + j3) + 1;
                                            IBlockState iblockstate = Blocks.AIR.getDefaultState();

                                            if (k4 <= 1)
                                            {
                                                iblockstate = Blocks.BEDROCK.getDefaultState();
                                            }
                                            else
                                            {
                                                label175:
                                                {
                                                    while (true)
                                                    {
                                                        --k4;
                                                        iblockstate = chunk.getBlockState(i4 + i3, k4, j4 + j3);
                                                        blockpos$mutableblockpos.setPos((chunk.x << 4) + i4 + i3, k4, (chunk.z << 4) + j4 + j3);

                                                        if (iblockstate.getMapColor(worldIn, blockpos$mutableblockpos) != MapColor.AIR || k4 <= 0)
                                                        {
                                                            break;
                                                        }
                                                    }

                                                    if (k4 > 0 && iblockstate.getMaterial().isLiquid())
                                                    {
                                                        int l4 = k4 - 1;

                                                        while (true)
                                                        {
                                                            IBlockState iblockstate1 = chunk.getBlockState(i4 + i3, l4--, j4 + j3);
                                                            ++k3;

                                                            if (l4 <= 0 || !iblockstate1.getMaterial().isLiquid())
                                                            {
                                                                break label175;
                                                            }
                                                        }
                                                    }
                                                }
                                            }

                                            d1 += (double)k4 / (double)(scaleNum * scaleNum);
                                            multiset.add(iblockstate.getMapColor(worldIn, blockpos$mutableblockpos));
                                        }
                                    }
                                }

                                k3 = k3 / (scaleNum * scaleNum);
                                double d2 = (d1 - d0) * 4.0D / (double)(scaleNum + 4) + ((double)(pixelX + pixelZ & 1) - 0.5D) * 0.4D;
                                int i5 = 1;

                                if (d2 > 0.6D)
                                {
                                    i5 = 2;
                                }

                                if (d2 < -0.6D)
                                {
                                    i5 = 0;
                                }

                                MapColor mapcolor = (MapColor)Iterables.getFirst(Multisets.copyHighestCountFirst(multiset), MapColor.AIR);

                                if (mapcolor == MapColor.WATER)
                                {
                                    d2 = (double)k3 * 0.1D + (double)(pixelX + pixelZ & 1) * 0.2D;
                                    i5 = 1;

                                    if (d2 < 0.5D)
                                    {
                                        i5 = 2;
                                    }

                                    if (d2 > 0.9D)
                                    {
                                        i5 = 0;
                                    }
                                }

                                d0 = d1;

                                if (pixelZ >= 0 && i2 * i2 + j2 * j2 < pixelViewRange * pixelViewRange && (!flag1 || (pixelX + pixelZ & 1) != 0))
                                {
                                    byte b0 = data.colors[pixelX + pixelZ * 128];
                                    byte b1 = (byte)(mapcolor.colorIndex * 4 + i5);

                                    if (b0 != b1)
                                    {
                                        data.colors[pixelX + pixelZ * 128] = b1;
                                        data.updateMapData(pixelX, pixelZ);
                                        flag = true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    protected static void enableMapTracking(ItemStack p_185064_0_, World p_185064_1_)
    {
        MapData mapdata = getMyMapData(p_185064_0_, p_185064_1_);
        p_185064_0_.setItemDamage(p_185064_1_.getUniqueDataId("map"));
        MapData mapdata1 = new MegaMapData("map_" + p_185064_0_.getMetadata());
        mapdata1.trackingPosition = true;

        if (mapdata != null)
        {
            mapdata1.xCenter = mapdata.xCenter;
            mapdata1.zCenter = mapdata.zCenter;
            mapdata1.scale = mapdata.scale;
            mapdata1.dimension = mapdata.dimension;
            mapdata1.markDirty();
            p_185064_1_.setData("map_" + p_185064_0_.getMetadata(), mapdata1);
        }
    }
}
