package moe.plushie.dakimakuramod.common.crafting;

import moe.plushie.dakimakuramod.common.block.ModBlocks;
import moe.plushie.dakimakuramod.common.dakimakura.Daki;
import moe.plushie.dakimakuramod.common.dakimakura.serialize.DakiNbtSerializer;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class RecipeDakiClear extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

    @Override
    public boolean isDynamic() {
        return true;
    }
    
    @Override
    public boolean matches(InventoryCrafting inventoryCrafting, World world) {
        ItemStack stackDaki = ItemStack.EMPTY;
        
        for (int slotId = 0; slotId < inventoryCrafting.getSizeInventory(); slotId++) {
            ItemStack stack = inventoryCrafting.getStackInSlot(slotId);
            if (!stack.isEmpty()) {
                if (stack.getItem() == Item.getItemFromBlock(ModBlocks.blockDakimakura)) {
                    if (stackDaki.isEmpty()) {
                        stackDaki = stack;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        }
        
        if (stackDaki.isEmpty()) {
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
        ItemStack stackDaki = ItemStack.EMPTY;
        
        for (int slotId = 0; slotId < inventoryCrafting.getSizeInventory(); slotId++) {
            ItemStack stack = inventoryCrafting.getStackInSlot(slotId);
            if (!stack.isEmpty()) {
                if (stack.getItem() == Item.getItemFromBlock(ModBlocks.blockDakimakura)) {
                    if (stackDaki.isEmpty()) {
                        stackDaki = stack;
                    } else {
                        return ItemStack.EMPTY;
                    }
                } else {
                    return ItemStack.EMPTY;
                }
            }
        }
        
        if (stackDaki.isEmpty()) {
            return ItemStack.EMPTY;
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
    public ItemStack getRecipeOutput() {
        return ItemStack.EMPTY;
    }
    
    @Override
    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inventoryCrafting) {
        return ForgeHooks.defaultRecipeGetRemainingItems(inventoryCrafting);
    }

    @Override
    public boolean canFit(int width, int height) {
        return width * height >= 1;
    }
}
