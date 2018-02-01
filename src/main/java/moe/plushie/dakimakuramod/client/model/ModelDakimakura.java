package moe.plushie.dakimakuramod.client.model;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import moe.plushie.dakimakuramod.DakimakuraMod;
import moe.plushie.dakimakuramod.client.texture.DakiTexture;
import moe.plushie.dakimakuramod.client.texture.DakiTextureManager;
import moe.plushie.dakimakuramod.common.dakimakura.Daki;
import moe.plushie.dakimakuramod.common.lib.LibModInfo;
import moe.plushie.dakimakuramod.proxies.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

@SideOnly(Side.CLIENT)
public class ModelDakimakura extends ModelBase {
    
    private static final ResourceLocation MODEL_LOCATION = new ResourceLocation(LibModInfo.ID, "models/daki-new-uv.obj");
    private static final ResourceLocation TEXTURE_BLANK = new ResourceLocation(LibModInfo.ID, "textures/models/blank.png");
    
    private final IModelCustom DAKIMAKURA_MODEL;
    private final DakiTextureManager dakiTextureManager;
    
    public ModelDakimakura(DakiTextureManager dakiTextureManager) {
        DAKIMAKURA_MODEL = AdvancedModelLoader.loadModel(MODEL_LOCATION);
        this.dakiTextureManager = dakiTextureManager;
    }
    
    public void render(Daki daki) {
        if (daki == null) {
            Minecraft.getMinecraft().renderEngine.bindTexture(TEXTURE_BLANK);
        } else {
            DakiTexture dakiTexture = ((ClientProxy)DakimakuraMod.getProxy()).getDakiTextureManager().getTextureForDaki(daki);
            if (dakiTexture.isLoaded()) {
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, dakiTexture.getGlTextureId());
            } else {
                Minecraft.getMinecraft().renderEngine.bindTexture(TEXTURE_BLANK);
            }
        }
        
        GL11.glPushMatrix();
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        //GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_CULL_FACE);
        Tessellator tess = Tessellator.instance;
        float scale = 1F / 16F;
        
        GL11.glColor4f(1, 1, 1, 1);
        
        GL11.glEnable(GL11.GL_NORMALIZE);
        
        GL11.glScalef(0.55F, 0.55F, 0.55F);
        GL11.glTranslatef(0, 0.35F, 0);
        DAKIMAKURA_MODEL.renderAll();
        /*
        GL11.glScalef(0.08F, 0.08F, 0.08F);
        GL11.glTranslatef(0, -8F, 0);
        DAKIMAKURA_MODEL.renderAll();
        */
        GL11.glPopAttrib();
        GL11.glPopMatrix();
    }
}
