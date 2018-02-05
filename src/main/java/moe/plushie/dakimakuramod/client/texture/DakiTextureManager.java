package moe.plushie.dakimakuramod.client.texture;

import java.util.HashMap;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import moe.plushie.dakimakuramod.DakimakuraMod;
import moe.plushie.dakimakuramod.common.dakimakura.Daki;

@SideOnly(Side.CLIENT)
public class DakiTextureManager {
    
    private final HashMap<Daki, DakiTexture> textureMap;
    
    public DakiTextureManager() {
        textureMap = new HashMap<Daki, DakiTexture>();
    }
    
    public DakiTexture getTextureForDaki(Daki daki) {
        DakiTexture dakiTexture  = textureMap.get(daki);
        if (dakiTexture == null) {
            DakimakuraMod.getLogger().info("Creating texture for: " + daki.toString());
            dakiTexture = new DakiTexture(daki);
            textureMap.put(daki, dakiTexture);
        }
        return dakiTexture;
    }
}
