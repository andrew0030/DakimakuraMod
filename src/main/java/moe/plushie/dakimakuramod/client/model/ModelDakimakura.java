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
import net.minecraft.profiler.Profiler;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class ModelDakimakura extends ModelBase {
    
    private static final String MODEL_PATH = "models/dakimakura.obj";
    private static final String MODEL_PATH_LOD = "models/dakimakura-lod-%d.obj";
    
    private static final ResourceLocation TEXTURE_BLANK = new ResourceLocation(LibModInfo.ID, "textures/models/blank.png");
    
    private final ObjModel DAKIMAKURA_MODEL;
    private final ObjModel[] DAKIMAKURA_MODEL_LODS;
    
    private final DakiTextureManagerClient dakiTextureManager;
    private final Profiler profiler;
    
    public ModelDakimakura(DakiTextureManagerClient dakiTextureManager) {
        DAKIMAKURA_MODEL = ObjModel.loadModel(new ResourceLocation(LibModInfo.ID, MODEL_PATH));
        DAKIMAKURA_MODEL_LODS = new ObjModel[4];
        for (int i = 0; i < DAKIMAKURA_MODEL_LODS.length; i++) {
            DAKIMAKURA_MODEL_LODS[i] = ObjModel.loadModel(new ResourceLocation(LibModInfo.ID, String.format(MODEL_PATH_LOD, i + 1)));
        }
        
        this.dakiTextureManager = dakiTextureManager;
        profiler = Minecraft.getMinecraft().mcProfiler;
    }
    
    public void render(Daki daki, double x, double y, double z) {
        double distance = Minecraft.getMinecraft().thePlayer.getDistance(x, y, z);
        render(daki, distance);
    }
    
    public void render(Daki daki, double distance) {
        int lod = MathHelper.floor_double(distance / 16D);
        render(daki, lod);
    }
    
    public void render(Daki daki, int lod) {
        lod = MathHelper.clamp_int(lod, 0, 4);
        profiler.startSection("texture");
        if (daki == null) {
            Minecraft.getMinecraft().renderEngine.bindTexture(TEXTURE_BLANK);
        } else {
            DakiTexture dakiTexture = ((ClientProxy) DakimakuraMod.getProxy()).getDakiTextureManager().getTextureForDaki(daki);
            if (dakiTexture.isLoaded()) {
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, dakiTexture.getGlTextureId());
            } else {
                Minecraft.getMinecraft().renderEngine.bindTexture(TEXTURE_BLANK);
            }
        }
        profiler.endStartSection("model");
        GL11.glPushMatrix();
        GL11.glPushAttrib(GL11.GL_POLYGON_BIT | GL11.GL_ENABLE_BIT | GL11.GL_LIGHTING_BIT);
        // Enable bit.
        GL11.glEnable(GL11.GL_NORMALIZE);
        
        // Polygon bit.
        GL11.glCullFace(GL11.GL_BACK);
        GL11.glEnable(GL11.GL_CULL_FACE);
        
        // Lighting bit.
        GL11.glShadeModel(GL11.GL_SMOOTH);
        
        GL11.glColor4f(1, 1, 1, 1);
        GL11.glScalef(0.55F, 0.55F, 0.55F);
        GL11.glTranslatef(0, 0.35F, 0);
        GL11.glScalef(-1, -1, 1);
        
        switch (lod) {
        case 0:
            DAKIMAKURA_MODEL.render();
            break;
        case 1:
            DAKIMAKURA_MODEL_LODS[0].render();
            break;
        case 2:
            DAKIMAKURA_MODEL_LODS[1].render();
            break;
        case 3:
            DAKIMAKURA_MODEL_LODS[2].render();
            break;
        case 4:
            DAKIMAKURA_MODEL_LODS[3].render();
            break;
        default:
            break;
        }
        
        GL11.glPopAttrib();
        GL11.glPopMatrix();
        profiler.endSection();
    }
}
