package moe.plushie.dakimakuramod.common.creativetab;

import moe.plushie.dakimakuramod.DakimakuraMod;
import moe.plushie.dakimakuramod.common.lib.LibModInfo;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class CreativeTabDakimakura extends CreativeTabs {
    
    public CreativeTabDakimakura() {
        super(LibModInfo.ID);
    }

    @Override
    public Item getTabIconItem() {
        return Item.getItemFromBlock(DakimakuraMod.getProxy().getModBlocks().BLOCK_DAKIMAKURA);
    }
}
