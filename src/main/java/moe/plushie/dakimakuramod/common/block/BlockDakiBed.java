package moe.plushie.dakimakuramod.common.block;

import cpw.mods.fml.common.registry.GameRegistry;
import moe.plushie.dakimakuramod.DakimakuraMod;
import moe.plushie.dakimakuramod.common.items.block.ModItemBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;

public class BlockDakiBed extends BlockBed {
    
    public BlockDakiBed() {
        setCreativeTab(DakimakuraMod.creativeTabDakimakura);
        setBlockName("daki-bed");
    }
    
    @Override
    public Block setBlockName(String name) {
        GameRegistry.registerBlock(this, ModItemBlock.class, "block." + name);
        return super.setBlockName(name);
    }
    
    @Override
    public void registerBlockIcons(IIconRegister p_149651_1_) {
    }
    
    @Override
    public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
        return Blocks.bed.getIcon(p_149691_1_, p_149691_2_);
    }
}
