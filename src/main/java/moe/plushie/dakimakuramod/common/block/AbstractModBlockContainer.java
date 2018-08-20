package moe.plushie.dakimakuramod.common.block;

import moe.plushie.dakimakuramod.DakimakuraMod;
import moe.plushie.dakimakuramod.common.lib.LibModInfo;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;

public abstract class AbstractModBlockContainer extends BlockContainer {
    
    public AbstractModBlockContainer(String name) {
        super(Material.IRON);
        setCreativeTab(DakimakuraMod.creativeTabDakimakura);
        setHardness(3.0F);
        setSoundType(SoundType.METAL);
        setUnlocalizedName(name);
    }
    
    public AbstractModBlockContainer(String name, Material material, SoundType soundType, boolean addCreativeTab) {
        super(material);
        if (addCreativeTab) {
            setCreativeTab(DakimakuraMod.creativeTabDakimakura);
        }
        setHardness(3.0F);
        setSoundType(soundType);
        setUnlocalizedName(name);
    }
    
    @Override
    public Block setUnlocalizedName(String name) {
        super.setUnlocalizedName(name);
        setRegistryName(new ResourceLocation(LibModInfo.ID, "tile." + name));
        return this;
    }
    
    @Override
    public String getUnlocalizedName() {
        return getModdedUnlocalizedName(super.getUnlocalizedName());
    }

    protected String getModdedUnlocalizedName(String unlocalizedName) {
        String name = unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
        return "tile." + LibModInfo.ID.toLowerCase() + ":" + name;
    }
}
