package moe.plushie.dakimakuramod.client.texture;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import moe.plushie.dakimakuramod.DakimakuraMod;
import moe.plushie.dakimakuramod.common.config.ConfigHandler;
import moe.plushie.dakimakuramod.common.dakimakura.Daki;
import moe.plushie.dakimakuramod.common.dakimakura.DakiImageData;
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
    
    private DakiImageData imageData;
    
    public DakiTexture(Daki daki) {
        this.daki = daki;
    }
    
    public boolean isLoaded() {
        if (glTextureId == -1) {
            if (lastLoad + 200 < System.currentTimeMillis()) {
                if (load()) {
                    lastLoad = System.currentTimeMillis();
                } else {
                    if (!requested) {
                        if (daki != null) {
                            requested = true;
                            PacketHandler.NETWORK_WRAPPER.sendToServer(new MessageClientRequestTextures(daki));
                        }
                    }
                }
            }
            return false;
        }
        return true;
    }
    
    public void setImage(DakiImageData imageData) {
        this.imageData = imageData;
    }  
    
    @Override
    protected void finalize() throws Throwable {
        deleteGlTexture();
        super.finalize();
    }
    
    private boolean load() {
        if (imageData == null) {
            return false;
        }
        //DakimakuraMod.getLogger().info("loading raw texture " + daki.toString());
        
        deleteGlTexture();
        InputStream inputstream = null;
        try {
            if (imageData.getTextureFront() != null) {
                inputstream = new ByteArrayInputStream(imageData.getTextureFront());
            } else {
                inputstream = getMissingTexture();
            }
            BufferedImage bufferedimageFront = ImageIO.read(inputstream);
            inputstream.close();
            
            if (imageData.getTextureBack() != null) {
                inputstream = new ByteArrayInputStream(imageData.getTextureBack());
            } else {
                inputstream = getMissingTexture();
            }
            BufferedImage bufferedimageBack = ImageIO.read(inputstream);
            inputstream.close();
            
            int heightF = bufferedimageFront.getHeight();
            int heightB = bufferedimageBack.getHeight();
            
            int maxTexture = Math.max(heightF, heightB);
            int textureSize = getMaxTextureSize();
            textureSize = Math.min(textureSize, getNextPowerOf2(maxTexture));
            
            bufferedimageFront = resize(bufferedimageFront, textureSize / 2, textureSize);
            bufferedimageBack = resize(bufferedimageBack, textureSize / 2, textureSize);
            
            BufferedImage bufferedimageFull = new BufferedImage(textureSize, textureSize, BufferedImage.TYPE_INT_RGB);
            
            Graphics2D g2d = bufferedimageFull.createGraphics();
            g2d.drawImage(bufferedimageFront, 0, 0, null);
            g2d.drawImage(bufferedimageBack, textureSize / 2, 0, null);
            g2d.dispose();
            
            imageData = null;
            
            DakimakuraMod.getLogger().info("uploading texture " + textureSize + " - " + daki.toString());
            TextureUtil.uploadTextureImageAllocate(this.getGlTextureId(), bufferedimageFull, false, false);
        } catch (Exception e) {
            e.printStackTrace();
            IOUtils.closeQuietly(inputstream);
            return false;
        } finally {
            IOUtils.closeQuietly(inputstream);
        }
        return true;
    }
    
    private InputStream getMissingTexture() {
        return DakiTexture.class.getClassLoader().getResourceAsStream("assets/dakimakuramod/textures/models/missing.png");
    }
    
    @Override
    public void loadTexture(IResourceManager resourceManager) throws IOException {

    }
    
    private int getNextPowerOf2(int value) {
        return (int) Math.pow(2, 32 - Integer.numberOfLeadingZeros(value - 1));
    }
    
    private int getMaxTextureSize() {
        int maxGpuSize = ((ClientProxy)DakimakuraMod.getProxy()).getMaxGpuTextureSize();
        int maxConfigSize = ConfigHandler.textureMaxSize;
        return Math.min(maxGpuSize, maxConfigSize);
    }
    
    public static BufferedImage resize(BufferedImage img, int newW, int newH) { 
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_RGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }
}
