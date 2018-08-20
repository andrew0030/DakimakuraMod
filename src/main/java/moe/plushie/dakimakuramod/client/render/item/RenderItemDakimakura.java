package moe.plushie.dakimakuramod.client.render.item;

import moe.plushie.dakimakuramod.client.model.ModelDakimakura;
import moe.plushie.dakimakuramod.common.dakimakura.Daki;
import moe.plushie.dakimakuramod.common.dakimakura.serialize.DakiNbtSerializer;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.ItemStack;

public class RenderItemDakimakura extends TileEntityItemStackRenderer {
    
    private final ModelDakimakura modelDakimakura;
    
    public RenderItemDakimakura(ModelDakimakura modelDakimakura) {
        this.modelDakimakura = modelDakimakura;
    }
    
    @Override
    public void renderByItem(ItemStack itemStackIn) {
        Daki daki = DakiNbtSerializer.deserialize(itemStackIn.getTagCompound());
        modelDakimakura.render(daki, 0);
    }

}
