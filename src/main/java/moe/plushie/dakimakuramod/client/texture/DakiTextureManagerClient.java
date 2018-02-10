package moe.plushie.dakimakuramod.client.texture;

import java.util.HashMap;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import moe.plushie.dakimakuramod.common.dakimakura.Daki;
import moe.plushie.dakimakuramod.common.dakimakura.DakiTextureManagerCommon.DakiBufferedImages;

@SideOnly(Side.CLIENT)
public class DakiTextureManagerClient {
    
    private final HashMap<Daki, DakiTexture> textureMap;
    
    public DakiTextureManagerClient() {
        textureMap = new HashMap<Daki, DakiTexture>();
    }
    
    public DakiTexture getTextureForDaki(Daki daki) {
        DakiTexture dakiTexture  = null;
        synchronized (textureMap) {
            dakiTexture = textureMap.get(daki);
            if (dakiTexture == null) {
                //DakimakuraMod.getLogger().info("Creating texture for: " + daki.toString());
                dakiTexture = new DakiTexture(daki);
                textureMap.put(daki, dakiTexture);
            }
        }
        return dakiTexture;
    }
    
    public void reloadTextures() {
        synchronized (textureMap) {
            deleteTextures();
        }
    }
    
    private void deleteTextures() {
        Daki[] keys = (Daki[]) textureMap.keySet().toArray(new Daki[textureMap.size()]);
        for (int i = 0; i < keys.length; i++) {
            DakiTexture dakiTexture = textureMap.remove(keys[i]);
            dakiTexture.deleteGlTexture();
        }
    }
    
    public void serverSentTextures(Daki daki, DakiBufferedImages bufferedImages) {
        DakiTexture dakiTexture = textureMap.get(daki);
        if (dakiTexture != null) {
            dakiTexture.setImage(bufferedImages);
        }
    }
}
