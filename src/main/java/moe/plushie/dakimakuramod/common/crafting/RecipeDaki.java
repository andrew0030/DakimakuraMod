package moe.plushie.dakimakuramod.common.crafting;

import moe.plushie.dakimakuramod.common.block.ModBlocks;
import moe.plushie.dakimakuramod.common.dakimakura.Daki;
import moe.plushie.dakimakuramod.common.dakimakura.serialize.DakiNbtSerializer;
import moe.plushie.dakimakuramod.common.items.ModItems;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class RecipeDaki implements IRecipe {

    @Override
    public boolean matches(InventoryCrafting inventoryCrafting, World world) {
        ItemStack stackDaki = null;
        ItemStack stackDesign = null;
        
        for (int slotId = 0; slotId < inventoryCrafting.getSizeInventory(); slotId++) {
            ItemStack stack = inventoryCrafting.getStackInSlot(slotId);
            if (stack != null) {
                if (stack.getItem() == ModItems.dakiDesign) {
                    if (stackDesign == null) {
                        stackDesign = stack;
                    } else {
                        return false;
                    }
                } else if (stack.getItem() == Item.getItemFromBlock(ModBlocks.blockDakimakura)) {
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
        
        if (stackDesign == null) {
            return false;
        }
        if (stackDaki == null) {
            return false;
        }
        
        Daki dakiDesign = DakiNbtSerializer.deserialize(stackDesign.getTagCompound());
        if (dakiDesign == null) {
            return false;
        }
        Daki dakiBlack = DakiNbtSerializer.deserialize(stackDaki.getTagCompound());
        if (dakiBlack != null) {
            return false;
        }
        
        return true;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inventoryCrafting) {
        ItemStack stackDaki = null;
        ItemStack stackDesign = null;
        
        for (int slotId = 0; slotId < inventoryCrafting.getSizeInventory(); slotId++) {
            ItemStack stack = inventoryCrafting.getStackInSlot(slotId);
            if (stack != null) {
                if (stack.getItem() == ModItems.dakiDesign) {
                    if (stackDesign == null) {
                        stackDesign = stack;
                    } else {
                        return null;
                    }
                } else if (stack.getItem() == Item.getItemFromBlock(ModBlocks.blockDakimakura)) {
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
        
        if (stackDesign == null) {
            return null;
        }
        if (stackDaki == null) {
            return null;
        }
        
        Daki dakiDesign = DakiNbtSerializer.deserialize(stackDesign.getTagCompound());
        if (dakiDesign == null) {
            return null;
        }
        
        ItemStack result = stackDaki.copy();
        if (!result.hasTagCompound()) {
            result.setTagCompound(new NBTTagCompound());
        }
        DakiNbtSerializer.serialize(dakiDesign, result.getTagCompound());
        
        ModItems.dakiDesign.setContainerItem(ModItems.dakiDesign);
        
        return result;
    }

    @Override
    public int getRecipeSize() {
        return 2;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return null;
    }

    @Override
    public ItemStack[] getRemainingItems(InventoryCrafting inv) {
        // TODO Auto-generated method stub
        return null;
    }
}
