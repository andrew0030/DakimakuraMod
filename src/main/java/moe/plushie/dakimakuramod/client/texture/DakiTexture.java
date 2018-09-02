package moe.plushie.dakimakuramod.client.texture;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import moe.plushie.dakimakuramod.DakimakuraMod;
import moe.plushie.dakimakuramod.common.dakimakura.Daki;
import moe.plushie.dakimakuramod.common.network.PacketHandler;
import moe.plushie.dakimakuramod.common.network.message.client.MessageClientRequestTextures;
import moe.plushie.dakimakuramod.proxies.ClientProxy;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;

@SideOnly(Side.CLIENT)
public class DakiTexture extends AbstractTexture {

    private static long lastLoad;
    private boolean requested = false;
    
    private final Daki daki;
    
    private BufferedImage bufferedImageFull;
    
    public DakiTexture(Daki daki) {
        this.daki = daki;
    }
    
    public boolean isLoaded() {
        if (glTextureId == -1) {
            if (lastLoad + 25 < System.currentTimeMillis()) {
                if (load()) {
                    lastLoad = System.currentTimeMillis();
                } else {
                    if (!requested) {
                        DakiTextureManagerClient textureManager = ((ClientProxy)DakimakuraMod.getProxy()).getDakiTextureManager();
                        int requests = textureManager.getTextureRequests().get();
                        if (requests < 2) {
                            textureManager.getTextureRequests().incrementAndGet();
                            if (daki != null) {
                                requested = true;
                                PacketHandler.NETWORK_WRAPPER.sendToServer(new MessageClientRequestTextures(daki));
                            }
                        }
                    } else {
                        if (bufferedImageFull != null) {
                            load();
                        }
                    }
                }
            }
            return false;
        }
        return true;
    }
    
    public void setBufferedImageFull(BufferedImage bufferedImageFull) {
        this.bufferedImageFull = bufferedImageFull;
    }
    
    @Override
    protected void finalize() throws Throwable {
        deleteGlTexture();
        super.finalize();
    }
    
    private boolean load() {
        if (bufferedImageFull == null) {
            return false;
        }
        deleteGlTexture();
        try {
            int sizeW = bufferedImageFull.getWidth();
            int sizeH = bufferedImageFull.getHeight();
            //DakimakuraMod.getLogger().info(String.format("Uploading texture of size %d*%d to GPU for daki %s", sizeW, sizeH, daki.toString()));
            TextureUtil.uploadTextureImageAllocate(this.getGlTextureId(), bufferedImageFull, false, false);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    private InputStream getMissingTexture() {
        return DakiTexture.class.getClassLoader().getResourceAsStream("assets/dakimakuramod/textures/models/missing.png");
    }
    
    @Override
    public void loadTexture(IResourceManager resourceManager) throws IOException {
    }
}
