package moe.plushie.dakimakuramod.client.render.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import moe.plushie.dakimakuramod.client.model.ModelDakimakura;
import moe.plushie.dakimakuramod.common.dakimakura.Daki;
import moe.plushie.dakimakuramod.common.dakimakura.serialize.DakiNbtSerializer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

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
        Daki daki = DakiNbtSerializer.deserialize(itemStack.getTagCompound());
        modelDakimakura.render(daki);
    }
}
