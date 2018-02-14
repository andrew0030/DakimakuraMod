package moe.plushie.dakimakuramod.client.model;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import moe.plushie.dakimakuramod.DakimakuraMod;
import moe.plushie.dakimakuramod.client.texture.DakiTexture;
import moe.plushie.dakimakuramod.client.texture.DakiTextureManagerClient;
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
    private final DakiTextureManagerClient dakiTextureManager;
    private int modelList = -1;
    
    public ModelDakimakura(DakiTextureManagerClient dakiTextureManager) {
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
        GL11.glPushAttrib(GL11.GL_POLYGON_BIT | GL11.GL_ENABLE_BIT);
        GL11.glCullFace(GL11.GL_FRONT);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_NORMALIZE);
        
        float scale = 1F / 16F;
        GL11.glColor4f(1, 1, 1, 1);
        GL11.glScalef(-1, 1, 1);
        GL11.glScalef(0.55F, 0.55F, 0.55F);
        GL11.glTranslatef(0, 0.35F, 0);
        if (modelList == -1) {
            modelList = GLAllocation.generateDisplayLists(1);
            GL11.glNewList(modelList, GL11.GL_COMPILE);
            DAKIMAKURA_MODEL.renderAll();
            GL11.glEndList();
        }
        GL11.glCallList(modelList);
        GL11.glPopAttrib();
        GL11.glPopMatrix();
    }
}
