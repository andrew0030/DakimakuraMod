package moe.plushie.dakimakuramod.common.crafting;

import moe.plushie.dakimakuramod.common.block.ModBlocks;
import moe.plushie.dakimakuramod.common.dakimakura.Daki;
import moe.plushie.dakimakuramod.common.dakimakura.serialize.DakiNbtSerializer;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

public class RecipeDakiClear implements IRecipe {

    @Override
    public boolean matches(InventoryCrafting inventoryCrafting, World world) {
        ItemStack stackDaki = null;
        
        for (int slotId = 0; slotId < inventoryCrafting.getSizeInventory(); slotId++) {
            ItemStack stack = inventoryCrafting.getStackInSlot(slotId);
            if (stack != null) {
                if (stack.getItem() == Item.getItemFromBlock(ModBlocks.blockDakimakura)) {
                    if (stackDaki == null) {
                        stackDaki = stack;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        }
        
        if (stackDaki == null) {
            return false;
        }
        
        Daki dakiBlack = DakiNbtSerializer.deserialize(stackDaki.getTagCompound());
        if (dakiBlack == null) {
            return false;
        }
        
        return true;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inventoryCrafting) {
        ItemStack stackDaki = null;
        
        for (int slotId = 0; slotId < inventoryCrafting.getSizeInventory(); slotId++) {
            ItemStack stack = inventoryCrafting.getStackInSlot(slotId);
            if (stack != null) {
                if (stack.getItem() == Item.getItemFromBlock(ModBlocks.blockDakimakura)) {
                    if (stackDaki == null) {
                        stackDaki = stack;
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            }
        }
        
        if (stackDaki == null) {
            return null;
        }
        
        Daki dakiBlack = DakiNbtSerializer.deserialize(stackDaki.getTagCompound());
        if (dakiBlack == null) {
            return null;
        }
        
        ItemStack result = stackDaki.copy();
        result.setTagCompound(null);
        
        return result;
    }

    @Override
    public int getRecipeSize() {
        return 1;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return null;
    }

    @Override
    public ItemStack[] getRemainingItems(InventoryCrafting inventoryCrafting) {
        return ForgeHooks.defaultRecipeGetRemainingItems(inventoryCrafting);
    }
}
