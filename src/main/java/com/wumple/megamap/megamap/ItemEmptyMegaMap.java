package com.wumple.megamap.megamap;

import com.wumple.megamap.ModConfig;
import com.wumple.megamap.api.IItemEmptyMegaMap;
import com.wumple.util.misc.RegistrationHelpers;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemEmptyMap;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemEmptyMegaMap extends ItemEmptyMap implements IItemEmptyMegaMap
{
    public static final String ID = "megamap:megamap_empty";

    public ItemEmptyMegaMap()
    {
    	super();
    	
        setMaxStackSize(64);
        setCreativeTab(CreativeTabs.MISC);

        RegistrationHelpers.nameHelper(this, ID);
    }


    /**
     * Called when the equipped item is right clicked.
     */
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
    	byte scale = ModConfig.defaultScale;
        ItemStack itemstack = ItemMegaMap.setupNewMap(worldIn, playerIn.posX, playerIn.posZ, scale, true, false);
        ItemStack itemstack1 = playerIn.getHeldItem(handIn);
        itemstack1.shrink(1);

        if (itemstack1.isEmpty())
        {
            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
        }
        else
        {
            if (!playerIn.inventory.addItemStackToInventory(itemstack.copy()))
            {
                playerIn.dropItem(itemstack, false);
            }

            playerIn.addStat(StatList.getObjectUseStats(this));
            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack1);
        }
    }
    
    @Override
    public String getID()
    { return ID; }
}