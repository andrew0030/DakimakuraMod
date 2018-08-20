package moe.plushie.dakimakuramod.common.crafting;

import moe.plushie.dakimakuramod.common.dakimakura.Daki;
import moe.plushie.dakimakuramod.common.dakimakura.serialize.DakiNbtSerializer;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class RecipeDakiRecycle implements IRecipe {
    
    @Override
    public boolean matches(InventoryCrafting inventoryCrafting, World world) {
        ItemStack stackDesign1 = ItemStack.EMPTY;
        ItemStack stackDesign2 = ItemStack.EMPTY;
        for (int slotId = 0; slotId < inventoryCrafting.getSizeInventory(); slotId++) {
            ItemStack stack = inventoryCrafting.getStackInSlot(slotId);
            if (stack != ItemStack.EMPTY) {
                if (stackDesign1 == ItemStack.EMPTY) {
                    stackDesign1 = stack;
                } else if (stackDesign2 == ItemStack.EMPTY) {
                    stackDesign2 = stack;
                } else {
                    return false;
                }   
            }
        }
        
        if (stackDesign1 == ItemStack.EMPTY) {
            return false;
        }
        if (stackDesign2 == ItemStack.EMPTY) {
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
            if (stack != ItemStack.EMPTY) {
                if (stackDesign1 == ItemStack.EMPTY) {
                    stackDesign1 = stack;
                } else if (stackDesign2 == ItemStack.EMPTY) {
                    stackDesign2 = stack;
                } else {
                    return ItemStack.EMPTY;
                }   
            }
        }
        
        if (stackDesign1 == ItemStack.EMPTY) {
            return ItemStack.EMPTY;
        }
        if (stackDesign2 == ItemStack.EMPTY) {
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
    public ItemStack getRecipeOutput() {
        return ItemStack.EMPTY;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
        return NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
    }

    @Override
    public IRecipe setRegistryName(ResourceLocation name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ResourceLocation getRegistryName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Class<IRecipe> getRegistryType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean canFit(int width, int height) {
        // TODO Auto-generated method stub
        return false;
    }
}
