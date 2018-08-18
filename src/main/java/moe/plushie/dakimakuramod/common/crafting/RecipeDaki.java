package moe.plushie.dakimakuramod.common.crafting;

import moe.plushie.dakimakuramod.DakimakuraMod;
import moe.plushie.dakimakuramod.common.block.ModBlocks;
import moe.plushie.dakimakuramod.common.dakimakura.Daki;
import moe.plushie.dakimakuramod.common.dakimakura.serialize.DakiNbtSerializer;
import moe.plushie.dakimakuramod.common.items.ModItems;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

public class RecipeDaki implements IRecipe {

    @Override
    public boolean matches(InventoryCrafting inventoryCrafting, World world) {
        ItemStack stackDaki = ItemStack.EMPTY;
        ItemStack stackDesign = ItemStack.EMPTY;
        
        for (int slotId = 0; slotId < inventoryCrafting.getSizeInventory(); slotId++) {
            ItemStack stack = inventoryCrafting.getStackInSlot(slotId);
            if (stack != ItemStack.EMPTY) {
                if (stack.getItem() == ModItems.dakiDesign) {
                    if (stackDesign == ItemStack.EMPTY) {
                        stackDesign = stack;
                    } else {
                        return false;
                    }
                } else if (stack.getItem() == Item.getItemFromBlock(ModBlocks.blockDakimakura)) {
                    if (stackDaki == ItemStack.EMPTY) {
                        stackDaki = stack;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        }
        
        if (stackDesign == ItemStack.EMPTY) {
            return false;
        }
        if (stackDaki == ItemStack.EMPTY) {
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
        ItemStack stackDaki = ItemStack.EMPTY;
        ItemStack stackDesign = ItemStack.EMPTY;
        
        for (int slotId = 0; slotId < inventoryCrafting.getSizeInventory(); slotId++) {
            ItemStack stack = inventoryCrafting.getStackInSlot(slotId);
            if (stack != ItemStack.EMPTY) {
                if (stack.getItem() == ModItems.dakiDesign) {
                    if (stackDesign == ItemStack.EMPTY) {
                        stackDesign = stack;
                    } else {
                        return ItemStack.EMPTY;
                    }
                } else if (stack.getItem() == Item.getItemFromBlock(ModBlocks.blockDakimakura)) {
                    if (stackDaki == ItemStack.EMPTY) {
                        stackDaki = stack;
                    } else {
                        return ItemStack.EMPTY;
                    }
                } else {
                    return ItemStack.EMPTY;
                }
            }
        }
        
        if (stackDesign == ItemStack.EMPTY) {
            return ItemStack.EMPTY;
        }
        if (stackDaki == ItemStack.EMPTY) {
            return ItemStack.EMPTY;
        }
        
        Daki dakiDesign = DakiNbtSerializer.deserialize(stackDesign.getTagCompound());
        if (dakiDesign == null) {
            return ItemStack.EMPTY;
        }
        
        ItemStack result = stackDaki.copy();
        if (!result.hasTagCompound()) {
            result.setTagCompound(new NBTTagCompound());
        }
        DakiNbtSerializer.serialize(dakiDesign, result.getTagCompound());
        
        return result;
    }

    @Override
    public int getRecipeSize() {
        return 2;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return ItemStack.EMPTY;
    }
    
    @Override
    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
        NonNullList<ItemStack> ret = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
        for (int i = 0; i < ret.size(); i++) {
        	ItemStack stack = inv.getStackInSlot(i);
        	if (stack.getItem() == ModItems.dakiDesign) {
        		ret.set(i, stack.copy());
        	}
        }
        return ret;
    }
}
