package moe.plushie.dakimakuramod.common.crafting;

import moe.plushie.dakimakuramod.DakimakuraMod;
import moe.plushie.dakimakuramod.common.dakimakura.Daki;
import moe.plushie.dakimakuramod.common.dakimakura.serialize.DakiNbtSerializer;
import moe.plushie.dakimakuramod.common.items.ModItems;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.ForgeHooks;

public class RecipeDakiRecycle implements IRecipe {
    
    @Override
    public boolean matches(InventoryCrafting inventoryCrafting, World world) {
        ItemStack stackDesign1 = ItemStack.EMPTY;
        ItemStack stackDesign2 = ItemStack.EMPTY;
        for (int slotId = 0; slotId < inventoryCrafting.getSizeInventory(); slotId++) {
            ItemStack stack = inventoryCrafting.getStackInSlot(slotId);
            if (!stack.isEmpty()) {
                if (stack.getItem() != ModItems.dakiDesign) {
                    return false;
                }
                if (stackDesign1.isEmpty()) {
                    stackDesign1 = stack;
                } else if (stackDesign2.isEmpty()) {
                    stackDesign2 = stack;
                } else {
                    return false;
                }   
            }
        }
        
        if (stackDesign1.isEmpty()) {
            return false;
        }
        if (stackDesign2.isEmpty()) {
            return false;
        }
        
        Daki dakiDesign1 = DakiNbtSerializer.deserialize(stackDesign1.getTagCompound());
        if (dakiDesign1 == null) {
            return false;
        }
        
        Daki dakiDesign2 = DakiNbtSerializer.deserialize(stackDesign2.getTagCompound());
        if (dakiDesign2 == null) {
            return false;
        }
        
        return true;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inventoryCrafting) {
        ItemStack stackDesign1 = ItemStack.EMPTY;
        ItemStack stackDesign2 = ItemStack.EMPTY;
        
        for (int slotId = 0; slotId < inventoryCrafting.getSizeInventory(); slotId++) {
            ItemStack stack = inventoryCrafting.getStackInSlot(slotId);
            if (!stack.isEmpty()) {
                if (stack.getItem() != ModItems.dakiDesign) {
                    return ItemStack.EMPTY;
                }
                if (stackDesign1.isEmpty()) {
                    stackDesign1 = stack;
                } else if (stackDesign2.isEmpty()) {
                    stackDesign2 = stack;
                } else {
                    return ItemStack.EMPTY;
                }   
            }
        }
        
        if (stackDesign1.isEmpty()) {
            return ItemStack.EMPTY;
        }
        if (stackDesign2.isEmpty()) {
            return ItemStack.EMPTY;
        }
        
        Daki dakiDesign1 = DakiNbtSerializer.deserialize(stackDesign1.getTagCompound());
        if (dakiDesign1 == null) {
            return ItemStack.EMPTY;
        }
        
        Daki dakiDesign2 = DakiNbtSerializer.deserialize(stackDesign2.getTagCompound());
        if (dakiDesign2 == null) {
            return ItemStack.EMPTY;
        }
        return new ItemStack(stackDesign1.getItem(), 1, 0);
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
        return ForgeHooks.defaultRecipeGetRemainingItems(inv);
    }
}
