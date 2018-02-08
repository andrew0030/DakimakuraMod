package moe.plushie.dakimakuramod.common.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import moe.plushie.dakimakuramod.common.lib.LibModInfo;
import net.minecraft.client.renderer.texture.IIconRegister;

public class ItemDakiDesign extends AbstractModItem {

    public ItemDakiDesign() {
        super("dakiDesign");
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister register) {
        itemIcon = register.registerIcon(LibModInfo.ID + ":" + "dakiDesign");
    }
}
