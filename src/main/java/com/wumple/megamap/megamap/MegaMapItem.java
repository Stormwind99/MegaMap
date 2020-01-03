package com.wumple.megamap.megamap;

import com.wumple.megamap.ModConfig;
import com.wumple.megamap.api.IMegaMapItem;
import com.wumple.megamap.api.MegaMapAPI;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MapItem;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class MegaMapItem extends MapItem implements IMegaMapItem
{
	public static final String ID = "megamap:megamap_empty";

	public MegaMapItem(Item.Properties builder)
	{
		super(builder.maxStackSize(64));

		setRegistryName(ID);
	}

	@Override
	public String getID()
	{
		return ID;
	}

	/**
	 * Called to trigger the item's "innate" right click behavior. To handle when
	 * this item is used on a Block, see {@link #onItemUse}.
	 */
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn)
	{
		int scale = ModConfig.General.defaultScale.get();
		ItemStack itemstack = MegaMapAPI.getInstance().setupNewMap(worldIn, MathHelper.floor(playerIn.posX),
				MathHelper.floor(playerIn.posZ), (byte)scale, true, false);
		ItemStack itemstack1 = playerIn.getHeldItem(handIn);
		if (!playerIn.abilities.isCreativeMode)
		{
			itemstack1.shrink(1);
		}

		if (itemstack1.isEmpty())
		{
			return new ActionResult<>(ActionResultType.SUCCESS, itemstack);
		}
		else
		{
			if (!playerIn.inventory.addItemStackToInventory(itemstack.copy()))
			{
				playerIn.dropItem(itemstack, false);
			}

			playerIn.addStat(Stats.ITEM_USED.get(this));
			return new ActionResult<>(ActionResultType.SUCCESS, itemstack1);
		}
	}

}