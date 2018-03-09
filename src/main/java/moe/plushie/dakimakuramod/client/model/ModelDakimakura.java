package moe.plushie.dakimakuramod.client.model;

import org.lwjgl.opengl.GL11;

import moe.plushie.dakimakuramod.DakimakuraMod;
import moe.plushie.dakimakuramod.client.texture.DakiTexture;
import moe.plushie.dakimakuramod.client.texture.DakiTextureManagerClient;
import moe.plushie.dakimakuramod.common.dakimakura.Daki;
import moe.plushie.dakimakuramod.common.lib.LibModInfo;
import moe.plushie.dakimakuramod.proxies.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.CullFace;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.profiler.Profiler;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelDakimakura extends ModelBase {
    
    private static final ResourceLocation MODEL_LOCATION = new ResourceLocation(LibModInfo.ID, "models/bolster-new-uv.obj");
    private static final ResourceLocation TEXTURE_BLANK = new ResourceLocation(LibModInfo.ID, "textures/models/blank.png");
    
    private final ObjModel DAKIMAKURA_MODEL;
    private final DakiTextureManagerClient dakiTextureManager;
    private final Profiler profiler;
    private int modelList = -1;
    
    public ModelDakimakura(DakiTextureManagerClient dakiTextureManager) {
        DAKIMAKURA_MODEL = ObjModel.loadModel(MODEL_LOCATION); 
        this.dakiTextureManager = dakiTextureManager;
        profiler = Minecraft.getMinecraft().mcProfiler;
    }
    
    public void render(Daki daki) {
        profiler.startSection("texture");
        if (daki == null) {
            Minecraft.getMinecraft().renderEngine.bindTexture(TEXTURE_BLANK);
        } else {
            DakiTexture dakiTexture = ((ClientProxy)DakimakuraMod.getProxy()).getDakiTextureManager().getTextureForDaki(daki);
            if (dakiTexture.isLoaded()) {
                GlStateManager.bindTexture(dakiTexture.getGlTextureId());
            } else {
                Minecraft.getMinecraft().renderEngine.bindTexture(TEXTURE_BLANK);
            }
        }
        profiler.endStartSection("model");
        
        GlStateManager.resetColor();
        GlStateManager.pushMatrix();
        GL11.glPushAttrib(GL11.GL_POLYGON_BIT | GL11.GL_ENABLE_BIT | GL11.GL_LIGHTING_BIT);
        // Enable bit.
        GlStateManager.enableNormalize();
        
        // Polygon bit.
        GlStateManager.cullFace(CullFace.BACK);
        GlStateManager.enableCull();
        
        // Lighting bit.
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        GlStateManager.color(1F, 1F, 1F, 1F);
        GlStateManager.scale(0.55F, 0.55F, 0.55F);
        GlStateManager.translate(0, 0.35F, 0);
        GlStateManager.scale(-1, -1, 1);
        
        //State manager is shit. 
        GL11.glCullFace(GL11.GL_BACK);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_NORMALIZE);
        GL11.glEnable(GL11.GL_LIGHTING);
        
        //drawDebugRender();
        if (modelList == -1) {
            modelList = GLAllocation.generateDisplayLists(1);
            GL11.glNewList(modelList, GL11.GL_COMPILE);
            DAKIMAKURA_MODEL.render();
            GL11.glEndList();
        }
        GlStateManager.callList(modelList);
        GlStateManager.shadeModel(GL11.GL_FLAT);
        GlStateManager.popAttrib();
        GlStateManager.popMatrix();
        profiler.endSection();
    }
    
    private void drawDebugRender() {
        Tessellator tess = Tessellator.getInstance();
        VertexBuffer buff = tess.getBuffer();
        
        buff.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        
        //Front
        buff.pos(-0.5F, 1.5F, -0.2F);
        buff.tex(0, 0);
        buff.endVertex();
        
        buff.pos(0.5F, 1.5F, -0.2F);
        buff.tex(0.5F, 0);
        buff.endVertex();
        
        buff.pos(0.5F, -1.5F, -0.2F);
        buff.tex(0.5F, 1);
        buff.endVertex();
        
        buff.pos(-0.5F, -1.5F, -0.2F);
        buff.tex(0, 1);
        buff.endVertex();
        
        
        //Back
        buff.pos(-0.5F, -1.5F, 0);
        buff.tex(0.5F, 1);
        buff.endVertex();
        
        buff.pos(0.5F, -1.5F, 0);
        buff.tex(1, 1);
        buff.endVertex();
        
        
        buff.pos(0.5F, 1.5F, 0);
        buff.tex(1, 0);
        buff.endVertex();
        
        buff.pos(-0.5F, 1.5F, 0);
        buff.tex(0.5F, 0);
        buff.endVertex();
        
        tess.draw();
    }
}
