package com.wumple.megamap.recipes;

import javax.annotation.Nullable;

import com.google.gson.JsonObject;
import com.wumple.megamap.Reference;
import com.wumple.megamap.api.MegaMapAPI;
import com.wumple.util.base.misc.Util;
import com.wumple.util.crafting.RecipeUtil;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class MegaMapExtendingRecipeFactory implements IRecipeFactory
{
    /**
     * hook for JSON to be able to use this recipe
     * 
     * @see _factories.json
     * @see filled_map_transcribe.json
     */
    @Override
    public IRecipe parse(JsonContext context, JsonObject json)
    {
        final String group = JsonUtils.getString(json, "group", "");
        final CraftingHelper.ShapedPrimer primer = RecipeUtil.parseShaped(context, json);
        final ItemStack result = CraftingHelper.getItemStack(JsonUtils.getJsonObject(json, "result"), context);

        ResourceLocation loc = group.isEmpty() ? new ResourceLocation(Reference.MOD_ID, "megamap_extending") : new ResourceLocation(group);

        return new MegaMapExtendingRecipe(loc, result, primer);
    }

    public static class MegaMapExtendingRecipe extends ShapedOreRecipe
    {
        public MegaMapExtendingRecipe(@Nullable final ResourceLocation group, final ItemStack result, final CraftingHelper.ShapedPrimer primer)
        {
            super(group, result, primer);
            // register for events so received onCrafting event can handle map transcription
            MinecraftForge.EVENT_BUS.register(this);
        }

        /**
         * Used to check if a recipe matches current crafting inventory
         */
        public boolean matches(InventoryCrafting inv, World worldIn)
        {
            if (!super.matches(inv, worldIn))
            {
                return false;
            }
            else
            {
                ItemStack itemstack = ItemStack.EMPTY;

                for (int i = 0; i < inv.getSizeInventory() && itemstack.isEmpty(); ++i)
                {
                    ItemStack itemstack1 = inv.getStackInSlot(i);

                    if (MegaMapAPI.getInstance().isFilledMap(itemstack1))
                    {
                        itemstack = itemstack1;
                    }
                }

                if (itemstack.isEmpty())
                {
                    return false;
                }
                else
                {
                    ItemMap item = Util.as(itemstack.getItem(), ItemMap.class);
                    MapData mapdata = (item != null) ? item.getMapData(itemstack, worldIn) : null;

                    if (mapdata == null)
                    {
                        return false;
                    }
                    else if (MegaMapAPI.getInstance().isExplorationMap(mapdata))
                    {
                        return false;
                    }
                    else
                    {
                        byte testScale = (byte) (mapdata.scale + 1);
                        return MegaMapAPI.getInstance().isMapScaleValid(itemstack, testScale);
                    }
                }
            }
        }

        /**
         * Returns an Item that is the result of this recipe
         */
        public ItemStack getCraftingResult(InventoryCrafting inv)
        {
            ItemStack itemstack = ItemStack.EMPTY;

            for (int i = 0; i < inv.getSizeInventory() && itemstack.isEmpty(); ++i)
            {
                ItemStack itemstack1 = inv.getStackInSlot(i);

                if (MegaMapAPI.getInstance().isFilledMap(itemstack1))
                {
                    itemstack = itemstack1;
                }
            }

            itemstack = itemstack.copy();
            itemstack.setCount(1);

            if (itemstack.getTagCompound() == null)
            {
                itemstack.setTagCompound(new NBTTagCompound());
            }

            itemstack.getTagCompound().setInteger("map_scale_direction", 1);
            return itemstack;
        }

        /**
         * If true, this recipe does not appear in the recipe book and does not respect recipe unlocking (and the doLimitedCrafting gamerule)
         */
        public boolean isDynamic()
        {
            return true;
        }

        @Override
        public String getGroup()
        {
            return group == null ? "" : group.toString();
        }
    }
}
