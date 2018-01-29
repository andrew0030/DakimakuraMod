package moe.plushie.dakimakuramod.common.block;

import moe.plushie.dakimakuramod.DakimakuraMod;
import moe.plushie.dakimakuramod.common.lib.LibModInfo;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;

public abstract class AbstractModBlockContainer extends BlockContainer {
    
    public AbstractModBlockContainer(String name) {
        super(Material.iron);
        setCreativeTab(DakimakuraMod.creativeTabDakimakura);
        setHardness(3.0F);
        setStepSound(soundTypeMetal);
        setBlockName(name);
    }
    
    public AbstractModBlockContainer(String name, Material material, SoundType soundType, boolean addCreativeTab) {
        super(material);
        if (addCreativeTab) {
            setCreativeTab(DakimakuraMod.creativeTabDakimakura);
        }
        setHardness(3.0F);
        setStepSound(soundType);
        setBlockName(name);
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
