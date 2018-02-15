package moe.plushie.dakimakuramod.client.texture;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import moe.plushie.dakimakuramod.common.dakimakura.Daki;
import moe.plushie.dakimakuramod.common.dakimakura.DakiImageData;

@SideOnly(Side.CLIENT)
public class DakiTextureManagerClient {
    
    private final HashMap<Daki, DakiTexture> textureMap;
    private final AtomicInteger textureRequests;
    
    public DakiTextureManagerClient() {
        textureMap = new HashMap<Daki, DakiTexture>();
        textureRequests = new AtomicInteger(0);
    }
    
    public DakiTexture getTextureForDaki(Daki daki) {
        DakiTexture dakiTexture  = null;
        synchronized (textureMap) {
            dakiTexture = textureMap.get(daki);
            if (dakiTexture == null) {
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
    
    public AtomicInteger getTextureRequests() {
        return textureRequests;
    }
    
    public void serverSentTextures(Daki daki, DakiImageData imageData) {
        textureRequests.decrementAndGet();
        DakiTexture dakiTexture = textureMap.get(daki);
        if (dakiTexture != null) {
            dakiTexture.setImage(imageData);
        }
    }
}
