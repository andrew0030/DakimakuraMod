package moe.plushie.dakimakuramod.common.creativetab;

import moe.plushie.dakimakuramod.common.block.ModBlocks;
import moe.plushie.dakimakuramod.common.lib.LibModInfo;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CreativeTabDakimakura extends CreativeTabs {
    
    public CreativeTabDakimakura() {
        super(LibModInfo.ID);
    }

    @Override
    public ItemStack getTabIconItem() {
        return new ItemStack(Item.getItemFromBlock(ModBlocks.blockDakimakura));
    }
}
