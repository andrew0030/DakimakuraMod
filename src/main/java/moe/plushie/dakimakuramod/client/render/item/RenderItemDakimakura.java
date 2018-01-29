package moe.plushie.dakimakuramod.client.render.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import moe.plushie.dakimakuramod.DakimakuraMod;
import moe.plushie.dakimakuramod.client.model.ModelDakimakura;
import moe.plushie.dakimakuramod.common.dakimakura.Daki;
import moe.plushie.dakimakuramod.common.items.block.ItemBlockDakimakura;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.common.util.Constants.NBT;

@SideOnly(Side.CLIENT)
public class RenderItemDakimakura implements IItemRenderer {

    private final ModelDakimakura modelDakimakura;
    
    public RenderItemDakimakura(ModelDakimakura modelDakimakura) {
        this.modelDakimakura = modelDakimakura;
    }
    
    @Override
    public boolean handleRenderType(ItemStack itemStack, ItemRenderType renderType) {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType renderType, ItemStack itemStack, ItemRendererHelper rendererHelper) {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType renderType, ItemStack itemStack, Object... data) {
        Daki daki = null;
        if (itemStack.hasTagCompound()) {
            NBTTagCompound compound = itemStack.getTagCompound();
            if (compound.hasKey(ItemBlockDakimakura.TAG_DAKI_PACK_NAME, NBT.TAG_STRING) & compound.hasKey(ItemBlockDakimakura.TAG_DAKI_DIR_NAME, NBT.TAG_STRING)) {
                String packName = compound.getString(ItemBlockDakimakura.TAG_DAKI_PACK_NAME);
                String dakiName = compound.getString(ItemBlockDakimakura.TAG_DAKI_DIR_NAME);
                daki = DakimakuraMod.dakimakuraManager.getDakiFromMap(packName, dakiName);
            }
        }
        modelDakimakura.render(daki);
    }
}
