package moe.plushie.dakimakuramod.common.dakimakura;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
    
    public boolean load(Daki daki) {
        DakimakuraMod.getLogger().info("Loading daki from disk: " + daki);
        boolean valid = true;
        File dir = DakimakuraMod.getProxy().getDakimakuraManager().getPackFolder();
        
        dir = new File(dir, daki.getPackDirectoryName());
        dir = new File(dir, daki.getDakiDirectoryName());
        
        File fileFront = new File(dir, daki.getImageFront());
        File fileBack = new File(dir, daki.getImageBack());
        
        InputStream inputstream = null;
        try {
            inputstream = new FileInputStream(fileFront);
            textureFront = IOUtils.toByteArray(inputstream);
            inputstream.close();
            
            inputstream = new FileInputStream(fileBack);
            textureBack = IOUtils.toByteArray(inputstream);
            inputstream.close();
        } catch (IOException e) {
            e.printStackTrace();
            valid = false;
        } finally {
            IOUtils.closeQuietly(inputstream);
        }
        return valid;
    }
    
    public byte[] getTextureFront() {
        return textureFront;
    }
    
    public byte[] getTextureBack() {
        return textureBack;
    }
}
