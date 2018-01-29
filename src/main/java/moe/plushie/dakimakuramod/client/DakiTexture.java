package moe.plushie.dakimakuramod.client;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import moe.plushie.dakimakuramod.DakimakuraMod;
import moe.plushie.dakimakuramod.common.dakimakura.Daki;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;

@SideOnly(Side.CLIENT)
public class DakiTexture extends AbstractTexture {

    private final Daki daki;
    
    public DakiTexture(Daki daki) {
        this.daki = daki;
    }
    
    public boolean isLoaded() {
        if (glTextureId == -1) {
            
            try {
                loadTexture(Minecraft.getMinecraft().getResourceManager());
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            return false;
        }
        
        return true;
    }
    
    @Override
    public void loadTexture(IResourceManager resourceManager) throws IOException {
        DakimakuraMod.logger.info("loading raw texture " + daki.toString());
        File dir = DakimakuraMod.dakimakuraManager.getPackFolder();
        dir = new File(dir, daki.getPackDirectoryName());
        dir = new File(dir, daki.getDakiDirectoryName());
        
        File fileFront = new File(dir, daki.getImageFront());
        File fileBack = new File(dir, daki.getImageBack());
        
        deleteGlTexture();
        InputStream inputstream = null;
        try {
            inputstream = new FileInputStream(fileFront);
            BufferedImage bufferedimageFront = ImageIO.read(inputstream);
            inputstream.close();
            
            inputstream = new FileInputStream(fileBack);
            BufferedImage bufferedimageBack = ImageIO.read(inputstream);
            inputstream.close();
            
            BufferedImage bufferedimageFull = new BufferedImage(bufferedimageFront.getWidth() + bufferedimageBack.getWidth(), Math.max(bufferedimageFront.getHeight(), bufferedimageBack.getHeight()), BufferedImage.TYPE_INT_RGB);
            
            for (int ix = 0; ix < bufferedimageFront.getWidth(); ix++) {
                for (int iy = 0; iy < bufferedimageFront.getHeight(); iy++) {
                    bufferedimageFull.setRGB(ix, iy, bufferedimageFront.getRGB(ix, iy));
                }
            }
            
            for (int ix = 0; ix < bufferedimageBack.getWidth(); ix++) {
                for (int iy = 0; iy < bufferedimageBack.getHeight(); iy++) {
                    bufferedimageFull.setRGB(ix + bufferedimageFront.getWidth(), iy, bufferedimageBack.getRGB(ix, iy));
                }
            }
            
            TextureUtil.uploadTextureImageAllocate(this.getGlTextureId(), bufferedimageFull, false, false);
        } finally {
            IOUtils.closeQuietly(inputstream);
        }
    }
}
