package moe.plushie.dakimakuramod.client.render.entity;

import org.lwjgl.opengl.GL11;

import moe.plushie.dakimakuramod.client.model.ModelDakimakura;
import moe.plushie.dakimakuramod.common.entities.EntityDakimakura;
import moe.plushie.dakimakuramod.common.lib.LibModInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderEntityDakimakura extends Render<EntityDakimakura> {
    
    private final ModelDakimakura modelDakimakura;
    
    public RenderEntityDakimakura(RenderManager renderManager, ModelDakimakura modelDakimakura) {
        super(renderManager);
        this.modelDakimakura = modelDakimakura;
    }

    @Override
    public void doRender(EntityDakimakura entity, double x, double y, double z, float entityYaw, float partialTicks) {
        Minecraft mc = Minecraft.getMinecraft();
        mc.mcProfiler.startSection("dakimakura");
        float scale = 0.0625F;
        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z);
        GL11.glTranslatef(8F * scale, -5F * scale, 8F * scale);
        EnumFacing rot = ((EntityDakimakura)entity).getRotation();
        if (rot == EnumFacing.WEST) {
            GL11.glRotatef(-90, 0, 1, 0);
        }
        if (rot == EnumFacing.NORTH) {
            GL11.glRotatef(180, 0, 1, 0);
        }
        if (rot == EnumFacing.EAST) {
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
        
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
    
    @Override
    public void doRenderShadowAndFire(Entity entityIn, double x, double y, double z, float yaw, float partialTicks) {
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityDakimakura entity) {
        return new ResourceLocation(LibModInfo.ID, "models/bolster-new-uv.obj");
    }
}
