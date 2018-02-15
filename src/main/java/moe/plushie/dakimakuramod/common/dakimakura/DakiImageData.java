package moe.plushie.dakimakuramod.common.dakimakura;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

import moe.plushie.dakimakuramod.DakimakuraMod;

public class DakiImageData {
    
    private byte[] textureFront;
    private byte[] textureBack;
    
    public DakiImageData(Daki daki) {
        load(daki);
    }
    
    public DakiImageData(byte[] textureFront, byte[] textureBack) {
        this.textureFront = textureFront;
        this.textureBack = textureBack;
    }
    
    public void load(Daki daki) {
        DakimakuraMod.getLogger().info("Loading daki from disk: " + daki);
        File dir = DakimakuraMod.getProxy().getDakimakuraManager().getPackFolder();
        
        dir = new File(dir, daki.getPackDirectoryName());
        dir = new File(dir, daki.getDakiDirectoryName());
        
        File fileFront = new File(dir, daki.getImageFront());
        File fileBack = new File(dir, daki.getImageBack());
        
        InputStream inputstream = null;
        try {
            if (fileFront.exists()) {
                inputstream = new FileInputStream(fileFront);
                textureFront = IOUtils.toByteArray(inputstream);
                inputstream.close();
            }
            if (fileBack.exists()) {
                inputstream = new FileInputStream(fileBack);
                textureBack = IOUtils.toByteArray(inputstream);
                inputstream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(inputstream);
        }
    }
    
    public byte[] getTextureFront() {
        return textureFront;
    }
    
    public byte[] getTextureBack() {
        return textureBack;
    }
}
