package moe.plushie.dakimakuramod.common.crafting;

import moe.plushie.dakimakuramod.common.dakimakura.Daki;
import moe.plushie.dakimakuramod.common.dakimakura.serialize.DakiNbtSerializer;
import moe.plushie.dakimakuramod.common.items.ModItems;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

public class RecipeDakiRecycle implements IRecipe {
    
    @Override
    public boolean matches(InventoryCrafting inventoryCrafting, World world) {
        ItemStack stackDesign1 = null;
        ItemStack stackDesign2 = null;
        
        for (int slotId = 0; slotId < inventoryCrafting.getSizeInventory(); slotId++) {
            ItemStack stack = inventoryCrafting.getStackInSlot(slotId);
            if (stack != null) {
                if (stackDesign1 == null) {
                    stackDesign1 = stack;
                } else if (stackDesign2 == null) {
                    stackDesign2 = stack;
                } else {
                    return false;
                }   
            }
        }
        
        if (stackDesign1 == null) {
            return false;
        }
        if (stackDesign2 == null) {
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
        ItemStack stackDesign1 = null;
        ItemStack stackDesign2 = null;
        
        for (int slotId = 0; slotId < inventoryCrafting.getSizeInventory(); slotId++) {
            ItemStack stack = inventoryCrafting.getStackInSlot(slotId);
            if (stack != null) {
                if (stackDesign1 == null) {
                    stackDesign1 = stack;
                } else if (stackDesign2 == null) {
                    stackDesign2 = stack;
                } else {
                    return null;
                }   
            }
        }
        
        if (stackDesign1 == null) {
            return null;
        }
        if (stackDesign2 == null) {
            return null;
        }
        
        Daki dakiDesign1 = DakiNbtSerializer.deserialize(stackDesign1.getTagCompound());
        if (dakiDesign1 == null) {
            return null;
        }
        
        Daki dakiDesign2 = DakiNbtSerializer.deserialize(stackDesign2.getTagCompound());
        if (dakiDesign2 == null) {
            return null;
        }
        
        ModItems.dakiDesign.setContainerItem(null);
        
        return new ItemStack(ModItems.dakiDesign);
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
    public ItemStack[] getRemainingItems(InventoryCrafting inventoryCrafting) {
        return ForgeHooks.defaultRecipeGetRemainingItems(inventoryCrafting);
    }
}
