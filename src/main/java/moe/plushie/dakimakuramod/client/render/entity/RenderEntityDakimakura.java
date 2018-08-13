package moe.plushie.dakimakuramod.client.render.entity;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import moe.plushie.dakimakuramod.client.model.ModelDakimakura;
import moe.plushie.dakimakuramod.common.entities.EntityDakimakura;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;

@SideOnly(Side.CLIENT)
public class RenderEntityDakimakura extends Render {

    private final ModelDakimakura modelDakimakura;
    
    public RenderEntityDakimakura(ModelDakimakura modelDakimakura) {
        this.modelDakimakura = modelDakimakura;
    }
    
    @Override
    public void doRender(Entity entity, double x, double y, double z, float p_76986_8_, float p_76986_9_) {
        Minecraft mc = Minecraft.getMinecraft();
        mc.mcProfiler.startSection("dakimakura");
        float scale = 0.0625F;
        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z);
        GL11.glTranslatef(8F * scale, -5F * scale, 8F * scale);
        ForgeDirection rot = ((EntityDakimakura)entity).getRotation();
        if (rot == ForgeDirection.WEST) {
            GL11.glRotatef(-90, 0, 1, 0);
        }
        if (rot == ForgeDirection.NORTH) {
            GL11.glRotatef(180, 0, 1, 0);
        }
        if (rot == ForgeDirection.EAST) {
            GL11.glRotatef(90, 0, 1, 0);
        }
        GL11.glTranslatef(0F * scale, 0F * scale, 4F * scale);
        GL11.glRotatef(90, 1, 0, 0);
        if (((EntityDakimakura)entity).isFlipped()) {
            GL11.glRotatef(180, 0, 1, 0);
        }
        modelDakimakura.render(((EntityDakimakura)entity).getDaki(), x, y, z);
        GL11.glPopMatrix();
        Minecraft.getMinecraft().mcProfiler.endSection();
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return null;
    }
}
