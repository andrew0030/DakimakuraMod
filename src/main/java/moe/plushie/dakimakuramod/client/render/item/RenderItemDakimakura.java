package moe.plushie.dakimakuramod.client.render.item;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import moe.plushie.dakimakuramod.client.model.ModelDakimakura;
import moe.plushie.dakimakuramod.common.dakimakura.Daki;
import moe.plushie.dakimakuramod.common.dakimakura.serialize.DakiNbtSerializer;
import moe.plushie.dakimakuramod.common.items.block.ItemBlockDakimakura;
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
        GL11.glPushMatrix();
        float scale = 0.0625F;
        boolean flipped = ItemBlockDakimakura.isFlipped(itemStack);
        if (renderType == ItemRenderType.INVENTORY) {
            GL11.glTranslated(0, -3.5F * scale, 0);
            GL11.glRotatef(180, 0, 1, 0);
            GL11.glScalef(1.1F, 1.1F, 1.1F);
            if (flipped) {
                GL11.glRotatef(180, 0, 1, 0);
            }
        }
        if (renderType == ItemRenderType.EQUIPPED_FIRST_PERSON) {
            GL11.glRotatef(90, 0, 1, 0);
            GL11.glTranslated(0, 12 * scale, 0);
            GL11.glTranslated(-8 * scale, 4 * scale, -3 * scale);
            if (flipped) {
                GL11.glRotatef(180, 0, 1, 0);
            }
        }
        if (renderType == ItemRenderType.EQUIPPED) {
            GL11.glScalef(2.8F, 2.8F, 2.8F);
            GL11.glTranslated(3 * scale, 0, 4 * scale);
            GL11.glRotatef(180, 0, 1, 0);
            if (flipped) {
                GL11.glRotatef(180, 0, 1, 0);
            }
        }
        if (renderType == ItemRenderType.ENTITY) {
            GL11.glTranslated(0, 32 * scale, 0);
            GL11.glScalef(4F, 4F, 4F);
        }

        modelDakimakura.render(daki, 0);
        GL11.glPopMatrix();
    }
}
